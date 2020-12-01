package com.olrox.aot;

import com.olrox.aot.layout.EditWordDialog;
import com.olrox.aot.layout.factory.JMenuBarFactory;
import com.olrox.aot.layout.model.WordTableModel;
import com.olrox.aot.lib.dict.Dictionary;
import com.olrox.aot.lib.dict.DictionaryImpl;
import com.olrox.aot.lib.text.Text;
import com.olrox.aot.lib.word.Word;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainFrame extends JFrame {

    private Dictionary dictionary = new DictionaryImpl();
    private List<Word> showedWords = new ArrayList<>();

    private JPanel rootPanel;
    private JTable wordTable;
    private JTextField findWordField;
    private WordTableModel wordTableModel = new WordTableModel(showedWords, dictionary);

    public MainFrame() {
        super("MyPaint");
        setContentPane(rootPanel);

        JMenuBarFactory jMenuBarFactory = new JMenuBarFactory(this);
        setJMenuBar(jMenuBarFactory.getJMenuBar());
        setMinimumSize(new Dimension(500, 500));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);

        findWordField.addActionListener((actionEvent) -> {
            wordTableModel.setFilter(findWordField.getText());
        });

        wordTable.setModel(wordTableModel);
        wordTable.setAutoCreateRowSorter(true);
        wordTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    var dialog = new EditWordDialog(
                            wordTableModel,
                            wordTable.convertRowIndexToModel(wordTable.getSelectedRow()),
                            wordTable.convertColumnIndexToModel(wordTable.getSelectedColumn())
                    );
                    dialog.setVisible(true);
                }
            }
        });
//        readText(Paths.get("./src/main/resources/text1.txt"));
//        readText(Paths.get("./src/main/resources/text2.txt"));
//        readText(Paths.get("./src/main/resources/text3.txt"));
//        readText(Paths.get("./src/main/resources/text4.txt"));
//        readText(Paths.get("./src/main/resources/text5.txt"));
    }

    public void readText(Path path) {
        Text text = new Text(path);
        text.read();
        dictionary.addWords(text);
        wordTableModel.fireTableDataChanged();
    }

    public void addWord(String word) {
        if (!dictionary.contains(word)) {
            dictionary.addWord(word);
            wordTableModel.fireTableDataChanged();
        } else {
            JOptionPane.showMessageDialog(this, "Word is already in dictionary!");
        }
    }

    public void clearDictionary() {
        dictionary.clear();
        wordTableModel.fireTableDataChanged();
    }

    public void tagDictionary() {
        dictionary.tagDictionary();
        wordTableModel.fireTableDataChanged();
    }

    public Dictionary getDictionary() {
        return dictionary;
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        wordTableModel.setDictionary(dictionary);
    }

    public static void main(String[] args) throws UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        MainFrame mainFrame = new MainFrame();
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
