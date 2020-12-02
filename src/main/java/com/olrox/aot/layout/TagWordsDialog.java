package com.olrox.aot.layout;

import com.olrox.aot.lib.dict.Dictionary;
import com.olrox.aot.lib.util.nlp.Tagger;
import com.olrox.aot.lib.text.Text;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TagWordsDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;
    private JLabel statusLabel;
    private JButton helpButton;
    private JLabel textNameLabel;

    private Text text;
    private Dictionary dictionary;

    public TagWordsDialog(Text text, Dictionary dictionary) {
        this.text = text;
        this.dictionary = dictionary;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        textNameLabel.setText(text.getPathToText());

        buttonOK.setEnabled(false);
        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
        helpButton.addActionListener(l -> {
            AboutTagsDialog aboutTagsDialog = new AboutTagsDialog();
            aboutTagsDialog.setModal(true);
            aboutTagsDialog.setVisible(true);
        });

        textArea1.setLineWrap(true);
        textArea1.setWrapStyleWord(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        pack();

        new Thread(this::startTagging).start();
    }

    private void startTagging() {
        String taggedTextRepresentation;
        if (text.getTaggedTextRepresentation() == null) {
            taggedTextRepresentation = Tagger.tagString(text.getAsString());
        } else {
            taggedTextRepresentation = text.getTaggedTextRepresentation();
        }
        textArea1.setText(taggedTextRepresentation);
        statusLabel.setText("Tagged!");
        buttonOK.setEnabled(true);
    }

    private void onOK() {
        String updatedTaggedText = textArea1.getText();
        text.setTaggedTextRepresentation(updatedTaggedText);
        //Tagger.addTagsToWords(taggedText, dictionary);
        //dictionary.getSortedByFrequency().forEach(System.out::println);
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
