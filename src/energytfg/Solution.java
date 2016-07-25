/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package energytfg;

// <editor-fold desc="">
// </editor-fold>

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Cesar
 */
public class Solution {
    
// <editor-fold desc="Class Variables">    
    private HashMap<Integer,ProblemVariable> pVariables = new HashMap<>();
    private double epsilon;
    private Problem problem;
// </editor-fold>
    
// <editor-fold desc="Constructor">
    
    public Solution(Problem pro){
        problem = pro;
        for (Integer clave : pro.getYears().keySet()) {
            pVariables.put(clave, new ProblemVariable());
        }
        epsilon = -5 + (Math.random()*10);
        
    }
// </editor-fold>
    
// <editor-fold desc="Getters & Setters">

    public HashMap<Integer, ProblemVariable> getpVariables() {
        return pVariables;
    }

    public void setpVariables(HashMap<Integer, ProblemVariable> pVariables) {
        this.pVariables = pVariables;
    }

    public ProblemVariable get(String key) {
        return pVariables.get(key);
    }

    public double getEpsilon() {
        return epsilon;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public Problem getProblem() {
        return problem;
    }

    public void setProblem(Problem problem) {
        this.problem = problem;
    }
    
// </editor-fold>

// <editor-fold desc="Custom Functions">    
    
        public double solve (){

////        double e = -5 + (Math.random()*10);
//////        System.out.println("e vale: " + e);
////        for (int i=0; i<years.size(); i++){
//////            System.out.println(i);
////            ArrayList<Double> datas = years.get(i).getFullData();
////            results.add(expression(e,datas));
////        }
////        
//        
//        return new Solution();
//    }
        randomize();
        return evaluate();
        
        //return MAE
        //weka
        //libsvm
        //smo
        //www3.ntu.edu.sg/home/egbhuang/elm_codes.html
        //metodos seleccion de características
        //clasificador para regresion 
        //red de neuronas caffe
        //en c hay libsvm
        //analizar la calidad, en tiempo de entrenamiento y error de clasificación
        //pybrain blackbox +++ (PSO y GA)
        //primero ELM y y despues MLP (neuronal)
        
    }
        
     // <editor-fold desc="Private Functions">

    
    private void randomize() {
        Iterator it = pVariables.values().iterator();
        while (it.hasNext()){
            
            ProblemVariable pv = (ProblemVariable) it.next();
            pv.randomize();   
        }
    }
    
    private double evaluate(){
        double result = 0.0;
        
        return result;
    }

    private double expression (double e, ArrayList<Double> datas){
        double summation= 0.0;
        double a = -1 + (Math.random()*2);
        double b = -1 + (Math.random()*2);
        
//        System.out.println("a " + a);
//        System.out.println("b " + b);
        
        for (double data : datas) {
            summation = summation + a*Math.pow(data,b);
        }
        
        return e+summation;
    }
    
    // </editor-fold>
        
// </editor-fold>

// <editor-fold desc="Overrided">
    @Override
    public String toString() {
        return "Solution{" + "epsilon=" + epsilon + ", pVariables=" + pVariables + '}';
    }
// </editor-fold>
    
}
