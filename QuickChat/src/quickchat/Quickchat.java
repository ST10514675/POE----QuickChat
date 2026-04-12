/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.Scanner;

// This is my main class for the Quickchat 
public class Quickchat {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        Login user = new Login();

        // Registration Heading
        System.out.println("QUICKCHAT REGISTRATION");

        System.out.print("First name: ");
        user.setFirstName(scan.nextLine());

        System.out.print("Last name: ");
        user.setLastName(scan.nextLine());

        System.out.print("Username: ");
        user.setUsername(scan.nextLine());

        System.out.print("Password: ");
        user.setPassword(scan.nextLine());

        System.out.print("Phone (+27...): ");
        user.setPhoneNumber(scan.nextLine());

        // Process Registration
        String msg = user.registerUser();
        System.out.println("\n" + msg);

        // Rubric Check: Specific Cell Phone status message (Image 12)
        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            // Text matched exactly to Image 12 rubric
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        // Logic Check: Only proceed if registration was successful
        if (msg.contains("successfully")) {

            System.out.println("QUICKCHAT LOGIN");

            System.out.print("Username: ");
            String u = scan.nextLine();

            System.out.print("Password: ");
            String p = scan.nextLine();

            // Run login check
            boolean logged = user.loginUser(u, p);

            // Display result from returnLoginStatus
            System.out.println(user.returnLoginStatus(logged));
        }
    }
}