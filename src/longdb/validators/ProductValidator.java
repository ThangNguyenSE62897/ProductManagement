/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.validators;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import longdb.daos.TblProductDAO;
import longdb.dtos.TblProductDTO;

/**
 *
 * @author ACER
 */
public class ProductValidator implements Serializable {

    public boolean checkDupplicateProductID(String productID) throws ClassNotFoundException, SQLException {
        TblProductDAO productDAO = new TblProductDAO();
        List<TblProductDTO> listProduct = productDAO.getAllItems();
        for (TblProductDTO dto : listProduct) {
            if (dto.getProductID().equals(productID)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkRegexProductID(String productID) {
        String pattern = "[a-zA-Z0-9]+{3,15}";
        if (!productID.matches(pattern)) {
            return false;
        }
        return true;
    }

    public boolean checkRegexProductName(String productName) {
        if (productName.isEmpty() || productName.length() < 3 || productName.length() > 15) {
            return false;
        }
        return true;
    }

    public boolean checkRegexUnit(String unit) {
        if (unit.isEmpty() || unit.length() > 10) {
            return false;
        }
        return true;
    }

    public boolean checkRegexPrice(String priceString) {
        String priceStringModified = priceString.trim();
        if (priceStringModified.isEmpty() || priceStringModified.length() > 5) {
            return false;
        }
        float price = Float.parseFloat(priceStringModified);
        if (price <= 0) {
            return false;
        }
        return true;
    }

    public boolean checkRegeQuatity(String quantityString) {
        String quantityStringModified = quantityString.trim();
        if (quantityStringModified.isEmpty() || quantityStringModified.length() > 5) {
            return false;
        }
        float price = Integer.parseInt(quantityStringModified);
        if (price <= 0) {
            return false;
        }
        return true;
    }

}
