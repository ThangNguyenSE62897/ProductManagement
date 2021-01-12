/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.validators;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import longdb.daos.TblCatagoryDAO;
import longdb.dtos.TblCatagoryDTO;

/**
 *
 * @author ACER
 */
public class CatagoryValidator implements Serializable {

    public boolean checkDupplicateCatagoryID(String catagoryID) throws ClassNotFoundException, SQLException {
        TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
        List<TblCatagoryDTO> supList = catagoryDAO.getAllCatagory();
        for (TblCatagoryDTO dto : supList) {
            if (dto.getCategoryID().equals(catagoryID)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkRegexCatagoryID(String catagoryID) {
        String pattern = "[a-zA-Z0-9]+{3,15}";
        boolean result = catagoryID.matches(pattern);
        if (result) {
            return true;
        }
        return false;
    }

    public boolean checkRegexcatagoryName(String catagoryName) {
        if (catagoryName.isEmpty() || catagoryName.length() < 3 || catagoryName.length() > 15) {
            return false;
        }
        return true;
    }

    public boolean checkRegexDescription(String description) {
        if (description.isEmpty() || description.length() < 3 || description.length() > 30) {
            return false;
        }
        return true;
    }
}
