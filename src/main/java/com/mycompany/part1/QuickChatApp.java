package com.mycompany.part1;

import java.util.Scanner;
import java.util.ArrayList;

public class QuickChatApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login auth = new Login();
        
        System.out.println("=== Registration ===");
        System.out.print("Enter First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lName = scanner.nextLine();
        System.out.print("Enter Username: ");
        String uName = scanner.nextLine();
        System.out.print("Enter Password: ");
        String pWord = scanner.nextLine();
        System.out.print("Enter Cell Phone (+27): ");
        String cell = scanner.nextLine();

        String regStatus = auth.registerUser(fName, lName, uName, pWord, cell);
        System.out.println(regStatus);

        if (!regStatus.equals("User successfully registered.")) {
            System.out.println("Registration failed. Exiting app.");
            return;
        }

        System.out.println("\n=== Login ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();
            System.out.print("Enter Password: ");
            String loginPass = scanner.nextLine();

            loggedIn = auth.loginUser(loginUser, loginPass);
            System.out.println(auth.returnLoginStatus(loggedIn));
        }

        if (loggedIn) {
            System.out.println("\nWelcome to QuickChat.");
            ArrayList<Message> sentMessages = new ArrayList<>();
            boolean running = true;
            
            while (running) {
                System.out.println("\nMenu:");
                System.out.println("1. Send Messages");
                System.out.println("2. Show recently sent messages");
                System.out.println("3. Quit");
                System.out.print("Choose an option: ");
                
                String choice = scanner.nextLine();
                
                switch (choice) {
                    case "1":
                        System.out.print("How many messages do you wish to enter? ");
                        int numMessages = Integer.parseInt(scanner.nextLine());
                        
                        for (int i = 0; i < numMessages; i++) {
                            System.out.println("\nMessage " + (i + 1) + " of " + numMessages);
                            
                            System.out.print("Enter recipient number (+...): ");
                            String recipient = scanner.nextLine();
                            
                            System.out.print("Enter message: ");
                            String text = scanner.nextLine();
                            
                            Message msg = new Message(i, recipient, text);
                            
                            // Using the String validation checks now
                            String recipientCheck = msg.checkRecipientCell();
                            if (!recipientCheck.equals("Cell phone number successfully captured.")) {
                                System.out.println(recipientCheck);
                                continue; 
                            }
                            
                            String lengthCheck = msg.checkMessageLength();
                            if (!lengthCheck.equals("Message ready to send.")) {
                                System.out.println(lengthCheck);
                                continue; 
                            }
                            
                            System.out.println("Message successfully sent.");
                            System.out.println("Message ID: " + msg.getMessageId());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipient());
                            System.out.println("Message: " + msg.getMessageText());
                            
                            msg.storeMessage(); 
                            sentMessages.add(msg);
                        }
                        break;
                        
                    case "2":
                        System.out.println("Coming Soon.");
                        break;
                        
                    case "3":
                        running = false;
                        System.out.println("Exiting application.");
                        break;
                        
                    default:
                        System.out.println("Invalid option.");
                }
            }
            System.out.println("\nTotal messages sent: " + sentMessages.size());
        }
        scanner.close();
    }
}