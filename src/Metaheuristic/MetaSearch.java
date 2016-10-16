/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

// <editor-fold desc="">
// </editor-fold>
import Exceptions.NotOddNumberException;
import Models.Problem;
import Models.Solution;
import java.util.ArrayList;

/**
 *
 * @author Cesar
 */
public class MetaSearch {

// <editor-fold desc="Constructor">
    public MetaSearch(Problem pro, int numBranches, int branchIterations) throws NotOddNumberException {
        ArrayList<MetaSolution> threads = new ArrayList<>();
        for (int i = 0; i < numBranches; i++) {
            int parts = 5;
            MetaSolution ms = new MetaSolution(pro, branchIterations, parts);
            ms.start();
            threads.add(ms);
        }
        
        //State st = threads.get(0).getState();
        Solution result;
        while((result=threads.get(0).result())==null){
//            System.out.println("sin result");
        }
        System.out.println(result);
        
        
        
    }
// </editor-fold>

}
