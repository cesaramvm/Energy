package util.optimizers;

import java.util.Map;

import metaheuristic.models.MetaSolution;
import metaheuristic.models.MetaVariable;

/**
 * @author César Valdés
 */
public interface Optimizer {

	public abstract void optimize(MetaSolution solution);

	public abstract double evaluate(Map<Integer, MetaVariable> variables, Double epsi);
}
