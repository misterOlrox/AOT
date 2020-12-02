package com.olrox.aot.lib.util.nlp

import edu.stanford.nlp.ling.CoreAnnotations
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.util.CoreMap

class CanonicalFormUtils {

    public static StanfordCoreNLP pipeline
    public static Map<String, String> canonicalTagsMap;


    static {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    static {
        Map<String, String> morphMap = new TreeMap<>();
        morphMap.put("CC", "CC");
        morphMap.put("CD", "CD");
        morphMap.put("DT", "DT");
        morphMap.put("EX", "EX");
        morphMap.put("FW", "FW");
        morphMap.put("IN", "IN");
        morphMap.put("JJ", "JJ");
        morphMap.put("JJR", "JJ");
        morphMap.put("JJS", "JJ");
        morphMap.put("LS", "LS");
        morphMap.put("MD", "MD");
        morphMap.put("NN", "NN");
        morphMap.put("NNS", "NN");
        morphMap.put("NNP", "NNP");
        morphMap.put("NNPS", "NNP");
        morphMap.put("PDT", "PDT");
        morphMap.put("POS", "POS");
        morphMap.put("PRP", "PRP");
        morphMap.put('PRP$', "PRP");
        morphMap.put("RB", "RB");
        morphMap.put("RBR", "RB");
        morphMap.put("RBS", "RB");
        morphMap.put("RP", "RP");
        morphMap.put("SYM", "SYM");
        morphMap.put("TO", "TO");
        morphMap.put("UH", "UH");
        morphMap.put("VB", "VB");
        morphMap.put("VBD", "VB");
        morphMap.put("VBG", "VB");
        morphMap.put("VBN", "VB");
        morphMap.put("VBP", "VB");
        morphMap.put("VBZ", "VB");
        morphMap.put("WDT", "WDT");
        morphMap.put("WP", "WP");
        morphMap.put('WP$', "WP");
        morphMap.put("WRB", "WRB");
        canonicalTagsMap = Collections.unmodifiableMap(morphMap);
    }

    static String generate(String word) {
        Annotation tokenAnnotation = new Annotation(word);
        pipeline.annotate(tokenAnnotation);  // necessary for the LemmaAnnotation to be set.
        List<CoreMap> list = tokenAnnotation.get(CoreAnnotations.SentencesAnnotation.class);
        list.get(0)
                .get(CoreAnnotations.TokensAnnotation.class)
                .get(0)
                .get(CoreAnnotations.LemmaAnnotation.class)
    }

    static Set<String> tagsOfCanonicalForm(Set<String> tags) {
        Set<String> resultSet = new HashSet<>()
        tags.forEach(t -> {
            resultSet.add(canonicalTagsMap.get(t))
        })
        resultSet
    };

//    static List<Triple<String, String, String>> calculate(String text){
//        Annotation document = new Annotation(text);
//
//        pipeline.annotate(document);
//
//        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
//
//        List<Triple<String, String, String>> words = new ArrayList<>();
//
//        for(CoreMap sentence: sentences) {
//            // traversing the words in the current sentence
//            // a CoreLabel is a CoreMap with additional token-specific methods
//            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
//                // this is the text of the token
//                String word = token.get(CoreAnnotations.TextAnnotation.class);
//
////                if(Vocabulary.clearRawWord(word).equals("")){
////                    if(isPunctuationMark(word)){
////                        String pos = "PM";
////                        // this is the NER label of the token
////                        String lemma = word;
////
////                        words.add(Triple.of(word.toLowerCase(), pos, lemma.toLowerCase()));
////                    }
////                    continue;
////                }
//
//                // this is the POS tag of the token
//                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
//                // this is the NER label of the token
//                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
//
//                words.add(Triple.of(word.toLowerCase(), pos, lemma.toLowerCase()));
//
////                System.out.println(String.format("word: [%s], pos: [%s], lemma: {%s]", word, pos, lemma));
//            }
//        }
//        return words;
//    }

}
