package com.company;

import java.sql.Struct;
import java.util.Scanner;

public class ATM {
    public static void main(String[] args) {
        //init scanner
        Scanner sc = new Scanner(System.in);
        // init Bank
        Bank theBank = new Bank("Alfa Bank");

        //add user, which also creates a savings account
        User aUser = theBank.addUser("Mike", "Scot", "1234");
        User bUser = theBank.addUser("Jake", "Scot", "1234");

        //add a checking account for our user
        Account newAccount1 = new Account("Checking", aUser, theBank);
        aUser.addAccount(newAccount1);
        theBank.addAccount(newAccount1);

        Account newAccount2 = new Account("Checking", bUser, theBank);
        bUser.addAccount(newAccount2);
        theBank.addAccount(newAccount2);


        // login

        User curUser;
        while (true) {
            // stay in the login prompt until successful login
            curUser = ATM.mainMenuPrompt(theBank, sc);
            // stay in main menu until user quits
            ATM.printUserMenu(curUser, sc);

        }

    }

    /*
     *Print the ATM's login menu
     * @param theBank        the Bank object whose accounts to use
     * @param sc             the Scanner object to use for user input
     * @return               return valid authorized user
     * */

    public static User mainMenuPrompt(Bank theBank, Scanner sc) {
        // init
        String userID;
        String pin;
        User authUser;

        //prompt the user for user ID/pin combo until a correct one is reached
        do {
            System.out.printf("\n\nWelcome to %s\n\n", theBank.getName());
            System.out.print("Enter User ID: ");
            userID = sc.nextLine();
            System.out.print("Enter PIN: ");
            pin = sc.nextLine();

            // try to get the user object corresponding to the ID and pin combo

            authUser = theBank.userLogin(userID, pin);
            if (authUser == null) {
                System.out.println("Incorrect user ID/PIN combination. Please try again.");
            }
        } while (authUser == null); // continue looping until successful login

        return authUser;
    }

    public static void printUserMenu(User user, Scanner sc) {
        // print a summary of the user's accounts
        user.printAccountsSummary();

        //init
        int choice;

        //user menu
        do {
            System.out.printf("Welcome %s, what would you like to do?\n", user.getFirstName());
            System.out.println("1) Show account transaction history.");
            System.out.println("2) Withdrawal.");
            System.out.println("3) Deposit");
            System.out.println("4) Transfer");
            System.out.println("6) Transfer between user");
            System.out.println("5) Quit");
            System.out.println("\n");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();

            if (choice < 1 || choice > 6) {
                System.out.println("Invalid choice. Please choose 1-5");
            }
        } while (choice < 1 || choice > 6);
        // process the choice

        switch (choice) {
            case 1:
                ATM.showTransactionHistory(user, sc);
                break;
            case 2:
                ATM.withdrawalFunds(user, sc);
                break;
            case 3:
                ATM.depositFunds(user, sc);
                break;
            case 4:
                ATM.transferFunds(user, sc);
                break;
            case 5:
                // gobble up rest of previous input
                sc.nextLine();
                break;
            case 6:
                ATM.transferFundsAnotherAcct(user, sc);
        }

        // redisplay the menu unless the user wants to quit
        if (choice != 5) {
            ATM.printUserMenu(user, sc);

        }
    }

    /*
     *Show the transaction history for an account
     *@param user the logged-in User object
     *@param sc the Scanner object used for user input
     * */
    public static void showTransactionHistory(User user, Scanner sc) {
        int theAcct;
        // get account whose transaction history to look at
        do {
            System.out.printf("Enter the number (1 - %d) of the account\n" +
                    "whose transactions you want ot see: ", user.numAccounts());
            theAcct = sc.nextInt() - 1; // convert to programming numbering

            if (theAcct < 0 || theAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }

        } while (theAcct < 0 || theAcct >= user.numAccounts());

        // print the transaction history
        user.printAccountsSummary(theAcct);
    }

