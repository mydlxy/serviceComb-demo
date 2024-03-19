package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidPmCountryEntity;

import java.util.List;

/**
 *
 * @Description: 国家代码中间表服务
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
public interface IMidPmCountryService extends ICrudService<MidPmCountryEntity> {
    /**
     * 执行数据处理逻辑(考虑异步)
     *
     */
    void excute(String logid);

    /**
     * 获取全部数据
     * @return
     */
    List<MidPmCountryEntity> getAllDatas();

    List<MidPmCountryEntity> getListByLog(Long id);
}