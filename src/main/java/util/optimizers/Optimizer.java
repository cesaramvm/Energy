package util.optimizers;

import java.util.HashMap;

import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;

/**
 * @author C�sar Vald�s
 */
public interface Optimizer {

	abstract public void optimize(MetaSolution solution);

	abstract public double evaluate(HashMap<Integer, MetaVariable> variables, Double epsi);
}
