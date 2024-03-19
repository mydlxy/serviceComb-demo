package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.dto.PqsInspectionTemplateItemDto;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 检验模板-项目Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsInspectionTemplateItemMapper extends IBaseMapper<PqsInspectionTemplateItemEntity> {
    /**
     * 根据模板列表
     *
     * @param templateId
     * @return
     */
    List<PqsInspectionTemplateItemDto> getTempalteDetail(String templateId);
}