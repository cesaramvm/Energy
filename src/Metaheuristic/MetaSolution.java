/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Metaheuristic;

import Models.Problem;
import Models.ProblemVariable;
import Models.Solution;
import Models.YearInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Cesar
 */
public class MetaSolution extends Thread {

    private final Problem problem;
    private final int branchIterations;
    private final int parts;
    private final ArrayList<Double> newValueList;
    private final ArrayList<Double> newEpsilonList;

    private Double evaluation;
    private Solution solution;
    private boolean done;

    public MetaSolution(Problem pro, int branchIt, int newParts) throws Exception {
        if (((newParts & 1) == 0)) {
            throw new Exception("Las partes deben ser impar");
        }
        parts = newParts;
        newValueList = this.newRandomList(1.0, newParts);
        newEpsilonList = this.newRandomList(5.0, newParts);
        problem = pro;
        branchIterations = branchIt;
    }

    @Override
    public void run() {
        //System.err.println("Thread # " + Thread.currentThread().getId() + " is doing this task");

        HashMap<Integer, ProblemVariable> newSolVariables = new HashMap<>();
        for (int j = 0; j < problem.getNumParams(); j++) {
            newSolVariables.put(j, new ProblemVariable());
        }
        Double newEpsilon = -5 + (Math.random() * 10);

        evaluation = this.evaluate(newSolVariables, newEpsilon);
        solution = new Solution(newEpsilon, newSolVariables);
        this.solve();
    }

    public void solve() {
        for (int i = 0; i < branchIterations; i++) {
            this.improveSolution();
        }
        done = true;
//        System.err.println("        " + Thread.currentThread().getId() + " END");
    }

    private void improveSolution() {
        //Min + (int)(Math.random() * ((Max - Min) + 1)) max=14 min=0
        int selectedChange = (int) (Math.random() * 15);
        Double newEpsilon;
        HashMap<Integer, ProblemVariable> newProbVariables;

        if (selectedChange == 14) {
            newProbVariables = solution.getProbVariables();
            newEpsilon = this.getNewEpsilon();
        } else {
            newEpsilon = solution.getEpsilon();
            newProbVariables = (HashMap<Integer, ProblemVariable>) solution.getProbVariables().clone();
            Double newValue = this.getNewValue();
            Boolean changeAlpha = new Random().nextBoolean();
            if (changeAlpha) {
                newProbVariables.get(selectedChange).setAlfa(newValue);
            } else {
                newProbVariables.get(selectedChange).setBeta(newValue);
            }

        }
        double newEvaluation = this.evaluate(newProbVariables, newEpsilon);
        if (newEvaluation < evaluation) {
            this.evaluation = newEvaluation;
            this.solution.setEpsilon(newEpsilon);
            this.solution.setProbVariables(newProbVariables);
        }

    }

// <editor-fold desc="Custom Functions">    
    // <editor-fold desc="Private Functions">
    private double evaluate(HashMap<Integer, ProblemVariable> variables, Double epsi) {
        int numYears = this.problem.getYears().size();
        Double aux = 1.0 / numYears;
        Double summation = 0.0;

        for (YearInfo yi : this.problem.getYears().values()) {
            Double objective = yi.getObj();
            Double predictSum = 0.0;
            for (int j = 0; j < yi.getFullData().size(); j++) {
                ProblemVariable solVar = variables.get(j);
                predictSum += solVar.getAlfa() * Math.pow(yi.getData(j), solVar.getBeta());
            }
            Double predict = epsi + predictSum;
            summation += Math.pow(objective - predict, 2);
        }

        double result = aux * summation;

        return result;
    }

    public void printActualSolution() {

        System.out.println("La solucion actual es \n Epsilon: " + solution.getEpsilon() + "\n Variables: " + solution.getProbVariables().toString());

    }

    public Solution result() {
        if (!done) {
            return null;
        } else {
            return solution;
        }
    }
//
//    private double expression() {
//        YearInfo yi = problem.getYears().get(2000);
//        double summation = 0.0;
//
//        for (int i = 0; i < solVariables.size(); i++) {
//            ProblemVariable solVar = solVariables.get(i);
//            Double data = yi.getData(i);
//            summation = summation + solVar.getAlfa() * Math.pow(data, solVar.getBeta());
//        }
//
//        return epsilon + summation;
//    }

    // </editor-fold>
// </editor-fold>
//    private Double newValue(Double value) {
//        ArrayList<Double> numbers = new ArrayList<>();
//        numbers.add(0.0);
//        double aux = (parts - 1) / 2;
//        double part = value / aux;
//        for (int i = 1; i <= aux; i++) {
//            numbers.add(i * part);
//            numbers.add(-(i * part));
//        }
//        long seed = System.nanoTime();
//        Collections.shuffle(numbers, new Random(seed));
//        return numbers.get(0);
//    }
    private ArrayList<Double> newRandomList(Double value, int parts) {
        ArrayList<Double> numbers = new ArrayList<>();
        numbers.add(0.0);
        double aux = (parts - 1) / 2;
        double part = value / aux;
        for (int i = 1; i <= aux; i++) {
            numbers.add(i * part);
            numbers.add(-(i * part));
        }
        long seed = System.nanoTime();
        Collections.shuffle(numbers, new Random(seed));
        return numbers;
    }

    private Double getNewEpsilon() {
        return newEpsilonList.get((new Random()).nextInt(parts));
    }

    private Double getNewValue() {
        return newValueList.get((new Random()).nextInt(parts));
    }

}
