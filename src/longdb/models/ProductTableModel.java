/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.models;

import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import longdb.dtos.TblProductDTO;

/**
 *
 * @author ACER
 */
public class ProductTableModel extends AbstractTableModel {

    String[] headers;
    int[] indexes;
    List<TblProductDTO> list = new Vector<>();

    public List<TblProductDTO> getList() {
        return list;
    }

    public ProductTableModel(String[] headers, int[] indexes) {
        this.headers = headers;
        this.indexes = indexes;
    }

    public void loadData(List<TblProductDTO> list) {
        if (list != null) {
            for (TblProductDTO dto : list) {
                this.list.add(dto);
            }
        }
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return indexes.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        TblProductDTO productDTO = this.list.get(rowIndex);
        switch (indexes[columnIndex]) {
            case 1:
                return productDTO.getProductID();
            case 2:
                return productDTO.getProductName();
            case 3:
                return productDTO.getUnit();
            case 4:
                return productDTO.getQuantity();
            case 5:
                return productDTO.getPrice();
            case 6:
                return productDTO.getCatagoryID();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        String columnName = headers[column];
        return columnName;
    }

}
