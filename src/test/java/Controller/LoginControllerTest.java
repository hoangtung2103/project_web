import Controller.LoginController;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import DAO.LoginDAO;
import Models.TaiKhoan;
import javax.servlet.*;
import javax.servlet.http.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.mail.MessagingException;
import java.io.IOException;
import javax.mail.internet.AddressException;

public class LoginControllerTest {

    @Mock
    private LoginDAO loginDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private HttpSession session;

    private LoginController loginController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginController = new LoginController();
        loginController.loginDAO = loginDAO;
    }

    // Kiểm tra trường hợp đăng nhập thành công
    @Test
    void testAuthenticateSuccessful() throws ServletException, IOException, ClassNotFoundException {
        String username = "testuser";
        String password = "testpass";
        TaiKhoan mockUser = new TaiKhoan();
        mockUser.setTenDangNhap(username);
        mockUser.setMatKhau(password);
        mockUser.setQuyen("admin");

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(loginDAO.validate(any(TaiKhoan.class))).thenReturn(mockUser);
        when(request.getSession()).thenReturn(session);

        loginController.authenticate(request, response);

        verify(session).setAttribute("user", mockUser);
        verify(response).sendRedirect("views/quanli/QuanLiCauTruc.jsp");
    }

    // Kiểm tra trường hợp đăng nhập thất bại
    @Test
    void testAuthenticateFailure() throws ServletException, IOException, ClassNotFoundException {
        String username = "testuser";
        String password = "wrongpass";

        when(request.getParameter("username")).thenReturn(username);
        when(request.getParameter("password")).thenReturn(password);
        when(loginDAO.validate(any(TaiKhoan.class))).thenReturn(null);

        loginController.authenticate(request, response);

        verify(request).setAttribute("errMsg", "Tài khoản không tồn tại");
        verify(request).getRequestDispatcher("views/system/login.jsp");
    }

    // Kiểm tra trường hợp gửi email thành công
    @Test
    void testEmailSendingServletSuccess() throws ServletException, IOException, MessagingException, AddressException, ClassNotFoundException {
        String email = "test@example.com";
        String username = "testuser";
        String password = "testpass";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("tenDangNhap")).thenReturn(username);
        when(LoginDAO.LayMatKhau(username, email)).thenReturn(password);
        doNothing().when(LoginDAO.class);
        LoginDAO.sendEmail(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());

        loginController.EmailSendingServlet(request, response);

        verify(LoginDAO.class);
        LoginDAO.sendEmail(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
        verify(response).sendRedirect("views/system/login.jsp");
    }

    // Kiểm tra trường hợp gửi email thất bại (tài khoản hoặc email không chính xác)
    @Test
    void testEmailSendingServletFailure() throws ServletException, IOException, MessagingException {
        String email = "test@example.com";
        String username = "testuser";

        when(request.getParameter("email")).thenReturn(email);
        when(request.getParameter("tenDangNhap")).thenReturn(username);
        when(LoginDAO.LayMatKhau(username, email)).thenReturn("");

        loginController.EmailSendingServlet(request, response);

        verify(request).setAttribute("errMsg", "Tài khoản hoặc email không chính xác");
        verify(request).getRequestDispatcher("views/system/forgotPass.jsp");
    }

    // Kiểm tra trường hợp đăng xuất
    @Test
    void testLogout() throws ServletException, IOException {
        when(request.getSession()).thenReturn(session);

        loginController.Logout(request, response);

        verify(session).invalidate();
        verify(request).getRequestDispatcher("views/system/login.jsp");
    }

    // Kiểm tra trường hợp khi không có user đăng nhập (được chuyển hướng đến trang login)
    @Test
    void testDoGet_UserNotLoggedIn() throws ServletException, IOException, ClassNotFoundException {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        loginController.doGet(request, response);

        verify(response).sendRedirect("views/system/login.jsp");
        verify(loginDAO, never()).validate(any(TaiKhoan.class)); // Kiểm tra không gọi validate
    }

    // Kiểm tra trường hợp khi user không phải admin, giamdoc, truongphong hay nhanvien
    @Test
    void testDoGet_UnauthorizedUser() throws ServletException, IOException {
        TaiKhoan unauthorizedUser = new TaiKhoan();
        unauthorizedUser.setQuyen("unauthorized");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(unauthorizedUser);

        loginController.doGet(request, response);

        // Kiểm tra không có yêu cầu chuyển hướng nào và không có thông báo lỗi
        verify(response, never()).sendRedirect(anyString());
        verify(request, never()).getRequestDispatcher(anyString());
    }
}
