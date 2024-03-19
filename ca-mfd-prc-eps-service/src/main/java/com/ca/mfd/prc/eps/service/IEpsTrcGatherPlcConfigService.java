package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.CollectBarcodePara;
import com.ca.mfd.prc.eps.entity.EpsTrcGatherPlcConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: PLC收集追溯条码配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsTrcGatherPlcConfigService extends ICrudService<EpsTrcGatherPlcConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsTrcGatherPlcConfigEntity> getAllDatas();

    /**
     * 采集条码
     *
     * @param para
     */
    void collectBarcode(CollectBarcodePara para);
}