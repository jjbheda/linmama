package com.linmama.dinning.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jingkang on 2017/3/10
 */

public class RegexUtils {
    public static String subString(String s) {
        Pattern p = Pattern.compile("\\{.*?\\}");
        Matcher m = p.matcher(s);
        if (m.find()) {
            return m.group(0);
        } else {
            return "";
        }
    }

}
