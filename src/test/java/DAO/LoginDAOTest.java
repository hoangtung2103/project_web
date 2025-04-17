package DAO;

import Models.TaiKhoan;
import JDBCUtils.JDBCUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LoginDAOTest {

    private Connection connection;

    @BeforeAll
    public void setUp() throws SQLException {
        connection = JDBCUtil.getConnection();
        connection.setAutoCommit(false); // Để kiểm soát commit thủ công
    }

    @BeforeEach
    public void insertTestData() throws SQLException {
        // Chèn tài khoản
        try (PreparedStatement ps1 = connection.prepareStatement(
                "INSERT INTO taikhoan (MaTaiKhoan, TenDangNhap, MatKhau, Quyen) VALUES (?, ?, ?, ?)")) {
            ps1.setString(1, "TK_TEST");
            ps1.setString(2, "test_user");
            ps1.setString(3, "test_pass");
            ps1.setString(4, "admin");
            ps1.executeUpdate();
        }

        // Chèn thông tin người dùng
        try (PreparedStatement ps2 = connection.prepareStatement(
                "INSERT INTO thongtinnguoidung (MaTaiKhoan, HoTen, GioiTinh, CCCD, NgayCap, NoiCap, NgaySinh, SoDienThoai, Email, " +
                        "SoNha, Xa, Huyen, Tinh, HeSoLuong, TrangThai, TrinhDo, NgayBatDauLam) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            ps2.setString(1, "TK_TEST");
            ps2.setString(2, "Nguyen Van Test");
            ps2.setString(3, "Nam");
            ps2.setString(4, "123456789012");
            ps2.setString(5, "2020-01-01");
            ps2.setString(6, "Hà Nội");
            ps2.setString(7, "1995-05-05");
            ps2.setString(8, "0123456789");
            ps2.setString(9, "test_user@example.com");
            ps2.setString(10, "123A");
            ps2.setString(11, "Phường 1");
            ps2.setString(12, "Quận 1");
            ps2.setString(13, "TP.HCM");
            ps2.setFloat(14, 1.5f);
            ps2.setString(15, "Đang làm");
            ps2.setString(16, "Đại học");
            ps2.setString(17, "2022-06-01");
            ps2.executeUpdate();
        }

        // Commit dữ liệu để LoginDAO có thể thấy
        connection.commit();
    }

    @AfterEach
    public void cleanUp() throws SQLException {
        // Xóa dữ liệu thủ công thay vì rollback
        try (PreparedStatement ps1 = connection.prepareStatement("DELETE FROM thongtinnguoidung WHERE MaTaiKhoan = ?")) {
            ps1.setString(1, "TK_TEST");
            ps1.executeUpdate();
        }
        try (PreparedStatement ps2 = connection.prepareStatement("DELETE FROM taikhoan WHERE MaTaiKhoan = ?")) {
            ps2.setString(1, "TK_TEST");
            ps2.executeUpdate();
        }
        // Commit việc xóa
        connection.commit();
    }

    @AfterAll
    public void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    public void testValidateLogin_CorrectCredentials() throws ClassNotFoundException {
        // Test đúng tài khoản và mật khẩu
        TaiKhoan login = new TaiKhoan("test_user", "test_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNotNull(result);
        assertEquals("test_user", result.getTenDangNhap());
        assertEquals("test_pass", result.getMatKhau());
        assertEquals("TK_TEST", result.getMaTaiKhoan());
        assertEquals("admin", result.getQuyen());
    }

    @Test
    public void testValidateLogin_WrongPassword() throws ClassNotFoundException {
        // Sai mật khẩu
        TaiKhoan login = new TaiKhoan("test_user", "wrong_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_WrongUsername() throws ClassNotFoundException {
        // Sai tên đăng nhập
        TaiKhoan login = new TaiKhoan("wrong_user", "test_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_EmptyUsername() throws ClassNotFoundException {
        // Tên đăng nhập rỗng
        TaiKhoan login = new TaiKhoan("", "test_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_EmptyPassword() throws ClassNotFoundException {
        // Mật khẩu rỗng
        TaiKhoan login = new TaiKhoan("test_user", "", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_NullUsername() throws ClassNotFoundException {
        // Tên đăng nhập null
        TaiKhoan login = new TaiKhoan(null, "test_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_NullPassword() throws ClassNotFoundException {
        // Mật khẩu null
        TaiKhoan login = new TaiKhoan("test_user", null, "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result);
    }

    @Test
    public void testValidateLogin_UsernameWithSpaces() throws ClassNotFoundException {
        // Tên đăng nhập có khoảng trắng đầu/cuối
        TaiKhoan login = new TaiKhoan(" test_user ", "test_pass", "", "");
        LoginDAO dao = new LoginDAO();
        TaiKhoan result = dao.validate(login);

        assertNull(result); // Nếu DAO không trim() chuỗi thì sẽ thất bại
    }

// ------------------- Forgot Password -------------------

    @Test
    public void testLayMatKhau_DungTenDangNhapVaEmail() {
        // Đúng tên đăng nhập và email
        String matKhau = LoginDAO.LayMatKhau("test_user", "test_user@example.com");
        assertEquals("test_pass", matKhau);
    }

    @Test
    public void testLayMatKhau_SaiEmail() {
        // Sai email
        String matKhau = LoginDAO.LayMatKhau("test_user", "wrong_email@example.com");
        assertEquals("", matKhau);
    }

    @Test
    public void testLayMatKhau_SaiTenDangNhap() {
        // Sai tên đăng nhập
        String matKhau = LoginDAO.LayMatKhau("wrong_user", "test_user@example.com");
        assertEquals("", matKhau);
    }

    @Test
    public void testLayMatKhau_TenDangNhapRong() {
        // Tên đăng nhập rỗng
        String matKhau = LoginDAO.LayMatKhau("", "test_user@example.com");
        assertEquals("", matKhau);
    }

    @Test
    public void testLayMatKhau_EmailRong() {
        // Email rỗng
        String matKhau = LoginDAO.LayMatKhau("test_user", "");
        assertEquals("", matKhau);
    }

    @Test
    public void testLayMatKhau_TenDangNhapNull() {
        // Tên đăng nhập null
        String matKhau = LoginDAO.LayMatKhau(null, "test_user@example.com");
        assertEquals("", matKhau);
    }

    @Test
    public void testLayMatKhau_EmailNull() {
        // Email null
        String matKhau = LoginDAO.LayMatKhau("test_user", null);
        assertEquals("", matKhau);
    }

}