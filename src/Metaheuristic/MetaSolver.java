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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final int parts;
    private Class evaluationClass;
    private Long totalConcurrentTime;
    private MetaResults results;

// <editor-fold desc="Constructor">
    public MetaSolver(Problem pro, int numBranches, int branchIterations, int parts) {

        this.problem = pro;
        this.numBranches = numBranches;
        this.branchIterations = branchIterations;
        this.parts = parts;
        this.evaluationClass = RandomEvaluationOptimizer.class;

    }

    public void setEvaluationClass(Class evaluationClass) {
        this.evaluationClass = evaluationClass;
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
                Constructor<?> cons = evaluationClass.getConstructor(int.class, Problem.class, Random.class);
                EvaluationOptimizer eo = (EvaluationOptimizer) cons.newInstance(parts, problem, r);
                futures.add(es.submit(new MetaSearch(problem, branchIterations, eo, r)));
            }
            for (Future f : futures) {
                Solution s = (Solution) f.get();
                soluciones.add(s);
            }
            totalConcurrentTime = System.currentTimeMillis() - totalConcurrentTime;
        } catch (InterruptedException | ExecutionException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
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
            Long avgTime = totalSecuentialTime / soluciones.size();
            Double avgError = avgErrorAux / soluciones.size();
            results = new MetaResults(bestSolution, totalSecuentialTime, totalConcurrentTime, avgTime, avgError);
        }

        return results;
    }

    private Solution findBestSolution() {
        if(soluciones.isEmpty()){
            this.search();
        }
        Solution bestSol = soluciones.get(0);
        for (Solution sol : soluciones) {
            if (sol.getEvaluation() < bestSol.getEvaluation()) {
                bestSol = sol;
            }
        }
        return bestSol;
    }
    
    
    
    
    public void writeTable(String path, boolean append) {
        if (results == null) {
            findBestSolution();
        }
        String realpath = "MetaSolutions/";
        realpath += path;
        
        try {
            boolean existance = false;
            File f = new File(realpath);
            if (f.exists() && !f.isDirectory()) {
                existance = true;
            }
            FileWriter fw = new FileWriter(realpath, append);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);
            write(pw, existance);
        } catch (IOException ex) {
            Logger.getLogger(MetaSolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void write(PrintWriter fullwriter, boolean existance) {

        ArrayList<String> columns = new ArrayList<>();
        columns.addAll(Arrays.asList("Eval", "Branches", "Iterations", "Parts", "min MAE","real Time","sum Time","avg Mae","avg Time"));
        if (!existance) {
            String firstRow = "";
            for (String columnName : columns) {
                firstRow = firstRow + columnName + ";";
            }
            fullwriter.println(firstRow);
        }

        String evalName = evaluationClass.getName();
        
        String nextRow = evalName.substring(evalName.lastIndexOf(".")+1, evalName.indexOf("E"))+ ";" + numBranches + ";" + branchIterations + ";" + parts + ";";
        nextRow += results.getBestSolution().getEvaluation() + ";";
        nextRow += results.getTotalConcurrentTime() + ";";
        nextRow += results.getTotalSecuentialTime() + ";";
        nextRow += results.getAvgError() + ";";
        nextRow += results.getAvgTime() + ";";
        
        fullwriter.println(nextRow);
//        for (int j = 0; j < graphToBePrinted.size(); j++) {
//            ChartData trainChart = graphToBePrinted.get(j);
//            String nextRow = trainChart.getTransferType().substring(0, 2) + " " + trainChart.getLearningRate() + ";" + Arrays.toString(graphToBePrinted.get(0).getLayersConf()) + ";";
//            for (Integer columnIndex : columns) {
//                String error = ERROR_DF.format(trainChart.get(columnIndex));
//                nextRow += error + ";";
//            }
//            fullwriter.println(nextRow);
//        }
        fullwriter.close();

    }

}
