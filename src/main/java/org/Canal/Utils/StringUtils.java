package org.Canal.Utils;

public class StringUtils {
    public static int countOccurrences(String str, String subStr) {
        if (str == null || subStr == null || subStr.isEmpty()) {
            return 0;
        }
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(subStr, index)) != -1) {
            count++;
            index += subStr.length(); // Move index forward to avoid infinite loop
        }
        return count;
    }
}
