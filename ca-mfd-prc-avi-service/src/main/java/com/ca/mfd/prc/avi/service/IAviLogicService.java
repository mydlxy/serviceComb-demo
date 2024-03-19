package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.dto.AviStationDTO;
import com.ca.mfd.prc.avi.dto.TpsCodeSanDTO;

/**
 * AVI 过点相关操作
 *
 * @author banny.luo
 * @since 1.0.0 2023-04-06
 */
public interface IAviLogicService {
    /**
     * 获取Avi站点数据
     *
     * @param ip ip地址
     * @return 获取一个Avi过点实体
     */
    AviStationDTO getAviStationInfo(String ip);

    /**
     * 车辆识别码打印
     *
     * @param tpsCode  参数TPS 编码
     * @param shopCode 参数车间code
     */
    void tpsCodePrint(String tpsCode, String shopCode);

    /**
     * 条码扫描过点
     *
     * @param tpsCodeSanInfo 参数列表
     */
    void tpsCodeScan(TpsCodeSanDTO tpsCodeSanInfo);
}
