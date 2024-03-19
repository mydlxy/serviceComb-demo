package com.ca.mfd.prc.pps.communication.service;

/**
 * @author inkelink
 * @Description: 自动锁定计划服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IPlanAutoLockService {

    /**
     * 执行数据处理逻辑
     */
    String autolockPartsPlan(Integer orderCategory);

    /**
     * 执行数据处理逻辑
     */
    String excute(Integer status);
}