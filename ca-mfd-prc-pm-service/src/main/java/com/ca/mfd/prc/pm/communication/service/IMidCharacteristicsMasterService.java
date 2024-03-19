package com.ca.mfd.prc.pm.communication.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidCharacteristicsMasterEntity;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @Description: 特征主数据服务
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
public interface IMidCharacteristicsMasterService extends ICrudService<MidCharacteristicsMasterEntity> {

    /**
     * 获取所有数据
     * @return
     */
    List<MidCharacteristicsMasterEntity> getAllDatas();

    /**
     * 获取特征主数据
     * @param startDate
     * @param endDate
     */
    void receive(String startDate, String endDate);

    /**
     * 处理特征主数据
     */
    void excute(String logid);

    /**
     * 根据日志ID获取中间表数据
     * @param logid
     * @return
     */
    List<MidCharacteristicsMasterEntity> getListByLog(Long logid);
}