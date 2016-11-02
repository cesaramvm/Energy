/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

// <editor-fold desc="">
// </editor-fold>
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
public class MetaSolution {

    private final ArrayList<Future> futures = new ArrayList<>();
    private final ArrayList<Solution> soluciones = new ArrayList<>();
    private final Problem problem;
    private final int numBranches;
    private final int branchIterations;
    private final int parts;
// <editor-fold desc="Constructor">

    public MetaSolution(Problem pro, int numBranches, int branchIterations, int parts) {

        this.problem = pro;
        this.numBranches = numBranches;
        this.branchIterations = branchIterations;
        this.parts = parts;

    }

    public void search() {
        futures.clear();
        soluciones.clear();
        ExecutorService es = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < numBranches; i++) {
                Random r = new Random(i);
//                EvaluationOptimizer eo = new RandomEvaluationOptimizer(parts, problem, r);
                EvaluationOptimizer eo = new LSFIEvaluationOptimizer(parts, problem, r);
//                EvaluationOptimizer eo = new LSBIEvaluationOptimizer(parts, problem, r);
                futures.add(es.submit(new MetaSearch(problem, branchIterations, eo, r)));
            }
            for (Future f : futures){
                Solution s = (Solution) f.get();
                soluciones.add(s);
            }
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(MetaSolution.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            es.shutdown();
        }

    }

    public Solution findBestSolution(){

        if (!soluciones.isEmpty()) {
            return this.bestSolution();
        } else {
            return null;
        }
    }
// </editor-fold>

    private Solution meanSolution() {
        Solution finalSolution = new Solution(14);
        for (Solution sol : soluciones) {
            double lastEpsilon = finalSolution.getEpsilon();
            finalSolution.setEpsilon(lastEpsilon + sol.getEpsilon() / numBranches);
            for (int i = 0; i < sol.getProbVariables().size(); i++) {
                double newAlpha = sol.getProbVariables().get(i).getAlfa();
                double newBeta = sol.getProbVariables().get(i).getBeta();
                double lastAlpha = finalSolution.getProbVariables().get(i).getAlfa();
                double lastBeta = finalSolution.getProbVariables().get(i).getBeta();
                finalSolution.getProbVariables().get(i).setAlfa(lastAlpha + newAlpha / numBranches);
                finalSolution.getProbVariables().get(i).setBeta(lastBeta + newBeta / numBranches);
            }
        }

        return finalSolution;
    }

    private Solution bestSolution() {
        Solution bestSol = soluciones.get(0);
         for (Solution sol : soluciones) {
             if(sol.getEvaluation() < bestSol.getEvaluation()){
                 bestSol = sol;
             }
         }
         return bestSol;
    }

}
