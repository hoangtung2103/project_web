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

public class CapNhatThongTinDAO {

  private static final String SELECT_EMPLOYEE_INFO = "SELECT * FROM thongtinnguoidung WHERE MaTaiKhoan = ?;";
  private static final String UPDATE_EMPLOYEE_INFO = "UPDATE thongtinnguoidung SET HoTen = ?, GioiTinh = ?, CCCD = ?, NgayCap = ?, NoiCap = ?, NgaySinh = ?, SoDienThoai = ?, Email = ?, SoNha = ?, Xa = ?, Huyen = ?, Tinh = ? WHERE MaTaiKhoan = ?;";
  private static final String SECLECT_EMPLOYEE_INFO_WORK = "SELECT * FROM thongtincongtacnhanvien ttc JOIN thongtinnguoidung tnd ON ttc.MaTaiKhoan = tnd.MaTaiKhoan JOIN chucvu cv ON ttc.MaChucVu = cv.MaChucVu JOIN chinhanh cn ON ttc.MaChiNhanh = cn.MaChiNhanh JOIN thongtinphongban tpb ON ttc.MaPhongBan = tpb.MaPB WHERE ttc.MaTaiKhoan = ?;";
  private static final String SELECT_EMPLOYEE_QUYETDINH = "SELECT * FROM quyetdinh WHERE MaNhanVien = ?;";

  public CapNhatThongTinDAO() {
  }

