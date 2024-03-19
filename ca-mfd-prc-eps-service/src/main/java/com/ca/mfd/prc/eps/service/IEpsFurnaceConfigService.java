package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsFurnaceConfigEntity;

/**
 *
 * @Description: 熔化炉配置服务
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
public interface IEpsFurnaceConfigService extends ICrudService<EpsFurnaceConfigEntity> {

    /**
     * 获取
     *
     * @param furnaceNo
     * @return
     */
    EpsFurnaceConfigEntity getFirstByFurnaceNo(String furnaceNo);
}