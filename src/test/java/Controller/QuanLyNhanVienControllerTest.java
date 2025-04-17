import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Controller.QuanLyNhanVienController;
import DAO.QuanLyNhanVienDAO;
import Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class QuanLyNhanVienControllerTest {

    @Mock
    private QuanLyNhanVienDAO quanLyNhanVienDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private RequestDispatcher dispatcher;

    @Mock
    private HttpSession session;

    private QuanLyNhanVienController controller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Mở mocks
        controller = new QuanLyNhanVienController();
        controller.init(); // Khởi tạo controller
    }

    // Test: ListEmployee when logged in
    @Test
    public void testListEmployee_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("giamdoc");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);

        // Đảm bảo mô phỏng trả về List<ThongTinCongTac> (chứ không phải List<ThongTinNguoiDung>)
        List<ThongTinCongTac> mockEmployeeList = Arrays.asList(
            new ThongTinCongTac("1", "Nguyen A", "Nam", new Date(), "Giam doc", "1", "Chi nhánh 1", "1", "Phong ban 1"),
            new ThongTinCongTac("2", "Tran B", "Nu", new Date(), "Nhan vien", "2", "Chi nhánh 2", "2", "Phong ban 2")
        );

        // Mô phỏng DAO trả về List<ThongTinCongTac> thay vì List<ThongTinNguoiDung>
        when(quanLyNhanVienDAO.selectAllUsers(anyString(), anyString(), anyString())).thenReturn(mockEmployeeList);

        when(request.getRequestDispatcher("views/giamdoc/QuanLyNhanVien.jsp")).thenReturn(dispatcher);

        // Gọi phương thức của controller
        controller.ListEmployee(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("thongTinNhanVien", mockEmployeeList);
        verify(request).setAttribute("listEmployee", mockEmployeeList);
        verify(dispatcher).forward(request, response);
    }

    // Test: ListEmployee when not logged in
    @Test
    public void testListEmployee_notLoggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        // Gọi phương thức của controller
        controller.ListEmployee(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }

    // Test: FindEmployee when logged in
    @Test
    public void testFindEmployee_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);

        List<ThongTinCongTac> mockEmployeeList = Arrays.asList(
            new ThongTinCongTac("1", "Nguyen A", "Nam", new Date(), "Giam doc", "1", "Chi nhánh 1", "1", "Phong ban 1"),
            new ThongTinCongTac("2", "Tran B", "Nu", new Date(), "Nhan vien", "2", "Chi nhánh 2", "2", "Phong ban 2")
        );

        when(quanLyNhanVienDAO.findAllEmployee(anyString(), anyString(), anyString())).thenReturn(mockEmployeeList);

        when(request.getRequestDispatcher("views/quanli/QuanLiNhanVien.jsp")).thenReturn(dispatcher);

        // Thiết lập tham số
        when(request.getParameter("tenCN")).thenReturn("Chi nhánh 1");
        when(request.getParameter("tenPB")).thenReturn("Phong Ban 1");
        when(request.getParameter("tenCV")).thenReturn("Chuc Vu 1");

        // Gọi phương thức của controller
        controller.FindInfo(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("listEmployee", mockEmployeeList);
        verify(dispatcher).forward(request, response);
    }

    // Test: AddEmployee when logged in
    @Test
    public void testAddEmployee_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);

        ThongTinNguoiDung newEmployee = new ThongTinNguoiDung("1", "Nguyen A", "Nam", "123456789", "01/01/1980", "Noi Cap", "01/01/2020", "0123456789", "nguyena@example.com", "123 Street", "Xa", "Huyen", "Tinh", 1.0f, "Active", "Bachelor", "01/01/2020");

        when(quanLyNhanVienDAO.AddEmployee(newEmployee)).thenReturn(true);

        when(request.getRequestDispatcher("views/quanli/QuanLiNhanVien.jsp")).thenReturn(dispatcher);

        // Thiết lập tham số
        when(request.getParameter("MaNhanVien")).thenReturn("1");
        when(request.getParameter("HoVaTen")).thenReturn("Nguyen A");
        when(request.getParameter("NgaySinh")).thenReturn("01/01/1980");
        // ... các tham số khác tương tự

        // Gọi phương thức của controller
        controller.AddEmployee(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Thêm nhân viên thành công");
        verify(dispatcher).forward(request, response);
    }

    // Test: DeleteEmployee when logged in
    @Test
    public void testDeleteEmployee_loggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(login);

        String maNhanVien = "1";
        when(request.getParameter("MaNhanVien")).thenReturn(maNhanVien);
        when(quanLyNhanVienDAO.DeleteEmployee(maNhanVien)).thenReturn(true);

        when(request.getRequestDispatcher("views/quanli/QuanLiNhanVien.jsp")).thenReturn(dispatcher);

        // Gọi phương thức của controller
        controller.DeleteEmployee(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Xóa nhân viên thành công");
        verify(dispatcher).forward(request, response);
    }

    // Test: DeleteEmployee when not logged in
    @Test
    public void testDeleteEmployee_notLoggedIn() throws Exception {
        // Thiết lập dữ liệu giả lập
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        // Gọi phương thức của controller
        controller.DeleteEmployee(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }
}
