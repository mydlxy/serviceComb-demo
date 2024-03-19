package com.ca.mfd.prc.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 网络工具
 *
 * @author inkelink
 * @date 2023年4月4日
 * @变更说明 BY eric.zhou At 2023年4月4日
 */
public class NetworkUtils {

    private NetworkUtils(){}
    static Pattern pattern = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);

    public static boolean ping(String ipAddress) throws IOException {
        int timeOut = 3000;
        return InetAddress.getByName(ipAddress).isReachable(timeOut);
    }

    @SuppressWarnings("unused")
    public static boolean ping(String ipAddress, int pingTimes, int timeOut) {
        Runtime r = Runtime.getRuntime();
        // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;

        // Linux命令如下:ping -c  [pingTimes]  -w  [timeOut] [ipAddress]

        try(BufferedReader in = new BufferedReader(new InputStreamReader(r.exec(pingCommand).getInputStream()))) {
            int connectedCount = 0;
            String line;
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
            return connectedCount == pingTimes;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     */
    private static int getCheckResult(String line) {
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return 1;
        }
        return 0;
    }

}
