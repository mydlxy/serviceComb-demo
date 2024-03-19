package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: 检验模板管理Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsInspectionTemplateMapper extends IBaseMapper<PqsInspectionTemplateEntity> {

    /**
     * 获取excel获取的数据
     *
     * @return
     */
    List<PqsInspectionTemplateEntity> getExcelData();

    /**
     * 获取Excel数据MA
     * @return
     */
    List<PqsInspectionTemplateEntity> getExcelDataMa();
}