/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import java.util.ArrayList;

/**
 *
 * @author Cesar
 */
public class YearInfo {

    private int year;
    private double obj;
    private ArrayList<Double> data = new ArrayList<>();

    //----------------CONSTRUCTOR------------------
    public YearInfo(int year, Double obj) {
        this.year = year;
        this.obj = obj;
    }

    //-------------GETTERS & SETTERS---------------
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public ArrayList<Double> getFullData() {
        return data;
    }

    public Double getData(int n) {
        return data.get(n);
    }

    public void setFullData(ArrayList<Double> data) {
        this.data = data;
    }

    public void setData(int n, Double newData) {
        data.set(n, newData);
    }

    ;

    public double getObj() {
        return obj;
    }

    public void setObj(double obj) {
        this.obj = obj;
    }

    //----------METODO INSERTAR DATOS-------------
    public void insertData(Double newData) {
        data.add(newData);
    }

    ;


    @Override
    public String toString() {
        return "YearInfo{" + "year=" + year + ", obj=" + obj + ", data=" + data.toString() + "}" + "\n";
    }

}
