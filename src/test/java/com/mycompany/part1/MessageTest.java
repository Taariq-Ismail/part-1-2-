
package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message(
                0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight?");

        assertEquals(
                "Message ready to send.",
                msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        // Create a message that is 10 characters too long
        String longMessage = "A".repeat(260);

        Message msg = new Message(
                0, "+27718693002", longMessage);

        assertEquals(
                "Message exceeds 250 characters by 10; "
                + "please reduce the size.",
                msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthBoundary() {
        // Exactly 250 characters should be accepted
        Message msg = new Message(
                1, "+27718693002", "A".repeat(250));

        assertEquals(
                "Message ready to send.",
                msg.checkMessageLength());
    }

    @Test
    public void testEmptyMessage() {
        Message msg = new Message(
                1, "+27718693002", "");

        assertEquals(
                "Message cannot be empty.",
                msg.checkMessageLength());
    }

    @Test
    public void testRecipientFormatSuccess() {
        Message msg = new Message(
                0, "+27718693002", "Test Message");

        assertEquals(
                "Cell phone number successfully captured.",
                msg.checkRecipientCell());
    }

    @Test
    public void testRecipientFormatFailure() {
        Message msg = new Message(
                0, "08575975889", "Test Message");

        assertEquals(
                "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.",
                msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashCreation() {
        Message msg = new Message(
                0, "+27718693002",
                "Hi Mike, can you join us for dinner tonight Thanks");

        String hash = msg.getMessageHash();

        // The hash contains the message number and first/last words
        assertTrue(hash.contains(":0:HI"));
        assertTrue(hash.endsWith("THANKS"));
        assertEquals(hash, hash.toUpperCase());
    }

    @Test
    public void testMessageIdCreation() {
        Message msg = new Message(
                0, "+27718693002", "Test");

        // Every generated ID must have 10 digits
        assertTrue(msg.checkMessageID());
        assertEquals(10, msg.getMessageId().length());
    }

    @Test
    public void testMessageStatus() {
        Message msg = new Message(
                1, "+27718693002", "Hello");

        // A new message starts as pending
        assertEquals("Pending", msg.getStatus());

        msg.setStatus("Sent");

        assertEquals("Sent", msg.getStatus());
    }

    @Test
    public void testPrintMessage() {
        Message msg = new Message(
                1, "+27718693002", "Hello there");

        msg.setStatus("Sent");

        String result = msg.printMessage();

        assertTrue(result.contains("Hello there"));
        assertTrue(result.contains("+27718693002"));
        assertTrue(result.contains("Status: Sent"));
        assertTrue(result.contains("Message Hash:"));
    }
}
