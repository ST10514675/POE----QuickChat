/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package quickchat;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

// testing the login class stuffs
public class LoginTest {

    private Login login;

    // this runs before every test to make a new loginss
    @Before
    public void setUp() {
        login = new Login();
    }

    @Test
    public void testUserNameWorks() {
        login.setUsername("kyl_1");
        boolean result = login.checkUserName();
        assertTrue("this username should work", result);
    }

    @Test
    public void testUserNameFails() {
        login.setUsername("kyle!!!!!!!");
        boolean result = login.checkUserName();
        assertFalse("should not work because its too long", result);
    }

    @Test
    public void testPasswordWorks() {
        login.setPassword("Ch&&sec@ke99!");
        boolean result = login.checkPasswordComplexity();
        assertTrue("this password is good", result);
    }

    @Test
    public void testPasswordFails() {
        login.setPassword("password");
        boolean result = login.checkPasswordComplexity();
        assertFalse("password is too weak", result);
    }

    @Test
    public void testPhoneNumberWorks() {
        login.setPhoneNumber("+27838965976");
        boolean result = login.checkCellPhoneNumber();
        assertTrue("phone number is fine", result);
    }

    @Test
    public void testPhoneNumberFails() {
        login.setPhoneNumber("0838965976");
        boolean result = login.checkCellPhoneNumber();
        assertFalse("should of failed because no +27", result);
    }

    @Test
    public void testLoginSuccessful() {
        // you can use your own username, include the underscore 
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        
        // try to login with same stuff you inserted
        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");
        assertTrue("login should work here", result);
    }

    @Test
    public void testLoginFailed() {
        login.setUsername("kyl_1");
        login.setPassword("Ch&&sec@ke99!");
        
        // use the wrong password, and the system wont let you in
        boolean result = login.loginUser("kyl_1", "wrong_password");
        assertFalse("should not let me in", result);
    }
}