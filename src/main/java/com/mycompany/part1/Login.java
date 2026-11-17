
package com.mycompany.part1;

public class Login {

    private String username;
    private String password;
    private String cellPhoneNumber;
    private String firstName;
    private String lastName;

    // Check that the username has an underscore and is not too long
    public boolean checkUserName(String username) {
        return username != null
                && !username.isBlank()
                && username.length() <= 5
                && username.contains("_");
    }

    // Check the password requirements
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean upper = false;
        boolean number = false;
        boolean special = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upper = true;
            } else if (Character.isDigit(c)) {
                number = true;
            } else if (!Character.isLetterOrDigit(c)) {
                special = true;
            }
        }

        return upper && number && special;
    }

    // Check that the number starts with an international code
    public boolean checkCellPhoneNumber(String cellPhone) {
        return cellPhone != null
                && cellPhone.matches("\\+[1-9]\\d{1,10}");
    }

    // Register the user only when all details are valid
    public String registerUser(String firstName, String lastName,
            String username, String password, String cellPhoneNumber) {

        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()) {
            return "First name and last name cannot be empty.";
        }

        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure "
                    + "that your username contains an underscore and is "
                    + "no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure "
                    + "that the password contains at least eight characters, "
                    + "a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber(cellPhoneNumber)) {
            return "Cell phone number incorrectly formatted or does "
                    + "not contain international code.";
        }

        // Save the details after validation
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;

        return "User successfully registered.";
    }

    // Compare login details with the registered account
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null
                && password != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    // Show the correct message after a login attempt
    public String returnLoginStatus(boolean isLoggedIn) {
        if (isLoggedIn) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}
