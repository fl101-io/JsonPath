package com.jayway.jsonpath.internal.function.text;

import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.util.List;

public class Replace implements PathFunction {
    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        if (null != parameters && parameters.size() > 1 && (model instanceof String || model instanceof TextNode)) {
            String result = model instanceof TextNode ? ((TextNode) model).asText() : (String)model;
            for (int i = 0; i < parameters.size(); i+=2) {
                result = result.replaceAll(
                        getParameter(ctx, parameters, i),
                        getParameter(ctx, parameters, i+1)
                );
            }
            return result;
        }
        return null;
    }

    private String getParameter(EvaluationContext ctx, List<Parameter> parameters, int index) {
        List<String> params = Parameter.toList(String.class, ctx, parameters);
        return params.size() > index ? params.get(index) : "";
    }
}
