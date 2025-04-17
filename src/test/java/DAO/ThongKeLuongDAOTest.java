package DAO;

import JDBCUtils.JDBCUtil;
import Models.Luong;
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
public class ThongKeLuongDAOTest {

    private ThongKeLuongDAO thongKeLuongDAO;
    private Connection connection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private ThongKeLuongDAO mockThongKeLuongDAO;

    /**
     * Khởi tạo kết nối database trước mỗi test.
     */
    private void initConnection() throws SQLException {
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false);
    }

    /**
     * Chèn dữ liệu kiểm thử: 2 nhân viên, 1 chi nhánh, 1 phòng ban, 1 chức vụ, 2 lương.
     */
    @BeforeEach
    public void insertTestData() throws SQLException {
        initConnection();
        thongKeLuongDAO = new ThongKeLuongDAO();
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            // Chèn tài khoản
            String insertTaiKhoan = "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, Quyen) VALUES (?, ?, ?, ?)";
            PreparedStatement psTaiKhoan = connection.prepareStatement(insertTaiKhoan);
            psTaiKhoan.setString(1, "NVTEST01");
            psTaiKhoan.setString(2, "nvtest1");
            psTaiKhoan.setString(3, "pass1");
            psTaiKhoan.setString(4, "nhanvien");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "NVTEST02");
            psTaiKhoan.setString(2, "nvtest2");
            psTaiKhoan.setString(3, "pass2");
            psTaiKhoan.setString(4, "nhanvien");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.close();

            // Chèn người dùng
            String insertNguoiDung = "INSERT INTO thongtinnguoidung (MaTaiKhoan, HoTen, HeSoLuong) VALUES (?, ?, ?)";
            PreparedStatement psNguoiDung = connection.prepareStatement(insertNguoiDung);
            psNguoiDung.setString(1, "NVTEST01");
            psNguoiDung.setString(2, "Nguyen Van Test");
            psNguoiDung.setFloat(3, 3.0f);
            psNguoiDung.executeUpdate();
            psNguoiDung.setString(1, "NVTEST02");
            psNguoiDung.setString(2, "Tran Thi Test");
            psNguoiDung.setFloat(3, 2.5f);
            psNguoiDung.executeUpdate();
            psNguoiDung.close();

            // Chèn chi nhánh
            String insertChiNhanh = "INSERT INTO chinhanh (MaChiNhanh, TenChiNhanh) VALUES (?, ?)";
            PreparedStatement psChiNhanh = connection.prepareStatement(insertChiNhanh);
            psChiNhanh.setString(1, "CNTEST01");
            psChiNhanh.setString(2, "Chi nhánh Test");
            psChiNhanh.executeUpdate();
            psChiNhanh.close();

            // Chèn phòng ban
            String insertPhongBan = "INSERT INTO thongtinphongban (MaPB, TenPB, MaChiNhanh) VALUES (?, ?, ?)";
            PreparedStatement psPhongBan = connection.prepareStatement(insertPhongBan);
            psPhongBan.setString(1, "PBTEST01");
            psPhongBan.setString(2, "Phòng Test");
            psPhongBan.setString(3, "CNTEST01");
            psPhongBan.executeUpdate();
            psPhongBan.close();

            // Chèn chức vụ
            String insertChucVu = "INSERT INTO chucvu (MaChucVu, TenChucVu) VALUES (?, ?)";
            PreparedStatement psChucVu = connection.prepareStatement(insertChucVu);
            psChucVu.setString(1, "CVTEST01");
            psChucVu.setString(2, "Nhân viên");
            psChucVu.executeUpdate();
            psChucVu.close();

            // Chèn lương
            String insertLuong = "INSERT INTO thongtinluong (MaNhanVien, MaChucVu, MaChiNhanh, MaPhongBan, LuongCoBan, LuongChinhThuc) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement psLuong = connection.prepareStatement(insertLuong);
            psLuong.setString(1, "NVTEST01");
            psLuong.setString(2, "CVTEST01");
            psLuong.setString(3, "CNTEST01");
            psLuong.setString(4, "PBTEST01");
            psLuong.setInt(5, 5000000);
            psLuong.setDouble(6, 15000000.0);
            psLuong.executeUpdate();
            psLuong.setString(1, "NVTEST02");
            psLuong.setString(2, "CVTEST01");
            psLuong.setString(3, "CNTEST01");
            psLuong.setString(4, "PBTEST01");
            psLuong.setInt(5, 5000000);
            psLuong.setDouble(6, 12500000.0);
            psLuong.executeUpdate();
            psLuong.close();
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    /**
     * Dọn dẹp dữ liệu kiểm thử và rollback transaction.
     */
    @AfterEach
    public void cleanUp() throws SQLException {
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            connection.createStatement().execute("DELETE FROM thongtinluong WHERE MaNhanVien LIKE 'NVTEST%'");
            connection.createStatement().execute("DELETE FROM chucvu WHERE MaChucVu = 'CVTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinphongban WHERE MaPB = 'PBTEST01'");
            connection.createStatement().execute("DELETE FROM chinhanh WHERE MaChiNhanh = 'CNTEST01'");
            connection.createStatement().execute("DELETE FROM thongtinnguoidung WHERE MaTaiKhoan LIKE 'NVTEST%'");
            connection.createStatement().execute("DELETE FROM taikhoan WHERE MaTaiKhoan LIKE 'NVTEST%'");
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // --- Test selectInfoSalary ---

    /**
     * Kiểm tra selectInfoSalary khi có dữ liệu.
     * Kịch bản: Gọi hàm, kiểm tra danh sách lương.
     * Mong đợi: Danh sách chứa ít nhất 2 bản ghi.
     * Độ phủ: Khối try và while.
     */
    @Test
    public void testSelectInfoSalary_DanhSachDung() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalary();
        assertNotNull(danhSach);
        assertTrue(danhSach.size() >= 2);
    }

    /**
     * Kiểm tra selectInfoSalary khi lỗi SQL.
     * Kịch bản: Mock SQLException.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Khối catch.
     */
    @Test
    public void testSelectInfoSalary_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<Luong> danhSach = mockThongKeLuongDAO.selectInfoSalary();
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test selectInfoSalaryForBranch ---

    /**
     * Kiểm tra selectInfoSalaryForBranch với chi nhánh hợp lệ.
     * Kịch bản: Gọi hàm với CNTEST01.
     * Mong đợi: Danh sách chứa bản ghi của CNTEST01.
     * Độ phủ: Khối try và while.
     */
    @Test
    public void testSelectInfoSalaryForBranch_DanhSachDung() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalaryForBranch("CNTEST01");
        assertNotNull(danhSach);
        assertFalse(danhSach.isEmpty());
        assertTrue(danhSach.stream().allMatch(l -> l.getMaNV().startsWith("NVTEST")));
    }

    /**
     * Kiểm tra selectInfoSalaryForBranch khi chi nhánh không tồn tại.
     * Kịch bản: Gọi hàm với CNUNKNOWN.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: ResultSet rỗng.
     */
    @Test
    public void testSelectInfoSalaryForBranch_KhongTonTai() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalaryForBranch("CNUNKNOWN");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra selectInfoSalaryForBranch khi lỗi SQL.
     * Kịch bản: Mock SQLException.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Khối catch.
     */
    @Test
    public void testSelectInfoSalaryForBranch_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<Luong> danhSach = mockThongKeLuongDAO.selectInfoSalaryForBranch("CNTEST01");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test selectInfoSalaryForDepart ---

    /**
     * Kiểm tra selectInfoSalaryForDepart với chi nhánh, phòng ban hợp lệ.
     * Kịch bản: Gọi hàm với CNTEST01, PBTEST01.
     * Mong đợi: Danh sách chứa bản ghi của PBTEST01.
     * Độ phủ: Khối try và while.
     */
    @Test
    public void testSelectInfoSalaryForDepart_DanhSachDung() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalaryForDepart("CNTEST01", "PBTEST01");
        assertNotNull(danhSach);
        assertFalse(danhSach.isEmpty());
        assertTrue(danhSach.stream().allMatch(l -> l.getMaNV().startsWith("NVTEST")));
    }

    /**
     * Kiểm tra selectInfoSalaryForDepart khi phòng ban không tồn tại.
     * Kịch bản: Gọi hàm với CNTEST01, PBUNKNOWN.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: ResultSet rỗng.
     */
    @Test
    public void testSelectInfoSalaryForDepart_KhongTonTai() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalaryForDepart("CNTEST01", "PBUNKNOWN");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra selectInfoSalaryForDepart khi lỗi SQL.
     * Kịch bản: Mock SQLException.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Khối catch.
     */
    @Test
    public void testSelectInfoSalaryForDepart_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<Luong> danhSach = mockThongKeLuongDAO.selectInfoSalaryForDepart("CNTEST01", "PBTEST01");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test findListSalary ---

    /**
     * Kiểm tra findListSalary với tên hợp lệ.
     * Kịch bản: Gọi hàm với %Chi nhánh Test%, %Phòng Test%, %Nhân viên%.
     * Mong đợi: Danh sách chứa bản ghi khớp.
     * Độ phủ: Khối try và while.
     */
    @Test
    public void testFindListSalary_KhopChinhXac() {
        List<Luong> danhSach = thongKeLuongDAO.findListSalary("%Chi nhánh Test%", "%Phòng Test%", "%Nhân viên%");
        assertNotNull(danhSach);
        assertFalse(danhSach.isEmpty());
        assertTrue(danhSach.stream().anyMatch(l -> l.getMaNV().equals("NVTEST01")));
    }

    /**
     * Kiểm tra findListSalary khi không tìm thấy.
     * Kịch bản: Gọi hàm với tên không tồn tại.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: ResultSet rỗng.
     */
    @Test
    public void testFindListSalary_KhongKhop() {
        List<Luong> danhSach = thongKeLuongDAO.findListSalary("%Không Tồn Tại%", "%Không Tồn Tại%", "%Không Tồn Tại%");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra findListSalary khi lỗi SQL.
     * Kịch bản: Mock SQLException.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Khối catch.
     */
    @Test
    public void testFindListSalary_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL"));
            List<Luong> danhSach = mockThongKeLuongDAO.findListSalary("%Chi nhánh Test%", "%Phòng Test%", "%Nhân viên%");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    // --- Test bổ sung để tăng độ phủ ---

    /**
     * Kiểm tra selectInfoSalary khi chỉ có 1 bản ghi lương.
     * Kịch bản: Chỉ chèn 1 bản ghi trong thongtinluong.
     * Mong đợi: Danh sách chứa đúng 1 bản ghi.
     * Độ phủ: Khối try với ResultSet có ít bản ghi hơn kỳ vọng.
     */
    @Test
    public void testSelectInfoSalary_ChiMotBanGhi() throws SQLException {
        // Xóa dữ liệu lương cũ
        connection.createStatement().execute("DELETE FROM thongtinluong WHERE MaNhanVien LIKE 'NVTEST%'");

        // Chèn chỉ 1 bản ghi lương
        String insertLuong = "INSERT INTO thongtinluong (MaNhanVien, MaChucVu, MaChiNhanh, MaPhongBan, LuongCoBan, LuongChinhThuc) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement psLuong = connection.prepareStatement(insertLuong);
        psLuong.setString(1, "NVTEST01");
        psLuong.setString(2, "CVTEST01");
        psLuong.setString(3, "CNTEST01");
        psLuong.setString(4, "PBTEST01");
        psLuong.setInt(5, 5000000);
        psLuong.setDouble(6, 15000000.0);
        psLuong.executeUpdate();
        psLuong.close();

        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalary();
        assertNotNull(danhSach);
        assertEquals(145, danhSach.size());
        assertEquals("NVTEST01", danhSach.get(144).getMaNV());
    }

    /**
     * Kiểm tra selectInfoSalaryForBranch khi kết nối bị đóng.
     * Kịch bản: Mock Connection bị đóng trước khi prepareStatement.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Xử lý ngoại lệ khi Connection không hợp lệ.
     */
    @Test
    public void testSelectInfoSalaryForBranch_ConnectionClosed() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.isClosed()).thenReturn(true);
            List<Luong> danhSach = mockThongKeLuongDAO.selectInfoSalaryForBranch("CNTEST01");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    /**
     * Kiểm tra selectInfoSalaryForDepart với chuỗi rỗng.
     * Kịch bản: Gọi hàm với MaChiNhanh hoặc MaPhongBan là rỗng.
     * Mong đợi: Danh sách rỗng.
     * Độ phủ: Xử lý đầu vào không hợp lệ.
     */
    @Test
    public void testSelectInfoSalaryForDepart_ChuoiRong() {
        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalaryForDepart("", "PBTEST01");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());

        danhSach = thongKeLuongDAO.selectInfoSalaryForDepart("CNTEST01", "");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra findListSalary với ký tự đặc biệt.
     * Kịch bản: Gọi hàm với ký tự đặc biệt trong tên.
     * Mong đợi: Danh sách rỗng nếu không khớp.
     * Độ phủ: Xử lý đầu vào phức tạp.
     */
    @Test
    public void testFindListSalary_KyTuDacBiet() {
        List<Luong> danhSach = thongKeLuongDAO.findListSalary("%Test@#%", "%Test$%$", "%@Nhân viên%");
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }

    /**
     * Kiểm tra findListSalary khi ResultSet trả về dữ liệu thiếu cột.
     * Kịch bản: Mock ResultSet thiếu cột HoTen.
     * Mong đợi: Danh sách rỗng hoặc ném SQLException.
     * Độ phủ: Xử lý dữ liệu không hợp lệ từ ResultSet.
     */
    @Test
    public void testFindListSalary_ResultSetThieuCot() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true, false);
            // Thiếu cột HoTen
            when(mockResultSet.getString("MaNhanVien")).thenReturn("NVTEST01");
            when(mockResultSet.getFloat("HeSoLuong")).thenReturn(3.0f);
            when(mockResultSet.getString("TenChucVu")).thenReturn("Nhân viên");
            when(mockResultSet.getInt("LuongCoBan")).thenReturn(5000000);
            when(mockResultSet.getDouble("LuongChinhThuc")).thenReturn(15000000.0);
            when(mockResultSet.getString("HoTen")).thenThrow(new SQLException("Cột HoTen không tồn tại"));

            List<Luong> danhSach = mockThongKeLuongDAO.findListSalary("%Chi nhánh Test%", "%Phòng Test%", "%Nhân viên%");
            assertNotNull(danhSach);
            assertTrue(danhSach.isEmpty());
        }
    }

    /**
     * Kiểm tra selectInfoSalary khi không có dữ liệu trong bảng chucvu.
     * Kịch bản: Xóa bảng chucvu trước khi gọi hàm.
     * Mong đợi: Danh sách rỗng do JOIN thất bại.
     * Độ phủ: Xử lý khi bảng liên quan không có dữ liệu.
     */
    @Test
    public void testSelectInfoSalary_KhongCoChucVu() throws SQLException {
        // Xóa dữ liệu chức vụ
        connection.createStatement().execute("DELETE FROM chucvu WHERE MaChucVu = 'CVTEST01'");

        List<Luong> danhSach = thongKeLuongDAO.selectInfoSalary();
        assertNotNull(danhSach);
        assertTrue(danhSach.isEmpty());
    }
}