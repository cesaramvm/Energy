/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import Models.YearInfo;
import Optimizers.EvaluationOptimizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
public class MetaSearch implements Callable<Solution> {

    private final Problem problem;
    private final int branchIterations;
    private final EvaluationOptimizer optimizer;

    private Solution solution;
    private boolean done;

    public MetaSearch(Problem pro, int branchIt, EvaluationOptimizer eo) {
        this.optimizer = eo;
        problem = pro;
        branchIterations = branchIt;
    }

    @Override
    public Solution call() throws Exception {
        //System.err.println("Thread # " + Thread.currentThread().getId() + " is doing this task");

        HashMap<Integer, ProblemVariable> newSolVariables = new HashMap<>();
        for (int j = 0; j < problem.getNumParams(); j++) {
            newSolVariables.put(j, new ProblemVariable());
        }
        Double newEpsilon = -5 + (Math.random() * 10);

        Double evaluation = optimizer.evaluate(newSolVariables, newEpsilon);
        solution = new Solution(newEpsilon, newSolVariables, evaluation);
        return this.solve();
    }

    public Solution solve() {
        for (int i = 0; i < branchIterations; i++) {
            optimizer.optimize(solution);
        }
        return solution;
//        System.err.println("        " + Thread.currentThread().getId() + " END");
    }

}
