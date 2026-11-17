
package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameValid() {
        Login login = new Login();

        // A valid username contains _ and has at most 5 characters
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameInvalid() {
        Login login = new Login();

        // These usernames are too long
        assertFalse(login.checkUserName("kyle!!!!!!!"));
        assertFalse(login.checkUserName("kyle_12345"));

        // This username has no underscore
        assertFalse(login.checkUserName("kyle"));
    }

    @Test
    public void testPasswordValid() {
        Login login = new Login();

        assertTrue(login.checkPasswordComplexity(
                "Ch&sec@ke99!"));
    }

    @Test
    public void testPasswordInvalid() {
        Login login = new Login();

        // Missing an uppercase letter
        assertFalse(login.checkPasswordComplexity(
                "ch&sec@ke99!"));

        // Missing a number
        assertFalse(login.checkPasswordComplexity(
                "Ch&sec@ke!!"));

        // Missing a special character
        assertFalse(login.checkPasswordComplexity(
                "Chsecake99"));

        // Password is too short
        assertFalse(login.checkPasswordComplexity(
                "Ab1!"));
    }

    @Test
    public void testCellPhoneValid() {
        Login login = new Login();

        assertTrue(login.checkCellPhoneNumber(
                "+27838968976"));
    }

    @Test
    public void testCellPhoneInvalid() {
        Login login = new Login();

        // Missing the international dialling code
        assertFalse(login.checkCellPhoneNumber(
                "0838968976"));

        assertFalse(login.checkCellPhoneNumber(
                "27838968976"));
    }

    @Test
    public void testRegistrationSuccessful() {
        Login login = new Login();

        String result = login.registerUser(
                "Kyle", "Smith",
                "kyl_1", "Ch&sec@ke99!",
                "+27838968976");

        assertEquals(
                "User successfully registered.", result);
    }

    @Test
    public void testRegistrationFailed() {
        Login login = new Login();

        // Invalid username should prevent registration
        String result = login.registerUser(
                "Kyle", "Smith",
                "kyle123", "Ch&sec@ke99!",
                "+27838968976");

        assertTrue(result.contains(
                "Username is not correctly formatted"));
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();

        login.registerUser(
                "Kyle", "Smith",
                "kyl_1", "Ch&sec@ke99!",
                "+27838968976");

        assertTrue(login.loginUser(
                "kyl_1", "Ch&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();

        login.registerUser(
                "Kyle", "Smith",
                "kyl_1", "Ch&sec@ke99!",
                "+27838968976");

        // A wrong password must not allow login
        assertFalse(login.loginUser(
                "kyl_1", "WrongPassword1!"));
    }

    @Test
    public void testLoginStatus() {
        Login login = new Login();

        login.registerUser(
                "Kyle", "Smith",
                "kyl_1", "Ch&sec@ke99!",
                "+27838968976");

        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(true));

        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}
