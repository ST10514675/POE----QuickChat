/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;


// QuickChat Message Class.
//This class handles individual message data, hash generation, 
//and keeping track of what has been sent.

public class message {

    // This are the fields to hold the details for single message
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String message;
    private String messageHash;

    // Static variables to keep track of the history while the app is running.
    private static ArrayList<String[]> sentMessages = new ArrayList<>();
    private static int totalMessagesSent = 0;

    // Main Constructor: we use this when we create brand new message
    public message(int messageNumber, String recipient, String message) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        
        // These are automatically created as soon as the object is created
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    // Secondary Constructor: Used when we already have an ID ,helpful for testing.
    public message(int messageNumber, String recipient, String message, String messageID) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.message = message;
        this.messageID = messageID;
        this.messageHash = createMessageHash();
    }

    // Helper method to create a random 10-digit ID
    private String generateMessageID() {
        Random rand = new Random();
        // Generating a random long to ensure we get a full 10 digit range
        long randomNum = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(randomNum);
    }

    // Simple check to ensure the ID didn't exceed the 10 character limit.
    public boolean checkMessageID() {
        if (messageID.length() <= 10) {
            return true;
        }
        return false;
    }

    // Validates that the phone number starts with the plus symbol.
    public String checkRecipientCell() {
        if (recipient.startsWith("+")) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Logic to build the unique message hash
    public String createMessageHash() {
        
        String idPrefix = messageID.substring(0, 2);

        String[] parts = message.trim().split(" ");
        
        String firstWordClean = parts[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWordClean = parts[parts.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        this.messageHash = idPrefix + ":" + messageNumber + ":" + firstWordClean + lastWordClean;
        return this.messageHash;
    }

    // Handles the user's menu choice for what to do with the message
    public String sentMessage(String choice) {
        // Choice 1: Send it and log it
        if (choice.equals("1")) {
            totalMessagesSent++;
            sentMessages.add(new String[]{messageID, messageHash, recipient, message});
            return "Message successfully sent";
        } 
        // Choice 2: Prompt for deletion
        else if (choice.equals("2")) {
            return "Press 0 to delete the message";
        } 
        // Choice 3: Save it to a file
        else if (choice.equals("3")) {
            storeMessage();
            return "Message successfully stored";
        } 
        // Anything else is wrong
        else {
            return "Invalid option.";
        }
    }

    // Builds a big string showing every message we've sent so far
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }

        String output = "";
        for (int i = 0; i < sentMessages.size(); i++) {
            String[] data = sentMessages.get(i);
            output += "Message ID: " + data[0] + "\n";
            output += "Message Hash: " + data[1] + "\n";
            output += "Recipient: " + data[2] + "\n";
            output += "Message: " + data[3] + "\n";
            output += "---------------------------------------------\n";
        }
        return output;
    }

    // Just returns the running total of messages sent
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    // Static version of the total count for easy access from other classes
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    // Logic to save all sent messages
    public void storeMessage() {
        // Add current message to the list first
        sentMessages.add(new String[]{messageID, messageHash, recipient, message});
        
        
        String jsonBuilder = "[\n";
        for (int i = 0; i < sentMessages.size(); i++) {
            String[] m = sentMessages.get(i);
            jsonBuilder += "  {\n";
            jsonBuilder += "    \"messageID\": \"" + m[0] + "\",\n";
            jsonBuilder += "    \"messageHash\": \"" + m[1] + "\",\n";
            jsonBuilder += "    \"recipient\": \"" + m[2] + "\",\n";
            jsonBuilder += "    \"message\": \"" + m[3] + "\"\n";
            jsonBuilder += "  }";
            
            
            if (i < sentMessages.size() - 1) {
                jsonBuilder += ",";
            }
            jsonBuilder += "\n";
        }
        jsonBuilder += "]";

     
        try {
            FileWriter writer = new FileWriter("messages.json");
            writer.write(jsonBuilder);
            writer.close();
            System.out.println("Message stored in messages.json");
        } catch (IOException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

    // Clean up function to wipe the history (mostly for testing purposes)
    public static void resetMessages() {
        sentMessages.clear();
        totalMessagesSent = 0;
    }

    // Standard Getters to retrieve private data
    public String getMessageID() {
        return messageID;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessage() {
        return message;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public int getMessageNumber() {
        return messageNumber;
    }
}
