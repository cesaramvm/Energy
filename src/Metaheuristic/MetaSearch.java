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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
public class MetaSearch {

    private final ArrayList<MetaSolution> threads = new ArrayList<>();
    private final ArrayList<Solution> soluciones = new ArrayList<>();
    private final Problem problem;
    private final int numBranches;
    private final int branchIterations;
// <editor-fold desc="Constructor">

    public MetaSearch(Problem pro, int numBranches, int branchIterations) {

        this.problem = pro;
        this.numBranches = numBranches;
        this.branchIterations = branchIterations;

    }

    public void search() {

        try {

            for (int i = 0; i < numBranches; i++) {

                int parts = 9;
                MetaSolution ms = new MetaSolution(problem, branchIterations, parts);
                ms.start();
                threads.add(ms);
            }
            //State st = threads.get(0).getState();
            int i = 0;

            while (!threads.isEmpty()) {
                MetaSolution ms = threads.get(i);
                if (!(ms.isAlive())) {
                    soluciones.add(ms.result());
                    threads.remove(ms);
                }
                if (!threads.isEmpty()) {
                    i = (i + 1) % threads.size();
                }
            }

//            System.out.println(soluciones);
//            Solution result = threads.get(0).result();

//            while ((result = threads.get(0).result()) == null) {
//            System.out.println("sin result");
//            }
        } catch (Exception ex) {
            Logger.getLogger(MetaSearch.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public Solution findBestSolution() throws Exception{
        
        if(soluciones.size()!=0){
            return this.meanSolution();
        } else {
            throw new Exception("search Not Made");
        }
    }
// </editor-fold>

    private Solution meanSolution() {
        Solution finalSolution = new Solution(14);
        for(Solution sol: soluciones){
            double lastEpsilon = finalSolution.getEpsilon();
            finalSolution.setEpsilon(lastEpsilon + sol.getEpsilon()/numBranches);
            for (int i=0; i<sol.getProbVariables().size(); i++){
                double newAlpha = sol.getProbVariables().get(i).getAlfa();
                double newBeta = sol.getProbVariables().get(i).getBeta();
                double lastAlpha =  finalSolution.getProbVariables().get(i).getAlfa();
                double lastBeta =  finalSolution.getProbVariables().get(i).getBeta();
                finalSolution.getProbVariables().get(i).setAlfa(lastAlpha + newAlpha/numBranches);
                finalSolution.getProbVariables().get(i).setBeta(lastBeta + newBeta/numBranches);
            }
        }
        
        
        return finalSolution;
    }

}
