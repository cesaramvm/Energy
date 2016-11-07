package energytfg;

import Metaheuristic.MetaSolver;
import Models.MetaResults;
import Models.Problem;
import Models.Solution;
import NeuralNetwork.NeurophSolution;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
//raul cabido
//juanjo
//montemayor
//jose velez
//buenaposada
public class Main {

    private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
    private static final String TRAINPATH = "ProjectData/N-train.csv";
    private static final String TESTPATH = "ProjectData/N-test.csv";
    private static final Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);

    public static void main(String[] args) {

        ArrayList<Class> optimizers = new ArrayList<>(Arrays.asList(RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class, LSBIEvaluationOptimizer.class));

        ArrayList<Integer> numBranches = new ArrayList<>(Arrays.asList(4, 8, 16, 32));
        ArrayList<Integer> numIterations = new ArrayList<>(Arrays.asList(1000, 5000, 10000));
        ArrayList<Integer> parts = new ArrayList<>(Arrays.asList(499, 999, 3999));

        for (int part : parts) {
            for (int iterations : numIterations) {
                for (int branches : numBranches) {
                    for (Class optimizer : optimizers) {
                        System.out.println(optimizer.getCanonicalName());
                        MetaSolver metaSol = new MetaSolver(problem, branches, iterations, part);
                        metaSol.setEvaluationClass(optimizer);
                        metaSol.search();
                        MetaResults results = metaSol.getResults();
                        metaSol.writeTable("MetaData.csv", true);
                    }
                }
            }
        }

//        easy();

//            NeurophSolution ns = new NeurophSolution(FULLPATH, TRAINPATH, TESTPATH);
//            ns.fullSearch(problem);
//            ns.findBestNetwork1(problem);
//            String fileRoute = "Net.nnet";
//            ns.networkTest(fileRoute, problem, "FinalNnetOut.csv");

    }

    private static void easy() {

        int searchBranches = 5;
        int branchIterations = 5;
        int parts = 1001;

        MetaSolver metaSol = new MetaSolver(problem, searchBranches, branchIterations, parts);
        //RandomEvaluationOptimizer
        //LSFIEvaluationOptimizer
        //LSBIEvaluationOptimizer
//        metaSol.setEvaluationClass(RandomEvaluationOptimizer.class);
        metaSol.setEvaluationClass(LSFIEvaluationOptimizer.class);
//        metaSol.setEvaluationClass(LSBIEvaluationOptimizer.class);
        metaSol.search();
        MetaResults results = metaSol.getResults();
        metaSol.writeTable("MetaData.csv", true);

        System.out.println(results.getBestSolution());
        System.out.println("Secuencial: " + results.getTotalSecuentialTime());
        System.out.println("Concurrent: " + results.getTotalConcurrentTime());
        System.out.println("Avg Error :" + results.getAvgError());
        System.out.println("Avg Time  :" + results.getAvgTime());
//            System.out.println(solution);
//            System.out.println("Tiempo de busqueda " + );

    }

}
