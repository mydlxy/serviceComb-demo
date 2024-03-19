package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviDefectAnomalyAviEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;

/**
 * @author inkelink
 * @Description: AVI缺陷阻塞配置[作废]服务
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
public interface IAviDefectAnomalyAviService extends ICrudService<AviDefectAnomalyAviEntity> {
    /**
     * 通过aviCode查询
     *
     * @param aviCode 站点Code
     * @return 列表
     */
    List<AviDefectAnomalyAviEntity> getAnomalysByAviId(String aviCode);
}