package energytfg;

import Metaheuristic.MetaSolver;
import Models.MetaResults;
import Models.Problem;
import Models.Solution;
import NeuralNetwork.NeurophSolution;
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


    public static void main(String[] args) {
            Problem problem = new Problem("ProjectData/O-data.txt");
            problem.saveNormalizedData(FULLPATH, TRAINPATH, TESTPATH);
            int searchBranches = 5;
            int branchIterations = 100;
            int parts = 10001;

            MetaSolver metaSol = new MetaSolver(problem, searchBranches, branchIterations, parts);
            metaSol.search();
            MetaResults results = metaSol.getResults();
            
            System.out.println(results.getBestSolution());
            System.out.println("Secuencial: " + results.getTotalSecuentialTime());
            System.out.println("Concurrent: " + results.getTotalConcurrentTime());
            System.out.println("Avg Error :" + results.getAvgError());
            System.out.println("Avg Time  :" + results.getAvgTime());
//            System.out.println(solution);
//            System.out.println("Tiempo de busqueda " + );
            
        try {
//            Solution sol = metaSol.findBestSolution();
//            System.out.println(sol.toString());

            //NeurophSolution ns = new NeurophSolution(FULLPATH, TRAINPATH, TESTPATH);
            //ns.fullSearch(problem);
            //ns.findBestNetwork1(problem);
            //String fileRoute = "Net.nnet";
            //ns.networkTest(fileRoute, problem, "FinalNnetOut.csv");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
