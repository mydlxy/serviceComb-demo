package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.dto.TextAndValueMappingDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmBopGroupEntity;

import java.util.List;

/**
 *
 * @Description: 分组配置服务
 * @author inkelink
 * @date 2023年11月24日
 * @变更说明 BY inkelink At 2023年11月24日
 */
public interface IPmBopGroupService extends ICrudService<PmBopGroupEntity> {

    List<TextAndValueMappingDTO> getPmCombo(String workshopCode);

}