/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.regex.Pattern;

public class Login {
    // these are for storing the user details
    String name;
    String surname;
    String user;
    String pass;
    String cell;

    // saving the names and stuff from the main part
    public void setFirstName(String f) {
        this.name = f;
    }

    public void setLastName(String l) {
        this.surname = l;
    }

    public void setUsername(String u) {
        this.user = u;
    }

    public void setPassword(String p) {
        this.pass = p;
    }

    public void setPhoneNumber(String num) {
        this.cell = num;
    }

    // this part checks if the username is okay
    public boolean checkUserName() {
        if (user == null) {
            return false;
        }
        
        // checking for underscore and that its not too long
        boolean hasUnderscore = user.contains("_");
        boolean shortEnough = user.length() <= 5;
        
        if (hasUnderscore && shortEnough) {
            return true;
        } else {
            return false;
        }
    }

    // making sure the password is strong enough for the project
    public boolean checkPasswordComplexity() {
        if (pass == null || pass.length() < 8) {
            return false;
        }

        boolean cap = false;
        boolean num = false;
        boolean spec = false;

        // go through every letter to check for caps and numbers
        for (int i = 0; i < pass.length(); i++) {
            char c = pass.charAt(i);

            if (Character.isUpperCase(c)) cap = true;
            if (Character.isDigit(c)) num = true;
            if (!Character.isLetterOrDigit(c)) spec = true;
        }

        return cap && num && spec;
    }

    // check if phone starts with +27
    public boolean checkCellPhoneNumber() {
        if (cell == null) return false;
        
        String pattern = "^\\+27\\d{9}$";
        return Pattern.matches(pattern, cell);
    }

    // this checks everything and gives back the message
    public String registerUser() {
        
        if (!checkUserName()) {
            return "Username is not correctly formated, please ensure that your username contains an underscore and is no more than 5 characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted, please ensure that the password contains at least 8 characters, a capital letter, a number and a special character.";
        }

        return "The two above conditions have been met, and the user has been registered successfully.";
    }

    // just checks if the login info matches what we saved
    public boolean loginUser(String uIn, String pIn) {
        if (uIn == null || pIn == null) return false;
        
        if (uIn.equals(user) && pIn.equals(pass)) {
            return true;
        }
        return false;
    }

    // showing the welcome message if they got in
    public String returnLoginStatus(boolean ok) {
        if (ok) {
            // make sure to keep the comma and the names
            return ("Welcome " + name + "  " + surname + " it is great to see you again.");
        } else {
            return ("Username or password incorrect, please try again.");
        }
    }
}