    public static void transferFundsAnotherAcct(User user, Scanner sc) {
        // inits
        int fromAcct;
        String toAcct;
        double amount;
        double acctBal;
        boolean existAcct = false;

        //get the account to transfer from
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", user.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAccountBalance(fromAcct);

        //get the account to transfer to
        do {
            System.out.println("Enter UUID account where you want to transfer to:\n");
            toAcct = sc.nextLine();
            existAcct = user.accountExistsInBank(toAcct);
            if (!existAcct) {
                System.out.printf("There is no such UUID %s recognized, try again", toAcct);
            } else {
                existAcct = true;
            }
            //8147652277
        } while (!existAcct);

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max %.02f EUR): EUR",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.00 EUR");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than total balance of the account ( %.02f EUR)\n", acctBal);
            }
        } while (amount < 0 || amount >= acctBal);



        // finally, do the transfer DMS
        //1. Credit
        user.addAcctTransaction(fromAcct, -1 * amount, String.format(
                "Transfer from account %s", toAcct
        ));
        //2. Debit
        user.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", user.getAcctUUID(fromAcct)));

    }


    /*
     * Process transferring funds from one account to another
     * @param user           the logged-in User object
     * @param sc             the Scanner object used for user input
     * */
    public static void transferFunds(User user, Scanner sc) {
        // inits
        int fromAcct;
        int toAcct;
        double amount;
        double acctBal;

        //get the account to transfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer from: ", user.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAccountBalance(fromAcct);

        // get the account to transfer to
        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to transfer to: ", user.numAccounts());
            toAcct = sc.nextInt() - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to transfer (max %.02f EUR): EUR",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.00 EUR");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than total balance of the account ( %.02f EUR)\n", acctBal);
            }
        } while (amount < 0 || amount >= acctBal);

        // finally, do the transfer DMS
        //1. Debit
        user.addAcctTransaction(fromAcct, -1 * amount, String.format(
                "Transfer to account %s", user.getAcctUUID(toAcct)
        ));
        //2. Credit
        user.addAcctTransaction(toAcct, amount, String.format(
                "Transfer to account %s", user.getAcctUUID(fromAcct)));
    }


    /*
     * Process a fund withdraw from an account
     * @param user           the logged-in User object
     * @param sc             the Scanner object used for user input
     * */
    public static void withdrawalFunds(User user, Scanner sc) {
        // inits
        int fromAcct;
        double amount;
        double acctBal;
        String memo;

        //get the account to transfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to withdraw from: ", user.numAccounts());
            fromAcct = sc.nextInt() - 1;
            if (fromAcct < 0 || fromAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }
        } while (fromAcct < 0 || fromAcct >= user.numAccounts());
        acctBal = user.getAccountBalance(fromAcct);

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to withdraw (max %.02f EUR): EUR",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.00 EUR");
            } else if (amount > acctBal) {
                System.out.printf("Amount must be less than total balance of the account ( %.02f EUR)\n", acctBal);
            }
        } while (amount < 0 || amount >= acctBal);

        // gobble up rest of previous input

        sc.nextLine();

        // get a memo

        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        user.addAcctTransaction(fromAcct, -1 * amount, memo);

    }

    /*
     * Process a fund deposit from an account
     * @param user           the logged-in User object
     * @param sc             the Scanner object used for user input
     * */
    public static void depositFunds(User user, Scanner sc) {
        // inits
        int toAcct;
        double amount;
        double acctBal;
        String memo;

        //get the account to transfer from

        do {
            System.out.printf("Enter the number (1-%d) of the account\n" +
                    "to deposit: ", user.numAccounts());
            toAcct = ((Integer) sc.nextInt()) - 1;
            if (toAcct < 0 || toAcct >= user.numAccounts()) {
                System.out.println("Invalid account number. Please select another");
            }
        } while (toAcct < 0 || toAcct >= user.numAccounts());
        acctBal = user.getAccountBalance(toAcct);

        // get the amount to transfer
        do {
            System.out.printf("Enter the amount to deposit (min %.02f EUR): EUR",
                    acctBal);
            amount = sc.nextDouble();
            if (amount < 0) {
                System.out.println("Amount must be greater than 0.00 EUR");
            }
        } while (amount < 0);

        // gobble up rest of previous input
        sc.nextLine();

        // get a memo

        System.out.println("Enter a memo: ");
        memo = sc.nextLine();

        // do the withdrawal
        user.addAcctTransaction(toAcct, amount, memo);
    }
}
