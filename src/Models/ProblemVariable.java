/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Cesar
 */
public class ProblemVariable implements Cloneable{

    // <editor-fold desc="Class Variables">
//    boolean selected; 
    private double alfa;
    private double beta;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public ProblemVariable() {
//        selected=true;
        alfa = -1 + (Math.random() * 2);
        beta = -1 + (Math.random() * 2);
    }
    
    public ProblemVariable(double number) {
//        selected=true;
        alfa = number;
        beta = number;
    }
    // </editor-fold>

    // <editor-fold desc="Getters & Setters">
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
    // </editor-fold>

    // <editor-fold desc="Custom Functions">
    // <editor-fold desc="Private Functions">
//    private boolean randomBoolean(){
//        Random random = new Random();
//        return random.nextBoolean();
//    }
    // </editor-fold>
// </editor-fold>
    // <editor-fold desc="Overrided">
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
