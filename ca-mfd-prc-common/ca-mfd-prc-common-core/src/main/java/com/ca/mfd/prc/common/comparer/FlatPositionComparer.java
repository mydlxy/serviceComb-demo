package com.ca.mfd.prc.common.comparer;

import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.Objects;

import static java.lang.Integer.parseInt;

/**
 * FlatPositionComparer
 *
 * @author inkelink
 * @date 2023-08-17
 */
public class FlatPositionComparer implements Comparator<String> {

    private static final int NORMALIZE_KNOWN_PARTITIONS_LEN = 5;
    private static final String NORMALIZE_KNOWN_PARTITIONS_BEFORE = "before";
    private static final String NORMALIZE_KNOWN_PARTITIONS_AFTER = "after";

    /**
     * 不区分大小写比较两个字符串。
     *
     * @param x 第1个字符串
     * @param y 第2个字符串
     *以下是"compare"方法开始的调用链路和每个调用方法的功能：
     *<p>
     *1. `compare(String x, String y)`方法是`Comparator`接口的实现方法，用于比较两个字符串的扁平位置。
     *- 调用`normalizeString(String str)`方法对输入字符串进行格式化处理。
     *- 调用`splitString(String str)`方法将字符串按特定分隔符拆分为数组。
     *- 调用`compareParts(String[] xParts, String[] yParts)`方法比较两个字符串的各个部分。
     *- 如果`compareParts`方法返回非零结果，则直接返回该结果。
     *- 否则，调用`compareLength(String[] xParts, String[] yParts)`方法比较字符串的长度。
     *<p>
     *2. `normalizeString(String str)`方法用于格式化字符串，去除空格并处理特殊情况。
     *- 如果输入字符串为`null`，返回一个已知分区的特定字符串。
     *- 调用`trim(String str)`方法去除字符串两端的空格。
     *- 如果字符串为空，则返回"0"。
     *- 否则，调用`trim(String str)`方法去除字符串两端的空格。
     *<p>
     *3. `splitString(String str)`方法根据特定的分隔符将字符串拆分为字符串数组。
     *<p>
     *4. `compareParts(String[] xParts, String[] yParts)`方法用于比较两个字符串的各个部分。
     *- 使用循环遍历字符串的各个部分。
     *- 如果`yParts`的长度小于当前索引加1，则说明`y`的部分数量少于`x`，`x`在`y`之后，返回正数。
     *- 调用`parseIntOrKnownPartition(String str)`方法将当前部分解析为整数或处理已知分区。
     *- 如果`xPos`和`yPos`都是整数，则使用`Integer.compare(xPos, yPos)`比较它们的大小。
     *- 否则，调用`compareIgnoreCase(String str1, String str2)`方法不区分大小写比较两个字符串。
     *- 如果比较结果不为零，则直接返回该结果。
     *- 如果循环结束后仍未返回结果，则调用`compareLength(String[] xParts, String[] yParts)`方法比较字符串的长度。
     *<p>
     *5. `parseIntOrKnownPartition(String str)`方法用于解析字符串为整数或处理特定的已知分区。
     *- 如果字符串为空白，则返回-1。
     *- 调用`tryParse(String str)`方法尝试将字符串解析为整数，如果解析成功则返回解析后的整数。
     *- 否则，调用`normalizeKnownPartition(String partition)`方法处理已知分区对应的特定值。
     *<p>
     *6. `normalizeKnownPartition(String partition)`方法用于处理已知分区对应的特定值。
     *- 如果分区字符串长度小于"NORMALIZE_KNOWN_PARTITIONS_LEN"，则调用`parseInt(String str)`方法将其解析为整数。
     *- 如果字符串与"NORMALIZE_KNOWN_PARTITIONS_BEFORE"不区分大小写比较相等，则返回-9999。
     *- 如果字符串与"NORMALIZE_KNOWN_PARTITIONS_AFTER"不区分大小写比较相等，则返回9999。
     *- 否则，调用`parseInt(String str)`方法将其解析为整数。
     *<p>
     *7. `compareIgnoreCase(String str1, String str2)`方法用于不区分大小写比较两个字符串。
     **/
    @Override
    public int compare(String x, String y) {

        if (Objects.equals(x, y)) {
            return 0;
        }
        x = normalizeString(x);
        y = normalizeString(y);

        String[] xParts = splitString(x);

        String[] yParts = splitString(y);
        int result = compareParts(xParts, yParts);
        if (result != 0) {
            return result;
        }

        return compareLength(xParts, yParts);
    }

    private String normalizeString(String str) {
        if (str == null) {
            return NORMALIZE_KNOWN_PARTITIONS_BEFORE;
        }
        str = str.trim();
        if (str.isEmpty()) {
            return "0";
        }
        return trim(str);
    }

    private String[] splitString(String str) {
        return str.split("[.|:]");
    }

    private int compareParts(String[] xParts, String[] yParts) {
        for (int i = 0; i < xParts.length; i++) {
            if (yParts.length < i + 1) {
                return 1;
            }

            int xPos = parseIntOrKnownPartition(xParts[i]);
            int yPos = parseIntOrKnownPartition(yParts[i]);

            int comparisonResult;
            if (xPos != -1 && yPos != -1) {
                comparisonResult = Integer.compare(xPos, yPos);
            } else {
                comparisonResult = compareIgnoreCase(xParts[i], yParts[i]);
            }
            if (comparisonResult != 0) {
                return comparisonResult;
            }
        }

        return compareLength(xParts, yParts);
    }

    private int parseIntOrKnownPartition(String str) {
        if (StringUtils.isBlank(str)) {
            return -1;
        }

        Integer parsedInt = tryParse(str);
        if (parsedInt != null) {
            return parsedInt;
        }

        return normalizeKnownPartition(str);
    }

    private int normalizeKnownPartition(String partition) {
        if (partition.length() < NORMALIZE_KNOWN_PARTITIONS_LEN) {
            return parseInt(partition);
        } else if (StringUtils.compareIgnoreCase(partition, NORMALIZE_KNOWN_PARTITIONS_BEFORE) == 0) {
            return -9999;
        } else if (StringUtils.compareIgnoreCase(partition, NORMALIZE_KNOWN_PARTITIONS_AFTER) == 0) {
            return 9999;
        } else {
            return parseInt(partition);
        }
    }
    private int compareIgnoreCase(String str1, String str2) {
        return StringUtils.compareIgnoreCase(str1, str2);
    }

    private Integer tryParse(String inStr) {
        if (StringUtils.isBlank(inStr)) {
            return null;
        }
        try {
            return parseInt(inStr);
        } catch (NumberFormatException ignored) {
            return null;
        }

    }

    private String trim(String inStr) {
        //':').TrimEnd('.')
        inStr = com.ca.mfd.prc.common.utils.StringUtils.trim(inStr, ":");
        inStr = com.ca.mfd.prc.common.utils.StringUtils.trimEnd(inStr, ".");
        return inStr;
    }

    private int compareLength(String[] xParts, String[] yParts) {
        if (xParts.length < yParts.length) {
            return -1;
        } else {
            return 0;
        }
    }
}