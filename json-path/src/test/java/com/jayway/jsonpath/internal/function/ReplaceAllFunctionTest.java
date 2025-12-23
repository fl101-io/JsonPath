package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

public class ReplaceAllFunctionTest extends BaseFunctionTest {
    private static final String text = "{\"text\": \"Hello, John\\n\"}";

    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEncode(Configuration conf) {
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            String result = using(conf).parse(text).read("$.text.replaceAll(\"Hello\",\"Hi\",\"John\",\"Tom\",\"\\n\")", String.class);
            assertThat(result).isEqualTo("Hi, Tom");
        }
    }
}
