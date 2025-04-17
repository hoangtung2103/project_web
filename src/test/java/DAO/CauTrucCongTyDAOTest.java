package DAO;

import Models.CayChiNhanh;
import Models.CayPhongBan;
import Models.CayNhom;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CauTrucCongTyDAOTest {

    private CauTrucCongTyDAO dao;

    @BeforeEach
    void setUp() {
        dao = new CauTrucCongTyDAO();
    }

    @Test
    void testLoadStructure_NotNull() {
        List<CayChiNhanh> result = dao.LoadStructure();
        assertNotNull(result, "Danh sách chi nhánh không được null");
    }

    @Test
    void testLoadStructure_ChiNhanhNotEmpty() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        assertFalse(chiNhanhs.isEmpty(), "Danh sách chi nhánh không được rỗng");
    }

    @Test
    void testLoadStructure_PhongBanTonTai() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        assertNotNull(chiNhanhs);
        assertFalse(chiNhanhs.isEmpty());

        for (CayChiNhanh cn : chiNhanhs) {
            List<CayPhongBan> phongBans = cn.getPhongbans();
            assertNotNull(phongBans, "Danh sách phòng ban phải tồn tại với chi nhánh: " + cn.getMaChiNhanh());
        }
    }

    @Test
    void testLoadStructure_NhomTonTai() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        assertNotNull(chiNhanhs);
        assertFalse(chiNhanhs.isEmpty());

        for (CayChiNhanh cn : chiNhanhs) {
            for (CayPhongBan pb : cn.getPhongbans()) {
                List<CayNhom> nhoms = pb.getNhoms();
                assertNotNull(nhoms, "Danh sách nhóm phải tồn tại cho phòng ban: " + pb.getMaPhongBan());
            }
        }
    }

    @Test
    void testLoadStructure_ChiNhanhDuTenVaMa() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        for (CayChiNhanh cn : chiNhanhs) {
            assertNotNull(cn.getMaChiNhanh());
            assertNotNull(cn.getTenChiNhanh());
            assertFalse(cn.getMaChiNhanh().isBlank(), "Mã CN không được trống");
            assertFalse(cn.getTenChiNhanh().isBlank(), "Tên CN không được trống");
        }
    }

    @Test
    void testLoadStructure_PhongBanDuTenVaMa() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        for (CayChiNhanh cn : chiNhanhs) {
            for (CayPhongBan pb : cn.getPhongbans()) {
                assertNotNull(pb.getMaPhongBan());
                assertNotNull(pb.getTenPhongBan());
            }
        }
    }

    @Test
    void testLoadStructure_NhomDuTenVaMa() {
        List<CayChiNhanh> chiNhanhs = dao.LoadStructure();
        for (CayChiNhanh cn : chiNhanhs) {
            for (CayPhongBan pb : cn.getPhongbans()) {
                for (CayNhom nhom : pb.getNhoms()) {
                    assertNotNull(nhom.getMaNhom());
                    assertNotNull(nhom.getTenNhom());
                }
            }
        }
    }
}
