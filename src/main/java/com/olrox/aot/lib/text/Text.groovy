package com.olrox.aot.lib.text

import com.olrox.aot.lib.alphabet.EnglishAlphabet

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors

class Text implements Serializable {

    public final static String WORD_DELIMITER = "[\\s/.,‚!`?:;‘’“”*_+—…\\d\"'\\)\\(\\n]+";
    private final static TextSaver textSaver = new TextSaver()

    final String pathToText
    private String stringRepresentation
    String taggedTextRepresentation
    private List<String> words

    Text(Path path) {
        this.pathToText = path.toString()
    }

    void read() {
        words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToText))) {
            String line;
            while ((line = br.readLine()) != null) {
                words.addAll(Arrays.stream(line.trim().toLowerCase().split(WORD_DELIMITER))
                        .filter(s -> !s.isBlank() && !s.startsWith("-") && !s.endsWith("-") && EnglishAlphabet.isEnglish(s))
                        .collect(Collectors.toList())
                );
            }
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }

    void save(String editedText) {
        stringRepresentation = editedText
        textSaver.save(this)
    }

    List<String> getWords() {
        return Collections.unmodifiableList(words)
    }

    String getAsString() {
        if (stringRepresentation == null) {
            stringRepresentation = Files.readString(Paths.get(pathToText)).replaceAll("\\r", "")
        }
        stringRepresentation
    }
}
