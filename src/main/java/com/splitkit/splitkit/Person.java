package com.splitkit.splitkit;

import java.util.ArrayList;
import java.util.List;

public class Person {
	private ArrayList<Float> expenses_detailed = new ArrayList<>();
    private float expenses;       /* sum of all expenses */
    
    public String name;
    public float shareInPercent;            /* sip: share in percent*/
    public float residual;      /* difference between expenses and the share */
    public float share;         /* share of the sum of all people*/
    
    public Person(String name, float sip){
        this.name = name;
        this.shareInPercent = sip;
        this.expenses = 0.0f;
        this.residual = 0.0f;
        this.share = 0.0f;
    }
    
    public Person(Person person2copy){
        this.expenses_detailed = person2copy.expenses_detailed;
        this.name = person2copy.name;
        this.shareInPercent = person2copy.shareInPercent;
        this.expenses = person2copy.getExpenses();
        this.residual = person2copy.residual;
        this.share = person2copy.share;
    }
    
    public void addExpense(float expense){
    	expenses_detailed.add(expense);
        computeTotalExpenses();
    }
    
    public void addExpense(float[] expense){
        for (float i: expense){
            expenses_detailed.add(i);
        }
        computeTotalExpenses();
    }

    public void computeShare(float amount){
        this.share = shareInPercent / 100.0f * amount;
    }
    
    public void computeResidual(){
        this.residual = expenses - share;
    }
         
    public String varsToString(){
        return String.format("%nName: %s%nTotal Expense: %.2f%n"
                + "Share in Percent: %.2f%n"
                + "Share: %.2f%n"
                + "Residual: %.2f%n%n", 
                this.name, this.expenses, this.shareInPercent, this.share, this.residual);
    }
    
    public static Person getPerson(String queryPersonName, List<Person> people){
        int id = -1;
        for (int i=0; i < people.size(); i++)
            if (people.get(i).name.equals(queryPersonName)){
                id = i;
                return people.get(id);
            }
        return null;
    }
    
    private void computeTotalExpenses(){
        float sum = 0.0f;
        for (float i: this.expenses_detailed){
            sum += i;
        }
        this.expenses = sum;
    }
    
    /*
    ========== SETTER AND GETTER ==========
    */
    public String getName(){
        return this.name;
    }
    
    public void setName(String name){
        this.name = name;
    }
    
    public float getExpenses(){
        return this.expenses;
    }
    
    public void setExpenses(float expenses){
        this.expenses = expenses;
    }
    
    public float getSip(){
        return this.shareInPercent;
    }
    
    public void setSip(float sip){
        this.shareInPercent = sip;
    }

}
