/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.util.ArrayList;

/**
 * Unit Tests for the Message Class 
 *
 * Covers:
 *  - Message ID generation and length
 *  - Recipient cell number validation
 *  - Message length validation
 *  - Message hash correctness
 *  - Send / Disregard / Store actions
 *  - Total messages counter
 *  - Part 3: array population, longest message, search by ID,
 *    search by recipient, delete by hash, and full report
 
 */
public class messageTest {

    @Before
    public void setUp() {
        // Reset all static state before every test so nothing carries over
        message.resetMessages();
    }

    
    // Message ID Tests
   

    @Test
    public void testMessageIDLengthSuccess() {
        // A generated ID must be no more than 10 characters
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue("Message ID should not be more than 10 characters", msg.checkMessageID());
    }

    @Test
    public void testMessageIDGenerated() {
        // An ID must actually be created (not null or empty)
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertNotNull("Message ID should be generated", msg.getMessageID());
        System.out.println("Message ID generated: " + msg.getMessageID());
    }
    
    // Recipient Cell Number Validation Tests
    
    @Test
    public void testCheckRecipientCellSuccess() {
        // A number starting with '+' should be accepted
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals(
            "Cell phone number successfully captured.",
            msg.checkRecipientCell()
        );
    }

    @Test
    public void testCheckRecipientCellFailure() {
        // A number without the international '+' code must be rejected
        message msg = new message(0, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            msg.checkRecipientCell()
        );
    }

    // Message Length Tests
    

    @Test
    public void testMessageLengthSuccess() {
        // A message within 250 characters should return the ready message
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        // A 260-character message is 10 over the limit — must state the exact overage
        String longMessage = "A".repeat(260);
        message msg = new message(0, "+27718693002", longMessage);
        assertEquals(
            "Message exceeds 250 characters by 10; please reduce the size.",
            msg.checkMessageLength()
        );
    }
    
    // Message Hash Tests
    

    @Test
    public void testMessageHashCorrect() {
        // Hash format: first two digits of ID : message number : FIRSTWORD+LASTWORD (caps)
        // ID "0012345678" → prefix "00", number 0, first word "Hi", last word "tonight?" → HITONIGHT
        message msg = new message(0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?", "0012345678");
        assertEquals("00:0:HITONIGHT", msg.getMessageHash());
    }

    
    // Send / Disregard / Store Action Tests
    

    @Test
    public void testSentMessageSend() {
        // Choice "1" must return the exact success string (note the period)
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent.", msg.sentMessage("1"));
    }

    @Test
    public void testSentMessageDisregard() {
        // Choice "2" must return the delete-prompt string (note the period)
        message msg = new message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message.", msg.sentMessage("2"));
    }

    @Test
    public void testSentMessageStore() {
        // Choice "3" must return the store-success string (note the period)
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored.", msg.sentMessage("3"));
    }

    // Total Messages Counter Test.
    

    @Test
    public void testReturnTotalMessages() {
        // Only choice "1" (Send) increments the total; we send two expect 2
        message msg1 = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg1.sentMessage("1");

        message msg2 = new message(1, "+27718693002", "Hi Keegan, did you receive the payment?");
        msg2.sentMessage("1");

        assertEquals(2, msg1.returnTotalMessages());
    }

    
    // Part 3 Tests — Array population, searching, deleting, reporting
    //
    // Test data (from PoE brief):
    //   Message 1: +27834557896  "Did you get the cake?"                          → Sent
    //   Message 2: +27838884567  "Where are you? You are late! I have asked
    //                             you to be on time."                              → Stored
    //   Message 3: +27834484567  "Yohoooo, I am at your gate."                   → Disregard
    //   Message 4: 0838884567    "It is dinner time !"                            → Sent
    //   Message 5: +27838884567  "Ok, I am leaving without you."                 → Stored
    

    @Test
    public void testSentMessagesArrayPopulated() {
        // Sent Messages array must contain exactly the two "Sent" messages in order
        message msg1 = new message(0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage("1");

        message msg4 = new message(1, "0838884567", "It is dinner time !");
        msg4.sentMessage("1");

        ArrayList<String[]> sent = message.getSentMessages();
        assertEquals("Did you get the cake?", sent.get(0)[3]);
        assertEquals("It is dinner time !",   sent.get(1)[3]);
    }

    @Test
    public void testDisregardedMessagesArrayPopulated() {
        // Disregarded Messages array must contain Message 3
        message msg3 = new message(2, "+27834484567", "Yohoooo, I am at your gate.");
        msg3.sentMessage("2");

        ArrayList<String[]> discarded = message.getDisregardedMessages();
        assertEquals(1, discarded.size());
        assertEquals("Yohoooo, I am at your gate.", discarded.get(0)[3]);
    }

    @Test
    public void testMessageHashesArrayPopulated() {
        // Sending a message must add its hash to the messageHashes array
        message msg1 = new message(0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage("1");

        ArrayList<String> hashes = message.getMessageHashes();
        assertTrue("Hash array must contain the sent message hash",
                hashes.contains(msg1.getMessageHash()));
    }

    @Test
    public void testMessageIDsArrayPopulated() {
        // Sending a message must add its ID to the messageIDs array
        message msg1 = new message(0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage("1");

        ArrayList<String> ids = message.getMessageIDs();
        assertTrue("ID array must contain the sent message ID",
                ids.contains(msg1.getMessageID()));
    }

    

    @Test
    public void testSearchByMessageID() {
        // Search for Message 4 using its ID (0838884567) — must return recipient and message
        message msg4 = new message(1, "0838884567", "It is dinner time !", "0838884567");
        msg4.sentMessage("1");

        String result = message.searchByMessageID("0838884567");
        assertTrue("Result must contain the recipient", result.contains("0838884567"));
        assertTrue("Result must contain the message",   result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchByRecipient() {
        // Both Message 2 and Message 5 were sent to +27838884567 — both must be returned
        message msg2 = new message(0, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage("3");

        message msg5 = new message(1, "+27838884567", "Ok, I am leaving without you.");
        msg5.sentMessage("3");

        ArrayList<String> found = message.searchByRecipient("+27838884567");
        assertTrue(found.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(found.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        // Delete Message 2 using its hash — must return the rubric-specified success string
        message msg2 = new message(0, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        msg2.sentMessage("3");

        String hash   = msg2.getMessageHash();
        String result = message.deleteMessageByHash(hash);

        assertEquals(
            "Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
            result
        );
    }

    @Test
    public void testDisplayReport() {
        // Report must include Message Hash, Recipient and Message for each sent message
        message msg1 = new message(0, "+27834557896", "Did you get the cake?");
        msg1.sentMessage("1");

        String report = message.displayStoredReport();
        assertTrue("Report must contain the message text",    report.contains("Did you get the cake?"));
        assertTrue("Report must contain the recipient",        report.contains("+27834557896"));
        assertTrue("Report must contain the message hash",    report.contains(msg1.getMessageHash()));
    }
}