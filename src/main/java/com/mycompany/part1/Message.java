package com.mycompany.part1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Message {
    private String messageId;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // "Sent", "Stored", "Disregard"

    // Normal constructor (used when user creates a message)
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageId = generateMessageId();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.flag = "Sent";
    }

    // Constructor used when loading from JSON or test data
    public Message(String messageId, int messageNumber, String recipient, 
                   String messageText, String messageHash, String flag) {
        this.messageId = messageId;
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.flag = flag;
    }

    private String generateMessageId() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public Boolean checkMessageID() {
        return this.messageId != null && this.messageId.length() <= 10;
    }

    public String checkRecipientCell() {
        if (this.recipient.startsWith("+") && this.recipient.length() <= 12) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    public String checkMessageLength() {
        if (this.messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = this.messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

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

    // Clean JSON writing (proper array format)
    public void storeMessage() {
        try {
            Path path = Paths.get("messages.json");
            List<String> lines = new ArrayList<>();

            if (Files.exists(path)) {
                lines = Files.readAllLines(path);
            }

            // Clean up existing file so we can append properly
            if (!lines.isEmpty() && lines.get(lines.size() - 1).trim().equals("]")) {
                lines.remove(lines.size() - 1);
                if (!lines.isEmpty()) {
                    String last = lines.get(lines.size() - 1).trim();
                    if (last.endsWith(",")) {
                        lines.set(lines.size() - 1, last.substring(0, last.length() - 1));
                    }
                }
            } else if (lines.isEmpty()) {
                lines.add("[");
            }

            String jsonObject = String.format(
                "  {\"messageId\":\"%s\",\"messageNumber\":%d,\"recipient\":\"%s\",\"messageText\":\"%s\",\"messageHash\":\"%s\",\"flag\":\"%s\"}",
                messageId,
                messageNumber,
                recipient.replace("\"", "\\\""),
                messageText.replace("\"", "\\\""),
                messageHash,
                flag
            );

            if (lines.size() > 1) {
                // Add comma to previous object
                String prev = lines.get(lines.size() - 1);
                if (!prev.trim().endsWith(",") && !prev.trim().equals("[")) {
                    lines.set(lines.size() - 1, prev + ",");
                }
            }

            lines.add(jsonObject);
            lines.add("]");

            Files.write(path, lines);
        } catch (IOException e) {
            System.out.println("Error saving to JSON: " + e.getMessage());
        }
    }

    // Getters
    public String getMessageId() { return messageId; }
    public int getMessageNumber() { return messageNumber; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getFlag() { return flag; }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Message ID : " + messageId +
               "\nHash       : " + messageHash +
               "\nRecipient  : " + recipient +
               "\nMessage    : " + messageText +
               "\nFlag       : " + flag;
    }
}