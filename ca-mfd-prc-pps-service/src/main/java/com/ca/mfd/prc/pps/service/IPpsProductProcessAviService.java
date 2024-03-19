package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.remote.app.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 工艺路径详细
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPpsProductProcessAviService extends ICrudService<PpsProductProcessAviEntity> {

    /**
     * 工艺路径详细
     *
     * @param productProcessId
     * @return List<PpsProductProcessAviEntity>
     */
    List<PpsProductProcessAviEntity> getByProductProcessId(Long productProcessId);

    /**
     * 获取AVI站点已设置列表
     *
     * @param productProcessId
     * @return
     */
    List<PpsProductProcessAviEntity> getSetAviInfos(Long productProcessId);

    /**
     * 获取未设置的AVI点
     *
     * @param productProcessId
     * @return
     */
    List<AviInfoDTO> getNoSetAviInfo(Long productProcessId);
}