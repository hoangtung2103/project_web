import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Controller.QuanLyChiNhanhController;
import DAO.QuanLyChiNhanhDAO;
import Models.ChiNhanh;
import Models.TaiKhoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class QuanLyChiNhanhControllerTest {

    @Mock
    private QuanLyChiNhanhDAO quanLyChiNhanhDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private HttpSession session;

    private QuanLyChiNhanhController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mở mocks
        controller = new QuanLyChiNhanhController();
        controller.init(); // Khởi tạo controller
    }

    @Test
    public void testListChiNhanh_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);
        
        List<ChiNhanh> mockChiNhanhList = Arrays.asList(
                new ChiNhanh("1", "Chi nhánh 1", "123", "Xa", "Huyen", "Tinh", "2025-04-17", "0123456789", "Giam Doc 1"),
                new ChiNhanh("2", "Chi nhánh 2", "456", "Xa", "Huyen", "Tinh", "2025-04-17", "0123456789", "Giam Doc 2")
        );
        when(quanLyChiNhanhDAO.selectAllChiNhanh()).thenReturn(mockChiNhanhList);
        when(quanLyChiNhanhDAO.selecttenChiNhanh()).thenReturn(mockChiNhanhList);
        
        when(request.getRequestDispatcher("views/quanli/QuanLiChiNhanh.jsp")).thenReturn(dispatcher);

        // Gọi phương thức của controller
        controller.ListChiNhanh(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("listChiNhanh", mockChiNhanhList);
        verify(request).setAttribute("listtenChiNhanh", mockChiNhanhList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testAddChinhanh_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);
        
        ChiNhanh newChinhanh = new ChiNhanh("1", "Chi nhánh 1", "123", "Xa", "Huyen", "Tinh", "2025-04-17", "0123456789", "Giam Doc 1");
        when(quanLyChiNhanhDAO.Addchinhanh(newChinhanh)).thenReturn(true);
        
        when(request.getRequestDispatcher("views/quanli/QuanLiChiNhanh.jsp")).thenReturn(dispatcher);

        // Thiết lập tham số
        when(request.getParameter("maChiNhanh")).thenReturn("1");
        when(request.getParameter("tenChiNhanh")).thenReturn("Chi nhánh 1");
        when(request.getParameter("soNha")).thenReturn("123");
        when(request.getParameter("xa")).thenReturn("Xa");
        when(request.getParameter("huyen")).thenReturn("Huyen");
        when(request.getParameter("tinh")).thenReturn("Tinh");
        when(request.getParameter("ngayTaoChiNhanh")).thenReturn("2025-04-17");
        when(request.getParameter("sdt")).thenReturn("0123456789");
        when(request.getParameter("maGiamDoc")).thenReturn("Giam Doc 1");

        // Gọi phương thức của controller
        controller.AddChinhanh(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Thêm chi nhánh thành công");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testDeletechinhanh_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);
        
        String maChiNhanh = "1";
        when(request.getParameter("xoamaChiNhanh")).thenReturn(maChiNhanh);
        when(quanLyChiNhanhDAO.Deletechinhanh(maChiNhanh)).thenReturn(true);

        when(request.getRequestDispatcher("views/quanli/QuanLiChiNhanh.jsp")).thenReturn(dispatcher);

        // Gọi phương thức của controller
        controller.Deletechinhanh(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Xóa chi nhánh thành công");
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testFindInfo_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);

        List<ChiNhanh> mockChiNhanhList = Arrays.asList(
                new ChiNhanh("1", "Chi nhánh 1", "123", "Xa", "Huyen", "Tinh", "2025-04-17", "0123456789", "Giam Doc 1"));
        when(quanLyChiNhanhDAO.findAllChiNhanh("%")).thenReturn(mockChiNhanhList);

        when(request.getRequestDispatcher("views/quanli/QuanLiChiNhanh.jsp")).thenReturn(dispatcher);

        // Thiết lập tham số
        when(request.getParameter("tenCN")).thenReturn("%");

        // Gọi phương thức của controller
        controller.FindInfo(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("listChiNhanh", mockChiNhanhList);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testListChiNhanh_notLoggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập
        
        // Gọi phương thức của controller
        controller.ListChiNhanh(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }
    
    @Test
    public void testAddChinhanh_notLoggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        // Gọi phương thức của controller
        controller.AddChinhanh(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }

    @Test
    public void testDeletechinhanh_notLoggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        // Gọi phương thức của controller
        controller.Deletechinhanh(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }
}
