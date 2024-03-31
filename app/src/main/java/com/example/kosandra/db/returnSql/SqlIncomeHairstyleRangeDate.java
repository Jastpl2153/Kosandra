package com.example.kosandra.db.returnSql;

public class SqlIncomeHairstyleRangeDate {
    private String haircutName;
    private int countHairstyle;
    private int avgPrice;

    public SqlIncomeHairstyleRangeDate(String haircutName, int countHairstyle, int avgPrice) {
        this.haircutName = haircutName;
        this.countHairstyle = countHairstyle;
        this.avgPrice = avgPrice;
    }

    public String getHaircutName() {
        return haircutName;
    }

    public void setHaircutName(String haircutName) {
        this.haircutName = haircutName;
    }

    public int getCountHairstyle() {
        return countHairstyle;
    }

    public void setCountHairstyle(int countHairstyle) {
        this.countHairstyle = countHairstyle;
    }

    public int getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(int avgPrice) {
        this.avgPrice = avgPrice;
    }
}
