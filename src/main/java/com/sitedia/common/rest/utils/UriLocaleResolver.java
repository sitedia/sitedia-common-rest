package com.sitedia.common.rest.utils;

import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.web.servlet.i18n.AbstractLocaleContextResolver;

public class UriLocaleResolver extends AbstractLocaleContextResolver {

    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        if (request.getRequestURI().contains("/fr/")) {
            return Locale.FRENCH;
        }
        return Locale.ENGLISH;
    }

    @Override
    public LocaleContext resolveLocaleContext(HttpServletRequest request) {
        return new TimeZoneAwareLocaleContext() {
            @Override
            public Locale getLocale() {
                return getDefaultLocale();
            }

            @Override
            public TimeZone getTimeZone() {
                return getDefaultTimeZone();
            }
        };
    }

    @Override
    public void setLocaleContext(HttpServletRequest request, HttpServletResponse response, LocaleContext localeContext) {
        throw new UnsupportedOperationException("Cannot change fixed locale - use a different locale resolution strategy");
    }

}
