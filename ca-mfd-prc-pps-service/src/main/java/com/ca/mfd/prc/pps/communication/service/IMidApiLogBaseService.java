package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.entity.MidApiLogEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 接口记录表服务
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidApiLogBaseService extends ICrudService<MidApiLogEntity> {
    /**
     * 获取需要处理的日志数据
     *
     * @param apitype
     * @return
     */
    List<MidApiLogEntity> getDoList(String apitype, Long logid);
}