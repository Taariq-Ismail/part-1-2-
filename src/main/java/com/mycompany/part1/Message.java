
package com.mycompany.part1;

import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status = "Pending";

    public Message(int messageNumber, String recipient,
            String messageText) {

        // Give every new message a random 10-digit ID
        this.messageId = String.valueOf(
                ThreadLocalRandom.current().nextLong(
                        1_000_000_000L, 10_000_000_000L));

        this.messageNumber = messageNumber;
        this.recipient = recipient == null ? "" : recipient;
        this.messageText = messageText == null ? "" : messageText;
        this.messageHash = createMessageHash();
    }

    // Make sure the generated ID is exactly 10 digits
    public Boolean checkMessageID() {
        return messageId.matches("\\d{10}");
    }

    // Validate the recipient's international phone number
    public String checkRecipientCell() {
        if (recipient.matches("\\+[1-9]\\d{1,10}")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Messages must contain text and cannot exceed 250 characters
    public String checkMessageLength() {
        if (messageText.isBlank()) {
            return "Message cannot be empty.";
        }

        if (messageText.length() > 250) {
            return "Message exceeds 250 characters by "
                    + (messageText.length() - 250)
                    + "; please reduce the size.";
        }

        return "Message ready to send.";
    }

    // Create a hash using the ID, message number and first/last words
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");

        String ends = messageText.isBlank()
                ? "EMPTY"
                : words[0] + words[words.length - 1];

        return (messageId.substring(0, 2)
                + ":" + messageNumber
                + ":" + ends).toUpperCase();
    }

    // Save the message to our JSON file
    public void storeMessage() {
        MessageStore.append(this);
    }

    // Display all the details for one message
    public String printMessage() {
        return "Message ID: " + messageId
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText
                + "\nStatus: " + status;
    }

    public String getMessageId() {
        return messageId;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
