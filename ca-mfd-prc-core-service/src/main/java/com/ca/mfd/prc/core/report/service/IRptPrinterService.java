package com.ca.mfd.prc.core.report.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.report.dto.EditStatusDto;
import com.ca.mfd.prc.core.report.entity.RptPrinterEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 报表打印机服务
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
public interface IRptPrinterService extends ICrudService<RptPrinterEntity> {
    /**
     * 查询所有数据
     *
     * @return 数据列表
     */
    List<RptPrinterEntity> getListAll();

    /**
     * 根据ip集合查询
     *
     * @param ips ip集合
     * @return 列表
     */
    List<RptPrinterEntity> getListByIps(List<String> ips);

    /**
     * 根据ID 更新状态
     *
     * @param ids         主键集合
     * @param printStatus 状态
     */
    void updatePrintStatusByIds(List<Long> ids, String printStatus);

    /**
     * 更新打印状态
     *
     * @param printStatus 状态
     * @param id          主键
     */
    void updateQueuePrintStatus(String printStatus, Long id);

    /**
     * 根据打印代码查询报表
     *
     * @param bizCode 打印代码
     * @return 报表
     */
    RptPrinterEntity getInfoByBizCode(String bizCode);

    /**
     * 供iot打印调用
     * @param dto
     */
    void editStatus(EditStatusDto dto);

    List<RptPrinterEntity> getData(String bizCode);
}