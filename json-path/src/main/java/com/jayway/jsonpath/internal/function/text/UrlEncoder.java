package com.jayway.jsonpath.internal.function.text;

import com.fasterxml.jackson.databind.node.TextNode;
import com.jayway.jsonpath.internal.EvaluationContext;
import com.jayway.jsonpath.internal.PathRef;
import com.jayway.jsonpath.internal.function.Parameter;
import com.jayway.jsonpath.internal.function.PathFunction;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.net.URLEncoder;

public class UrlEncoder implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        if (model instanceof String) {
            try {
                return URLEncoder.encode((String)model, StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        if (model instanceof TextNode) {
            try {
                return URLEncoder.encode(((TextNode) model).asText(), StandardCharsets.UTF_8.toString());
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return null;
    }
}
