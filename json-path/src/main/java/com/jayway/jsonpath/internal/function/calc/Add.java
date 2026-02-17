package com.jayway.jsonpath.internal.function.calc;

import java.math.BigDecimal;

public class Add extends AbstractCalcFunction {

    @Override
    protected Number calculate(Number v1, Number v2) {
        if (v1 instanceof Double || v2 instanceof Double || v1 instanceof BigDecimal || v2 instanceof BigDecimal) {
            isRoundRequied = true;
            return v1.doubleValue() + v2.doubleValue();
        } else if (v1 instanceof Long || v2 instanceof Long) {
            return v1.longValue() + v2.longValue();
        } else {
            return v1.intValue() + v2.intValue();
        }
    }
}
