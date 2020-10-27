package com.olrox.aot.layout.factory;

import com.olrox.aot.MainFrame;
import com.olrox.aot.layout.AboutTagsDialog;
import com.olrox.aot.layout.TagWordsDialog;
import com.olrox.aot.layout.TextChooserDialog;
import com.olrox.aot.lib.dict.Dictionary;
import com.olrox.aot.lib.text.Text;
import com.olrox.aot.lib.util.DictionarySerializationLoader;
import com.olrox.aot.lib.util.DictionarySerializationSaver;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import java.io.File;
import java.nio.file.Paths;

public class JMenuBarFactory {

    private MainFrame parentFrame;

    public JMenuBarFactory(MainFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public JMenuBar getJMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu textsMenu = new JMenu("Texts");
        menuBar.add(textsMenu);
        JMenu dictionariesMenu = new JMenu("Dictionaries");
        menuBar.add(dictionariesMenu);
        JMenu wordsMenu = new JMenu("Words");
        menuBar.add(wordsMenu);
        JMenu helpMenu = new JMenu("Help");
        menuBar.add(helpMenu);

        JMenuItem addTextItem = new JMenuItem("Add...");
        textsMenu.add(addTextItem);
        JMenuItem tagTextItem = new JMenuItem("Tag...");
        textsMenu.add(tagTextItem);

        JMenuItem saveDictionaryItem = new JMenuItem("Save current dictionary...");
        dictionariesMenu.add(saveDictionaryItem);
        JMenuItem loadDictionaryItem = new JMenuItem("Load dictionary...");
        dictionariesMenu.add(loadDictionaryItem);
        dictionariesMenu.addSeparator();
        JMenuItem clearDictionaryItem = new JMenuItem("Clear dictionary");
        dictionariesMenu.add(clearDictionaryItem);

        JMenuItem addWordItem = new JMenuItem("Add word...");
        wordsMenu.add(addWordItem);

        JMenuItem wordTagsItem = new JMenuItem("About word tags...");
        helpMenu.add(wordTagsItem);

        addTextItem.addActionListener(actionEvent -> {
            JFileChooser fileopen = new JFileChooser("./src/main/resources");
            int ret = fileopen.showDialog(parentFrame, "Open");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                parentFrame.readText(Paths.get(file.getPath()));
            }
        });
        tagTextItem.addActionListener(l -> {
            while (true) {
                TextChooserDialog textChooserDialog = new TextChooserDialog(parentFrame.getDictionary());
                textChooserDialog.setVisible(true);
                String chosenText = textChooserDialog.getResult();
                if (chosenText == null) {
                    break;
                }
                Text text = parentFrame.getDictionary().getTextByPath(chosenText);
                TagWordsDialog tagWordsDialog = new TagWordsDialog(text, parentFrame.getDictionary());
                tagWordsDialog.setVisible(true);
            }
//            parentFrame.getDictionary().getSortedByFrequency().forEach(Word::removeAllTags);
//            parentFrame.getDictionary().getTexts().forEach(text -> {
//                parentFrame.reTag(text);
//            });
        });

        saveDictionaryItem.addActionListener(l -> {
            Dictionary dictionary = parentFrame.getDictionary();
            var saver = new DictionarySerializationSaver();
            JFileChooser fileopen = new JFileChooser("./src/main/resources/dictionaries");
            int ret = fileopen.showSaveDialog(parentFrame);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                saver.save(dictionary, file.getPath());
            }
        });

        loadDictionaryItem.addActionListener(l -> {
            var loader = new DictionarySerializationLoader();
            JFileChooser fileopen = new JFileChooser("./src/main/resources/dictionaries");
            int ret = fileopen.showOpenDialog(parentFrame);
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                var dict = loader.load(file.getPath());
                parentFrame.setDictionary(dict);
            }
        });

        clearDictionaryItem.addActionListener(l -> {
            parentFrame.clearDictionary();
        });

        addWordItem.addActionListener(l -> {
            String result = JOptionPane.showInputDialog(parentFrame, "New word: ");
            parentFrame.addWord(result);
        });

        wordTagsItem.addActionListener(l -> {
            AboutTagsDialog aboutTagsDialog = new AboutTagsDialog();
            aboutTagsDialog.setVisible(true);
        });

        return menuBar;
    }
}
