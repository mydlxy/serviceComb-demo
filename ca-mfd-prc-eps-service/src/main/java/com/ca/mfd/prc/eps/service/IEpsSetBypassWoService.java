package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsSetBypassWoEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 设置进工位BYPASS工艺服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IEpsSetBypassWoService extends ICrudService<EpsSetBypassWoEntity> {
    /**
     * 获取所有的数据
     *
     * @return
     */
    List<EpsSetBypassWoEntity> getAllDatas();


    /**
     * 获取工位bypass的工艺列表
     *
     * @param workstationCode
     * @param wocodes
     * @return
     */
    List<String> getBypassWoCodes(String workstationCode, List<String> wocodes);
}