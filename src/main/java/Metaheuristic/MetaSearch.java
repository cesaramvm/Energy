/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import Util.Optimizers.EvaluationOptimizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 * @author Cesar
 */
public class MetaSearch implements Callable<List<Solution>> {

    private final Problem problem;
    private final int leaves;
    private final EvaluationOptimizer optimizer;
    private Long startTime;

    private ArrayList<Solution> solutions = new ArrayList<>();
    private final Random random;

    public MetaSearch(Problem pro, int leaves, EvaluationOptimizer eo, Random r) {
        this.optimizer = eo;
        this.problem = pro;
        this.leaves = leaves;
        this.random = r;
    }

    @Override
    public List<Solution> call() throws Exception {
        //System.err.println("Thread # " + Thread.currentThread().getId() + " is doing this task");
        for (int i = 0; i < leaves; i++) {
            HashMap<Integer, ProblemVariable> newSolVariables = new HashMap<>();
            for (int j = 0; j < problem.getNumParams(); j++) {
                newSolVariables.put(j, new ProblemVariable(random));
            }
            Double newEpsilon = -5 + (random.nextDouble() * 10);

            Double evaluation = optimizer.evaluate(newSolVariables, newEpsilon);
            Solution solution = new Solution(newEpsilon, newSolVariables, evaluation);
            solutions.add(solution);
        }

        return this.solve();
    }

    public List<Solution> solve() {
        for (Solution sol : solutions) {
            this.startTime = System.currentTimeMillis();
            optimizer.optimize(sol);
            sol.setExecutionTime(System.currentTimeMillis() - startTime);
        }
        return solutions;
    }

}
