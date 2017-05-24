package Util;

import java.util.ArrayList;
import java.util.HashMap;

import Global.YearInfo;

/**
 * @author César Valdés
 */
public class Normalizer {

    private final int num_Params;
    private final int norm_Type;
    private final double norm_Range_Percentage;
    ArrayList<Double> maxs = new ArrayList<>();
    ArrayList<Double> mins = new ArrayList<>();

    public Normalizer(int num_params, int norm_type, double norm_range_percentage) {
        num_Params = num_params;
        norm_Type = norm_type;
        norm_Range_Percentage = norm_range_percentage;

    }

    public HashMap<Integer, YearInfo> normalizeData(HashMap<Integer, YearInfo> auxYears, ArrayList<Double> max_array, ArrayList<Double> min_array) {
        for (int i = 0; i < num_Params; i++) {
            double dataRange = max_array.get(i) - min_array.get(i);
            double dataMax = max_array.get(i) + norm_Range_Percentage * dataRange;
            double dataMin = min_array.get(i) - norm_Range_Percentage * dataRange;
//            System.out.println("El maximo era: " + maxs.get(i + 1) + "ahora es " + dataMax);
//            System.out.println("El minimo era: " + mins.get(i + 1) + "ahora es " + dataMin + "\n");
            maxs.add(dataMax);
            mins.add(dataMin);
        }
        for (YearInfo auxYear : auxYears.values()) {
            double objNormaliation = normalizeOne(auxYear.getObj(), maxs.get(0), mins.get(0));
            auxYear.setObj(objNormaliation);
            for (int i = 0; i < num_Params - 1; i++) {
                double normalization = normalizeOne(auxYear.getData(i), maxs.get(i + 1), mins.get(i + 1));
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

            return (normalizedData * max - normalizedData * min) + min;

        } else {

            return (normalizedData * max);

        }
    }

}
