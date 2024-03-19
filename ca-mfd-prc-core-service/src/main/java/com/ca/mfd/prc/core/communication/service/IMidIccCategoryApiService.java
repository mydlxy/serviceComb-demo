package com.ca.mfd.prc.core.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.communication.entity.MidIccCategoryApiEntity;

import java.util.List;

/**
 *
 * @Description: ICC分类接口中间表服务
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
public interface IMidIccCategoryApiService extends ICrudService<MidIccCategoryApiEntity> {
    /**
     * 获取全部数据
     * @return
     */
    List<MidIccCategoryApiEntity> getAllDatas();
}