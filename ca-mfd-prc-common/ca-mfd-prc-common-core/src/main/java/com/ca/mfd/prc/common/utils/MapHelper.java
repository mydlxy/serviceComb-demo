package com.ca.mfd.prc.common.utils;

import cn.hutool.json.JSONException;
import cn.hutool.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * map工具类
 *
 * @author inkelink ERIC.ZHOU
 */
public class MapHelper {

    public static String getJsonString(JSONObject rsp) {
        if (rsp == null) {
            return "";
        }
        return rsp.toString();
    }

    public static String getJsonKeyString(JSONObject rsp, String key) throws JSONException {
        if (rsp == null || StringUtils.isBlank(key) || rsp.get(key) == null) {
            return "";
        }
        return rsp.get(key).toString();
    }

    public static String getMpValue(Map<String, String> map, String key, Object defaultVal) {
        String mpVal = "";
        if (map == null || map.size() == 0 || StringUtils.isBlank(key) || !map.containsKey(key)) {
            mpVal = "";
        } else {
            mpVal = map.get(key);
        }
        if (StringUtils.isBlank(mpVal)) {
            mpVal = Objects.isNull(defaultVal) ? "" : defaultVal.toString();
        }
        return mpVal;
    }

    public static Object getValue(Map<String, Object> map, String key) {
        if (map == null || map.size() == 0 || StringUtils.isBlank(key) || !map.containsKey(key)) {
            return null;
        }
        return map.get(key);
    }

    public static String getValueString(Map<String, Object> map, String key) {
        Object sval = getValue(map, key);
        if (sval != null) {
            return sval.toString().trim();
        }
        return "";
    }

    public static String getValueString(Map<String, Object> map, String key, String defaultVal) {
        String sval = getValueString(map, key);
        if (StringUtils.isBlank(sval)) {
            return defaultVal;
        }
        return sval;
    }

    public static Integer getValueInt(Map<String, Object> map, String key) {
        String sval = getValueString(map, key);
        if (StringUtils.isNotBlank(sval)) {
            return Integer.parseInt(sval);
        }
        return null;
    }

    public static Double getValueDouble(Map<String, Object> map, String key) {
        String sval = getValueString(map, key);
        if (StringUtils.isNotBlank(sval)) {
            return Double.parseDouble(sval);
        }
        return null;
    }
}
