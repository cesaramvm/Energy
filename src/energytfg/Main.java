package energytfg;

import Metaheuristic.MetaSolution;
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
            int searchBranches = 500;
            int branchIterations = 50000;

            MetaSolution metaSol = new MetaSolution(problem, searchBranches, branchIterations);
            Long start = System.currentTimeMillis();
            metaSol.search();
            Long end = System.currentTimeMillis();
            System.out.println("Tiempo: " + (end-start));
            
            Solution bestSol = metaSol.findBestSolution();
            System.out.println(bestSol);
            
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
