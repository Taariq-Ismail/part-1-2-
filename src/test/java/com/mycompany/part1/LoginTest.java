
package com.mycompany.part1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    @Test
    public void testValidUsername() {
        Login login = new Login();
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    public void testInvalidUsername() {
        Login login = new Login();
        assertFalse(login.checkUserName("kyle123"));
        assertFalse(login.checkUserName("kyle"));
    }

    @Test
    public void testValidPassword() {
        Login login = new Login();
        assertTrue(login.checkPasswordComplexity("Ch&sec@ke99!"));
    }

    @Test
    public void testInvalidPassword() {
        Login login = new Login();
        assertFalse(login.checkPasswordComplexity("password123"));
        assertFalse(login.checkPasswordComplexity("Ab1!"));
    }

    @Test
    public void testValidCellNumber() {
        Login login = new Login();
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    public void testInvalidCellNumber() {
        Login login = new Login();
        assertFalse(login.checkCellPhoneNumber("0838968976"));
    }

    @Test
    public void testRegistrationAndLogin() {
        Login login = new Login();

        assertEquals("User successfully registered.",
                login.registerUser("Kyle", "Smith",
                        "kyl_1", "Ch&sec@ke99!", "+27838968976"));

        assertTrue(login.loginUser("kyl_1", "Ch&sec@ke99!"));
        assertFalse(login.loginUser("kyl_1", "Wrong123!"));
    }

    @Test
    public void testLoginStatus() {
        Login login = new Login();

        login.registerUser("Kyle", "Smith",
                "kyl_1", "Ch&sec@ke99!", "+27838968976");

        assertEquals(
                "Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(true));

        assertEquals(
                "Username or password incorrect, please try again.",
                login.returnLoginStatus(false));
    }
}
