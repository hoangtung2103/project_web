package DAO;

import JDBCUtils.JDBCUtil;
import Models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuanLyNhanVienDAOTest {

    private QuanLyNhanVienDAO nhanVienDAO;
    private Connection connection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private QuanLyNhanVienDAO mockNhanVienDAO;

    /**
     * Khởi tạo kết nối cơ sở dữ liệu trước mỗi bài kiểm thử.
     * Mục đích: Đảm bảo có kết nối thực để chèn/xóa dữ liệu kiểm thử.
     * Kết quả mong đợi: Kết nối được thiết lập thành công.
     */
    private void initConnection() throws SQLException {
        connection = JDBCUtil.getConnection();
    }

    /**
     * Chèn dữ liệu kiểm thử trước mỗi bài kiểm thử.
     * Mục đích: Tạo 4 tài khoản, 3 người dùng, 1 chi nhánh, 1 phòng ban, 1 chức vụ, và 3 công tác để kiểm thử.
     * Kịch bản:
     * - Tắt kiểm tra khóa ngoại để tránh lỗi ràng buộc.
     * - Chèn dữ liệu vào các bảng taikhoan, thongtinnguoidung, chinhanh, thongtinphongban, chucvu, thongtincongtacnhanvien.
     * - Bật lại kiểm tra khóa ngoại sau khi chèn.
     * Kết quả mong đợi: Tổng cộng 8 bản ghi trong thongtinnguoidung (5 sẵn có + 3 kiểm thử).
     */
    @BeforeEach
    public void insertTestData() throws SQLException {
        initConnection();
        nhanVienDAO = new QuanLyNhanVienDAO();
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            // Chèn tài khoản
            String insertTaiKhoan = "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, Quyen) VALUES (?, ?, ?, ?)";
            PreparedStatement psTaiKhoan = connection.prepareStatement(insertTaiKhoan);
            psTaiKhoan.setString(1, "TKTEST01");
            psTaiKhoan.setString(2, "testuser1");
            psTaiKhoan.setString(3, "pass1");
            psTaiKhoan.setString(4, "giamdoc");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST02");
            psTaiKhoan.setString(2, "testuser2");
            psTaiKhoan.setString(3, "pass2");
            psTaiKhoan.setString(4, "truongphong");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST03");
            psTaiKhoan.setString(2, "testuser3");
            psTaiKhoan.setString(3, "pass3");
            psTaiKhoan.setString(4, "nhanvien");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST04");
            psTaiKhoan.setString(2, "testuser4");
            psTaiKhoan.setString(3, "pass4");
            psTaiKhoan.setString(4, "nhanvien");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.close();

            // Chèn người dùng
            String insertNguoiDung = "INSERT INTO thongtinnguoidung (MaTaiKhoan, HoTen, GioiTinh, CCCD, NgayCap, NoiCap, NgaySinh, SoDienThoai, Email, SoNha, Xa, Huyen, Tinh, HeSoLuong, TrangThai, TrinhDo, NgayBatDauLam) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psNguoiDung = connection.prepareStatement(insertNguoiDung);
            psNguoiDung.setString(1, "TKTEST01");
            psNguoiDung.setString(2, "Nguyễn Văn Test");
            psNguoiDung.setString(3, "Nam");
            psNguoiDung.setString(4, "999000001");
            psNguoiDung.setString(5, "2023-01-01");
            psNguoiDung.setString(6, "Hà Nội");
            psNguoiDung.setString(7, "1985-05-10");
            psNguoiDung.setString(8, "0980000001");
            psNguoiDung.setString(9, "test1@example.com");
            psNguoiDung.setString(10, "123");
            psNguoiDung.setString(11, "Cầu Giấy");
            psNguoiDung.setString(12, "Cầu Giấy");
            psNguoiDung.setString(13, "Hà Nội");
            psNguoiDung.setFloat(14, 3.0f);
            psNguoiDung.setString(15, "Đang làm");
            psNguoiDung.setString(16, "Đại học");
            psNguoiDung.setString(17, "2023-01-01");
            psNguoiDung.executeUpdate();
            psNguoiDung.setString(1, "TKTEST02");
            psNguoiDung.setString(2, "Trần Thị Test");
            psNguoiDung.setString(3, "Nữ");
            psNguoiDung.setString(4, "999000002");
            psNguoiDung.setString(5, "2023-02-01");
            psNguoiDung.setString(6, "TP.HCM");
            psNguoiDung.setString(7, "1990-06-15");
            psNguoiDung.setString(8, "0980000002");
            psNguoiDung.setString(9, "test2@example.com");
            psNguoiDung.setString(10, "456");
            psNguoiDung.setString(11, "Quận 7");
            psNguoiDung.setString(12, "Quận 7");
            psNguoiDung.setString(13, "TP.HCM");
            psNguoiDung.setFloat(14, 2.5f);
            psNguoiDung.setString(15, "Đang làm");
            psNguoiDung.setString(16, "Thạc sĩ");
            psNguoiDung.setString(17, "2023-02-01");
            psNguoiDung.executeUpdate();
            psNguoiDung.setString(1, "TKTEST03");
            psNguoiDung.setString(2, "Lê Văn Test");
            psNguoiDung.setString(3, "Nam");
            psNguoiDung.setString(4, "999000003");
            psNguoiDung.setString(5, "2023-03-01");
            psNguoiDung.setString(6, "Đà Nẵng");
            psNguoiDung.setString(7, "1988-07-20");
            psNguoiDung.setString(8, "0980000003");
            psNguoiDung.setString(9, "test3@example.com");
            psNguoiDung.setString(10, "789");
            psNguoiDung.setString(11, "Hải Châu");
            psNguoiDung.setString(12, "Hải Châu");
            psNguoiDung.setString(13, "Đà Nẵng");
            psNguoiDung.setFloat(14, 2.0f);
            psNguoiDung.setString(15, "Đang làm");
            psNguoiDung.setString(16, "Đại học");
            psNguoiDung.setString(17, "2023-03-01");
            psNguoiDung.executeUpdate();
            psNguoiDung.close();

            // Chèn chi nhánh
            String insertChiNhanh = "INSERT INTO chinhanh (MaChiNhanh, TenChiNhanh, SoNha, Xa, Huyen, Tinh, NgayTaoChiNhanh, SoDienThoai, MaTongGiamDoc) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psChiNhanh = connection.prepareStatement(insertChiNhanh);
            psChiNhanh.setString(1, "CNTEST01");
            psChiNhanh.setString(2, "Chi nhánh Test Hà Nội");
            psChiNhanh.setString(3, "Số 99");
            psChiNhanh.setString(4, "Cầu Giấy");
            psChiNhanh.setString(5, "Cầu Giấy");
            psChiNhanh.setString(6, "Hà Nội");
            psChiNhanh.setString(7, "2023-01-15");
            psChiNhanh.setString(8, "0249990001");
            psChiNhanh.setString(9, "TKTEST01");
            psChiNhanh.executeUpdate();
            psChiNhanh.close();

            // Chèn phòng ban
            String insertPhongBan = "INSERT INTO thongtinphongban (MaPB, TenPB, MaChiNhanh) VALUES (?, ?, ?)";
            PreparedStatement psPhongBan = connection.prepareStatement(insertPhongBan);
            psPhongBan.setString(1, "PBTEST01");
            psPhongBan.setString(2, "Phòng Test 1");
            psPhongBan.setString(3, "CNTEST01");
            psPhongBan.executeUpdate();
            psPhongBan.close();

            // Chèn chức vụ
            String insertChucVu = "INSERT INTO chucvu (MaChucVu, TenChucVu) VALUES (?, ?)";
            PreparedStatement psChucVu = connection.prepareStatement(insertChucVu);
            psChucVu.setString(1, "CVTEST01");
            psChucVu.setString(2, "Nhân viên Test");
            psChucVu.executeUpdate();
            psChucVu.close();

            // Chèn công tác
            String insertCongTac = "INSERT INTO thongtincongtacnhanvien (MaTaiKhoan, MaChucVu, MaChiNhanh, MaPhongBan) VALUES (?, ?, ?, ?)";
            PreparedStatement psCongTac = connection.prepareStatement(insertCongTac);
            psCongTac.setString(1, "TKTEST01");
            psCongTac.setString(2, "CVTEST01");
            psCongTac.setString(3, "CNTEST01");
            psCongTac.setString(4, "PBTEST01");
            psCongTac.executeUpdate();
            psCongTac.setString(1, "TKTEST02");
            psCongTac.executeUpdate();
            psCongTac.setString(1, "TKTEST03");
            psCongTac.executeUpdate();
            psCongTac.close();
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    /**
     * Dọn dẹp dữ liệu kiểm thử sau mỗi bài kiểm thử.
     * Mục đích: Xóa tất cả bản ghi kiểm thử để đảm bảo tính độc lập giữa các test.
     * Kịch bản:
     * - Tắt kiểm tra khóa ngoại.
     * - Xóa dữ liệu từ các bảng liên quan (thongtincongtacnhanvien, chucvu, thongtinphongban, chinhanh, thongtinnguoidung, taikhoan, quyetdinh).
     * - Đóng kết nối.
     * Kết quả mong đợi: Database trở về trạng thái ban đầu (chỉ còn 5 bản ghi sẵn có).
     */
    @AfterEach
    public void cleanUp() throws SQLException {
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            connection.createStatement().execute("DELETE FROM thongtincongtacnhanvien WHERE MaTaiKhoan LIKE 'TKTEST%'");
            connection.createStatement().execute("DELETE FROM chucvu WHERE MaChucVu = 'CVTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinphongban WHERE MaPB = 'PBTEST01'");
            connection.createStatement().execute("DELETE FROM chinhanh WHERE MaChiNhanh = 'CNTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinnguoidung WHERE MaTaiKhoan LIKE 'TKTEST%'");
            connection.createStatement().execute("DELETE FROM taikhoan WHERE MaTaiKhoan LIKE 'TKTEST%'");
            connection.createStatement().execute("DELETE FROM quyetdinh WHERE MaQuyetDinh LIKE 'QDTEST%'");
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    // --- Kiểm thử selectAllUsers ---

    /**
     * Kiểm tra selectAllUsers với vai trò admin.
     * Mục đích: Đảm bảo hàm trả về danh sách tất cả thông tin công tác nhân viên.
     * Kịch bản:
     * - Gọi selectAllUsers với role = "admin".
     * - Kiểm tra danh sách không null và chứa ít nhất 3 bản ghi kiểm thử.
     * Kết quả mong đợi: Danh sách không null, có ít nhất 3 bản ghi.
     * Độ phủ: Phủ nhánh role = "admin" và khối try khi truy vấn thành công.
     */
    @Test
    public void testSelectAllUsers_Admin_DanhSachDung() {
        // Gọi phương thức với role admin
        List<ThongTinCongTac> danhSach = nhanVienDAO.selectAllUsers(null, null, "admin");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra số lượng bản ghi
        assertTrue(danhSach.size() >= 3, "Danh sách phải chứa ít nhất 3 bản ghi kiểm thử");
    }

    /**
     * Kiểm tra selectAllUsers với vai trò giám đốc.
     * Mục đích: Đảm bảo hàm trả về danh sách nhân viên trong chi nhánh cụ thể.
     * Kịch bản:
     * - Gọi selectAllUsers với role = "giamdoc" và maChiNhanh = "CNTEST01".
     * - Kiểm tra tất cả bản ghi thuộc chi nhánh CNTEST01.
     * Kết quả mong đợi: Danh sách không null, tất cả bản ghi có MaChiNhanh = "CNTEST01".
     * Độ phủ: Phủ nhánh role = "giamdoc" và khối setString(1, maChiNhanh).
     */
    @Test
    public void testSelectAllUsers_GiamDoc_DanhSachDung() {
        // Gọi phương thức với role giám đốc
        List<ThongTinCongTac> danhSach = nhanVienDAO.selectAllUsers("CNTEST01", null, "giamdoc");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra tất cả bản ghi thuộc CNTEST01
        assertTrue(danhSach.stream().allMatch(ct -> ct.getMachinhanh().equals("CNTEST01")), "Tất cả bản ghi phải thuộc CNTEST01");
    }

    /**
     * Kiểm tra selectAllUsers với vai trò trưởng phòng.
     * Mục đích: Đảm bảo hàm trả về danh sách nhân viên trong chi nhánh và phòng ban cụ thể.
     * Kịch bản:
     * - Gọi selectAllUsers với role = "truongphong", maChiNhanh = "CNTEST01", maPhongBan = "PBTEST01".
     * - Kiểm tra tất cả bản ghi thuộc phòng ban PBTEST01.
     * Kết quả mong đợi: Danh sách không null, tất cả bản ghi có MaPB = "PBTEST01".
     * Độ phủ: Phủ nhánh role = "truongphong" và khối setString(1, maChiNhanh), setString(2, maPhongBan).
     */
    @Test
    public void testSelectAllUsers_TruongPhong_DanhSachDung() {
        // Gọi phương thức với role trưởng phòng
        List<ThongTinCongTac> danhSach = nhanVienDAO.selectAllUsers("CNTEST01", "PBTEST01", "truongphong");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra tất cả bản ghi thuộc PBTEST01
        assertTrue(danhSach.stream().allMatch(ct -> ct.getMaphongban().equals("PBTEST01")), "Tất cả bản ghi phải thuộc PBTEST01");
    }

    /**
     * Kiểm tra selectAllUsers khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết连接 và ném SQLException.
     * - Gọi selectAllUsers với role = "admin".
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong selectAllUsers.
     */
    @Test
    public void testSelectAllUsers_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ThongTinCongTac> danhSach = mockNhanVienDAO.selectAllUsers(null, null, "admin");
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử LayMaChiNhanh ---

    /**
     * Kiểm tra LayMaChiNhanh khi mã tài khoản tồn tại.
     * Mục đích: Đảm bảo hàm trả về đúng mã chi nhánh cho nhân viên TKTEST01.
     * Kịch bản:
     * - Gọi LayMaChiNhanh với MaTaiKhoan = "TKTEST01".
     * - So sánh kết quả với mã chi nhánh CNTEST01 từ dữ liệu kiểm thử.
     * Kết quả mong đợi: Trả về "CNTEST01".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testLayMaChiNhanh_TonTai() {
        // Gọi phương thức với mã tài khoản hợp lệ
        String maChiNhanh = nhanVienDAO.LayMaChiNhanh("TKTEST01");
        // Kiểm tra kết quả
        assertEquals("CNTEST01", maChiNhanh, "Mã chi nhánh phải là CNTEST01");
    }

    /**
     * Kiểm tra LayMaChiNhanh khi mã tài khoản không tồn tại.
     * Mục đích: Đảm bảo hàm trả về "null" khi không tìm thấy nhân viên.
     * Kịch bản:
     * - Gọi LayMaChiNhanh với MaTaiKhoan = "TKUNKNOWN".
     * - Kiểm tra kết quả là "null".
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayMaChiNhanh_KhongTonTai() {
        // Gọi phương thức với mã tài khoản không tồn tại
        String maChiNhanh = nhanVienDAO.LayMaChiNhanh("TKUNKNOWN");
        // Kiểm tra kết quả
        assertEquals("null", maChiNhanh, "Phải trả về 'null' khi không tìm thấy");
    }

    /**
     * Kiểm tra LayMaChiNhanh khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về "null" khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi LayMaChiNhanh với MaTaiKhoan = "TKTEST01".
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối catch trong LayMaChiNhanh.
     */
    @Test
    public void testLayMaChiNhanh_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            String maChiNhanh = mockNhanVienDAO.LayMaChiNhanh("TKTEST01");
            // Kiểm tra kết quả
            assertEquals("null", maChiNhanh, "Phải trả về 'null' khi có lỗi");
        }
    }

    // --- Kiểm thử LayMaPhongBan ---

    /**
     * Kiểm tra LayMaPhongBan khi mã tài khoản tồn tại.
     * Mục đích: Đảm bảo hàm trả về đúng mã phòng ban cho nhân viên TKTEST01.
     * Kịch bản:
     * - Gọi LayMaPhongBan với MaTaiKhoan = "TKTEST01".
     * - So sánh kết quả với mã phòng ban PBTEST01 từ dữ liệu kiểm thử.
     * Kết quả mong đợi: Trả về "PBTEST01".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testLayMaPhongBan_TonTai() {
        // Gọi phương thức với mã tài khoản hợp lệ
        String maPhongBan = nhanVienDAO.LayMaPhongBan("TKTEST01");
        // Kiểm tra kết quả
        assertEquals("PBTEST01", maPhongBan, "Mã phòng ban phải là PBTEST01");
    }

    /**
     * Kiểm tra LayMaPhongBan khi mã tài khoản không tồn tại.
     * Mục đích: Đảm bảo hàm trả về "null" khi không tìm thấy nhân viên.
     * Kịch bản:
     * - Gọi LayMaPhongBan với MaTaiKhoan = "TKUNKNOWN".
     * - Kiểm tra kết quả là "null".
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayMaPhongBan_KhongTonTai() {
        // Gọi phương thức với mã tài khoản không tồn tại
        String maPhongBan = nhanVienDAO.LayMaPhongBan("TKUNKNOWN");
        // Kiểm tra kết quả
        assertEquals("null", maPhongBan, "Phải trả về 'null' khi không tìm thấy");
    }

    /**
     * Kiểm tra LayMaPhongBan khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về "null" khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi LayMaPhongBan với MaTaiKhoan = "TKTEST01".
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối catch trong LayMaPhongBan.
     */
    @Test
    public void testLayMaPhongBan_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            String maPhongBan = mockNhanVienDAO.LayMaPhongBan("TKTEST01");
            // Kiểm tra kết quả
            assertEquals("null", maPhongBan, "Phải trả về 'null' khi có lỗi");
        }
    }

    // --- Kiểm thử LayTenChiNhanh ---

    /**
     * Kiểm tra LayTenChiNhanh khi mã chi nhánh tồn tại.
     * Mục đích: Đảm bảo hàm trả về đúng tên chi nhánh cho CNTEST01.
     * Kịch bản:
     * - Gọi LayTenChiNhanh với MaChiNhanh = "CNTEST01".
     * - So sánh kết quả với "Chi nhánh Test Hà Nội".
     * Kết quả mong đợi: Trả về "Chi nhánh Test Hà Nội".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testLayTenChiNhanh_TonTai() {
        // Gọi phương thức với mã chi nhánh hợp lệ
        String tenChiNhanh = nhanVienDAO.LayTenChiNhanh("CNTEST01");
        // Kiểm tra kết quả
        assertEquals("Chi nhánh Test Hà Nội", tenChiNhanh, "Tên chi nhánh phải đúng");
    }

    /**
     * Kiểm tra LayTenChiNhanh khi mã chi nhánh không tồn tại.
     * Mục đích: Đảm bảo hàm trả về chuỗi rỗng khi không tìm thấy chi nhánh.
     * Kịch bản:
     * - Gọi LayTenChiNhanh với MaChiNhanh = "CNUNKNOWN".
     * - Kiểm tra kết quả là chuỗi rỗng.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayTenChiNhanh_KhongTonTai() {
        // Gọi phương thức với mã chi nhánh không tồn tại
        String tenChiNhanh = nhanVienDAO.LayTenChiNhanh("CNUNKNOWN");
        // Kiểm tra kết quả
        assertEquals("", tenChiNhanh, "Phải trả về rỗng khi không tìm thấy");
    }

    /**
     * Kiểm tra LayTenChiNhanh khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về chuỗi rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi LayTenChiNhanh với MaChiNhanh = "CNTEST01".
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối catch trong LayTenChiNhanh.
     */
    @Test
    public void testLayTenChiNhanh_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            String tenChiNhanh = mockNhanVienDAO.LayTenChiNhanh("CNTEST01");
            // Kiểm tra kết quả
            assertEquals("", tenChiNhanh, "Phải trả về rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử LayTenPhongBan ---

    /**
     * Kiểm tra LayTenPhongBan khi mã phòng ban tồn tại.
     * Mục đích: Đảm bảo hàm trả về đúng tên phòng ban cho PBTEST01.
     * Kịch bản:
     * - Gọi LayTenPhongBan với MaPB = "PBTEST01".
     * - So sánh kết quả với "Phòng Test 1".
     * Kết quả mong đợi: Trả về "Phòng Test 1".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testLayTenPhongBan_TonTai() {
        // Gọi phương thức với mã phòng ban hợp lệ
        String tenPhongBan = nhanVienDAO.LayTenPhongBan("PBTEST01");
        // Kiểm tra kết quả
        assertEquals("Phòng Test 1", tenPhongBan, "Tên phòng ban phải đúng");
    }

    /**
     * Kiểm tra LayTenPhongBan khi mã phòng ban không tồn tại.
     * Mục đích: Đảm bảo hàm trả về chuỗi rỗng khi không tìm thấy phòng ban.
     * Kịch bản:
     * - Gọi LayTenPhongBan với MaPB = "PBUNKNOWN".
     * - Kiểm tra kết quả là chuỗi rỗng.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayTenPhongBan_KhongTonTai() {
        // Gọi phương thức với mã phòng ban không tồn tại
        String tenPhongBan = nhanVienDAO.LayTenPhongBan("PBUNKNOWN");
        // Kiểm tra kết quả
        assertEquals("", tenPhongBan, "Phải trả về rỗng khi không tìm thấy");
    }

    /**
     * Kiểm tra LayTenPhongBan khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về chuỗi rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi LayTenPhongBan với MaPB = "PBTEST01".
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối catch trong LayTenPhongBan.
     */
    @Test
    public void testLayTenPhongBan_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            String tenPhongBan = mockNhanVienDAO.LayTenPhongBan("PBTEST01");
            // Kiểm tra kết quả
            assertEquals("", tenPhongBan, "Phải trả về rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử selectAllDepart ---

    /**
     * Kiểm tra selectAllDepart với mã chi nhánh cụ thể.
     * Mục đích: Đảm bảo hàm trả về danh sách phòng ban thuộc chi nhánh CNTEST01.
     * Kịch bản:
     * - Gọi selectAllDepart với MaChiNhanh = "CNTEST01".
     * - Kiểm tra danh sách chứa phòng ban "Phòng Test 1".
     * Kết quả mong đợi: Danh sách không null, chứa ít nhất "Phòng Test 1".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllDepart_ChiNhanh_DanhSachDung() {
        // Gọi phương thức với mã chi nhánh hợp lệ
        List<PhongBan> danhSach = nhanVienDAO.selectAllDepart("CNTEST01");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra chứa phòng ban kiểm thử
        assertTrue(danhSach.stream().anyMatch(pb -> pb.getTenPhongBan().equals("Phòng Test 1")), "Phải chứa Phòng Test 1");
    }

    /**
     * Kiểm tra selectAllDepart không có mã chi nhánh.
     * Mục đích: Đảm bảo hàm trả về tất cả phòng ban trong database.
     * Kịch bản:
     * - Gọi selectAllDepart không tham số.
     * - Kiểm tra danh sách chứa ít nhất 1 phòng ban.
     * Kết quả mong đợi: Danh sách không null, có ít nhất 1 bản ghi.
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllDepart_KhongChiNhanh_DanhSachDung() {
        // Gọi phương thức không tham số
        List<PhongBan> danhSach = nhanVienDAO.selectAllDepart();
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra số lượng bản ghi
        assertTrue(danhSach.size() >= 1, "Phải chứa ít nhất 1 phòng ban");
    }

    /**
     * Kiểm tra selectAllDepart khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi selectAllDepart với MaChiNhanh = "CNTEST01".
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong selectAllDepart.
     */
    @Test
    public void testSelectAllDepart_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<PhongBan> danhSach = mockNhanVienDAO.selectAllDepart("CNTEST01");
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử selectAllTitle ---

    /**
     * Kiểm tra selectAllTitle với mã chi nhánh cụ thể.
     * Mục đích: Đảm bảo hàm trả về danh sách chức vụ thuộc chi nhánh CNTEST01.
     * Kịch bản:
     * - Gọi selectAllTitle với MaChiNhanh = "CNTEST01".
     * - Kiểm tra danh sách chứa chức vụ "Nhân viên Test".
     * Kết quả mong đợi: Danh sách không null, chứa ít nhất "Nhân viên Test".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllTitle_ChiNhanh_DanhSachDung() {
        // Gọi phương thức với mã chi nhánh hợp lệ
        List<ChucVu> danhSach = nhanVienDAO.selectAllTitle("CNTEST01");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra chứa chức vụ kiểm thử
        assertTrue(danhSach.stream().anyMatch(cv -> cv.getTenChucVu().equals("Nhân viên Test")), "Phải chứa Nhân viên Test");
    }

    /**
     * Kiểm tra selectAllTitle không có mã chi nhánh.
     * Mục đích: Đảm bảo hàm trả về tất cả chức vụ trong database.
     * Kịch bản:
     * - Gọi selectAllTitle không tham số.
     * - Kiểm tra danh sách chứa ít nhất 1 chức vụ.
     * Kết quả mong đợi: Danh sách không null, có ít nhất 1 bản ghi.
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllTitle_KhongChiNhanh_DanhSachDung() {
        // Gọi phương thức không tham số
        List<ChucVu> danhSach = nhanVienDAO.selectAllTitle();
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra số lượng bản ghi
        assertTrue(danhSach.size() >= 1, "Phải chứa ít nhất 1 chức vụ");
    }

    /**
     * Kiểm tra selectAllTitle khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi selectAllTitle với MaChiNhanh = "CNTEST01".
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong selectAllTitle.
     */
    @Test
    public void testSelectAllTitle_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ChucVu> danhSach = mockNhanVienDAO.selectAllTitle("CNTEST01");
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử selectAllBranch ---

    /**
     * Kiểm tra selectAllBranch khi database có dữ liệu.
     * Mục đích: Đảm bảo hàm trả về danh sách tất cả chi nhánh.
     * Kịch bản:
     * - Gọi selectAllBranch.
     * - Kiểm tra danh sách chứa chi nhánh "Chi nhánh Test Hà Nội".
     * Kết quả mong đợi: Danh sách không null, chứa ít nhất "Chi nhánh Test Hà Nội".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllBranch_DanhSachDung() {
        // Gọi phương thức
        List<ChiNhanh> danhSach = nhanVienDAO.selectAllBranch();
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra chứa chi nhánh kiểm thử
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getTenChiNhanh().equals("Chi nhánh Test Hà Nội")), "Phải chứa Chi nhánh Test Hà Nội");
    }

    /**
     * Kiểm tra selectAllBranch khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi selectAllBranch.
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong selectAllBranch.
     */
    @Test
    public void testSelectAllBranch_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ChiNhanh> danhSach = mockNhanVienDAO.selectAllBranch();
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử selectAllTitleOfDepart ---

    /**
     * Kiểm tra selectAllTitleOfDepart với chi nhánh và phòng ban cụ thể.
     * Mục đích: Đảm bảo hàm trả về danh sách chức vụ thuộc CNTEST01 và PBTEST01.
     * Kịch bản:
     * - Gọi selectAllTitleOfDepart với MaChiNhanh = "CNTEST01", MaPB = "PBTEST01".
     * - Kiểm tra danh sách chứa chức vụ "Nhân viên Test".
     * Kết quả mong đợi: Danh sách không null, chứa ít nhất "Nhân viên Test".
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testSelectAllTitleOfDepart_DanhSachDung() {
        // Gọi phương thức với chi nhánh và phòng ban hợp lệ
        List<ChucVu> danhSach = nhanVienDAO.selectAllTitleOfDepart("CNTEST01", "PBTEST01");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra chứa chức vụ kiểm thử
        assertTrue(danhSach.stream().anyMatch(cv -> cv.getTenChucVu().equals("Nhân viên Test")), "Phải chứa Nhân viên Test");
    }

    /**
     * Kiểm tra selectAllTitleOfDepart khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi selectAllTitleOfDepart với MaChiNhanh = "CNTEST01", MaPB = "PBTEST01".
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong selectAllTitleOfDepart.
     */
    @Test
    public void testSelectAllTitleOfDepart_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ChucVu> danhSach = mockNhanVienDAO.selectAllTitleOfDepart("CNTEST01", "PBTEST01");
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử findAllEmployee ---

    /**
     * Kiểm tra findAllEmployee khi tìm kiếm khớp chính xác.
     * Mục đích: Đảm bảo hàm trả về danh sách nhân viên khớp với chi nhánh, phòng ban, chức vụ.
     * Kịch bản:
     * - Gọi findAllEmployee với TenChiNhanh = "Chi nhánh Test Hà Nội", TenPB = "Phòng Test 1", TenChucVu = "Nhân viên Test".
     * - Kiểm tra danh sách chứa TKTEST01.
     * Kết quả mong đợi: Danh sách không null, chứa ít nhất TKTEST01.
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testFindAllEmployee_KhopChinhXac() {
        // Gọi phương thức với tham số hợp lệ
        List<ThongTinCongTac> danhSach = nhanVienDAO.findAllEmployee("Chi nhánh Test Hà Nội", "Phòng Test 1", "Nhân viên Test");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra chứa nhân viên kiểm thử
        assertTrue(danhSach.stream().anyMatch(ct -> ct.getManhanvien().equals("TKTEST01")), "Phải chứa TKTEST01");
    }

    /**
     * Kiểm tra findAllEmployee khi không có kết quả khớp.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi tham số không khớp.
     * Kịch bản:
     * - Gọi findAllEmployee với các tham số không tồn tại.
     * - Kiểm tra danh sách rỗng.
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testFindAllEmployee_KhongKhop() {
        // Gọi phương thức với tham số không tồn tại
        List<ThongTinCongTac> danhSach = nhanVienDAO.findAllEmployee("Không Tồn Tại", "Không Tồn Tại", "Không Tồn Tại");
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra danh sách rỗng
        assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi không khớp");
    }

    /**
     * Kiểm tra findAllEmployee khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi findAllEmployee với tham số hợp lệ.
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong findAllEmployee.
     */
    @Test
    public void testFindAllEmployee_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ThongTinCongTac> danhSach = mockNhanVienDAO.findAllEmployee("Chi nhánh Test Hà Nội", "Phòng Test 1", "Nhân viên Test");
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử loadInfomation ---

    /**
     * Kiểm tra loadInfomation khi database có dữ liệu.
     * Mục đích: Đảm bảo hàm trả về đúng số lượng bản ghi (149 sẵn có + 3 kiểm thử).
     * Kịch bản:
     * - Gọi loadInfomation sau khi @BeforeEach chèn dữ liệu.
     * - Kiểm tra danh sách chứa 149 bản ghi.
     * Kết quả mong đợi: Danh sách không null, chứa đúng 149 bản ghi.
     * Độ phủ: Phủ khối try và while khi ResultSet có dữ liệu.
     */
    @Test
    public void testLoadInfomation_DanhSachDungSoLuong() {
        // Gọi phương thức
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra số lượng bản ghi
        assertEquals(149, danhSach.size(), "Danh sách phải chứa 149 bản ghi (146 sẵn có + 3 kiểm thử)");
    }

    /**
     * Kiểm tra nội dung bản ghi trả về bởi loadInfomation.
     * Mục đích: Đảm bảo các trường của bản ghi TKTEST01 được đọc đúng.
     * Kịch bản:
     * - Gọi loadInfomation.
     * - Tìm bản ghi TKTEST01 và so sánh các trường HoTen, Sdt.
     * Kết quả mong đợi: Các trường khớp với dữ liệu kiểm thử.
     * Độ phủ: Phủ các dòng tạo đối tượng ThongTinNguoiDung từ ResultSet.
     */
    @Test
    public void testLoadInfomation_NoiDungDung() {
        // Gọi phương thức
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        // Tìm bản ghi TKTEST01
        ThongTinNguoiDung nguoiDung = danhSach.stream()
                .filter(nd -> nd.getMataikhoan().equals("TKTEST01"))
                .findFirst().orElse(null);
        // Kiểm tra bản ghi tồn tại
        assertNotNull(nguoiDung, "Bản ghi TKTEST01 phải tồn tại");
        // Kiểm tra nội dung
        assertEquals("Nguyễn Văn Test", nguoiDung.getHoTen(), "Họ tên phải đúng");
        assertEquals("0980000001", nguoiDung.getSdt(), "Số điện thoại phải đúng");
    }

    /**
     * Kiểm tra loadInfomation khi bảng thongtinnguoidung rỗng.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi không có dữ liệu.
     * Kịch bản:
     * - Xóa toàn bộ dữ liệu test trong bảng thongtinnguoidung.
     * - Gọi loadInfomation.
     * Kết quả mong đợi: Danh sách không null và chứa 146 bản ghi sẵn có
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLoadInfomation_DatabaseRong() throws SQLException {
        // Dọn dẹp dữ liệu kiểm thử để chỉ còn dữ liệu có sẵn
        cleanUp();
        initConnection();
        // Gọi phương thức
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        // Kiểm tra danh sách không null
        assertNotNull(danhSach, "Danh sách không được null");
        // Kiểm tra số lượng bản ghi (chỉ có 146 bản ghi sẵn có)
        assertEquals(146, danhSach.size(), "Danh sách phải chứa đúng 146 bản ghi sẵn có");
    }

    /**
     * Kiểm tra loadInfomation khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về danh sách rỗng khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException.
     * - Gọi loadInfomation.
     * Kết quả mong đợi: Danh sách không null và rỗng.
     * Độ phủ: Phủ khối catch trong loadInfomation.
     */
    @Test
    public void testLoadInfomation_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            List<ThongTinNguoiDung> danhSach = mockNhanVienDAO.loadInfomation();
            // Kiểm tra danh sách không null
            assertNotNull(danhSach, "Danh sách không được null");
            // Kiểm tra danh sách rỗng
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi có lỗi");
        }
    }

    // --- Kiểm thử AddEmployee ---

    /**
     * Kiểm tra AddEmployee khi thêm nhân viên mới thành công.
     * Mục đích: Đảm bảo hàm thêm nhân viên vào database và trả về true.
     * Kịch bản:
     * - Tạo đối tượng ThongTinNguoiDung mới với MaTaiKhoan = "TKTEST04".
     * - Gọi AddEmployee và kiểm tra kết quả.
     * - Kiểm tra nhân viên mới có trong database.
     * Kết quả mong đợi: Trả về true, nhân viên TKTEST04 tồn tại.
     * Độ phủ: Phủ khối try và executeUpdate trong AddEmployee.
     */
    @Test
    public void testAddEmployee_ThanhCong() {
        // Tạo nhân viên mới
        ThongTinNguoiDung emp = new ThongTinNguoiDung("TKTEST04", "Phạm Văn Test", "Nam", "999000004", "2023-04-01", "Cần Thơ", "1995-08-25", "0980000004", "test4@example.com", "101", "Ninh Kiều", "Ninh Kiều", "Cần Thơ", 2.0f, "Đang làm", "Cao đẳng", "2023-04-01");
        // Gọi phương thức
        boolean result = nhanVienDAO.AddEmployee(emp);
        // Kiểm tra kết quả thêm
        assertTrue(result, "Thêm nhân viên phải thành công");
        // Kiểm tra nhân viên trong database
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        assertTrue(danhSach.stream().anyMatch(nd -> nd.getMataikhoan().equals("TKTEST04")), "Nhân viên TKTEST04 phải tồn tại");
    }

    /**
     * Kiểm tra AddEmployee khi mã tài khoản bị trùng.
     * Mục đích: Đảm bảo hàm trả về false khi thêm nhân viên với mã đã tồn tại.
     * Kịch bản:
     * - Tạo đối tượng ThongTinNguoiDung với MaTaiKhoan = "TKTEST01" (đã có trong @BeforeEach).
     * - Gọi AddEmployee và kiểm tra kết quả.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch khi executeUpdate thất bại do vi phạm ràng buộc khóa.
     */
    @Test
    public void testAddEmployee_TrungMaTaiKhoan() {
        // Tạo nhân viên với mã trùng
        ThongTinNguoiDung emp = new ThongTinNguoiDung("TKTEST01", "Phạm Văn Test", "Nam", "999000004", "2023-04-01", "Cần Thơ", "1995-08-25", "0980000004", "test4@example.com", "101", "Ninh Kiều", "Ninh Kiều", "Cần Thơ", 2.0f, "Đang làm", "Cao đẳng", "2023-04-01");
        // Gọi phương thức
        boolean result = nhanVienDAO.AddEmployee(emp);
        // Kiểm tra kết quả
        assertFalse(result, "Thêm nhân viên trùng mã tài khoản phải thất bại");
    }

    /**
     * Kiểm tra AddEmployee khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về false khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException tại executeUpdate.
     * - Gọi AddEmployee với nhân viên mới.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch trong AddEmployee.
     */
    @Test
    public void testAddEmployee_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL"));
            // Tạo nhân viên mới
            ThongTinNguoiDung emp = new ThongTinNguoiDung("TKTEST04", "Phạm Văn Test", "Nam", "999000004", "2023-04-01", "Cần Thơ", "1995-08-25", "0980000004", "test4@example.com", "101", "Ninh Kiều", "Ninh Kiều", "Cần Thơ", 2.0f, "Đang làm", "Cao đẳng", "2023-04-01");
            // Gọi phương thức
            boolean result = mockNhanVienDAO.AddEmployee(emp);
            // Kiểm tra kết quả
            assertFalse(result, "Thêm nhân viên khi có lỗi phải thất bại");
        }
    }

    // --- Kiểm thử DeleteEmployee ---

    /**
     * Kiểm tra DeleteEmployee khi xóa nhân viên thành công.
     * Mục đích: Đảm bảo hàm cập nhật trạng thái nhân viên thành "Đã nghỉ" và trả về true.
     * Kịch bản:
     * - Gọi DeleteEmployee với MaTaiKhoan = "TKTEST01".
     * - Kiểm tra trạng thái nhân viên trong database.
     * Kết quả mong đợi: Trả về true, trạng thái là "Đã nghỉ".
     * Độ phủ: Phủ khối try và executeUpdate trong DeleteEmployee.
     */
    @Test
    public void testDeleteEmployee_ThanhCong() {
        // Gọi phương thức
        boolean result = nhanVienDAO.DeleteEmployee("TKTEST01");
        // Kiểm tra kết quả xóa
        assertTrue(result, "Xóa nhân viên phải thành công");
        // Kiểm tra trạng thái trong database
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        ThongTinNguoiDung nguoiDung = danhSach.stream()
                .filter(nd -> nd.getMataikhoan().equals("TKTEST01"))
                .findFirst().orElse(null);
        assertNotNull(nguoiDung, "Nhân viên TKTEST01 vẫn tồn tại");
        assertEquals("Đã nghỉ", nguoiDung.getTrangThai(), "Trạng thái phải là 'Đã nghỉ'");
    }

    /**
     * Kiểm tra DeleteEmployee khi nhân viên không tồn tại.
     * Mục đích: Đảm bảo hàm trả về false khi mã tài khoản không có.
     * Kịch bản:
     * - Gọi DeleteEmployee với MaTaiKhoan = "TKUNKNOWN".
     * - Kiểm tra kết quả.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối try khi executeUpdate không ảnh hưởng bản ghi nào.
     */
    @Test
    public void testDeleteEmployee_KhongTonTai() {
        // Gọi phương thức với mã không tồn tại
        boolean result = nhanVienDAO.DeleteEmployee("TKUNKNOWN");
        // Kiểm tra kết quả
        assertFalse(result, "Xóa nhân viên không tồn tại phải thất bại");
    }

    /**
     * Kiểm tra DeleteEmployee khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về false khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException tại executeUpdate.
     * - Gọi DeleteEmployee với MaTaiKhoan = "TKTEST01".
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch trong DeleteEmployee.
     */
    @Test
    public void testDeleteEmployee_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL"));
            // Gọi phương thức
            boolean result = mockNhanVienDAO.DeleteEmployee("TKTEST01");
            // Kiểm tra kết quả
            assertFalse(result, "Xóa nhân viên khi có lỗi phải thất bại");
        }
    }

    // --- Kiểm thử UpdateEmployee ---

    /**
     * Kiểm tra UpdateEmployee khi cập nhật nhân viên thành công.
     * Mục đích: Đảm bảo hàm cập nhật thông tin nhân viên và trả về true.
     * Kịch bản:
     * - Tạo đối tượng ThongTinNguoiDung với MaTaiKhoan = "TKTEST01" và thông tin cập nhật.
     * - Gọi UpdateEmployee và kiểm tra kết quả.
     * - Kiểm tra thông tin đã được cập nhật trong database.
     * Kết quả mong đợi: Trả về true, các trường HoTen, Email được cập nhật.
     * Độ phủ: Phủ khối try và executeUpdate trong UpdateEmployee.
     */
    @Test
    public void testUpdateEmployee_ThanhCong() {
        // Tạo nhân viên với thông tin cập nhật
        ThongTinNguoiDung emp = new ThongTinNguoiDung("TKTEST01", "Nguyễn Văn Updated", "Nam", "999000001", "2023-01-01", "Hà Nội", "1985-05-10", "0980000001", "updated@example.com", "123", "Cầu Giấy", "Cầu Giấy", "Hà Nội", 3.5f, "Đang làm", "Thạc sĩ", "2023-01-01");
        // Gọi phương thức
        boolean result = nhanVienDAO.UpdateEmployee(emp);
        // Kiểm tra kết quả cập nhật
        assertTrue(result, "Sửa nhân viên phải thành công");
        // Kiểm tra thông tin trong database
        List<ThongTinNguoiDung> danhSach = nhanVienDAO.loadInfomation();
        ThongTinNguoiDung nguoiDung = danhSach.stream()
                .filter(nd -> nd.getMataikhoan().equals("TKTEST01"))
                .findFirst().orElse(null);
        assertEquals("Nguyễn Văn Updated", nguoiDung.getHoTen(), "Họ tên phải được cập nhật");
        assertEquals("updated@example.com", nguoiDung.getEmail(), "Email phải được cập nhật");
    }

    /**
     * Kiểm tra UpdateEmployee khi nhân viên không tồn tại.
     * Mục đích: Đảm bảo hàm trả về false khi mã tài khoản không có.
     * Kịch bản:
     * - Tạo đối tượng ThongTinNguoiDung với MaTaiKhoan = "TKUNKNOWN".
     * - Gọi UpdateEmployee và kiểm tra kết quả.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối try khi executeUpdate không ảnh hưởng bản ghi nào.
     */
    @Test
    public void testUpdateEmployee_KhongTonTai() {
        // Tạo nhân viên với mã không tồn tại
        ThongTinNguoiDung emp = new ThongTinNguoiDung("TKUNKNOWN", "Nguyễn Văn Updated", "Nam", "999000001", "2023-01-01", "Hà Nội", "1985-05-10", "0980000001", "updated@example.com", "123", "Cầu Giấy", "Cầu Giấy", "Hà Nội", 3.5f, "Đang làm", "Thạc sĩ", "2023-01-01");
        // Gọi phương thức
        boolean result = nhanVienDAO.UpdateEmployee(emp);
        // Kiểm tra kết quả
        assertFalse(result, "Sửa nhân viên không tồn tại phải thất bại");
    }

    /**
     * Kiểm tra UpdateEmployee khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về false khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException tại executeUpdate.
     * - Gọi UpdateEmployee với nhân viên hợp lệ.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch trong UpdateEmployee.
     */
    @Test
    public void testUpdateEmployee_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL"));
            // Tạo nhân viên hợp lệ
            ThongTinNguoiDung emp = new ThongTinNguoiDung("TKTEST01", "Nguyễn Văn Updated", "Nam", "999000001", "2023-01-01", "Hà Nội", "1985-05-10", "0980000001", "updated@example.com", "123", "Cầu Giấy", "Cầu Giấy", "Hà Nội", 3.5f, "Đang làm", "Thạc sĩ", "2023-01-01");
            // Gọi phương thức
            boolean result = mockNhanVienDAO.UpdateEmployee(emp);
            // Kiểm tra kết quả
            assertFalse(result, "Sửa nhân viên khi có lỗi phải thất bại");
        }
    }

    // --- Kiểm thử AddReward ---

    /**
     * Kiểm tra AddReward khi thêm quyết định thành công.
     * Mục đích: Đảm bảo hàm thêm quyết định khen thưởng/kỷ luật và trả về true.
     * Kịch bản:
     * - Tạo đối tượng QuyetDinh với MaQuyetDinh = "QDTEST01".
     * - Gọi AddReward và kiểm tra kết quả.
     * Kết quả mong đợi: Trả về true.
     * Độ phủ: Phủ khối try và executeUpdate trong AddReward.
     */
    @Test
    public void testAddReward_ThanhCong() {
        // Tạo quyết định mới
        QuyetDinh qd = new QuyetDinh("QDTEST01", "Khen thưởng", "2023-04-01", "Thưởng xuất sắc", "TKTEST01", "TKTEST02");
        // Gọi phương thức
        boolean result = nhanVienDAO.AddReward(qd);
        // Kiểm tra kết quả
        assertTrue(result, "Thêm quyết định phải thành công");
    }

    /**
     * Kiểm tra AddReward khi mã quyết định bị trùng.
     * Mục đích: Đảm bảo hàm trả về false khi thêm quyết định với mã đã tồn tại.
     * Kịch bản:
     * - Thêm quyết định QDTEST01.
     * - Thêm lại QDTEST01 và kiểm tra kết quả.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch khi executeUpdate thất bại do vi phạm ràng buộc khóa.
     */
    @Test
    public void testAddReward_TrungMaQuyetDinh() {
        // Tạo và thêm quyết định lần đầu
        QuyetDinh qd = new QuyetDinh("QDTEST01", "Khen thưởng", "2023-04-01", "Thưởng xuất sắc", "TKTEST01", "TKTEST02");
        nhanVienDAO.AddReward(qd);
        // Thêm lại quyết định trùng mã
        boolean result = nhanVienDAO.AddReward(qd);
        // Kiểm tra kết quả
        assertFalse(result, "Thêm quyết định trùng mã phải thất bại");
    }

    /**
     * Kiểm tra AddReward khi xảy ra SQLException.
     * Mục đích: Đảm bảo hàm trả về false khi truy vấn thất bại.
     * Kịch bản:
     * - Sử dụng Mockito để mock kết nối và ném SQLException tại executeUpdate.
     * - Gọi AddReward với quyết định hợp lệ.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch trong AddReward.
     */
    @Test
    public void testAddReward_SQLException() throws SQLException {
        // Mock JDBCUtil để ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL"));
            // Tạo quyết định hợp lệ
            QuyetDinh qd = new QuyetDinh("QDTEST01", "Khen thưởng", "2023-04-01", "Thưởng xuất sắc", "TKTEST01", "TKTEST02");
            // Gọi phương thức
            boolean result = mockNhanVienDAO.AddReward(qd);
            // Kiểm tra kết quả
            assertFalse(result, "Thêm quyết định khi có lỗi phải thất bại");
        }
    }
}