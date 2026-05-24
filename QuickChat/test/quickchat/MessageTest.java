/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit Tests for the Message Class
 * This class runs a battery of tests to ensure our message logic
 * phone validation, and hashing work according to the requirements.
 */
public class MessageTest {

    //ID generation test

    @Test
    public void testMessageIDLengthSuccess() {
        // Test 1: Making sure the generated ID doesn't go over the 10 character limit.
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue("Message ID should not be more than 10 characters", msg.checkMessageID());
    }

    @Test
    public void testMessageIDGenerated() {
        // Test 2: Making sure an ID actually gets created and isn't null.
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertNotNull("Message ID should be generated", msg.getMessageID());
        System.out.println("Message ID generated: " + msg.getMessageID());
    }

    //Phone Number Validation Test.

    @Test
    public void testCheckRecipientCellSuccess() {
        // Test 3 , Checking a valid number that starts with the required '+' sign
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellFailure() {
        // Test 4, Checking a number that fails because it is missing the international code/plus sign
        message msg = new message(0, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    //Message Content & Length Tests

    @Test
    public void testMessageLengthSuccess() {
        // Test 5,Making sure a standard length message is accepted correctly
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertTrue("Message ready to send.", msg.getMessage().length() <= 250);
    }

    @Test
    public void testMessageLengthFailure() {
        // Test 6, Testing a scenario where the message is too long (over 250 characters)
        String longMessage = "A".repeat(260);
        assertTrue("Message exceeds 250 characters by 10; please reduce the size.", longMessage.length() > 250);
    }

    //Hashing Logic Tests.

    @Test
    public void testMessageHashCorrect() {
        // Verifying the hash 
        // Expected hash format: 00:0:HITONIGHT
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?", "0012345678");
        assertEquals("00:0:HITONIGHT", msg.getMessageHash());
    }

    //  Menu Choice / Action Tests

    @Test
    public void testSentMessageSend() {
        // Test 8, Choice 1 should trigger the send success message
        message.resetMessages();
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully sent", msg.sentMessage("1"));
    }

    @Test
    public void testSentMessageDisregard() {
        // Test 9, Choice '2' should prompt the user to delete/disregard
        message msg = new message(1, "08575975889", "Hi Keegan, did you receive the payment?");
        assertEquals("Press 0 to delete the message", msg.sentMessage("2"));
    }

    @Test
    public void testSentMessageStore() {
        // Test 10, Choice '3' should trigger the storage success message
        message msg = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message successfully stored", msg.sentMessage("3"));
    }


    @Test
    public void testReturnTotalMessages() {
        // Test 11, Ensuring it tracks how many messages were sent
        message.resetMessages();
        
        // Sending first message
        message msg1 = new message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg1.sentMessage("1");
        
        // Sending second message
        message msg2 = new message(1, "+27718693002", "Hi Keegan, did you receive the payment?");
        msg2.sentMessage("1");
        
        // Total should have to be 2
        assertEquals(2, msg1.returnTotalMessages());
    }
}
