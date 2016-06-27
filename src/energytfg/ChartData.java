/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.util.ArrayList;

/**
 *
 * @author sobremesa
 */
public class ChartData implements Cloneable{
    
    private String learningRate;
    private String transferType;
    private ArrayList<Double> graphData = new ArrayList<>();

    public ChartData(String learningRate, String transferType) {
        this.learningRate = learningRate;
        this.transferType = transferType;
    }

    public String getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(String learningRate) {
        this.learningRate = learningRate;
    }

    public String getTransferType() {
        return transferType;
    }

    public void setTransferType(String transferType) {
        this.transferType = transferType;
    }
    
    public ArrayList<Double> getGraphData() {
        return graphData;
    }

    public void setGraphData(ArrayList<Double> graphData) {
        this.graphData = graphData;
    }

    public Double get(int index) {
        return graphData.get(index);
    }

    public boolean add(Double e) {
        return graphData.add(e);
    }
    
    @Override
    public ChartData clone(){
        
        ChartData clone = new ChartData(learningRate, transferType);
        clone.setGraphData(graphData);
        return clone;
                
        
    }

    public String toString(){
        
        String aux = "ChartData: LR: " + learningRate + " TF: " + transferType + "\n"; 
        aux = aux+graphData.toString();
        return aux;  
        
    }
    
}
