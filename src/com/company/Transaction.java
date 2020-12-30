package com.company;

import java.util.Date;

public class Transaction {
    // the amount of this transaction.
    private double amount;
    // the time and date of this transaction.
    private Date timestamp;
    // A memo for this transaction
    private String memo;
    // the account in which the transaction was performed
    private Account inAccount;


    /*
    * Return the amount of transaction to account
    * @return the amount of transaction
    * */
    public double getAmount() {
        return amount;
    }

    /*
     * Create a new transaction
     * @param amount - the amount authorized
     * @param inAccount - the account the transaction belongs to
     * */
    public Transaction(double amount, Account inAccount) {
        this.amount = amount;
        this.inAccount = inAccount;
        this.timestamp = new Date();
        this.memo = "";
    }
    /*
     * Create a new transaction
     * @param amount - the amount authorized
     * @param inAccount - the account the transaction belongs to
     * @param memo - the memo for the transaction
     * */
    public Transaction(double amount, String memo, Account inAccount) {
        //call the two-arq constructor first
        // if previous constructor called, all changes ar stored and there is no need to write it again
        this(amount, inAccount);

        // set the memo
        this.memo = memo;

    }
/*
* Get a string summarizing the transaction
*@return the summary string
* */
    public String getSummaryLine(){
        if(this.amount > 0) {
            return String.format("%s : %.02f EUR : %s ", this.timestamp.toString(),
                    this.amount, this.memo);
        } else {
            return String.format("%s : (%.02f) EUR : %s ", this.timestamp.toString(),
                    this.amount, this.memo);
        }
    }
}
