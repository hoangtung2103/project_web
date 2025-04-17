import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import Controller.QuanLyPhongBanController;
import DAO.QuanLyPhongBanDAO;
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
import java.util.List;

public class QuanLyPhongBanControllerTest {

    @Mock
    private QuanLyPhongBanDAO quanLyPhongBanDAO; // Mô phỏng DAO quản lý phòng ban

    @Mock
    private QuanLyNhanVienDAO quanLyNhanVienDAO; // Mô phỏng DAO quản lý nhân viên

    @Mock
    private HttpServletRequest request; // Mô phỏng HttpServletRequest

    @Mock
    private HttpServletResponse response; // Mô phỏng HttpServletResponse

    @Mock
    private RequestDispatcher dispatcher; // Mô phỏng RequestDispatcher để chuyển tiếp yêu cầu

    @Mock
    private HttpSession session; // Mô phỏng HttpSession để lấy thông tin người dùng

    private QuanLyPhongBanController controller; // Đối tượng controller cần kiểm thử

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Khởi tạo các mocks
        controller = new QuanLyPhongBanController(); // Khởi tạo đối tượng controller
        controller.init(); // Khởi tạo controller
    }

    // Kiểm tra trường hợp người dùng đã đăng nhập và liệt kê phòng ban
    @Test
    public void testListPhongBan_loggedIn() throws Exception {
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin"); // Thiết lập quyền của người dùng là admin
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(login); // Mô phỏng thông tin người dùng

        // Mô phỏng danh sách phòng ban và chi nhánh trả về từ DAO
        List<PhongBan> mockPhongBanList = Arrays.asList(
                new PhongBan("Phong ban 1"),
                new PhongBan("Phong ban 2")
        );
        List<ChiNhanh> mockChiNhanhList = Arrays.asList(
                new ChiNhanh("Chi nhánh 1"),
                new ChiNhanh("Chi nhánh 2")
        );

        // Mô phỏng phương thức trả về danh sách chi nhánh
        when(quanLyNhanVienDAO.selectAllBranch()).thenReturn(mockChiNhanhList);
        when(request.getRequestDispatcher("views/quanli/QuanLiPhongBan.jsp")).thenReturn(dispatcher); // Mô phỏng RequestDispatcher

        // Gọi phương thức listPhongBan trong controller
        controller.listPhongBan(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("listPhongBan", mockPhongBanList); // Kiểm tra xem đã set attribute đúng chưa
        verify(request).setAttribute("listChiNhanh", mockChiNhanhList); // Kiểm tra xem đã set attribute đúng chưa
        verify(dispatcher).forward(request, response); // Kiểm tra việc chuyển tiếp đến JSP
    }

    // Kiểm tra trường hợp người dùng chưa đăng nhập
    @Test
    public void testListPhongBan_notLoggedIn() throws Exception {
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        // Gọi phương thức listPhongBan trong controller
        controller.listPhongBan(request, response);

        // Kiểm tra xem có chuyển hướng đến trang login không
        verify(response).sendRedirect("views/system/login.jsp");
    }

    // Kiểm tra trường hợp thêm phòng ban thành công
    @Test
    public void testAddPhongBan_loggedIn() throws Exception {
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin"); // Thiết lập quyền người dùng là admin
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(login); // Mô phỏng thông tin người dùng

        // Dữ liệu giả lập để thêm phòng ban
        String maChiNhanh = "1";
        String maPhongBan = "PB01";
        String tenPhongBan = "Phong ban A";
        String ngayTao = "2025-04-17";
        String sdt = "0123456789";
        String maChucVu = "CV01";
        String tenChucVu = "Chuc vu A";
        String luongCoBan = "5000";
        String maTruongPhong = "TP01";
        String ngayBatDau = "2025-04-17";

        // Mô phỏng phương thức thêm phòng ban thành công
        when(quanLyPhongBanDAO.AddPhongBan(maChiNhanh, maPhongBan, tenPhongBan, ngayTao, sdt, maChucVu,
                tenChucVu, Integer.parseInt(luongCoBan), maTruongPhong, ngayBatDau)).thenReturn(true);

        when(request.getRequestDispatcher("views/quanli/QuanLiPhongBan.jsp")).thenReturn(dispatcher);

        // Gọi phương thức thêm phòng ban
        controller.AddPhongBan(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Thêm Phòng Ban thành công");
        verify(dispatcher).forward(request, response);
    }

    // Kiểm tra trường hợp thêm phòng ban thất bại
    @Test
    public void testAddPhongBan_failed() throws Exception {
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin"); // Thiết lập quyền người dùng là admin
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(login); // Mô phỏng thông tin người dùng

        // Dữ liệu giả lập để thêm phòng ban
        String maChiNhanh = "1";
        String maPhongBan = "PB01";
        String tenPhongBan = "Phong ban A";
        String ngayTao = "2025-04-17";
        String sdt = "0123456789";
        String maChucVu = "CV01";
        String tenChucVu = "Chuc vu A";
        String luongCoBan = "5000";
        String maTruongPhong = "TP01";
        String ngayBatDau = "2025-04-17";

        // Mô phỏng phương thức thêm phòng ban thất bại
        when(quanLyPhongBanDAO.AddPhongBan(maChiNhanh, maPhongBan, tenPhongBan, ngayTao, sdt, maChucVu,
                tenChucVu, Integer.parseInt(luongCoBan), maTruongPhong, ngayBatDau)).thenReturn(false);

        when(request.getRequestDispatcher("views/quanli/QuanLiPhongBan.jsp")).thenReturn(dispatcher);

        // Gọi phương thức thêm phòng ban
        controller.AddPhongBan(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Thêm Phòng Ban thất bại");
        verify(dispatcher).forward(request, response);
    }

    // Kiểm tra trường hợp xóa phòng ban thành công
    @Test
    public void testDeletePhongBan_loggedIn() throws Exception {
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin"); // Thiết lập quyền người dùng là admin
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(login); // Mô phỏng thông tin người dùng

        String maPhongBan = "PB01";
        when(request.getParameter("DeleteMaPhongBan")).thenReturn(maPhongBan); // Mô phỏng tham số mã phòng ban

        // Mô phỏng phương thức xóa phòng ban thành công
        when(quanLyPhongBanDAO.XoaPhongBan(maPhongBan)).thenReturn(true);

        when(request.getRequestDispatcher("views/quanli/QuanLiPhongBan.jsp")).thenReturn(dispatcher);

        // Gọi phương thức xóa phòng ban
        controller.DeletePhongBan(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("Result", "Xóa Phòng Ban thành công");
        verify(dispatcher).forward(request, response);
    }

    // Kiểm tra trường hợp tìm kiếm phòng ban khi người dùng đã đăng nhập
    @Test
    public void testFindDepartment_loggedIn() throws Exception {
        TaiKhoan login = new TaiKhoan();
        login.setQuyen("admin"); // Thiết lập quyền người dùng là admin
        when(request.getSession()).thenReturn(session); // Mô phỏng lấy session
        when(session.getAttribute("user")).thenReturn(login); // Mô phỏng thông tin người dùng

        // Dữ liệu giả lập để tìm kiếm phòng ban
        List<ThongTinTruongPhong> mockPhongBanList = Arrays.asList(
                new ThongTinTruongPhong("Phong ban 1", "Truong phong 1", "Chi nhánh 1", "Phong ban 1", "Chuc vu 1", "0123456789", "email@domain.com", "2025-04-17", "123 Main St"),
                new ThongTinTruongPhong("Phong ban 2", "Truong phong 2", "Chi nhánh 2", "Phong ban 2", "Chuc vu 2", "0123456789", "email@domain.com", "2025-04-18", "456 Another St")
        );

        when(quanLyPhongBanDAO.findDepartment(anyString(), anyString())).thenReturn(mockPhongBanList);

        when(request.getRequestDispatcher("views/quanli/QuanLiPhongBan.jsp")).thenReturn(dispatcher);

        when(request.getParameter("tenPB")).thenReturn("Phong ban 1");
        when(request.getParameter("tenCN")).thenReturn("Chi nhánh 1");

        controller.FindDepartment(request, response);

        // Kiểm tra các hành động
        verify(request).setAttribute("listTruongPhong", mockPhongBanList);
        verify(dispatcher).forward(request, response);
    }

    // Kiểm tra trường hợp tìm kiếm phòng ban khi người dùng chưa đăng nhập
    @Test
    public void testFindDepartment_notLoggedIn() throws Exception {
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("user")).thenReturn(null); // Người dùng chưa đăng nhập

        controller.FindDepartment(request, response);

        // Kiểm tra điều hướng
        verify(response).sendRedirect("views/system/login.jsp");
    }
}
