package com.example.kosandra.db.returnSql;

public class SqlIncomeHairstyle {
    private String haircutName;
    private int cost;

    public SqlIncomeHairstyle(String haircutName, int cost) {
        this.haircutName = haircutName;
        this.cost = cost;
    }

    public String getHaircutName() {
        return haircutName;
    }

    public void setHaircutName(String haircutName) {
        this.haircutName = haircutName;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
