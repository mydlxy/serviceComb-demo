package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.dto.BomConfigDto;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomConfigEntity;

import java.util.List;

/**
 *
 * @Description: 单车配置字服务
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
public interface IMidSoftwareBomConfigService extends ICrudService<MidSoftwareBomConfigEntity> {

    /**
     * 获取所有数据
     * @return
     */
    List<MidSoftwareBomConfigEntity> getAllDatas();

    /**
     * 获取配置字信息
     * @param materialNo
     * @return
     */
    List<BomConfigDto> getBomConfig(String materialNo,String effectiveDate);

}