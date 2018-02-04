package net.cheltsov.shtoss.controller;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Map;

public class ShtossSessionListener implements HttpSessionListener {
    private static final String ATTR_PARAMETER_MAP = "parameterMap";

    /**
     * Creating new Map for saving request parameters
     */
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        Map<String, String[]> previousParameterMap = new HashMap<>();
        se.getSession().setAttribute(ATTR_PARAMETER_MAP, previousParameterMap);
    }
}
