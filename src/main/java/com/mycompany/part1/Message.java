
package com.mycompany.part1;

import java.util.concurrent.ThreadLocalRandom;

public class Message {

    private final String messageId;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status;

    // Used when creating a new message
    public Message(int number, String recipient, String text) {
        this(String.valueOf(ThreadLocalRandom.current()
                .nextLong(1_000_000_000L, 10_000_000_000L)),
                number, recipient, text, null, "Pending");
    }

    // Also used when restoring messages from the JSON file
    public Message(String id, int number, String recipient,
            String text, String hash, String status) {

        this.messageId = id;
        this.messageNumber = number;
        this.recipient = recipient == null ? "" : recipient;
        this.messageText = text == null ? "" : text;
        this.status = status == null ? "Pending" : status;
        this.messageHash = hash == null
                ? createMessageHash() : hash;
    }

    // Check the message ID
    public Boolean checkMessageID() {
        return messageId != null
                && messageId.matches("\\d{10}");
    }

    // Validate the recipient number
    public String checkRecipientCell() {
        if (recipient.matches("\\+[1-9]\\d{1,10}")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or "
                + "does not contain an international code. "
                + "Please correct the number and try again.";
    }

    // Check the 250-character limit
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

    // Hash uses the first two ID digits and first/last words
    public String createMessageHash() {
        String[] words = messageText.trim().split("\\s+");

        String ending = messageText.isBlank()
                ? "EMPTY"
                : words[0] + words[words.length - 1];

        return (messageId.substring(0, 2) + ":"
                + messageNumber + ":" + ending).toUpperCase();
    }

    public void storeMessage() {
        MessageStore.append(this);
    }

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
