package com.jayway.jsonpath.internal.function.indicator;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

abstract public class AbstractIndicatorFunction implements PathFunction {

    private static double round(double value, int places) {
        if (places < 0) return value;
        return BigDecimal.valueOf(value)
                .setScale(places, RoundingMode.HALF_UP)
                .doubleValue();
    }

    protected int getParameter(EvaluationContext ctx, List<Parameter> parameters, int index, int defaultValue) {
        List<Number> params = Parameter.toList(Number.class, ctx, parameters);
        return params.size() > index ? params.get(index).intValue() : defaultValue;
    }

    protected List<Double> roundList(List<Double> values, int scale) {
        return Optional.ofNullable(values)
                .map(l -> l.stream()
                        .map(v -> round(v, scale))
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
