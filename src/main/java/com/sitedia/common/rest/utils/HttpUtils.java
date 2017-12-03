package com.sitedia.common.rest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

public final class HttpUtils {

    private HttpUtils() {
        // Static class
    }

    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> result = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            result.put(entry.getKey(), entry.getValue()[0]);
        }
        return result;
    }

}
