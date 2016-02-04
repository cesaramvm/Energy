/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

import java.util.Random;

/**
 *
 * @author Cesar
 */
public class ProblemVariable {
    
    // <editor-fold desc="Class Variables">
    boolean selected; 
    double alfa;
    double beta;
    // </editor-fold>
    
    // <editor-fold desc="Constructor">
    public ProblemVariable(){
        selected=false;
        alfa= 0;
        beta= 0;
    }
    // </editor-fold>
    
    // <editor-fold desc="Getters & Setters">
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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
    // </editor-fold>
        
    // <editor-fold desc="Custom Functions">
    public void randomize() {
        selected=randomBoolean();
        if(selected){
            alfa = -1 + (Math.random()*2);
            beta = -1 + (Math.random()*2);
        }
    }
    
    // <editor-fold desc="Private Functions">
    private boolean randomBoolean(){
        Random random = new Random();
        return random.nextBoolean();
    }
    // </editor-fold>
// </editor-fold>
    
    // <editor-fold desc="Overrided">
    @Override
    public String toString() {
        return "ProblemVariable{" + "selected=" + selected + ", alfa=" + alfa + ", beta=" + beta + "}\n";
    }
    // </editor-fold>
    
}
