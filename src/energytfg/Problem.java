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
    private int numParams;
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

        normalizeData(auxYears, maxs, mins);

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

    private void normalizeData(HashMap<Integer, YearInfo> auxYears, ArrayList<Double> maxs, ArrayList<Double> mins) {
        double objRange = maxs.get(0) - mins.get(0);
        double objMax = maxs.get(0) + NORMALIZATION_RANGE_PERCENTAGE * objRange;
        double objMin = mins.get(0) + NORMALIZATION_RANGE_PERCENTAGE * objRange;
        System.out.println("El maximo era: " + maxs.get(0) + "ahora es " + objMax);
        System.out.println("El minimo era: " + mins.get(0) + "ahora es " + objMin + "\n");

        for (int i = 0; i < numParams; i++) {
            double dataRange = maxs.get(i + 1) - mins.get(i + 1);
            double dataMax = maxs.get(i + 1) + NORMALIZATION_RANGE_PERCENTAGE * dataRange;
            double dataMin = mins.get(i + 1) + NORMALIZATION_RANGE_PERCENTAGE * dataRange;
            System.out.println("El maximo era: " + maxs.get(i + 1) + "ahora es " + dataMax);
            System.out.println("El minimo era: " + mins.get(i + 1) + "ahora es " + dataMin + "\n");
            for (YearInfo auxYear : auxYears.values()) {
                double objNormaliation = normalizeOne(auxYear.getObj(), objMax, objMin);
                auxYear.setObj(objNormaliation);
                double normalization = normalizeOne(auxYear.getData(i), dataMax, dataMin);
                auxYear.setData(i, normalization);
            }

        }
        
        years = auxYears;
    }

    private double normalizeOne(double data, double max, double min) {
        if (NORMALIZATION_TYPE == 1) {

            return ((data - min) / (max - min));

        } else {

            return (data / max);

        }
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
                        trainwriter.print(",");
                    } else {
                        testwriter.print(d);
                        testwriter.print(",");
                    }
                    fullwriter.print(d);
                    fullwriter.print(",");
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
