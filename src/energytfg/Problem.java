/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cesar
 */
class Problem {

    private HashMap<Integer, YearInfo> years = new HashMap<>();
    private static int numParams;
    private static Normalizer normalizer;
    //1 MEANS: KEEPING PROPORCIONALITY IN RANGE (0,1]
    //0 MEANS: NOT KEEPING PROPORCIONALITY IN RANGE [0,1] Abraham
    private static final int NORMALIZATION_TYPE = 0;
    private static final double NORMALIZATION_RANGE_PERCENTAGE = 0.15;

    public Problem(String fileroute) {

        String cadena;
        FileReader f;
        HashMap<Integer, YearInfo> auxYears = new HashMap<>();
        ArrayList<Double> maxs = new ArrayList<>();
        ArrayList<Double> mins = new ArrayList<>();
        try {
            f = new FileReader(fileroute);

            BufferedReader b = new BufferedReader(f);

            while ((cadena = b.readLine()) != null) {

                cadena = cadena.replace(",", ".");
                String[] auxString = cadena.split(" ");
                numParams = auxString.length - 2;
                YearInfo year = new YearInfo(Integer.parseInt(auxString[0]), Double.parseDouble(auxString[1]));
                if (maxs.isEmpty()) {
                    for (int j = 0; j < numParams + 1; j++) {
                        maxs.add(Double.NEGATIVE_INFINITY);
                        mins.add(Double.POSITIVE_INFINITY);
                    }
                }

                for (int i = 0; i < numParams + 1; i++) {
                    double number = Double.parseDouble(auxString[i + 1]);
                    if (i != 0) {
                        year.insertData(number);
                    }
                    if (number > maxs.get(i)) {
                        maxs.set(i, number);
                    }
                    if (number < mins.get(i)) {
                        mins.set(i, number);
                    }
                }
                auxYears.put(year.getYear(), year);
            }
            b.close();
        } catch (FileNotFoundException ex) {
            System.err.println("File not Found EXCEPTION");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.err.println("IoEXCEPTION");
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

        Normalizer norm = new Normalizer(numParams+1, NORMALIZATION_TYPE, NORMALIZATION_RANGE_PERCENTAGE);
        years = norm.normalizeData(auxYears, maxs, mins);
        normalizer = norm;
        
//        System.out.println(auxYears.get(2000));
//        System.out.println(years.get(2000));
//        System.out.println(normalizer.denormalizeObjective(years.get(2000).getObj()));
//        System.exit(0);
    }

    public HashMap<Integer, YearInfo> getYears() {
        return years;
    }

    public void setYears(HashMap<Integer, YearInfo> years) {
        this.years = years;
    }

    public int getNumParams() {
        return numParams;
    }

    public void setNumParams(int numParams) {
        this.numParams = numParams;
    }

    public Normalizer getNormalizer() {
        return normalizer;
    }

    public void setNormalizer(Normalizer normalizer) {
        Problem.normalizer = normalizer;
    }
    
    

    public void saveNormalizedData(String full, String train, String test) {

        try {
            PrintWriter fullwriter = new PrintWriter(full, "UTF-8");
            PrintWriter testwriter = new PrintWriter(test, "UTF-8");
            PrintWriter trainwriter = new PrintWriter(train, "UTF-8");
            for (int i = 1981; i <= 2011; i++) {
                YearInfo year = years.get(i);
                ArrayList<Double> yearData = year.getFullData();
                Double yearObj = year.getObj();
                for (Double d : yearData) {
                    if (i <= 2005) {
                        trainwriter.print(d);
                        trainwriter.print(";");
                    } else {
                        testwriter.print(d);
                        testwriter.print(";");
                    }
                    fullwriter.print(d);
                    fullwriter.print(";");
                }
                if (i <= 2005) {
                    trainwriter.print(yearObj);
                    trainwriter.println();
                } else {
                    testwriter.print(yearObj);
                    testwriter.println();
                }
                fullwriter.print(yearObj);
                fullwriter.println();
            }
            fullwriter.close();
            testwriter.close();
            trainwriter.close();

        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(Problem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
