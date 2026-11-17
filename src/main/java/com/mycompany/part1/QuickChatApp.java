
package com.mycompany.part1;

import java.util.ArrayList;
import java.util.Scanner;

public class QuickChatApp {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        Login auth = new Login();

        System.out.println("=== QuickChat Registration ===");

        // Get the user's name before checking account details
        String first = readNonEmpty(input, "First name: ");
        String last = readNonEmpty(input, "Last name: ");

        String username;

        // Keep asking until the username is valid
        do {
            System.out.print("Username (max 5 characters, include _): ");
            username = input.nextLine().trim();

            if (!auth.checkUserName(username)) {
                System.out.println("Invalid username. "
                        + "Include an underscore and use at most 5 characters.");
            }

        } while (!auth.checkUserName(username));

        String password;

        // An invalid password should not close the application
        do {
            System.out.print("Password: ");
            password = input.nextLine();

            if (!auth.checkPasswordComplexity(password)) {
                System.out.println("Invalid password. Use at least "
                        + "8 characters, an uppercase letter, "
                        + "a number and a special character.");
            }

        } while (!auth.checkPasswordComplexity(password));

        String phone;

        // Ask again if the cellphone number is incorrect
        do {
            System.out.print("Cell phone (+27...): ");
            phone = input.nextLine().trim();

            if (!auth.checkCellPhoneNumber(phone)) {
                System.out.println("Invalid phone number. "
                        + "Example: +27831234567");
            }

        } while (!auth.checkCellPhoneNumber(phone));

        System.out.println(auth.registerUser(
                first, last, username, password, phone));

        // Allow the user to retry if their login is incorrect
        boolean loggedIn = false;

        while (!loggedIn) {
            System.out.println("\n=== Login ===");

            System.out.print("Username: ");
            String enteredUsername = input.nextLine();

            System.out.print("Password: ");
            String enteredPassword = input.nextLine();

            loggedIn = auth.loginUser(
                    enteredUsername, enteredPassword);

            System.out.println(auth.returnLoginStatus(loggedIn));
        }

        System.out.println("\nWelcome to QuickChat.");

        // Keep separate lists for the different message statuses
        ArrayList<Message> sent = new ArrayList<>();
        ArrayList<Message> stored = new ArrayList<>();
        ArrayList<Message> disregarded = new ArrayList<>();

        int nextNumber = 1;

        // The main menu stays open until the user chooses Quit
        while (true) {
            System.out.println("\n=== QuickChat Menu ===");
            System.out.println("1. Send Messages");
            System.out.println("2. Show recently sent messages");
            System.out.println("3. Quit");
            System.out.print("Choose an option: ");

            String choice = input.nextLine().trim();

            switch (choice) {

                case "1": {
                    int count = readPositiveInt(
                            input, "How many messages? ");

                    for (int i = 0; i < count; i++) {
                        System.out.println("\nMessage "
                                + (i + 1) + " of " + count);

                        String recipient;

                        // The recipient must have a valid international number
                        do {
                            System.out.print("Recipient (+country code): ");
                            recipient = input.nextLine().trim();

                            if (!auth.checkCellPhoneNumber(recipient)) {
                                System.out.println(
                                        "Invalid recipient number. Try again.");
                            }

                        } while (!auth.checkCellPhoneNumber(recipient));

                        String body;

                        // Prevent empty or overly long messages
                        do {
                            System.out.print("Message (1-250 characters): ");
                            body = input.nextLine();

                            if (body.isBlank()) {
                                System.out.println("Message cannot be empty.");
                            } else if (body.length() > 250) {
                                System.out.println(
                                        "Message exceeds 250 characters by "
                                        + (body.length() - 250) + ".");
                            }

                        } while (body.isBlank() || body.length() > 250);

                        Message message = new Message(
                                nextNumber, recipient, body);

                        String action;

                        // Let the user decide what happens to the message
                        do {
                            System.out.println("\n1. Send");
                            System.out.println("2. Disregard");
                            System.out.println("3. Store for later");
                            System.out.print("Choose: ");

                            action = input.nextLine().trim();

                            if (!action.equals("1")
                                    && !action.equals("2")
                                    && !action.equals("3")) {
                                System.out.println("Choose 1, 2 or 3.");
                            }

                        } while (!action.equals("1")
                                && !action.equals("2")
                                && !action.equals("3"));

                        if (action.equals("2")) {
                            message.setStatus("Disregarded");
                            disregarded.add(message);
                            System.out.println("Message disregarded.");

                        } else {
                            if (action.equals("1")) {
                                message.setStatus("Sent");
                            } else {
                                message.setStatus("Stored");
                            }

                            // Save sent and stored messages to JSON
                            try {
                                message.storeMessage();

                                if (action.equals("1")) {
                                    sent.add(message);
                                    System.out.println(
                                            "Message successfully sent.");
                                    System.out.println(
                                            message.printMessage());
                                } else {
                                    stored.add(message);
                                    System.out.println(
                                            "Message stored for later.");
                                }

                            } catch (IllegalStateException ex) {
                                System.out.println(
                                        "Could not save message: "
                                        + ex.getMessage());
                            }
                        }

                        nextNumber++;
                    }

                    break;
                }

                case "2":
                    // Show only messages that were marked as sent
                    if (sent.isEmpty()) {
                        System.out.println("No messages sent yet.");
                    } else {
                        System.out.println("\n=== Recently Sent Messages ===");

                        for (Message message : sent) {
                            System.out.println();
                            System.out.println(message.printMessage());
                        }
                    }
                    break;

                case "3":
                    // Display the totals before closing QuickChat
                    System.out.println("\n=== Message Summary ===");
                    System.out.println("Total messages sent: "
                            + sent.size());
                    System.out.println("Total messages stored: "
                            + stored.size());
                    System.out.println("Total messages disregarded: "
                            + disregarded.size());
                    System.out.println("Goodbye!");
                    return;

                default:
                    System.out.println(
                            "Invalid option. Select 1, 2 or 3.");
            }
        }
    }

    // Used for fields that should not be left blank
    private static String readNonEmpty(
            Scanner input, String prompt) {

        String value;

        do {
            System.out.print(prompt);
            value = input.nextLine().trim();

            if (value.isEmpty()) {
                System.out.println("This field cannot be empty.");
            }

        } while (value.isEmpty());

        return value;
    }

    // Read a positive number without crashing on letters or symbols
    private static int readPositiveInt(
            Scanner input, String prompt) {

        while (true) {
            System.out.print(prompt);

            try {
                int number = Integer.parseInt(
                        input.nextLine().trim());

                if (number > 0) {
                    return number;
                }

            } catch (NumberFormatException ex) {
                // Invalid input is handled by the message below
            }

            System.out.println(
                    "Enter a positive whole number.");
        }
    }
}
