package com.company;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.security.MessageDigest;

public class User {

    /*
     * The first name of the user.
     */
    private String firstName;
    /*
     * The second name of the user.
     */
    private String lastName;
    /*
     * The ID number of the user.
     */
    private String uuid;
    /*
     * The MD5 has of the user's pin number.
     */
    private byte pinHash[];
    /*
     * The list of accounts for this user.
     */
    private ArrayList<Account> accounts;

    /*
     * Return the first name for ATM's interface
     * @return the User's first name
     * */
    public String getFirstName() {
        return firstName;
    }

    /*
     * Create a new user:
     * @param firstName - the user's first name;
     * @param lastName - the user's last name;
     * @param pin - the user's account pin number;
     * @param theBank - the Bank object that the user is a customer of
     * */


    public User(String firstName, String lastName, String pin, Bank theBank) {
        // set user's name;
        this.firstName = firstName;
        this.lastName = lastName;

        // store the pin's MD5 hash, instead of original value

        try {
            // call algorithm from md class MD5 hashing
            MessageDigest md = MessageDigest.getInstance("MD5");
            // convert bytes of origin pin to hashed byte value
            this.pinHash = md.digest(pin.getBytes());

            //This will never call, just to verify java
        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }

        // get a new, unique universal ID for the user
        this.uuid = theBank.getNewUserUUID();

        // create empty list of accounts

        this.accounts = new ArrayList<Account>();

        // print log message
        System.out.printf("New user %s, %s with ID %s created.\n", lastName, firstName, this.uuid);
    }

    /*
     * Add an account for the user
     * @param anAcct - the account to add
     * */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /*
     * Return the user's UUID
     * @return the uuid
     * */
    public String getUUID() {
        return uuid;
    }

    /*
     * Check whether a given pin matches the true User pin
     * @param aPin           the pin to check
     * @return               whether the pin is valid or not
     * */
    public boolean validatePin(String aPin) {

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(md.digest(aPin.getBytes()), this.pinHash);

        } catch (NoSuchAlgorithmException e) {
            System.err.println("error, caught NoSuchAlgorithmException");
            e.printStackTrace();
            System.exit(1);
        }
        return false;
    }

    /*
     * Print summaries for the accounts of this user
     * */
    public void printAccountsSummary() {
        System.out.printf("\n\n%s's accounts summary\n", this.firstName);
        for (int a = 0; a < this.accounts.size(); a++) {
            System.out.printf("%d) %s\n", a + 1, //get the number of account
                    this.accounts.get(a).getSummaryLine());
        }
        System.out.println();
    }

    /*
     * Return the total number of account
     * @return the number of accounts
     * */

    public int numAccounts() {
        return this.accounts.size();
    }

    /*
     * Print transaction history for a particular account.
     * @param acctIdx the index of the account to use
     * */
    public void printAccountsSummary(int acctIdx) {
        this.accounts.get(acctIdx).printTransHistory();
    }

    /*
    * Get the balance of a particular account
    * @param acctIdx   the index of the account to use
    * @return          the balance of the account
    * */
    public double getAccountBalance(int acctIdx) {
        return this.accounts.get(acctIdx).getBalance();
    }

    /*
    * Get the UUID of a particular account
    * @param acctIdx the index of the account to use
    * @return        the UUID of account to use
    * */
    public String getAcctUUID(int acctIdx) {
        return  this.accounts.get(acctIdx).getUUID();
    }
    /*
    * Add a transaction to a particular account
    * @param acctIdx        the index of the account
    * @param amount         the amount of the transaction
    * @param memo           the memo of the transaction
    * */
    public void addAcctTransaction(int acctIdx, double amount, String  memo) {
        this.accounts.get(acctIdx).addTransaction(amount, memo);
    }
}
