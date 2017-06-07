package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import global.YearInfo;

/**
 * @author César Valdés
 */
public class Normalizer {

	private final int numParams;
	private final int normType;
	private final double normRangePercentage;
	List<Double> maxs = new ArrayList<>();
	List<Double> mins = new ArrayList<>();

	public Normalizer(int numParams, int normType, double normRangePercentage) {
		this.numParams = numParams;
		this.normType = normType;
		this.normRangePercentage = normRangePercentage;

	}

	public Map<Integer, YearInfo> normalizeData(Map<Integer, YearInfo> auxYears, List<Double> maxArray,
			List<Double> minArray) {
		for (int i = 0; i < numParams; i++) {
			double dataRange = maxArray.get(i) - minArray.get(i);
			double dataMax = maxArray.get(i) + normRangePercentage * dataRange;
			double dataMin = minArray.get(i) - normRangePercentage * dataRange;
			maxs.add(dataMax);
			mins.add(dataMin);
		}
		for (YearInfo auxYear : auxYears.values()) {
			double objNormaliation = normalizeOne(auxYear.getObj(), maxs.get(0), mins.get(0));
			auxYear.setObj(objNormaliation);
			for (int i = 0; i < numParams - 1; i++) {
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
		if (normType == 1) {

			return (data - min) / (max - min);

		} else {

			return data / max;

		}
	}

	private double deNormalizeOne(double normalizedData, double max, double min) {
		if (normType == 1) {

			return (normalizedData * max - normalizedData * min) + min;

		} else {

			return normalizedData * max;

		}
	}

}
