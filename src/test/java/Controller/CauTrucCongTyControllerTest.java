package Controller;

import DAO.CauTrucCongTyDAO;
import Models.CayChiNhanh;
import Models.TaiKhoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CauTrucCongTyControllerTest {

    @InjectMocks
    private CauTrucCongTyController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CauTrucCongTyDAO cauTrucCongTyDAO;

    private List<CayChiNhanh> mockStructure;

    @BeforeEach
    public void setUp() {
        // Khởi tạo dữ liệu giả lập cho cấu trúc công ty
        mockStructure = Arrays.asList(
                new CayChiNhanh("CN1", "Chi nhánh 1"),
                new CayChiNhanh("CN2", "Chi nhánh 2")
        );

        // Giả lập getSession() trả về session
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGet_UserNotLoggedIn() throws IOException, ServletException {
        // Giả lập: Không có user trong session
        when(session.getAttribute("user")).thenReturn(null);

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra: Chuyển hướng đến trang login
        verify(response).sendRedirect("views/system/login.jsp");
        verify(cauTrucCongTyDAO, never()).LoadStructure();
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    public void testDoGet_AdminUser() throws IOException, ServletException {
        // Giả lập: User là admin
        TaiKhoan admin = new TaiKhoan();
        admin.setQuyen("admin");
        when(session.getAttribute("user")).thenReturn(admin);
        when(cauTrucCongTyDAO.LoadStructure()).thenReturn(mockStructure);
        when(request.getRequestDispatcher("views/quanli/QuanLiCauTruc.jsp")).thenReturn(requestDispatcher);

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra
        verify(cauTrucCongTyDAO).LoadStructure();
        verify(request).setAttribute("CauTrucCongTy", mockStructure);
        verify(request).getRequestDispatcher("views/quanli/QuanLiCauTruc.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_GiamDocUser() throws IOException, ServletException {
        // Giả lập: User là giám đốc
        TaiKhoan giamdoc = new TaiKhoan();
        giamdoc.setQuyen("giamdoc");
        when(session.getAttribute("user")).thenReturn(giamdoc);
        when(cauTrucCongTyDAO.LoadStructure()).thenReturn(mockStructure);
        when(request.getRequestDispatcher("views/giamdoc/XemCauTruc.jsp")).thenReturn(requestDispatcher);

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra
        verify(cauTrucCongTyDAO).LoadStructure();
        verify(request).setAttribute("CauTrucCongTy", mockStructure);
        verify(request).getRequestDispatcher("views/giamdoc/XemCauTruc.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_TruongPhongUser() throws IOException, ServletException {
        // Giả lập: User là trưởng phòng
        TaiKhoan truongphong = new TaiKhoan();
        truongphong.setQuyen("truongphong");
        when(session.getAttribute("user")).thenReturn(truongphong);
        when(cauTrucCongTyDAO.LoadStructure()).thenReturn(mockStructure);
        when(request.getRequestDispatcher("views/truongphong/XemCauTruc.jsp")).thenReturn(requestDispatcher);

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra
        verify(cauTrucCongTyDAO).LoadStructure();
        verify(request).setAttribute("CauTrucCongTy", mockStructure);
        verify(request).getRequestDispatcher("views/truongphong/XemCauTruc.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_NhanVienUser() throws IOException, ServletException {
        // Giả lập: User là nhân viên
        TaiKhoan nhanvien = new TaiKhoan();
        nhanvien.setQuyen("nhanvien");
        when(session.getAttribute("user")).thenReturn(nhanvien);
        when(cauTrucCongTyDAO.LoadStructure()).thenReturn(mockStructure);
        when(request.getRequestDispatcher("views/nhanvien/NhanVienCauTruc.jsp")).thenReturn(requestDispatcher);

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra
        verify(cauTrucCongTyDAO).LoadStructure();
        verify(request).setAttribute("CauTrucCongTy", mockStructure);
        verify(request).getRequestDispatcher("views/nhanvien/NhanVienCauTruc.jsp");
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_SQLException() throws IOException, ServletException {
        // Giả lập: User là admin, nhưng DAO ném RuntimeException (thay vì SQLException)
        TaiKhoan admin = new TaiKhoan();
        admin.setQuyen("admin");
        when(session.getAttribute("user")).thenReturn(admin);
        when(cauTrucCongTyDAO.LoadStructure()).thenThrow(new RuntimeException("Database error"));

        // Gọi doGet
        controller.doGet(request, response);

        // Kiểm tra: Không chuyển tiếp vì ngoại lệ được xử lý
        verify(cauTrucCongTyDAO).LoadStructure();
        verify(request, never()).getRequestDispatcher(anyString());
        verify(requestDispatcher, never()).forward(any(), any());
    }

    @Test
    public void testDoPost_CallsDoGet() throws IOException, ServletException {
        // Gọi doPost
        controller.doPost(request, response);

        // Kiểm tra: doPost gọi doGet
        verify(request).getSession();
    }
}