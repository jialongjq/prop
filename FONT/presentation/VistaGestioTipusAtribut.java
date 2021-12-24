/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentation;

import java.awt.Color;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author roberto
 */
public class VistaGestioTipusAtribut extends javax.swing.JFrame {
    
    private CtrlPresentacio ctrlPresentacio;
    private String nomTipus;
    private int selectedItem;
        
    /**
     * Creates new form VistaGestioTipusAtributNew
     */
    public VistaGestioTipusAtribut(
            CtrlPresentacio ctrlPresentacio,
            String nomTipusItem
    ) 
    {
        this.ctrlPresentacio = ctrlPresentacio;
        this.nomTipus = nomTipusItem;
        
        initComponents();
        
        this.tItemLabel.setText(nomTipus);
        
        this.editLabel.setText("Afegir atribut");
        this.selectedItem = -1;
        this.fileField.setColumns(18);
        
        loadAtributs();
    }
    
    private void loadAtributs()
    {
        // Deshabilita l'edicio
        atrlTable.setDefaultEditor(Object.class, null);
        
        atrlTable.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting())
                    itemSelected();
            }
        });
        
        obteAtributs();
    }
    
    private void obteAtributs()
    {
        List<String> nomAtr = ctrlPresentacio.getTipusAtributs(this.nomTipus);
        
        boolean any = false;
        DefaultTableModel tm = (DefaultTableModel) atrlTable.getModel();
        for (String atr : nomAtr) {
            String tipus = ctrlPresentacio.getTAtribut(this.nomTipus, atr);
            tm.addRow(new String[] {atr, tipus});
            
            if (!atr.equals("id"))
                any = true;
        }
        
        // Si hi ha algun atribut ocultar l'opcio de carrega
        if (any)
            this.filePanel.setVisible(false);
    }
    
    private void itemSelected()
    {
        msgLabel.setVisible(false);
        int selRow = atrlTable.getSelectedRow();
        
        okBtn.setEnabled(true);
        
        if (selRow == -1) {
            editLabel.setText("Afegir atribut");
            nameTextField.setText("");
            typeCombo.setSelectedIndex(0);
            typeCombo.setEnabled(true);
            removeBtn.setEnabled(false);
        }
        else {
            String nomAtr = (String)atrlTable.getValueAt(selRow, 0);
            String tipusAtr = (String)atrlTable.getValueAt(selRow, 1);

            nameTextField.setText(nomAtr);
            typeCombo.setSelectedItem(tipusAtr);  
            typeCombo.setEnabled(false);
            
            if (!nomAtr.equals("id")) 
                removeBtn.setEnabled(true);
            else {
                removeBtn.setEnabled(false);
                okBtn.setEnabled(false);
            }
            
            editLabel.setText("Editar atribut");
        }
        
        this.selectedItem = selRow;
    }
    
    private void infoMsg(String msg)
    {
        msgLabel.setText(msg);
        msgLabel.setForeground(new Color(3, 107, 46));
        msgLabel.setVisible(true);
    }
    
    private void errorMsg(String msg)
    {
        msgLabel.setText(msg);
        msgLabel.setForeground(Color.RED);
        msgLabel.setVisible(true);
    }
    
    private void afegirAtribut(String nomAtr, String nomTipusAtr)
    {
        // Crea l'atribut
        int retVal = ctrlPresentacio.afegirTipusAtribut(this.nomTipus, nomAtr, nomTipusAtr);
        
        if (retVal == 0) {
            infoMsg("L'atribut s'ha afegit correctament");
        
            // Afegir el nou atribut a la taula
            DefaultTableModel dtm = (DefaultTableModel)atrlTable.getModel();
            dtm.addRow(new String[] {nomAtr, nomTipusAtr});
        }
        else if (retVal == -1) errorMsg("ERROR: El tipus d'ítem especificat no existeix");
        else errorMsg("ERROR: El tipus d'atribut especificat ja existeix");
    }
    
    private void editarAtribut(String nomAtr)
    {
        String nomOrig = (String)atrlTable.getValueAt(this.selectedItem, 0);
        int retVal = ctrlPresentacio.canviarNomTipusAtribut(this.nomTipus, nomOrig, nomAtr);
        if (retVal == 0) {
            infoMsg("Nom del tipus d'atribut canviat correctament");
            
            // Afegir el nou atribut a la taula
            DefaultTableModel dtm = (DefaultTableModel)atrlTable.getModel();
            dtm.setValueAt(nomAtr, this.selectedItem, 0);
        }
        else if (retVal == -1) errorMsg("ERROR: El tipus d'ítem especificat no existeix");
        else if (retVal == -2) errorMsg("ERROR: El tipus d'atribut especificat no existeix");
        else if (retVal == -3) errorMsg("ERROR: El nom d'atribut ja està en ús");
    }
            
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        titleLabel = new javax.swing.JLabel();
        tablePanel = new javax.swing.JScrollPane();
        atrlTable = new javax.swing.JTable();
        msgLabel = new javax.swing.JLabel();
        editPanel = new javax.swing.JPanel();
        editLabel = new javax.swing.JLabel();
        nameTextField = new javax.swing.JTextField();
        nameLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        typeCombo = new javax.swing.JComboBox<>();
        okBtn = new javax.swing.JButton();
        removeBtn = new javax.swing.JButton();
        desBtn = new javax.swing.JButton();
        closeBtn = new javax.swing.JButton();
        filePanel = new javax.swing.JPanel();
        fileLabel = new javax.swing.JLabel();
        fileField = new javax.swing.JTextField();
        exploreBtn = new javax.swing.JButton();
        loadBtn = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        tItemLabel = new javax.swing.JLabel();
        gestItemBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Modificar tipus item");
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        titleLabel.setText("Tipus atributs");

        atrlTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Atribut", "Tipus"
            }
        ));
        atrlTable.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tablePanel.setViewportView(atrlTable);

        editLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        editLabel.setText("dummy");

        nameLabel.setText("nom");

        typeLabel.setText("tipus");

        typeCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Boolea", "Categoric", "Descriptiu", "Numeric" }));

        okBtn.setText("Confirmar");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editLabel)
                    .addGroup(editPanelLayout.createSequentialGroup()
                        .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(typeLabel))
                        .addGap(35, 35, 35)
                        .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(nameTextField)
                            .addComponent(typeCombo, 0, 159, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addComponent(okBtn)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nameLabel))
                .addGap(18, 18, 18)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(typeLabel)
                    .addComponent(typeCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(okBtn))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {nameLabel, nameTextField});

        editPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {typeCombo, typeLabel});

        removeBtn.setText("Eliminar");
        removeBtn.setEnabled(false);
        removeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeBtnActionPerformed(evt);
            }
        });

        desBtn.setText("Treure sel.");
        desBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desBtnActionPerformed(evt);
            }
        });

        closeBtn.setText("Sortir");
        closeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeBtnActionPerformed(evt);
            }
        });

        fileLabel.setText("Carrega items (.csv)");

        fileField.setMaximumSize(new java.awt.Dimension(64, 23));

        exploreBtn.setText("Cerca");
        exploreBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exploreBtnActionPerformed(evt);
            }
        });

        loadBtn.setText("Carrega");
        loadBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filePanelLayout = new javax.swing.GroupLayout(filePanel);
        filePanel.setLayout(filePanelLayout);
        filePanelLayout.setHorizontalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(fileLabel)
                .addGap(18, 18, 18)
                .addComponent(fileField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(exploreBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(loadBtn))
        );
        filePanelLayout.setVerticalGroup(
            filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, filePanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(filePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(fileLabel)
                    .addComponent(fileField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(exploreBtn)
                    .addComponent(loadBtn)))
        );

        filePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {fileField, fileLabel});

        tItemLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 14)); // NOI18N
        tItemLabel.setText("dummy");

        gestItemBtn.setText("Gestionar items");
        gestItemBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gestItemBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 696, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(titleLabel)
                                .addGap(62, 62, 62)
                                .addComponent(tItemLabel)))
                        .addContainerGap(46, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(gestItemBtn)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(msgLabel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(editPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGap(106, 106, 106))
                                .addComponent(tablePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 560, Short.MAX_VALUE)
                                .addComponent(filePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(64, 64, 64)
                                .addComponent(closeBtn)
                                .addContainerGap(46, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(desBtn)
                                    .addComponent(removeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {desBtn, removeBtn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(gestItemBtn)
                    .addComponent(tItemLabel)
                    .addComponent(titleLabel))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(desBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(removeBtn)))
                .addGap(18, 18, 18)
                .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(closeBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(filePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tItemLabel, titleLabel});

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void desBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desBtnActionPerformed
        atrlTable.clearSelection();
    }//GEN-LAST:event_desBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        String nomAtribut = nameTextField.getText();
        if (nomAtribut.length() == 0) {
            errorMsg("ERROR: Els nom d'atribut és obligatori"); 
        }
        else {
            String tipusAtr = (String)typeCombo.getSelectedItem();
            if (this.selectedItem == -1) afegirAtribut(nomAtribut, tipusAtr);
            else editarAtribut(nomAtribut);
        }
    }//GEN-LAST:event_okBtnActionPerformed

    private void closeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeBtnActionPerformed
        ctrlPresentacio.vistaGestioTipusAtributClose();
    }//GEN-LAST:event_closeBtnActionPerformed

    private void removeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeBtnActionPerformed
        String nomAtribut = (String) atrlTable.getValueAt(selectedItem, 0);
        int retVal = ctrlPresentacio.eliminarTipusAtribut(nomTipus, nomAtribut);
            if (retVal == 0) {
                DefaultTableModel dtm = (DefaultTableModel)atrlTable.getModel();
                dtm.removeRow(this.selectedItem);
                atrlTable.clearSelection();
                
                infoMsg("El tipus d'atribut indicat s'ha eliminat correctament"); 
            }
            else if (retVal == -1) errorMsg("ERROR: El tipus d'ítem especificat no existeix");
            else if (retVal == -2) errorMsg("ERROR: El tipus d'atribut especificat no existeix");
            else if (retVal == -3) errorMsg("ERROR: Existeixen ítems del tipus d'ítem especificat");
    }//GEN-LAST:event_removeBtnActionPerformed

    private void exploreBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exploreBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            this.fileField.setText(selectedFile.getAbsolutePath());
        }
    }//GEN-LAST:event_exploreBtnActionPerformed

    private void loadBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadBtnActionPerformed
        int result = ctrlPresentacio.carregaItemsCSV(this.nomTipus, this.fileField.getText());
        if (result == 0) {
            obteAtributs();
            infoMsg("Carregat correctament");
        }
        else if (result == -1) errorMsg("ERROR: El tipus de ítem ja té atributs definits");
        else if (result == -2) errorMsg("ERROR: Aquest tipus ja tenia ítems associats");
        else if (result == -3) errorMsg("ERROR: Es requereix l'atribut id al fitxer per a tots els ítems");
        else if (result == -4) errorMsg("ERROR: S'ha intentat carregar un ítem amb un id existent");
    }//GEN-LAST:event_loadBtnActionPerformed

    private void gestItemBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gestItemBtnActionPerformed
        ctrlPresentacio.vistaGestioItems(this.nomTipus);
        dispose();
    }//GEN-LAST:event_gestItemBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable atrlTable;
    private javax.swing.JButton closeBtn;
    private javax.swing.JButton desBtn;
    private javax.swing.JLabel editLabel;
    private javax.swing.JPanel editPanel;
    private javax.swing.JButton exploreBtn;
    private javax.swing.JTextField fileField;
    private javax.swing.JLabel fileLabel;
    private javax.swing.JPanel filePanel;
    private javax.swing.JButton gestItemBtn;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JButton loadBtn;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTextField;
    private javax.swing.JButton okBtn;
    private javax.swing.JButton removeBtn;
    private javax.swing.JLabel tItemLabel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JComboBox<String> typeCombo;
    private javax.swing.JLabel typeLabel;
    // End of variables declaration//GEN-END:variables
}
