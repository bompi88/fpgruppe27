package tests;

import static org.junit.Assert.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import junit.extensions.jfcunit.JFCTestCase;
import org.junit.Test;
import database.PasswordHash;

public class TestPasswordHash extends JFCTestCase {

	@Test
	public void testPasswordHash() {
		try {
			String correctPassword = "qwerty";
			String wrongPassword = "qwerty1";
			String hash;
			String wrongHash;
	
			hash = PasswordHash.createHash(correctPassword);
			wrongHash = PasswordHash.createHash(correctPassword);
			
			assertFalse(hash.equals(wrongHash));
			assertTrue(PasswordHash.validatePassword(correctPassword, hash));
			assertTrue(PasswordHash.validatePassword(correctPassword, wrongHash));
			assertFalse(PasswordHash.validatePassword(wrongPassword, hash));

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	
	}
	

}
