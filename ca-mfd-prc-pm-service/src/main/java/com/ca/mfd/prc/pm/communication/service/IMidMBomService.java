package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidMBomEntity;

import java.util.List;

/**
 *
 * @Description: 制造域BOM服务
 * @author inkelink
 * @date 2023年12月08日
 * @变更说明 BY inkelink At 2023年12月08日
 */
public interface IMidMBomService extends ICrudService<MidMBomEntity> {

    /**
     * 接收MBOM数据
     * @param orgCode
     * @param specifyDate
     * @param bomRoom
     */
    int receive(String orgCode, String specifyDate, String bomRoom);

    /**
     * 处理数据到自己系统
     */
    void excute(String logid);

    /**
     * 根据记录表id查询数据
     * @param logid
     * @return
     */
    List<MidMBomEntity> getListByLog(Long logid);

    /**
     * 获取全部数据
     * @return
     */
    List<MidMBomEntity> getAllDatas();
}