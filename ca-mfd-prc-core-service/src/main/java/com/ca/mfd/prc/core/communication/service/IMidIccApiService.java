package com.ca.mfd.prc.core.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.communication.dto.ApiResultVo;
import com.ca.mfd.prc.core.communication.dto.IccCategoryDto;
import com.ca.mfd.prc.core.communication.dto.IccDto;
import com.ca.mfd.prc.core.communication.entity.MidIccApiEntity;

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
}