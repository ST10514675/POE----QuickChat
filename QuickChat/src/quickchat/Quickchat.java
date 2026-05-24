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

        // Registration Heading , basically shows the user the name of the app ,this is the first thing that will appear before the program runs
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

        // if your cellphone number is correct it will display this when the project runs
        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            // If user doesnt start by entering the international code, the number format will be wrong
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        // Only proceed if registration was successful, then you will have to Login
        if (msg.contains("successfully")) {
            System.out.println("\nQUICKCHAT LOGIN");
            System.out.print("Username: ");
            String u = scan.nextLine();
            System.out.print("Password: ");
            String p = scan.nextLine();

            // Run login check.
            boolean logged = user.loginUser(u, p);

            // Display result from returnLoginStatus
            System.out.println(user.returnLoginStatus(logged));

            // The users should only be able to send messages if they have logged in successfully
            if (logged) {

                // The application must display the following welcome message: "Welcome to QuickChat."
                System.out.println("\nWelcome to QuickChat.");

                // Users should define how many messages they wish to enter when the application starts
                System.out.print("How many messages would you like to send? ");
                int numMessages = Integer.parseInt(scan.nextLine());

                // Keep track of how many messages the user has sent so far
                int messagesSentThisSession = 0;

                // The application should run until the user selects 'quit' to exit
                boolean running = true;
                while (running) {

                    // The user should then be able to choose one of the following features from a numeric menu
                    System.out.println("\nOption 1) Send Messages");
                    System.out.println("Option 2) Show recently sent messages");
                    System.out.println("Option 3) Quit");
                    System.out.print("Please select an option: ");
                    String menuChoice = scan.nextLine();

                    if (menuChoice.equals("1")) {

                        // The application should allow the user to enter only the set number of messages
                        if (messagesSentThisSession >= numMessages) {
                            System.out.println("You have reached your message limit of " + numMessages + " message(s).");
                        } else {

                            // Collect recipient and message from the user
                            System.out.print("Recipient (+27...): ");
                            String recipient = scan.nextLine();
                            System.out.print("Enter your message: ");
                            String messageText = scan.nextLine();

                            // Create a new message object with the current message number
                            message newMessage = new message(messagesSentThisSession, recipient, messageText);

                            // Validate the recipient cell number and display result
                            System.out.println(newMessage.checkRecipientCell());

                            // Validate message length - message should not exceed 250 characters
                            System.out.println(newMessage.checkMessageLength());

                            // Only continue if both the number and message are valid
                            if (recipient.startsWith("+") && messageText.length() <= 250) {

                                // Once the message is completed, the system should ask the user to choose one of the following options
                                System.out.println("\n1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                System.out.print("Please select an option: ");
                                String sendChoice = scan.nextLine();

                                // Process the choice and show the result
                                String result = newMessage.sentMessage(sendChoice);
                                System.out.println(result);

                                // The full details of each message should be displayed on the screen after it has been sent
                                // Shown in the following order: Message ID, Message Hash, Recipient, Message
                                if (result.equals("Message successfully sent")) {
                                    messagesSentThisSession++;
                                    System.out.println("\nMessage ID: " + newMessage.getMessageID());
                                    System.out.println("Message Hash: " + newMessage.getMessageHash());
                                    System.out.println("Recipient: " + newMessage.getRecipient());
                                    System.out.println("Message: " + newMessage.getMessage());
                                }

                                // If the user chose to store the message, count it too
                                if (result.equals("Message successfully stored")) {
                                    messagesSentThisSession++;
                                }
                            }
                        }

                    } else if (menuChoice.equals("2")) {
                        // Option 2) Show recently sent messages - this feature is still in development
                        // and should display the following message: "Coming Soon."
                        System.out.println("Coming Soon.");

                    } else if (menuChoice.equals("3")) {
                        // Option 3) Quit - stop the loop and wrap up
                        // The total number of messages should be accumulated and displayed once all the messages have been sent
                        System.out.println("\nTotal messages sent: " + message.getTotalMessagesSent());
                        System.out.println(new message(0, "", " ").printMessages());
                        running = false;

                    } else {
                        // Handle any option that isn't 1, 2, or 3
                        System.out.println("Invalid option. Please select 1, 2, or 3.");
                    }
                }
            }
        }

        scan.close();
    }
}