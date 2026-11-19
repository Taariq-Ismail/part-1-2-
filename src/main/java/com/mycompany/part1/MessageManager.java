
package com.mycompany.part1;

import java.util.ArrayList;
import java.util.Arrays;

public class MessageManager {

    private final ArrayList<Message> messages = new ArrayList<>();

    // These arrays hold the data required for Part 3
    private Message[] sentMessages = new Message[0];
    private Message[] disregardedMessages = new Message[0];
    private Message[] storedMessages = new Message[0];
    private String[] messageHashes = new String[0];
    private String[] messageIds = new String[0];

    // Add a message and refresh the arrays
    public void addMessage(Message message) {
        messages.add(message);
        populateArrays();
    }

    // Rebuild all arrays after adding or deleting a message
    public void populateArrays() {
        ArrayList<Message> sent = new ArrayList<>();
        ArrayList<Message> disregarded = new ArrayList<>();
        ArrayList<Message> stored = new ArrayList<>();
        ArrayList<String> hashes = new ArrayList<>();
        ArrayList<String> ids = new ArrayList<>();

        for (Message message : messages) {
            switch (message.getStatus()) {
                case "Sent":
                    sent.add(message);
                    break;
                case "Disregarded":
                    disregarded.add(message);
                    break;
                case "Stored":
                    stored.add(message);
                    break;
                default:
                    break;
            }

            hashes.add(message.getMessageHash());
            ids.add(message.getMessageId());
        }

        sentMessages = sent.toArray(new Message[0]);
        disregardedMessages = disregarded.toArray(new Message[0]);
        storedMessages = stored.toArray(new Message[0]);
        messageHashes = hashes.toArray(new String[0]);
        messageIds = ids.toArray(new String[0]);
    }

    // Load previously stored messages from the JSON file
    public void loadStoredMessages() {
        Message[] loaded = MessageStore.readMessages();

        for (Message message : loaded) {
            boolean exists = messages.stream().anyMatch(
                    existing -> existing.getMessageId()
                            .equals(message.getMessageId()));

            if (!exists) {
                messages.add(message);
            }
        }

        populateArrays();
    }

    // Return the longest sent or stored message
    public Message getLongestMessage() {
        Message longest = null;

        for (Message message : messages) {
            if (message.getStatus().equals("Disregarded")) {
                continue;
            }

            if (longest == null
                    || message.getMessageText().length()
                    > longest.getMessageText().length()) {
                longest = message;
            }
        }

        return longest;
    }

    // Find the message matching a particular ID
    public Message searchById(String id) {
        for (Message message : messages) {
            if (message.getMessageId().equals(id)) {
                return message;
            }
        }

        return null;
    }

    // Search sent and stored messages for a recipient
    public Message[] searchByRecipient(String recipient) {
        ArrayList<Message> matches = new ArrayList<>();

        for (Message message : messages) {
            if (message.getRecipient().equals(recipient)
                    && !message.getStatus().equals("Disregarded")) {
                matches.add(message);
            }
        }

        return matches.toArray(new Message[0]);
    }

    // Delete a message using its unique hash
    public boolean deleteByHash(String hash) {
        Message found = null;

        for (Message message : messages) {
            if (message.getMessageHash().equalsIgnoreCase(hash)) {
                found = message;
                break;
            }
        }

        if (found == null) {
            return false;
        }

        // Update the JSON file before changing the in-memory arrays
        if (found.getStatus().equals("Stored")) {
            if (!MessageStore.deleteByHash(hash)) {
                return false;
            }
        }

        messages.remove(found);
        populateArrays();

        return true;
    }

    // Display the sender and recipient of stored messages
    public String displayStoredRecipients(String sender) {
        if (storedMessages.length == 0) {
            return "No stored messages.";
        }

        StringBuilder result = new StringBuilder();

        for (Message message : storedMessages) {
            result.append("Sender: ").append(sender)
                    .append("\nRecipient: ")
                    .append(message.getRecipient())
                    .append("\n\n");
        }

        return result.toString();
    }

    // Build the full report using all active messages
    public String displayReport() {
        if (messages.isEmpty()) {
            return "No messages available.";
        }

        StringBuilder report = new StringBuilder(
                "=== QUICKCHAT MESSAGE REPORT ===\n");

        for (Message message : messages) {
            report.append("\n")
                    .append(message.printMessage())
                    .append("\n------------------------\n");
        }

        report.append("\nTotal messages: ")
                .append(messages.size())
                .append("\nSent: ").append(sentMessages.length)
                .append("\nStored: ").append(storedMessages.length)
                .append("\nDisregarded: ")
                .append(disregardedMessages.length);

        return report.toString();
    }

    public Message[] getSentMessages() {
        return Arrays.copyOf(sentMessages, sentMessages.length);
    }

    public Message[] getStoredMessages() {
        return Arrays.copyOf(storedMessages, storedMessages.length);
    }

    public Message[] getDisregardedMessages() {
        return Arrays.copyOf(disregardedMessages,
                disregardedMessages.length);
    }

    public String[] getMessageHashes() {
        return Arrays.copyOf(messageHashes, messageHashes.length);
    }

    public String[] getMessageIds() {
        return Arrays.copyOf(messageIds, messageIds.length);
    }
}
