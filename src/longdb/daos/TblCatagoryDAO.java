/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.daos;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import longdb.dtos.TblCatagoryDTO;
import longdb.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class TblCatagoryDAO implements Serializable {

    public List<TblCatagoryDTO> getAllCatagory() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblCatagoryDTO> list = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select categoryID, categoryName, description From TblCategories";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String categoryID = rs.getString("categoryID");
                    String categoryName = rs.getString("categoryName");
                    String description = rs.getString("description");

                    TblCatagoryDTO dto = new TblCatagoryDTO(categoryID, categoryName, description);
                    if (list == null) {
                        list = new Vector<>();
                    }
                    list.add(dto);
                }
                return list;
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public TblCatagoryDTO getCatagoryDTOByCatagoryID(String categoryID) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select categoryName, description From TblCategories Where categoryID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, categoryID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String categoryName = rs.getString("categoryName");
                    String description = rs.getString("description");
                    TblCatagoryDTO dto = new TblCatagoryDTO(categoryID, categoryName, description);
                    return dto;
                }

            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return null;
    }

    public int getIndexCatagoryDTO(TblCatagoryDTO dto) throws ClassNotFoundException, SQLException {
        List<TblCatagoryDTO> list = getAllCatagory();
        if (list != null) {
            for (TblCatagoryDTO dtoSup : list) {
                if (dto.getCategoryID().equals(dtoSup.getCategoryID())) {
                    return list.indexOf(dtoSup);
                }
            }
        }
        return -1;
    }

    public boolean addCatagory(TblCatagoryDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into TblCategories (categoryID, categoryName, description) Values (?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getCategoryID());
                ps.setString(2, dto.getCategoryName());
                ps.setString(3, dto.getDescription());
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {

            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean updateCatagory(TblCatagoryDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Update TblCategories Set categoryName =?, description =? Where categoryID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getCategoryName());
                ps.setString(2, dto.getDescription());
                ps.setString(4, dto.getCategoryID());
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

    public boolean deleteCatagory(TblCatagoryDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Delete From TblCategories Where categoryID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getCategoryID());
                int row = ps.executeUpdate();
                if (row > 0) {
                    return true;
                }
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return false;
    }

}
