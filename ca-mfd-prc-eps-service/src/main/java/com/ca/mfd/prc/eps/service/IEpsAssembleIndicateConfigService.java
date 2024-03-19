package com.ca.mfd.prc.eps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.eps.entity.EpsAssembleIndicateConfigEntity;

import java.util.List;

/**
 * 装配指示配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
public interface IEpsAssembleIndicateConfigService extends ICrudService<EpsAssembleIndicateConfigEntity> {


    /**
     * 获取工位上面的装配指示列表
     *
     * @param sn              产品唯一码
     * @param workstationCode 工位代码
     * @return 列表 装配指示配置
     */
    List<EpsAssembleIndicateConfigEntity> getWorkstationData(String sn, String workstationCode);
}