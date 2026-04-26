package com.pluralsight;

import java.util.ArrayList;
import java.util.Scanner;

public class Program {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>(); // both need to be static because main is static

    public static void main(String[] args) {
//        loadTransactions();

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
        System.out.println("Loading Transactions");
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
