/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.Problem;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author cvaldes
 */
public abstract class LSEvaluationOptimizer extends EvaluationOptimizer {

    protected List<Integer> paramsIndex = new LinkedList<>();
    private List<Integer> paramsIndexBackup = new LinkedList<>();

    public LSEvaluationOptimizer(int newParts, Problem problem, Random r) {
        super(newParts, problem, r);
        for (Integer i = 0; i <= problem.getNumParams(); i++) {
            paramsIndexBackup.add(i);
        }
        this.restoreParamsIndex();
    }

    public final void restoreParamsIndex() {
        paramsIndex = new LinkedList<>(paramsIndexBackup);
    }

}
