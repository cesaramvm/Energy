/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class LSFIEvaluationOptimizer extends EvaluationOptimizer {

    private ArrayList<Double> valueListCopy;
    private ArrayList<Double> epsilonListCopy;

    public LSFIEvaluationOptimizer(int parts, Problem problem, Random r) {
        super(parts, problem, r);
    }

    @Override
    public void optimize(Solution solution) {

        valueListCopy = new ArrayList<>(valueList);
        epsilonListCopy = new ArrayList<>(epsilonList);
        double newEvaluation = solution.getEvaluation();
        //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
        int selectedChange = (int) (random.nextDouble() * 15);
        Double newEpsilon = solution.getEpsilon();
        HashMap<Integer, ProblemVariable> newProbVariables = solution.getProbVariables();
        if (selectedChange != 14) {
            newProbVariables = this.cloneMap(solution.getProbVariables());
        }
        while (newEvaluation >= solution.getEvaluation() && (!valueListCopy.isEmpty() && !epsilonListCopy.isEmpty())) {
            if (selectedChange == 14) {
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
            newEvaluation = this.evaluate(newProbVariables, newEpsilon);
        }

        solution.setEpsilon(newEpsilon);
        solution.setProbVariables(newProbVariables);
        solution.setEvaluation(newEvaluation);

    }

    @Override
    protected Double getNewEpsilon() {
        Double newEpsilon = epsilonListCopy.get(random.nextInt(epsilonListCopy.size()));
        epsilonListCopy.remove(newEpsilon);
        return newEpsilon;
    }

    @Override
    protected Double getNewValue() {
        Double newValue = valueListCopy.get(random.nextInt(valueListCopy.size()));
        valueListCopy.remove(newValue);
        return newValue;
    }

}
