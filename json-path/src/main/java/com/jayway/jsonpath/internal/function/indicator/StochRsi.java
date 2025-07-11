package com.jayway.jsonpath.internal.function.indicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Calculate the StochRSI series of a JSONArray
 *
 * Created by Andrei on 10/07/25.
 */
public class StochRsi extends AbstractWindowFunction {
    @Override
    protected List<Double> calculate(List<Double> values, int window) {
        if (values == null || values.isEmpty() || window <= 0 || values.size() < 2 * window) return new ArrayList<>();

        List<Double> rsi = new Rsi().calculate(values, window);
        if (rsi.isEmpty()) return new ArrayList<>();

        List<Double> stochRSI = new ArrayList<>();
        for (int i = window - 1; i < rsi.size(); i++) {
            int start = Math.max(0, i - window + 1);
            List<Double> rsiWindow = rsi.subList(start, i + 1);

            double highestRSI = rsiWindow.stream().max(Double::compare).orElse(0.0);
            double lowestRSI = rsiWindow.stream().min(Double::compare).orElse(0.0);
            double stochRSIValue = highestRSI == lowestRSI ? 0.0 : (rsi.get(i) - lowestRSI) / (highestRSI - lowestRSI) * 100.0;

            stochRSI.add(stochRSIValue);
        }
        return stochRSI;
    }
}