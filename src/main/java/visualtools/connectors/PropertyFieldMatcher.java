/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualtools.connectors;

import de.bitocean.dspm.inspectors.SOLRSchemaInspector;
import java.awt.BorderLayout;
import java.io.File;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.xml.xpath.XPathExpressionException;
import org.apache.tika.metadata.Metadata;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.SyntaxScheme;

/**
 *
 * @author kamir
 */
public class PropertyFieldMatcher extends javax.swing.JFrame {

    RSyntaxTextArea mapperMl = new RSyntaxTextArea();

    public int selectedRowPDF = 0;
    public int selectedRowSOLR = 0;

    public String fPDF = "?";
    public String fSOLR = "?";

    
    public void process(Metadata md, File f, String schema, String ft) {

        this.jLabel2.setText(f.getName());

        this.setVisible(true);

        this.fullText.setText(ft);

        this.jtSOLR.setCellSelectionEnabled(true);
        this.jtPDF.setCellSelectionEnabled(true);

        ListSelectionModel cellSelectionModel = this.jtSOLR.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedData = null;

                int[] selectedRow = jtSOLR.getSelectedRows();
                int[] selectedColumns = jtSOLR.getSelectedColumns();

                int selectedRowSOLR = jtSOLR.getSelectedRow();

                for (int i = 0; i < selectedRow.length; i++) {
                    for (int j = 0; j < selectedColumns.length; j++) {
                        selectedData = (String) jtSOLR.getValueAt(selectedRow[i], selectedColumns[j]);
                    }
                }
                fSOLR = selectedData;
                
                System.out.println("Selected SOLR field:  " + selectedRowSOLR + " " + selectedData);
                updateMappingSelection();
            }

        });

        ListSelectionModel cellSelectionModel2 = this.jtPDF.getSelectionModel();
        cellSelectionModel2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel2.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String selectedData = null;

                int[] selectedRow = jtPDF.getSelectedRows();
                int[] selectedColumns = jtPDF.getSelectedColumns();

                int selectedRowPDF = jtPDF.getSelectedRow();

                for (int i = 0; i < selectedRow.length; i++) {
                    for (int j = 0; j < selectedColumns.length; j++) {
                        selectedData = (String) jtPDF.getValueAt(selectedRow[i], selectedColumns[j]);
                    }
                }
                
                fPDF = selectedData;

                System.out.println("Selected PDF field: " + selectedRowPDF + " " + selectedData);
                updateMappingSelection();
                
            }

        });

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();

        // SOLR
        DefaultTableModel model3 = (DefaultTableModel) jtSOLR.getModel();

        // PDF - in
        DefaultTableModel model2 = (DefaultTableModel) jtPDF.getModel();

        for (String n : md.names()) {

            Vector row = new Vector();
            row.add(n);
            row.add(md.get(n));

            model.addRow(row);

            Vector row2 = new Vector();
            row2.add(n);
            row2.add(md.get(n));
            row.add(md.isMultiValued(n));

            model2.addRow(row2);

        }

        // fill in the schema fields loaded via the SOLRSchemaInspector. ..
        SOLRSchemaInspector solrInspector = new SOLRSchemaInspector(schema);
        try {
            solrInspector.populateTableModel(model3);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(PropertyFieldMatcher.class.getName()).log(Level.SEVERE, null, ex);
        }

        RSyntaxTextArea ta = new RSyntaxTextArea();

        ta.setText(schema);
        ta.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_XML);
        ta.setCodeFoldingEnabled(true);

        String templateText = "setValues {\n\n\n}";

        mapperMl.setText(templateText);
        mapperMl.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JSON);
        mapperMl.setCodeFoldingEnabled(true);

        jScrollPane5.getViewport().add(ta, BorderLayout.CENTER);
        jScrollPane7.getViewport().add(mapperMl, BorderLayout.CENTER);

        repaint();

    }

    /**
     * Creates new form PropertyFieldMatcher
     */
    public PropertyFieldMatcher() {
        initComponents();
        setSize(1024, 768);
        updateMappingSelection();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        fullText = new javax.swing.JTextArea();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jtPDF = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel9 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        jtSOLR = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        schemaEditor = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT);
        flowLayout1.setAlignOnBaseline(true);
        jPanel2.setLayout(flowLayout1);

        jLabel1.setText("Document : ");
        jPanel2.add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 102, 102));
        jLabel2.setText("...");
        jPanel2.add(jLabel2);

        jPanel1.add(jPanel2, java.awt.BorderLayout.NORTH);

        jTabbedPane1.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);

        jPanel4.setLayout(new java.awt.BorderLayout());

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jScrollPane2.setViewportView(jScrollPane1);

        jPanel4.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        fullText.setColumns(20);
        fullText.setLineWrap(true);
        fullText.setRows(5);
        jScrollPane6.setViewportView(fullText);

        jPanel4.add(jScrollPane6, java.awt.BorderLayout.PAGE_START);

        jTabbedPane1.addTab("Metadata Fields", jPanel4);

        jPanel5.setLayout(new java.awt.GridLayout(1, 4));

        jScrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("PDF-File-Metadata"));

        jtPDF.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value", "isMultiValue"
            }
        ));
        jScrollPane3.setViewportView(jtPDF);

        jPanel5.add(jScrollPane3);

        jPanel7.setLayout(new java.awt.BorderLayout());

        jPanel8.setLayout(new java.awt.BorderLayout());

        jScrollPane7.setBorder(javax.swing.BorderFactory.createTitledBorder("Morphline Snippet"));
        jPanel8.add(jScrollPane7, java.awt.BorderLayout.CENTER);

        jPanel7.add(jPanel8, java.awt.BorderLayout.CENTER);

        jButton3.setText("add mapping to snippet");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3)
                .addContainerGap(65, Short.MAX_VALUE))
        );

        jPanel7.add(jPanel9, java.awt.BorderLayout.PAGE_START);

        jPanel5.add(jPanel7);

        jScrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("SOLR Schema fields"));

        jtSOLR.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "name", "type", "indexed", "stored", "mutivalued"
            }
        ));
        jScrollPane4.setViewportView(jtSOLR);

        jPanel5.add(jScrollPane4);

        jTabbedPane1.addTab("Mapping", jPanel5);

        jPanel6.setLayout(new java.awt.BorderLayout());

        schemaEditor.setLayout(new java.awt.BorderLayout());
        schemaEditor.add(jScrollPane5, java.awt.BorderLayout.CENTER);

        jPanel6.add(schemaEditor, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab("schema.xml", jPanel6);

        jPanel1.add(jTabbedPane1, java.awt.BorderLayout.CENTER);

        jButton1.setText("Close");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        jButton2.setText("Add to index ...");
        jPanel3.add(jButton2);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_jButton1ActionPerformed

    
    public void updateMappingSelection() {
  
        String command = "        " + fSOLR + " : \"@{" + fPDF + "}\"";
        
        this.currentMapping = command;
        
        System.out.println( "MAPPING: " + command );
        
    }
    
    
    public String currentMapping = "?";
    
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String command = currentMapping;

        System.out.println(command);
        
        int position = this.mapperMl.getSelectionStart();
        
        this.mapperMl.insert(command, position);
        
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(PropertyFieldMatcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PropertyFieldMatcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PropertyFieldMatcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PropertyFieldMatcher.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PropertyFieldMatcher().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea fullText;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jtPDF;
    private javax.swing.JTable jtSOLR;
    private javax.swing.JPanel schemaEditor;
    // End of variables declaration//GEN-END:variables
}
