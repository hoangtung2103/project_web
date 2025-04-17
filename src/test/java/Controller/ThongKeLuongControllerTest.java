package Controller;

import DAO.QuanLyNhanVienDAO;
import DAO.ThongKeLuongDAO;
import Models.ChiNhanh;
import Models.ChucVu;
import Models.Luong;
import Models.PhongBan;
import Models.TaiKhoan;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class ThongKeLuongControllerTest {

    private HttpServletRequest request;
    private HttpServletResponse response;
    private HttpSession session;
    private RequestDispatcher dispatcher;
    private ThongKeLuongController controller;

    @BeforeEach
    public void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        session = mock(HttpSession.class);
        dispatcher = mock(RequestDispatcher.class);

        controller = new ThongKeLuongController() {
            @Override
            protected ThongKeLuongDAO initThongKeLuongDAO() {
                return new FakeThongKeLuongDAO();
            }

            @Override
            protected QuanLyNhanVienDAO initQuanLyNhanVienDAO() {
                return new FakeQuanLyNhanVienDAO();
            }
        };

        controller.init();

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(dispatcher);
    }

    private TaiKhoan user(String role) {
        TaiKhoan tk = new TaiKhoan();
        tk.setMaTaiKhoan("TK01");
        tk.setQuyen(role);
        return tk;
    }

    // ----------- /listSalary ------------

    @Test
    public void testListSalary_Admin() throws Exception {
        when(request.getServletPath()).thenReturn("/listSalary");
        when(session.getAttribute("user")).thenReturn(user("admin"));

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testListSalary_GiamDoc() throws Exception {
        when(request.getServletPath()).thenReturn("/listSalary");
        when(session.getAttribute("user")).thenReturn(user("giamdoc"));

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testListSalary_TruongPhong() throws Exception {
        when(request.getServletPath()).thenReturn("/listSalary");
        when(session.getAttribute("user")).thenReturn(user("truongphong"));

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testListSalary_NotLoggedIn() throws Exception {
        when(request.getServletPath()).thenReturn("/listSalary");
        when(session.getAttribute("user")).thenReturn(null);

        controller.doGet(request, response);
        verify(response).sendRedirect("views/system/login.jsp");
    }

    // ----------- /findSalary ------------

    @Test
    public void testFindSalary_Admin() throws Exception {
        when(request.getServletPath()).thenReturn("/findSalary");
        when(session.getAttribute("user")).thenReturn(user("admin"));
        when(request.getParameter("tenCN")).thenReturn("Chọn chi nhánh");
        when(request.getParameter("tenPB")).thenReturn("Chọn phòng ban");
        when(request.getParameter("tenCV")).thenReturn("Chọn chức vụ");

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testFindSalary_GiamDoc() throws Exception {
        when(request.getServletPath()).thenReturn("/findSalary");
        when(session.getAttribute("user")).thenReturn(user("giamdoc"));
        when(request.getParameter("tenCN")).thenReturn("CN1");
        when(request.getParameter("tenPB")).thenReturn("PB1");
        when(request.getParameter("tenCV")).thenReturn("CV1");

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testFindSalary_TruongPhong() throws Exception {
        when(request.getServletPath()).thenReturn("/findSalary");
        when(session.getAttribute("user")).thenReturn(user("truongphong"));
        when(request.getParameter("tenCN")).thenReturn("CN1");
        when(request.getParameter("tenPB")).thenReturn("PB1");
        when(request.getParameter("tenCV")).thenReturn("CV1");

        controller.doGet(request, response);
        verify(dispatcher).forward(request, response);
    }

    @Test
    public void testFindSalary_NotLoggedIn() throws Exception {
        when(request.getServletPath()).thenReturn("/findSalary");
        when(session.getAttribute("user")).thenReturn(null);

        controller.doGet(request, response);
        verify(response).sendRedirect("views/system/login.jsp");
    }

    // ------------------ Fake DAO classes ------------------

    private static class FakeThongKeLuongDAO extends ThongKeLuongDAO {
        @Override
        public List<Luong> selectInfoSalary() {
            return Collections.emptyList();
        }

        @Override
        public List<Luong> selectInfoSalaryForBranch(String maCN) {
            return Collections.emptyList();
        }

        @Override
        public List<Luong> selectInfoSalaryForDepart(String maCN, String maPB) {
            return Collections.emptyList();
        }

        @Override
        public List<Luong> findListSalary(String cn, String pb, String cv) {
            return Collections.emptyList();
        }
    }

    private static class FakeQuanLyNhanVienDAO extends QuanLyNhanVienDAO {
        @Override
        public String LayMaChiNhanh(String maTK) {
            return "CN01";
        }

        @Override
        public String LayMaPhongBan(String maTK) {
            return "PB01";
        }

        @Override
        public List<ChiNhanh> selectAllBranch() {
            return Collections.emptyList();
        }

        @Override
        public List<PhongBan> selectAllDepart() {
            return Collections.emptyList();
        }

        @Override
        public List<PhongBan> selectAllDepart(String maCN) {
            return Collections.emptyList();
        }

        @Override
        public List<ChucVu> selectAllTitle() {
            return Collections.emptyList();
        }

        @Override
        public List<ChucVu> selectAllTitle(String maCN) {
            return Collections.emptyList();
        }

        @Override
        public List<ChucVu> selectAllTitleOfDepart(String maCN, String maPB) {
            return Collections.emptyList();
        }

        @Override
        public String LayTenChiNhanh(String maCN) {
            return "ChiNhanh";
        }

        @Override
        public String LayTenPhongBan(String maPB) {
            return "PhongBan";
        }
    }
}
