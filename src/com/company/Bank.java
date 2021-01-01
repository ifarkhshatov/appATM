package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Bank {
    private String name;
    private ArrayList<User> users;
    private ArrayList<Account> accounts;


    /*
    * Return the name of the Bank
    * @return name of the bank
    * */
    public String getName() {
        return name;
    }

    /*
* Create a new Bank object with empty lists of users and accounts
* @param name       New Bank name
* */
    public Bank(String name) {
        this.name = name;
        this.accounts = new ArrayList<Account>();
        this.users = new ArrayList<User>();
    }

    /*
     * Generate a new universally unique ID for a user.
     * @return the uuid
     * */
    public String getNewUserUUID() {
        //init
        String uuid;
        Random rng = new Random();
        int length = 6;
        boolean nonUnique;

        // continue looping until we get a unique ID
        nonUnique = false;
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c < length; c++) {
                // add each loop to range a Integer next in from 0 to 10 and convert to string
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            // check to make sure it is unique
            for (User u : this.users) {
                if (uuid.compareTo(u.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;

    }

    /*
     * Generate a new universally unique ID for a account.
     * @return the uuid
     * */
    public String getNewAccountUUID() {
        //init
        String uuid;
        Random rng = new Random();
        int length = 10;
        boolean nonUnique;

        // continue looping until we get a unique ID
        nonUnique = false;
        do {
            // generate the number
            uuid = "";
            for (int c = 0; c < length; c++) {
                // add each loop to range a Integer next in from 0 to 10 and convert to string
                uuid += ((Integer) rng.nextInt(10)).toString();
            }
            // check to make sure it is unique
            for (Account a : this.accounts) {
                if (uuid.compareTo(a.getUUID()) == 0) {
                    nonUnique = true;
                    break;
                }
            }

        } while (nonUnique);

        return uuid;
    }

    /*
     * Add an account for the user
     * @param anAcct - the account to add
     * */
    public void addAccount(Account anAcct) {
        this.accounts.add(anAcct);
    }

    /*
     * Generate a new universally unique ID for a account.
     * @param firstName - the user's first name;
     * @param lastName - the user's last name;
     * @param pin - the user's pin
     * @return - the new User object
     * */
    public User addUser(String firstName, String lastName, String pin) {
        // create a new User object and add it to bank list
        User newUser = new User(firstName, lastName, pin, this);
        this.users.add(newUser);

        // create a savings account for the user and add to User and Bank
        // account lists
        Account newAccount = new Account("Savings", newUser, this);

        //add this account to holder and bank lists
        newUser.addAccount(newAccount);
        this.accounts.add(newAccount);

    return newUser;
    }


    /*
    * Get the User object associated with a particular userID and pin, if they are valid
    * @param userID     the UUID of the user to log in
    * @param pin        the pin of the user
    * @return           the User object, if the login is successful, or null, if it is not
    * */
        // return valid user if parameters match otherwise return null
    public User userLogin(String userID, String pin) {

        // search through userList
            for (User u: this.users) {
                // check user ID
                if (u.getUUID().compareTo(userID) == 0 && u.validatePin(pin)) {
                    return u;
                }
            }
            // if we have not found the user or have an incorrect pin
        return null;
    }

    /*
    * Return does the UUID exists in the Bank
    * @param  acctUUID      UUID to check validity
    * @return               exists or not
    * */
    public boolean existsUUID(String acctUUID) {
        for(Account a: accounts) {
            if (a.getUUID().equals(acctUUID)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }
}
