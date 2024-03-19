package com.ca.mfd.prc.pps.communication.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.communication.dto.SoftwareBomListDto;
import com.ca.mfd.prc.pps.communication.entity.MidSoftwareBomListEntity;

import java.util.List;

/**
 *
 * @Description: 单车软件清单服务
 * @author inkelink
 * @date 2023年11月23日
 * @变更说明 BY inkelink At 2023年11月23日
 */
public interface IMidSoftwareBomListService extends ICrudService<MidSoftwareBomListEntity> {

    /**
     * 根据整车物料号获取软件清单
     * @param materialNo
     * @return
     */
    List<SoftwareBomListDto> getSoftBom(String materialNo,String effectiveDate);

    /**
     * 获取全部数据
     * @return
     */
    List<MidSoftwareBomListEntity> getAllDatas();
}