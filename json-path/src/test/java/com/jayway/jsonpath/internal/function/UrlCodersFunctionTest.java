package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Configurations;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

public class UrlCodersFunctionTest extends BaseFunctionTest {

    private static final String textToEncode = "{\"details\": \"line 1\\nline 2\\nline 3\\n\"}";
    private static final String textToDecode = "{\"details\": \"line+1%0Aline+2%0Aline+3%0A\"}";

    public static Iterable<Configuration> configurations() {
        return Configurations.configurations();
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testEncode(Configuration conf) {
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            String result = using(conf).parse(textToEncode).read("$.details.encode()", String.class);
            assertThat(result).isEqualTo("line+1%0Aline+2%0Aline+3%0A");
        }
    }

    @ParameterizedTest
    @MethodSource("configurations")
    public void testDecode(Configuration conf) {
        if (!(conf.jsonProvider() instanceof GsonJsonProvider)) {
            String result = using(conf).parse(textToDecode).read("$.details.decode()", String.class);
            assertThat(result).isEqualTo("line 1\nline 2\nline 3\n");
        }
    }
}
