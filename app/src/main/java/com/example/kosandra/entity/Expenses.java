package com.example.kosandra.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/**
 * The Expenses class represents an entity for storing expenses in the database.
 * <p>
 * Each expense record includes a unique identifier, type of expenses, name of expenses,
 * <p>
 * cost, and date of the expense.
 */
@Entity(tableName = "expenses")
public class Expenses {
    @PrimaryKey(autoGenerate = true)
    int id;
    String typeExpenses;
    String nameExpenses;
    int cost;
    LocalDate date;

    public Expenses(String typeExpenses, String nameExpenses, int cost, LocalDate date) {
        this.typeExpenses = typeExpenses;
        this.nameExpenses = nameExpenses;
        this.cost = cost;
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeExpenses() {
        return typeExpenses;
    }

    public void setTypeExpenses(String typeExpenses) {
        this.typeExpenses = typeExpenses;
    }

    public String getNameExpenses() {
        return nameExpenses;
    }

    public void setNameExpenses(String nameExpenses) {
        this.nameExpenses = nameExpenses;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
