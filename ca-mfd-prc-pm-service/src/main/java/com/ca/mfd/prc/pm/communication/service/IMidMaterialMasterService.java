package com.ca.mfd.prc.pm.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;

import java.util.List;

/**
 *
 * @Description: 物料主数据服务
 * @author inkelink
 * @date 2023年10月24日
 * @变更说明 BY inkelink At 2023年10月24日
 */
public interface IMidMaterialMasterService extends ICrudService<MidMaterialMasterEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidMaterialMasterEntity> getAllDatas();

    /**
     * 获取物料主数据
     */
    void receive();

    /**
     * 同步处理逻辑
     */
    void excute(String logid);

    /**
     * 根据日志ID获取数据
     * @param logid
     * @return
     */
    List<MidMaterialMasterEntity> getListByLog(Long logid);
}