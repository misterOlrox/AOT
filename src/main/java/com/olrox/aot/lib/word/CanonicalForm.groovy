package com.olrox.aot.lib.word

import com.olrox.aot.lib.util.nlp.CanonicalFormUtils

class CanonicalForm implements Serializable {
    String value
    private Set<String> tags = new HashSet<>()
    private Word word

    CanonicalForm(String value, Word word) {
        this.value = value
        this.word = word
    }

    void addTag(String tag) {
        tags.add(tag)
    }

    void generateCanonicalTags() {
        tags.addAll(CanonicalFormUtils.tagsOfCanonicalForm(word.getTags()))
    }

    @Override
    String toString() {
        List<String> tagsList = new ArrayList<>(tags);
        tagsList.sort(String::compareTo);
        tagsList.add(0, value);
        return String.join("_", tagsList);
    }
}
