package Util.Optimizers;

import java.util.HashMap;

import Metaheuristic.Models.MetaSolution;
import Metaheuristic.Models.MetaVariable;

/**
 * @author César Valdés
 */
public interface Optimizer {

    abstract public void optimize(MetaSolution solution);

    abstract public double evaluate(HashMap<Integer, MetaVariable> variables, Double epsi);
}
