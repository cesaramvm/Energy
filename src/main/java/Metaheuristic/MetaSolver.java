/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

// <editor-fold desc="">
// </editor-fold>
import Models.MetaResults;
import Models.Problem;
import Models.Solution;
import Util.Optimizers.Optimizer;
import Util.Writers.CSVTableWriter;

/**
 *
 * @author Cesar
 */
public class MetaSolver {

    private final ArrayList<Future<List<Solution>>> futures = new ArrayList<>();
    private final ArrayList<Solution> soluciones = new ArrayList<>();
    private final int numBranches;
    private final int branchLeaves;
    private final int parts;
    private Class<? extends Object> evaluationClass;
    private Long totalConcurrentTime;
    private MetaResults results;

// <editor-fold desc="Constructor">
    public MetaSolver(int numBranches, int numLeaves, int parts, Class<? extends Object> evaluationClass) {
    	
        this.numBranches = numBranches;
        this.branchLeaves = numLeaves;
        this.parts = parts;
        this.evaluationClass = evaluationClass;
    }

    public void search(Problem problem) {

        futures.clear();
        soluciones.clear();
        ExecutorService es = Executors.newCachedThreadPool();
        try {
            totalConcurrentTime = System.currentTimeMillis();
            for (int i = 0; i < numBranches; i++) {
                int seed = i + 5;
                Random r = new Random(seed);
                Constructor<?> cons = evaluationClass.getConstructor(int.class, Problem.class, Random.class);
                Optimizer eo = (Optimizer) cons.newInstance(parts, problem, r);
                futures.add(es.submit(new MetaSearch(problem, branchLeaves, eo, r)));
            }
            for (Future<List<Solution>> f : futures) {
                List<Solution> s = f.get();
                soluciones.addAll(s);
            }
            totalConcurrentTime = System.currentTimeMillis() - totalConcurrentTime;
        } catch (InterruptedException | ExecutionException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            es.shutdown();

        }

    }

    public MetaResults getResults() {
        if (soluciones.isEmpty()) {
            throw new Error("Search still not done");
        }
        if (results == null) {
            Solution bestSolution = soluciones.get(0);
            Long totalSecuentialTime = 0L;
            Double avgErrorAux = 0.0;

            for (Solution sol : soluciones) {
                if (sol.getEvaluation() < bestSolution.getEvaluation()) {
                    bestSolution = sol;
                }
                totalSecuentialTime += sol.getExecutionTime();
                avgErrorAux += sol.getEvaluation();
            }
            Long avgTime = totalSecuentialTime / soluciones.size();
            Double avgError = avgErrorAux / soluciones.size();
            results = new MetaResults(bestSolution, totalSecuentialTime, totalConcurrentTime, avgTime, avgError);
        }

        return results;
    }
//
//    private Solution findBestSolution() {
//        if (soluciones.isEmpty()) {
//            this.search();
//        }
//        return Collections.min(soluciones);
//    }

    public void writeRow(CSVTableWriter tw) {
        if (results == null) {
            getResults();
        }
        try {
            ArrayList<String> nextRow = new ArrayList<>();
            String evalName = evaluationClass.getName();
            Double minMAe = results.getBestSolution().getEvaluation();
            Double avgMae = results.getAvgError();

            nextRow.add(evalName.substring(evalName.lastIndexOf(".") + 1, evalName.indexOf("E")));
            nextRow.add(String.valueOf(numBranches));
            nextRow.add(String.valueOf(branchLeaves));
            nextRow.add(String.valueOf(parts));
            nextRow.add(minMAe.toString().replace('.', ','));
            nextRow.add(String.valueOf(results.getTotalConcurrentTime()));
            nextRow.add(String.valueOf(results.getTotalSecuentialTime()));
            nextRow.add(avgMae.toString().replace('.', ','));
            nextRow.add(String.valueOf(results.getAvgTime()));

            tw.printRow(nextRow);

        } catch (Exception e) {
            System.err.println("EXCEPCION CAPTURADA");
        }
    }

    public static CSVTableWriter initTableWriter(String path) {
        String realpath = "MetaSolutions/";
        realpath += path;
        ArrayList<String> tableHeaders = new ArrayList<>();
        tableHeaders.addAll(Arrays.asList("Eval", "Branches", "Leaves", "Parts", "min MAE", "real Time", "sum Time", "avg Mae", "avg Time"));
        CSVTableWriter tw = null;
        try{
            tw = new CSVTableWriter(realpath, tableHeaders);
        }catch (Exception e){
            
        }
        
        return tw;
    }

}
