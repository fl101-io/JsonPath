package com.jayway.jsonpath.internal.function.calc;

public class Multiplication extends AbstractCalcFunction {
    @Override
    protected Number calculate(Number v1, Number v2) {
        if (v1 instanceof Double || v2 instanceof Double) {
            return v1.doubleValue() * v2.doubleValue();
        } else if (v1 instanceof Long || v2 instanceof Long) {
            return v1.longValue() * v2.longValue();
        } else {
            return v1.intValue() * v2.intValue();
        }
    }
}
