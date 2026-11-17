
package com.mycompany.part1;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class MessageStore {

    private static Path file = Path.of("messages.json");

    // This class only contains methods for saving messages
    private MessageStore() {
    }

    // Allows the test file to use a different save location
    public static void setFile(Path path) {
        file = path;
    }

    // Escape characters that could breaks the JSON format
    private static String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Add a message while keeping the file as a valid JSON array
    public static synchronized void append(Message message) {

        String object = "  {\n"
                + "    \"messageId\": \""
                + escape(message.getMessageId()) + "\",\n"
                + "    \"messageNumber\": "
                + message.getMessageNumber() + ",\n"
                + "    \"recipient\": \""
                + escape(message.getRecipient()) + "\",\n"
                + "    \"messageText\": \""
                + escape(message.getMessageText()) + "\",\n"
                + "    \"messageHash\": \""
                + escape(message.getMessageHash()) + "\",\n"
                + "    \"status\": \""
                + escape(message.getStatus()) + "\"\n"
                + "  }";

        try {
            // Start with an empty array if no file exists yet
            String old = Files.exists(file)
                    ? Files.readString(
                            file, StandardCharsets.UTF_8).trim()
                    : "[]";

            if (!old.startsWith("[") || !old.endsWith("]")) {
                throw new IOException(
                        "Existing messages.json is not a JSON array.");
            }

            String inner = old.substring(
                    1, old.length() - 1).trim();

            String updated = "[\n"
                    + (inner.isEmpty() ? "" : inner + ",\n")
                    + object + "\n]\n";

            Files.writeString(
                    file, updated, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            throw new IllegalStateException(
                    "Unable to save message: " + ex.getMessage(), ex);
        }
    }
}
