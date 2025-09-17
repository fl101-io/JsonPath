package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

public class CalcPathFunctionTest extends BaseFunctionTest {
    private static final String doubleNumbers = "{\"numbers\": [109118.0, 109117.93, 109117.94]}";
    private static final String longNumbers = "{\"numbers\": [2147483700, 2147483701, 2147483702]}";
    private static final String intNumbers = "{\"numbers\": [700, 701, 702]}";
    private static final String numbers = "{\"d\": 109117.93, \"l\": 2147483701, \"i\": 701}";

    private static final List<Double> expectedDoubleAddResult;

    static {
        expectedDoubleAddResult = new ArrayList<>();
        expectedDoubleAddResult.add(109118.1000d);
        expectedDoubleAddResult.add(109118.0300d);
        expectedDoubleAddResult.add(109118.0400d);
    }

    private static final List<Long> expectedLongAddResult;

    static {
        expectedLongAddResult = new ArrayList<>();
        expectedLongAddResult.add(2147483701L);
        expectedLongAddResult.add(2147483702L);
        expectedLongAddResult.add(2147483703L);
    }

    private static final List<Integer> expectedIntAddResult;

    static {
        expectedIntAddResult = new ArrayList<>();
        expectedIntAddResult.add(701);
        expectedIntAddResult.add(702);
        expectedIntAddResult.add(703);
    }

    private static final List<Double> expectedDoubleMulResult;

    static {
        expectedDoubleMulResult = new ArrayList<>();
        expectedDoubleMulResult.add(1091.1800d);
        expectedDoubleMulResult.add(1091.1793d);
        expectedDoubleMulResult.add(1091.1794d);
    }

    private static final List<Long> expectedLongMulResult;

    static {
        expectedLongMulResult = new ArrayList<>();
        expectedLongMulResult.add(6442451100L);
        expectedLongMulResult.add(6442451103L);
        expectedLongMulResult.add(6442451106L);
    }

    private static final List<Integer> expectedIntMulResult;

    static {
        expectedIntMulResult = new ArrayList<>();
        expectedIntMulResult.add(490000);
        expectedIntMulResult.add(490700);
        expectedIntMulResult.add(491400);
    }

    private static final List<Double> expectedDoubleRoundResult;

    static {
        expectedDoubleRoundResult = new ArrayList<>();
        expectedDoubleRoundResult.add(109118d);
        expectedDoubleRoundResult.add(109118d);
        expectedDoubleRoundResult.add(109118d);
    }

    private static final List<Double> expectedDoubleTruncResult;

    static {
        expectedDoubleTruncResult = new ArrayList<>();
        expectedDoubleTruncResult.add(109118d);
        expectedDoubleTruncResult.add(109117d);
        expectedDoubleTruncResult.add(109117d);
    }

    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleAdd(Configuration conf) {
        List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.add(0.1,4)", ArrayList.class);
        assertThat(result).isEqualTo(expectedDoubleAddResult);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleMul(Configuration conf) {
        List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.mul(0.01,4)", ArrayList.class);
        assertThat(result).isEqualTo(expectedDoubleMulResult);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongAdd(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            List<?> result = using(conf).parse(longNumbers).read("$.numbers.add(1)", ArrayList.class);
            assertThat(result).isEqualTo(expectedLongAddResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongMul(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            List<?> result = using(conf).parse(longNumbers).read("$.numbers.mul(3)", ArrayList.class);
            assertThat(result).isEqualTo(expectedLongMulResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntAdd(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            List<?> result = using(conf).parse(intNumbers).read("$.numbers.add(1)", ArrayList.class);
            assertThat(result).isEqualTo(expectedIntAddResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntMul(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            List<?> result = using(conf).parse(intNumbers).read("$.numbers.mul(700)", ArrayList.class);
            assertThat(result).isEqualTo(expectedIntMulResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testRound(Configuration conf) {
        List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.round(0)", ArrayList.class);
        assertThat(result).isEqualTo(expectedDoubleRoundResult);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testTrunc(Configuration conf) {
        List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.trunc(0)", ArrayList.class);
        assertThat(result).isEqualTo(expectedDoubleTruncResult);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSingleDoubleValue(Configuration conf) {
        Object result = using(conf).parse(numbers).read("$.d.add(0.1, 4)");
        assertThat(result).isEqualTo(109118.0300d);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSingleLongValue(Configuration conf) {
        Object result = using(conf).parse(numbers).read("$.l.add(1)");
        assertThat(result).isEqualTo(2147483702L);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSingleIntValue(Configuration conf) {
        Object result = using(conf).parse(numbers).read("$.i.add(1)");
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            assertThat(result).isEqualTo(702);
        } else {
            assertThat(result).isEqualTo(702L);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSingleRoundValue(Configuration conf) {
        Object result = using(conf).parse(numbers).read("$.d.round(0)");
        assertThat(result).isEqualTo(109118d);
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testSingleTruncValue(Configuration conf) {
        Object result = using(conf).parse(numbers).read("$.d.trunc(0)");
        assertThat(result).isEqualTo(109117d);
    }
}
