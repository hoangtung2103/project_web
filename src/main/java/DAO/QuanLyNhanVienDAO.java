package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.*;
import JDBCUtils.HandleException;
import JDBCUtils.JDBCUtil;

public class QuanLyNhanVienDAO {
    private static final String INSERT_EMPLOYEE = "INSERT INTO thongtinnguoidung" +
            "  (MaTaiKhoan, HoTen, GioiTinh, CCCD,  NgayCap," +
            " NoiCap, NgaySinh, SoDienThoai, Email, SoNha, Xa, Huyen, Tinh," +
            " HeSoLuong, TrangThai, TrinhDo, NgayBatDauLam) VALUES " + " (?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?, ?,?,?);";

    private static final String UPDATE_EMPLOYEE = "UPDATE thongtinnguoidung set HoTen = ?, GioiTinh= ?, CCCD =?, " +
            "NgayCap =?, NoiCap = ?, NgaySinh=?, SoDienThoai=?, Email=?, SoNha=?, Xa=?, Huyen=?, Tinh=?," +
            "HeSoLuong=?, TrangThai=?, TrinhDo=?, NgayBatDauLam=? where MaTaiKhoan = ?;";

    private static final String INSERT_REWARD = "INSERT INTO quyetdinh" +
            "  (MaQuyetDinh, LoaiQuyetDinh, Ngay, NoiDung,  MaNhanVien, MaNguoiQuyetDinh)" +
            " VALUES " + " (?, ?, ?, ?, ?, ?);";

    private static final String SELECT_EMPLOYEE = "SELECT * FROM thongtinnguoidung tnd JOIN thongtincongtacnhanvien ttc ON tnd.MaTaiKhoan = ttc.MaTaiKhoan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB;";
    private static final String SELECT_EMPLOYEE_BRANCH = "SELECT * FROM thongtinnguoidung tnd JOIN thongtincongtacnhanvien ttc ON tnd.MaTaiKhoan = ttc.MaTaiKhoan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB where cn.MaChiNhanh = ?";
    private static final String SELECT_EMPLOYEE_DEPART = "SELECT * FROM thongtinnguoidung tnd JOIN thongtincongtacnhanvien ttc ON tnd.MaTaiKhoan = ttc.MaTaiKhoan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB where cn.MaChiNhanh = ? and tpb.MaPB = ?";

    public QuanLyNhanVienDAO(){ }

