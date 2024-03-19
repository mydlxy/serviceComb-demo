package com.ca.mfd.prc.common.utils;

/**
 * TimeSpan
 *
 * @author inkelink
 * @date 2023-08-17
 */
public class TimeSpan {
    private long seconds;

    public TimeSpan(long seconds) {
        this.seconds = seconds;
    }

    public int getHours() {
        return (int) (seconds / 3600);
    }

    public int getMinutes() {
        return (int) ((seconds % 3600) / 60);
    }

    public int getSeconds() {
        return (int) (seconds % 60);
    }
}
