package metaheuristic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import metaheuristic.models.MetaResults;
import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;
import util.CSVTableWriter;
import util.optimizers.Optimizer;

/**
 * @author César Valdés
 */
public class MetaSolver {

	private final ArrayList<Future<List<MetaSolution>>> futures = new ArrayList<>();
	private final ArrayList<MetaSolution> allSolutions = new ArrayList<>();
	private final int numBranches;
	private final int branchLeaves;
	private final int parts;
	private Class<? extends Object> evaluationClass;
	private Long totalConcurrentTime;
	private MetaResults results;
	private ArrayList<MetaSearch> metaSearches = new ArrayList<>();
	private CSVTableWriter tableWriter;
	private static final String CSV_SAVES = "MetaSolutions/";

	public MetaSolver(int numBranches, int numLeaves, int parts, Class<? extends Object> evaluationClass,
			String writerPath) {

		this.numBranches = numBranches;
		this.branchLeaves = numLeaves;
		this.parts = parts;
		this.evaluationClass = evaluationClass;
		CSVTableWriter writer = initTableWriter(writerPath);
		this.setTableWriter(writer);
		search();
	}

	public MetaSolver(int numBranches, int numLeaves, int parts, Class<? extends Object> evaluationClass,
			CSVTableWriter writer) {

		this.numBranches = numBranches;
		this.branchLeaves = numLeaves;
		this.parts = parts;
		this.evaluationClass = evaluationClass;
		this.setTableWriter(writer);
		search();
	}

	private void search() {

		futures.clear();
		allSolutions.clear();
		ExecutorService es = Executors.newCachedThreadPool();
		try {
			totalConcurrentTime = System.currentTimeMillis();
			for (int i = 1; i <= numBranches; i++) {
				int seed = i*numBranches;
				Random r = new Random(seed);
				Constructor<?> cons = evaluationClass.getConstructor(int.class, Random.class);
				Optimizer eo = (Optimizer) cons.newInstance(parts, r);
				metaSearches.add(new MetaSearch(branchLeaves, eo, r));
				futures.add(es.submit(metaSearches.get(i-1)));
			}
			for (Future<List<MetaSolution>> f : futures) {
				List<MetaSolution> s = f.get();
				allSolutions.addAll(s);
			}
			totalConcurrentTime = System.currentTimeMillis() - totalConcurrentTime;
		} catch (InterruptedException | ExecutionException | NoSuchMethodException | SecurityException
				| InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException ex) {
			Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			es.shutdown();

		}

	}

	public MetaResults getAndSaveResults() {
		if (results == null) {
			MetaSolution bestSolution = allSolutions.get(0);
			Long totalSecuentialTime = 0L;
			Double avgErrorAux = 0.0;

			for (MetaSolution sol : allSolutions) {
				if (sol.getEvaluation() < bestSolution.getEvaluation()) {
					bestSolution = sol;
				}
				totalSecuentialTime += sol.getExecutionTime();
				avgErrorAux += sol.getEvaluation();
			}
			Long avgTime = totalSecuentialTime / allSolutions.size();
			Double avgError = avgErrorAux / allSolutions.size();
			results = new MetaResults(bestSolution, totalSecuentialTime, totalConcurrentTime, avgTime, avgError);
		}

		try {
			this.writeRow();
		} catch (Exception ex) {
			Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
		}
		return results;
	}

	public void setTableWriter(CSVTableWriter tableWriter) {
		this.tableWriter = tableWriter;
	}

	public void closeTableWriter() {
		tableWriter.close();

	}

	private void writeRow() {
		try {
			ArrayList<String> nextRow = new ArrayList<>();
			
			String evalName = evaluationClass.getName();
			Double avgMae = results.getAvgError();
			
			MetaSolution bestSolution = results.getBestSolution();
			Double minMAe = bestSolution.getEvaluation();
			Double epsilon = bestSolution.getEpsilon();
			
			
			Collection<MetaVariable> metaVariables = bestSolution.getProbVariables().values();
			List<String> alphas = metaVariables.stream().map(MetaVariable::getAlfa).map(MetaSolver::variableFormat).collect(Collectors.toList());
			List<String> betas = metaVariables.stream().map(MetaVariable::getBeta).map(MetaSolver::variableFormat).collect(Collectors.toList());
			
			nextRow.add(evalName.substring(evalName.lastIndexOf('.') + 1, evalName.indexOf('E')));
			nextRow.add(String.valueOf(numBranches));
			nextRow.add(String.valueOf(branchLeaves));
			nextRow.add(String.valueOf(parts));
			nextRow.add(minMAe.toString().replace('.', ','));
			nextRow.add(String.valueOf(results.getTotalConcurrentTime()));
			nextRow.add(String.valueOf(results.getTotalSecuentialTime()));
			nextRow.add(avgMae.toString().replace('.', ','));
			nextRow.add(String.valueOf(results.getAvgTime()));
			nextRow.add(String.valueOf(epsilon).replace('.', ','));
			nextRow.add("\"" + String.valueOf(alphas).replace('.', ',').replace(" ", "\n") + "\"");
			nextRow.add("\"" + String.valueOf(betas).replace('.', ',').replace(" ", "\n") + "\"");

			tableWriter.printRow(nextRow);

		} catch (Exception ex) {
			Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	private static String variableFormat(Double d) {
		
		DecimalFormat errorFormat = new DecimalFormat("0.0000");
		return errorFormat.format(d);
	}

	public static CSVTableWriter initTableWriter(String path) {
		String realpath = CSV_SAVES;
		realpath += path;
		ArrayList<String> tableHeaders = new ArrayList<>();
		tableHeaders.addAll(Arrays.asList("Eval", "Branches", "Leaves", "Parts", "min MAE", "real Time", "sum Time",
				"avg Mae", "avg Time", "epsilon", "alphas", "betas"));
		CSVTableWriter tw = null;
		try {
			tw = new CSVTableWriter(realpath, tableHeaders);
		} catch (Exception ex) {
			Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
		}

		return tw;
	}

}
