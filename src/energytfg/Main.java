package energytfg;

import java.util.ArrayList;
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Cesar
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    
    private static final String FULLPATH = "ProjectData/N-fulldataset.csv";
    private static final String TRAINPATH = "ProjectData/N-train.csv";
    private static final String TESTPATH = "ProjectData/N-test.csv";
    
    public static void main(String[] args) {

        Problem problem = new Problem("ProjectData/O-data.txt");
        problem.saveNormalizedData(FULLPATH,TRAINPATH,TESTPATH);
        Solution sol = new Solution(problem);
        sol.solve();

    }

}
