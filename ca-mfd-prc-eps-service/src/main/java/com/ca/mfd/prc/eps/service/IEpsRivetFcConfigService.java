package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.dto.VerifyRivetPara;
import com.ca.mfd.prc.eps.entity.EpsRivetFcConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 铆钉防错配置服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsRivetFcConfigService extends ICrudService<EpsRivetFcConfigEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsRivetFcConfigEntity> getAllDatas();

    /**
     * 校验料口上料
     *
     * @param para
     * @return
     */
    Boolean verifyRivet(VerifyRivetPara para);
}