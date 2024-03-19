package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviCutLogEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * AVICUT记录表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviCutLogService extends ICrudService<AviCutLogEntity> {

    /**
     * 根据站点ID和状态查询CUT记录
     *
     * @param aviId  站点ID
     * @param status 状态
     * @return 返回CUT记录
     */
    List<AviCutLogEntity> getAviCutLogListByAviId(String aviId, int status);
}