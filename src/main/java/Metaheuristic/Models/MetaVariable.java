package Metaheuristic.Models;

import java.util.Random;

/**
 * @author César Valdés
 */
public class MetaVariable implements Cloneable {
//    boolean selected; 
    private double alfa;
    private double beta;
    // </editor-fold>

    public MetaVariable(Random r) {
//        selected=true;
        alfa = -1 + (r.nextDouble() * 2);
        beta = -1 + (r.nextDouble() * 2);
    }

    public MetaVariable(double number) {
//        selected=true;
        alfa = number;
        beta = number;
    }
//    public boolean isSelected() {
//        return selected;
//    }
//
//    public void setSelected(boolean selected) {
//        this.selected = selected;
//    }
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
//    private boolean randomBoolean(){
//        Random random = new Random();
//        return random.nextBoolean();
//    }
    
    @Override
    public String toString() {
        return "SolutionVariable {" + "alfa=" + alfa + ", beta=" + beta + "}\n";
    }
    // </editor-fold>

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }

}
