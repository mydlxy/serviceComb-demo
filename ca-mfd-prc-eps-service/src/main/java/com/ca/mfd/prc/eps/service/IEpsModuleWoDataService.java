package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.CollectModuleWoPara;
import com.ca.mfd.prc.eps.entity.EpsModuleWoDataEntity;

import java.util.List;

/**
 *
 * @Description: 模组工艺数据服务
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
public interface IEpsModuleWoDataService extends ICrudService<EpsModuleWoDataEntity> {

    /**
     * 收集预成组检测数据
     *
     * @param para
     */
    void collectModuleWo(CollectModuleWoPara para);

    /**
     * 获取检测数据
     *
     * @param sn
     * @return
     */
    List<EpsModuleWoDataEntity> getModuleWoBySn(String sn);
}