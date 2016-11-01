/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Util.Optimizers;

import Models.ProblemVariable;
import Models.Solution;
import java.util.HashMap;

/**
 *
 * @author Usuario
 */
public interface Optimizer {
    abstract public void optimize(Solution solution);
    abstract public double evaluate(HashMap<Integer, ProblemVariable> variables, Double epsi);
}
