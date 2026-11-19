
package com.mycompany.part1;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

public final class MessageStore {

    private static Path file = Path.of("messages.json");

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private MessageStore() {
    }

    // Use a separate file when running unit tests
    public static void setFile(Path path) {
        file = path;
    }

    // Read the JSON file into an array of messages
    public static synchronized Message[] readMessages() {
        try {
            if (!Files.exists(file)) {
                return new Message[0];
            }

            String json = Files.readString(
                    file, StandardCharsets.UTF_8);

            if (json.isBlank()) {
                return new Message[0];
            }

            Message[] messages = gson.fromJson(
                    json, Message[].class);

            if (messages == null) {
                throw new IllegalStateException(
                        "JSON must contain a message array.");
            }

            return messages;

        } catch (IOException | RuntimeException ex) {
            throw new IllegalStateException(
                    "Could not read messages: " + ex.getMessage(), ex);
        }
    }

    // Save the entire array to the JSON file
    public static synchronized void saveMessages(Message[] messages) {
        try {
            String json = gson.toJson(messages);

            Files.writeString(
                    file, json, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Could not save messages: " + ex.getMessage(), ex);
        }
    }

    // Add one message without losing the existing messages
    public static synchronized void append(Message message) {
        ArrayList<Message> messages = new ArrayList<>(
                Arrays.asList(readMessages()));

        messages.add(message);

        saveMessages(messages.toArray(new Message[0]));
    }

    // Delete the matching message and update the JSON file
    public static synchronized boolean deleteByHash(String hash) {
        ArrayList<Message> messages = new ArrayList<>(
                Arrays.asList(readMessages()));

        boolean removed = messages.removeIf(
                message -> message.getMessageHash().equalsIgnoreCase(hash));

        if (removed) {
            saveMessages(messages.toArray(new Message[0]));
        }

        return removed;
    }
}
