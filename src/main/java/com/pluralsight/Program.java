package com.pluralsight;

import java.io.*;
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
                    ╚----------------------------------------------╝
                    """);
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
            String line = bufferedReader.readLine(); // reads the first line from the file

            while(line != null) {
                if (!line.trim().isEmpty()){ //.trim() removes leading/trailing whitespace, only process if the line it's not empty
                    String[] parts = line.split("\\|");
                    LocalDate date = LocalDate.parse(parts[0]);
                    LocalTime time = LocalTime.parse(parts[1]);
                    String description = parts[2];
                    String vendor = parts[3];
                    double amount = Double.parseDouble(parts[4]);

                    transactions.add(new Transaction(date, time, description, vendor, amount)); // creates a new transaction object and adds it to the list

                }

                line = bufferedReader.readLine(); // reads the next line
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Could not load transaction file😿");
        }

    }

    //this method handles both payments and deposits
    public static void addTransaction(boolean isDeposit) {
        String description = askForText("Enter the description: ");
        String vendor = askForText("Enter the vendor: ");
        double amount = getPositiveAmount();

        //IF it's a payment, flip the amount to negative
        if (!isDeposit) {
            amount = -amount;
        }
        LocalDate date = LocalDate.now();
        LocalTime time = LocalTime.now();
        LocalTime cleanTime = time.withNano(0); // removes nanoseconds

        // creates the object and add it to arrayList
        Transaction transaction = new Transaction(date, cleanTime, description, vendor, amount);
        transactions.add(transaction);

        //save it to file
        boolean saved = saveTransaction(transaction);
        if (saved) {
            if (isDeposit) {
                System.out.println("\nDeposit recorded!🐾\n");

            } else {
                System.out.println("\nPayment recorded!🐾\n");
            }
        } else {
            System.out.println("Failed to save");
        }
    }

    // a loop for checking the history
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
                    System.out.println("""
                                         ╔────────────────────────────────╗
                                         * Returning to main menu 🐾🐾🐾 *
                                         ╚────────────────────────────────╝
                                  """);
                    isLedgerRunning = false;
                }
                default -> System.out.println("We don't recognize this character, try again. 🐶");
            }
        }
    }

    // display transactions depending on choice
    public static void displayTransactions (String choice){
        boolean isFound = false;
        // loop goes backwards so the newer transactions shows first
        for(int i = transactions.size()-1;i >= 0; i--) {
            boolean shouldPrint = false;
            Transaction transaction = transactions.get(i); // gets the current transaction at index i

            if (choice.equalsIgnoreCase("All")) {
                shouldPrint = true;
            }
            else if (choice.equalsIgnoreCase("Deposit") && transaction.getAmount() > 0) {
                shouldPrint = true; //shows only positive numbers
            }
            else if (choice.equalsIgnoreCase("Payment") && transaction.getAmount() < 0) {
                shouldPrint = true;// shows only negative numbers
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

    // opens reports menu
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
                case "1" -> displayReportsByDate("Month To Date");
                case "2" -> displayReportsByDate("Previous Month");
                case "3" -> displayReportsByDate("Year To Date");
                case "4" -> displayReportsByDate("Previous Year");
                case "5" -> searchByVendor();
                case "6" -> customSearch();
                case "0" -> {
                    System.out.println("""
                                     ╔───────────────────────────────────╗
                                     * Going back to Ledger page 🐾🐾🐾 *
                                     ╚───────────────────────────────────╝
                             """);
                    isReportsRunning = false;
                }
                default -> System.out.println("We don't recognize this character, try again. 🐶");
            }
        }
    }
    // displays reports based on a date category
    public static void displayReportsByDate(String dateType) {
        boolean isFound = false; // tracks if any of the matching dateTypes was found
        header(dateType);
        for (int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);
            boolean match = switch (dateType) {
                case "Month To Date" -> isMonthToDate(transaction); // check if the transaction is from this date
                case "Previous Month" -> isPreviousMonth(transaction);
                case "Year To Date" -> isYearToDate(transaction);
                case "Previous Year" -> isPreviousYear(transaction);
                default -> false; // the date type doesn't match with anything
            };

            if (match) { // if transaction matches the report:
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }
    // search transactions by vendor's name
    public static void searchByVendor(){
        header("Search By Vendor");
        String vendorName = askForText("Enter vendor name: ");
        boolean isFound = false; // tracks if any matching vendor was found

        for (int i = transactions.size()-1;i >= 0; i--){
            Transaction transaction = transactions.get(i);// get current transaction
            if (transaction.getVendor().toLowerCase().contains(vendorName.toLowerCase())) { // checks if the transaction contains what the user typed
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }

    public static void customSearch(){
        header("Custom Search");

        System.out.println("Press Enter to skip a filter 🐶");
        String startDateUser = askForText("Start date (YYYY-MM-DD): ");
        String endDateUser = askForText("End Date (YYYY-MM-DD): ");
        String description = askForText("Description: ");
        String vendor = askForText("Vendor: ");
        String amountUser = askForText("Amount: ");

        // I used null to mean the user skipped this line, I didn't use "" because the data types below are not Strings
        boolean isFound = false;
        LocalDate startDate = null;
        LocalDate endDate = null;
        Double amount = null; // Double with D because primitive double can't be null

        try {
            if (!startDateUser.isEmpty()) { // if user typed a start date convert it into a LocalDate, if they skipped leave it as null
                startDate = LocalDate.parse(startDateUser);
            }
            if (!endDateUser.isEmpty()) {
                endDate = LocalDate.parse(endDateUser);
            }
            if (!amountUser.isEmpty()) {
                amount = Double.parseDouble(amountUser);
            }
        } catch (Exception e) {
            System.out.println("Please input the correct information 😺");
            return; // even though it's a void method it will stop executing this method immediately
        }
        for (int i = transactions.size()-1;i >= 0; i--) {
            Transaction transaction = transactions.get(i);

            if (matchesCustomSearch(transaction, startDate, endDate, description, vendor, amount)) {
                printTransaction(transaction);
                isFound = true;
            }
        }
        if (!isFound) {
            System.out.println("No matches found 😿");
        }
    }


    // helper/reusable methods
    public static void printTransaction(Transaction transaction) {
        System.out.printf("| %s | %s | %-30s | %-25s | $%10.2f |%n", transaction.getDate(), transaction.getTime(), transaction.getDescription(), transaction.getVendor(), transaction.getAmount());

    }

    public static String askForText(String prompt) {
        System.out.print(prompt);
        return input.nextLine();
    }

    public static double getPositiveAmount() { // keeps asking until the user enter a positive number
        double amount = 0; // store the amount
        boolean isValid = false; // controls the loop
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
        }  while (!isValid); // repeat the loop until input is NOT valid

        return amount; // returns the positive amount
    }

    public static boolean saveTransaction(Transaction transaction) { // this method saves one transaction to the csv file

        try {
            FileWriter fileWriter = new FileWriter("transactions.csv", true); // true means append the mode, so it won't overwrite
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(transaction.toCsv());

            bufferedWriter.close();
            return true;
        } catch (IOException e) {
            System.out.println("Sorry, we could not read your transaction.");
            return false;
        }
    }

    public static void header(String title) {
        System.out.println("═════════════════════════════════════════ " + title + " ════════════════════════════════════════════");
    }

    // date helper methods for displayReportsByDate()
    public static boolean isMonthToDate (Transaction transaction) { //checks if a transaction happened in the current month and current year
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = transaction.getDate();

        return today.getYear() == transactionDate.getYear() && today.getMonthValue() == transactionDate.getMonthValue();
    }

    public static boolean isPreviousMonth(Transaction transaction) {
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = transaction.getDate();
        LocalDate lastMonth = today.minusMonths(1); // gets the date one month before today

        return transactionDate.getYear() == lastMonth.getYear() && transactionDate.getMonthValue() == lastMonth.getMonthValue();
    }

    public static boolean isYearToDate(Transaction transaction) {
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = transaction.getDate();

        return today.getYear() == transactionDate.getYear();
    }

    public static boolean isPreviousYear(Transaction transaction) {
        LocalDate today = LocalDate.now();
        LocalDate transactionDate = transaction.getDate();
        LocalDate lastYear = today.minusYears(1);

        return transactionDate.getYear() == lastYear.getYear();
    }

    // this method checks one transaction against all custom search filters
    public static boolean matchesCustomSearch(Transaction transaction, LocalDate startDate, LocalDate endDate, String description, String vendor, Double amount) {
            if (startDate != null && transaction.getDate().isBefore(startDate)) {
                return false;
            }
            if (endDate != null && transaction.getDate().isAfter(endDate)) {
                return false;
            }
            if (!description.isEmpty() && !transaction.getDescription().toLowerCase().contains(description.toLowerCase())) {
                return false;
            }
            if (!vendor.isEmpty() && !transaction.getVendor().toLowerCase().contains(vendor.toLowerCase())) {
                return false;
            }
            if (amount != null && transaction.getAmount() != amount) {
                return false; // it returns false if it fails in any of the filters above
            }
            return true; // it returns true if transaction matches

    }

}
