package Controller;

import DAO.EmailDAO;
import Models.TaiKhoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.*;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SendEmailTest {

    private SendEmail sendEmail;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private ServletContext context;

    @Mock
    private RequestDispatcher dispatcher;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Tạo subclass để override getServletContext()
        sendEmail = new SendEmail() {
            @Override
            public ServletContext getServletContext() {
                return context;
            }
        };

        // Mock context và request/session
        when(request.getSession()).thenReturn(session);
        when(context.getInitParameter("host")).thenReturn("smtp.example.com");
        when(context.getInitParameter("port")).thenReturn("587");
        when(context.getInitParameter("user")).thenReturn("test@example.com");
        when(context.getInitParameter("pass")).thenReturn("secret");

        sendEmail.init();
    }

    @Test
    void testInit() {
        verify(context).getInitParameter("host");
        verify(context).getInitParameter("port");
        verify(context).getInitParameter("user");
        verify(context).getInitParameter("pass");
    }

    @Test
    void testDoPost_UserNull() throws Exception {
        when(session.getAttribute("user")).thenReturn(null);
        sendEmail.doPost(request, response);
        verify(response).sendRedirect("views/system/login.jsp");
    }

    @Test
    void testDoPost_Admin() throws Exception {
        testRoleForwarding("admin", "views/quanli/QuanLiGuiMail.jsp");
    }

    @Test
    void testDoPost_GiamDoc() throws Exception {
        testRoleForwarding("giamdoc", "views/giamdoc/GuiEmail.jsp");
    }

    @Test
    void testDoPost_TruongPhong() throws Exception {
        testRoleForwarding("truongphong", "views/truongphong/GuiEmail.jsp");
    }

    @Test
    void testDoPost_NhanVien() throws Exception {
        testRoleForwarding("nhanvien", "views/nhanvien/NhanVienGuiMail.jsp");
    }

    @Test
    void testDoPost_EmailSendFails() throws Exception {
        TaiKhoan user = new TaiKhoan();
        user.setQuyen("admin");
        mockRequestAndSession(user);

        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        try (MockedStatic<EmailDAO> mockStatic = mockStatic(EmailDAO.class)) {
            mockStatic.when(() -> EmailDAO.sendEmail(any(), any(), any(), any(), any(), any(), any()))
                    .thenThrow(new RuntimeException("SMTP lỗi"));

            sendEmail.doPost(request, response);

            verify(request).setAttribute(eq("Result"), eq("Đã có lỗi xảy ra: SMTP lỗi"));
            verify(request).getRequestDispatcher("views/quanli/QuanLiGuiMail.jsp");
            verify(dispatcher).forward(request, response);
        }
    }

    @Test
    void testConvertToUTF8() throws Exception {
        String original = "Thử tiếng Việt";
        String isoString = new String(original.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        java.lang.reflect.Method method = SendEmail.class.getDeclaredMethod("ConvertToUTF8", String.class);
        method.setAccessible(true);
        String result = (String) method.invoke(sendEmail, isoString);

        assertEquals(original, result);
    }

    private void testRoleForwarding(String role, String expectedView) throws Exception {
        TaiKhoan user = new TaiKhoan();
        user.setQuyen(role);
        mockRequestAndSession(user);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);

        try (MockedStatic<EmailDAO> mockStatic = mockStatic(EmailDAO.class)) {
            mockStatic.when(() -> EmailDAO.sendEmail(any(), any(), any(), any(), any(), any(), any()))
                    .thenReturn(null);

            sendEmail.doPost(request, response);

            verify(request).setAttribute("Result", "Gửi email thành công");
            verify(request).getRequestDispatcher(expectedView);
            verify(dispatcher).forward(request, response);
        }
    }

    private void mockRequestAndSession(TaiKhoan user) {
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("received-email")).thenReturn("to@example.com");
        when(request.getParameter("title-email")).thenReturn("Tiêu đề");
        when(request.getParameter("content-email")).thenReturn("Nội dung");
    }
}
