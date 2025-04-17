package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Models.*;
import JDBCUtils.HandleException;
import JDBCUtils.JDBCUtil;

public class QuanLyPhongBanDAO {

  private static final String SELECT_PHONGBAN_ALL = "SELECT * FROM thongtinnguoidung tnd JOIN thongtincongtacnhanvien ct ON tnd.MaTaiKhoan = ct.MaTaiKhoan JOIN thongtinphongban tpb ON ct.MaPhongBan = tpb.MaPB JOIN chucvu cv ON ct.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ct.MaChiNhanh = cn.MaChiNhanh WHERE cv.TenChucVu = 'Trưởng phòng';";
  private static final String SELECT_PHONGBAN_NHANH = "SELECT * FROM thongtinnguoidung tnd JOIN thongtincongtacnhanvien ct ON tnd.MaTaiKhoan = ct.MaTaiKhoan JOIN thongtinphongban tpb ON ct.MaPhongBan = tpb.MaPB JOIN chucvu cv ON ct.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ct.MaChiNhanh = cn.MaChiNhanh WHERE ct.MaChiNhanh = ? AND cv.TenChucVu = 'Trưởng phòng';";
  private static final String CALL_INSERT_PHONGBAN = "CALL themPhongBan(?, ?, ?, ?, ?, ?);";
  private static final String CALL_UPDATE_PHONGBAN = "{CALL suaPhongBan(?, ?, ?, ?, ?, ?)}";
  private static final String CALL_DELETE_PHONGBAN = "CALL xoaPhongBan(?);";
  public QuanLyPhongBanDAO() {
  }

  public List<ThongTinTruongPhong> selectAllPhongBan(String maChiNhanh, String maPhongBan,
      String role) {
    List<ThongTinTruongPhong> result = new ArrayList<>();
    String query = "";
    if (role.equals("admin")) {
      query = SELECT_PHONGBAN_ALL;
    } else if (role.equals("giamdoc")) {
      query = SELECT_PHONGBAN_NHANH;
    }
    try (Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);) {
      if (role.equals("giamdoc")) {
        preparedStatement.setString(1, maChiNhanh);
      }
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaChiNhanh = rs.getString("MaChiNhanh");
        String TenChiNhanh = rs.getString("TenChiNhanh");
        String MaPhongBan = rs.getString("MaPB");
        String TenPhongBan = rs.getString("TenPB");
        String NgayTao = rs.getString("Ngaytao");
        String Sdt = rs.getString("SDT");
        String Machucvu = rs.getString("MaChucVu");
        String MaTruongPhong = rs.getString("MaTaiKhoan");
        String TenTruongPhong = rs.getString("HoTen");

        ThongTinTruongPhong thongTinTruongPhong = new ThongTinTruongPhong(MaChiNhanh, TenChiNhanh,
            MaPhongBan, TenPhongBan, NgayTao, Sdt, Machucvu, MaTruongPhong, TenTruongPhong);
        result.add(thongTinTruongPhong);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }

