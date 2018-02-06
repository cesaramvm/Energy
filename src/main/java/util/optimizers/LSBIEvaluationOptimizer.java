package util.optimizers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Random;

import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;

/**
 * @author César Valdés
 */
public class LSBIEvaluationOptimizer extends LSEvaluationOptimizer {

	public LSBIEvaluationOptimizer(int parts, Random r) {
		super(parts, r);
	}

	@Override
	public void optimize(MetaSolution solution) {
		Double newEvaluation;
		while (!paramsIndex.isEmpty()) {
			ArrayList<Double> evaluations = new ArrayList<>();
			Integer selectedChange = paramsIndex.get(random.nextInt(paramsIndex.size()));
			Double newEpsilon = solution.getEpsilon();
			Map<Integer, MetaVariable> newProbVariables = this.cloneMap(solution.getProbVariables());

			if (selectedChange == 14) {
				for (Double d : epsilonList) {
					evaluations.add(this.evaluate(newProbVariables, d));
				}
				int minIndex = evaluations.indexOf(Collections.min(evaluations));
				newEpsilon = epsilonList.get(minIndex);
				newEvaluation = evaluations.get(minIndex);
			} else {
				newProbVariables = this.cloneMap(solution.getProbVariables());
				Boolean changeAlpha = random.nextBoolean();
				if (changeAlpha) {
					for (Double alpha : valueList) {
						newProbVariables.get(selectedChange).setAlfa(alpha);
						evaluations.add(this.evaluate(newProbVariables, newEpsilon));
					}
					int minIndex = evaluations.indexOf(Collections.min(evaluations));
					newEvaluation = evaluations.get(minIndex);
					newProbVariables.get(selectedChange).setAlfa(valueList.get(minIndex));
				} else {
					for (Double beta : valueList) {
						newProbVariables.get(selectedChange).setBeta(beta);
						evaluations.add(this.evaluate(newProbVariables, newEpsilon));
					}
					int minIndex = evaluations.indexOf(Collections.min(evaluations));
					newEvaluation = evaluations.get(minIndex);
					newProbVariables.get(selectedChange).setBeta(valueList.get(minIndex));
				}

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

}
