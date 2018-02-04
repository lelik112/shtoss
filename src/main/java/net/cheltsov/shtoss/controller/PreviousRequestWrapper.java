package net.cheltsov.shtoss.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

/**
 * RequestWrapper implementation for executing command from different parameterMap
 */
public class PreviousRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String[]> parameterMap;

    /**
     * @param request      original HttpServletRequest
     * @param parameterMap a Map for substitution
     */
    public PreviousRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap) {
        super(request);
        this.parameterMap = parameterMap;
    }

    @Override
    public String getParameter(String name) {
        return parameterMap.get(name)[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(parameterMap.keySet());
    }

    @Override
    public String[] getParameterValues(String name) {
        return parameterMap.get(name);
    }
}