  public List<ThongTinTruongPhong> findDepartment(String tenChiNhanh, String tenPhongBan)
  {
    List<ThongTinTruongPhong> result = new ArrayList<>();
    try (Connection connection = JDBCUtil.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM thongtinphongban tpb JOIN chinhanh cn ON tpb.MaChiNhanh = cn.MaChiNhanh JOIN thongtincongtacnhanvien ttc ON tpb.MaPB = ttc.MaPhongBan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN thongtinnguoidung tnd ON ttc.MaTaiKhoan = tnd.MaTaiKhoan WHERE cn.TenChiNhanh LIKE ? and  tpb.TenPB LIKE ? and cv.TenChucVu = 'Trưởng phòng';");) {

      preparedStatement.setString(1,tenChiNhanh);
      preparedStatement.setString(2,tenPhongBan);

      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaChiNhanh = rs.getString("MaChiNhanh");
        String TenChiNhanh = rs.getString("TenChiNhanh");
        String MaPhongBan = rs.getString("MaPB");
        String TenPhongBan = rs.getString("TenPB");
        String NgayTao = rs.getString("Ngaytao");
        String Sdt = rs.getString("SDT");
        String Machucvu = rs.getString("MaChucVu");
        String MaTruongPhong = rs.getString("MaTruongPhong");
        String TenTruongPhong = rs.getString("HoTen");

        ThongTinTruongPhong thongTinTruongPhong = new ThongTinTruongPhong(MaChiNhanh, TenChiNhanh,
                MaPhongBan, TenPhongBan, NgayTao, Sdt, Machucvu, MaTruongPhong, TenTruongPhong);
        result.add(thongTinTruongPhong);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }

  public List<ThongTinTruongPhong> LoadInfoPhongBan() {
    List<ThongTinTruongPhong> result = new ArrayList<>();
    try (Connection connection = JDBCUtil.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT * FROM thongtinphongban tpb JOIN chinhanh cn ON tpb.MaChiNhanh = cn.MaChiNhanh JOIN thongtinnguoidung tnd ON tpb.MaTruongPhong = tnd.MaTaiKhoan JOIN thongtincongtacnhanvien ttc ON tnd.MaTaiKhoan = ttc.MaTaiKhoan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu where cv.TenChucVu = 'Trưởng phòng';");) {
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaChiNhanh = rs.getString("MaChiNhanh");
        String TenChiNhanh = rs.getString("TenChiNhanh");
        String MaPhongBan = rs.getString("MaPB");
        String TenPhongBan = rs.getString("TenPB");
        String NgayTao = rs.getString("NgayTao");
        String Sdt = rs.getString("SDT");
        String Machucvu = rs.getString("MaChucVu");
        String MaTruongPhong = rs.getString("MaTaiKhoan");
        String TenTruongPhong = rs.getString("HoTen");

        ThongTinTruongPhong thongTinTruongPhong = new ThongTinTruongPhong(MaChiNhanh, TenChiNhanh,
            MaPhongBan, TenPhongBan, NgayTao, Sdt, Machucvu, MaTruongPhong, TenTruongPhong);
        result.add(thongTinTruongPhong);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }

  public String LayMaChiNhanh(String maTaiKhoan) {
    String result = "null";
    try (Connection connection = JDBCUtil.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(
            "select MaChiNhanh from thongtincongtacnhanvien inner join chucvu on thongtincongtacnhanvien.MaChucVu=chucvu.MaChucVu where MaTaiKhoan = ?");) {
      preparedStatement.setString(1, maTaiKhoan);

      ResultSet rs = preparedStatement.executeQuery();

      while (rs.next()) {
        result = rs.getString("MaChiNhanh");
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }

  public String LayMaPhongBan(String mataikhoan) {
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

  public String LayTenChiNhanh(String maChiNhanh) {
    String result = "";
    try (Connection connection = JDBCUtil.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement(
            "SELECT DISTINCT cn.TenChiNhanh FROM thongtincongtacnhanvien ttc JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh WHERE cn.MaChiNhanh = ?;");) {
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

  public String LayTenPhongBan(String maPhongBan) {
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

  public boolean AddPhongBan(String maChiNhanh, String maPB, String tenPB, String ngayTao,
      String sdt, String maChucVu, String tenChucVu, int luongCoBan, String maTruongPhong,
      String ngayBatDau) {
    try (Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CALL_INSERT_PHONGBAN);) {
      preparedStatement.setString(1, maPB);
      preparedStatement.setString(2, tenPB);
      preparedStatement.setString(3, maChiNhanh);
      preparedStatement.setString(4, ngayTao);
      preparedStatement.setString(5, sdt);
      preparedStatement.setString(6, maTruongPhong);

      preparedStatement.executeUpdate();
      return true;
    } catch (SQLException e) {
      HandleException.printSQLException(e);
      return false;
    }
  }
  public boolean SuaPhongBan(String maChiNhanh, String maPB, String tenPB, String ngayTao,
                             String sdt, String maChucVu, int luongCoBan, String maTruongPhong, String ngayBatDau) {
    try (Connection connection = JDBCUtil.getConnection();
         CallableStatement callableStatement = connection.prepareCall(CALL_UPDATE_PHONGBAN)) {
      callableStatement.setString(1, maPB);
      callableStatement.setString(2, tenPB);
      callableStatement.setString(3, maChiNhanh);
      callableStatement.setString(4, ngayTao);
      callableStatement.setString(5, sdt);
      callableStatement.setString(6, maTruongPhong);

      return callableStatement.executeUpdate() > 0;
    } catch (SQLException e) {
      HandleException.printSQLException(e);
      return false;
    }
  }

  public boolean XoaPhongBan(String maPhongBan) {
    try (Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(CALL_DELETE_PHONGBAN);) {
      preparedStatement.setString(1, maPhongBan);

      if (preparedStatement.executeUpdate() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
      return false;
    }
  }

}
