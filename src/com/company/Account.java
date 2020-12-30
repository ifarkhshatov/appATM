package com.company;

import java.util.ArrayList;

public class Account {
    //    The name of the account
    private String name;
    // the ID number of the account
    private String uuid;
    // the User object that owns this account
    private User holder;
    // the list of transactions for this account
    private ArrayList<Transaction> transactions;

    /*
     * Create a new Account:
     * @param name - the name of the account
     * @param holder - the User object that holds this account
     * @param theBank the bank that issues the account
     * */
    public Account(String name, User holder, Bank theBank) {
        // set the account name and holder
        this.name = name;
        this.holder = holder;
        // get new account UUID;
        this.uuid = theBank.getNewAccountUUID();

        // init transactions
        this.transactions = new ArrayList<Transaction>();

    }
    /*
     * Return the account's UUID
     * @return the uuid
     * */
    public String getUUID() {
        return uuid;
    }
/*
* Get summary line for the account
*@return the string summary
* */
    public String getSummaryLine(){
        // get the account's balance
        double balance = this.getBalance();

        // format the summary line, depending on whether the balance is overdraft
        if (balance >=0) {
            return String.format("%s : %.02f EUR : %s", this.uuid, balance, this.name); //two digits after zero
        } else {
            return String.format("%s : (%.02f) EUR : %s", this.uuid, balance, this.name); //two digits after zero
        }
    }
    public double getBalance(){
        double balance = 0;
        for (Transaction t: this.transactions) {
            balance += t.getAmount();
        }
        return balance;
    }
/*
* Print the transaction history of the account
* */
    public void printTransHistory() {
        System.out.printf("\nTransaction history for account %s\n", this.uuid);
        for(int t = this.transactions.size()-1; t >= 0; t--) {
            System.out.println(this.transactions.get(t).getSummaryLine());
        }
//        System.out.println();
    }

    public void addTransaction(double amount, String memo) {
        // create new transaction object and add it to our list
        Transaction newTrans = new Transaction(amount, memo, this);
        this.transactions.add(newTrans); //add to list
    }
}
