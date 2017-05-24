package Util.Optimizers;

import Models.ProblemVariable;
import Models.MetaSolution;
import java.util.HashMap;

/**
 * @author César Valdés
 */
public interface Optimizer {

    abstract public void optimize(MetaSolution solution);

    abstract public double evaluate(HashMap<Integer, ProblemVariable> variables, Double epsi);
}
