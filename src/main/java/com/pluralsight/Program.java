package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.LocalDate;



public class Program {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>(); // both need to be static because main is static

    public static void main(String[] args) {
        loadTransactions();
        System.out.println("Loaded " + transactions.size() + " transactions!");
        boolean isRunning = true;

//        System.out.print("""
//                              /^\__/^\   (`(
//                            _|o o    |_   ) )
//                         *--(((---(((---------------------*
//                         * WELCOME TO THREE PAWS TREASURY *
//                         * -Pet Account Management System *
//                         *--------------------------------*""");

        while (isRunning) {
            System.out.println("""
                    *----------------------------*
                    * What would you like to do? *
                    * D) Add deposit             *
                    * P) Make Payment (Debit)    *
                    * L) Ledger                  *
                    * X) Exit                    *
                    *----------------------------*""");
            System.out.print("Choose your option: ");
            String mainCommand = input.nextLine();

            //switch case for checking choices

            switch (mainCommand.toUpperCase()) {
                case "D" -> addDeposit();
                case "P" -> makePayment();
                case "L" -> displayLedger();
                case "X" -> {
                    System.out.println("Until next time!🐾");
                    isRunning = false;
                }
                default -> System.out.println("We don't recognize this character, try again.");
            }
        }
    }
    //methods
    public static void loadTransactions(){

        try {
            FileReader filereader = new FileReader("transactions.csv");
            BufferedReader bufferedReader = new BufferedReader(filereader);
            String line = bufferedReader.readLine();

            while(line != null) {
                if (!line.trim().isEmpty()){ //.trim() removes leading/trailing whitespace
                    String[] parts = line.split("\\|");
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    transactions.add(new Transaction(date, time, description, vendor, amount));

                }

                line = bufferedReader.readLine();
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Could not load transaction file");
        }



    }

    public static void addDeposit(){
        System.out.println("prompt user for the deposit information and save it to the csv file");
    }

    public static void makePayment(){
        System.out.println("Make payment");
    }

    public static void displayLedger(){
        System.out.println("display the ledger screen");
        System.out.println("account history method");
    }
}
