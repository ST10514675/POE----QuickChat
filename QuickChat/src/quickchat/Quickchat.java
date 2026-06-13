/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.Scanner;
import java.util.ArrayList;

/**
 * QuickChat Main Application Entry Point.
 * Handles registration, login, and the full message menu (Parts 1–3).
 */
public class Quickchat {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Login user = new Login();

        System.out.println("QUICKCHAT REGISTRATION");
        System.out.print("First name: ");
        user.setFirstName(scan.nextLine());
        System.out.print("Last name: ");
        user.setLastName(scan.nextLine());
        System.out.print("Username (must contain '_' and be ≤5 chars): ");
        user.setUsername(scan.nextLine());
        System.out.print("Password: ");
        user.setPassword(scan.nextLine());
        System.out.print("Cell phone number (+27...): ");
        user.setPhoneNumber(scan.nextLine());

        // Run all validation and print the registration result
        String registrationMsg = user.registerUser();
        System.out.println("\n" + registrationMsg);

        // Showing phone validation message separately 
        if (user.checkCellPhoneNumber()) {
            System.out.println("Cell phone number successfully added.");
        } else {
            System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
        }

        // Only proceed to login if registration was fully successful
        if (!registrationMsg.contains("successfully")) {
            System.out.println("\nRegistration failed. Please restart and try again.");
            scan.close();
            return;
        }

        // ---------------------------------------------------------------
        // LOGIN
        // ---------------------------------------------------------------
        System.out.println("\n===== QUICKCHAT LOGIN =====");
        System.out.print("Username: ");
        String enteredUsername = scan.nextLine();
        System.out.print("Password: ");
        String enteredPassword = scan.nextLine();

        boolean loggedIn = user.loginUser(enteredUsername, enteredPassword);
        System.out.println(user.returnLoginStatus(loggedIn));

        // ---------------------------------------------------------------
        // MESSAGING — only accessible after a successful login
        // ---------------------------------------------------------------
        if (loggedIn) {

            // Welcome message (required by rubric)
            System.out.println("\nWelcome to QuickChat.");

            // User defines how many messages they want to send this session
            System.out.print("How many messages would you like to send? ");
            int numMessages = 0;
            try {
                numMessages = Integer.parseInt(scan.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Defaulting to 1.");
                numMessages = 1;
            }

            int messagesSentThisSession = 0;

            // Main menu loop — runs until the user selects Quit
            boolean running = true;
            while (running) {

                System.out.println("\n----------------------------------");
                System.out.println("Option 1) Send Messages");
                System.out.println("Option 2) Show recently sent messages");
                System.out.println("Option 3) Quit");
                System.out.println("Option 4) Stored Messages");
                System.out.print("Please select an option: ");
                String menuChoice = scan.nextLine().trim();

                switch (menuChoice) {

                    // ----------------------------------------------------------
                    // OPTION 1 — Send a message
                    // ----------------------------------------------------------
                    case "1":
                        if (messagesSentThisSession >= numMessages) {
                            System.out.println("You have reached your message limit of " + numMessages + " message(s).");
                        } else {
                            System.out.print("Recipient cell number (+27...): ");
                            String recipient = scan.nextLine().trim();

                            System.out.print("Enter your message (max 250 chars): ");
                            String messageText = scan.nextLine();

                            // Create the message object (message number = current count)
                            message newMessage = new message(messagesSentThisSession, recipient, messageText);

                            // Validate recipient number and message length
                            System.out.println(newMessage.checkRecipientCell());
                            System.out.println(newMessage.checkMessageLength());

                            // Only continue if recipient has international code AND message fits
                            if (recipient.startsWith("+") && messageText.length() <= 250) {

                                System.out.println("\n1) Send Message");
                                System.out.println("2) Disregard Message");
                                System.out.println("3) Store Message to send later");
                                System.out.print("Please select an option: ");
                                String sendChoice = scan.nextLine().trim();

                                String result = newMessage.sentMessage(sendChoice);
                                System.out.println(result);

                                // After sending: display full message details (ID, Hash, Recipient, Message)
                                if (result.equals("Message successfully sent.")) {
                                    messagesSentThisSession++;
                                    System.out.println("\nMessage ID: "   + newMessage.getMessageID());
                                    System.out.println("Message Hash: "  + newMessage.getMessageHash());
                                    System.out.println("Recipient: "     + newMessage.getRecipient());
                                    System.out.println("Message: "       + newMessage.getMessage());
                                }

                                // Stored messages also count toward the session limit
                                if (result.equals("Message successfully stored.")) {
                                    messagesSentThisSession++;
                                }

                                // Disregarded messages also count (user chose to enter them)
                                if (result.equals("Press 0 to delete the message.")) {
                                    messagesSentThisSession++;
                                }
                            }
                        }
                        break;

                    // ----------------------------------------------------------
                    // OPTION 2 — Recently sent messages (coming soon)
                    // ----------------------------------------------------------
                    case "2":
                        System.out.println("Coming Soon.");
                        break;

                    // ----------------------------------------------------------
                    // OPTION 3 — Quit
                    // ----------------------------------------------------------
                    case "3":
                        System.out.println("\nTotal messages sent: " + message.getTotalMessagesSent());
                        // Print all sent messages — printMessages() is now static
                        System.out.println(message.printMessages());
                        running = false;
                        break;

                    // ----------------------------------------------------------
                    // OPTION 4 — Stored Messages sub-menu (Part 3)
                    // ----------------------------------------------------------
                    case "4":
                        System.out.println("\na) Display sender and recipient of all stored messages");
                        System.out.println("b) Display the longest stored message");
                        System.out.println("c) Search for a message by Message ID");
                        System.out.println("d) Search for messages by recipient");
                        System.out.println("e) Delete a message using its Message Hash");
                        System.out.println("f) Display a full report of all messages");
                        System.out.print("Please select an option: ");
                        String storedChoice = scan.nextLine().trim();

                        switch (storedChoice.toLowerCase()) {
                            case "a":
                                System.out.println(message.displayStoredSenderAndRecipient());
                                break;

                            case "b":
                                System.out.println(message.getLongestStoredMessage());
                                break;

                            case "c":
                                System.out.print("Enter the Message ID to search for: ");
                                String searchID = scan.nextLine().trim();
                                System.out.println(message.searchByMessageID(searchID));
                                break;

                            case "d":
                                System.out.print("Enter the recipient number to search for: ");
                                String searchRecipient = scan.nextLine().trim();
                                ArrayList<String> found = message.searchByRecipient(searchRecipient);
                                if (found.isEmpty()) {
                                    System.out.println("No messages found for recipient: " + searchRecipient);
                                } else {
                                    for (String foundMsg : found) {
                                        System.out.println(foundMsg);
                                    }
                                }
                                break;

                            case "e":
                                System.out.print("Enter the Message Hash to delete: ");
                                String deleteHash = scan.nextLine().trim();
                                System.out.println(message.deleteMessageByHash(deleteHash));
                                break;

                            case "f":
                                // Full report — sent AND stored messages (Hash, Recipient, Message)
                                System.out.println(message.displayStoredReport());
                                break;

                            default:
                                System.out.println("Invalid option. Please select a–f.");
                        }
                        break;

                    // ----------------------------------------------------------
                    // Invalid menu option
                    // ----------------------------------------------------------
                    default:
                        System.out.println("Invalid option. Please select 1, 2, 3, or 4.");
                }
            }
        }

        scan.close();
    }
}