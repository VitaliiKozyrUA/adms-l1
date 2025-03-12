package com.kozyr.adms.l1.utils;

public class DoubleUtils {
    public static double stringToDouble(String str) {
        double num = 0;
        double num2 = 0;
        int idForDot = str.indexOf('.');
        boolean isNeg = false;
        String st;
        int start = 0;
        int end = str.length();

        if (idForDot != -1) {
            st = str.substring(0, idForDot);
            for (int i = str.length() - 1; i >= idForDot + 1; i--) {
                num2 = (num2 + str.charAt(i) - '0') / 10;
            }
        } else {
            st = str;
        }

        if (st.charAt(0) == '-') {
            isNeg = true;
            start++;
        } else if (st.charAt(0) == '+') {
            start++;
        }

        for (int i = start; i < st.length(); i++) {
            if (st.charAt(i) == ',') {
                continue;
            }
            num *= 10;
            num += st.charAt(i) - '0';
        }

        num = num + num2;
        if (isNeg) {
            num = -1 * num;
        }
        return num;
    }
}
