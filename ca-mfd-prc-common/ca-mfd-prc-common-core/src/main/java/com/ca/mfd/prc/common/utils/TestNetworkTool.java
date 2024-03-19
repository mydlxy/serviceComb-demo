package com.ca.mfd.prc.common.utils;

import java.io.IOException;
import java.net.InetAddress;

/**
 * TestNetworkTool
 *
 * @author inkelink
 * @date 2023-08-17
 */
public class TestNetworkTool {
    public boolean ping(String ipAddress) throws IOException {
        //超时应该在3钞以上
        int timeOut = 3000;
        // 当返回值是true时，说明host是可用的，false则不可
        return InetAddress.getByName(ipAddress).isReachable(timeOut);
    }
}
