package Util.Optimizers;

import java.util.HashMap;
import java.util.Random;

import Metaheuristic.Models.MetaSolution;
import Metaheuristic.Models.MetaVariable;

/**
 * @author C�sar Vald�s
 */
public class RandomEvaluationOptimizer extends EvaluationOptimizer {

    public RandomEvaluationOptimizer(int parts, Random r) {
        super(parts, r);
    }

    @Override
    public void optimize(MetaSolution solution) {
        int i = 0;
        while (i<35000) {
        	i++;
            //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
            int selectedChange = (int) (random.nextDouble() * (problem.getNumParams() + 1));
            Double newEpsilon = solution.getEpsilon();
            HashMap<Integer, MetaVariable> newProbVariables = this.cloneMap(solution.getProbVariables());

            if (selectedChange == problem.getNumParams()) {
                newEpsilon = this.getNewEpsilon();
            } else {
                Double newValue = this.getNewValue();
                Boolean changeAlpha = random.nextBoolean();
                if (changeAlpha) {
                    newProbVariables.get(selectedChange).setAlfa(newValue);
                } else {
                    newProbVariables.get(selectedChange).setBeta(newValue);
                }

            }
            double newEvaluation = this.evaluate(newProbVariables, newEpsilon);
            if (newEvaluation < solution.getEvaluation()) {
                solution.setEpsilon(newEpsilon);
                solution.setProbVariables(newProbVariables);
                solution.setEvaluation(newEvaluation);
            }
        }

    }

}
