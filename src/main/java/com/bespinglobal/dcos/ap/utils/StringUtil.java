package com.bespinglobal.dcos.ap.utils;

import org.springframework.boot.json.JacksonJsonParser;

import java.util.Arrays;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * Project : Auto-Provisioning
 * Class : com.bespinglobal.dcos.ic.utils.StringUtil
 * Version : 2019.07.25 v0.1
 * Created by taehyoung.yim on 2019-07-24.
 * *** 저작권 주의 ***
 */
public class StringUtil {

    /**
     * Method that splits a string with delimited fields into an array of field strings.
     *
     * @param str - the <code>String</code> that is split by the delimiter.
     * @param delimeter - the delimiter string
     */
    public static String[] split(String str, String delimeter) {
        return split(str, delimeter, false);
    }

    /**
     * Method that splits a string with delimited fields into an array of field strings.
     *
     * @param str - the <code>String</code> that is split by the delimiter.
     * @param delim - the delimiter string
     * @param trim - If it is true, trims the split string.
     */
    public static String[] split(String str, String delim, boolean trim) {
        return split(str, delim, trim, 0);
    }

    /**
     * Method that splits a string with delimited fields into an array of field strings.
     *
     * @param str - the <code>String</code> that is split by the delimiter.
     * @param delim - the delimiter string
     * @param trim - If it is true, trims the split string.
     * @param fixedLen - fixed length of the array
     */
    public static String[] split(String str, String delim, boolean trim, int fixedLen) {
        if (str == null || str.length() == 0) {
            if (fixedLen <= 0)
                return null;
            else
                return new String[fixedLen];
        }

        StringTokenizer stringTokenizer = new StringTokenizer(str, delim);
        int count = stringTokenizer.countTokens();
        String[] result = new String[fixedLen > 0 ? fixedLen : count == 0 ? 1 : count];

        result[0] = str;

        for (int i = 0; i < count; i++) {
            if (fixedLen > 0 && i >= fixedLen)
                break;

            if (trim)
                result[i] = stringTokenizer.nextToken().trim();
            else
                result[i] = stringTokenizer.nextToken();
        }
        return result;
    }

    /**
     * Method that replace some string which matched by pattern with replacements
     *
     * @param target - target String
     * @param pattern - pattern in target string
     * @param replacements - Strings to be replaced
     * @return - replaced target string
     */
    public static String replace(String target, String pattern, String... replacements) {
        StringBuilder sb = new StringBuilder();

        int pos = 0;
        int idx = target.indexOf(pattern);
        int patLen = pattern.length();
        int replaceIdx = 0;
        int replaceMaxIdx = replacements.length - 1;

        while (idx >= 0) {
            sb.append(target.substring(pos, idx));
            sb.append(replacements[replaceIdx]);

            pos = idx + patLen;
            idx = target.indexOf(pattern, pos);

            if (replaceIdx < replaceMaxIdx)
                replaceIdx++;
        }
        sb.append(target.substring(pos));
        return sb.toString();
    }

    public static String asString(String[] args) {
        if (args == null)
            return "";
        return Arrays.toString(args);
    }

    public static Map<String, Object> jsonStringToMap(String s) {
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(s);
    }
}
