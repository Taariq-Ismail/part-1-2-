package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        String longMessage = "A".repeat(260); // 10 characters over
        Message msg = new Message(0, "+27718693002", longMessage);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.checkMessageLength());
    }

    @Test
    public void testRecipientFormatSuccess() {
        Message msg = new Message(0, "+27718693002", "Test Message");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientFormatFailure() {
        Message msg = new Message(0, "08575975889", "Test Message");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashCreation() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight Thanks");
        String hash = msg.getMessageHash();
        assertTrue(hash.contains(":0:HI"));
        assertTrue(hash.endsWith("THANKS"));
        assertEquals(hash, hash.toUpperCase());
    }
    
    @Test
    public void testMessageIdCreation() {
        Message msg = new Message(0, "+27718693002", "Test");
        assertTrue(msg.checkMessageID());
        assertEquals(10, msg.getMessageId().length());
    }
}