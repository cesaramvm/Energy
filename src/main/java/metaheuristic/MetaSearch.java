package metaheuristic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

import global.Problem;
import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;
import util.optimizers.Optimizer;

/**
 * @author C�sar Vald�s
 */
public class MetaSearch implements Callable<List<MetaSolution>> {

	private static final Problem problem = Problem.getInstance();
	private final int leaves;
	private final Optimizer optimizer;
	private Long startTime;

	private ArrayList<MetaSolution> solutions = new ArrayList<>();
	private final Random random;

	public MetaSearch(int leaves, Optimizer eo, Random r) {
		this.optimizer = eo;
		this.leaves = leaves;
		this.random = r;
	}

	@Override
	public List<MetaSolution> call() throws Exception {
		for (int i = 0; i < leaves; i++) {
			HashMap<Integer, MetaVariable> newSolVariables = new HashMap<>();
			for (int j = 0; j < problem.getNumParams(); j++) {
				newSolVariables.put(j, new MetaVariable(random));
			}
			Double newEpsilon = -5 + (random.nextDouble() * 10);

			Double evaluation = optimizer.evaluate(newSolVariables, newEpsilon);
			MetaSolution solution = new MetaSolution(newEpsilon, newSolVariables, evaluation);
			solutions.add(solution);
		}

		return this.solve();
	}

	public List<MetaSolution> solve() {
		for (MetaSolution sol : solutions) {
			this.startTime = System.currentTimeMillis();
			optimizer.optimize(sol);
			sol.setExecutionTime(System.currentTimeMillis() - startTime);
		}
		return solutions;
	}

}
