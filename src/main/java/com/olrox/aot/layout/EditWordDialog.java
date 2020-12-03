package com.olrox.aot.layout;

import com.olrox.aot.layout.model.WordTableModel;
import com.olrox.aot.lib.util.nlp.Tagger;
import com.olrox.aot.lib.word.Word;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class EditWordDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField textField1;
    private JButton deleteButton;
    private JButton showEntriesButton;
    private JList<String> tagsList;
    private JButton addTagButton;
    private JButton removeTagButton;
    private JTextField canonicalFormTextField;

    public EditWordDialog(WordTableModel wordTableModel, int rowInd, int colInd) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        textField1.setText((String) wordTableModel.getValueAt(rowInd, 0));
        canonicalFormTextField.setText((String) wordTableModel.getValueAt(rowInd, 3));
        Word word = wordTableModel.getWord(rowInd);
        tagsList.setListData(word.getTags().toArray(new String[0]));

        buttonOK.addActionListener(e -> {
            wordTableModel.setValueAt(textField1.getText(), rowInd, colInd);
            word.setCanonicalForm(canonicalFormTextField.getText());
            dispose();
        });
        deleteButton.addActionListener((e) -> {
            int result = JOptionPane.showOptionDialog(this,
                    "Are you sure?",
                    "Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE, null, null, null);
            if (result == 0) {
                wordTableModel.delete(rowInd);
                dispose();
            }
        });
        showEntriesButton.addActionListener(e -> {
            var editTextDialog = new EditTextDialog(wordTableModel.getWord(rowInd));
            editTextDialog.setVisible(true);
            editTextDialog.getSavedTexts().forEach(wordTableModel::updateTableAfterTextChanged);
        });
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }

        });

        addTagButton.addActionListener(l -> {
            String result = JOptionPane.showInputDialog("Enter tag");
            if (!Tagger.tagsMap.containsKey(result)) {
                JOptionPane.showMessageDialog(this, "Wrong tag", "Wrong tag", JOptionPane.ERROR_MESSAGE);
            } else {
                word.addTag(result);
                tagsList.setListData(word.getTags().toArray(new String[0]));
            }
        });

        removeTagButton.addActionListener(l -> {
            int index = tagsList.getSelectedIndex();
            if (index == -1) {
                JOptionPane.showMessageDialog(this, "Select tag", "Select tag", JOptionPane.ERROR_MESSAGE);
            } else {
                String tagToDelete = tagsList.getSelectedValue();
                word.removeTag(tagToDelete);
                tagsList.setListData(word.getTags().toArray(new String[0]));
            }
        });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        setPreferredSize(new Dimension(600, 300));
        pack();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
