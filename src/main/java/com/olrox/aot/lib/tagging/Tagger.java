package com.olrox.aot.lib.tagging;

import com.olrox.aot.lib.dict.Dictionary;
import com.olrox.aot.lib.word.Word;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

public class Tagger {

    public static Map<String, String> tagsMap;
    private static MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words-distsim.tagger");

    static {
        Map<String, String> tempMap = new TreeMap<>();
        tempMap.put("CC", "Coordinating conjunction");
        tempMap.put("CD", "Cardinal number");
        tempMap.put("DT", "Determiner");
        tempMap.put("EX", "Existential there");
        tempMap.put("FW", "Foreign word");
        tempMap.put("IN", "Preposition or subordinating conjunction");
        tempMap.put("JJ", "Adjective");
        tempMap.put("JJR", "Adjective, comparative");
        tempMap.put("JJS", "Adjective, superlative");
        tempMap.put("LS", "List item marker");
        tempMap.put("MD", "Modal");
        tempMap.put("NN", "Noun, singular or mass");
        tempMap.put("NNS", "Noun, plural");
        tempMap.put("NNP", "Proper noun, singular");
        tempMap.put("NNPS", "Proper noun, plural");
        tempMap.put("PDT", "Predeterminer");
        tempMap.put("POS", "Possessive ending");
        tempMap.put("PRP", "Personal pronoun");
        tempMap.put("PRP$", "Possessive pronoun (prolog version PRP-S)");
        tempMap.put("RB", "Adverb");
        tempMap.put("RBR", "Adverb, comparative");
        tempMap.put("RBS", "Adverb, superlative");
        tempMap.put("RP", "Particle");
        tempMap.put("SYM", "Symbol");
        tempMap.put("TO", "to");
        tempMap.put("UH", "Interjection");
        tempMap.put("VB", "Verb, base form");
        tempMap.put("VBD", "Verb, past tense");
        tempMap.put("VBG", "Verb, gerund or present participle");
        tempMap.put("VBN", "Verb, past participle");
        tempMap.put("VBP", "Verb, non-3rd person singular present");
        tempMap.put("VBZ", "Verb, 3rd person singular present");
        tempMap.put("WDT", "Wh-determiner");
        tempMap.put("WP", "Wh-pronoun");
        tempMap.put("WP$", "Possessive wh-pronoun (prolog version WP-S)");
        tempMap.put("WRB", "Wh-adverb");
        tagsMap = Collections.unmodifiableMap(tempMap);
    }

    public static String tagString(String text) {
        return tagger.tagString(text);
    }

    public static void addTagsToWords(String taggedText, Dictionary dictionary) {
        String[] eachtag = taggedText.split("\\s+");
        for (int i = 0; i < eachtag.length; i++) {
            String entry = eachtag[i].split("_")[0];
            String tag = eachtag[i].split("_")[1];
            Word word = dictionary.getWord(entry);
            if (word == null) {
                // System.err.println("No word: " + entry);
            } else {
                word.addTag(tag);
            }
        }
        dictionary.getSortedByFrequency().forEach(word -> {
            if (word.getTags().isEmpty()) {
                String[] eachtag1 = tagger.tagString(word.getValue()).split("\\s+");
                String tag = eachtag1[eachtag1.length - 1].split("_")[1];
                if (tagsMap.containsKey(tag)) {
                    word.addTag(tag);
                }
            }
        });
    }
}
