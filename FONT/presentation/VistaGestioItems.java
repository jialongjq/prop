package presentation;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author yoqkn
 */
public class VistaGestioItems extends javax.swing.JFrame {
    /** Elements dinamics */
    private javax.swing.JTable mainTable;
    private DefaultTableModel dtm;
    
    private CtrlPresentacio ctrlPresentacio;
    private String nomTipusItem;
    
    private List<String> nomAtributs;
    private HashMap<String, HashMap<String, String>> mItems;
    private HashMap<String, Integer> idItemToRow;
    private HashMap<String, Integer> atrToCol;
    
    private int selectedRow;
    private int nextId;
    
    
    /**
     * Creates new form VistaGestioItems
     */
    public VistaGestioItems(CtrlPresentacio ctrlPresentacio, String nomTipusItem) {
        this.ctrlPresentacio = ctrlPresentacio;
        this.nomTipusItem = nomTipusItem;
        
        initComponents();
        
        this.atrToCol = new HashMap<>();
        this.idItemToRow = new HashMap<>();
        
        this.tipusLabel.setText(nomTipusItem);
        this.nomAtributs = ctrlPresentacio.getTipusAtributs(nomTipusItem);
        this.mItems = ctrlPresentacio.getItemStrings(nomTipusItem);
        this.dtm = null;
        this.selectedRow = -1;
        this.nextId = 0;
        
        // Actualitza el id suggerit
        this.updateMaxId();
        
        this.editPanel.setVisible(false);
        crearTablaDinamica();
        
        this.atrsCombo.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e){
                comboChange();
            }
        });
    }
    
    private void updateMaxId()
    {
        int max = -1;
        for (String idS : mItems.keySet())
            max = Math.max(Integer.parseInt(idS), max);
        
        this.nextId = max + 1;
        this.idItemField.setText(Integer.toString(nextId));
    }
    
    private void seleccioActualitzada(int[] rows)
    {        
        boolean selectedAny = rows.length > 0;
        
        this.addBtn.setEnabled(!selectedAny);
        this.editBtn.setEnabled(selectedAny);
        this.deleteBtn.setEnabled(selectedAny);
        this.idItemField.setEnabled(!selectedAny);
        this.desBtn.setEnabled(selectedAny);
        
        if (selectedAny) {
            int row = rows[0];
            String idString = (String)dtm.getValueAt(row, 0);
            this.idItemField.setText(idString);
            this.selectedRow = row;
        }
        else {
           this.idItemField.setText(Integer.toString(nextId));
           this.editPanel.setVisible(false);
           this.selectedRow = -1;
        }
        
        this.msgLabel.setText("");
    }
    
    private void afegirRegistreTaula(String id, HashMap<String, String> registre)
    {
        int colCount = dtm.getColumnCount();
        String[] row = new String[colCount];
        row[0] = id;
        for (int i = 1; i < colCount; i++) {
            String colName = dtm.getColumnName(i);
            String value = registre.get(colName);
            
            row[i] = value;
        }
        
        this.idItemToRow.put(id, dtm.getRowCount());
        dtm.addRow(row);
    }
    
    private void modificarColumnaTaula(String id, String nomAtr, String val)
    {
        int row = this.idItemToRow.get(id);
        int col = this.atrToCol.get(nomAtr);
        
        this.dtm.setValueAt(val, row, col);
        
        if (val.isEmpty())
            this.mItems.get(id).remove(nomAtr);
        else
            this.mItems.get(id).put(nomAtr, val);
    }
    
    private void eliminarFilaTaula(String id)
    {
        int row = this.idItemToRow.get(id);
        
        // Esborrar referencies a l'objecte
        this.idItemToRow.remove(id);
        this.mItems.remove(id);
        
        dtm.removeRow(row);
        
        // Actualitzar les files
        int rowCount = dtm.getRowCount();
        for (int i = row; i < rowCount; i++) 
            this.idItemToRow.put((String)dtm.getValueAt(i, 0), i);
        
        this.updateMaxId();
    }
    
    private void crearTablaDinamica()
    {
        mainTable = new javax.swing.JTable();
        
        String[] colArray = new String[this.nomAtributs.size()];
        int i = 1;
        for (String colName : this.nomAtributs) {
            if (!colName.equals("id")) {
                atrToCol.put(colName, i);
                colArray[i++] = colName;
                this.atrsCombo.addItem(colName);
            }
        }
        
        if (colArray.length > 0)
            colArray[0] = "id";
        
        dtm = new DefaultTableModel(
           colArray,
           0
        );
        
        for (Map.Entry<String, HashMap<String, String>> kv : mItems.entrySet())
            afegirRegistreTaula(kv.getKey(), kv.getValue());
        
        mainTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        mainTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                
        ListSelectionModel selMod = mainTable.getSelectionModel();
        selMod.addListSelectionListener((ListSelectionEvent e) -> {
            if (e.getValueIsAdjusting())
                return;
              
            int[] rows = mainTable.getSelectedRows();
            seleccioActualitzada(rows);
        });
        
        // Per a evitar l'edicio de celes
        mainTable.setDefaultEditor(Object.class, null);
        mainTable.setModel(dtm);
        
        tablePanel.setViewportView(mainTable);
    }
    
    private void comboChange()
    {
        if (this.selectedRow == -1) {
            this.valField.setText("");
            this.tAtrLabel.setText("");
            return;
        }
        
        String atrName = (String)this.atrsCombo.getSelectedItem();
        int col = atrToCol.get(atrName);
        
        String val = (String)dtm.getValueAt(this.selectedRow, col);
        this.valField.setText(val);
        this.tAtrLabel.setText(ctrlPresentacio.getTAtribut(nomTipusItem, atrName));
        
        String id = idItemField.getText();
        if (ctrlPresentacio.teAtributDefinit(id, atrName))
            this.delAtrBtn.setEnabled(true);
        else
            this.delAtrBtn.setEnabled(false);
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
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        titlePanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        tipusLabel = new javax.swing.JLabel();
        exitBtn = new javax.swing.JButton();
        tablePanel = new javax.swing.JScrollPane();
        itemActionsPanel = new javax.swing.JPanel();
        idItemLabel = new javax.swing.JLabel();
        idItemField = new javax.swing.JTextField();
        addBtn = new javax.swing.JButton();
        editBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();
        desBtn = new javax.swing.JButton();
        editPanel = new javax.swing.JPanel();
        atrsCombo = new javax.swing.JComboBox<>();
        editLabel = new javax.swing.JLabel();
        atrLabel = new javax.swing.JLabel();
        valLabel = new javax.swing.JLabel();
        valField = new javax.swing.JTextField();
        okBtn = new javax.swing.JButton();
        tAtrLabel = new javax.swing.JLabel();
        delAtrBtn = new javax.swing.JButton();
        msgLabel = new javax.swing.JLabel();

        jCheckBoxMenuItem1.setSelected(true);
        jCheckBoxMenuItem1.setText("jCheckBoxMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        titleLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        titleLabel.setText("Gestionar Items:");

        tipusLabel.setFont(new java.awt.Font("Helvetica Neue", 0, 18)); // NOI18N
        tipusLabel.setText("dummy");

        exitBtn.setText("Sortir");
        exitBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout titlePanelLayout = new javax.swing.GroupLayout(titlePanel);
        titlePanel.setLayout(titlePanelLayout);
        titlePanelLayout.setHorizontalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(titleLabel)
                .addGap(27, 27, 27)
                .addComponent(tipusLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 700, Short.MAX_VALUE)
                .addComponent(exitBtn)
                .addGap(27, 27, 27))
        );
        titlePanelLayout.setVerticalGroup(
            titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(titlePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(titlePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleLabel)
                    .addComponent(tipusLabel)
                    .addComponent(exitBtn))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        titlePanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {tipusLabel, titleLabel});

        idItemLabel.setText("Id ítem");
        idItemLabel.setToolTipText("");

        addBtn.setText("Afegir");
        addBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBtnActionPerformed(evt);
            }
        });

        editBtn.setText("Editar");
        editBtn.setEnabled(false);
        editBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editBtnActionPerformed(evt);
            }
        });

        deleteBtn.setText("Eliminar");
        deleteBtn.setEnabled(false);
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        desBtn.setText("Treu selecció");
        desBtn.setEnabled(false);
        desBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                desBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout itemActionsPanelLayout = new javax.swing.GroupLayout(itemActionsPanel);
        itemActionsPanel.setLayout(itemActionsPanelLayout);
        itemActionsPanelLayout.setHorizontalGroup(
            itemActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemActionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(itemActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(addBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(itemActionsPanelLayout.createSequentialGroup()
                        .addComponent(idItemLabel)
                        .addGap(18, 18, 18)
                        .addComponent(idItemField, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(editBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(desBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        itemActionsPanelLayout.setVerticalGroup(
            itemActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(itemActionsPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(itemActionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idItemLabel)
                    .addComponent(idItemField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(addBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(editBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteBtn)
                .addGap(27, 27, 27)
                .addComponent(desBtn)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        editLabel.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        editLabel.setText("Editar atributs");

        atrLabel.setText("Atribut");

        valLabel.setText("Valor");

        okBtn.setText("Confirmar");
        okBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okBtnActionPerformed(evt);
            }
        });

        tAtrLabel.setText("dummy");

        delAtrBtn.setText("Eliminar");
        delAtrBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                delAtrBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout editPanelLayout = new javax.swing.GroupLayout(editPanel);
        editPanel.setLayout(editPanelLayout);
        editPanelLayout.setHorizontalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(editLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(okBtn)
                        .addGroup(editPanelLayout.createSequentialGroup()
                            .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(atrLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(valLabel))
                            .addGap(26, 26, 26)
                            .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(editPanelLayout.createSequentialGroup()
                                    .addComponent(atrsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(tAtrLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(delAtrBtn))
                                .addComponent(valField, javax.swing.GroupLayout.PREFERRED_SIZE, 503, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(31, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {delAtrBtn, okBtn});

        editPanelLayout.setVerticalGroup(
            editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(editPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(editLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(atrsCombo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(atrLabel)
                    .addComponent(tAtrLabel)
                    .addComponent(delAtrBtn))
                .addGap(18, 18, 18)
                .addGroup(editPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valLabel)
                    .addComponent(valField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(okBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        editPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {valField, valLabel});

        editPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {atrLabel, atrsCombo, tAtrLabel});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(93, 93, 93)
                                .addComponent(itemActionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(editPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 1018, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(titlePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(msgLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(itemActionsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(editPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void desBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_desBtnActionPerformed
        mainTable.clearSelection();
        editPanel.setVisible(false);
    }//GEN-LAST:event_desBtnActionPerformed

    private void editBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editBtnActionPerformed
        editPanel.setVisible(true);
        comboChange();
        this.msgLabel.setText("");
    }//GEN-LAST:event_editBtnActionPerformed

    private void addBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBtnActionPerformed
        String id = idItemField.getText();
        if (id.isBlank() || id.isEmpty()) {
            errorMsg("ERROR: Introduiu l'id de l'ítem que voleu crear");
            return;
        }
        
        int retVal = ctrlPresentacio.crearItem(id, nomTipusItem);
        if (retVal == 0) {
            HashMap<String, String> atrs = new HashMap<>();
            
            ctrlPresentacio.afegirAtribut(id, "id", id);

            // Initialise to empty string every atr
            int colCount = dtm.getColumnCount();
            for (int i = 0; i < colCount; i++) {
                String colName = dtm.getColumnName(i);
                atrs.put(colName, "");
            }

            afegirRegistreTaula(id, atrs);
            infoMsg("Item creat correctament");
            this.mItems.put(id, atrs);
            updateMaxId();
        }
        else if (retVal == -1) errorMsg("ERROR: Ja existeix un ítem amb el id especificat");
        else if (retVal == -3) errorMsg("ERROR: el id de l'ítem ha de ser un nombre enter");
    }//GEN-LAST:event_addBtnActionPerformed

    private void okBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okBtnActionPerformed
        String valor = valField.getText();
        if (valor.isBlank() || valor.isEmpty()) {
            errorMsg("ERROR: El valor de l'atribut no pot ser buit");
            return;
        }
        
        String id = this.idItemField.getText();
        String nomAtr = (String)this.atrsCombo.getSelectedItem();
        if (!ctrlPresentacio.teAtributDefinit(id, nomAtr)) {
            int retVal = ctrlPresentacio.afegirAtribut(id, nomAtr, valor);
            if (retVal == 0) {
                modificarColumnaTaula(id, nomAtr, valor);
                infoMsg("L'atribut s'ha afegit correctament.");
                this.editPanel.setVisible(false);
            }
            else if (retVal == -1) errorMsg("ERROR: L'ítem amb l'id especificat no existeix");
            else if (retVal == -2) errorMsg("ERROR: El tipus d'atribut especificat ja existeix");
            else if (retVal == -3) errorMsg("ERROR: El tipus d'atribut especificat no existeix al tipus d'ítem corresponent");
            else if (retVal == -4) errorMsg("ERROR: El TAtribut no coincideix");
            else errorMsg("ERROR: l'id de l'ítem ha de ser un numero enter");
        }
        else {
            int retVal = ctrlPresentacio.modificarAtribut(id, nomAtr, valor);
            if (retVal == 0) {
                modificarColumnaTaula(id, nomAtr, valor);
                infoMsg("L'atribut s'ha modificat correctament.");
                this.editPanel.setVisible(false);
                
            }
            else if (retVal == -1) errorMsg("ERROR: L'ítem amb l'id especificat no existeix");
            else if (retVal == -2) errorMsg("ERROR: El tipus d'atribut especificat no existeix");
            else if (retVal == -3) errorMsg("ERROR: El TAtribut no coincideix");
            else errorMsg("ERROR: l'id de l'ítem ha de ser un numero enter");
        }
    }//GEN-LAST:event_okBtnActionPerformed

    private void delAtrBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delAtrBtnActionPerformed
        String id = this.idItemField.getText();
        String nomAtr = (String)this.atrsCombo.getSelectedItem();
        
        if (ctrlPresentacio.teAtributDefinit(id, nomAtr)) {
            int retVal = ctrlPresentacio.eliminarAtribut(id, nomAtr);
            if (retVal == 0) {
                infoMsg("L'atribut s'ha eliminat correctament");
                this.editPanel.setVisible(false);
                this.modificarColumnaTaula(id, nomAtr, "");
            }
            else if (retVal == -1) errorMsg("ERROR: L'ítem amb l'id especificat no existeix");
            else if (retVal == -2) errorMsg("ERROR: El tipus d'atribut especificat no existeix");
            else if (retVal == -3) errorMsg("ERROR: El TAtribut no coincideix");
            else errorMsg("Error: l'id de l'ítem ha de ser un numero enter");
        }
    }//GEN-LAST:event_delAtrBtnActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        String id = idItemField.getText();
        
        int retVal = ctrlPresentacio.eliminarItem(id);
        if (retVal == 0) {
            eliminarFilaTaula(id);
            infoMsg("Item eliminat correctament");
        }
        else if (retVal == -1) errorMsg("ERROR: Item que voleu eliminar no existeix");
        else errorMsg("ERROR: l'id de l'ítem ha de ser un numero enter");
        
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void exitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitBtnActionPerformed
        ctrlPresentacio.vistaPrincipal();
        dispose();
    }//GEN-LAST:event_exitBtnActionPerformed

    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addBtn;
    private javax.swing.JLabel atrLabel;
    private javax.swing.JComboBox<String> atrsCombo;
    private javax.swing.JButton delAtrBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JButton desBtn;
    private javax.swing.JButton editBtn;
    private javax.swing.JLabel editLabel;
    private javax.swing.JPanel editPanel;
    private javax.swing.JButton exitBtn;
    private javax.swing.JTextField idItemField;
    private javax.swing.JLabel idItemLabel;
    private javax.swing.JPanel itemActionsPanel;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JLabel msgLabel;
    private javax.swing.JButton okBtn;
    private javax.swing.JLabel tAtrLabel;
    private javax.swing.JScrollPane tablePanel;
    private javax.swing.JLabel tipusLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlePanel;
    private javax.swing.JTextField valField;
    private javax.swing.JLabel valLabel;
    // End of variables declaration//GEN-END:variables
}
