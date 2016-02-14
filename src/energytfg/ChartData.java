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
    
    private String name;
    private ArrayList<Double> graphData = new ArrayList<>();

    public ChartData(String name) {
        this.name = name;
    }
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        
        ChartData clone = new ChartData(name);
        clone.setGraphData(graphData);
        return clone;
                
        
    }

    public String toString(){
        
        String aux = "ChartDataName: " + name + "\n"; 
        aux = aux+graphData.toString();
        return aux;  
        
    }
    
}
