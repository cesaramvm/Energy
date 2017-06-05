package Util.Optimizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import Global.GlobalConstants;
import Global.Models.YearInfo;
import Metaheuristic.Models.MetaSolution;
import Metaheuristic.Models.MetaVariable;

/**
 * @author César Valdés
 */
public abstract class EvaluationOptimizer extends GlobalConstants implements Optimizer {

	protected final ArrayList<Double> valueList;
	protected final ArrayList<Double> epsilonList;
	protected final int parts;
	protected final Random random;

	protected EvaluationOptimizer(int newParts, Random r) {

		this.parts = newParts;
		this.random = r;
		valueList = this.newRandomList(1.0, newParts);
		epsilonList = this.newRandomList(5.0, newParts);
	}

	@Override
	public double evaluate(HashMap<Integer, MetaVariable> variables, Double epsi) {
		int numYears = problem.getYears().size();
		Double aux = 1.0 / numYears;
		Double summation = 0.0;

		for (YearInfo yi : problem.getYears().values()) {
			Double objective = yi.getObj();
			Double predictSum = 0.0;
			for (int j = 0; j < yi.getFullData().size(); j++) {
				MetaVariable solVar = variables.get(j);
				predictSum += solVar.getAlfa() * Math.pow(yi.getData(j), solVar.getBeta());
			}
			Double predict = epsi + predictSum;
			summation += Math.pow(objective - predict, 2);
		}

		double result = aux * summation;

		return result;
	}

	protected final ArrayList<Double> newRandomList(Double value, int parts) {
		ArrayList<Double> numbers = new ArrayList<>();
		numbers.add(0.0);
		double aux = (double) (parts - 1) / 2;
		double part = value / aux;
		for (int i = 1; i <= aux; i++) {
			numbers.add(i * part);
			numbers.add(-(i * part));
		}
		Collections.shuffle(numbers, random);
		return numbers;
	}

	protected final HashMap<Integer, MetaVariable> cloneMap(HashMap<Integer, MetaVariable> original) {
		HashMap<Integer, MetaVariable> clone = new HashMap<Integer, MetaVariable>();
		for (Map.Entry<Integer, MetaVariable> entry : original.entrySet()) {
			try {
				clone.put(entry.getKey(), (MetaVariable) entry.getValue().clone());
			} catch (CloneNotSupportedException ex) {
				Logger.getLogger(EvaluationOptimizer.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return clone;
	}

	protected Double getNewEpsilon() {
		return epsilonList.get(random.nextInt(parts));
	}

	protected Double getNewValue() {
		return valueList.get(random.nextInt(parts));
	}

	@Override
	abstract public void optimize(MetaSolution solution);

}
