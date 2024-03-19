package com.ca.mfd.prc.avi.host.scheduling;

public interface IEntryReportPassAviService {
    /**
     * 报工触发过点
     *
     * @param orderCategory 分类
     */
    void start(int orderCategory);
}
