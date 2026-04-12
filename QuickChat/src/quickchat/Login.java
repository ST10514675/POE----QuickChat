/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.regex.Pattern;

public class Login {
    // These are my global variables to store user info
    String firstName;
    String lastName;
    String username;
    String password;
    String phoneNumber;

    // Standard setters to save the data from the main class
    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    public void setUsername(String uName) {
        this.username = uName;
    }

    public void setPassword(String pWord) {
        this.password = pWord;
    }

    public void setPhoneNumber(String pNumber) {
        this.phoneNumber = pNumber;
    }

    // This method checks if the username follows the rules
    public boolean checkUserName() {
        if (username == null) return false;
        
        // Rules: must have underscore and max 5 characters
        boolean hasUnderscore = username.contains("_");
        boolean isShortEnough = username.length() <= 5;
        
        return hasUnderscore && isShortEnough;
    }

    // Checking if the password is complex enough for the rubric
    public boolean checkPasswordComplexity() {
        if (password == null || password.length() < 8) {
            return false;
        }

        // I used flags here to make sure every single rule is met
        boolean foundCap = false;
        boolean foundNum = false;
        boolean foundSpec = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) foundCap = true;
            if (Character.isDigit(c)) foundNum = true;
            if (!Character.isLetterOrDigit(c)) foundSpec = true;
        }

        // All three must be true to pass
        return foundCap && foundNum && foundSpec;
    }

    // Using regex to check the phone number format
    public boolean checkCellPhoneNumber() {
        // Regex for South African international code +27 and 9 digits
        String cellPattern = "^\\+27\\d{9}$";
        
        if (phoneNumber == null) {
            return false;
        }

        return Pattern.matches(cellPattern, phoneNumber);
    }

    // This runs the checks and returns the correct messages for the POE
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formated, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }

        // Success message required by the rubric
        return "The two above conditions have been met, and the user has been registered successfully.";
    }

    // Basic login check to see if details match
    public boolean loginUser(String u, String p) {
        if (u == null || p == null) return false;
        
        return u.equals(username) && p.equals(password);
    }

    // Returns the final welcome message
    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            // Added the comma after first name as per the image
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}