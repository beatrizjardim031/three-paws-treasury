package com.pluralsight;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    //fields, final means once they're set in the constructor, they cannot be changed
    private final LocalDate date;
    private final LocalTime time;
    private final String description;
    private final String vendor;
    private final double amount;

    //constructor
    public Transaction(LocalDate date, LocalTime time, String description, String vendor, double amount) {
        this.date = date;
        this.time = time;
        this.description = description;
        this.vendor = vendor;
        this.amount = amount;
    }

    //getters (I removed the setters because real accounting apps don't allow editing, transactions are immutable)
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

    // this method converts a transaction object into one line of text for the CSV file
    public String toCsv() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = this.time.format(fmt);

        return String.format("%s|%s|%s|%s|$%.2f%n", this.date, formattedTime, this.description, this.vendor, this.amount);
    }
}
