package com.olrox.aot.lib.dict;

import com.olrox.aot.lib.text.Text;
import com.olrox.aot.lib.util.nlp.Tagger;
import com.olrox.aot.lib.word.EnglishWord;
import com.olrox.aot.lib.word.Word;
import com.olrox.aot.lib.word.WordEntry;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class DictionaryImpl implements Dictionary {

    private long wordUsageCounter = 0;
    private Map<String, Word> entireMap = new HashMap<>();
    private Set<Text> texts = new HashSet<>();

    @Override
    public Word addWord(String word) {
        wordUsageCounter++;
        Word existingWord = entireMap.get(word);
        if (existingWord == null) {
            Word newWord = new EnglishWord(word);
            entireMap.put(word, newWord);
            return newWord;
        } else {
            System.err.println("Word already in dictionary");
            return existingWord;
        }
    }

    @Override
    public Word addWord(String word, Text text) {
        texts.add(text);
        wordUsageCounter++;
        Word existingWord = entireMap.get(word);
        if (existingWord == null) {
            Word newWord = new EnglishWord(word);
            entireMap.put(word, newWord);
            newWord.addEntry(new WordEntry(newWord, text));
            return newWord;
        } else {
            existingWord.addEntry(new WordEntry(existingWord, text));
            return existingWord;
        }
    }

    @Override
    public Word editWord(String oldValue, String newValue) {
        Word oldWord = entireMap.get(oldValue);
        Word newWord = entireMap.get(newValue);
        if (oldValue.equals(newValue)) {
            return oldWord;
        }
        if (newWord == null) {
            entireMap.remove(oldValue);
            oldWord.setValue(newValue);
            entireMap.put(newValue, oldWord);
        } else {
            entireMap.remove(oldValue);
            newWord.addEntries(oldWord.getWordEntries());
        }

        return newWord;
    }

    @Override
    public boolean contains(String word) {
        return entireMap.containsKey(word);
    }

    @Override
    public Word getWord(String word) {
        return entireMap.get(word.toLowerCase());
    }

    @Override
    public void addWords(Text text) {
        text.getWords().forEach(w -> this.addWord(w, text));
    }

    @Override
    public List<Word> getSortedByFrequency() {
        return entireMap
                .values()
                .stream()
                .sorted(Comparator.comparingLong(Word::getFrequency).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public long getWordUsageCounter() {
        return wordUsageCounter;
    }

    @Override
    public long getWordsInDictionary() {
        return entireMap.size();
    }

    @Override
    public void deleteWord(String word) {
        entireMap.remove(word);
    }

    @Override
    public void onTextChanged(Text changedText) {
        Set<String> toRemove = new HashSet<>();
        entireMap.forEach((k, v) -> {
            v.onTextRemoved(changedText);
            if (v.getFrequency() == 0) {
                toRemove.add(k);
            }
        });
        toRemove.forEach(k -> entireMap.remove(k));
        changedText.read();
        addWords(changedText);
    }

    @Override
    public void clear() {
        entireMap.clear();
        wordUsageCounter = 0;
    }

    @Override
    public Set<Text> getTexts() {
        return Collections.unmodifiableSet(texts);
    }

    @Override
    public Text getTextByPath(String path) {
        return texts.stream().filter(t -> t.getPathToText().equals(path)).findFirst().get();
    }

    @Override
    public void tagDictionary() {
        entireMap.forEach((k, v) -> v.removeAllTags());
        texts.forEach(t -> {
            if (t.getTaggedTextRepresentation() != null) {
                Tagger.addTagsToWords(t.getTaggedTextRepresentation(), this);
            }
        });
    }

    @Override
    public void generateCanonicalForms() {
        entireMap.forEach((k, word) -> {
            word.generateCanonicalForm();
        });
    }

    @Override
    public NavigableMap<String, Integer> tagsFrequency() {
        TreeMap<String, Integer> tagCount = new TreeMap<>();
        Tagger.tagsMap.keySet().forEach(k -> tagCount.put(k, 0));
        texts.forEach(t -> {
            if (t.getTaggedTextRepresentation() != null) {
                String taggedText = t.getTaggedTextRepresentation();
                String[] eachtag = taggedText.split("\\s+");
                for (String s : eachtag) {
                    String tag = s.split("_")[1];
                    if (Tagger.tagsMap.containsKey(tag)) {
                        int old = tagCount.get(tag);
                        tagCount.put(tag, ++old);
                    }
                }
            }
        });

        return tagCount;
    }

    @Override
    public NavigableMap<Pair<String, String>, Integer> tagsTagsPairFrequency() {
        TreeMap<Pair<String, String>, Integer> count = new TreeMap<>();
        Set<String> setOfTags = new HashSet<>();
        setOfTags.addAll(Tagger.tagsMap.keySet());
        setOfTags.addAll(Tagger.punkt);
        setOfTags.forEach(tag1 -> {
            setOfTags.forEach(tag2 -> {
                count.put(Pair.of(tag1, tag2), 0);
            });
        });
        texts.forEach(t -> {
            if (t.getTaggedTextRepresentation() != null) {
                String taggedText = t.getTaggedTextRepresentation();
                String[] eachtag = taggedText.split("\\s+");
                for (int i = 1; i < eachtag.length; i++) {
                    String tag1 = eachtag[i - 1].split("_")[1];
                    String tag2 = eachtag[i].split("_")[1];
                    if ((Tagger.tagsMap.containsKey(tag1) || Tagger.isPunctuationMark(tag1))
                            && (Tagger.tagsMap.containsKey(tag2) || Tagger.isPunctuationMark(tag2))) {
                        var pair = Pair.of(tag1, tag2);
                        Integer old = count.get(pair);
                        count.put(pair, ++old);
                    }
                }
            }
        });

        return count;
    }

    @Override
    public NavigableMap<Pair<String, String>, Integer> wordTagPairsFrequency() {
        TreeMap<Pair<String, String>, Integer> count = new TreeMap<>();
        texts.forEach(t -> {
            if (t.getTaggedTextRepresentation() != null) {
                String taggedText = t.getTaggedTextRepresentation();
                String[] eachtag = taggedText.split("\\s+");
                for (String s : eachtag) {
                    String word = s.split("_")[0].toLowerCase();
                    String tag = s.split("_")[1];
                    if (entireMap.containsKey(word) && (Tagger.tagsMap.containsKey(tag) || Tagger.isPunctuationMark(tag))) {
                        var pair = Pair.of(word, tag);
                        Integer old = count.get(pair);
                        count.put(pair, old == null ? 1 : ++old);
                    }
                }
            }
        });

        return count;
    }
}
