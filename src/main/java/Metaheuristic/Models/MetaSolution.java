package Metaheuristic.Models;

import java.util.HashMap;

/**
 * @author César Valdés
 */
public class MetaSolution implements Comparable<MetaSolution> {

    private double epsilon;
    private HashMap<Integer, ProblemVariable> probVariables = new HashMap<>();
    private double evaluation;
    private Long executionTime;

    public MetaSolution(double epsilon, HashMap<Integer, ProblemVariable> probVariables, double evaluation) {
        this.epsilon = epsilon;
        this.probVariables = probVariables;
        this.evaluation = evaluation;
    }

    public MetaSolution(int probVariablesSize) {
        epsilon = 0.0;
        for (int i = 0; i < probVariablesSize; i++) {
            probVariables.put(i, new ProblemVariable(0.0));

        }
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public HashMap<Integer, ProblemVariable> getProbVariables() {
        return probVariables;
    }

    public void setProbVariables(HashMap<Integer, ProblemVariable> probVariables) {
        this.probVariables = probVariables;
    }

    public double getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(double evaluation) {
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
        return "Solution{" + "epsilon=" + epsilon + ", probVariables=" + probVariables + ", evaluation=" + evaluation + ", executionTime=" + executionTime + '}';
    }

    @Override
    public int compareTo(MetaSolution other) {
        return Double.compare(this.getEvaluation(), other.getEvaluation());
    }

}
