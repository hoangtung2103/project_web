package DAO;

import JDBCUtils.HandleException;
import JDBCUtils.JDBCUtil;
import Models.CayChiNhanh;
import Models.CayNhom;
import Models.CayPhongBan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CauTrucCongTyDAO {
    private List<CayChiNhanh> chinhanhs = null;

    public CauTrucCongTyDAO() {
        this.chinhanhs = new ArrayList<>();
    }

    public List <CayChiNhanh> LoadStructure ()
    {
        try (Connection connection = JDBCUtil.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT distinct MaChiNhanh, TenChiNhanh FROM chinhanh;");) {

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String macn = rs.getString("MaChiNhanh");
                String tencn = rs.getString("TenChiNhanh");

                chinhanhs.add(new CayChiNhanh(macn,tencn));
            }
            LoadDepartment();
        } catch (SQLException e) {
            HandleException.printSQLException(e);
        }
        return chinhanhs;
    }

    private void LoadDepartment()
    {
        for (int i = 0; i < chinhanhs.size(); i++ )
        {
            List <CayPhongBan> phongbans = new ArrayList<>();

            try (Connection connection = JDBCUtil.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT distinct MaPB, TenPB FROM thongtinphongban WHERE MaChiNhanh = ? and MaPB is not null;");) {

                preparedStatement.setString(1,chinhanhs.get(i).getMaChiNhanh());
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    String mapb = rs.getString("MaPB");
                    String tenpb = rs.getString("TenPB");

                    phongbans.add(new CayPhongBan(mapb,tenpb));
                }
                phongbans = LoadTeam(i, phongbans);
            } catch (SQLException e) {
                HandleException.printSQLException(e);
            }
            chinhanhs.get(i).setPhongbans(phongbans);
        }
    }

    private List<CayPhongBan> LoadTeam (int indexChiNhanh, List<CayPhongBan> old)
    {
        List<CayPhongBan> newStruture = new ArrayList<>(old);
        for (int i = 0; i < old.size(); i++ )
        {
            List<CayNhom> nhoms = new ArrayList<>();
            newStruture.get(i).setMaPhongBan(old.get(i).getMaPhongBan());
            newStruture.get(i).setTenPhongBan(old.get(i).getTenPhongBan());

            try (Connection connection = JDBCUtil.getConnection();

                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "SELECT distinct MaNhom, TenNhom  FROM nhom  WHERE MaPB = ? and MaNhom is not null;");) {

                preparedStatement.setString(1,newStruture.get(i).getMaPhongBan());
                ResultSet rs = preparedStatement.executeQuery();

                while (rs.next()) {
                    String manhom = rs.getString("MaNhom");
                    String tennhom = rs.getString("TenNhom");

                    nhoms.add(new CayNhom(manhom, tennhom));
                }
            } catch (SQLException e) {
                HandleException.printSQLException(e);
            }
            newStruture.get(i).setNhoms(nhoms);
        }
        return newStruture;
    }
}
