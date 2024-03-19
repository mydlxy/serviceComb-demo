package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.MaintainDataParaInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkStationListInfo;
import com.ca.mfd.prc.pqs.dto.WpAnomalyWorkstationParaInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyWpEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 常用缺陷服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDefectAnomalyWpService extends ICrudService<PqsDefectAnomalyWpEntity> {
    /**
     * 维护数据
     *
     * @param info
     */
    void maintainData(MaintainDataParaInfo info);

    /**
     * 获取工位信息
     *
     * @param info
     * @return
     */
    PageData<WpAnomalyWorkStationListInfo> getWorkStationList(WpAnomalyWorkstationParaInfo info);

    /**
     * 获取所有常用缺陷
     *
     * @return
     */
    List<PqsDefectAnomalyWpEntity> getAllDatas();
}