  public List<ThongTinNguoiDung> ThongTinCaNhan(String MaTaiKhoan) {
    List<ThongTinNguoiDung> result = new ArrayList<ThongTinNguoiDung>();
    try (Connection conn = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = conn.prepareStatement(SELECT_EMPLOYEE_INFO);) {
      preparedStatement.setString(1, MaTaiKhoan);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaNhanVien = rs.getString("MaTaiKhoan");
        String HoTen = rs.getString("HoTen");
        String GioiTinh = rs.getString("GioiTinh");
        String CCCD = rs.getString("CCCD");
        String NgayCap = rs.getString("NgayCap");
        String NoiCap = rs.getString("NoiCap");
        String NgaySinh = rs.getString("NgaySinh");
        String SoDienThoai = rs.getString("SoDienThoai");
        String Email = rs.getString("Email");
        String SoNha = rs.getString("SoNha");
        String Xa = rs.getString("Xa");
        String Huyen = rs.getString("Huyen");
        String Tinh = rs.getString("Tinh");
        float HeSoLuong = rs.getFloat("HeSoLuong");
        String TrangThai = rs.getString("TrangThai");
        String TrinhDo = rs.getString("TrinhDo");
        String NgayBatDauLam = rs.getString("NgayBatDauLam");
        ThongTinNguoiDung thongTinNguoiDung = new ThongTinNguoiDung(MaNhanVien, HoTen, GioiTinh,
            CCCD, NgayCap, NoiCap, NgaySinh, SoDienThoai, Email, SoNha, Xa, Huyen, Tinh, HeSoLuong,
            TrangThai, TrinhDo, NgayBatDauLam);
        result.add(thongTinNguoiDung);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }

  public List<ThongTinCongTac> ThongTinCaNhanCongTy(String MaTaiKhoan) {
    List<ThongTinCongTac> result = new ArrayList<ThongTinCongTac>();
    try (Connection conn = JDBCUtil.getConnection()
        ; PreparedStatement preparedStatement = conn.prepareStatement(
        SECLECT_EMPLOYEE_INFO_WORK);) {
      preparedStatement.setString(1, MaTaiKhoan);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaNhanVien = rs.getString("MaTaiKhoan");
        String TenNhanVien = rs.getString("HoTen");
        String GioiTinh = rs.getString("GioiTinh");
        Date NgaySinh = rs.getDate("NgaySinh");
        String ChucVu = rs.getString("TenChucVu");
        String MaChiNhanh = rs.getString("MaChiNhanh");
        String ChiNhanh = rs.getString("TenChiNhanh");
        String MaPhongBan = rs.getString("MaPhongBan");
        String PhongBan = rs.getString("TenPB");
        String NgayBatDau = rs.getString("NgayBD");
        ThongTinCongTac thongTinCongTac = new ThongTinCongTac(MaNhanVien, TenNhanVien, GioiTinh,
            NgaySinh, ChucVu, MaChiNhanh, ChiNhanh, MaPhongBan, PhongBan, NgayBatDau);
        result.add(thongTinCongTac);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }
  public List<QuyetDinh> XemQuyetDinh(String MaTaiKhoan)
  {
    List<QuyetDinh> result = new ArrayList<QuyetDinh>();
    try (Connection conn = JDBCUtil.getConnection()
        ; PreparedStatement preparedStatement = conn.prepareStatement(
        SELECT_EMPLOYEE_QUYETDINH);) {
      preparedStatement.setString(1, MaTaiKhoan);
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        String MaQuyetDinh = rs.getString("MaQuyetDinh");
        String LoaiQuyetDinh = rs.getString("LoaiQuyetDinh");
        String Ngay = rs.getString("Ngay");
        String NoiDung = rs.getString("NoiDung");
        String MaNhanVien = rs.getString("MaNhanVien");
        String MaNguoiQuyetDinh = rs.getString("MaNguoiQuyetDinh");
        QuyetDinh quyetDinh = new QuyetDinh(MaQuyetDinh, LoaiQuyetDinh, Ngay, NoiDung, MaNhanVien, MaNguoiQuyetDinh);
        result.add(quyetDinh);
      }
    } catch (SQLException e) {
      HandleException.printSQLException(e);
    }
    return result;
  }
  public boolean UpdateThongTinCaNhan(ThongTinNguoiDung thongTinNguoiDung) throws SQLException {
    boolean rowUpdated;
    try (Connection conn = JDBCUtil.getConnection();
        PreparedStatement statement = conn.prepareStatement(UPDATE_EMPLOYEE_INFO);) {
      statement.setString(1, thongTinNguoiDung.getHoTen());
      statement.setString(2, thongTinNguoiDung.getGioitinh());
      statement.setString(3, thongTinNguoiDung.getCccd());
      statement.setString(4, thongTinNguoiDung.getNgayCap());
      statement.setString(5, thongTinNguoiDung.getNoiCap());
      statement.setString(6, thongTinNguoiDung.getNgaySinh());
      statement.setString(7, thongTinNguoiDung.getSdt());
      statement.setString(8, thongTinNguoiDung.getEmail());
      statement.setString(9, thongTinNguoiDung.getSoNha());
      statement.setString(10, thongTinNguoiDung.getXa());
      statement.setString(11, thongTinNguoiDung.getHuyen());
      statement.setString(12, thongTinNguoiDung.getTinh());
      statement.setString(13, thongTinNguoiDung.getMataikhoan());
      rowUpdated = statement.executeUpdate() > 0;
    }
    return rowUpdated;
  }

  //  Test
  public static void main(String[] args) {
    CapNhatThongTinDAO capNhatThongTinDAO = new CapNhatThongTinDAO();
    List<ThongTinCongTac> result = capNhatThongTinDAO.ThongTinCaNhanCongTy("TK022");
    for (ThongTinCongTac thongTinCongTac : result) {
      System.out.println(thongTinCongTac.getManhanvien());
      System.out.println(thongTinCongTac.getTennhanvien());
      System.out.println(thongTinCongTac.getGioitinh());
      System.out.println(thongTinCongTac.getNgaysinh());
      System.out.println(thongTinCongTac.getChucvu());
      System.out.println(thongTinCongTac.getChinhanh());
      System.out.println(thongTinCongTac.getPhongban());
      System.out.println(thongTinCongTac.getDate());
    }
  }
}
