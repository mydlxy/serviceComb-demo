package com.ca.mfd.prc.pqs.mapper;

import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.pqs.dto.GetAnomalyByQualityGateBlankIdInfo;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateAnomalyEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG检验项-缺陷Mapper
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Mapper
public interface IPqsQualityGateAnomalyMapper extends IBaseMapper<PqsQualityGateAnomalyEntity> {

    /**
     * 获取
     *
     * @param qualityGateBlankId
     * @param tpsCode
     * @return
     */
    List<GetAnomalyByQualityGateBlankIdInfo> getAnomalyByQualityGateBlankId(String qualityGateBlankId, String tpsCode);
}