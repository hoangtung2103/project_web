package Controller;

import DAO.QuanLyNhanVienDAO;
import DAO.ThongKeLuongDAO;
import JDBCUtils.HandleException;
import Models.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ThongKeLuongController", urlPatterns = {"/listSalary","/findSalary"})
public class ThongKeLuongController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ThongKeLuongDAO thongKeLuongDAO = null;
    private QuanLyNhanVienDAO quanLyNhanVienDAO = new QuanLyNhanVienDAO();

    public void init() {
        thongKeLuongDAO = new ThongKeLuongDAO();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getServletPath();
        try
        {
            switch (action)
            {
                case "/listSalary":
                    LoadSalary(request,response);
                    break;
                case "/findSalary":
                    FindListSalary(request,response);
                    break;
            }
        }
        catch (SQLException ex){
            HandleException.printSQLException(ex);

        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    private void LoadSalary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException
    {
        HttpSession session = request.getSession();
        TaiKhoan login = new TaiKhoan();
        login=(TaiKhoan)session.getAttribute("user");

        if(login == null)
            response.sendRedirect("views/system/login.jsp");

        else
        {
            String maChiNhanh = quanLyNhanVienDAO.LayMaChiNhanh(login.getMaTaiKhoan());
            String maPhongBan = quanLyNhanVienDAO.LayMaPhongBan(login.getMaTaiKhoan());

            if(login.getQuyen().equals("admin"))
            {
                List <Luong> listInfoSalary = thongKeLuongDAO.selectInfoSalary();
                List <ChiNhanh> listChiNhanh = quanLyNhanVienDAO.selectAllBranch();
                List <PhongBan> listPhongBan = quanLyNhanVienDAO.selectAllDepart();
                List <ChucVu> listChucVu = quanLyNhanVienDAO.selectAllTitle();

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("listChiNhanh",listChiNhanh);
                request.setAttribute("listPhongBan",listPhongBan);
                request.setAttribute("listChucVu",listChucVu);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/quanli/QuanLiThongKeLuong.jsp");
                dispatcher.forward(request,response);

            } else if (login.getQuyen().equals("giamdoc")) {
                List <Luong> listInfoSalary = thongKeLuongDAO.selectInfoSalaryForBranch(maChiNhanh);
                String tenChiNhanh = quanLyNhanVienDAO.LayTenChiNhanh(maChiNhanh);
                List < PhongBan > listDepartment = quanLyNhanVienDAO.selectAllDepart(maChiNhanh);
                List < ChucVu > listTitle = quanLyNhanVienDAO.selectAllTitle(maChiNhanh);

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("tenChiNhanh",tenChiNhanh);
                request.setAttribute("listPhongBan",listDepartment);
                request.setAttribute("listChucVu",listTitle);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/giamdoc/ThongKeTienLuong.jsp");
                dispatcher.forward(request,response);
            }
            else if (login.getQuyen().equals("truongphong"))
            {
                List <Luong> listInfoSalary = thongKeLuongDAO.selectInfoSalaryForDepart(maChiNhanh,maPhongBan);
                String tenChiNhanh = quanLyNhanVienDAO.LayTenChiNhanh(maChiNhanh);
                String tenPhongBan = quanLyNhanVienDAO.LayTenPhongBan(maPhongBan);
                List < ChucVu > listTitle = quanLyNhanVienDAO.selectAllTitleOfDepart(maChiNhanh,maPhongBan);

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("tenChiNhanh",tenChiNhanh);
                request.setAttribute("tenPhongBan",tenPhongBan);
                request.setAttribute("listTitle",listTitle);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/truongphong/ThongKeTienLuong.jsp");
                dispatcher.forward(request,response);
            }
        }
    }

    private void FindListSalary(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException, ServletException
    {
        HttpSession session = request.getSession();
        TaiKhoan login = new TaiKhoan();
        login=(TaiKhoan)session.getAttribute("user");

        if(login == null)
            response.sendRedirect("views/system/login.jsp");
        else {
            String tenCN = request.getParameter("tenCN");
            String tenPB = request.getParameter("tenPB");
            String tenCV = request.getParameter("tenCV");

            String maChiNhanh = quanLyNhanVienDAO.LayMaChiNhanh(login.getMaTaiKhoan());
            String maPhongBan = quanLyNhanVienDAO.LayMaPhongBan(login.getMaTaiKhoan());

            tenCN = ConvertToUTF8(tenCN);
            tenPB = ConvertToUTF8(tenPB);
            tenCV = ConvertToUTF8(tenCV);

            if(tenCN.equals("Chọn chi nhánh"))
                tenCN = "%";
            if(tenPB.equals("Chọn phòng ban"))
                tenPB = "%";
            if(tenCV.equals("Chọn chức vụ"))
                tenCV = "%";

            List <Luong> listInfoSalary = thongKeLuongDAO.findListSalary(tenCN, tenPB, tenCV);

            if(login.getQuyen().equals("admin"))
            {
                List <ChiNhanh> listChiNhanh = quanLyNhanVienDAO.selectAllBranch();
                List <PhongBan> listPhongBan = quanLyNhanVienDAO.selectAllDepart();
                List <ChucVu> listChucVu = quanLyNhanVienDAO.selectAllTitle();

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("listChiNhanh",listChiNhanh);
                request.setAttribute("listPhongBan",listPhongBan);
                request.setAttribute("listChucVu",listChucVu);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/quanli/QuanLiThongKeLuong.jsp");
                dispatcher.forward(request,response);
            }
            else if (login.getQuyen().equals("giamdoc"))
            {
                List < PhongBan > listDepartment = quanLyNhanVienDAO.selectAllDepart(maChiNhanh);
                List < ChucVu > listTitle = quanLyNhanVienDAO.selectAllTitle(maChiNhanh);

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("tenChiNhanh",tenCN);
                request.setAttribute("listPhongBan",listDepartment);
                request.setAttribute("listChucVu",listTitle);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/giamdoc/ThongKeTienLuong.jsp");
                dispatcher.forward(request,response);
            }
            else if (login.getQuyen().equals("truongphong"))
            {
                List < ChucVu > listTitle = quanLyNhanVienDAO.selectAllTitleOfDepart(maChiNhanh,maPhongBan);

                request.setAttribute("listInfoSalary",listInfoSalary);
                request.setAttribute("tenChiNhanh",tenCN);
                request.setAttribute("tenPhongBan",tenPB);
                request.setAttribute("listTitle",listTitle);

                RequestDispatcher dispatcher = request.getRequestDispatcher("views/truongphong/ThongKeTienLuong.jsp");
                dispatcher.forward(request,response);
            }
        }
    }

    private String ConvertToUTF8(String item)
    {
        String result;
        byte[] bytes = item.getBytes(StandardCharsets.ISO_8859_1);
        result = new String(bytes, StandardCharsets.UTF_8);

        return result;
    }
}
