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
import longdb.dtos.TblProductDTO;
import longdb.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class TblProductDAO implements Serializable {

    public List<TblProductDTO> getAllItems() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblProductDTO> list = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select productID, productName, catagoryID, unit, price, quantity From TblProducts";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    String productID = rs.getString("productID");
                    String productName = rs.getString("productName");
                    String catagoryID = rs.getString("catagoryID");
                    String unit = rs.getString("unit");
                    float price = rs.getFloat("price");
                    int quantity = rs.getInt("quantity");
                    TblProductDTO dto = new TblProductDTO(productID, productName, catagoryID, unit, quantity, price);
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

    public String getCatagoryIDByProductID(String productID) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select catagoryID From TblProducts Where productID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, productID);
                rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getString("catagoryID");
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

    public boolean updateProduct(TblProductDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Update TblProducts Set productName = ?, unit = ?, price = ?, quantity = ? , catagoryID = ? Where productID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getProductName());
                ps.setString(2, dto.getUnit());
                ps.setFloat(3, dto.getPrice());
                ps.setInt(4, dto.getQuantity());
                ps.setString(5, dto.getCatagoryID());
                ps.setString(6, dto.getProductID());
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

    public boolean addProduct(TblProductDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into TblProducts (productID, productName, unit, price, quantity, catagoryID) Values (?,?,?,?,?,?)";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getProductID());
                ps.setString(2, dto.getProductName());
                ps.setString(3, dto.getUnit());
                ps.setFloat(4, dto.getPrice());
                ps.setInt(5, dto.getQuantity());
                ps.setString(6, dto.getCatagoryID());
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

    public boolean deleteProduct(TblProductDTO dto) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Delete From TblProducts Where productID = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, dto.getProductID());
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
