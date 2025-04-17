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

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuanLyPhongBanDAOTest {

    private QuanLyPhongBanDAO phongBanDAO;
    private Connection connection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @Mock
    private CallableStatement mockCallableStatement;

    @InjectMocks
    private QuanLyPhongBanDAO mockPhongBanDAO;

    /**
     * Khởi tạo kết nối database trước mỗi test.
     * Mục đích: Tạo kết nối thực để chèn/xóa dữ liệu kiểm thử.
     */
    private void initConnection() throws SQLException {
        connection = JDBCUtil.getConnection();
    }

    /**
     * Chèn dữ liệu kiểm thử trước mỗi test.
     * Mục đích: Tạo 3 tài khoản, 3 người dùng, 1 chi nhánh, 1 phòng ban, 1 chức vụ, 3 công tác.
     * Kịch bản: Chèn dữ liệu vào taikhoan, thongtinnguoidung, chinhanh, thongtinphongban, chucvu, thongtincongtacnhanvien.
     * Kết quả mong đợi: Database sẵn sàng với dữ liệu kiểm thử.
     */
    @BeforeEach
    public void insertTestData() throws SQLException {
        initConnection();
        phongBanDAO = new QuanLyPhongBanDAO();
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            // Chèn tài khoản
            String insertTaiKhoan = "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, Quyen) VALUES (?, ?, ?, ?)";
            PreparedStatement psTaiKhoan = connection.prepareStatement(insertTaiKhoan);
            psTaiKhoan.setString(1, "TKTEST01");
            psTaiKhoan.setString(2, "testuser1");
            psTaiKhoan.setString(3, "pass1");
            psTaiKhoan.setString(4, "truongphong");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST02");
            psTaiKhoan.setString(2, "testuser2");
            psTaiKhoan.setString(3, "pass2");
            psTaiKhoan.setString(4, "truongphong");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST03");
            psTaiKhoan.setString(2, "testuser3");
            psTaiKhoan.setString(3, "pass3");
            psTaiKhoan.setString(4, "truongphong");
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
            String insertChiNhanh = "INSERT INTO chinhanh (MaChiNhanh, TenChiNhanh, SoNha, Xa, Huyen, Tinh, NgayTaoChiNhanh, SoDienThoai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement psChiNhanh = connection.prepareStatement(insertChiNhanh);
            psChiNhanh.setString(1, "CNTEST01");
            psChiNhanh.setString(2, "Chi nhánh Test Hà Nội");
            psChiNhanh.setString(3, "Số 99");
            psChiNhanh.setString(4, "Cầu Giấy");
            psChiNhanh.setString(5, "Cầu Giấy");
            psChiNhanh.setString(6, "Hà Nội");
            psChiNhanh.setString(7, "2023-01-15");
            psChiNhanh.setString(8, "0249990001");
            psChiNhanh.executeUpdate();
            psChiNhanh.close();

            // Chèn phòng ban
            String insertPhongBan = "INSERT INTO thongtinphongban (MaPB, TenPB, MaChiNhanh, NgayTao, SDT, MaTruongPhong) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psPhongBan = connection.prepareStatement(insertPhongBan);
            psPhongBan.setString(1, "PBTEST01");
            psPhongBan.setString(2, "Phòng Test 1");
            psPhongBan.setString(3, "CNTEST01");
            psPhongBan.setString(4, "2023-01-15");
            psPhongBan.setString(5, "0249990002");
            psPhongBan.setString(6, "TKTEST01");
            psPhongBan.executeUpdate();
            psPhongBan.close();

            // Chèn chức vụ
            String insertChucVu = "INSERT INTO chucvu (MaChucVu, TenChucVu) VALUES (?, ?)";
            PreparedStatement psChucVu = connection.prepareStatement(insertChucVu);
            psChucVu.setString(1, "CVTEST01");
            psChucVu.setString(2, "Trưởng phòng");
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
     * Dọn dẹp dữ liệu kiểm thử sau mỗi test.
     * Mục đích: Xóa dữ liệu kiểm thử để đảm bảo tính độc lập.
     * Kịch bản: Xóa dữ liệu từ các bảng liên quan.
     * Kết quả mong đợi: Database trở về trạng thái ban đầu.
     */
    @AfterEach
    public void cleanUp() throws SQLException {
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            connection.createStatement().execute("DELETE FROM thongtincongtacnhanvien WHERE MaTaiKhoan LIKE 'TKTEST%'");
            connection.createStatement().execute("DELETE FROM chucvu WHERE MaChucVu = 'CVTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinphongban WHERE MaPB LIKE 'PBTEST%'");
            connection.createStatement().execute("DELETE FROM chinhanh WHERE MaChiNhanh = 'CNTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinnguoidung WHERE MaTaiKhoan LIKE 'TKTEST%'");
            connection.createStatement().execute("DELETE FROM taikhoan WHERE MaTaiKhoan LIKE 'TKTEST%'");
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }

    // --- Test selectAllPhongBan ---

    /**
     * Kiểm tra selectAllPhongBan với role admin.
     * Mục đích: Đảm bảo trả về danh sách trưởng phòng.
     * Kịch bản: Gọi hàm với role admin, kiểm tra danh sách.
     * Kết quả mong đợi: Danh sách chứa ít nhất 3 bản ghi.
     * Độ phủ: Phủ nhánh role = admin và khối try.
     */
    @Test
    public void testSelectAllPhongBan_Admin_DanhSachDung() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.selectAllPhongBan(null, null, "admin");
        assertNotNull(danhSach);
        assertTrue(danhSach.size() >= 3);
    }

    /**
     * Kiểm tra selectAllPhongBan với role giám đốc.
     * Mục đích: Đảm bảo trả về trưởng phòng trong chi nhánh.
     * Kịch bản: Gọi hàm với role giám đốc và CNTEST01.
     * Kết quả mong đợi: Danh sách chỉ chứa CNTEST01.
     * Độ phủ: Phủ nhánh role = giamdoc.
     */
    @Test
    public void testSelectAllPhongBan_GiamDoc_DanhSachDung() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.selectAllPhongBan("CNTEST01", null, "giamdoc");
        assertNotNull(danhSach);
        assertTrue(danhSach.stream().allMatch(tp -> tp.getMaChiNhanh().equals("CNTEST01")));
    }


    /**
     * Kiểm tra selectAllPhongBan với role không hợp lệ.
     * Mục đích: Đảm bảo khi role không hợp lệ thì trả về danh sách rỗng.
     * Kịch bản: Gọi hàm với role "khonghople" và mã chi nhánh "CNTEST01".
     * Kết quả mong đợi: Danh sách trả về rỗng.
     * Độ phủ: Phủ nhánh role không hợp lệ.
     */
    @Test
    public void testSelectAllPhongBan_RoleKhongHopLe() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.selectAllPhongBan("CNTEST01", null, "khonghople");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }


    /**
     * Kiểm tra selectAllPhongBan với mã chi nhánh rỗng và role giám đốc.
     * Mục đích: Đảm bảo không trả về kết quả khi mã chi nhánh rỗng.
     * Kịch bản: Gọi hàm với mã chi nhánh rỗng ("") và role "giamdoc".
     * Kết quả mong đợi: Danh sách trả về rỗng.
     * Độ phủ: Phủ nhánh kiểm tra điều kiện mã chi nhánh rỗng.
     */
    @Test
    public void testSelectAllPhongBan_GiamDoc_MaChiNhanhRong() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.selectAllPhongBan("", null, "giamdoc");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra selectAllPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về danh sách rỗng khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Danh sách rỗng.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testSelectAllPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<ThongTinTruongPhong> danhSach = mockPhongBanDAO.selectAllPhongBan(null, null, "admin");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test findDepartment ---

    /**
     * Kiểm tra findDepartment với tên chi nhánh và phòng ban hợp lệ.
     * Mục đích: Đảm bảo trả về danh sách trưởng phòng khớp.
     * Kịch bản: Gọi hàm với Chi nhánh Test Hà Nội, Phòng Test 1.
     * Kết quả mong đợi: Danh sách chứa TKTEST01.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testFindDepartment_KhopChinhXac() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.findDepartment("%Chi nhánh Test Hà Nội%", "%Phòng Test 1%");
        assertNotNull(danhSach);
        assertTrue(danhSach.stream().anyMatch(tp -> tp.getMaNhanVien().equals("TKTEST01")));
    }

    /**
     * Kiểm tra findDepartment khi không tìm thấy.
     * Mục đích: Đảm bảo trả về danh sách rỗng.
     * Kịch bản: Gọi hàm với tên không tồn tại.
     * Kết quả mong đợi: Danh sách rỗng.
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testFindDepartment_KhongKhop() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.findDepartment("%Không Tồn Tại%", "%Không Tồn Tại%");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra findDepartment khi lỗi SQL.
     * Mục đích: Đảm bảo trả về danh sách rỗng khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Danh sách rỗng.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testFindDepartment_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<ThongTinTruongPhong> danhSach = mockPhongBanDAO.findDepartment("%Chi nhánh Test Hà Nội%", "%Phòng Test 1%");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test LoadInfoPhongBan ---

    /**
     * Kiểm tra LoadInfoPhongBan khi có dữ liệu.
     * Mục đích: Đảm bảo trả về danh sách thông tin phòng ban.
     * Kịch bản: Gọi hàm, kiểm tra danh sách.
     * Kết quả mong đợi: Danh sách chứa PBTEST01.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testLoadInfoPhongBan_DanhSachDung() {
        List<ThongTinTruongPhong> danhSach = phongBanDAO.LoadInfoPhongBan();
        assertNotNull(danhSach);
        assertTrue(danhSach.stream().anyMatch(tp -> tp.getMaPB().equals("PBTEST01")));
    }


    /**
     * Kiểm tra LoadInfoPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về danh sách rỗng khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Danh sách rỗng.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testLoadInfoPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<ThongTinTruongPhong> danhSach = mockPhongBanDAO.LoadInfoPhongBan();
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test LayMaChiNhanh ---

    /**
     * Kiểm tra LayMaChiNhanh khi tài khoản tồn tại.
     * Mục đích: Đảm bảo trả về mã chi nhánh đúng.
     * Kịch bản: Gọi hàm với TKTEST01.
     * Kết quả mong đợi: Trả về CNTEST01.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testLayMaChiNhanh_TonTai() {
        String maChiNhanh = phongBanDAO.LayMaChiNhanh("TKTEST01");
        assertEquals("CNTEST01", maChiNhanh);
    }

    /**
     * Kiểm tra LayMaChiNhanh khi tài khoản không tồn tại.
     * Mục đích: Đảm bảo trả về "null".
     * Kịch bản: Gọi hàm với TKUNKNOWN.
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayMaChiNhanh_KhongTonTai() {
        String maChiNhanh = phongBanDAO.LayMaChiNhanh("TKUNKNOWN");
        assertEquals("null", maChiNhanh);
    }

    /**
     * Kiểm tra LayMaChiNhanh khi lỗi SQL.
     * Mục đích: Đảm bảo trả về "null" khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testLayMaChiNhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            String maChiNhanh = mockPhongBanDAO.LayMaChiNhanh("TKTEST01");
            assertEquals("null", maChiNhanh);
        }
    }

    // --- Test LayMaPhongBan ---

    /**
     * Kiểm tra LayMaPhongBan khi tài khoản tồn tại.
     * Mục đích: Đảm bảo trả về mã phòng ban đúng.
     * Kịch bản: Gọi hàm với TKTEST01.
     * Kết quả mong đợi: Trả về PBTEST01.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testLayMaPhongBan_TonTai() {
        String maPhongBan = phongBanDAO.LayMaPhongBan("TKTEST01");
        assertEquals("PBTEST01", maPhongBan);
    }

    /**
     * Kiểm tra LayMaPhongBan khi tài khoản không tồn tại.
     * Mục đích: Đảm bảo trả về "null".
     * Kịch bản: Gọi hàm với TKUNKNOWN.
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayMaPhongBan_KhongTonTai() {
        String maPhongBan = phongBanDAO.LayMaPhongBan("TKUNKNOWN");
        assertEquals("null", maPhongBan);
    }

    /**
     * Kiểm tra LayMaPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về "null" khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về "null".
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testLayMaPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            String maPhongBan = mockPhongBanDAO.LayMaPhongBan("TKTEST01");
            assertEquals("null", maPhongBan);
        }
    }

    // --- Test LayTenChiNhanh ---

    /**
     * Kiểm tra LayTenChiNhanh khi chi nhánh tồn tại.
     * Mục đích: Đảm bảo trả về tên chi nhánh đúng.
     * Kịch bản: Gọi hàm với CNTEST01.
     * Kết quả mong đợi: Trả về Chi nhánh Test Hà Nội.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testLayTenChiNhanh_TonTai() {
        String tenChiNhanh = phongBanDAO.LayTenChiNhanh("CNTEST01");
        assertEquals("Chi nhánh Test Hà Nội", tenChiNhanh);
    }

    /**
     * Kiểm tra LayTenChiNhanh khi chi nhánh không tồn tại.
     * Mục đích: Đảm bảo trả về chuỗi rỗng.
     * Kịch bản: Gọi hàm với CNUNKNOWN.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayTenChiNhanh_KhongTonTai() {
        String tenChiNhanh = phongBanDAO.LayTenChiNhanh("CNUNKNOWN");
        assertEquals("", tenChiNhanh);
    }

    /**
     * Kiểm tra LayTenChiNhanh khi lỗi SQL.
     * Mục đích: Đảm bảo trả về chuỗi rỗng khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testLayTenChiNhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            String tenChiNhanh = mockPhongBanDAO.LayTenChiNhanh("CNTEST01");
            assertEquals("", tenChiNhanh);
        }
    }

    // --- Test LayTenPhongBan ---

    /**
     * Kiểm tra LayTenPhongBan khi phòng ban tồn tại.
     * Mục đích: Đảm bảo trả về tên phòng ban đúng.
     * Kịch bản: Gọi hàm với PBTEST01.
     * Kết quả mong đợi: Trả về Phòng Test 1.
     * Độ phủ: Phủ khối try và while.
     */
    @Test
    public void testLayTenPhongBan_TonTai() {
        String tenPhongBan = phongBanDAO.LayTenPhongBan("PBTEST01");
        assertEquals("Phòng Test 1", tenPhongBan);
    }

    /**
     * Kiểm tra LayTenPhongBan khi phòng ban không tồn tại.
     * Mục đích: Đảm bảo trả về chuỗi rỗng.
     * Kịch bản: Gọi hàm với PBUNKNOWN.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối try khi ResultSet rỗng.
     */
    @Test
    public void testLayTenPhongBan_KhongTonTai() {
        String tenPhongBan = phongBanDAO.LayTenPhongBan("PBUNKNOWN");
        assertEquals("", tenPhongBan);
    }

    /**
     * Kiểm tra LayTenPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về chuỗi rỗng khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về "".
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testLayTenPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            String tenPhongBan = mockPhongBanDAO.LayTenPhongBan("PBTEST01");
            assertEquals("", tenPhongBan);
        }
    }

    // --- Test AddPhongBan ---

    /**
     * Kiểm tra AddPhongBan khi thêm thành công.
     * Mục đích: Đảm bảo thêm phòng ban mới và trả về true.
     * Kịch bản: Gọi hàm với dữ liệu hợp lệ.
     * Kết quả mong đợi: Trả về true.
     * Độ phủ: Phủ khối try.
     */
    @Test
    public void testAddPhongBan_ThanhCong() {
        boolean result = phongBanDAO.AddPhongBan("CNTEST01", "PBTEST02", "Phòng Test 2", "2023-04-01", "0249990003", "CVTEST01", "Trưởng phòng", 5000000, "TKTEST02", "2023-04-01");
        assertTrue(result);
    }

    /**
     * Kiểm tra AddPhongBan khi mã phòng ban trùng.
     * Mục đích: Đảm bảo trả về false khi trùng mã.
     * Kịch bản: Thêm PBTEST01 hai lần.
     * Kết quả mong đợi: Lần hai trả về false.
     * Độ phủ: Phủ khối catch khi lỗi ràng buộc khóa.
     */
    @Test
    public void testAddPhongBan_TrungMa() {
        phongBanDAO.AddPhongBan("CNTEST01", "PBTEST01", "Phòng Test 1", "2023-01-15", "0249990002", "CVTEST01", "Trưởng phòng", 5000000, "TKTEST01", "2023-01-15");
        boolean result = phongBanDAO.AddPhongBan("CNTEST01", "PBTEST01", "Phòng Test 1", "2023-01-15", "0249990002", "CVTEST01", "Trưởng phòng", 5000000, "TKTEST01", "2023-01-15");
        assertFalse(result);
    }


    /**
     * Kiểm tra AddPhongBan với mã chi nhánh không tồn tại.
     * Mục đích: Đảm bảo khi mã chi nhánh không tồn tại thì không thể thêm phòng ban.
     * Kịch bản: Gọi AddPhongBan với mã chi nhánh "CNUNKNOWN" không tồn tại trong hệ thống.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ nhánh kiểm tra ràng buộc khóa ngoại mã chi nhánh.
     */
    @Test
    public void testAddPhongBan_MaChiNhanhKhongTonTai() {
        boolean result = phongBanDAO.AddPhongBan("CNUNKNOWN", "PBTEST04", "Phòng Test 4", "2023-04-01", "0249990005", "CVTEST01", "Trưởng phòng", 5000000, "TKTEST01", "2023-04-01");
        assertFalse(result);
    }


    /**
     * Kiểm tra AddPhongBan với mã trưởng phòng không tồn tại.
     * Mục đích: Đảm bảo khi mã trưởng phòng không tồn tại thì không thể thêm phòng ban.
     * Kịch bản: Gọi AddPhongBan với mã trưởng phòng "TKUNKNOWN" không tồn tại trong hệ thống.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ nhánh kiểm tra ràng buộc khóa ngoại mã trưởng phòng.
     */
    @Test
    public void testAddPhongBan_MaTruongPhongKhongTonTai() {
        boolean result = phongBanDAO.AddPhongBan("CNTEST01", "PBTEST04", "Phòng Test 4", "2023-04-01", "0249990005", "CVTEST01", "Trưởng phòng", 5000000, "TKUNKNOWN", "2023-04-01");
        assertFalse(result);
    }

    /**
     * Kiểm tra AddPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về false khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testAddPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            boolean result = mockPhongBanDAO.AddPhongBan("CNTEST01", "PBTEST02", "Phòng Test 2", "2023-04-01", "0249990003", "CVTEST01", "Trưởng phòng", 5000000, "TKTEST02", "2023-04-01");
            assertFalse(result);
        }
    }

    // --- Test SuaPhongBan ---

    /**
     * Kiểm tra SuaPhongBan khi cập nhật thành công.
     * Mục đích: Đảm bảo cập nhật phòng ban và trả về true.
     * Kịch bản: Cập nhật PBTEST01 với tên mới.
     * Kết quả mong đợi: Trả về true.
     * Độ phủ: Phủ khối try và executeUpdate > 0.
     */
    @Test
    public void testSuaPhongBan_ThanhCong() throws SQLException {
        // Mock JDBCUtil.getConnection() trả về mockConnection
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);

            // Mock CallableStatement
            when(mockConnection.prepareCall("{CALL suaPhongBan(?, ?, ?, ?, ?, ?)}"))
                    .thenReturn(mockCallableStatement);
            when(mockCallableStatement.executeUpdate()).thenReturn(1);

            // Gọi phương thức
            boolean result = mockPhongBanDAO.SuaPhongBan("CNTEST01", "PBTEST01", "Phòng Test Updated",
                    "2023-01-15", "0249990002", "CVTEST01",
                    5000000, "TKTEST01", "2023-01-15");

            // Kiểm tra kết quả
            assertTrue(result);

            // Verify các tham số được gán đúng
            verify(mockCallableStatement).setString(1, "PBTEST01");
            verify(mockCallableStatement).setString(2, "Phòng Test Updated");
            verify(mockCallableStatement).setString(3, "CNTEST01");
            verify(mockCallableStatement).setString(4, "2023-01-15");
            verify(mockCallableStatement).setString(5, "0249990002");
            verify(mockCallableStatement).setString(6, "TKTEST01");
            verify(mockCallableStatement).executeUpdate();
        }
    }

    /**
     * Kiểm tra SuaPhongBan khi phòng ban không tồn tại.
     * Mục đích: Đảm bảo trả về false khi không tìm thấy.
     * Kịch bản: Cập nhật PBUNKNOWN.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ nhánh executeUpdate = 0.
     */
    @Test
    public void testSuaPhongBan_KhongTonTai() throws SQLException {
        // Mock JDBCUtil.getConnection() trả về mockConnection
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);

            // Mock CallableStatement
            when(mockConnection.prepareCall("{CALL suaPhongBan(?, ?, ?, ?, ?, ?)}"))
                    .thenReturn(mockCallableStatement);
            when(mockCallableStatement.executeUpdate()).thenReturn(0);

            // Gọi phương thức
            boolean result = mockPhongBanDAO.SuaPhongBan("CNTEST01", "PBUNKNOWN", "Phòng Không Tồn Tại",
                    "2023-04-01", "0249990003", "CVTEST01",
                    5000000, "TKTEST02", "2023-04-01");

            // Kiểm tra kết quả
            assertFalse(result);

            // Verify các tham số được gán đúng
            verify(mockCallableStatement).setString(1, "PBUNKNOWN");
            verify(mockCallableStatement).setString(2, "Phòng Không Tồn Tại");
            verify(mockCallableStatement).setString(3, "CNTEST01");
            verify(mockCallableStatement).setString(4, "2023-04-01");
            verify(mockCallableStatement).setString(5, "0249990003");
            verify(mockCallableStatement).setString(6, "TKTEST02");
            verify(mockCallableStatement).executeUpdate();
        }
    }


    /**
     * Kiểm tra SuaPhongBan với mã chi nhánh không tồn tại.
     * Mục đích: Đảm bảo khi mã chi nhánh không tồn tại thì không thể cập nhật phòng ban.
     * Kịch bản: Gọi SuaPhongBan với mã chi nhánh "CNUNKNOWN" không tồn tại trong hệ thống.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ nhánh kiểm tra ràng buộc khóa ngoại mã chi nhánh.
     */
    @Test
    public void testSuaPhongBan_MaChiNhanhKhongTonTai() {
        boolean result = phongBanDAO.SuaPhongBan("CNUNKNOWN", "PBTEST01", "Phòng Test Updated", "2023-01-15", "0249990002", "CVTEST01", 5000000, "TKTEST01", "2023-01-15");
        assertFalse(result);
    }

    /**
     * Kiểm tra SuaPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về false khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testSuaPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            boolean result = mockPhongBanDAO.SuaPhongBan("CNTEST01", "PBTEST01", "Phòng Test Updated", "2023-01-15", "0249990002", "CVTEST01", 5000000, "TKTEST01", "2023-01-15");
            assertFalse(result);
        }
    }

    // --- Test XoaPhongBan ---

    /**
     * Kiểm tra XoaPhongBan khi xóa thành công.
     * Mục đích: Đảm bảo xóa phòng ban và trả về true.
     * Kịch bản: Xóa các bản ghi phụ thuộc trong thongtincongtacnhanvien, sau đó xóa PBTEST01.
     * Kết quả mong đợi: Trả về true.
     * Độ phủ: Phủ khối try và executeUpdate > 0.
     */
    @Test
    public void testXoaPhongBan_ThanhCong() throws SQLException {
        boolean result = phongBanDAO.XoaPhongBan("PBTEST01");
        assertTrue(result);
    }

    /**
     * Kiểm tra XoaPhongBan khi phòng ban không tồn tại.
     * Mục đích: Đảm bảo trả về false khi không tìm thấy.
     * Kịch bản: Xóa PBUNKNOWN.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ nhánh executeUpdate = 0.
     */
    @Test
    public void testXoaPhongBan_KhongTonTai() {
        boolean result = phongBanDAO.XoaPhongBan("PBUNKNOWN");
        assertFalse(result);
    }

    /**
     * Kiểm tra XoaPhongBan khi lỗi SQL.
     * Mục đích: Đảm bảo trả về false khi lỗi.
     * Kịch bản: Mock SQLException, gọi hàm.
     * Kết quả mong đợi: Trả về false.
     * Độ phủ: Phủ khối catch.
     */
    @Test
    public void testXoaPhongBan_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            boolean result = mockPhongBanDAO.XoaPhongBan("PBTEST01");
            assertFalse(result);
        }
    }
}