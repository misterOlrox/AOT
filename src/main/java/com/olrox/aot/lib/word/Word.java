package com.olrox.aot.lib.word;

import com.olrox.aot.lib.text.Text;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface Word extends Serializable {
    String getValue();
    void setValue(String value);
    void addEntry(WordEntry wordEntry);
    long getFrequency();
    void addEntries(List<WordEntry> wordEntries);

    List<WordEntry> getWordEntries();

    void onTextRemoved(Text text);

    void addTag(String tag);

    void removeTag(String tag);

    void removeAllTags();

    Set<String> getTags();

    String getTagsRepr();
}
