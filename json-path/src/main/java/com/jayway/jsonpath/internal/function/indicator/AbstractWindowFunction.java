package com.jayway.jsonpath.internal.function.indicator;

import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.AbstractPathFunction;
import com.jayway.jsonpath.internal.function.Parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract public class AbstractWindowFunction extends AbstractPathFunction {

    protected abstract List<Double> calculate(List<Double> value, int window);

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        int n = getParameter(ctx, parameters, 0, 0).intValue();
        int k = getParameter(ctx, parameters, 1, 2).intValue();
        if (ctx.configuration().jsonProvider().isArray(model) && n > 0 && n <= ctx.configuration().jsonProvider().length(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            List<Double> prices = StreamSupport.stream(objects.spliterator(), false)
                    .filter(Number.class::isInstance)
                    .map(obj -> ((Number) obj).doubleValue())
                    .collect(Collectors.toList());
            return roundList(calculate(prices, n), k);
        }
        return new ArrayList<Double>();
    }
}
