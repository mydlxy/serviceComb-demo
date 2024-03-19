package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.MaterialPutPara;
import com.ca.mfd.prc.eps.entity.EpsFurnacePutLogEntity;

/**
 *
 * @Description: 熔化炉投料记录服务
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
public interface IEpsFurnacePutLogService extends ICrudService<EpsFurnacePutLogEntity> {

    /**
     * 放料
     *
     * @param request
     * */
    void putMaterial(MaterialPutPara request);
}