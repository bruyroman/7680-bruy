package ru.cft.focusstart;

import java.io.UnsupportedEncodingException;

public class CharsetConverter {

    private CharsetConverter() {}

    public static String cp1251ToUtf8(String s) {
        try {
            return new String(s.getBytes("CP1251"), "UTF8");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }

    public static String utf8ToCp1251(String s) {
        try {
            return new String(s.getBytes("UTF8"), "CP1251");
        } catch (UnsupportedEncodingException e) {
            return s;
        }
    }
}
