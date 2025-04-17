package Controller;

import DAO.CapNhatThongTinDAO;
import Models.*;
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
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CapNhatThongTinControllerTest {

    @InjectMocks
    private CapNhatThongTinController controller;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private HttpSession session;

    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    private CapNhatThongTinDAO capNhatThongTinDAO;

    @BeforeEach
    public void setUp() {
        controller = new CapNhatThongTinController();
        controller.init();
    }

    @Test
    public void testDoGet_InfoEmployee_Success_Admin() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK001", "admin", "password", "admin");
        when(session.getAttribute("user")).thenReturn(user);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK001")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK001")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        QuyetDinh quyetDinh = new QuyetDinh("QD001", "Tang luong", "2023-10-01", "Tang luong 10%", "TK001", "GD001");
        when(capNhatThongTinDAO.XemQuyetDinh("TK001")).thenReturn(Collections.singletonList(quyetDinh));
        when(request.getRequestDispatcher("views/quanli/QuanLiCapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(request).setAttribute("quyetdinh", Collections.singletonList(quyetDinh));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InfoEmployee_Success_GiamDoc() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK002", "giamdoc", "password", "giamdoc");
        when(session.getAttribute("user")).thenReturn(user);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK002")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK002")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        QuyetDinh quyetDinh = new QuyetDinh("QD002", "Bo nhiem", "2023-11-01", "Bo nhiem giam doc", "TK002", "GD002");
        when(capNhatThongTinDAO.XemQuyetDinh("TK002")).thenReturn(Collections.singletonList(quyetDinh));
        when(request.getRequestDispatcher("views/giamdoc/CapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(request).setAttribute("quyetdinh", Collections.singletonList(quyetDinh));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InfoEmployee_Success_TruongPhong() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK003", "truongphong", "password", "truongphong");
        when(session.getAttribute("user")).thenReturn(user);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK003")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK003")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        QuyetDinh quyetDinh = new QuyetDinh("QD003", "Khen thuong", "2023-12-01", "Khen thuong nhan vien xuat sac", "TK003", "GD003");
        when(capNhatThongTinDAO.XemQuyetDinh("TK003")).thenReturn(Collections.singletonList(quyetDinh));
        when(request.getRequestDispatcher("views/truongphong/CapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(request).setAttribute("quyetdinh", Collections.singletonList(quyetDinh));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InfoEmployee_Success_NhanVien() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK004", "nhanvien", "password", "nhanvien");
        when(session.getAttribute("user")).thenReturn(user);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK004")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK004")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        QuyetDinh quyetDinh = new QuyetDinh("QD004", "Ky luat", "2023-12-15", "Ky luat vi vi pham", "TK004", "GD004");
        when(capNhatThongTinDAO.XemQuyetDinh("TK004")).thenReturn(Collections.singletonList(quyetDinh));
        when(request.getRequestDispatcher("views/nhanvien/NhanVienCapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(request).setAttribute("quyetdinh", Collections.singletonList(quyetDinh));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_InfoEmployee_SessionNull() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(response).sendRedirect("views/system/login.jsp");
    }

    @Test
    public void testDoGet_InfoEmployee_SQLException() throws SQLException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK001", "admin", "password", "admin");
        when(session.getAttribute("user")).thenReturn(user);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK001")).thenThrow(new SQLException("Database error"));

        // Act & Assert
        try {
            controller.doGet(request, response);
        } catch (Exception e) {
            verify(capNhatThongTinDAO).ThongTinCaNhan("TK001");
        }
    }

    @Test
    public void testDoGet_UpdateInfoEmployee_Success_Admin() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/updateInfoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK001", "admin", "password", "admin");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("HoTen")).thenReturn("Nguyen Van A");
        when(request.getParameter("GioiTinh")).thenReturn("Nam");
        when(request.getParameter("CCCD")).thenReturn("123456789");
        when(request.getParameter("NgayCap")).thenReturn("2020-01-01");
        when(request.getParameter("NoiCap")).thenReturn("Hanoi");
        when(request.getParameter("NgaySinh")).thenReturn("1990-01-01");
        when(request.getParameter("SoDienThoai")).thenReturn("0123456789");
        when(request.getParameter("Email")).thenReturn("test@example.com");
        when(request.getParameter("SoNha")).thenReturn("123");
        when(request.getParameter("Xa")).thenReturn("Xa A");
        when(request.getParameter("Huyen")).thenReturn("Huyen B");
        when(request.getParameter("Tinh")).thenReturn("Tinh C");
        when(capNhatThongTinDAO.UpdateThongTinCaNhan(any(ThongTinNguoiDung.class))).thenReturn(true);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK001")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK001")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        when(request.getRequestDispatcher("views/quanli/QuanLiCapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("message", "Cập nhật thông tin thành công");
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_UpdateInfoEmployee_Failure_GiamDoc() throws ServletException, IOException, SQLException, ParseException {
        // Arrange
        when(request.getServletPath()).thenReturn("/updateInfoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK002", "giamdoc", "password", "giamdoc");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("HoTen")).thenReturn("Nguyen Van B");
        when(request.getParameter("GioiTinh")).thenReturn("Nu");
        when(request.getParameter("CCCD")).thenReturn("987654321");
        when(request.getParameter("NgayCap")).thenReturn("2021-02-02");
        when(request.getParameter("NoiCap")).thenReturn("HCM");
        when(request.getParameter("NgaySinh")).thenReturn("1985-02-02");
        when(request.getParameter("SoDienThoai")).thenReturn("0987654321");
        when(request.getParameter("Email")).thenReturn("test2@example.com");
        when(request.getParameter("SoNha")).thenReturn("456");
        when(request.getParameter("Xa")).thenReturn("Xa B");
        when(request.getParameter("Huyen")).thenReturn("Huyen C");
        when(request.getParameter("Tinh")).thenReturn("Tinh D");
        when(capNhatThongTinDAO.UpdateThongTinCaNhan(any(ThongTinNguoiDung.class))).thenReturn(false);
        when(capNhatThongTinDAO.ThongTinCaNhan("TK002")).thenReturn(Collections.singletonList(new ThongTinNguoiDung()));
        when(capNhatThongTinDAO.ThongTinCaNhanCongTy("TK002")).thenReturn(Collections.singletonList(new ThongTinCongTac()));
        when(request.getRequestDispatcher("views/giamdoc/CapNhatThongTin.jsp")).thenReturn(requestDispatcher);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(request).setAttribute("message", "Cập nhật thông tin thất bại");
        verify(request).setAttribute("thongtincanhan", Collections.singletonList(new ThongTinNguoiDung()));
        verify(request).setAttribute("thongtincanhanCongTy", Collections.singletonList(new ThongTinCongTac()));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGet_UpdateInfoEmployee_SessionNull() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/updateInfoEmployee");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null);

        // Act
        controller.doGet(request, response);

        // Assert
        verify(response).sendRedirect("views/system/login.jsp");
    }

    @Test
    public void testDoGet_UpdateInfoEmployee_ParseException() throws SQLException {
        // Arrange
        when(request.getServletPath()).thenReturn("/updateInfoEmployee");
        when(request.getSession()).thenReturn(session);
        TaiKhoan user = new TaiKhoan("TK003", "truongphong", "password", "truongphong");
        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("HoTen")).thenReturn("Nguyen Van C");
        when(request.getParameter("GioiTinh")).thenReturn("Nam");
        when(request.getParameter("CCCD")).thenReturn("123123123");
        when(request.getParameter("NgayCap")).thenReturn("invalid-date"); // Gây ParseException
        when(request.getParameter("NoiCap")).thenReturn("Da Nang");
        when(request.getParameter("NgaySinh")).thenReturn("1988-03-03");
        when(request.getParameter("SoDienThoai")).thenReturn("0912345678");
        when(request.getParameter("Email")).thenReturn("test3@example.com");
        when(request.getParameter("SoNha")).thenReturn("789");
        when(request.getParameter("Xa")).thenReturn("Xa C");
        when(request.getParameter("Huyen")).thenReturn("Huyen D");
        when(request.getParameter("Tinh")).thenReturn("Tinh E");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> controller.doGet(request, response));
    }

    @Test
    public void testDoPost_CallsDoGet() throws ServletException, IOException {
        // Arrange
        when(request.getServletPath()).thenReturn("/infoEmployee");

        // Act
        controller.doPost(request, response);

        // Assert
        verify(request).getServletPath();
    }

    @Test
    public void testConvertToUTF8_ValidInput() throws Exception {
        // Arrange
        String input = "Nguyễn Văn A";

        // Act
        String result = invokePrivateMethod(controller, "ConvertToUTF8", String.class, input);

        // Assert
        assertEquals(input, result);
    }

    @Test
    public void testConvertToUTF8_NullInput() throws Exception {
        // Arrange
        String input = null;

        // Act
        String result = invokePrivateMethod(controller, "ConvertToUTF8", String.class, input);

        // Assert
        assertNull(result);
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<T> returnType, Object... args) throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, String.class);
        method.setAccessible(true);
        return returnType.cast(method.invoke(object, args));
    }
}