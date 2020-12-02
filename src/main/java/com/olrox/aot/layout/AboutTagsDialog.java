package com.olrox.aot.layout;

import com.olrox.aot.lib.util.nlp.Tagger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AboutTagsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table1;

    public AboutTagsDialog() {
        setContentPane(contentPane);
        setModal(false);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        setPreferredSize(new Dimension(1000, 700));
        pack();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void createUIComponents() {
        DefaultTableModel tableModel = new DefaultTableModel();
        Object[] columnsHeader = new String[] {"Tag", "Meaning"};
        tableModel.setColumnIdentifiers(columnsHeader);
        Tagger.tagsMap.forEach((k, v) -> {
            tableModel.addRow(new String[]{k, v});
        });

        table1 = new JTable(tableModel);
    }
}
