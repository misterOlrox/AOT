package com.olrox.aot.lib.word;

import com.olrox.aot.lib.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EnglishWord implements Word {
    private String value;
    private List<WordEntry> wordEntries = new ArrayList<>();
    private Set<String> tags = new HashSet<>();

    public EnglishWord(String value) {
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public long getFrequency() {
        return wordEntries.size();
    }

    @Override
    public void addEntry(WordEntry wordEntry) {
        wordEntries.add(wordEntry);
    }

    @Override
    public void addEntries(List<WordEntry> wordEntries) {
        this.wordEntries.addAll(wordEntries);
    }

    @Override
    public List<WordEntry> getWordEntries() {
        return wordEntries;
    }

    @Override
    public void onTextRemoved(Text text) {
        wordEntries.removeIf(wordEntry -> wordEntry.getText().equals(text));
    }

    @Override
    public void addTag(String tag) {
        tags.add(tag);
    }

    @Override
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    @Override
    public Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    @Override
    public String toString() {
        return "EnglishWord{" +
                "value='" + value + '\'' +
                ", tags=" + tags +
                '}';
    }
}
