package com.jayway.jsonpath.internal.function.calc;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.NumericNode;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.AbstractPathFunction;
import com.jayway.jsonpath.internal.function.Parameter;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract public class AbstractCalcFunction extends AbstractPathFunction {
    protected abstract Number calculate(Number v1, Number v2);

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        Number paramValue = getParameter(ctx, parameters, 0, 0);
        if (ctx.configuration().jsonProvider().isArray(model)) {
            Iterable<?> objects = ctx.configuration().jsonProvider().toIterable(model);
            List<Number> result = StreamSupport.stream(objects.spliterator(), false)
                    .filter(Number.class::isInstance)
                    .map(obj -> ((Number) obj))
                    .map(v -> calculate(v, paramValue))
                    .collect(Collectors.toList());
            if (parameters.size() > 1) {
                int k = getParameter(ctx, parameters, 1, 2).intValue();
                return roundList(result.stream().map(Number::doubleValue).collect(Collectors.toList()), k);
            } else {
                return result;
            }

        } else if (model instanceof Number) {
            return invokeCalculation((Number) model, paramValue, ctx, parameters);
        } else if (model instanceof NumericNode) {
            return invokeCalculation(((NumericNode) model).numberValue(), paramValue, ctx, parameters);
        }
        return model;
    }

    private Number invokeCalculation(Number value, Number paramValue, EvaluationContext ctx, List<Parameter> parameters) {
        if (parameters.size() > 1) {
            int k = getParameter(ctx, parameters, 1, 2).intValue();
            double result = calculate(value, paramValue).doubleValue();
            return round(result, k, RoundingMode.HALF_UP);
        } else {
            return calculate(value, paramValue);
        }
    }
}
