package com.olrox.aot.lib.dict;

import com.olrox.aot.lib.text.Text;
import com.olrox.aot.lib.word.Word;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Dictionary extends Serializable {
    Word addWord(String word);

    Word addWord(String word, Text text);

    Word editWord(String oldValue, String newValue);

    boolean contains(String word);

    Word getWord(String word);

    void addWords(Text text);

    List<Word> getSortedByFrequency();

    long getWordUsageCounter();

    long getWordsInDictionary();

    void deleteWord(String word);

    void onTextChanged(Text changedText);

    void clear();

    Set<Text> getTexts();

    Text getTextByPath(String path);

    void tagDictionary();

    void generateCanonicalForms();
}
