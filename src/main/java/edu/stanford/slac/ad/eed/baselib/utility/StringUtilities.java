package edu.stanford.slac.ad.eed.baselib.utility;


import java.text.Normalizer;
import java.util.Objects;

public class StringUtilities {
    /**
     * Normalize the input string to be all in lowercase and
     * to be composed only by ascii character and at the end
     * replace the space with the dash
     * @param name is the string to convert
     * @param stringToReplace this is the string to replace
     * @param targetString this is the string used for the replace operation
     * @return the normalized string
     */
    public static String normalizeStringWithReplace(String name, String stringToReplace, String targetString) {
        return Normalizer
                .normalize(
                        name.trim().toLowerCase(),
                        Normalizer.Form.NFD
                ).replaceAll(
                        "[^\\p{ASCII}]",
                        "")
                .replaceAll(
                        Objects.requireNonNull(stringToReplace),
                        Objects.requireNonNull(targetString)
                );
    }
}
