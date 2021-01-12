/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.dtos;

import java.io.Serializable;

/**
 *
 * @author ACER
 */
public class TblCatagoryDTO implements Serializable {

    private String categoryID;
    private String categoryName;
    private String description;

    public TblCatagoryDTO() {
    }

    public TblCatagoryDTO(String categoryID, String categoryName, String description) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
        this.description = description;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return categoryID + "-" + categoryName;
    }

}
