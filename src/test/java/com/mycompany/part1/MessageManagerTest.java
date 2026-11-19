
package com.mycompany.part1;

import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

public class MessageManagerTest {

    @TempDir
    Path tempDir;

    // These are the five messages provided in the assignment
    private MessageManager createTestData() {
        MessageManager manager = new MessageManager();

        Message m1 = new Message(1, "+27834557896",
                "Did you get the cake?");
        m1.setStatus("Sent");

        Message m2 = new Message(2, "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        m2.setStatus("Stored");

        Message m3 = new Message(3, "+27834484567",
                "Yohoooo, I am at your gate.");
        m3.setStatus("Disregarded");

        Message m4 = new Message(4, "0838884567",
                "It is dinner time !");
        m4.setStatus("Sent");

        Message m5 = new Message(5, "+27838884567",
                "Ok, I am leaving without you.");
        m5.setStatus("Stored");

        manager.addMessage(m1);
        manager.addMessage(m2);
        manager.addMessage(m3);
        manager.addMessage(m4);
        manager.addMessage(m5);

        return manager;
    }

    @Test
    public void testSentMessagesArray() {
        MessageManager manager = createTestData();

        // The rubric expects messages 1 and 4
        assertEquals(2, manager.getSentMessages().length);
        assertEquals("Did you get the cake?",
                manager.getSentMessages()[0].getMessageText());
        assertEquals("It is dinner time !",
                manager.getSentMessages()[1].getMessageText());
    }

    @Test
    public void testAllArraysPopulated() {
        MessageManager manager = createTestData();

        assertEquals(2, manager.getSentMessages().length);
        assertEquals(2, manager.getStoredMessages().length);
        assertEquals(1, manager.getDisregardedMessages().length);
        assertEquals(5, manager.getMessageHashes().length);
        assertEquals(5, manager.getMessageIds().length);
    }

    @Test
    public void testLongestMessage() {
        MessageManager manager = createTestData();

        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                manager.getLongestMessage().getMessageText());
    }

    @Test
    public void testSearchMessageId() {
        MessageManager manager = createTestData();

        // Use the generated ID of test message 4
        String id = manager.getSentMessages()[1].getMessageId();

        Message result = manager.searchById(id);

        assertNotNull(result);
        assertEquals("0838884567", result.getRecipient());
        assertEquals("It is dinner time !", result.getMessageText());
    }

    @Test
    public void testSearchRecipient() {
        MessageManager manager = createTestData();

        Message[] results = manager.searchByRecipient("+27838884567");

        assertEquals(2, results.length);
        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                results[0].getMessageText());
        assertEquals(
                "Ok, I am leaving without you.",
                results[1].getMessageText());
    }

    @Test
    public void testDeleteByHash() {
        MessageManager manager = createTestData();

        // Save the two stored messages to a temporary JSON file
        MessageStore.setFile(tempDir.resolve("messages.json"));
        MessageStore.saveMessages(manager.getStoredMessages());

        String hash = manager.getStoredMessages()[0].getMessageHash();

        assertTrue(manager.deleteByHash(hash));
        assertEquals(1, manager.getStoredMessages().length);

        // Check that the deleted message is gone from the file too
        assertEquals(1, MessageStore.readMessages().length);
        assertEquals(
                "Ok, I am leaving without you.",
                MessageStore.readMessages()[0].getMessageText());
    }

    @Test
    public void testReadJsonIntoArray() {
        MessageManager manager = createTestData();

        MessageStore.setFile(tempDir.resolve("messages.json"));
        MessageStore.saveMessages(manager.getStoredMessages());

        Message[] loaded = MessageStore.readMessages();

        assertEquals(2, loaded.length);
        assertEquals("Stored", loaded[0].getStatus());
        assertEquals("+27838884567", loaded[0].getRecipient());
    }

    @Test
    public void testDisplayReport() {
        MessageManager manager = createTestData();

        String report = manager.displayReport();

        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
        assertTrue(report.contains("Total messages: 5"));
    }

    @Test
    public void testStoredSenderAndRecipient() {
        MessageManager manager = createTestData();

        String output = manager.displayStoredRecipients("+27831234567");

        assertTrue(output.contains("Sender: +27831234567"));
        assertTrue(output.contains("Recipient: +27838884567"));
    }
}
