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
public class TblProductDTO implements Serializable {

    private String productID;
    private String productName;
    private String catagoryID;
    private String unit;
    private float price;
    private int quantity;

    public TblProductDTO() {
    }

    public TblProductDTO(String productID, String productName, String catagoryID, String unit, int quantity, float price) {
        this.productID = productID;
        this.productName = productName;
        this.catagoryID = catagoryID;
        this.unit = unit;
        this.quantity = quantity;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getCatagoryID() {
        return catagoryID;
    }

    public String getUnit() {
        return unit;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setCatagoryID(String catagoryID) {
        this.catagoryID = catagoryID;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
