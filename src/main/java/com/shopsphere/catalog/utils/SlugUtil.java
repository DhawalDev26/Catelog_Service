package com.shopsphere.catalog.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;
import java.util.UUID;

public class SlugUtil {

    private static final Pattern NONLATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");

    public static String generateSlug(String productName) {
        String now = String.valueOf(System.currentTimeMillis());
        String baseSlug = toSlug(productName);
        return baseSlug + "-" + now.substring(now.length() - 5); // short suffix
    }

    private static String toSlug(String input) {
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
