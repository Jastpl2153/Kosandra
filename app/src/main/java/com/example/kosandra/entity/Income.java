package com.example.kosandra.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/**
 * Entity class representing a record of income details.
 */
@Entity(tableName = "income")
public class Income {
    @PrimaryKey(autoGenerate = true)
    int id;
    String typeIncome;
    String nameIncome;
    int cost;
    LocalDate date;

    public Income(String typeIncome, String nameIncome, int cost, LocalDate date) {
        this.typeIncome = typeIncome;
        this.nameIncome = nameIncome;
        this.cost = cost;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeIncome() {
        return typeIncome;
    }

    public void setTypeIncome(String typeIncome) {
        this.typeIncome = typeIncome;
    }

    public String getNameIncome() {
        return nameIncome;
    }

    public void setNameIncome(String nameIncome) {
        this.nameIncome = nameIncome;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
