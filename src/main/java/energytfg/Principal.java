package energytfg;

import Metaheuristic.MetaSolver;
import Models.MetaResults;
import Models.Problem;
import Util.Optimizers.LSBIEvaluationOptimizer;
import Util.Optimizers.LSFIEvaluationOptimizer;
import Util.Optimizers.RandomEvaluationOptimizer;
import Util.Writers.CSVTableWriter;
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
//TODO QUITAR SuppressWarnings
public class Principal {

    private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
    private static final String TRAINPATH = "ProjectData/N-train.csv";
    private static final String TESTPATH = "ProjectData/N-test.csv";
    private static final Problem problem = new Problem("ProjectData/O-data.txt").saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);

    public static void main(String[] args) {
        try {
            easy();
//            advanced();
        } catch (Exception ex) {
            Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
        }
//            NeurophSolution ns = new NeurophSolution(FULLPATH, TRAINPATH, TESTPATH);
//            ns.fullSearch(problem);
//            ns.findBestNetwork1(problem);
//            String fileRoute = "Net.nnet";
//            ns.networkTest(fileRoute, problem, "FinalNnetOut.csv");
    }

    private static void easy() {

        int searchBranches = 1;
        int leaves = 1;
        int parts = 3;

        MetaSolver metaSol = new MetaSolver(problem, searchBranches, leaves, parts);
        //RandomEvaluationOptimizer
        //LSFIEvaluationOptimizer
        //LSBIEvaluationOptimizer
        metaSol.setEvaluationClass(LSBIEvaluationOptimizer.class);
//        metaSol.setEvaluationClass(LSBIEvaluationOptimizer.class);
        metaSol.search();
        MetaResults results = metaSol.getResults();
        CSVTableWriter tw = MetaSolver.initTableWriter("test.csv");
        metaSol.writeRow(tw);
        tw.close();
        
        System.out.println(results.getBestSolution());
        System.out.println("Secuencial: " + results.getTotalSecuentialTime());
        System.out.println("Concurrent: " + results.getTotalConcurrentTime());
        System.out.println("Avg Error :" + results.getAvgError());
        System.out.println("Avg Time  :" + results.getAvgTime());
//            System.out.println(solution);
//            System.out.println("Tiempo de busqueda " + );

    }

    @SuppressWarnings("unused")
	private static void advanced() {

        ArrayList<Class<? extends Object>> optimizers = new ArrayList<>(Arrays.asList(RandomEvaluationOptimizer.class, LSFIEvaluationOptimizer.class, LSBIEvaluationOptimizer.class));

        ArrayList<Integer> parts = new ArrayList<>(Arrays.asList(99, 499, 999, 2999));
        ArrayList<Integer> numLeaves = new ArrayList<>(Arrays.asList(50, 100, 500));
        ArrayList<Integer> numBranches = new ArrayList<>(Arrays.asList(1, 2, 4, 8));

        CSVTableWriter tw = MetaSolver.initTableWriter("test.csv");
        for (int part : parts) {
            for (int branches : numBranches) {
                for (int leaves : numLeaves) {
                    for (Class<? extends Object> optimizer : optimizers) {

                        MetaSolver metaSol = new MetaSolver(problem, branches, leaves, part);
                        metaSol.setEvaluationClass(optimizer);
                        metaSol.search();
                        MetaResults results = metaSol.getResults();
                        System.out.println(results.getBestSolution());
                        System.out.println("Secuencial: " + results.getTotalSecuentialTime());
                        System.out.println("Concurrent: " + results.getTotalConcurrentTime());
                        System.out.println("Avg Error :" + results.getAvgError());
                        System.out.println("Avg Time  :" + results.getAvgTime());
//                                System.out.println(LSFIEvaluationOptimizer.class.getCanonicalName());
                        metaSol.writeRow(tw);

//                                Runtime runtime = Runtime.getRuntime();
//                                Process proc = runtime.exec("shutdown -s -t 0");
//                                System.exit(0);
//                            }
                    }
                }
            }
        }
        tw.close();

    }

}
