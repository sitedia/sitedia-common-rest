package com.sitedia.common.rest.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.HtmlSanitizer;
import org.owasp.html.HtmlStreamEventReceiver;
import org.owasp.html.HtmlStreamRenderer;

import com.google.common.base.Function;

/**
 * Sanitizer tool, for security purpose
 * @author cedric
 *
 */
public class SanitizeUtils {

    /**
     * Private constructor
     */
    private SanitizeUtils() {
        // Static class
    }

    /**
     * Cleans the HTML code
     */
    public static final Function<HtmlStreamEventReceiver, HtmlSanitizer.Policy> POLICY_DEFINITION = new HtmlPolicyBuilder()
            .allowStandardUrlProtocols()
            // Allow title="..." on any element.
            .allowAttributes("title").globally()
            // Allow href="..." on <a> elements.
            .allowAttributes("href").onElements("a")
            // Defeat link spammers.
            .requireRelNofollowOnLinks()
            // Allow lang= with an alphabetic value on any element.
            .allowAttributes("lang").matching(Pattern.compile("[a-zA-Z]{2,20}")).globally()
            // The align attribute on <p> elements can have any value below.
            .allowAttributes("align").matching(true, "center", "left", "right", "justify", "char").onElements("p")
            // Style
            .allowAttributes("style").matching(true, "text-align: right;").onElements("p")
            // These elements are allowed.
            .allowElements("a", "p", "div", "i", "b", "u", "em", "blockquote", "tt", "strong", "br", "ul", "ol", "li")
            // Custom slashdot tags.
            // These could be rewritten in the sanitizer using an ElementPolicy.
            .allowElements("quote", "ecode").toFactory();

    /**
     * Sanitizes a HTML text
     * 
     * @param html
     * @return
     * @throws IOException
     */
    public static String sanitize(String html) {
        StringBuilder result = new StringBuilder();

        // Create the renderer
        HtmlStreamRenderer renderer = HtmlStreamRenderer.create(result,
                ex -> Logger.getLogger(SanitizeUtils.class.getName()).log(Level.SEVERE, ex.getMessage(), ex), x -> {
                    throw new AssertionError(x);
                });

        // Sanitize the text
        HtmlSanitizer.sanitize(html, POLICY_DEFINITION.apply(renderer));

        // Return the sanitized text
        return html;
    }

}