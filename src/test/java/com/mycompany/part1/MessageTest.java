
package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageId() {
        Message message = new Message(
                1, "+27834557896", "Hello");

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageId().length());
    }

    @Test
    public void testValidRecipient() {
        Message message = new Message(
                1, "+27834557896", "Hello");

        assertEquals("Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    public void testInvalidRecipient() {
        Message message = new Message(
                1, "0838884567", "Hello");

        assertTrue(message.checkRecipientCell()
                .contains("incorrectly formatted"));
    }

    @Test
    public void testMessageLength() {
        Message message = new Message(
                1, "+27834557896", "A".repeat(250));

        assertEquals("Message ready to send.",
                message.checkMessageLength());
    }

    @Test
    public void testMessageTooLong() {
        Message message = new Message(
                1, "+27834557896", "A".repeat(260));

        assertTrue(message.checkMessageLength()
                .contains("exceeds 250 characters by 10"));
    }

    @Test
    public void testMessageHash() {
        Message message = new Message(
                1, "+27834557896", "Did you get the cake?");

        String hash = message.getMessageHash();

        assertTrue(hash.endsWith("DIDCAKE?"));
        assertTrue(hash.contains(":1:"));
    }

    @Test
    public void testMessageStatus() {
        Message message = new Message(
                1, "+27834557896", "Hello");

        message.setStatus("Sent");

        assertEquals("Sent", message.getStatus());
    }

    @Test
    public void testPrintMessage() {
        Message message = new Message(
                1, "+27834557896", "Hello");

        String output = message.printMessage();

        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Message Hash:"));
        assertTrue(output.contains("Hello"));
    }
}
