package metaheuristic.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author César Valdés
 */
public class MetaSolution implements Comparable<MetaSolution> {

	private Double epsilon;
	private Map<Integer, MetaVariable> probVariables = new HashMap<>();
	private Double evaluation;
	private Long executionTime;

	public MetaSolution(Double epsilon, Map<Integer, MetaVariable> probVariables, Double evaluation) {
		this.epsilon = epsilon;
		this.probVariables = probVariables;
		this.evaluation = evaluation;
	}

	public MetaSolution(int probVariablesSize) {
		epsilon = 0.0;
		for (int i = 0; i < probVariablesSize; i++) {
			probVariables.put(i, new MetaVariable(0.0));

		}
	}

	public Double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(Double epsilon) {
		this.epsilon = epsilon;
	}

	public Map<Integer, MetaVariable> getProbVariables() {
		return probVariables;
	}

	public void setProbVariables(Map<Integer, MetaVariable> probVariables) {
		this.probVariables = probVariables;
	}

	public Double getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(Double evaluation) {
		this.evaluation = evaluation;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	@Override
	public String toString() {
		return "Solution{" + "epsilon=" + epsilon + ", probVariables=" + probVariables + ", evaluation=" + evaluation
				+ ", executionTime=" + executionTime + '}';
	}

	@Override
	public int compareTo(MetaSolution other) {
		return Double.compare(this.getEvaluation(), other.getEvaluation());
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof MetaSolution){
			MetaSolution ms = (MetaSolution) other;
			return this.epsilon == ms.getEpsilon() && this.evaluation == ms.getEvaluation() && this.executionTime == ms.executionTime && this.probVariables.equals(ms.getProbVariables());
		} else {
			return false;
		}
		
	}

	@Override
	public int hashCode(){
	    return Objects.hash(this.epsilon, this.evaluation, this.executionTime, this.probVariables);
	}
	

}
