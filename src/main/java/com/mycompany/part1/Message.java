package com.mycompany.part1;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Message {
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageId = generateMessageId();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
    }

    private String generateMessageId() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    // Brief requires Boolean return type
    public Boolean checkMessageID() {
        return this.messageId != null && this.messageId.length() <= 10;
    }

    // Brief requires String return type with exact messages
    public String checkRecipientCell() {
        if (this.recipient.startsWith("+") && this.recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Brief requires String return type with exact messages
    public String checkMessageLength() {
        if (this.messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = this.messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    // Exact Hash string manipulation from brief
    public String createMessageHash() {
        String firstTwoId = this.messageId.substring(0, 2);
        String[] words = this.messageText.trim().split("\\s+");
        
        if (words.length == 0 || this.messageText.isEmpty()) {
            return (firstTwoId + ":" + this.messageNumber + ":EMPTY").toUpperCase();
        }

        String firstWord = words[0];
        String lastWord = words[words.length - 1];

        return (firstTwoId + ":" + this.messageNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    // Saves to JSON properly
    public void storeMessage() {
        String json = "{\n" +
                "  \"messageId\": \"" + this.messageId + "\",\n" +
                "  \"messageNumber\": " + this.messageNumber + ",\n" +
                "  \"recipient\": \"" + this.recipient + "\",\n" +
                "  \"messageText\": \"" + this.messageText + "\",\n" +
                "  \"messageHash\": \"" + this.messageHash + "\"\n" +
                "}\n";
        try (FileWriter file = new FileWriter("messages.json", true)) {
            file.write(json);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageId() { return messageId; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
}