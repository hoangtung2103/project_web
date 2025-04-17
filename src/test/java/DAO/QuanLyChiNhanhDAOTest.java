package DAO;

import JDBCUtils.JDBCUtil;
import Models.ChiNhanh;
import Models.ThongTinNguoiDung;
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
public class QuanLyChiNhanhDAOTest {

    private QuanLyChiNhanhDAO chiNhanhDAO;

    private Connection connection;

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockPreparedStatement;

    @Mock
    private ResultSet mockResultSet;

    @InjectMocks
    private QuanLyChiNhanhDAO mockChiNhanhDAO;

    // Thiết lập kết nối cơ sở dữ liệu
    private void initConnection() throws SQLException {
        connection = JDBCUtil.getConnection();
    }

    @BeforeEach
    public void insertTestData() throws SQLException {
        // Mục đích: Chèn dữ liệu kiểm thử trước mỗi bài kiểm thử
        // Kịch bản: Chèn 3 tài khoản, 3 người dùng, 3 chi nhánh; database có 5 chi nhánh sẵn
        // Kết quả mong đợi: Tổng cộng 8 chi nhánh
        initConnection();
        chiNhanhDAO = new QuanLyChiNhanhDAO();
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            String insertTaiKhoan = "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, Quyen) VALUES (?, ?, ?, ?)";
            PreparedStatement psTaiKhoan = connection.prepareStatement(insertTaiKhoan);
            psTaiKhoan.setString(1, "TKTEST01");
            psTaiKhoan.setString(2, "giamdoctest1");
            psTaiKhoan.setString(3, "testpass1");
            psTaiKhoan.setString(4, "giamdoc");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST02");
            psTaiKhoan.setString(2, "giamdoctest2");
            psTaiKhoan.setString(3, "testpass2");
            psTaiKhoan.setString(4, "giamdoc");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.setString(1, "TKTEST03");
            psTaiKhoan.setString(2, "giamdoctest3");
            psTaiKhoan.setString(3, "testpass3");
            psTaiKhoan.setString(4, "giamdoc");
            psTaiKhoan.executeUpdate();
            psTaiKhoan.close();

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
            psNguoiDung.setFloat(14, 4.0f);
            psNguoiDung.setString(15, "Đang làm");
            psNguoiDung.setString(16, "Đại học");
            psNguoiDung.setString(17, "2023-03-01");
            psNguoiDung.executeUpdate();
            psNguoiDung.close();

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
            psChiNhanh.setString(1, "CNTEST02");
            psChiNhanh.setString(2, "Chi nhánh Test TP.HCM");
            psChiNhanh.setString(3, "Số 88");
            psChiNhanh.setString(4, "Quận 7");
            psChiNhanh.setString(5, "Quận 7");
            psChiNhanh.setString(6, "TP.HCM");
            psChiNhanh.setString(7, "2023-02-15");
            psChiNhanh.setString(8, "0289990002");
            psChiNhanh.setString(9, "TKTEST02");
            psChiNhanh.executeUpdate();
            psChiNhanh.setString(1, "CNTEST03");
            psChiNhanh.setString(2, "Chi nhánh Test Đà Nẵng");
            psChiNhanh.setString(3, "Số 77");
            psChiNhanh.setString(4, "Hải Châu");
            psChiNhanh.setString(5, "Hải Châu");
            psChiNhanh.setString(6, "Đà Nẵng");
            psChiNhanh.setString(7, "2023-03-15");
            psChiNhanh.setString(8, "02369990003");
            psChiNhanh.setString(9, "TKTEST03");
            psChiNhanh.executeUpdate();
            psChiNhanh.close();
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        }
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Mục đích: Xóa dữ liệu kiểm thử
        // Kết quả mong đợi: Database sạch, chỉ còn 5 chi nhánh sẵn có
        connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
        try {
            String deleteChiNhanh = "DELETE FROM chinhanh WHERE MaChiNhanh LIKE 'CNTEST%'";
            PreparedStatement psChiNhanh = connection.prepareStatement(deleteChiNhanh);
            psChiNhanh.executeUpdate();
            psChiNhanh.close();

            String deleteNguoiDung = "DELETE FROM thongtinnguoidung WHERE MaTaiKhoan LIKE 'TKTEST%'";
            PreparedStatement psNguoiDung = connection.prepareStatement(deleteNguoiDung);
            psNguoiDung.executeUpdate();
            psNguoiDung.close();

            String deleteTaiKhoan = "DELETE FROM taikhoan WHERE MaTaiKhoan LIKE 'TKTEST%'";
            PreparedStatement psTaiKhoan = connection.prepareStatement(deleteTaiKhoan);
            psTaiKhoan.executeUpdate();
            psTaiKhoan.close();
        } finally {
            connection.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        }
    }


    // --- Kiểm thử loadInfomation ---
    @Test
    public void testLoadInfomation_DanhSachDungSoLuong() {
        // Mục đích: Kiểm tra số lượng bản ghi trả về
        // Kịch bản: Database có 146 bản ghi sẵn + 3 bản ghi kiểm thử
        // Kết quả mong đợi: 149 bản ghi
        List<ThongTinNguoiDung> danhSach = chiNhanhDAO.loadInfomation();
        assertNotNull(danhSach, "Danh sách thông tin người dùng không được null");
        assertEquals(149, danhSach.size(), "Danh sách phải chứa đúng 149 bản ghi");
    }

    @Test
    public void testLoadInfomation_NoiDungDung() {
        // Mục đích: Kiểm tra nội dung bản ghi TKTEST01
        // Kết quả mong đợi: Các trường HoTen, SoDienThoai, Email, v.v. đúng
        List<ThongTinNguoiDung> danhSach = chiNhanhDAO.loadInfomation();
        ThongTinNguoiDung nguoiDung = danhSach.stream()
                .filter(nd -> nd.getMataikhoan().equals("TKTEST01"))
                .findFirst().orElse(null);
        assertNotNull(nguoiDung, "Bản ghi TKTEST01 phải tồn tại");
        assertEquals("Nguyễn Văn Test", nguoiDung.getHoTen(), "Họ tên phải đúng");
        assertEquals("Nam", nguoiDung.getGioitinh(), "Giới tính phải đúng");
        assertEquals("999000001", nguoiDung.getCccd(), "CCCD phải đúng");
        assertEquals("2023-01-01", nguoiDung.getNgayCap(), "Ngày cấp phải đúng");
        assertEquals("Hà Nội", nguoiDung.getNoiCap(), "Nơi cấp phải đúng");
        assertEquals("1985-05-10", nguoiDung.getNgaySinh(), "Ngày sinh phải đúng");
        assertEquals("0980000001", nguoiDung.getSdt(), "Số điện thoại phải đúng");
        assertEquals("test1@example.com", nguoiDung.getEmail(), "Email phải đúng");
        assertEquals("123", nguoiDung.getSoNha(), "Số nhà phải đúng");
        assertEquals("Cầu Giấy", nguoiDung.getXa(), "Xã phải đúng");
        assertEquals("Cầu Giấy", nguoiDung.getHuyen(), "Huyện phải đúng");
        assertEquals("Hà Nội", nguoiDung.getTinh(), "Tỉnh phải đúng");
        assertEquals(3.0f, nguoiDung.getHeSoLuong(), 0.001f, "Hệ số lương phải đúng");
        assertEquals("Đang làm", nguoiDung.getTrangThai(), "Trạng thái phải đúng");
        assertEquals("Đại học", nguoiDung.getTrinhDo(), "Trình độ phải đúng");
        assertEquals("2023-01-01", nguoiDung.getNgayBatDauLam(), "Ngày bắt đầu làm phải đúng");
    }


    @Test
    public void testLoadInfomation_SQLException() throws SQLException {
        // Mục đích: Kiểm tra khi xảy ra SQLException
        // Kịch bản: Mock JDBCUtil.getConnection() và ném SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL giả lập"));
            List<ThongTinNguoiDung> danhSach = mockChiNhanhDAO.loadInfomation();
            assertNotNull(danhSach, "Danh sách không được null");
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi xảy ra lỗi");
        }
    }


    // --- Kiểm thử loadInfomationchinhanh ---
    @Test
    public void testLoadInfomationchinhanh_DanhSachDungSoLuong() {
        // Mục đích: Kiểm tra số lượng chi nhánh
        // Kết quả mong đợi: 8 chi nhánh (5 sẵn có + 3 kiểm thử)
        List<ChiNhanh> danhSach = QuanLyChiNhanhDAO.loadInfomationchinhanh();
        assertNotNull(danhSach, "Danh sách chi nhánh không được null");
        assertEquals(8, danhSach.size(), "Danh sách phải chứa đúng 8 chi nhánh");
    }

    @Test
    public void testLoadInfomationchinhanh_ChiNhanhDungNoiDung() {
        List<ChiNhanh> danhSach = QuanLyChiNhanhDAO.loadInfomationchinhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertNotNull(cn, "Chi nhánh CNTEST01 phải tồn tại");
        assertEquals("Chi nhánh Test Hà Nội", cn.getTenChiNhanh(), "Tên chi nhánh phải đúng");
        assertEquals("TKTEST01", cn.getMaGiamDoc(), "Mã giám đốc phải đúng");
        assertEquals("0249990001", cn.getSdt(), "Số điện thoại phải đúng");
    }


    @Test
    public void testLoadInfomationchinhanh_SQLException() throws SQLException {
        // Mục đích: Kiểm tra khi xảy ra SQLException
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL giả lập"));

            List<ChiNhanh> danhSach = QuanLyChiNhanhDAO.loadInfomationchinhanh();
            assertNotNull(danhSach, "Danh sách không được null");
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi xảy ra lỗi");
        }
    }

    // --- Kiểm thử selectAllChiNhanh ---
    @Test
    public void testSelectAllChiNhanh_DanhSachDungSoLuong() {
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertNotNull(danhSach, "Danh sách chi nhánh không được null");
        assertEquals(8, danhSach.size(), "Danh sách phải chứa đúng 8 chi nhánh");
    }

    @Test
    public void testSelectAllChiNhanh_ContainAllChiNhanh() {
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST01")), "Phải chứa CNTEST01");
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST02")), "Phải chứa CNTEST02");
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST03")), "Phải chứa CNTEST03");
    }

    @Test
    public void testSelectAllChiNhanh_ChiNhanhDungNoiDung() {
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST02"))
                .findFirst().orElse(null);
        assertNotNull(cn, "Chi nhánh CNTEST02 phải tồn tại");
        assertEquals("Chi nhánh Test TP.HCM", cn.getTenChiNhanh(), "Tên chi nhánh phải đúng");
        assertEquals("TKTEST02", cn.getMaGiamDoc(), "Mã giám đốc phải đúng");
    }


    @Test
    public void testSelectAllChiNhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL giả lập"));

            List<ChiNhanh> danhSach = mockChiNhanhDAO.selectAllChiNhanh();
            assertNotNull(danhSach, "Danh sách không được null");
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi xảy ra lỗi");
        }
    }

    // --- Kiểm thử Addchinhanh ---
    @Test
    public void testAddchinhanh_ThanhCong() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST04", "Chi nhánh Test Cần Thơ", "Số 66",
                "Ninh Kiều", "Ninh Kiều", "Cần Thơ", "2023-04-01", "02929990004", "TKTEST01");
        boolean result = chiNhanhDAO.Addchinhanh(chiNhanh);
        assertTrue(result, "Thêm chi nhánh mới phải thành công");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh addedChiNhanh = danhSach.stream()
                .filter(cn -> cn.getMaChiNhanh().equals("CNTEST04"))
                .findFirst().orElse(null);
        assertNotNull(addedChiNhanh, "Chi nhánh vừa thêm phải tồn tại");
        assertEquals("Chi nhánh Test Cần Thơ", addedChiNhanh.getTenChiNhanh(), "Tên chi nhánh phải đúng");
        assertEquals("TKTEST01", addedChiNhanh.getMaGiamDoc(), "Mã giám đốc phải đúng");
    }

    @Test
    public void testAddchinhanh_MaTongGiamDocKhongTonTai() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST05", "Chi nhánh Test Hải Phòng", "Số 55",
                "Lê Chân", "Lê Chân", "Hải Phòng", "2023-05-01", "02259990005", "TKKHONGTONTAI");
        boolean result = chiNhanhDAO.Addchinhanh(chiNhanh);
        assertFalse(result, "Thêm chi nhánh với MaGiamDoc không tồn tại phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertFalse(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST05")), "Chi nhánh không được thêm");
    }

    @Test
    public void testAddchinhanh_MaChiNhanhTrung() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "Chi nhánh Trùng", "Số 11",
                "Ba Đình", "Ba Đình", "Hà Nội", "2023-06-01", "0241111111", "TKTEST02");
        boolean result = chiNhanhDAO.Addchinhanh(chiNhanh);
        assertFalse(result, "Thêm chi nhánh với MaChiNhanh trùng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertEquals("Chi nhánh Test Hà Nội", cn.getTenChiNhanh(), "Tên chi nhánh không được thay đổi");
    }

    @Test
    public void testAddchinhanh_DuLieuRong() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST06", "", "", "", "", "", "", "", "TKTEST01");
        boolean result = chiNhanhDAO.Addchinhanh(chiNhanh);
        assertFalse(result, "Thêm chi nhánh với dữ liệu rỗng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertFalse(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST06")), "Chi nhánh không được thêm");
    }

    @Test
    public void testAddchinhanh_NullChiNhanh() {
        boolean result = chiNhanhDAO.Addchinhanh(null);
        assertFalse(result, "Thêm chi nhánh null phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }


    @Test
    public void testAddchinhanh_NgaySaiDinhDang() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST08", "Chi nhánh Test Nha Trang", "Số 33",
                "Vĩnh Hải", "Vĩnh Hải", "Khánh Hòa", "2023-13-01", "02589990008", "TKTEST01");
        boolean result = chiNhanhDAO.Addchinhanh(chiNhanh);
        assertFalse(result, "Thêm chi nhánh với ngày sai định dạng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertFalse(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST08")), "Chi nhánh không được thêm");
    }

    @Test
    public void testAddchinhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL giả lập"));

            ChiNhanh chiNhanh = new ChiNhanh("CNTEST04", "Chi nhánh Test Cần Thơ", "Số 66",
                    "Ninh Kiều", "Ninh Kiều", "Cần Thơ", "2023-04-01", "02929990004", "TKTEST01");
            boolean result = mockChiNhanhDAO.Addchinhanh(chiNhanh);
            assertFalse(result, "Thêm chi nhánh khi xảy ra SQLException phải thất bại");
        }
    }

    // --- Kiểm thử selecttenChiNhanh ---
    @Test
    public void testSelecttenChiNhanh_DanhSachDungSoLuong() {
        List<ChiNhanh> danhSachTen = chiNhanhDAO.selecttenChiNhanh();
        assertNotNull(danhSachTen, "Danh sách tên chi nhánh không được null");
        assertEquals(8, danhSachTen.size(), "Danh sách phải chứa đúng 8 tên chi nhánh");
    }

    @Test
    public void testSelecttenChiNhanh_TenChiNhanhDung() {
        List<ChiNhanh> danhSachTen = chiNhanhDAO.selecttenChiNhanh();
        assertTrue(danhSachTen.stream().anyMatch(cn -> cn.getTenChiNhanh().equals("Chi nhánh Test Hà Nội")), "Phải chứa tên Chi nhánh Test Hà Nội");
        assertTrue(danhSachTen.stream().anyMatch(cn -> cn.getTenChiNhanh().equals("Chi nhánh Test TP.HCM")), "Phải chứa tên Chi nhánh Test TP.HCM");
        assertTrue(danhSachTen.stream().anyMatch(cn -> cn.getTenChiNhanh().equals("Chi nhánh Test Đà Nẵng")), "Phải chứa tên Chi nhánh Test Đà Nẵng");
    }

    @Test
    public void testSelecttenChiNhanh_ChiNhanhChiChuaTen() {
        List<ChiNhanh> danhSachTen = chiNhanhDAO.selecttenChiNhanh();
        ChiNhanh cn = danhSachTen.stream()
                .filter(c -> c.getTenChiNhanh().equals("Chi nhánh Test Hà Nội"))
                .findFirst().orElse(null);
        assertNotNull(cn, "Tên chi nhánh phải tồn tại");
        assertNotNull(cn.getTenChiNhanh(), "Tên chi nhánh không được null");
        assertNull(cn.getMaChiNhanh(), "MaChiNhanh phải null");
        assertNull(cn.getSdt(), "Sdt phải null");
        assertNull(cn.getMaGiamDoc(), "MaGiamDoc phải null");
    }


    @Test
    public void testSelecttenChiNhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL giả lập"));

            List<ChiNhanh> danhSach = mockChiNhanhDAO.selecttenChiNhanh();
            assertNotNull(danhSach, "Danh sách không được null");
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi xảy ra lỗi");
        }
    }

    // --- Kiểm thử findAllChiNhanh ---
    @Test
    public void testFindAllChiNhanh_KhopChinhXac() {
        List<ChiNhanh> danhSach = chiNhanhDAO.findAllChiNhanh("Chi nhánh Test Hà Nội");
        assertNotNull(danhSach, "Danh sách tìm kiếm không được null");
        assertEquals(1, danhSach.size(), "Chỉ tìm thấy 1 chi nhánh khớp tên");
        assertEquals("CNTEST01", danhSach.get(0).getMaChiNhanh(), "Chi nhánh phải là CNTEST01");
        assertEquals("Chi nhánh Test Hà Nội", danhSach.get(0).getTenChiNhanh(), "Tên chi nhánh phải đúng");
    }

    @Test
    public void testFindAllChiNhanh_KhopMotPhan() {
        List<ChiNhanh> danhSach = chiNhanhDAO.findAllChiNhanh("Test");
        assertNotNull(danhSach, "Danh sách tìm kiếm không được null");
        assertEquals(3, danhSach.size(), "Tìm thấy 3 chi nhánh chứa 'Test'");
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST01")), "Phải chứa CNTEST01");
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST02")), "Phải chứa CNTEST02");
        assertTrue(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST03")), "Phải chứa CNTEST03");
    }

    @Test
    public void testFindAllChiNhanh_KhongKhop() {
        List<ChiNhanh> danhSach = chiNhanhDAO.findAllChiNhanh("Không Tồn Tại");
        assertNotNull(danhSach, "Danh sách tìm kiếm không được null");
        assertTrue(danhSach.isEmpty(), "Tìm kiếm tên không tồn tại phải trả về danh sách rỗng");
    }

    @Test
    public void testFindAllChiNhanh_ChuoiRong() {
        List<ChiNhanh> danhSach = chiNhanhDAO.findAllChiNhanh("");
        assertNotNull(danhSach, "Danh sách tìm kiếm không được null");
        assertEquals(8, danhSach.size(), "Tìm với chuỗi rỗng phải trả về tất cả chi nhánh");
    }

    @Test
    public void testFindAllChiNhanh_ChuoiNull() {
        List<ChiNhanh> danhSach = chiNhanhDAO.findAllChiNhanh(null);
        assertNotNull(danhSach, "Danh sách tìm kiếm không được null");
        assertTrue(danhSach.isEmpty() || danhSach.size() == 8, "Tìm với null phải trả về rỗng hoặc tất cả chi nhánh");
    }

    @Test
    public void testFindAllChiNhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Lỗi SQL giả lập"));

            List<ChiNhanh> danhSach = mockChiNhanhDAO.findAllChiNhanh("Test");
            assertNotNull(danhSach, "Danh sách không được null");
            assertTrue(danhSach.isEmpty(), "Danh sách phải rỗng khi xảy ra lỗi");
        }
    }

    // --- Kiểm thử Deletechinhanh ---
    @Test
    public void testDeletechinhanh_ThanhCong() {
        boolean result = chiNhanhDAO.Deletechinhanh("CNTEST01");
        assertTrue(result, "Xóa chi nhánh tồn tại phải thành công");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(7, danhSach.size(), "Sau khi xóa, chỉ còn 7 chi nhánh");
        assertFalse(danhSach.stream().anyMatch(cn -> cn.getMaChiNhanh().equals("CNTEST01")), "Chi nhánh CNTEST01 không được tồn tại");
    }

    @Test
    public void testDeletechinhanh_KhongTonTai() {
        boolean result = chiNhanhDAO.Deletechinhanh("CNKHONGTONTAI");
        assertFalse(result, "Xóa chi nhánh không tồn tại phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }

    @Test
    public void testDeletechinhanh_MaRong() {
        boolean result = chiNhanhDAO.Deletechinhanh("");
        assertFalse(result, "Xóa chi nhánh với mã rỗng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }

    @Test
    public void testDeletechinhanh_MaNull() {
        boolean result = chiNhanhDAO.Deletechinhanh(null);
        assertFalse(result, "Xóa chi nhánh với mã null phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }

    @Test
    public void testDeletechinhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL giả lập"));

            boolean result = mockChiNhanhDAO.Deletechinhanh("CNTEST01");
            assertFalse(result, "Xóa chi nhánh khi xảy ra SQLException phải thất bại");
        }
    }

    // --- Kiểm thử UpdateChinhanh ---
    @Test
    public void testUpdateChinhanh_ThanhCong() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "Chi nhánh Test Hà Nội Updated", "Số 100",
                "Ba Đình", "Ba Đình", "Hà Nội", "2023-01-20", "0248888888", "TKTEST02");
        boolean result = chiNhanhDAO.UpdateChinhanh(chiNhanh);
        assertTrue(result, "Sửa chi nhánh phải thành công");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh updatedChiNhanh = danhSach.stream()
                .filter(cn -> cn.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertNotNull(updatedChiNhanh, "Chi nhánh vừa sửa phải tồn tại");
        assertEquals("Chi nhánh Test Hà Nội Updated", updatedChiNhanh.getTenChiNhanh(), "Tên chi nhánh phải được cập nhật");
        assertEquals("Số 100", updatedChiNhanh.getSoNha(), "Số nhà phải được cập nhật");
        assertEquals("TKTEST02", updatedChiNhanh.getMaGiamDoc(), "Mã giám đốc phải được cập nhật");
    }

    @Test
    public void testUpdateChinhanh_KhongTonTai() {
        ChiNhanh chiNhanh = new ChiNhanh("CNKHONGTONTAI", "Chi nhánh Không Tồn Tại", "Số 00",
                "Không có", "Không có", "Không có", "2023-01-01", "0000000000", "TKTEST01");
        boolean result = chiNhanhDAO.UpdateChinhanh(chiNhanh);
        assertFalse(result, "Sửa chi nhánh không tồn tại phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }

    @Test
    public void testUpdateChinhanh_MaTongGiamDocKhongTonTai() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "Chi nhánh Test Hà Nội Updated", "Số 100",
                "Ba Đình", "Ba Đình", "Hà Nội", "2023-01-20", "0248888888", "TKKHONGTONTAI");
        boolean result = chiNhanhDAO.UpdateChinhanh(chiNhanh);
        assertFalse(result, "Sửa chi nhánh với MaGiamDoc không tồn tại phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertEquals("Chi nhánh Test Hà Nội", cn.getTenChiNhanh(), "Tên chi nhánh không được thay đổi");
    }

    @Test
    public void testUpdateChinhanh_DuLieuRong() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "", "", "", "", "", "", "", "TKTEST01");
        boolean result = chiNhanhDAO.UpdateChinhanh(chiNhanh);
        assertFalse(result, "Sửa chi nhánh với dữ liệu rỗng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertEquals("Chi nhánh Test Hà Nội", cn.getTenChiNhanh(), "Tên chi nhánh không được thay đổi");
    }

    @Test
    public void testUpdateChinhanh_NullChiNhanh() {
        boolean result = chiNhanhDAO.UpdateChinhanh(null);
        assertFalse(result, "Sửa chi nhánh null phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        assertEquals(8, danhSach.size(), "Danh sách vẫn phải chứa 8 chi nhánh");
    }


    @Test
    public void testUpdateChinhanh_NgaySaiDinhDang() {
        ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "Chi nhánh Test Hà Nội Updated", "Số 100",
                "Ba Đình", "Ba Đình", "Hà Nội", "2023-13-01", "0248888888", "TKTEST02");
        boolean result = chiNhanhDAO.UpdateChinhanh(chiNhanh);
        assertFalse(result, "Sửa chi nhánh với ngày sai định dạng phải thất bại");
        List<ChiNhanh> danhSach = chiNhanhDAO.selectAllChiNhanh();
        ChiNhanh cn = danhSach.stream()
                .filter(c -> c.getMaChiNhanh().equals("CNTEST01"))
                .findFirst().orElse(null);
        assertEquals("Chi nhánh Test Hà Nội", cn.getTenChiNhanh(), "Tên chi nhánh không được thay đổi");
    }

    @Test
    public void testUpdateChinhanh_SQLException() throws SQLException {
        try (var mockedStatic = mockStatic(JDBCUtil.class)) {
            mockedStatic.when(JDBCUtil::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeUpdate()).thenThrow(new SQLException("Lỗi SQL giả lập"));

            ChiNhanh chiNhanh = new ChiNhanh("CNTEST01", "Chi nhánh Test Hà Nội Updated", "Số 100",
                    "Ba Đình", "Ba Đình", "Hà Nội", "2023-01-20", "0248888888", "TKTEST02");
            boolean result = mockChiNhanhDAO.UpdateChinhanh(chiNhanh);
            assertFalse(result, "Sửa chi nhánh khi xảy ra SQLException phải thất bại");
        }
    }
}