package com.olrox.aot.layout;

import org.apache.commons.lang3.tuple.Pair;

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
import java.util.Map;

public class PairFrequencyDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JTable table1;
    private JButton helpButton;

    public PairFrequencyDialog(String columnName1, String columnName2, Map<Pair<String, String>, Integer> countMap) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        DefaultTableModel tableModel = (DefaultTableModel) table1.getModel();
        Object[] columnsHeader = new String[]{columnName1, columnName2, "Count"};
        tableModel.setColumnIdentifiers(columnsHeader);
        countMap.forEach((k, v) -> {
            tableModel.addRow(new String[]{k.getLeft(), k.getRight(), String.valueOf(v)});
        });
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tableModel);
        table1.setRowSorter(sorter);
        sorter.setComparator(2, (Comparator<String>) (s1, s2) -> Integer.valueOf(s1).compareTo(Integer.valueOf(s2)));

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

    private void createUIComponents() {
        DefaultTableModel tableModel = new DefaultTableModel();
        Object[] columnsHeader = new String[]{"Word", "Tag", "Count"};
        tableModel.setColumnIdentifiers(columnsHeader);

        table1 = new JTable(tableModel);
    }
}
