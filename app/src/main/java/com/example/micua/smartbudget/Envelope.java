package com.example.micua.smartbudget;


import java.util.ArrayList;
import java.util.List;

class Envelope {
    private String name, budget;
    private int spinnerValue;
    private int updatedBudget;
    private int choice = 0;
    private List<Float> budgetTrans = new ArrayList<>();
    private List<Float> totalBudget = new ArrayList<>();
    private List<String> dateTrans = new ArrayList<>(), nameTrans = new ArrayList<>();
    private String type = "Distribution";

    public Envelope(String name, String budget, int spinnerValue) {
        this.name = name;
        this.budget = budget;
        this.spinnerValue = spinnerValue;
        this.updatedBudget = Integer.valueOf(budget);

    }

    public Envelope(String name, String budget) {
        this.name = name;
        this.budget = budget;
        this.updatedBudget = Integer.valueOf(budget);
    }

    private void init() {
        nameTrans.add("Periodic Fill");
        totalBudget.add(Float.valueOf(budget));
        budgetTrans.add(Float.valueOf(budget));
        dateTrans.add("00/00");
    }

    public void addBudgetTrans(float budget) {
        this.budgetTrans.add(budget);
    }

    public void addDateTrans(String date) {
        this.dateTrans.add(date);
    }

    public void addTotalBudget(float budget) {
        this.totalBudget.add(budget);
    }

    public void addNameTrans(String name) {
        this.nameTrans.add(name);
    }

    public List<Float> getBudgetTrans() {
        return budgetTrans;
    }

    public void setBudgetTrans(List<Float> budgetTrans) {
        this.budgetTrans = budgetTrans;
    }

    public List<Float> getTotalBudget() {
        return totalBudget;
    }

    public void setTotalBudget(List<Float> totalBudget) {
        this.totalBudget = totalBudget;
    }

    public List<String> getDateTrans() {
        return dateTrans;
    }

    public void setDateTrans(List<String> dateTrans) {
        this.dateTrans = dateTrans;
    }

    public List<String> getNameTrans() {
        return nameTrans;
    }

    public void setNameTrans(List<String> nameTrans) {
        this.nameTrans = nameTrans;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getUpdatedBudget() {
        return updatedBudget;
    }

    public void setUpdatedBudget(int updatedBudget) {
        this.updatedBudget = updatedBudget;
    }

    public int getChoice() {
        return choice;
    }

    public void setChoice(int choice) {
        this.choice = choice;
    }

    public String getName() {
        return name;
    }

    public String getBudget() {
        return budget;
    }

    public int getSpinnerValue() {
        return spinnerValue;
    }

    public void setSpinnerValue(int spinnerValue) {
        this.spinnerValue = spinnerValue;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }


}
