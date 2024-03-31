package com.example.kosandra.db.returnSql;

public class SqlBarCharts {
    String type;
    int sumCost;

    public SqlBarCharts(String type, int sumCost) {
        this.type = type;
        this.sumCost = sumCost;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSumCost() {
        return sumCost;
    }

    public void setSumCost(int sumCost) {
        this.sumCost = sumCost;
    }
}
