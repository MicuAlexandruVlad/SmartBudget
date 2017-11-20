package com.example.micua.smartbudget;


class Envelope {
    private String name, budget;
    private int spinnerValue;
    private int updatedBudget;
    private int choice = 0;

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
