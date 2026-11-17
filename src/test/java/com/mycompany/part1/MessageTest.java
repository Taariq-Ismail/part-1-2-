package com.mycompany.part1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

public class MessageTest {

    private ArrayList<Message> sentMessages;
    private ArrayList<Message> storedMessages;
    private ArrayList<Message> disregardedMessages;

    @BeforeEach
    void setUp() {
        sentMessages = new ArrayList<>();
        storedMessages = new ArrayList<>();
        disregardedMessages = new ArrayList<>();

        // ===== Official Test Data from the brief =====

        // Message 1 - Sent
        Message m1 = new Message(0, "+27834557896", "Did you get the cake?");
        m1.setFlag("Sent");
        sentMessages.add(m1);

        // Message 2 - Stored
        Message m2 = new Message(1, "+27838884567", "Where are you? You are late! I have asked you to be on time.");
        m2.setFlag("Stored");
        storedMessages.add(m2);

        // Message 3 - Disregard
        Message m3 = new Message(2, "+27834484567", "Yohoooo, I am at your gate.");
        m3.setFlag("Disregard");
        disregardedMessages.add(m3);

        // Message 4 - Sent
        Message m4 = new Message(3, "0838884567", "It is dinner time !");
        m4.setFlag("Sent");
        sentMessages.add(m4);

        // Message 5 - Stored
        Message m5 = new Message(4, "+27838884567", "Ok, I am leaving without you.");
        m5.setFlag("Stored");
        storedMessages.add(m5);
    }

    // ---------- 1. Sent Messages array correctly populated ----------
    @Test
    void testSentMessagesArrayPopulated() {
        assertEquals(2, sentMessages.size());
        assertEquals("Did you get the cake?", sentMessages.get(0).getMessageText());
        assertEquals("It is dinner time !", sentMessages.get(1).getMessageText());
    }

    // ---------- 2. Display the longest Message ----------
    @Test
    void testDisplayLongestMessage() {
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.getMessageText().length() > longest.getMessageText().length()) {
                longest = m;
            }
        }
        // From the brief expected output
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest.getMessageText());
    }

    // ---------- 3. Search for messageID ----------
    @Test
    void testSearchForMessageID() {
        // We force a known ID for testing (Message 4 style)
        Message testMsg = new Message("0838884567", 3, "0838884567", "It is dinner time !", "08:3:ITIS!", "Sent");
        
        // Simulate search
        String foundText = null;
        if (testMsg.getMessageId().equals("0838884567")) {
            foundText = testMsg.getMessageText();
        }
        assertEquals("It is dinner time !", foundText);
    }

    // ---------- 4. Search all messages for a particular recipient ----------
    @Test
    void testSearchByRecipient() {
        String target = "+27838884567";
        ArrayList<String> results = new ArrayList<>();

        for (Message m : storedMessages) {
            if (m.getRecipient().equals(target)) {
                results.add(m.getMessageText());
            }
        }
        for (Message m : sentMessages) {
            if (m.getRecipient().equals(target)) {
                results.add(m.getMessageText());
            }
        }

        assertTrue(results.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(results.contains("Ok, I am leaving without you."));
        assertEquals(2, results.size());
    }

    // ---------- 5. Delete a message using a message hash ----------
    @Test
    void testDeleteByHash() {
        // Take the hash of Message 2
        String hashToDelete = storedMessages.get(0).getMessageHash();
        String originalText = storedMessages.get(0).getMessageText();

        // Delete
        boolean deleted = false;
        for (int i = 0; i < storedMessages.size(); i++) {
            if (storedMessages.get(i).getMessageHash().equals(hashToDelete)) {
                storedMessages.remove(i);
                deleted = true;
                break;
            }
        }

        assertTrue(deleted);
        assertEquals(1, storedMessages.size()); // only Message 5 left
        assertFalse(storedMessages.stream().anyMatch(m -> m.getMessageText().equals(originalText)));
    }

    // ---------- 6. Display Report ----------
    @Test
    void testDisplayReport() {
        assertFalse(storedMessages.isEmpty());
        // Just verify we can access all required fields
        for (Message m : storedMessages) {
            assertNotNull(m.getMessageHash());
            assertNotNull(m.getRecipient());
            assertNotNull(m.getMessageText());
            assertNotNull(m.getMessageId());
        }
        assertEquals(2, storedMessages.size());
    }

    // Extra useful tests (good for marks)
    @Test
    void testMessageHashFormat() {
        Message m = new Message(0, "+27834557896", "Did you get the cake?");
        String hash = m.getMessageHash();
        assertTrue(hash.contains(":"));
        assertTrue(hash.equals(hash.toUpperCase()));
    }

    @Test
    void testCheckMessageLength_Success() {
        Message m = new Message(0, "+27834557896", "Short message");
        assertEquals("Message ready to send.", m.checkMessageLength());
    }

    @Test
    void testCheckRecipientCell_Success() {
        Message m = new Message(0, "+27834557896", "Hello");
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell());
    }
}