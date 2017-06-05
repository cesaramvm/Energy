package util.optimizers;

import java.util.Map;

import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;

/**
 * @author C�sar Vald�s
 */
public interface Optimizer {

	public abstract void optimize(MetaSolution solution);

	public abstract double evaluate(Map<Integer, MetaVariable> variables, Double epsi);
}
