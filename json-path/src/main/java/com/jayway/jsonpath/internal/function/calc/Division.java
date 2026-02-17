package com.jayway.jsonpath.internal.function.calc;

public class Division extends AbstractCalcFunction {
    @Override
    protected Number calculate(Number v1, Number v2) {
        isRoundRequied = true;
        return v1.doubleValue() / v2.doubleValue();
    }
}
