/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Optimizers;

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

    public RandomEvaluationOptimizer(int parts, Problem problem) {
        super(parts, problem);
    }

    @Override
    public void optimize(Solution solution) {

        //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
        int selectedChange = (int) (Math.random() * 15);
        Double newEpsilon;
        HashMap<Integer, ProblemVariable> newProbVariables;

        if (selectedChange == 14) {
            newProbVariables = solution.getProbVariables();
            newEpsilon = this.getNewEpsilon();
        } else {
            newEpsilon = solution.getEpsilon();
            newProbVariables = new HashMap<Integer, ProblemVariable>(solution.getProbVariables());
            Double newValue = this.getNewValue();
            Boolean changeAlpha = new Random().nextBoolean();
            if (changeAlpha) {
                System.out.println(newProbVariables.get(selectedChange) == solution.getProbVariables().get(selectedChange));
                newProbVariables.get(selectedChange).setAlfa(newValue);
            } else {
                System.out.println(newProbVariables.get(selectedChange) == solution.getProbVariables().get(selectedChange));
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
