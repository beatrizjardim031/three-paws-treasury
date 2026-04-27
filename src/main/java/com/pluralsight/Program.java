package com.pluralsight;

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalTime;
import java.time.LocalDate;



public class Program {
    static Scanner input = new Scanner(System.in);
    static ArrayList<Transaction> transactions = new ArrayList<>(); // both need to be static because main is static

    public static void main(String[] args) {
        loadTransactions();
        System.out.println("Loaded " + transactions.size() + " transactions!");
        boolean isRunning = true; //controls whether the menu loop keeps running

//        System.out.print("""
//                              /^\__/^\   (`(
//                            _|o o    |_   ) )
//                         *--(((---(((---------------------*
//                         * WELCOME TO THREE PAWS TREASURY *
//                         * -Pet Account Management System *
//                         *--------------------------------*""");

        while (isRunning) { //loops forever until user picks x
            System.out.println("""
                    *----------------------------------------------*
                    *          What would you like to do?          *
                    *             D) Add deposit                   *
                    *             P) Make Payment (Debit)          *
                    *             L) Ledger                        *
                    *             X) Exit                          *
                    *----------------------------------------------*""");
            System.out.print("Choose your option: ");
            String mainCommand = input.nextLine();

            //switch case for checking choices

            switch (mainCommand.toUpperCase()) { //.toUpperCase() converts user input to uppercase
                case "D" -> addDeposit();

                case "P" -> makePayment();

                case "L" -> displayLedger();

                case "X" -> {
                    System.out.println("Until next time!🐾");
                    isRunning = false;

                }
                default -> System.out.println("We don't recognize this character, try again. 🐶");
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
            System.out.println("Could not load transaction file😿");
        }

    }// Reads transactions.csv and fills the 'transactions' ArrayList.

    public static void addDeposit(){

        System.out.print("Enter the description: ");
        String description = input.nextLine();
        System.out.print("Enter the vendor: ");
        String vendor = input.nextLine();

        double amount = 0;
        boolean isValid = false;
      do {
          try {
              System.out.print("Enter the amount: ");
              amount = Double.parseDouble(input.nextLine());

              if (amount > 0) {
                  isValid = true;
              } else {
                  System.out.println("Amount must be pawsitive 🐾");
              }
          } catch (NumberFormatException e) {
              System.out.println("That's not a number😾Please try again");

          }
      }  while (!isValid);
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(fmt);

        transactions.add(new Transaction(date, time, description, vendor,amount));

        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String csv = String.format("%s|%s|%s|%s|%.2f%n", date, formattedTime, description, vendor, amount);
            bufferedWriter.write(csv);



            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Sorry, we could not read your transaction.");
        }
        System.out.println("Deposit recorded!🐾");
    }

    public static void makePayment(){

        System.out.print("Enter the description: ");
        String description = input.nextLine();
        System.out.print("Enter the vendor: ");
        String vendor = input.nextLine();

        double amount = 0;
        boolean isValid = false;
        do {
            try {
                System.out.print("Enter the amount: ");
                amount = Double.parseDouble(input.nextLine());

                if (amount > 0) {
                    isValid = true;
                } else {
                    System.out.println("Amount must be pawsitive 🐾");
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a number😾Please try again");
            }
        }  while (!isValid);
        amount = -amount;
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = time.format(fmt);

        transactions.add(new Transaction(date, time, description, vendor,amount));

        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            String csv = String.format("%s|%s|%s|%s|%.2f%n", date, formattedTime, description, vendor, amount);
            bufferedWriter.write(csv);



            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Sorry, we could not read your transaction.");
        }
        System.out.println("Payment recorded!🐾");
    }

    public static void displayLedger(){
        boolean isLedgerRunning = true;

        while (isLedgerRunning) {
            System.out.println("""
                    *-------------------------------------------------*
                    *          What would you like to see?            *
                    *                  A) All                         *
                    *                  D) Deposits                    *
                    *                  P) Payments                    *
                    *                  R) Reports                     *
                    *                  H) Home                        *
                    *-------------------------------------------------*
                    """);
            System.out.print("Choose your option: ");
            String userCommand = input.nextLine();
            switch (userCommand.toUpperCase()) { //.toUpperCase() converts user input to uppercase
                case "A" -> displayEntries();

                case "D" -> displayDeposits();

                case "P" -> displayPayments();

                case "R" -> {
                    displayReports();

                }

                case "H" -> {
                    System.out.println("Returning to main menu 🐾🐾🐾");
                    isLedgerRunning = false;

                }

                default -> System.out.println("We don't recognize this character, try again. 🐶");
            }
        }

    }
    public static void displayEntries(){

        for(int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            System.out.println("---------------------------------------------------------------------------------------------------");
            System.out.printf("| %s | %s | %-30s | %-25s | %10.2f |%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

        }
        System.out.println("---------------------------------------------------------------------------------------------------");

    }
    public static void displayDeposits(){
        boolean foundDeposits = false;
        for(int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() > 0) {
                System.out.println("---------------------------------------------------------------------------------------------------");
                System.out.printf("| %s | %s | %-30s | %-25s | %10.2f |%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                foundDeposits = true;
            }
        }
        System.out.println("---------------------------------------------------------------------------------------------------");
        if (!foundDeposits) {
            System.out.println("No deposits found");
        }
    }
    public static void displayPayments(){
        boolean foundPayments = false;
        for(int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            if (transaction.getAmount() < 0) {
                System.out.println("---------------------------------------------------------------------------------------------------");
                System.out.printf("| %s | %s | %-30s | %-25s | %10.2f |%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());
                foundPayments = true;
            }
        }
        System.out.println("---------------------------------------------------------------------------------------------------");
        if (!foundPayments) {
            System.out.println("No payments found");
        }
    }
    public static void displayReports(){
        System.out.println("Displaying reports");
    }























}
