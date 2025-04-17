package DAO;

import JDBCUtils.HandleException;
import JDBCUtils.JDBCUtil;
import Models.ChiNhanh;
import Models.ThongTinNguoiDung;
import javax.swing.text.AsyncBoxView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuanLyChiNhanhDAO {
    public QuanLyChiNhanhDAO(){ }
    private static final String INSERT_CHINHANH = "INSERT INTO chinhanh" +
            "  (MaChiNhanh, TenChiNhanh, SoNha, Xa, Huyen, Tinh," +
            " NgayTaoChiNhanh, SoDienThoai,MaTongGiamDoc) VALUES " + " (?, ?, ?, ?, ?,?, ?, ?,?);";

    private static final String UPDATE_CHINHANH = "UPDATE chinhanh set TenChiNhanh = ?, SoNha= ?, Xa =?, " +
            "Huyen =?, Tinh = ?, NgayTaoChiNhanh=?, SoDienThoai=?, MaTongGiamDoc=? where MaChiNhanh = ?;";

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
    public static   List <ChiNhanh> loadInfomationchinhanh ()
    {


        List <ChiNhanh> result = new ArrayList < > ();
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement("SELECT * from chinhanh" );) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String MaChiNhanh = rs.getString("MaChiNhanh");
                String ten = rs.getString("TenChiNhanh");
                String sonha = rs.getString("SoNha");
                String xa = rs.getString("Xa");
                String huyen = rs.getString("Huyen");
                String tinh = rs.getString("Tinh");
                String ngaytaochinhanh = rs.getString("NgayTaoChiNhanh");
                String sdt = rs.getString("SoDienThoai");
                String matonggiamdoc = rs.getString("MaTongGiamDoc");
                result.add(new ChiNhanh(MaChiNhanh,ten,sonha,xa,huyen,tinh,ngaytaochinhanh,sdt,matonggiamdoc));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }

        return result;
    }
    public List <ChiNhanh> selectAllChiNhanh() {
        List <ChiNhanh> info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select * from chinhanh ");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String MaChiNhanh = rs.getString("MaChiNhanh");
                String ten = rs.getString("TenChiNhanh");
                String sonha = rs.getString("SoNha");
                String xa = rs.getString("Xa");
                String huyen = rs.getString("Huyen");
                String tinh = rs.getString("Tinh");
                String ngaytaochinhanh = rs.getString("NgayTaoChiNhanh");
                String sdt = rs.getString("SoDienThoai");
                String matonggiamdoc = rs.getString("MaTongGiamDoc");

                info.add(new ChiNhanh(MaChiNhanh,ten,sonha,xa,huyen,tinh,ngaytaochinhanh,sdt,matonggiamdoc));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }
    public boolean Addchinhanh(ChiNhanh emp)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CHINHANH);)
        {
            preparedStatement.setString(1,emp.getMaChiNhanh());
            preparedStatement.setString(2,emp.getTenChiNhanh());
            preparedStatement.setString(3,emp.getSoNha());
            preparedStatement.setString(4,emp.getXa());
            preparedStatement.setString(5,emp.getHuyen());
            preparedStatement.setString(6,emp.getTinh());
            preparedStatement.setString(7,emp.getNgayTaoChiNhanh());
            preparedStatement.setString(8,emp.getSdt());
            preparedStatement.setString(9,emp.getMaGiamDoc());

            preparedStatement.executeUpdate();
            return true;
        }
        catch(SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }

    public List <ChiNhanh> selecttenChiNhanh()
    {
        List < ChiNhanh > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("select  TenChiNhanh from chinhanh");) {
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
    public List < ChiNhanh > findAllChiNhanh( String tenCN) {
        List < ChiNhanh > info = new ArrayList < > ();

        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement("select * from chinhanh where  TenChiNhanh LIKE ?");) {
            preparedStatement.setString(1, "%" + tenCN + "%");

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String MaChiNhanh = rs.getString("MaChiNhanh");
                String ten = rs.getString("TenChiNhanh");
                String sonha = rs.getString("SoNha");
                String xa = rs.getString("Xa");
                String huyen = rs.getString("Huyen");
                String tinh = rs.getString("Tinh");
                String ngaytaochinhanh = rs.getString("NgayTaoChiNhanh");
                String sdt = rs.getString("SoDienThoai");
                String matonggiamdoc = rs.getString("MaTongGiamDoc");

                info.add(new ChiNhanh(MaChiNhanh,ten,sonha,xa,huyen,tinh,ngaytaochinhanh,sdt,matonggiamdoc));
            }
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return info;
    }
    public boolean Deletechinhanh(String machinhanh)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("delete from chinhanh  where MaChiNhanh=?");) {

            preparedStatement.setString(1,machinhanh);
            preparedStatement.executeUpdate();
            return true;
        }
        catch (SQLException e)
        {
            HandleException.printSQLException(e);
            return false;
        }
    }
    public boolean UpdateChinhanh(ChiNhanh emp)
    {
        try (Connection connection = JDBCUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CHINHANH);)
        {
            preparedStatement.setString(1,emp.getTenChiNhanh());
            preparedStatement.setString(2,emp.getSoNha());
            preparedStatement.setString(3,emp.getXa());
            preparedStatement.setString(4,emp.getHuyen());
            preparedStatement.setString(5,emp.getTinh());
            preparedStatement.setString(6,emp.getNgayTaoChiNhanh());
            preparedStatement.setString(7,emp.getSdt());
            preparedStatement.setString(8,emp.getMaGiamDoc());
            preparedStatement.setString(9,emp.getMaChiNhanh());

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
