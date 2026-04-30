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
        boolean isRunning = true; //controls whether the menu loop keeps running

        System.out.print("""
                                                 |\\__/'|   (`(
                                              _.|o o   |_   ) )
                                        *--(((---(((---------------------*
                                        * WELCOME TO THREE PAWS TREASURY *
                                        * -Pet Account Management System *
                                        *--------------------------------*
                                 """);

        while (isRunning) { //loops forever until user picks x
            System.out.println("""
                    ╔----------------------------------------------╗
                    │          What would you like to do?          │
                    │             D) Add deposit                   │
                    │             P) Make Payment (Debit)          │
                    │             L) Ledger                        │
                    │             X) Exit                          │
                    ╚----------------------------------------------╝""");
            System.out.print("Choose your option: ");
            String mainCommand = input.nextLine();

            //switch case for checking choices

            switch (mainCommand.toUpperCase()) { //.toUpperCase() converts user input to uppercase
                case "D" -> addTransaction(true);

                case "P" -> addTransaction(false);

                case "L" -> displayLedger();

                case "X" -> {
                    System.out.print("""
                                                /\\___/\\
                                                `)9 9('
                                                {_:Y:.}_
                            *------------------( ) U-'( )--------------------*
                            *                   ```   ```                    *
                            *     Thank you for using Three Paws Treasury!   *
                            *               Until next time!🐾               *
                            *                                                *
                            *------------------------------------------------*
                            """);
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

    public static void addTransaction(boolean isDeposit) {
        String description = askForText("Enter the description: ");
        String vendor = askForText("Enter the vendor: ");
        double amount = getPositiveAmount();

        if (!isDeposit) {
            amount = -amount;
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalTime cleanTime = time.withNano(0);

        Transaction transaction = new Transaction(date, cleanTime, description, vendor, amount);
        transactions.add(transaction);

        boolean saved = saveTransaction(transaction);
        if (saved) {
            if (isDeposit) {
                System.out.println("Deposit recorded!🐾");

            } else {
                System.out.println("Payment recorded!🐾");
            }
        } else {
            System.out.println("Failed to save");
        }
    }

    public static void displayLedger(){
        boolean isLedgerRunning = true;

        while (isLedgerRunning) {
            System.out.println("""            
                    ╔─────────────────────────────────────────────────╗
                    │          What would you like to see?            │
                    │                  A) All                         │
                    │                  D) Deposits                    │
                    │                  P) Payments                    │
                    │                  R) Reports                     │
                    │                  H) Home                        │
                    ╚─────────────────────────────────────────────────╝
                    """);
            System.out.print("Choose your option: ");
            String userCommand = input.nextLine();
            switch (userCommand.toUpperCase()) { //.toUpperCase() converts user input to uppercase
                case "A" -> displayTransactions("All");

                case "D" -> displayTransactions("Deposit");

                case "P" -> displayTransactions("Payment");

                case "R" -> displayReports();

                case "H" -> {
                    System.out.println("Returning to main menu 🐾🐾🐾");
                    isLedgerRunning = false;
                }
                default -> System.out.println("We don't recognize this character, try again. 🐶");
            }
        }
    }

    public static void displayTransactions (String choice){
        boolean isFound = false;

        for(int i = transactions.size()-1;i >= 0; i--) {
            boolean shouldPrint = false;
            Transaction transaction = transactions.get(i);

            if (choice.equalsIgnoreCase("All")) {
                shouldPrint = true;
            }
            else if (choice.equalsIgnoreCase("Deposit") && transaction.getAmount() > 0) {
                shouldPrint = true;
            }
            else if (choice.equalsIgnoreCase("Payment") && transaction.getAmount() < 0) {
                shouldPrint = true;
            }
            if (shouldPrint){
                System.out.println("---------------------------------------------------------------------------------------------------");
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        } else {
            System.out.println("---------------------------------------------------------------------------------------------------");
        }
    }

    public static void displayReports(){
        boolean isReportsRunning = true;

        while(isReportsRunning) {
            System.out.println("""
                     ╔─────────────────────────────────────────────────╗
                     │        How would you like to search by?         │
                     │                1) Month To Date                 │
                     │                2) Previous Month                │
                     │                3) Year To Date                  │
                     │                4) Previous Year                 │
                     │                5) Search by Vendor              │
                     │                6) Custom Search                 │
                     │                0) Back                          │
                     ╚─────────────────────────────────────────────────╝
                    """);
            System.out.print("Choose your option: ");
            String userCommand = input.nextLine();

            switch(userCommand) {
                case "1" -> monthToDate();
                case "2" -> previousMonth();
                case "3" -> yearToDate();
                case "4" -> previousYear();
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> {
                    System.out.println("Going back to ledger page 🐾🐾🐾");
                    isReportsRunning = false;
                }
                default -> System.out.println("We don't recognize this character, try again. 🐶");
            }
        }
    }

    public static void monthToDate(){
        LocalDate today = LocalDate.now();
        boolean isFound = false;
        System.out.println("══════════════════════════════════════════ Month To Date ═══════════════════════════════════════════════");
        for (int i = transactions.size()-1;i >= 0; i--){
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == today.getYear() && transactionDate.getMonthValue() == today.getMonthValue()) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void previousMonth(){
        LocalDate today = LocalDate.now();
        LocalDate lastMonth = today.minusMonths(1);
        boolean isFound = false;

        System.out.println("═════════════════════════════════════════ Previous Month ═════════════════════════════════════════════");
        for (int i = transactions.size()-1;i >= 0; i--){
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == lastMonth.getYear() && transactionDate.getMonthValue() == lastMonth.getMonthValue()) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void yearToDate(){
        LocalDate today = LocalDate.now();
        boolean isFound = false;
        System.out.println("═════════════════════════════════════════ Year To Date ═════════════════════════════════════════════");
        for (int i = transactions.size()-1;i >= 0; i--){
            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();

            if (transactionDate.getYear() == today.getYear()) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void previousYear(){
        LocalDate today = LocalDate.now();
        boolean isFound = false;

        System.out.println("═════════════════════════════════════════ Previous Year ════════════════════════════════════════════");
        for (int i = transactions.size()-1;i >= 0; i--){

            Transaction transaction = transactions.get(i);
            LocalDate transactionDate = transaction.getDate();
            if (transactionDate.getYear() == today.getYear() - 1) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void searchByVendor(){
        System.out.println("══════════════════════════════════════════ Search By Vendor ═══════════════════════════════════════════");
        System.out.print("Enter vendor name: " );
        String vendorName = input.nextLine();
        boolean isFound = false;

        for (int i = transactions.size()-1;i >= 0; i--){
            Transaction transaction = transactions.get(i);
            if (transaction.getVendor().toLowerCase().contains(vendorName.toLowerCase())) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void customSearch(){
        System.out.println("══════════════════════════════════════════ Custom Search ═══════════════════════════════════════════");
        System.out.println("Press Enter to skip a filter 🐶");
        System.out.print("Start date (YYYY-MM-DD): ");
        String startDateUser = input.nextLine();
        System.out.print("End Date (YYYY-MM-DD): ");
        String endDateUser = input.nextLine();
        System.out.print("Description: ");
        String description = input.nextLine();
        System.out.print("Vendor: ");
        String vendor = input.nextLine();
        System.out.print("Amount: ");
        String amountUser = input.nextLine();

        boolean isFound = false;

        for (int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            boolean match = true;

            try {
                if (!startDateUser.isEmpty()) {
                    LocalDate startDate = LocalDate.parse(startDateUser);
                    if (transaction.getDate().isBefore(startDate)) {
                        match = false;
                    }
                }
                if (!endDateUser.isEmpty()) {
                    LocalDate endDate = LocalDate.parse(endDateUser);
                    if (transaction.getDate().isAfter(endDate)) {
                        match = false;
                    }
                }
            } catch (Exception e) {
                System.out.println("Please input the correct date 😺");
            }
            if (!description.isEmpty() && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                match = false;
            }
            if (!vendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                match = false;
            }
            try {
                if (!amountUser.isEmpty()) {
                    double amount = Double.parseDouble(amountUser);
                    if (transaction.getAmount() != amount) {
                        match = false;
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("That's not a number 😾 Please try again");
            }
            if (match) {
                printTransaction(transaction);
                isFound = true;
            }
        } if (!isFound) { System.out.println("No matches found 😿");}
    }

    public static void printTransaction(Transaction transaction) {
        System.out.printf("| %s | %s | %-30s | %-25s | $%10.2f |%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

    }

    public static String askForText(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }
    // helper methods
    public static double getPositiveAmount() {
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
        return amount;
    }

    public static boolean saveTransaction(Transaction transaction) {

        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(transaction.toCsv());

            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("Sorry, we could not read your transaction.");
            return false;
        }


    }
}
