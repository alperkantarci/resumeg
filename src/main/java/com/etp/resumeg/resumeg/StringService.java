package com.etp.resumeg.resumeg;

public class StringService {

    protected static boolean testAllUpperCase(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= 97 && c <= 122) {
                return false;
            }
        }
        // str.charAt(index)
        return true;
    }
}
