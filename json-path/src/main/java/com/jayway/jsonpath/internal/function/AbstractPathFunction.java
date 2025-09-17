package com.jayway.jsonpath.internal.function;

import com.jayway.jsonpath.internal.EvaluationContext;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

abstract public class AbstractPathFunction implements PathFunction {

    protected double round(double value, int places, RoundingMode mode) {
        if (places < 0) return value;
        return BigDecimal.valueOf(value)
                .setScale(places, mode)
                .doubleValue();
    }

    protected Number getParameter(EvaluationContext ctx, List<Parameter> parameters, int index, int defaultValue) {
        List<Number> params = Parameter.toList(Number.class, ctx, parameters);
        return params.size() > index ? params.get(index) : defaultValue;
    }

    protected List<Double> roundList(List<Double> values, int scale) {
        return Optional.ofNullable(values)
                .map(l -> l.stream()
                        .map(v -> round(v, scale, RoundingMode.HALF_UP))
                        .collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }
}
