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
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usuario
 */
public class LSBIEvaluationOptimizer extends LSEvaluationOptimizer {

    public LSBIEvaluationOptimizer(int parts, Problem problem, Random r) {
        super(parts, problem, r);
    }

    @Override
    public void optimize(Solution solution) {
        Double newEvaluation;

        while (!paramsIndex.isEmpty()) {
            ArrayList<Double> evaluations = new ArrayList<>();
            Integer selectedChange = paramsIndex.get(random.nextInt(paramsIndex.size()));
            Double newEpsilon = solution.getEpsilon();
            HashMap<Integer, ProblemVariable> newProbVariables = this.cloneMap(solution.getProbVariables());

            if (selectedChange == 14) {
                for (Double d : epsilonList) {
                    evaluations.add(this.evaluate(newProbVariables, d));
                }
                int minIndex = evaluations.indexOf(Collections.min(evaluations));
                newEvaluation = evaluations.get(minIndex);
                newEpsilon = epsilonList.get(minIndex);
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
            
            //System.out.println(paramsIndex);

        }

    }

}
