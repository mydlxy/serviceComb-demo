package com.ca.mfd.prc.pps.service;

/**
 * PpsToEprService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-18
 */
public interface IPpsToEprService {

    /**
     * 获取erp bom 信息
     *
     * @param materialNo
     * @return
     */
    String getErpBomVersion(String materialNo);
}