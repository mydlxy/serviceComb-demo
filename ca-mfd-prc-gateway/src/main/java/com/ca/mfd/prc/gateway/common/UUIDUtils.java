//package com.ca.mfd.prc.common;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.UUID;
//
//
///**
// * UUID工具类
// *
// * @author inkelink ERIC.ZHOU
// */
//public class UUIDUtils {
//    private static final String EMPTY_ID = "00000000-0000-0000-0000-000000000000";
//    private static final String EMPTY_ID2 = "11111111-1111-1111-1111-111111111111";
//
//    /**
//     * 生成GUID.
//     */
//    public static String getGuid() {
//        return UUID.randomUUID().toString();
//    }
//
//    public static String newGuid() {
//        return UUID.randomUUID().toString();
//    }
//
//    /**
//     * 生成GUID.
//     */
//    public static String getGuidShort() {
//        return UUID.randomUUID().toString().replace("-", "");
//    }
//
//    /**
//     * 判断guid是否为空
//     */
//    public static boolean isGuidEmpty(String id) {
//        if (StringUtils.isBlank(id)) {
//            return true;
//        }
//        return StringUtils.endsWithIgnoreCase(id, EMPTY_ID);
//    }
//
//    /**
//     * 判断guid是否为空
//     */
//    public static boolean isGuidEmptyAll(String id) {
//        if (isGuidEmpty(id)) {
//            return true;
//        }
//        return StringUtils.endsWithIgnoreCase(id, EMPTY_ID2);
//    }
//
//    public static String getEmpty() {
//        return EMPTY_ID;
//    }
//
//}
