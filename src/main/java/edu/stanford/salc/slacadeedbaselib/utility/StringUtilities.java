package edu.stanford.salc.slacadeedbaselib.utility;


import java.text.Normalizer;

public class StringUtilities {
    static public String tokenNameNormalization(String tagName) {
        return Normalizer
                .normalize(
                        tagName.trim(),
                        Normalizer.Form.NFD
                ).replaceAll("[^\\p{ASCII}]", "");
    }

    public static String authenticationTokenNormalization(String name) {
        return Normalizer
                .normalize(
                        name.trim().toLowerCase(),
                        Normalizer.Form.NFD
                ).replaceAll("[^\\p{ASCII}]", "")
                .replaceAll(" ", "-");
    }
}
