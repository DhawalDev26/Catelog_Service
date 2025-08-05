package com.shopsphere.catalog.utils;

import java.util.Locale;
import java.util.UUID;

public class SKUUtil {

    public static String generateSKU(String productName, String brand) {
        String brandCode = getCode(brand);
        String productCode = getCode(productName);
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return brandCode + "-" + productCode + "-" + uniqueId;
    }

    private static String getCode(String input) {
        return input == null ? "XXX" :
                input.replaceAll("[^A-Za-z0-9]", "")
                     .toUpperCase(Locale.ENGLISH)
                     .substring(0, Math.min(3, input.length()));
    }
}
