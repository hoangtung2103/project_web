package Controller;

import DAO.CauTrucCongTyDAO;
import JDBCUtils.HandleException;
import Models.CayChiNhanh;
import Models.TaiKhoan;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "CauTrucCongTyController", urlPatterns = {"/xemcautruc"})
public class CauTrucCongTyController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CauTrucCongTyDAO cauTrucCongTyDAO;

    // Constructor mặc định cho môi trường thực tế
    public CauTrucCongTyController() {
        this.cauTrucCongTyDAO = new CauTrucCongTyDAO();
    }

    // Constructor cho kiểm thử
    public CauTrucCongTyController(CauTrucCongTyDAO cauTrucCongTyDAO) {
        this.cauTrucCongTyDAO = cauTrucCongTyDAO;
    }

    @Override
    public void init() {
        // Có thể thêm logic khởi tạo nếu cần
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            LoadStructure(request, response);
        } catch (SQLException ex) {
            HandleException.printSQLException(ex);
        }
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private void LoadStructure(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException {
        HttpSession session = request.getSession();
        TaiKhoan login = (TaiKhoan) session.getAttribute("user");

        if (login == null) {
            response.sendRedirect("views/system/login.jsp");
        } else {
            List<CayChiNhanh> structure = cauTrucCongTyDAO.LoadStructure();
            request.setAttribute("CauTrucCongTy", structure);

            if (login.getQuyen().equals("admin")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/quanli/QuanLiCauTruc.jsp");
                dispatcher.forward(request, response);
            } else if (login.getQuyen().equals("giamdoc")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/giamdoc/XemCauTruc.jsp");
                dispatcher.forward(request, response);
            } else if (login.getQuyen().equals("truongphong")) {
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/truongphong/XemCauTruc.jsp");
                dispatcher.forward(request, response);
            } else {
                RequestDispatcher dispatcher = request.getRequestDispatcher("views/nhanvien/NhanVienCauTruc.jsp");
                dispatcher.forward(request, response);
            }
        }
    }
}