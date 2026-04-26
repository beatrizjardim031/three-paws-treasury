package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transaction {
    //fields
    private LocalDate date;
    private LocalTime time;
    private String description;
    private String vendor;
    private double amount;

    //constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //getters (I removed the setters because real accounting apps don't allow editing transactions are immutable)
    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public String getDescription() {
        return description;
    }

    public String getVendor() {
        return vendor;
    }

    public double getAmount() {
        return amount;
    }
}
