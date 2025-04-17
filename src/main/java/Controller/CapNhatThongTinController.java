package Controller;

import DAO.CapNhatThongTinDAO;
import JDBCUtils.HandleException;
import Models.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@WebServlet(name = "CapNhatThongTinController", urlPatterns = {"/infoEmployee",
    "/updateInfoEmployee"})
public class CapNhatThongTinController extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private CapNhatThongTinDAO capNhatThongTinDAO = null;

  public void init() {
    capNhatThongTinDAO = new CapNhatThongTinDAO();
  }

  public CapNhatThongTinController() {
    super();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String action = request.getServletPath();
    try {
      switch (action) {
        case "/infoEmployee":
          ThongTinCaNhan(request, response);
          break;
        case "/updateInfoEmployee":
          UpdateThongTinCaNhan(request, response);
          break;
        default:
          break;
      }
    } catch (SQLException ex) {
      HandleException.printSQLException(ex);

    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doGet(request, response);
  }

  private void ThongTinCaNhan(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, IOException, ServletException, ParseException {
    HttpSession session = request.getSession();
    TaiKhoan login = new TaiKhoan();
    login = (TaiKhoan) session.getAttribute("user");

    if(login==null)
    {
      response.sendRedirect("views/system/login.jsp");
    }
    else
    {
      String MaTaiKhoan = login.getMaTaiKhoan();
      List<ThongTinNguoiDung> result = capNhatThongTinDAO.ThongTinCaNhan(MaTaiKhoan);
      List<ThongTinCongTac> result1 = capNhatThongTinDAO.ThongTinCaNhanCongTy(MaTaiKhoan);
      List<QuyetDinh> result2 = capNhatThongTinDAO.XemQuyetDinh(MaTaiKhoan);
      request.setAttribute("thongtincanhan", result);
      request.setAttribute("thongtincanhanCongTy", result1);
      request.setAttribute("quyetdinh", result2);

      if (login.getQuyen().equals("admin")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/quanli/QuanLiCapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("giamdoc")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/giamdoc/CapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("truongphong")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/truongphong/CapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("nhanvien")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/nhanvien/NhanVienCapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      }
    }
  }

  private void UpdateThongTinCaNhan(HttpServletRequest request, HttpServletResponse response)
      throws SQLException, IOException, ServletException, ParseException {
    HttpSession session = request.getSession();
    TaiKhoan login = new TaiKhoan();
    login = (TaiKhoan) session.getAttribute("user");

    if(login==null)
    {
      response.sendRedirect("views/system/login.jsp");
    }
    else
    {
      String MaTaiKhoan = login.getMaTaiKhoan();
      String HoTen = ConvertToUTF8(request.getParameter("HoTen"));
      String GioiTinh = ConvertToUTF8(request.getParameter("GioiTinh"));
      String CCCD = ConvertToUTF8(request.getParameter("CCCD"));
      String NgayCap = ConvertToUTF8(request.getParameter("NgayCap"));
      String NoiCap = ConvertToUTF8(request.getParameter("NoiCap"));
      String NgaySinh = ConvertToUTF8(request.getParameter("NgaySinh"));
      String SoDienThoai = ConvertToUTF8(request.getParameter("SoDienThoai"));
      String Email = ConvertToUTF8(request.getParameter("Email"));
      String SoNha = ConvertToUTF8(request.getParameter("SoNha"));
      String Xa = ConvertToUTF8(request.getParameter("Xa"));
      String Huyen = ConvertToUTF8(request.getParameter("Huyen"));
      String Tinh = ConvertToUTF8(request.getParameter("Tinh"));

      ThongTinNguoiDung thongTinNguoiDung = new ThongTinNguoiDung(MaTaiKhoan, HoTen, GioiTinh,
          CCCD, NgayCap, NoiCap, NgaySinh, SoDienThoai, Email, SoNha, Xa, Huyen, Tinh);
      if (capNhatThongTinDAO.UpdateThongTinCaNhan(thongTinNguoiDung)) {
        request.setAttribute("message", "Cập nhật thông tin thành công");
      } else {
        request.setAttribute("message", "Cập nhật thông tin thất bại");
      }
      List<ThongTinNguoiDung> result = capNhatThongTinDAO.ThongTinCaNhan(MaTaiKhoan);
      List<ThongTinCongTac> result1 = capNhatThongTinDAO.ThongTinCaNhanCongTy(MaTaiKhoan);
      request.setAttribute("thongtincanhan", result);
      request.setAttribute("thongtincanhanCongTy", result1);

      if (login.getQuyen().equals("admin")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/quanli/QuanLiCapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("giamdoc")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/giamdoc/CapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("truongphong")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/truongphong/CapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      } else if (login.getQuyen().equals("nhanvien")) {
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "views/nhanvien/NhanVienCapNhatThongTin.jsp");
        dispatcher.forward(request, response);
      }
    }
  }

  private String ConvertToUTF8(String item) {
    String result;
    byte[] bytes = item.getBytes(StandardCharsets.ISO_8859_1);
    result = new String(bytes, StandardCharsets.UTF_8);

    return result;
  }
}
