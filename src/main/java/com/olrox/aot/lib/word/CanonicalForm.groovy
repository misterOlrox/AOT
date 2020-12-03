package com.olrox.aot.lib.word

import com.olrox.aot.lib.util.nlp.CanonicalFormUtils

class CanonicalForm implements Serializable {
    String value
    private Set<String> tags = new HashSet<>()
    private Word word

    CanonicalForm(String value) {
        this.value = value
    }

    void addTag(String tag) {
        tags.add(tag)
    }

    void addWord(Word word) {
        this.word = word
        tags.addAll(CanonicalFormUtils.tagsOfCanonicalForm(word.getTags()))
    }

    @Override
    String toString() {
        List<String> tagsList = new ArrayList<>(tags);
        tagsList.sort(String::compareTo);
        return value + "_" + String.join("_", tagsList);
    }
}
