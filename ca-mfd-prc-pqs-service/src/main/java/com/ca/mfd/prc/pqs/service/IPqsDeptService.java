package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.entity.PqsDeptEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 责任部门配置服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDeptService extends ICrudService<PqsDeptEntity> {

    /**
     * 获取责任部门配置数据
     *
     * @return
     */
    List<PqsDeptEntity> getAllDatas();

    /**
     * getComboList
     *
     * @return
     */
    List<ComboInfoDTO> getComboList();
}