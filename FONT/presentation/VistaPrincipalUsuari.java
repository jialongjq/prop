/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package presentation;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author roberto
 */
public class VistaPrincipalUsuari extends javax.swing.JFrame {
    private javax.swing.JTable recomanacionsTable;
    private javax.swing.JTable mainTable;
    private CtrlPresentacio ctrlPresentacio;
    
    private HashMap<String, HashMap<String, String>> mItems;
    private HashMap<String, HashMap<String, String>> mItemsFiltered;
    
    private List<Integer> itemsRecomanats;
    
    private boolean valoracioExistent;
    private boolean displayOnlyVal;
    private String selectedId;
    
    /**
     * Creates new form VistaPrincipalUsuari
     */
    public VistaPrincipalUsuari(CtrlPresentacio ctrlPresentacio) {
        this.ctrlPresentacio = ctrlPresentacio;
        
        initComponents();
        
        valPanel.setVisible(false);
        msgValLabel.setVisible(false);
        msgRecLabel.setVisible(false);
        
        List<String> tipusItems = ctrlPresentacio.getNomTipusItem();
        for (String s : tipusItems) 
            tipusCombo.addItem(s);
        
        tipusCombo.setSelectedIndex(0);
        tipusCombo.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                comboChange();
            }
        });
        
        valSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
              double d = valSlider.getValue() / 2.;
              valLabel.setText(Double.toString(d));
            }
         });
        
        onlyValCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                displayOnlyVal = onlyValCheckBox.isSelected();
                clearSelections();
                carregaTaulaItems();
            }
        });
        
        mItemsFiltered = new HashMap<>();
        valoracioExistent = false;
        displayOnlyVal = false;
        selectedId = "";
        
        carregarTaules();
    }
    
    private void infoMsg(String msg, JLabel label)
    {
        label.setText(msg);
        label.setForeground(new Color(3, 107, 46));
        label.setVisible(true);
    }
    
    private void errorMsg(String msg, JLabel label)
    {
        label.setText(msg);
        label.setForeground(Color.RED);
        label.setVisible(true);
    }
    
    private void resetContainers()
    {
        valoracioExistent = false;
        msgValLabel.setVisible(false);
        msgRecLabel.setVisible(false);
    }
    
    private DefaultTableModel preparaModelTaula(String nomTipusItem)
    {
        List<String> nomAtributs = ctrlPresentacio.getTipusAtributs(nomTipusItem);
        
        String[] colArray = new String[nomAtributs.size()];
        int i = 1;
        for (String colName : nomAtributs) {
            if (!colName.equals("id")) 
                colArray[i++] = colName;
        }
        
        colArray[0] = "id";
        
        DefaultTableModel dtm = new DefaultTableModel(
           colArray,
           0
        );
        
        return dtm;
    }
    
    private void addEntry(DefaultTableModel dtm, String idItem)
    {
        if (!mItems.containsKey(idItem))
            return;
        
        HashMap<String, String> itemVals = mItems.get(idItem);
        
        int colCount = dtm.getColumnCount();
        String[] row = new String[colCount];
        row[0] = idItem;
        for (int c = 1; c < colCount; c++) {
            String colName = dtm.getColumnName(c);
            String value = itemVals.get(colName);

            row[c] = value;
        }

        dtm.addRow(row);
    }
    
    private void setupTable(JTable table, String name)
    {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
        ListSelectionModel selMod = table.getSelectionModel();
        selMod.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting())
                return;
              
            int[] rows = table.getSelectedRows();
            seleccioActualitzada(rows, table, name);
        });
        
        // Per a evitar l'edicio de celes
        table.setDefaultEditor(Object.class, null);
    }
    
    private void carregarTaules()
    {    
        carregaTaulaItems();
        carregaTaulaRecomanacions();
    }
    
    private void carregaTaulaRecomanacions()
    {
        resetContainers();
        String nomTipusItem = (String)tipusCombo.getSelectedItem();
        
        recomanacionsTable = new javax.swing.JTable();
        itemsRecomanats = ctrlPresentacio.getRecomanacions();
        
        DefaultTableModel dtm = preparaModelTaula(nomTipusItem);
        for (Integer id : itemsRecomanats) {
            String idS = Integer.toString(id);
            addEntry(dtm, idS);
        }
        
        recomanacionsTable.setModel(dtm);
        setupTable(recomanacionsTable, "rec");
        recPanel.setViewportView(recomanacionsTable);
    }
   
    private void carregaTaulaItems()
    {
        resetContainers();
        String nomTipusItem = (String)tipusCombo.getSelectedItem();
        
        mainTable = new javax.swing.JTable();        
        mItems = ctrlPresentacio.getItemStrings(nomTipusItem);

        DefaultTableModel dtm = preparaModelTaula(nomTipusItem);
        HashMap<String, HashMap<String, String>> mTarget = (mItemsFiltered.size() > 0 ? mItemsFiltered : mItems);
        for (Map.Entry<String, HashMap<String, String>> kv : mTarget.entrySet()) {
            if (!displayOnlyVal || ctrlPresentacio.getValoracio(kv.getKey()) >= 0)
                addEntry(dtm, kv.getKey());
        }
               
        mainTable.setModel(dtm);
        setupTable(mainTable, "main");
        tablePanel.setViewportView(mainTable);
    }
    
    private void seleccioActualitzada(int[] rows, JTable table, String name)
    {       
        if (rows.length == 0) {
            valPanel.setVisible(false);
            selectedId = "";
            return;
        }
        
        if (name.equals("main"))
            recomanacionsTable.clearSelection();
        else
            mainTable.clearSelection();
        
        msgValLabel.setVisible(false);
        msgRecLabel.setVisible(false);
        
        valPanel.setVisible(true);
        int row = rows[0];
        
        DefaultTableModel dtm = (DefaultTableModel)table.getModel();
        String idItem = (String)dtm.getValueAt(row, 0);
        
        selectedId = idItem;
        
        double valoracio = ctrlPresentacio.getValoracio(idItem);
        
        if (valoracio >= 0) {
            valoracioExistent = true;
            valBtn.setText("Modificar valoració");
            delValBtn.setEnabled(true);
        }
        else {
            valoracioExistent = false;
            valBtn.setText("Afegir valoració");
            delValBtn.setEnabled(false);
        }
        
        Double valClass = Math.max(valoracio, 0) * 2.0;
        int valoracioInt = valClass.intValue();
        
        valSlider.setValue(valoracioInt);
    }
    
    private void comboChange()
    {
        carregarTaules();
    }
    
    private void clearSelections()
    {
        mainTable.clearSelection();
        recomanacionsTable.clearSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        logoutBtn = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        searchLabel = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        tablePanel = new javax.swing.JScrollPane();
        searchBtn = new javax.swing.JButton();
        clearBtn = new javax.swing.JButton();
        valPanel = new javax.swing.JPanel();
        valTitleLabel = new javax.swing.JLabel();
        sliderPanel = new javax.swing.JPanel();
        valSlider = new javax.swing.JSlider();
        valLabel = new javax.swing.JLabel();
        valBtn = new javax.swing.JButton();
        delValBtn = new javax.swing.JButton();
        msgValLabel = new javax.swing.JLabel();
        onlyValCheckBox = new javax.swing.JCheckBox();
        titleLabel = new javax.swing.JLabel();
        recomPanel = new javax.swing.JPanel();
        recTitLabel = new javax.swing.JLabel();
        recPanel = new javax.swing.JScrollPane();
        tipusCombo = new javax.swing.JComboBox<>();
        saveRecomBtn = new javax.swing.JButton();
        loadRecomBtn = new javax.swing.JButton();
        msgRecLabel = new javax.swing.JLabel();
        profileBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        logoutBtn.setText("Logout");
        logoutBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutBtnActionPerformed(evt);
            }
        });

        searchLabel.setText("Cerca");

        searchBtn.setText("Cercar");
        searchBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchBtnActionPerformed(evt);
            }
        });

        clearBtn.setText("Neteja");
        clearBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearBtnActionPerformed(evt);
            }
        });

        valTitleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        valTitleLabel.setText("Valoració");

        valSlider.setMaximum(10);
        valSlider.setValue(0);

        valLabel.setText("0.0");

        javax.swing.GroupLayout sliderPanelLayout = new javax.swing.GroupLayout(sliderPanel);
        sliderPanel.setLayout(sliderPanelLayout);
        sliderPanelLayout.setHorizontalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sliderPanelLayout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addComponent(valSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(valLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        sliderPanelLayout.setVerticalGroup(
            sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sliderPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(sliderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(valSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valLabel))
                .addGap(21, 21, 21))
        );

        sliderPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {valLabel, valSlider});

        valBtn.setText("Afegir valoració");
        valBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                valBtnActionPerformed(evt);
            }
        });

        delValBtn.setText("Esborrar");
        delValBtn.setEnabled(false);
        delValBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delValBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout valPanelLayout = new javax.swing.GroupLayout(valPanel);
        valPanel.setLayout(valPanelLayout);
        valPanelLayout.setHorizontalGroup(
            valPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valPanelLayout.createSequentialGroup()
                .addGroup(valPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(valPanelLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(valTitleLabel))
                    .addGroup(valPanelLayout.createSequentialGroup()
                        .addGap(129, 129, 129)
                        .addComponent(valBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(delValBtn))
                    .addGroup(valPanelLayout.createSequentialGroup()
                        .addGap(113, 113, 113)
                        .addComponent(sliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(121, Short.MAX_VALUE))
        );
        valPanelLayout.setVerticalGroup(
            valPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(valPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(valTitleLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sliderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(valPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valBtn)
                    .addComponent(delValBtn))
                .addContainerGap(18, Short.MAX_VALUE))
        );

        msgValLabel.setText("dummy");

        onlyValCheckBox.setText("Només valorats");

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(valPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(tablePanel, javax.swing.GroupLayout.Alignment.CENTER)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(msgValLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(onlyValCheckBox)
                .addGap(141, 141, 141)
                .addComponent(searchLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchField, javax.swing.GroupLayout.DEFAULT_SIZE, 185, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(clearBtn)
                .addGap(1, 1, 1))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchLabel)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchBtn)
                    .addComponent(clearBtn)
                    .addComponent(onlyValCheckBox))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgValLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 23, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        titleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        titleLabel.setText("Sistema recomanador");

        recTitLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        recTitLabel.setText("Recomanacions");

        saveRecomBtn.setText("Guardar");
        saveRecomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveRecomBtnActionPerformed(evt);
            }
        });

        loadRecomBtn.setText("Carregar");
        loadRecomBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadRecomBtnActionPerformed(evt);
            }
        });

        msgRecLabel.setText("dummy");

        javax.swing.GroupLayout recomPanelLayout = new javax.swing.GroupLayout(recomPanel);
        recomPanel.setLayout(recomPanelLayout);
        recomPanelLayout.setHorizontalGroup(
            recomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recomPanelLayout.createSequentialGroup()
                .addComponent(recTitLabel)
                .addGap(24, 24, 24)
                .addComponent(loadRecomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(saveRecomBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tipusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(recPanel)
            .addGroup(recomPanelLayout.createSequentialGroup()
                .addComponent(msgRecLabel)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        recomPanelLayout.setVerticalGroup(
            recomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(recomPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(recomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(tipusCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(recTitLabel)
                    .addComponent(saveRecomBtn)
                    .addComponent(loadRecomBtn))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(recPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(msgRecLabel)
                .addContainerGap())
        );

        profileBtn.setText("Perfil");
        profileBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                profileBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(titleLabel)
                .addGap(93, 93, 93)
                .addComponent(profileBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logoutBtn)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(recomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 46, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(logoutBtn)
                            .addComponent(profileBtn)))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(titleLabel)))
                .addGap(18, 18, 18)
                .addComponent(recomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(mainPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void logoutBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutBtnActionPerformed
        ctrlPresentacio.logoutUsuari();
        ctrlPresentacio.vistaLoginRegistre();
        dispose();
    }//GEN-LAST:event_logoutBtnActionPerformed

    private void searchBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchBtnActionPerformed
        mItemsFiltered.clear();
        clearSelections();
        
        String pattern = searchField.getText();
        if (pattern.isBlank() || pattern.isEmpty()) 
            return;
        
        pattern = pattern.toLowerCase();
        
        for (Map.Entry<String, HashMap<String, String>> kv : mItems.entrySet()) {
            for (String s : kv.getValue().values()) {
                if (s.toLowerCase().contains(pattern)) {
                    mItemsFiltered.put(kv.getKey(), kv.getValue());
                }
            }
        }
        
        carregaTaulaItems();
    }//GEN-LAST:event_searchBtnActionPerformed

    private void clearBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearBtnActionPerformed
        searchField.setText("");
        mItemsFiltered.clear();
        clearSelections();
        carregaTaulaItems();
    }//GEN-LAST:event_clearBtnActionPerformed

    private void valBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_valBtnActionPerformed
        double valoracio = Double.parseDouble(valLabel.getText());
        
        if (!valoracioExistent) {
            int res = ctrlPresentacio.afegirValoracio(selectedId, valoracio);
            
            if (res == 0) {
                infoMsg("Valoració registrada correctament", msgValLabel);
                clearSelections();
            }
            else if (res == -3) errorMsg("Valoració ja existent", msgValLabel);
            else errorMsg("ERROR: Desconegut", msgValLabel);
        }
        else {
            int res = ctrlPresentacio.modificarValoracio(selectedId, valoracio);
            
            if (res == 0) {
                infoMsg("Valoració registrada correctament", msgValLabel);
                clearSelections();
            }
            else if (res == -3) errorMsg("Valoració no existent", msgValLabel);
            else errorMsg("ERROR: Desconegut", msgValLabel);
        }
        
        carregarTaules();
    }//GEN-LAST:event_valBtnActionPerformed

    private void saveRecomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveRecomBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            int res = ctrlPresentacio.guardarRecomanacions(selectedFile.getAbsolutePath());
            if (res == 0) infoMsg("Recomanacions guardades correctament", msgRecLabel);
            else errorMsg("ERROR: error d'entrada/sortida guardant les recomanacions", msgRecLabel);
        }
    }//GEN-LAST:event_saveRecomBtnActionPerformed

    private void loadRecomBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadRecomBtnActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")));
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            int res = ctrlPresentacio.carregarRecomanacions(selectedFile.getAbsolutePath());
            if (res == 0) {
                carregaTaulaRecomanacions();
                infoMsg("Recomanacions carregades correctament", msgRecLabel);
            }
            else errorMsg("ERROR: error d'entrada/sortida obtenint les recomanacions", msgRecLabel);
        }
    }//GEN-LAST:event_loadRecomBtnActionPerformed

    private void delValBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delValBtnActionPerformed
        int res = ctrlPresentacio.eliminarValoracio(selectedId);

        if (res == 0) {
            infoMsg("Valoració esborrada correctament", msgValLabel);
            clearSelections();
        }
        else if (res == -1) errorMsg("ERROR: Valoració no existent", msgValLabel);
        else errorMsg("ERROR: Desconegut", msgValLabel);
        
        carregarTaules();
    }//GEN-LAST:event_delValBtnActionPerformed

    private void profileBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_profileBtnActionPerformed
        ctrlPresentacio.vistaGestioPerfil();
        dispose();
    }//GEN-LAST:event_profileBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton clearBtn;
    private javax.swing.JButton delValBtn;
    private javax.swing.JButton loadRecomBtn;
    private javax.swing.JButton logoutBtn;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel msgRecLabel;
    private javax.swing.JLabel msgValLabel;
    private javax.swing.JCheckBox onlyValCheckBox;
    private javax.swing.JButton profileBtn;
    private javax.swing.JScrollPane recPanel;
    private javax.swing.JLabel recTitLabel;
    private javax.swing.JPanel recomPanel;
    private javax.swing.JButton saveRecomBtn;
    private javax.swing.JButton searchBtn;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JPanel sliderPanel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JComboBox<String> tipusCombo;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JButton valBtn;
    private javax.swing.JLabel valLabel;
    private javax.swing.JPanel valPanel;
    private javax.swing.JSlider valSlider;
    private javax.swing.JLabel valTitleLabel;
    // End of variables declaration//GEN-END:variables
}
