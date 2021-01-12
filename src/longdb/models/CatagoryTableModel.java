/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.models;

import java.util.List;
import java.util.Vector;
import longdb.dtos.TblCatagoryDTO;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author ACER
 */
public class CatagoryTableModel extends AbstractTableModel {

    String headers[];
    int indexes[];
    List<TblCatagoryDTO> list = new Vector<>();

    public List<TblCatagoryDTO> getList() {
        return list;
    }

    public CatagoryTableModel() {
    }

    public CatagoryTableModel(String[] headers, int[] indexes) {
        this.headers = headers;
        this.indexes = indexes;
    }

    public void loadData(List<TblCatagoryDTO> list) {
        if (list != null) {
            for (TblCatagoryDTO dto : list) {
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
        TblCatagoryDTO dto = this.list.get(rowIndex);
        switch (indexes[columnIndex]) {
            case 1:
                return dto.getCategoryID();
            case 2:
                return dto.getCategoryName();
            case 3:
                return dto.getDescription();
        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        String columnName = headers[column];
        return columnName;
    }

}
