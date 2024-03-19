package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidPmCountryEntity;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工厂服务
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
public interface IPmOrganizationService extends ICrudService<PmOrganizationEntity> {

    PmOrganizationEntity getPmOrganization(Long id, Boolean aFalse);

    /**
     * 获取工厂编码
     *
     * @return
     */
    String getCurrentOrgCode();

    /**
     * 接收国家代码数据
     * @param datas
     */
    void receiveData(List<MidPmCountryEntity> datas);

    /**
     * 获取所有数据
     * @return
     */
    List<PmOrganizationEntity> getAllDatas();
}