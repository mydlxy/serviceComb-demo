package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidColorBaseEntity;

import java.util.List;

/**
 *
 * @Description: 颜色代码库中间表服务
 * @author inkelink
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
public interface IMidColorBaseService extends ICrudService<MidColorBaseEntity> {


    /**
     * 获取颜色
     *
     * @param colorCode
     * @return
     */
    List<MidColorBaseEntity> getByClorCode(String colorCode);

    /**
     * 获取全部数据
     * @return
     */
    List<MidColorBaseEntity> getAllDatas();
    /**
     * 获取颜色代码库
     */
    void receive();
}