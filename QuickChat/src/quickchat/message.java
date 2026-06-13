/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import java.util.Random;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * QuickChat Message Class.
 * Handles individual message data, hash generation, JSON storage,
 * and all Part 3 array-based searching, deleting, and reporting features
 */
public class message {

    
    // Instance fields for a single message
    
    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    
    // Named static arrays as required by the rubric.
    // 
    
    private static ArrayList<String[]> sentMessages        = new ArrayList<>();
    private static ArrayList<String[]> disregardedMessages = new ArrayList<>();
    private static ArrayList<String[]> storedMessages      = new ArrayList<>();  
    private static ArrayList<String>   messageHashes       = new ArrayList<>();
    private static ArrayList<String>   messageIDs          = new ArrayList<>();

    private static int totalMessagesSent = 0;

    
    // Static initializer — loads any previously stored messages from
    // messages.json into the storedMessages array when the class is first loaded.
    // This ensures the in-memory list always reflects what is already on disk
  
    
    static {
        storedMessages.addAll(readStoredMessages());
    }

    
    
    

    /** Main constructor — auto-generates the message ID. */
    public message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = generateMessageID();
        this.messageHash   = createMessageHash();
    }

    /**
     * Secondary constructor — caller supplies the message ID.
     * Useful for unit tests where a known ID is needed to verify the hash.
     */
    public message(int messageNumber, String recipient, String messageText, String messageID) {
        this.messageNumber = messageNumber;
        this.recipient     = recipient;
        this.messageText   = messageText;
        this.messageID     = messageID;
        this.messageHash   = createMessageHash();
    }

    

    /** Generates a random 10-digit numeric message ID. */
    private String generateMessageID() {
        Random rand = new Random();
        long randomNum = 1000000000L + (long) (rand.nextDouble() * 9000000000L);
        return String.valueOf(randomNum);
    }


    /** Ensures the message ID is no more than 10 characters long. */
    public boolean checkMessageID() {
        return messageID.length() <= 10;
    }

    /**
     * Validates the recipient cell number.
     * Must start with '+' (international code).
     */
    public String checkRecipientCell() {
        if (recipient.startsWith("+")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Checks that the message does not exceed 250 characters.
     * Returns exact character overage in the failure message.
     */
    public String checkMessageLength() {
        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Builds and returns the Message Hash.
     * Format: first two digits of ID : message number : FIRSTWORDLASTWORD (all caps)
     * Example: 00:0:HITONIGHT
     */
    public String createMessageHash() {
        // Take the first two characters of the message ID
        String idPrefix = messageID.substring(0, 2);

        // Split on whitespace to get individual words
        String[] parts = messageText.trim().split("\\s+");

        // Strip non-alphanumeric characters and uppercase both words
        String firstWord = parts[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord  = parts[parts.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();

        this.messageHash = idPrefix + ":" + messageNumber + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    /**
     * Handles the user's send/disregard/store choice.
     * Populates the correct static arrays and updates all tracking lists.
     *
     * Returns:
     *   "Message successfully sent."
     *   "Press 0 to delete the message."
     *   "Message successfully stored."
     */
    public String sentMessage(String choice) {
        switch (choice) {
            case "1":
                // Send — add to sentMessages, messageHashes, and messageIDs arrays
                totalMessagesSent++;
                String[] sentEntry = {messageID, messageHash, recipient, messageText};
                sentMessages.add(sentEntry);
                messageHashes.add(messageHash);
                messageIDs.add(messageID);
                return "Message successfully sent.";

            case "2":
                // Disregard — add to disregardedMessages array
                disregardedMessages.add(new String[]{messageID, messageHash, recipient, messageText});
                return "Press 0 to delete the message.";

            case "3":
                // Store — save to JSON and load into storedMessages array
                storeMessage();
                return "Message successfully stored.";

            default:
                return "Invalid option.";
        }
    }

    /**
     * Returns a formatted string of all sent messages.
     * Order: Message ID, Message Hash, Recipient, Message.
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder output = new StringBuilder();
        for (String[] data : sentMessages) {
            output.append("Message ID: ").append(data[0]).append("\n");
            output.append("Message Hash: ").append(data[1]).append("\n");
            output.append("Recipient: ").append(data[2]).append("\n");
            output.append("Message: ").append(data[3]).append("\n");
            output.append("---------------------------------------------\n");
        }
        return output.toString();
    }

    /** Returns the total number of messages sent this session. */
    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    /** Static version of the total count for easy access. */
    public static int getTotalMessagesSent() {
        return totalMessagesSent;
    }

    
    // JSON Storage (Research component — attributed below)
   

    /**
     * Saves the current message to messages.json.
     * Also adds it to the storedMessages array and tracking lists.
     * JSON writing approach referenced from:
     */
    public void storeMessage() {
        // Add to in-memory storedMessages array and tracking lists
        String[] entry = {messageID, messageHash, recipient, messageText};
        storedMessages.add(entry);
        messageHashes.add(messageHash);
        messageIDs.add(messageID);

        // Re-read the existing file and append — this keeps all previously stored messages
        ArrayList<String[]> allStored = readStoredMessages();

        // Avoid duplicates: only add if this messageID isn't already in the file
        boolean alreadyExists = false;
        for (String[] m : allStored) {
            if (m[0].equals(messageID)) {
                alreadyExists = true;
                break;
            }
        }
        if (!alreadyExists) {
            allStored.add(entry);
        }

        rewriteStoredMessages(allStored);
        System.out.println("Message stored in messages.json");
    }

    
    // Part 3 — Array population & feature methods
    

    /**
     * Reads messages.json and populates the storedMessages array.
     */
    public static ArrayList<String[]> readStoredMessages() {
        ArrayList<String[]> loaded = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader("messages.json"));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            reader.close();

            // Each message sits between { } in the JSON file
            Pattern objectPattern = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL);
            Matcher objectMatcher = objectPattern.matcher(content.toString());

            while (objectMatcher.find()) {
                String block = objectMatcher.group(1);
                String id        = extractJsonValue(block, "messageID");
                String hash      = extractJsonValue(block, "messageHash");
                String recip     = extractJsonValue(block, "recipient");
                String msg       = extractJsonValue(block, "message");
                loaded.add(new String[]{id, hash, recip, msg});
            }
        } catch (IOException e) {
           
        }
        return loaded;
    }

    /** Extracts a value from a JSON key-value pair using regex. */
    private static String extractJsonValue(String block, String key) {
        Pattern p = Pattern.compile("\"" + key + "\"\\s*:\\s*\"(.*?)\"", Pattern.DOTALL);
        Matcher m = p.matcher(block);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    /** Rewrites messages.json with the given list (used after deletions). */
    private static void rewriteStoredMessages(ArrayList<String[]> stored) {
        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < stored.size(); i++) {
            String[] m = stored.get(i);
            json.append("  {\n");
            json.append("    \"messageID\": \"").append(m[0]).append("\",\n");
            json.append("    \"messageHash\": \"").append(m[1]).append("\",\n");
            json.append("    \"recipient\": \"").append(m[2]).append("\",\n");
            json.append("    \"message\": \"").append(m[3]).append("\"\n");
            json.append("  }");
            if (i < stored.size() - 1) json.append(",");
            json.append("\n");
        }
        json.append("]");

        try {
            FileWriter writer = new FileWriter("messages.json");
            writer.write(json.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Error updating messages.json: " + e.getMessage());
        }
    }

    /**
     * a) Displays sender and recipient of all stored messages.
     * Sender is shown as the registered user (stored in this app as "You").
     */
    public static String displayStoredSenderAndRecipient() {
        ArrayList<String[]> stored = readStoredMessages();
        if (stored.isEmpty()) {
            return "No stored messages found.";
        }
        StringBuilder output = new StringBuilder();
        for (String[] m : stored) {
            output.append("Sender: You | Recipient: ").append(m[2]).append("\n");
        }
        return output.toString();
    }

    /**
     * b) Finds and returns the longest message from the stored messages array.
     */
    public static String getLongestStoredMessage() {
        ArrayList<String[]> stored = readStoredMessages();
        if (stored.isEmpty()) {
            return "No stored messages found.";
        }
        String longest = stored.get(0)[3];
        for (String[] m : stored) {
            if (m[3].length() > longest.length()) {
                longest = m[3];
            }
        }
        return longest;
    }

    /**
     * c) Searches for a message by ID and returns the recipient and message text.
     * Checks sentMessages first, then storedMessages (from JSON).
     */
    public static String searchByMessageID(String id) {
        for (String[] m : sentMessages) {
            if (m[0].equals(id)) {
                return "Recipient: " + m[2] + "\nMessage: " + m[3];
            }
        }
        for (String[] m : readStoredMessages()) {
            if (m[0].equals(id)) {
                return "Recipient: " + m[2] + "\nMessage: " + m[3];
            }
        }
        return "No message found with ID: " + id;
    }

    /**
     * d) Returns all messages (sent or stored) for a particular recipient.
     */
    public static ArrayList<String> searchByRecipient(String recipient) {
        ArrayList<String> results = new ArrayList<>();
        for (String[] m : sentMessages) {
            if (m[2].equals(recipient)) {
                results.add(m[3]);
            }
        }
        for (String[] m : readStoredMessages()) {
            if (m[2].equals(recipient)) {
                results.add(m[3]);
            }
        }
        return results;
    }

    /**
     * e) Deletes a message using its Message Hash.
     * Checks sentMessages first, then the JSON file.
     * Returns the rubric-specified success/failure message.
     */
    public static String deleteMessageByHash(String hash) {
        // Search sent messages
        for (int i = 0; i < sentMessages.size(); i++) {
            if (sentMessages.get(i)[1].equals(hash)) {
                String deletedText = sentMessages.get(i)[3];
                sentMessages.remove(i);
                messageHashes.remove(hash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        // Search stored messages (JSON file)
        ArrayList<String[]> stored = readStoredMessages();
        for (int i = 0; i < stored.size(); i++) {
            if (stored.get(i)[1].equals(hash)) {
                String deletedText = stored.get(i)[3];
                stored.remove(i);
                rewriteStoredMessages(stored);
                // Also remove from in-memory storedMessages
                storedMessages.removeIf(m -> m[1].equals(hash));
                messageHashes.remove(hash);
                return "Message: \"" + deletedText + "\" successfully deleted.";
            }
        }
        return "No message found with hash: " + hash;
    }

    /**
     * f) Displays a full report of ALL messages (sent + stored).
     * Shows: Message Hash, Recipient, Message for each entry.
     */
    public static String displayStoredReport() {
        ArrayList<String[]> stored = readStoredMessages();
        if (sentMessages.isEmpty() && stored.isEmpty()) {
            return "No messages to report.";
        }

        StringBuilder output = new StringBuilder();

        // Sent messages section
        if (!sentMessages.isEmpty()) {
            output.append("=== SENT MESSAGES ===\n");
            for (String[] m : sentMessages) {
                output.append("Message Hash: ").append(m[1]).append("\n");
                output.append("Recipient: ").append(m[2]).append("\n");
                output.append("Message: ").append(m[3]).append("\n");
                output.append("---------------------------------------------\n");
            }
        }

        // Stored messages section
        if (!stored.isEmpty()) {
            output.append("=== STORED MESSAGES ===\n");
            for (String[] m : stored) {
                output.append("Message Hash: ").append(m[1]).append("\n");
                output.append("Recipient: ").append(m[2]).append("\n");
                output.append("Message: ").append(m[3]).append("\n");
                output.append("---------------------------------------------\n");
            }
        }

        return output.toString();
    }

    /**
     * Displays a report of sent messages only.
     * Used internally by printMessages / Quit option.
     */
    public static String displayReport() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }
        StringBuilder output = new StringBuilder();
        for (String[] m : sentMessages) {
            output.append("Message Hash: ").append(m[1]).append("\n");
            output.append("Recipient: ").append(m[2]).append("\n");
            output.append("Message: ").append(m[3]).append("\n");
            output.append("---------------------------------------------\n");
        }
        return output.toString();
    }

    // -------------------------------------------------------------------------
    // Getters for static arrays (used in tests)
    // -------------------------------------------------------------------------
    public static ArrayList<String[]> getSentMessages()        { return sentMessages; }
    public static ArrayList<String[]> getDisregardedMessages() { return disregardedMessages; }
    public static ArrayList<String[]> getStoredMessages()      { return storedMessages; }
    public static ArrayList<String>   getMessageHashes()       { return messageHashes; }
    public static ArrayList<String>   getMessageIDs()          { return messageIDs; }

    // -------------------------------------------------------------------------
    // Instance getters
    // -------------------------------------------------------------------------
    public String getMessageID()   { return messageID; }
    public String getRecipient()   { return recipient; }
    public String getMessage()     { return messageText; }
    public String getMessageHash() { return messageHash; }
    public int    getMessageNumber(){ return messageNumber; }

    // -------------------------------------------------------------------------
    // Test utility — resets all static state between unit tests
    // -------------------------------------------------------------------------
    public static void resetMessages() {
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
        totalMessagesSent = 0;
    }
}