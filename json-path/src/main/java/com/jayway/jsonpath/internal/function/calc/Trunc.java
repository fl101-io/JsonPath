package com.jayway.jsonpath.internal.function.calc;

import java.math.RoundingMode;

public class Trunc extends AbstractCalcFunction {
    @Override
    protected Number calculate(Number v1, Number v2) {
        return round(v1.doubleValue(), v2.intValue(), RoundingMode.FLOOR);
    }
}
