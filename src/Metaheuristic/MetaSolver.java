/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

// <editor-fold desc="">
// </editor-fold>
import Models.MetaResults;
import Models.Problem;
import Models.Solution;
import Util.Optimizers.EvaluationOptimizer;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
public class MetaSolver {

    private final ArrayList<Future> futures = new ArrayList<>();
    private final ArrayList<Solution> soluciones = new ArrayList<>();
    private final Problem problem;
    private final int numBranches;
    private final int branchIterations;
    private Long totalConcurrentTime;
    private MetaResults results;
    private final EvaluationOptimizer optimizer;

// <editor-fold desc="Constructor">
    public MetaSolver(Problem pro, int numBranches, int branchIterations, int parts, EvaluationOptimizer eo) {

        this.problem = pro;
        this.numBranches = numBranches;
        this.branchIterations = branchIterations;
        this.optimizer = eo;
        optimizer.setParts(parts);
        
    }

    public void search() {
        futures.clear();
        soluciones.clear();
        ExecutorService es = Executors.newCachedThreadPool();
        try {
            totalConcurrentTime = System.currentTimeMillis();
            for (int i = 0; i < numBranches; i++) {
                int seed = i + 5;
                Random r = new Random(seed);
                optimizer.setRandom(r);
                futures.add(es.submit(new MetaSearch(problem, branchIterations, optimizer, r)));
            }
            for (Future f : futures) {
                Solution s = (Solution) f.get();
                soluciones.add(s);
            }
            totalConcurrentTime = System.currentTimeMillis() - totalConcurrentTime;
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            es.shutdown();
            

        }

    }

    public MetaResults getResults() {
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
            Long avgTime = totalSecuentialTime/soluciones.size();
            Double avgError = avgErrorAux/soluciones.size();
            results = new MetaResults(bestSolution, totalSecuentialTime, totalConcurrentTime, avgTime, avgError);
        }

        return results;
    }

    private Solution findBestSolution() {
        Solution bestSol = soluciones.get(0);
        for (Solution sol : soluciones) {
            if (sol.getEvaluation() < bestSol.getEvaluation()) {
                bestSol = sol;
            }
        }
        return bestSol;
    }

}
