package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.entity.PqsEntryCheckItemEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 工单检验项Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsEntryCheckItemMapper extends IBaseMapper<PqsEntryCheckItemEntity> {

    /**
     * 获取检查项
     *
     * @param templateId
     * @return
     */
    List<PqsEntryCheckItemEntity> getNewPqsEntryCheckItem(Long templateId);
}