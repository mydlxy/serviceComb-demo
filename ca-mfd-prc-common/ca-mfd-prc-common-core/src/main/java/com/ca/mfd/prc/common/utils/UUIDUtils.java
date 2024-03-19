package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.SequentialGuidType;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;


/**
 * UUID工具类
 *
 * @author inkelink ERIC.ZHOU
 */
public class UUIDUtils {

    static final String EMPTY_GUID =   "00000000-0000-0000-0000-000000000000";

    /**
     * 生成GUID.
     */
    public static String getGuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成新guid
     *
     * @return guid
     */
    public static String newGuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成GUID.
     */
    public static String getGuidShort() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 判断guid是否为空
     */
    public static boolean isGuidEmpty(String id) {
        if (StringUtils.isBlank(id)) {
            return true;
        }
        return StringUtils.endsWithIgnoreCase(id, Constant.EMPTY_ID);
    }

    /**
     * 判断guid是否为空
     */
    public static boolean isGuidEmptyAll(String id) {
        if (isGuidEmpty(id)) {
            return true;
        }
        return StringUtils.endsWithIgnoreCase(id, Constant.EMPTY_ID2);
    }

    /**
     * 生成空guid
     *
     * @return guid
     */
    public static String getEmpty() {
        return EMPTY_GUID;
    }

    /**
     * 生成随机数的byte
     *
     * @return byte
     */
    public static byte[] getUuidBytes() {
        UUID uuid = UUID.randomUUID();
        long hi = uuid.getMostSignificantBits();
        long lo = uuid.getLeastSignificantBits();
        return ByteBuffer.allocate(16).putLong(hi).putLong(lo).array();
    }


    /**
     * 生成GUID.
     *
     * @return
     */
    public static String generateGuid() {
        byte[] array = getUuidBytes();
        Date dateTime = DateUtils.stringToDate("1900-01-01", DateUtils.DATE_PATTERN);
        Date now = new Date();
        long tickDays = DateUtils.getTwoDateDays(now, dateTime);
        byte[] bytes = ConvertUtils.getLongBytes(tickDays);
        //TimeSpan timeOfDay = now.TimeOfDay;
        long totalMilliseconds = DateUtils.parse(DateUtils.format(now, DateUtils.DATE_PATTERN)
                , DateUtils.DATE_PATTERN).getTime();
        byte[] bytes2 = ConvertUtils.getLongBytes((long) (totalMilliseconds / 3.333333));
        ArrayUtils.reverse(bytes);
        ArrayUtils.reverse(bytes2);
        System.arraycopy(bytes, bytes.length - 2, array, array.length - 6, 2);
        System.arraycopy(bytes2, bytes2.length - 4, array, array.length - 4, 4);
        return UUID.nameUUIDFromBytes(array).toString();
    }

    /**
     * 判断 CPU Endian 是否为 Little
     *
     * @return 判断结果
     */
    private static boolean isLittleEndian() {
        return ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN;
    }


    /**
     * 生成随机数的byte
     *
     * @param guidType
     * @return guid
     */
    public static String create(SequentialGuidType guidType) {
        byte[] randomBytes = new byte[10];
        ThreadLocalRandom.current().nextBytes(randomBytes);
        byte[] bytes = ConvertUtils.getLongBytes(System.currentTimeMillis());
        boolean isLittleEndiann = isLittleEndian();
        if (isLittleEndiann) {
            ArrayUtils.reverse(bytes);
        }
        byte[] array = new byte[16];
        switch (guidType) {
            case SequentialAsString:
            case SequentialAsBinary: {
                System.arraycopy(bytes, 2, array, 0, 6);
                System.arraycopy(randomBytes, 0, array, 6, 10);
                if (guidType == SequentialGuidType.SequentialAsString && isLittleEndiann) {
                    ArrayUtils.reverse(array, 0, 4);
                    ArrayUtils.reverse(array, 4, 2);
                }
            }
            break;
            case SequentialAtEnd: {
                System.arraycopy(randomBytes, 0, array, 0, 10);
                System.arraycopy(bytes, 2, array, 10, 6);
            }
            break;
            default:
                break;
        }
        return UUID.nameUUIDFromBytes(array).toString();
    }

}
