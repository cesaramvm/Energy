package metaheuristic.models;

import java.util.Random;

/**
 * @author César Valdés
 */
public class MetaVariable{
	private double alfa;
	private double beta;

	public MetaVariable(Random r) {
		alfa = -1 + (r.nextDouble() * 2);
		beta = -1 + (r.nextDouble() * 2);
	}

	public MetaVariable(MetaVariable metavariable) {
		alfa = metavariable.getAlfa();
		beta = metavariable.getBeta();
	}

	public MetaVariable(double number) {
		alfa = number;
		beta = number;
	}
	
	public double getAlfa() {
		return alfa;
	}

	public void setAlfa(double alfa) {
		this.alfa = alfa;
	}

	public double getBeta() {
		return beta;
	}

	public void setBeta(double beta) {
		this.beta = beta;
	}

	@Override
	public String toString() {
		return "SolutionVariable {" + "alfa=" + alfa + ", beta=" + beta + "}\n";
	}

}
