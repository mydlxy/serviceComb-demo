package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.GetAnomalyByQualityGateBlankIdInfo;
import com.ca.mfd.prc.pqs.entity.PqsQualityGateAnomalyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: QG检验项-缺陷服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsQualityGateAnomalyService extends ICrudService<PqsQualityGateAnomalyEntity> {

    /**
     * 获取所有QG检查项-缺陷记录
     *
     * @return
     */
    List<PqsQualityGateAnomalyEntity> getAllDatas();

    /**
     * 获取质量门色块对应的缺陷列表
     *
     * @param qualityGateBlankId 节点ID
     * @param tpsCode            tps编码
     * @return 返回缺陷列表
     */
    List<GetAnomalyByQualityGateBlankIdInfo> getAnomalyByQualityGateBlankId(String qualityGateBlankId, String tpsCode);
}