package com.sitedia.common.rest.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

/**
 * HTTP utils for HttpServletRequest and HttpServletResponse
 * 
 * @author sitedia
 *
 */
public final class HttpUtils {

    /**
     * Private constructor
     */
    private HttpUtils() {
        // Static class
    }

    /**
     * Extracts parameters from the request
     * 
     * @param request
     * @return
     */
    public static Map<String, Object> getParams(HttpServletRequest request) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, Object> result = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            result.put(entry.getKey(), StringUtils.arrayToCommaDelimitedString(entry.getValue()));
        }
        return result;
    }

}
