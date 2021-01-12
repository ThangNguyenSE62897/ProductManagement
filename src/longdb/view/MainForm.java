/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package longdb.view;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import longdb.daos.TblCatagoryDAO;
import longdb.daos.TblProductDAO;
import longdb.dtos.TblCatagoryDTO;
import longdb.dtos.TblProductDTO;
import longdb.models.CatagoryTableModel;
import longdb.models.ProductTableModel;
import longdb.validators.CatagoryValidator;
import longdb.validators.ProductValidator;

/**
 *
 * @author ACER
 */
public class MainForm extends javax.swing.JFrame {

    private String[] HEADERS_CATAGORY = {"Code", "Name", "Description"};
    private String[] HEADERS_PRODUCT = {"Code", "Name", "Unit", "Quantity", "Price", "CatagoryID"};
    private int[] INDEXES_CATAGORY = {1, 2, 3};
    private int[] INDEXES_PRODUCT = {1, 2, 3, 4, 5, 6};

    private CatagoryTableModel catagoryModel = new CatagoryTableModel(HEADERS_CATAGORY, INDEXES_CATAGORY);
    private ProductTableModel productModel = new ProductTableModel(HEADERS_PRODUCT, INDEXES_PRODUCT);

    //Logger
    private final longdb.errors.Logger writeLog = new longdb.errors.Logger();
    private List<String> exceptionList = new ArrayList<>();

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        initComponents();
        loadComboBox();
        updateUIProduct();
        updateUICatagory();
        setUnableProduct();
        setUnableCatagory();
    }

    private void setUnableCatagory() {
        txtCatagoryCode.setEnabled(false);
        txtCatagoryName.setEnabled(false);
        txtDescription.setEnabled(false);
    }

    private void setUnableProduct() {
        txtProductID.setEnabled(false);
        txtProductName.setEnabled(false);
        txtProductUnit.setEnabled(false);
        txtProductPrice.setEnabled(false);
    }

    private void setEnableCatagory() {
        txtCatagoryCode.setEnabled(true);
        txtCatagoryName.setEnabled(true);
        txtDescription.setEnabled(true);
    }

    private void setEnableProduct() {
        txtProductID.setEnabled(true);
        txtProductName.setEnabled(true);
        txtProductUnit.setEnabled(true);
        txtProductPrice.setEnabled(true);
    }

    private void setTextSupplier(TblCatagoryDTO dto) {
        if (dto != null) {
            // get fields
            String catagoryID = dto.getCategoryID();
            String catagoryName = dto.getCategoryName();
            String description = dto.getDescription();

            // set text
            txtCatagoryCode.setText(catagoryID);
            txtCatagoryName.setText(catagoryName);
            txtDescription.setText(description);
        }
    }

    private void setTextProduct(TblProductDTO dto) {
        if (dto != null) {
            try {
                //get fields
                String productID = dto.getProductID();
                String productName = dto.getProductName();
                String catagoryID = dto.getCatagoryID();
                String unit = dto.getUnit();
                float price = dto.getPrice();
                int quantity = dto.getQuantity();
                //set text
                txtProductID.setText(productID);
                txtProductName.setText(productName);

                //ý tưởng : combobox sẽ set những items bên trong dựa theo index của từng items
                //=> gọi dao để lấy ra catagoryDTO dựa vào catagoryID. Từ catagoryDTO đó mới lấy ra index của nó trong toàn bộ list catagoryDTO
                TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
                //getSupDTOBycatagoryID
                TblCatagoryDTO catagoryDTO = catagoryDAO.getCatagoryDTOByCatagoryID(catagoryID);
                //getIndexSupDTO
                int index = catagoryDAO.getIndexCatagoryDTO(catagoryDTO);
                //set selected index
                cbCatagory.setSelectedIndex(index);

                txtProductUnit.setText(unit);
                txtProductPrice.setText(price + "");
                txtProductQuantity.setText(quantity + "");

            } catch (ClassNotFoundException | SQLException ex) {
                exceptionList.add("Main Form - setTextProduct() : " + ex);
                writeLog.writeFile(exceptionList);
            }

        }
    }

    private void loadComboBox() {
        try {
            //ý tưởng : getAllSups lên rồi addItem vào combobox
            //lưu ý: trước khi addItem phải removeAllItems cũ ra khỏi combobox
            cbCatagory.removeAllItems();
            TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
            List<TblCatagoryDTO> list = catagoryDAO.getAllCatagory();
            if (list != null) {
                for (TblCatagoryDTO dto : list) {
                    cbCatagory.addItem(dto.getCategoryID() + "-" + dto.getCategoryName());
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - loadComboBox() :" + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private TblCatagoryDTO getTextCatagoryDTO() {
        String catagoryID = txtCatagoryCode.getText().trim();
        String catagotyName = txtCatagoryName.getText().trim();
        String description = txtDescription.getText().trim();

        TblCatagoryDTO dto = new TblCatagoryDTO(catagoryID, catagotyName, description);
        return dto;
    }

    private TblProductDTO getTextProductDTO() {
        String productID = txtProductID.getText().trim();
        String productName = txtProductName.getText().trim();
        TblProductDAO productDAO = new TblProductDAO();
        String catagory = (String) cbCatagory.getSelectedItem();
        String tmp[] = catagory.split("-");
        String catagoryID = tmp[0];
        String unit = txtProductUnit.getText().trim();
        String priceString = txtProductPrice.getText().trim();
        String quantityString = txtProductQuantity.getText().trim();
        float price = Float.parseFloat(priceString); // nhớ bắt lỗi numberformat exception
        int quantity = Integer.parseInt(quantityString);
        TblProductDTO itemDTO = new TblProductDTO(productID, productName, catagoryID, unit, quantity, price);
        return itemDTO;
    }

    private void resetFieldsSupplier() {
        txtCatagoryCode.setText("");
        txtCatagoryName.setText("");
        txtDescription.setText("");
    }

    private void resetFieldsProduct() {
        txtProductID.setText("");
        txtProductName.setText("");
        txtProductUnit.setText("");
        txtProductPrice.setText("");
    }

    private void updateUICatagory() {
        try {
            catagoryModel = new CatagoryTableModel(HEADERS_CATAGORY, INDEXES_CATAGORY);
            TblCatagoryDAO categoryDAO = new TblCatagoryDAO();
            List<TblCatagoryDTO> list = categoryDAO.getAllCatagory();
            catagoryModel.loadData(list);
            tblCatagory.setModel(catagoryModel);
            tblCatagory.updateUI();
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateUICatagory() : " + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private void updateUIProduct() {
        try {
            productModel = new ProductTableModel(HEADERS_PRODUCT, INDEXES_PRODUCT);
            TblProductDAO productDAO = new TblProductDAO();
            List<TblProductDTO> list = productDAO.getAllItems();
            productModel.loadData(list);
            tblProduct.setModel(productModel);
            tblProduct.updateUI();
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateUIProduct():" + ex);
            writeLog.writeFile(exceptionList);
        }
    }

    private boolean updateCatagory() {
        try {
            TblCatagoryDTO catagoryDTO = getTextCatagoryDTO();
            //validate
            CatagoryValidator validator = new CatagoryValidator();
            boolean checkValidCatagoryName = validator.checkRegexcatagoryName(catagoryDTO.getCategoryName());
            boolean checkValidDescription = validator.checkRegexDescription(catagoryDTO.getDescription());
            //
            //if
            if (!checkValidCatagoryName) {
                JOptionPane.showMessageDialog(this, "Catagory name have length [3-15] characters");
                return false;
            }
            if (!checkValidDescription) {
                JOptionPane.showMessageDialog(this, "Description have length [3-30] characters");
                return false;
            }
            //
            //update
            if (checkValidCatagoryName && checkValidDescription) {
                TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
                boolean result = catagoryDAO.updateCatagory(catagoryDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateCatagory() :" + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean saveSupplier() {
        try {
            TblCatagoryDTO catagoryDTO = getTextCatagoryDTO();
            //validate
            CatagoryValidator validator = new CatagoryValidator();
            boolean checkDupplicateCatagoryID = validator.checkDupplicateCatagoryID(catagoryDTO.getCategoryID());
            boolean checkValidCatagoryID = validator.checkRegexCatagoryID(catagoryDTO.getCategoryID());
            boolean checkValidCatagoryName = validator.checkRegexcatagoryName(catagoryDTO.getCategoryName());
            boolean checkValidDescription = validator.checkRegexDescription(catagoryDTO.getDescription());
            //
            //if...
            if (checkDupplicateCatagoryID) {
                JOptionPane.showMessageDialog(this, "Duplicated catagoryID");
                return false;
            }
            if (!checkValidCatagoryID) {
                JOptionPane.showMessageDialog(this, "catagoryID must not contain special characters and have length [3-15] characters");
                return false;
            }
            if (!checkValidCatagoryName) {
                JOptionPane.showMessageDialog(this, "Catagory name have length [3-15] characters");
                return false;
            }
            if (!checkValidDescription) {
                JOptionPane.showMessageDialog(this, "Description have length [3-30] characters");
                return false;
            }
            //
            //save
            if (checkDupplicateCatagoryID == false && checkValidCatagoryID && checkValidCatagoryName && checkValidDescription) {
                TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
                boolean result = catagoryDAO.addCatagory(catagoryDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException ex) {
            exceptionList.add("Main Form - saveCatagory(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (SQLException ex) {
            exceptionList.add("Main Form - saveCatarogy(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Duplicate catarogy ID");
            return false;
        }
        return false;
    }

    private boolean deleteCatagory() {
        try {
            TblCatagoryDTO catagoryDTO = getTextCatagoryDTO();
            boolean checkAvailable = checkCatagoryStillAvailable(catagoryDTO.getCategoryID());
            if (!checkAvailable) {
                TblCatagoryDAO catagoryDAO = new TblCatagoryDAO();
                boolean result = catagoryDAO.deleteCatagory(catagoryDTO);
                if (result) {
                    return true;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - deleteCatagory(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean checkCatagoryStillAvailable(String supplierCode) {
        try {
            TblProductDAO productDAO = new TblProductDAO();
            List<TblProductDTO> itemList = productDAO.getAllItems();
            for (TblProductDTO dto : itemList) {
                String catagoryID = productDAO.getCatagoryIDByProductID(dto.getProductID());
                if (supplierCode.equals(catagoryID)) {
                    return true;
                }
            }

        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - checkCatagoryStillAvailable(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    private boolean isSelectingRowCatagory() {
        int rowSelect = tblCatagory.getSelectedRow();
        if (rowSelect >= 0) {
            return true;
        }
        return false;
    }

    private boolean isSelectingRowProduct() {
        int rowSelect = tblProduct.getSelectedRow();
        if (rowSelect >= 0) {
            return true;
        }
        return false;
    }

    private boolean updateProuct() {
        try {
            TblProductDTO productDTO = getTextProductDTO();
            //validate
            ProductValidator validator = new ProductValidator();
            boolean checkRegexProductName = validator.checkRegexProductName(productDTO.getProductName());
            boolean checkRegexUnit = validator.checkRegexUnit(productDTO.getUnit());
            boolean checkRegexPrice = validator.checkRegexPrice(productDTO.getPrice() + "");
            boolean checkRegexQuantity = validator.checkRegexPrice(productDTO.getQuantity() + "");
            //
            //if
            if (!checkRegexProductName) {
                JOptionPane.showMessageDialog(this, "Product Name have length [3-15] characters");
                return false;
            }
            if (!checkRegexUnit) {
                JOptionPane.showMessageDialog(this, "Product Unit have length [1-10] characters");
                return false;
            }
            if (!checkRegexPrice) {
                JOptionPane.showMessageDialog(this, "Price must be a float number and > 0");
                return false;
            }
            if (!checkRegexQuantity) {
                JOptionPane.showMessageDialog(this, "Quatity must be a integer number and > 0");
                return false;
            }
            //
            //update
            if (checkRegexProductName && checkRegexUnit && checkRegexPrice && checkRegexQuantity) {
                TblProductDAO productDAO = new TblProductDAO();
                boolean result = productDAO.updateProduct(productDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form - updateProduct(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (NumberFormatException ex) {
            exceptionList.add("Main Form - updateProduct(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Wrong Number Format");
            return false;
        }
        return false;
    }

    private boolean saveProduct() {
        try {
            TblProductDTO productDTO = getTextProductDTO();
            //validate
            ProductValidator validator = new ProductValidator();
            boolean checkDupplicateProductID = validator.checkDupplicateProductID(productDTO.getProductID());
            boolean checkRegexProductID = validator.checkRegexProductID(productDTO.getProductID());
            boolean checkRegexproductName = validator.checkRegexProductName(productDTO.getProductName());
            boolean checkRegexUnit = validator.checkRegexUnit(productDTO.getUnit());
            boolean checkRegexPrice = validator.checkRegexPrice(productDTO.getPrice() + "");
            boolean checkRegexQuantity = validator.checkRegeQuatity(productDTO.getQuantity() + "");
            //
            //if..
            if (checkDupplicateProductID) {
                JOptionPane.showMessageDialog(this, "Duplicate Product ID");
                return false;
            }
            if (!checkRegexProductID) {
                JOptionPane.showMessageDialog(this, "Item Code not contains special characters and Have Length [3-15] characters ");
                return false;
            }
            if (!checkRegexproductName) {
                JOptionPane.showMessageDialog(this, "Item Name Have Length [3-15] characters ");
                return false;
            }
            if (!checkRegexUnit) {
                JOptionPane.showMessageDialog(this, "Unit Have Length [1-10] characters");
                return false;
            }
            if (!checkRegexPrice) {
                JOptionPane.showMessageDialog(this, "Price is in a float number and > 0");
                return false;
            }
            if (!checkRegexQuantity) {
                JOptionPane.showMessageDialog(this, "Quantity is in a integer number and > 0");
                return false;
            }
            //
            //save
            if (checkDupplicateProductID == false && checkRegexProductID && checkRegexproductName && checkRegexUnit && checkRegexPrice) {
                TblProductDAO productDAO = new TblProductDAO();
                boolean result = productDAO.addProduct(productDTO);
                if (result) {
                    return true;
                }
            }
        } catch (ClassNotFoundException ex) {
            exceptionList.add("Main Form - saveProduct(): " + ex);
            writeLog.writeFile(exceptionList);
        } catch (SQLException ex) {
            exceptionList.add("Main Form - saveProduct(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Duplicate Product ID");
            return false;
        } catch (NumberFormatException ex) {
            exceptionList.add("Main Form - saveProduct(): " + ex);
            writeLog.writeFile(exceptionList);
            JOptionPane.showMessageDialog(this, "Wrong Number Format");
            return false;
        }
        return false;
    }

    private boolean deleteProduct() {
        try {
            TblProductDTO productDTO = getTextProductDTO();
            TblProductDAO productDAO = new TblProductDAO();
            boolean deleteResult = productDAO.deleteProduct(productDTO);
            if (deleteResult) {
                return true;
            }
        } catch (ClassNotFoundException | SQLException ex) {
            exceptionList.add("Main Form -  deleteProduct(): " + ex);
            writeLog.writeFile(exceptionList);
        }
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtWelcome = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblCatagory = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtCatagoryCode = new javax.swing.JTextField();
        txtCatagoryName = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        btnCatagoryAdd = new javax.swing.JButton();
        btnCatagorySave = new javax.swing.JButton();
        btnCatagoryDel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtProductID = new javax.swing.JTextField();
        txtProductName = new javax.swing.JTextField();
        cbCatagory = new javax.swing.JComboBox<>();
        txtProductUnit = new javax.swing.JTextField();
        txtProductQuantity = new javax.swing.JTextField();
        txtProductPrice = new javax.swing.JTextField();
        btnProductAdd = new javax.swing.JButton();
        btnProductSave = new javax.swing.JButton();
        btnProductDelete = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProduct = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel5.setFont(new java.awt.Font("Tahoma", 0, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 51, 51));
        jLabel5.setText("Product Management");

        tblCatagory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        tblCatagory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCatagoryMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblCatagory);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 296, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
        );

        jLabel1.setText("Catagory ID: ");

        jLabel2.setText("Catagory Name: ");

        jLabel3.setText("Description: ");

        btnCatagoryAdd.setText("Add New");
        btnCatagoryAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatagoryAddActionPerformed(evt);
            }
        });

        btnCatagorySave.setText("Save");
        btnCatagorySave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatagorySaveActionPerformed(evt);
            }
        });

        btnCatagoryDel.setText("Delete");
        btnCatagoryDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatagoryDelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(27, 27, 27)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCatagoryCode)
                    .addComponent(txtCatagoryName)
                    .addComponent(txtDescription))
                .addContainerGap())
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnCatagoryAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCatagorySave)
                .addGap(18, 18, 18)
                .addComponent(btnCatagoryDel)
                .addGap(0, 36, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtCatagoryCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(48, 48, 48)
                        .addComponent(jLabel2))
                    .addComponent(txtCatagoryName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(124, 124, 124)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCatagoryAdd)
                    .addComponent(btnCatagorySave)
                    .addComponent(btnCatagoryDel))
                .addContainerGap(85, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Catagory", jPanel1);

        jLabel6.setText("Product ID:");

        jLabel7.setText("Product Name:");

        jLabel8.setText("Catagory Name:");

        jLabel9.setText("Unit:");

        jLabel10.setText("Price:");

        jLabel11.setText("Quantity");

        txtProductID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProductIDActionPerformed(evt);
            }
        });

        cbCatagory.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnProductAdd.setText("Add New");
        btnProductAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductAddActionPerformed(evt);
            }
        });

        btnProductSave.setText("Save");
        btnProductSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductSaveActionPerformed(evt);
            }
        });

        btnProductDelete.setText("Delete");
        btnProductDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProductDeleteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel11))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(txtProductQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtProductName)
                            .addComponent(cbCatagory, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtProductUnit)))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtProductID, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnProductAdd)
                            .addComponent(jLabel10))
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(btnProductSave)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnProductDelete))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtProductID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtProductName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cbCatagory, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtProductUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtProductQuantity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtProductPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnProductAdd)
                    .addComponent(btnProductSave)
                    .addComponent(btnProductDelete))
                .addContainerGap(93, Short.MAX_VALUE))
        );

        tblProduct.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6"
            }
        ));
        tblProduct.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProduct);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 286, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("Product", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel5)
                .addGap(97, 97, 97))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 598, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(txtWelcome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 462, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblCatagoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCatagoryMouseClicked
        setEnableCatagory();
        txtCatagoryCode.setEditable(false);
        int rowSelect = tblCatagory.getSelectedRow();
        if (rowSelect < 0) {
            return;
        } else {
            TblCatagoryDTO catagoryDTO = catagoryModel.getList().get(rowSelect);
            setTextSupplier(catagoryDTO);
        }
    }//GEN-LAST:event_tblCatagoryMouseClicked

    private void btnCatagoryAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatagoryAddActionPerformed
        setEnableCatagory();
        resetFieldsSupplier();
        txtCatagoryCode.setEditable(true);
        updateUICatagory();
    }//GEN-LAST:event_btnCatagoryAddActionPerformed

    private void btnCatagorySaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatagorySaveActionPerformed
        boolean result = isSelectingRowCatagory();
        if (result) {
            //update
            boolean updateResult = updateCatagory();
            if (updateResult) {
                JOptionPane.showMessageDialog(this, "Update Success");
                updateUICatagory();
                loadComboBox();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
                return;
            }
        } else {
            //save
            boolean saveResult = saveSupplier();
            if (saveResult) {
                JOptionPane.showMessageDialog(this, "Save Successful");
                updateUICatagory();
                loadComboBox();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Save Failed");
                return;
            }
        }
    }//GEN-LAST:event_btnCatagorySaveActionPerformed

    private void btnCatagoryDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatagoryDelActionPerformed
        boolean result = isSelectingRowCatagory();
        if (result) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to delete this catagory?");
            if (choice == 0) {

                boolean deleteResult = deleteCatagory();
                if (deleteResult) {
                    JOptionPane.showMessageDialog(this, "Delete Successful");
                    updateUICatagory();
                    loadComboBox();
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed. You need to delete all items that belong to this catagory first");
                    return;
                }
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnCatagoryDelActionPerformed

    private void txtProductIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProductIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProductIDActionPerformed

    private void btnProductAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductAddActionPerformed
        setEnableProduct();
        resetFieldsProduct();
        txtProductID.setEditable(true);
        updateUIProduct();
    }//GEN-LAST:event_btnProductAddActionPerformed

    private void btnProductSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductSaveActionPerformed
        boolean result = isSelectingRowProduct();
        if (result) {
            //update
            boolean updateResult = updateProuct();
            if (updateResult) {
                JOptionPane.showMessageDialog(this, "Update Successful");
                updateUIProduct();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Update Failed");
                return;
            }
        } else {
            //save
            boolean saveResult = saveProduct();
            if (saveResult) {
                JOptionPane.showMessageDialog(this, "Save Successful");
                updateUIProduct();
                return;
            } else {
                JOptionPane.showMessageDialog(this, "Save Failed");
                return;
            }
        }
    }//GEN-LAST:event_btnProductSaveActionPerformed

    private void btnProductDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProductDeleteActionPerformed
        boolean result = isSelectingRowProduct();
        if (result) {
            int choice = JOptionPane.showConfirmDialog(this, "Do you want to delete this Supplier?");
            if (choice == 0) {
                boolean deleteResult = deleteProduct();
                if (deleteResult) {
                    JOptionPane.showMessageDialog(this, "Delete Successful");
                    updateUIProduct();
                    btnCatagoryAddActionPerformed(evt);
                    return;
                } else {
                    JOptionPane.showMessageDialog(this, "Delete Failed");
                    return;
                }
            }
        } else {
            return;
        }
    }//GEN-LAST:event_btnProductDeleteActionPerformed

    private void tblProductMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductMouseClicked
        setEnableProduct();
        txtProductID.setEditable(false);
        int rowSelect = tblProduct.getSelectedRow();
        if (rowSelect < 0) {
            return;
        } else {
            TblProductDTO productDTO = productModel.getList().get(rowSelect);
            setTextProduct(productDTO);
        }
    }//GEN-LAST:event_tblProductMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCatagoryAdd;
    private javax.swing.JButton btnCatagoryDel;
    private javax.swing.JButton btnCatagorySave;
    private javax.swing.JButton btnProductAdd;
    private javax.swing.JButton btnProductDelete;
    private javax.swing.JButton btnProductSave;
    private javax.swing.JComboBox<String> cbCatagory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblCatagory;
    private javax.swing.JTable tblProduct;
    private javax.swing.JTextField txtCatagoryCode;
    private javax.swing.JTextField txtCatagoryName;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtProductID;
    private javax.swing.JTextField txtProductName;
    private javax.swing.JTextField txtProductPrice;
    private javax.swing.JTextField txtProductQuantity;
    private javax.swing.JTextField txtProductUnit;
    private javax.swing.JTextField txtWelcome;
    // End of variables declaration//GEN-END:variables
}
