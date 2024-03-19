package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsQgCheckListEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG必检项目服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQgCheckListService extends ICrudService<PqsQgCheckListEntity> {

    /**
     * 获取所有的组件信息
     *
     * @return
     */
    List<PqsQgCheckListEntity> getAllDatas();
}