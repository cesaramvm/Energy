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
        Long start = System.currentTimeMillis();
        while (System.currentTimeMillis()-start < 2000){
            //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
            int selectedChange = (int) (random.nextDouble() * (problem.getNumParams()+1));
            Double newEpsilon = solution.getEpsilon();
            HashMap<Integer, ProblemVariable> newProbVariables = this.cloneMap(solution.getProbVariables());

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
