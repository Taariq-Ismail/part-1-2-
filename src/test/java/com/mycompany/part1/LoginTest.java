package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testUsernameValid() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testUsernameInvalid() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle!!!!!!!"));
        assertFalse(login.checkUserName("kyle_12345"));
    }

    @Test
    public void testPasswordValid() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&sec@ke99!"));
    }

    @Test
    public void testPasswordInvalid() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("ch&sec@ke99!")); // No capital
        assertFalse(login.checkPasswordComplexity("Ch&sec@ke!!")); // No number
        assertFalse(login.checkPasswordComplexity("Chsecake99")); // No special
    }

    @Test
    public void testCellPhoneValid() {
        Login login = new Login();
        // Make absolutely sure there are no hidden spaces in this string
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testCellPhoneInvalid() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testLoginSuccessful() {
        Login login = new Login();
        // Register inside the test method to ensure memory is fresh
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        Login login = new Login();
        login.registerUser("Kyle", "Smith", "kyl_1", "Ch&sec@ke99!", "+27838968976");
        assertFalse(login.loginUser("kyl_1", "WrongPassword1!"));
    }
}