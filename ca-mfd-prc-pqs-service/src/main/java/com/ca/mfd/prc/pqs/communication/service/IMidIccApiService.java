package com.ca.mfd.prc.pqs.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.communication.entity.MidIccApiEntity;

import java.util.List;

/**
 *
 * @Description: ICC接口中间表服务
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidIccApiService extends ICrudService<MidIccApiEntity> {

    /**
     * 获取全部数据
     * @return
     */
    List<MidIccApiEntity> getAllDatas();

    /**
     * 根据记录表id查询数据
     * @param id
     * @return
     */
    List<MidIccApiEntity> getListByLog(Long id);
}