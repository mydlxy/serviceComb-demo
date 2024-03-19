package com.ca.mfd.prc.core.report.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.report.entity.RptFileEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 报表文件存储服务
 * @date 2023年09月23日
 * @变更说明 BY inkelink At 2023年09月23日
 */
public interface IRptFileService extends ICrudService<RptFileEntity> {

    /**
     * 关键字查询
     *
     * @param keys 关键字
     * @return 列表
     */
    List<RptFileEntity> getListByName(String keys);

    /**
     * 根据配置名查询
     *
     * @param displayName
     * @return
     */
    String getByDisplayName(String displayName);
}