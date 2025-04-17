package DAO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmailDAOTest {

    private String host;
    private String port;
    private String userName;
    private String password;
    private String toAddress;
    private String subject;
    private String message;

    @BeforeEach
    void setUp() {
        host = "smtp.example.com";
        port = "587";
        userName = "test@example.com";
        password = "password123";
        toAddress = "recipient@example.com";
        subject = "Test Subject";
        message = "<h1>Hello</h1><p>This is a test email.</p>";
    }

    @Test
    void testSendEmail_Success() {
        try (MockedStatic<Transport> transportMock = mockStatic(Transport.class)) {
            assertDoesNotThrow(() -> {
                EmailDAO.sendEmail(host, port, userName, password, toAddress, subject, message);
            });

            // Verify send is called once
            transportMock.verify(() -> Transport.send(any(Message.class)), times(1));
        }
    }

    @Test
    void testSendEmail_InvalidAddress_ShouldThrow() {
        String invalidEmail = "invalid-email";
        try {
            EmailDAO.sendEmail(host, port, userName, password, invalidEmail, subject, message);
            fail("Expected AddressException to be thrown");
        } catch (AddressException e) {
            // Passed
            assertTrue(e.getMessage().contains(invalidEmail));
        } catch (MessagingException | UnsupportedEncodingException e) {
            fail("Expected AddressException, but got " + e.getClass().getSimpleName());
        }
    }

    @Test
    void testSendEmail_NullParams_ShouldThrow() {
        assertThrows(NullPointerException.class, () -> {
            EmailDAO.sendEmail(null, null, null, null, null, null, null);
        });
    }

    @Test
    void testSendEmail_UnsupportedEncoding() {
        // Trường hợp subject chứa ký tự không mã hóa được → throw UnsupportedEncodingException
        // Trên thực tế `MimeUtility.encodeText()` có thể ném lỗi nếu charset không hỗ trợ

        try {
            // Hack subject để gây lỗi
            String badSubject = new String(new byte[]{(byte) 0xFF}, "ASCII");
            EmailDAO.sendEmail(host, port, userName, password, toAddress, badSubject, message);
        } catch (UnsupportedEncodingException e) {
            // Passed
            assertTrue(true);
        } catch (Exception e) {
            // Không phải lỗi encoding
            fail("Expected UnsupportedEncodingException, but got " + e.getClass().getSimpleName());
        }
    }
}
