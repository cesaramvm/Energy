/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class RandomEvaluationOptimizer extends EvaluationOptimizer {

    public RandomEvaluationOptimizer(int parts, Problem problem, Random r) {
        super(parts, problem, r);
    }

    @Override
    public void optimize(Solution solution) {

        //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
        int selectedChange = (int) (random.nextDouble() * 15);
        Double newEpsilon;
        HashMap<Integer, ProblemVariable> newProbVariables;

        if (selectedChange == 14) {
            newProbVariables = solution.getProbVariables();
            newEpsilon = this.getNewEpsilon();
        } else {
            newEpsilon = solution.getEpsilon();
            newProbVariables = this.cloneMap(solution.getProbVariables());
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
