import com.olrox.aot.lib.dict.Dictionary
import com.olrox.aot.lib.dict.DictionaryImpl
import com.olrox.aot.lib.util.nlp.CanonicalFormUtils
import com.olrox.aot.lib.word.Word
import edu.stanford.nlp.tagger.maxent.MaxentTagger
import org.junit.jupiter.api.Test

import static org.junit.Assert.assertEquals

class TestNlp {

    public static String text = "Joe Smith was born in California. " +
            "In 2017, he went to Paris, France in the summer. " +
            "His flight left at 3:00pm on July 10th, 2017. " +
            "After eating some escargot for the first time, Joe said, \"That was delicious!\" " +
            "He sent a postcard to his sister Jane Smith. " +
            "After hearing about Joe's trip, Jane decided she might go to France one day.";

    @Test
    void test() {
        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words-distsim.tagger");
        String text = tagger.tagString(new File("C:\\Users\\nikit\\Desktop\\ДС АОТ\\AOT\\src\\main\\resources\\text1.txt").text.toUpperCase())
        String[] eachtag = text.split("\\s+");
        Dictionary dictionary = new DictionaryImpl();
        for(int i = 0; i < eachtag.length; i++) {
            Word word = dictionary.addWord(eachtag[i].split("_")[0])
            word.addTag(eachtag[i].split("_")[1])
        }
        dictionary.getSortedByFrequency().stream().forEach(x -> println(x))
    }

    @Test
    void test1() {
        MaxentTagger tagger = new MaxentTagger("edu/stanford/nlp/models/pos-tagger/english-left3words-distsim.tagger");
        String text = tagger.tagString("zirak-zigil")
        String[] eachtag = text.split("\\s+");
        Dictionary dictionary = new DictionaryImpl();
        for(int i = 0; i < eachtag.length; i++) {
            Word word = dictionary.addWord(eachtag[i].split("_")[0])
            word.addTag(eachtag[i].split("_")[1])
        }
        dictionary.getSortedByFrequency().stream().forEach(x -> println(x))
    }

    @Test
    void testCanonicalForm() {
        assertEquals("work", CanonicalFormUtils.generate("worked"));
    }

    @Test
    void testCanonicalFormText() {
        def res = CanonicalFormUtils.calculate(CanonicalFormUtils.generate("I worked at the shop."))
        println(res)
    }
}
