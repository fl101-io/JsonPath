package com.jayway.jsonpath.internal.function.calc;

import com.fasterxml.jackson.databind.node.NumericNode;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.AbstractPathFunction;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.spi.json.JsonProvider;

import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract public class AbstractCalcFunction extends AbstractPathFunction {
    protected boolean isRoundRequied = false;
    protected abstract Number calculate(Number v1, Number v2);

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        JsonProvider jsonProvider = ctx.configuration().jsonProvider();
        Number paramValue = getParameter(ctx, parameters, 0, 0);
        if (jsonProvider.isArray(model)) {
            Iterable<?> objects = jsonProvider.toIterable(model);
            int count = 0;
            for  (Object o : objects) {
                if (o instanceof Number) {
                    jsonProvider.setArrayIndex(model, count, calculate((Number) o, paramValue));
                }
                count++;
            }
            if (parameters.size() > 1 && isRoundRequied) {
                int k = getParameter(ctx, parameters, 1, 2).intValue();
                return roundArray(ctx, model, k);
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
