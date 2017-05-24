package Metaheuristic.Models;

/**
 * @author César Valdés
 */
public class MetaResults {

    private final MetaSolution bestSolution;
    private final Long totalSecuentialTime;
    private final Long totalConcurrentTime;
    private final Double avgError;
    private final Long avgTime;

    public MetaResults(MetaSolution bestSolution, Long totalSecuentialTime, Long totalConcurrentTime, Long avgTime, Double avgError) {
        this.bestSolution = bestSolution;
        this.totalSecuentialTime = totalSecuentialTime;
        this.totalConcurrentTime = totalConcurrentTime;
        this.avgError = avgError;
        this.avgTime = avgTime;
    }

    public MetaSolution getBestSolution() {
        return bestSolution;
    }

    public Long getTotalSecuentialTime() {
        return totalSecuentialTime;
    }

    public Long getTotalConcurrentTime() {
        return totalConcurrentTime;
    }

    public Double getAvgError() {
        return avgError;
    }

    public Long getAvgTime() {
        return avgTime;
    }

    @Override
    public String toString() {
        return "MetaResults{" + "bestSolution=" + bestSolution + ", totalSecuentialTime=" + totalSecuentialTime + ", totalThreadedTime=" + totalConcurrentTime + ", avgError=" + avgError + ", avgTime=" + avgTime + '}';
    }

}
