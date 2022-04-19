package com.bd.assignment2.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private Utils() {
    }

    public static Fixes getObject(final String message) throws Exception {
        return objectMapper.readValue(message, Fixes.class);
    }

    public static String getString(final Fixes message) throws Exception {
        return objectMapper.writeValueAsString(message);
    }
}
