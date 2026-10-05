package vn.iotstar.model;
import org.junit.Test;
import static org.junit.Assert.*;
import vn.iotstar.util.PasswordUtil_24133003;
public class PasswordTest {
    @Test public void saltedHashesAndCorrectVerification() {
        String hash = PasswordUtil_24133003.hash("Mật khẩu 123");
        assertTrue(PasswordUtil_24133003.verify("Mật khẩu 123",hash));
        assertFalse(PasswordUtil_24133003.verify("wrong",hash));
        assertNotEquals(hash,PasswordUtil_24133003.hash("Mật khẩu 123"));
    }
    @Test public void legacyAndMalformedHashes() {
        assertTrue(PasswordUtil_24133003.verify("123456","123456"));
        assertFalse(PasswordUtil_24133003.verify("bad","123456"));
        assertFalse(PasswordUtil_24133003.verify("bad","pbkdf2$bad"));
        assertFalse(PasswordUtil_24133003.verify(null,null));
    }
}
