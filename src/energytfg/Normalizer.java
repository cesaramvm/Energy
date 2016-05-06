/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author sobremesa
 */
public class Normalizer {

    private final int num_Params;
    private final int norm_Type;
    private final double norm_Range_Percentage;
    ArrayList<Double> maxs = new ArrayList<>();
    ArrayList<Double> mins = new ArrayList<>();

    public Normalizer(int num_params, int norm_type, double norm_range_percentage, ArrayList<Double> max_array, ArrayList<Double> min_array) {
        num_Params = num_params;
        norm_Type = norm_type;
        norm_Range_Percentage = norm_range_percentage;
        maxs = max_array;
        mins = min_array;
    }

    public HashMap<Integer, YearInfo> normalizeData(HashMap<Integer, YearInfo> auxYears) {
        double objRange = maxs.get(0) - mins.get(0);
        double objMax = maxs.get(0) + norm_Range_Percentage * objRange;
        double objMin = mins.get(0) - norm_Range_Percentage * objRange;
//        System.out.println("El maximo era: " + maxs.get(0) + "ahora es " + objMax);
//        System.out.println("El minimo era: " + mins.get(0) + "ahora es " + objMin + "\n");

        ArrayList<Double> newMaxs = new ArrayList<>();
        ArrayList<Double> newMins = new ArrayList<>();

        for (int i = 0; i < num_Params; i++) {
            double dataRange = maxs.get(i + 1) - mins.get(i + 1);
            double dataMax = maxs.get(i + 1) + norm_Range_Percentage * dataRange;
            double dataMin = mins.get(i + 1) - norm_Range_Percentage * dataRange;
//            System.out.println("El maximo era: " + maxs.get(i + 1) + "ahora es " + dataMax);
//            System.out.println("El minimo era: " + mins.get(i + 1) + "ahora es " + dataMin + "\n");
            newMaxs.add(dataMax);
            newMins.add(dataMin);
        }
        for (YearInfo auxYear : auxYears.values()) {
            double objNormaliation = normalizeOne(auxYear.getObj(), objMax, objMin);
            auxYear.setObj(objNormaliation);
            for (int i = 0; i < num_Params; i++) {
                double normalization = normalizeOne(auxYear.getData(i), newMaxs.get(i), newMins.get(i));
                auxYear.setData(i, normalization);
            }

        }

        return auxYears;

    }

    public double denormalizeObjective(double normalizedObjective) {

        return deNormalizeOne(normalizedObjective, maxs.get(0), mins.get(0));
        
    }

    private double normalizeOne(double data, double max, double min) {
        if (norm_Type == 1) {

            return ((data - min) / (max - min));

        } else {

            return (data / max);

        }
    }

    private double deNormalizeOne(double normalizedData, double max, double min) {
        if (norm_Type == 1) {

            return (normalizedData * max - normalizedData * min)+min;

        } else {

            return (normalizedData * max);

        }
    }

}
