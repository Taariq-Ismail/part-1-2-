
package com.mycompany.part1;

public class Login {

    private String username;
    private String password;
    private String firstName;
    private String lastName;

    // Check the username before allowing registration
    public boolean checkUserName(String username) {
        return username != null
                && username.length() <= 5
                && username.contains("_");
    }

    // Password needs uppercase, number and special character
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        return password.matches(".*[A-Z].*")
                && password.matches(".*[0-9].*")
                && password.matches(".*[^a-zA-Z0-9].*");
    }

    // Check the international cellphone format
    public boolean checkCellPhoneNumber(String number) {
        return number != null
                && number.matches("\\+[1-9]\\d{1,10}");
    }

    // Only save the account when all the details are correct
    public String registerUser(String first, String last,
            String user, String pass, String phone) {

        if (first == null || first.isBlank()
                || last == null || last.isBlank()) {
            return "First name and last name cannot be empty.";
        }

        if (!checkUserName(user)) {
            return "Username is not correctly formatted.";
        }

        if (!checkPasswordComplexity(pass)) {
            return "Password is not correctly formatted.";
        }

        if (!checkCellPhoneNumber(phone)) {
            return "Cell phone number incorrectly formatted.";
        }

        firstName = first;
        lastName = last;
        username = user;
        password = pass;

        return "User successfully registered.";
    }

    // Compare the entered details to the registered account
    public boolean loginUser(String user, String pass) {
        return username != null
                && username.equals(user)
                && password.equals(pass);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }

        return "Username or password incorrect, please try again.";
    }
}
