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
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;

/**
 *
 * @author Cesar
 */
public class MetaSearch implements Callable<Solution> {

    private final Problem problem;
    private final int branchIterations;
    private final EvaluationOptimizer optimizer;
    private final Long startTime;

    private Solution solution;
    private final Random random;

    public MetaSearch(Problem pro, int branchIt, EvaluationOptimizer eo, Random r) {
        startTime = System.currentTimeMillis();
        this.optimizer = eo;
        problem = pro;
        branchIterations = branchIt;
        this.random = r;
    }

    @Override
    public Solution call() throws Exception {
        //System.err.println("Thread # " + Thread.currentThread().getId() + " is doing this task");

        HashMap<Integer, ProblemVariable> newSolVariables = new HashMap<>();
        for (int j = 0; j < problem.getNumParams(); j++) {
            newSolVariables.put(j, new ProblemVariable(random));
        }
        Double newEpsilon = -5 + (random.nextDouble() * 10);

        Double evaluation = optimizer.evaluate(newSolVariables, newEpsilon);
        solution = new Solution(newEpsilon, newSolVariables, evaluation);
        return this.solve();
    }

    public Solution solve() {
        for (int i = 0; i < branchIterations; i++) {
            optimizer.optimize(solution);
        }
        Long elapsedTime = System.currentTimeMillis() - startTime;
        solution.setExecutionTime(elapsedTime);
        return solution;
    }

}
