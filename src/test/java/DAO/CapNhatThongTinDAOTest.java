package DAO;

import Models.ThongTinCongTac;
import Models.ThongTinNguoiDung;
import Models.QuyetDinh;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CapNhatThongTinDAOTest {

    private CapNhatThongTinDAO dao;

    @BeforeEach
    void setUp() {
        dao = new CapNhatThongTinDAO();
    }

    @Test
    void testThongTinCaNhan_TonTaiMaTK() {
        String maTaiKhoan = "TK001"; // giả sử tồn tại trong CSDL test
        List<ThongTinNguoiDung> result = dao.ThongTinCaNhan(maTaiKhoan);
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Thông tin cá nhân không được rỗng");
    }

    @Test
    void testThongTinCaNhan_KhongTonTaiMaTK() {
        String maTaiKhoan = "KHONGTONTAI";
        List<ThongTinNguoiDung> result = dao.ThongTinCaNhan(maTaiKhoan);
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Phải rỗng nếu mã tài khoản không tồn tại");
    }

    @Test
    void testThongTinCaNhanCongTy_TonTaiMaTK() {
        String maTaiKhoan = "TK001"; // giả sử có trong CSDL test
        List<ThongTinCongTac> result = dao.ThongTinCaNhanCongTy(maTaiKhoan);
        assertNotNull(result);
    }

    @Test
    void testThongTinCaNhanCongTy_KhongTonTaiMaTK() {
        String maTaiKhoan = "XXX";
        List<ThongTinCongTac> result = dao.ThongTinCaNhanCongTy(maTaiKhoan);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testXemQuyetDinh_TonTaiMaTK() {
        String maTaiKhoan = "TK001"; // giả sử có quyết định trong CSDL test
        List<QuyetDinh> result = dao.XemQuyetDinh(maTaiKhoan);
        assertNotNull(result);
    }

    @Test
    void testXemQuyetDinh_KhongTonTai() {
        String maTaiKhoan = "NOPE";
        List<QuyetDinh> result = dao.XemQuyetDinh(maTaiKhoan);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testUpdateThongTinCaNhan_ThanhCong() throws SQLException {
        ThongTinNguoiDung thongTin = new ThongTinNguoiDung(
                "TK001", "Nguyen Van A", "Nam", "012345678901", "2020-01-01",
                "Hà Nội", "1990-05-15", "0909123456", "a@gmail.com",
                "123", "Xã A", "Huyện B", "Tỉnh C"
        );

        boolean result = dao.UpdateThongTinCaNhan(thongTin);
        assertTrue(result, "Phải cập nhật thành công nếu dữ liệu đúng");
    }

    @Test
    void testUpdateThongTinCaNhan_SaiMaTK() throws SQLException {
        ThongTinNguoiDung thongTin = new ThongTinNguoiDung(
                "TK_KHONG_TON_TAI", "Test", "Nam", "000000000000", "2022-01-01",
                "Test", "1990-01-01", "000", "test@test.com",
                "0", "X", "H", "T"
        );

        boolean result = dao.UpdateThongTinCaNhan(thongTin);
        assertFalse(result, "Không được cập nhật nếu mã tài khoản sai");
    }
}
