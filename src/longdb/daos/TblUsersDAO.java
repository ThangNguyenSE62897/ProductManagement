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
import longdb.dtos.TblUsersDTO;
import longdb.utils.DBHelpers;

/**
 *
 * @author ACER
 */
public class TblUsersDAO implements Serializable {

    public TblUsersDTO checkLogin(String username, String password) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select fullName, status From tblUsers Where userID = ? and password = ?";
                ps = con.prepareStatement(sql);
                ps.setString(1, username);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    boolean status = rs.getBoolean("status");
                    TblUsersDTO userDTO = new TblUsersDTO(username, password, fullName, status);
                    return userDTO;
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

    public List<TblUsersDTO> getStatus() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblUsersDTO> list = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select userID, password, fullname From tblUsers Where status = 1";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (list == null) {
                        list = new Vector<>();
                    }
                    String username = rs.getString("userID");
                    String password = rs.getString("password");
                    String fullName = rs.getString("fullName");
                    TblUsersDTO userDTO = new TblUsersDTO(username, password, fullName, true);

                    list.add(userDTO);
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

    public boolean checkStatus(TblUsersDTO dto) throws ClassNotFoundException, SQLException {
        List<TblUsersDTO> listAdmin = getStatus();

        for (TblUsersDTO userDTO : listAdmin) {
            if (dto.isStatus() && userDTO.isStatus()) {
                return true;
            }
        }
        return false;
    }

    public List<TblUsersDTO> getAllUsers() throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<TblUsersDTO> userList = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Select userID, password, fullName From tblUsers Where status = 0";
                ps = con.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (userList == null) {
                        userList = new Vector<>();
                    }
                    String username = rs.getString("userID");
                    String password = rs.getString("password");
                    String fullName = rs.getString("fullName");
                    TblUsersDTO userDTO = new TblUsersDTO(username, password, fullName, false);
                    userList.add(userDTO);
                }
                return userList;
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

    public boolean userRegistration(TblUsersDTO userDTO) throws ClassNotFoundException, SQLException {
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = DBHelpers.makeConnection();
            if (con != null) {
                String sql = "Insert Into tblUsers (userID, fullName, password, status) Values (?,?,?,0)";
                ps = con.prepareStatement(sql);
                ps.setString(1, userDTO.getUsername());
                ps.setString(2, userDTO.getFullName());
                ps.setString(3, userDTO.getPassword());
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
