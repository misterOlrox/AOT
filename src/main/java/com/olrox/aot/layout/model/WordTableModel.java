package com.olrox.aot.layout.model;

import com.olrox.aot.lib.dict.Dictionary;
import com.olrox.aot.lib.text.Text;
import com.olrox.aot.lib.word.Word;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class WordTableModel extends AbstractTableModel {

    private FilteredList filteredList;
    private Dictionary dictionary;

    public WordTableModel(List<Word> allWords, Dictionary dictionary) {
        this.filteredList = new FilteredList(allWords);
        this.dictionary = dictionary;
    }

    @Override
    public int getRowCount() {
        return filteredList.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int r, int c) {
        switch (c) {
            case 0:
                return filteredList.get(r).getValue();
            case 1:
                return filteredList.get(r).getFrequency();
            case 2:
                return filteredList.get(r).getTagsRepr();
            case 3:
                return filteredList.get(r).getCanonicalFormRepr();
            default:
                return "";
        }
    }

    public Word getWord(int rowIndex) {
        return filteredList.get(rowIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        String oldValue = filteredList.get(rowIndex).getValue();
        long oldQuant = dictionary.getWordsInDictionary();
        dictionary.editWord(oldValue, (String) aValue);
        if (oldQuant != dictionary.getWordsInDictionary()) {
            filteredList.remove(rowIndex);
        }
        fireTableDataChanged();
    }

    public void delete(int rowIndex) {
        dictionary.deleteWord(filteredList.get(rowIndex).getValue());
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int c) {
        switch (c) {
            case 0:
                return "Word";
            case 1:
                return "Frequency";
            case 2:
                return "Tags";
            case 3:
                return "Canonical form";
            default:
                return "Other Column";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (filteredList.size() == 0) {
            return Object.class;
        }
        return getValueAt(0, columnIndex).getClass();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public void fireTableDataChanged() {
        filteredList.updateWords(dictionary.getSortedByFrequency());
        super.fireTableDataChanged();
    }

    public void setFilter(String filter) {
        filteredList.setFilter(filter);
        fireTableDataChanged();
    }

    public void updateTableAfterTextChanged(Text changedText) {
        dictionary.onTextChanged(changedText);
        //String taggedText = Tagger.tagString(changedText.getAsString());
        //Tagger.addTagsToWords(taggedText, dictionary);
    }

    public void setDictionary(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.filteredList = new FilteredList(dictionary.getSortedByFrequency());
        fireTableDataChanged();
    }
}