    public List < ThongTinCongTac > selectAllUsers(String maChiNhanh, String maPhongBan, String role) {
        List < ThongTinCongTac > info = new ArrayList < > ();
        String query = "";

        if (role.equals("admin"))
            query = SELECT_EMPLOYEE;
        else if (role.equals("giamdoc")) {
            query = SELECT_EMPLOYEE_BRANCH;
        } else if (role.equals("truongphong")) {
            query = SELECT_EMPLOYEE_DEPART;
        }
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query);) {

            if (role.equals("giamdoc")) {
                preparedStatement.setString(1, maChiNhanh);
            } else if (role.equals("truongphong")) {
                preparedStatement.setString(1, maChiNhanh);
                preparedStatement.setString(2, maPhongBan);
            }
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String maNV = rs.getString("MaTaiKhoan");
                String ten = rs.getString("HoTen");
                String gioitinh = rs.getString("GioiTinh");
                Date ngaysinh = rs.getDate("NgaySinh");
                String chucvu = rs.getString("TenChucVu");
                String machinhanh = rs.getString("MaChiNhanh");
                String chinhanh = rs.getString("TenChiNhanh");
                String maPB = rs.getString("MaPhongBan");
                String phongban = rs.getString("TenPB");

                info.add(new ThongTinCongTac(maNV,ten,gioitinh,ngaysinh,chucvu,machinhanh,chinhanh,maPB,phongban));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public String LayMaChiNhanh(String mataikhoan)
    {
        String result = "null";
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select MaChiNhanh from thongtincongtacnhanvien inner join chucvu on thongtincongtacnhanvien.MaChucVu=chucvu.MaChucVu where MaTaiKhoan = ?");) {
            preparedStatement.setString(1, mataikhoan);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result = rs.getString("MaChiNhanh");
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return result;
    }

    public String LayMaPhongBan(String mataikhoan)
    {
        String result = "null";
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select MaPhongBan from thongtincongtacnhanvien inner join chucvu on thongtincongtacnhanvien.MaChucVu=chucvu.MaChucVu where MaTaiKhoan = ?");) {
            preparedStatement.setString(1, mataikhoan);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result = rs.getString("MaPhongBan");
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return result;
    }

    public String LayTenChiNhanh(String maChiNhanh)
    {
        String result = "";
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT cn.TenChiNhanh FROM thongtincongtacnhanvien ttc JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh WHERE ttc.MaChiNhanh = ?;");) {
            preparedStatement.setString(1, maChiNhanh);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result = rs.getString("TenChiNhanh");
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return result;
    }

    public String LayTenPhongBan(String maPhongBan)
    {
        String result = "";
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "select TenPB from thongtinphongban where MaPB = ?");) {
            preparedStatement.setString(1, maPhongBan);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                result = rs.getString("TenPB");
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return result;
    }

    public List <PhongBan> selectAllDepart(String maChiNhanh)
    {
        List < PhongBan > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT tpb.TenPB FROM thongtincongtacnhanvien ttc JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB WHERE ttc.MaChiNhanh = ? AND tpb.TenPB IS NOT NULL;");) {
            preparedStatement.setString(1, maChiNhanh);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenPB = rs.getString("TenPB");

                info.add(new PhongBan(tenPB));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <PhongBan> selectAllDepart()
    {
        List < PhongBan > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct tpb.TenPB FROM thongtincongtacnhanvien ttc JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB where TenPB is not null;");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenPB = rs.getString("TenPB");

                info.add(new PhongBan(tenPB));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <ChucVu> selectAllTitle(String maChiNhanh)
    {
        List < ChucVu > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT DISTINCT cv.TenChucVu FROM thongtincongtacnhanvien ttc JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu WHERE ttc.MaChiNhanh = ? AND cv.TenChucVu IS NOT NULL;");) {
            preparedStatement.setString(1, maChiNhanh);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenCV = rs.getString("TenChucVu");

                info.add(new ChucVu(tenCV));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <ChucVu> selectAllTitle()
    {
        List < ChucVu > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct cv.TenChucVu FROM thongtincongtacnhanvien ttc JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu WHERE cv.TenChucVu IS NOT NULL;");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenCV = rs.getString("TenChucVu");

                info.add(new ChucVu(tenCV));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <ChiNhanh> selectAllBranch()
    {
        List < ChiNhanh > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT TenChiNhanh FROM chinhanh;");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenCN = rs.getString("TenChiNhanh");

                info.add(new ChiNhanh(tenCN));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <ChucVu> selectAllTitleOfDepart(String maChiNhanh, String maPhongBan)
    {
        List < ChucVu > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct cv.TenChucVu FROM thongtincongtacnhanvien ttc JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu where ttc.MaChiNhanh = ? and ttc.MaPhongBan = ? and cv.TenChucVu is not null");) {
            preparedStatement.setString(1, maChiNhanh);
            preparedStatement.setString(2, maPhongBan);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String tenCV = rs.getString("TenChucVu");

                info.add(new ChucVu(tenCV));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List < ThongTinCongTac > findAllEmployee(String tenCN, String tenPB, String tenCV) {
        List < ThongTinCongTac > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM thongtincongtacnhanvien ttc JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB JOIN thongtinnguoidung tnd ON ttc.MaTaiKhoan = tnd.MaTaiKhoan WHERE cn.TenChiNhanh LIKE ? AND tpb.TenPB LIKE ? AND cv.TenChucVu LIKE ?;");) {
            preparedStatement.setString(1, tenCN);
            preparedStatement.setString(2, tenPB);
            preparedStatement.setString(3, tenCV);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String maNV = rs.getString("MaTaiKhoan");
                String ten = rs.getString("HoTen");
                String gioitinh = rs.getString("GioiTinh");
                Date ngaysinh = rs.getDate("NgaySinh");
                String chucvu = rs.getString("TenChucVu");
                String machinhanh = rs.getString("MaChiNhanh");
                String chinhanh = rs.getString("TenChiNhanh");
                String maPB = rs.getString("MaPB");
                String phongban = rs.getString("TenPB");

                info.add(new ThongTinCongTac(maNV,ten,gioitinh,ngaysinh,chucvu,machinhanh,chinhanh,maPB,phongban));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }

    public List <ThongTinNguoiDung> loadInfomation ()
    {
        List <ThongTinNguoiDung> result = new ArrayList < > ();
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM thongtinnguoidung");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String maNV = rs.getString("MaTaiKhoan");
                String ten = rs.getString("HoTen");
                String gioitinh = rs.getString("GioiTinh");
                String cccd = rs.getString("CCCD");
                String ngaycap = rs.getString("NgayCap");
                String noicap = rs.getString("NoiCap");
                String ngaysinh = rs.getString("NgaySinh");
                String sdt = rs.getString("SoDienThoai");
                String email = rs.getString("Email");
                String sonha = rs.getString("SoNha");
                String xa = rs.getString("Xa");
                String huyen = rs.getString("Huyen");
                String tinh = rs.getString("Tinh");
                float heso = rs.getFloat("HeSoLuong");
                String trangthai = rs.getString("TrangThai");
                String trinhdo = rs.getString("TrinhDo");
                String ngaylam = rs.getString("NgayBatDauLam");


                result.add(new ThongTinNguoiDung(maNV,ten,gioitinh,cccd,ngaycap,noicap,ngaysinh,sdt,email,sonha,xa,huyen,tinh,heso,trangthai,trinhdo,ngaylam));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }

        return result;
    }

    public boolean AddEmployee(ThongTinNguoiDung emp)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE);)
        {
            preparedStatement.setString(1,emp.getMataikhoan());

            preparedStatement.setString(2,emp.getHoTen());
            preparedStatement.setString(3,emp.getGioitinh());
            preparedStatement.setString(4,emp.getCccd());
            preparedStatement.setString(5,emp.getNgayCap());
            preparedStatement.setString(6,emp.getNoiCap());
            preparedStatement.setString(7,emp.getNgaySinh());
            preparedStatement.setString(8,emp.getSdt());
            preparedStatement.setString(9,emp.getEmail());
            preparedStatement.setString(10,emp.getSoNha());
            preparedStatement.setString(11,emp.getXa());
            preparedStatement.setString(12,emp.getHuyen());
            preparedStatement.setString(13,emp.getTinh());
            preparedStatement.setFloat(14,emp.getHeSoLuong());
            preparedStatement.setString(15,emp.getTrangThai());
            preparedStatement.setString(16,emp.getTrinhDo());
            preparedStatement.setString(17,emp.getNgayBatDauLam());

            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }

    public boolean DeleteEmployee(String mataikhoan)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("Update thongtinnguoidung set TrangThai='Đã nghỉ' where MaTaiKhoan=?");) {

            preparedStatement.setString(1,mataikhoan);
            preparedStatement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }
    public boolean UpdateEmployee(ThongTinNguoiDung emp)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE);)
        {
            preparedStatement.setString(1,emp.getHoTen());
            preparedStatement.setString(2,emp.getGioitinh());
            preparedStatement.setString(3,emp.getCccd());
            preparedStatement.setString(4,emp.getNgayCap());
            preparedStatement.setString(5,emp.getNoiCap());
            preparedStatement.setString(6,emp.getNgaySinh());
            preparedStatement.setString(7,emp.getSdt());
            preparedStatement.setString(8,emp.getEmail());
            preparedStatement.setString(9,emp.getSoNha());
            preparedStatement.setString(10,emp.getXa());
            preparedStatement.setString(11,emp.getHuyen());
            preparedStatement.setString(12,emp.getTinh());
            preparedStatement.setFloat(13,emp.getHeSoLuong());
            preparedStatement.setString(14,emp.getTrangThai());
            preparedStatement.setString(15,emp.getTrinhDo());
            preparedStatement.setString(16,emp.getNgayBatDauLam());
            preparedStatement.setString(17,emp.getMataikhoan());

            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }

    public boolean AddReward(QuyetDinh quyetDinh)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_REWARD);)
        {
            preparedStatement.setString(1, quyetDinh.getMaQuyetDinh());
            preparedStatement.setString(2, quyetDinh.getLoaiQuyetDinh());
            preparedStatement.setString(3, quyetDinh.getNgay());
            preparedStatement.setString(4, quyetDinh.getNoiDung());
            preparedStatement.setString(5, quyetDinh.getMaNhanVien());
            preparedStatement.setString(6, quyetDinh.getMaNguoiQuyetDinh());

            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }
}
