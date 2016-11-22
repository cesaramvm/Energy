package energytfg;

import Metaheuristic.MetaSolver;
import Models.MetaResults;
import Models.Problem;
import Models.Solution;
import NeuralNetwork.NeurophSolution;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;
import java.io.IOException;
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
        try {

            int searchBranches = 1;
            int leaves = 1;
            int parts = 3501;

            MetaSolver metaSol = new MetaSolver(problem, searchBranches, leaves, parts);
            //RandomEvaluationOptimizer
            //LSFIEvaluationOptimizer
            //LSBIEvaluationOptimizer
            metaSol.setEvaluationClass(LSBIEvaluationOptimizer.class);
//            metaSol.setEvaluationClass(LSFIEvaluationOptimizer.class);
//        metaSol.setEvaluationClass(LSBIEvaluationOptimizer.class);
            metaSol.search();
            MetaResults results = metaSol.getResults();
            System.out.println(results);

        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
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
        int leaves = 5;
        int parts = 1001;

        MetaSolver metaSol = new MetaSolver(problem, searchBranches, leaves, parts);
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

    private static void advanced() {

        ArrayList<Class> optimizers = new ArrayList<>(Arrays.asList(RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class, LSBIEvaluationOptimizer.class));

        ArrayList<Integer> parts = new ArrayList<>(Arrays.asList(499, 999, 3999));
        ArrayList<Integer> numLeaves = new ArrayList<>(Arrays.asList(1000, 5000, 10000));
        ArrayList<Integer> numBranches = new ArrayList<>(Arrays.asList(4, 8, 16, 32));

//            boolean continuar = false;
        for (int part : parts) {
            for (int branches : numBranches) {
                for (int leaves : numLeaves) {
                    for (Class optimizer : optimizers) {
//                            if (part == 3999 && iterations == 10000 && branches == 32 && optimizer == LSBIEvaluationOptimizer.class) {
//                                continuar = true;
//                            }
//                            if (continuar) {

                        MetaSolver metaSol = new MetaSolver(problem, branches, leaves, part);
                        metaSol.setEvaluationClass(optimizer);
                        metaSol.search();
                        MetaResults results = metaSol.getResults();
//                                System.out.println(LSFIEvaluationOptimizer.class.getCanonicalName());
//                                metaSol.writeTable("MetaData.csv", true);

//                                Runtime runtime = Runtime.getRuntime();
//                                Process proc = runtime.exec("shutdown -s -t 0");
//                                System.exit(0);
//                            }
                    }
                }
            }
        }

    }

}
