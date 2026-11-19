
package com.mycompany.part1;

import java.util.Scanner;

public class QuickChatApp {

    private static final Scanner input = new Scanner(System.in);
    private static final MessageManager manager = new MessageManager();

    public static void main(String[] args) {

        Login login = new Login();

        System.out.println("=== QUICKCHAT REGISTRATION ===");

        String first = readText("First name: ");
        String last = readText("Last name: ");

        String username;
        do {
            username = readText("Username: ");
            if (!login.checkUserName(username)) {
                System.out.println("Use an underscore and max 5 characters.");
            }
        } while (!login.checkUserName(username));

        String password;
        do {
            password = readText("Password: ");
            if (!login.checkPasswordComplexity(password)) {
                System.out.println("Password needs 8 characters, uppercase, "
                        + "number and special character.");
            }
        } while (!login.checkPasswordComplexity(password));

        String phone;
        do {
            phone = readText("Cell number (+27...): ");
            if (!login.checkCellPhoneNumber(phone)) {
                System.out.println("Invalid international number.");
            }
        } while (!login.checkCellPhoneNumber(phone));

        System.out.println(login.registerUser(
                first, last, username, password, phone));

        // Keep asking until the correct login details are entered
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n=== LOGIN ===");

            String user = readText("Username: ");
            String pass = readText("Password: ");

            loggedIn = login.loginUser(user, pass);
            System.out.println(login.returnLoginStatus(loggedIn));
        }

        // Load any stored messages from previous runs
        try {
            manager.loadStoredMessages();
        } catch (IllegalStateException ex) {
            System.out.println("Stored messages could not be loaded: "
                    + ex.getMessage());
            System.out.println("Fix the JSON file before continuing.");
            return;
        }

        System.out.println("\nWelcome to QuickChat!");

        int nextNumber = manager.getMessageIds().length + 1;

        // Main menu runs until the user chooses Quit
        while (true) {
            System.out.println("\n=== MAIN MENU ===");
            System.out.println("1. Send Messages");
            System.out.println("2. Show Recently Sent Messages");
            System.out.println("3. Quit");
            System.out.println("4. Stored Messages");

            String option = readText("Choose: ");

            switch (option) {
                case "1":
                    int count = readPositiveInt("How many messages? ");

                    for (int i = 0; i < count; i++) {
                        sendMessage(nextNumber++);
                    }
                    break;

                case "2":
                    Message[] sent = manager.getSentMessages();

                    if (sent.length == 0) {
                        System.out.println("No messages sent yet.");
                    } else {
                        for (Message message : sent) {
                            System.out.println(message.printMessage());
                            System.out.println();
                        }
                    }
                    break;

                case "3":
                    System.out.println("Goodbye!");
                    return;

                case "4":
                    storedMessagesMenu(phone);
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Create a message and let the user choose its status
    private static void sendMessage(int number) {
        String recipient;

        do {
            recipient = readText("Recipient (+country code): ");

            if (!recipient.matches("\\+[1-9]\\d{1,10}")) {
                System.out.println("Invalid recipient number.");
            }

        } while (!recipient.matches("\\+[1-9]\\d{1,10}"));

        String text;

        do {
            text = readText("Message: ");

            if (text.length() > 250) {
                System.out.println("Message is too long by "
                        + (text.length() - 250) + " characters.");
            }

        } while (text.length() > 250);

        Message message = new Message(number, recipient, text);

        while (true) {
            System.out.println("\n1. Send");
            System.out.println("2. Disregard");
            System.out.println("3. Store for later");

            String action = readText("Choose: ");

            switch (action) {
                case "1":
                    message.setStatus("Sent");
                    manager.addMessage(message);
                    System.out.println("Message successfully sent.");
                    System.out.println(message.printMessage());
                    return;

                case "2":
                    message.setStatus("Disregarded");
                    manager.addMessage(message);
                    System.out.println("Message disregarded.");
                    return;

                case "3":
                    message.setStatus("Stored");

                    try {
                        message.storeMessage();
                        manager.addMessage(message);
                        System.out.println("Message stored successfully.");
                    } catch (IllegalStateException ex) {
                        System.out.println("Save failed: " + ex.getMessage());
                    }
                    return;

                default:
                    System.out.println("Choose 1, 2 or 3.");
            }
        }
    }

    // All six features required for the Part 3 stored message menu
    private static void storedMessagesMenu(String sender) {
        while (true) {
            System.out.println("\n=== STORED MESSAGES ===");
            System.out.println("1. Display Senders and Recipients");
            System.out.println("2. Display Longest Message");
            System.out.println("3. Search by Message ID");
            System.out.println("4. Search by Recipient");
            System.out.println("5. Delete by Message Hash");
            System.out.println("6. Display Message Report");
            System.out.println("7. Back to Main Menu");

            String option = readText("Choose: ");

            switch (option) {
                case "1":
                    System.out.println(
                            manager.displayStoredRecipients(sender));
                    break;

                case "2":
                    Message longest = manager.getLongestMessage();

                    if (longest == null) {
                        System.out.println("No messages available.");
                    } else {
                        System.out.println("Longest message:");
                        System.out.println(longest.getMessageText());
                    }
                    break;

                case "3":
                    String id = readText("Enter Message ID: ");
                    Message found = manager.searchById(id);

                    if (found == null) {
                        System.out.println("Message not found.");
                    } else {
                        System.out.println("Recipient: "
                                + found.getRecipient());
                        System.out.println("Message: "
                                + found.getMessageText());
                    }
                    break;

                case "4":
                    String recipient = readText("Enter recipient: ");
                    Message[] matches = manager.searchByRecipient(recipient);

                    if (matches.length == 0) {
                        System.out.println("No messages found.");
                    } else {
                        for (Message message : matches) {
                            System.out.println(message.printMessage());
                            System.out.println();
                        }
                    }
                    break;

                case "5":
                    String hash = readText("Enter message hash: ");

                    try {
                        if (manager.deleteByHash(hash)) {
                            System.out.println(
                                    "Message successfully deleted.");
                        } else {
                            System.out.println("Message hash not found.");
                        }
                    } catch (IllegalStateException ex) {
                        System.out.println("Delete failed: "
                                + ex.getMessage());
                    }
                    break;

                case "6":
                    System.out.println(manager.displayReport());
                    break;

                case "7":
                    return;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }

    // Prevent blank input from being accepted
    private static String readText(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = input.nextLine().trim();

            if (!value.isEmpty()) {
                return value;
            }

            System.out.println("This field cannot be empty.");
        }
    }

    // Avoid crashing when the user types letters instead of a number
    private static int readPositiveInt(String prompt) {
        while (true) {
            try {
                int value = Integer.parseInt(readText(prompt));

                if (value > 0) {
                    return value;
                }
            } catch (NumberFormatException ex) {
                // The message below handles invalid input
            }

            System.out.println("Enter a positive whole number.");
        }
    }
}
