package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.dto.RiskProductFilterDto;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 质量围堵Mapper
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Mapper
public interface IPqsDefectAnomalyRiskMapper extends IBaseMapper<PqsDefectAnomalyRiskEntity> {

    List<RiskProductFilterDto> getProductList(Map<String, Object> maps);
}