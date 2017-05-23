package Util.Optimizers;

import Models.ProblemVariable;
import Models.Solution;
import java.util.HashMap;

/**
 * @author C�sar Vald�s
 */
public interface Optimizer {

    abstract public void optimize(Solution solution);

    abstract public double evaluate(HashMap<Integer, ProblemVariable> variables, Double epsi);
}
