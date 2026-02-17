package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import org.json.JSONArray;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private static final List<Double> expectedDoubleAddMulResult;

    static {
        expectedDoubleAddMulResult = new ArrayList<>();
        expectedDoubleAddMulResult.add(1091181.00d);
        expectedDoubleAddMulResult.add(1091180.30d);
        expectedDoubleAddMulResult.add(1091180.40d);
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

    private static final List<Double> expectedDoubleDivResult;

    static {
        expectedDoubleDivResult = new ArrayList<>();
        expectedDoubleDivResult.add(54559.0000d);
        expectedDoubleDivResult.add(54558.9650d);
        expectedDoubleDivResult.add(54558.9700d);

    }

    private static final List<Double> expectedIntDivResult;

    static {
        expectedIntDivResult = new ArrayList<>();
        expectedIntDivResult.add(350.0000d);
        expectedIntDivResult.add(350.5000d);
        expectedIntDivResult.add(351.0000d);

    }

    private static final List<Double> expectedLongDivResult;

    static {
        expectedLongDivResult = new ArrayList<>();
        expectedLongDivResult.add(1.07374185E9d);
        expectedLongDivResult.add(1.0737418505E9d);
        expectedLongDivResult.add(1.073741851E9d);

    }

    private static final List<Double> expectedDoubleModResult;

    static {
        expectedDoubleModResult = new ArrayList<>();
        expectedDoubleModResult.add(3.0000d);
        expectedDoubleModResult.add(2.9300d);
        expectedDoubleModResult.add(2.9400d);

    }

    private static final List<Integer> expectedIntModResult;

    static {
        expectedIntModResult = new ArrayList<>();
        expectedIntModResult.add(0);
        expectedIntModResult.add(1);
        expectedIntModResult.add(2);

    }

    private static final List<Long> expectedLongModResult;

    static {
        expectedLongModResult = new ArrayList<>();
        expectedLongModResult.add(0L);
        expectedLongModResult.add(1L);
        expectedLongModResult.add(2L);

    }

    private static final List<Double> expectedDoublePowerResult;

    static {
        expectedDoublePowerResult = new ArrayList<>();
        expectedDoublePowerResult.add(330.33013789d);
        expectedDoublePowerResult.add(330.33003194d);
        expectedDoublePowerResult.add(330.33004707d);

    }

    private static final List<Double> expectedIntPowerResult;

    static {
        expectedIntPowerResult = new ArrayList<>();
        expectedIntPowerResult.add(26.45751311d);
        expectedIntPowerResult.add(26.47640459d);
        expectedIntPowerResult.add(26.49528260d);

    }

    private static final List<Double> expectedLongPowerResult;

    static {
        expectedLongPowerResult = new ArrayList<>();
        expectedLongPowerResult.add(46340.95057290d);
        expectedLongPowerResult.add(46340.95058369d);
        expectedLongPowerResult.add(46340.95059448d);

    }

    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleAdd(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.add(0.1,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleAddResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.add(0.1,4)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleAddResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleMul(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.mul(0.01,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleMulResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.mul(0.01,4)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleMulResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleAddMul(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.add(0.1,4).mul(10,2)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleAddMulResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.add(0.1,4).mul(10,2)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleAddMulResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongAdd(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(longNumbers).read("$.numbers.add(1)", ArrayList.class);
                assertThat(result).isEqualTo(expectedLongAddResult);
            } else {
                JSONArray result = using(conf).parse(longNumbers).read("$.numbers.add(1)", JSONArray.class);
                assertThat(toList(result)).containsExactlyElementsOf(expectedLongAddResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongMul(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(longNumbers).read("$.numbers.mul(3)", ArrayList.class);
                assertThat(result).isEqualTo(expectedLongMulResult);
            } else {
                JSONArray result = using(conf).parse(longNumbers).read("$.numbers.mul(3)", JSONArray.class);
                assertThat(toList(result)).containsExactlyElementsOf(expectedLongMulResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntAdd(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(intNumbers).read("$.numbers.add(1)", ArrayList.class);
                assertThat(result).isEqualTo(expectedIntAddResult);
            } else {
                JSONArray result = using(conf).parse(intNumbers).read("$.numbers.add(1)", JSONArray.class);
                assertThat(toList(result)).containsExactlyElementsOf(expectedIntAddResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntMul(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(intNumbers).read("$.numbers.mul(700)", ArrayList.class);
                assertThat(result).isEqualTo(expectedIntMulResult);
            } else {
                JSONArray result = using(conf).parse(intNumbers).read("$.numbers.mul(700)", JSONArray.class);
                assertThat(toList(result)).containsExactlyElementsOf(expectedIntMulResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testRound(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.round(0)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleRoundResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.round(0)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleRoundResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testTrunc(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.trunc(0)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleTruncResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.trunc(0)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleTruncResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleDiv(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.div(2,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleDivResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.div(2,4)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedDoubleDivResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntDiv(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(intNumbers).read("$.numbers.div(2,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedIntDivResult);
        } else {
            JSONArray result = using(conf).parse(intNumbers).read("$.numbers.div(2,4)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedIntDivResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongDiv(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(longNumbers).read("$.numbers.div(2,4)", ArrayList.class);
            result = result.stream().map(x -> ((Number)x).doubleValue()).collect(Collectors.toList());
            assertThat(result).isEqualTo(expectedLongDivResult);
        } else {
            JSONArray result = using(conf).parse(longNumbers).read("$.numbers.div(2,4)", JSONArray.class);
            assertThat(toList(result)).containsExactlyElementsOf(expectedLongDivResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoubleMod(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.mod(5,4)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoubleModResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.mod(5,4)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedDoubleModResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntMod(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(intNumbers).read("$.numbers.mod(5,4)", ArrayList.class);
                assertThat(result).isEqualTo(expectedIntModResult);
            } else {
                JSONArray result = using(conf).parse(intNumbers).read("$.numbers.mod(5,4)", JSONArray.class);
                assertThat(toList(result)).isEqualTo(expectedIntModResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongMod(Configuration conf) {
        // Gson parses numbers to double
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
                List<?> result = using(conf).parse(longNumbers).read("$.numbers.mod(5,4)", ArrayList.class);
                result = result.stream().map(x -> ((Number)x).longValue()).collect(Collectors.toList());
                assertThat(result).isEqualTo(expectedLongModResult);
            } else {
                JSONArray result = using(conf).parse(longNumbers).read("$.numbers.mod(5,4)", JSONArray.class);
                assertThat(toList(result)).isEqualTo(expectedLongModResult);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDoublePower(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(doubleNumbers).read("$.numbers.pow(0.5,8)", ArrayList.class);
            assertThat(result).isEqualTo(expectedDoublePowerResult);
        } else {
            JSONArray result = using(conf).parse(doubleNumbers).read("$.numbers.pow(0.5,8)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedDoublePowerResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testIntPower(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(intNumbers).read("$.numbers.pow(0.5,8)", ArrayList.class);
            assertThat(result).isEqualTo(expectedIntPowerResult);
        } else {
            JSONArray result = using(conf).parse(intNumbers).read("$.numbers.pow(0.5,8)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedIntPowerResult);
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testLongPower(Configuration conf) {
        if (!(conf.jsonProvider() instanceof JsonOrgJsonProvider)) {
            List<?> result = using(conf).parse(longNumbers).read("$.numbers.pow(0.5,8)", ArrayList.class);
            assertThat(result).isEqualTo(expectedLongPowerResult);
        } else {
            JSONArray result = using(conf).parse(longNumbers).read("$.numbers.pow(0.5,8)", JSONArray.class);
            assertThat(toList(result)).isEqualTo(expectedLongPowerResult);
        }
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

    private static List<Object> toList(JSONArray array) {
        List<Object> list = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            list.add(array.get(i));
        }
        return list;
    }
}
