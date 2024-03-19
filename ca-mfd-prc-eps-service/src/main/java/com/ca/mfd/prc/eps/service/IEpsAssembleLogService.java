package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsAssembleLogEntity;

import java.util.List;

/**
 * 装配单日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsAssembleLogService extends ICrudService<EpsAssembleLogEntity> {
    /**
     * 获取装配指示详细数据
     *
     * @param sn
     * @return List<EpsAssembleLogEntity>
     */
    List<EpsAssembleLogEntity> getAssembleLogData(String sn);


    /**
     * 添加装配单日志
     *
     * @param sn
     * @param tplCode
     */
    void addInsertLog(String sn, String tplCode);
}