import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import static org.testng.Assert.*;

public class WordDistanceAnalyzerTest {
    static WordDistanceAnalyzer analyzer;

    @BeforeClass(alwaysRun=true)
    public void setUp() {
        System.out.println("Starting testing...");

        analyzer = new WordDistanceAnalyzer();
    }

    @Test(groups = { "nonParametrized" })
    void emptyInput() {
        assertThrows(IllegalArgumentException.class, () -> { analyzer.getPairsWithMaxDistance(""); });
    }

    @Test(groups = { "nonParametrized" })
    void singleWord() {
        var emptyArrayOfWordPairs = new ArrayList<Pair<String, String>>();
        assertEquals(analyzer.getPairsWithMaxDistance("Word"), emptyArrayOfWordPairs);
    }

    @Test(groups = { "nonParametrized" })
    void notNull() {
        assertNotNull(analyzer.getPairsWithMaxDistance("a b"));
    }

    @Test(groups = { "nonParametrized" })
    void caseSensitive() {
        var array = new ArrayList(Arrays.asList(new Pair("WORD", "words")));
        assertEquals(analyzer.getPairsWithMaxDistance("WORD words WORDS"), array);
    }

    @Test(groups = { "nonParametrized" })
    void simpleWithoutOrder() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), containsInAnyOrder(
                new Pair("a", "b"),
                new Pair("a", "c"),
                new Pair("b", "c")
        ));
    }

    @Test(groups = { "nonParametrized" })
    void simpleWithOrder() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), contains(
                new Pair("a", "b"),
                new Pair("a", "c"),
                new Pair("b", "c")
        ));
    }

    @Test(groups = { "nonParametrized" })
    void delimiters() {
        assertThat(analyzer.getPairsWithMaxDistance("a$b(c^d+e"), hasSize(10));
    }

    @Test(groups = { "nonParametrized" })
    void simpleCorrectSize() {
        assertThat(analyzer.getPairsWithMaxDistance("a b c"), hasSize(3));
    }

    @DataProvider (name = "parametersProvider")
    public Object[][] parametersProviderMethod(){
        return new Object[][] {
                {"a aa aaa", new ArrayList(Arrays.asList(new Pair("a", "aaa")))},
                {"a bb abb", new ArrayList(Arrays.asList(
                        new Pair("a", "bb"),
                        new Pair("a", "abb"),
                        new Pair("bb", "abb")))},
                {"aaa aaa bbb bbb bbb ccc ccc", new ArrayList(Arrays.asList(
                        new Pair("aaa", "bbb"),
                        new Pair("aaa", "ccc"),
                        new Pair("bbb", "ccc")))},
                {"aa ba ab cc", new ArrayList(Arrays.asList(
                        new Pair("aa", "cc"),
                        new Pair("ba", "ab"),
                        new Pair("ba", "cc"),
                        new Pair("ab", "cc")))}
        };
    }

    @Test (dataProvider = "parametersProvider", groups = { "parametrized" })
    void parameterizedTest(String input, ArrayList<Pair<String, String>> expectedRes) {
        assertEquals(analyzer.getPairsWithMaxDistance(input), expectedRes);
    }
}