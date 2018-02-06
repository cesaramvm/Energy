package util.optimizers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;

/**
 * @author César Valdés
 */
public class LSFIEvaluationOptimizer extends LSEvaluationOptimizer {

	private ArrayList<Double> valueListCopy;
	private ArrayList<Double> epsilonListCopy;

	public LSFIEvaluationOptimizer(int parts, Random r) {
		super(parts, r);
	}

	@Override
	public void optimize(MetaSolution solution) {
		double newEvaluation = solution.getEvaluation();
		while (!paramsIndex.isEmpty()) {
			valueListCopy = new ArrayList<>(valueList);
			epsilonListCopy = new ArrayList<>(epsilonList);

			Integer selectedChange = paramsIndex.get(random.nextInt(paramsIndex.size()));
			Double newEpsilon = solution.getEpsilon();
			Map<Integer, MetaVariable> newProbVariables = this.cloneMap(solution.getProbVariables());

			while (newEvaluation >= solution.getEvaluation() && !valueListCopy.isEmpty()
					&& !epsilonListCopy.isEmpty()) {
				if (selectedChange == 14) {
					newEpsilon = this.getNewEpsilon();
					epsilonListCopy.remove(newEpsilon);
				} else {
					Double newValue = this.getNewValue();
					valueListCopy.remove(newValue);
					Boolean changeAlpha = random.nextBoolean();
					if (changeAlpha) {
						newProbVariables.get(selectedChange).setAlfa(newValue);
					} else {
						newProbVariables.get(selectedChange).setBeta(newValue);
					}
				}
				newEvaluation = this.evaluate(newProbVariables, newEpsilon);
			}

			if (newEvaluation < solution.getEvaluation()) {
				solution.setEpsilon(newEpsilon);
				solution.setProbVariables(newProbVariables);
				solution.setEvaluation(newEvaluation);
				this.restoreParamsIndex();
			} else {
				paramsIndex.remove(selectedChange);
			}
		}
		this.restoreParamsIndex();
	}
	

	@Override
	protected Double getNewEpsilon() {
		return epsilonListCopy.get(random.nextInt(epsilonListCopy.size()));
	}
	@Override
	protected Double getNewValue() {
		return valueListCopy.get(random.nextInt(valueListCopy.size()));
	}

}
