package com.epam.java.rt.lab.dao.query;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * service-ms
 */
class SqlAdapter {

    static String convertName(String name) {
        Pattern pattern = Pattern.compile("[A-Z]");
        Matcher matcher = pattern.matcher(name);
        int startIndex = name.indexOf(".");
        if (startIndex < 0) startIndex = 0;
        while (matcher.find(startIndex)) {
            name = name.substring(0, matcher.start()).concat("_")
                    .concat(name.substring(matcher.start(), matcher.end()).toLowerCase())
                    .concat(name.substring(matcher.end()));
            matcher = pattern.matcher(name);
        }
        return name;
    }


}
