package com.olrox.aot.layout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Comparator;
import java.util.NavigableMap;

public class TagFrequenceDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table1;
    private JButton helpButton;

    public TagFrequenceDialog(NavigableMap<String, Integer> tagCountMap) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        tagCountMap.forEach((k, v) -> {
            tableModel.addRow(new String[]{k, String.valueOf(v)});
        });

        helpButton.addActionListener(l -> {
            AboutTagsDialog aboutTagsDialog = new AboutTagsDialog();
            aboutTagsDialog.setModal(true);
            aboutTagsDialog.setVisible(true);
        });

        setPreferredSize(new Dimension(1000, 700));
        pack();
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    private void createUIComponents() {
        DefaultTableModel tableModel = new DefaultTableModel();
        Object[] columnsHeader = new String[]{"Tag", "Count"};
        tableModel.setColumnIdentifiers(columnsHeader);

        table1 = new JTable(tableModel);
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table1.setRowSorter(sorter);
        sorter.setComparator(1, (Comparator<String>) (s1, s2) -> Integer.valueOf(s1).compareTo(Integer.valueOf(s2)));
    }
}
