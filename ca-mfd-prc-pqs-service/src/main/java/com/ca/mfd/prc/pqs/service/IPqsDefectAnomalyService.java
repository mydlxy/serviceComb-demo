package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.BatchAddAnomalyInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 组合缺陷库服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDefectAnomalyService extends ICrudService<PqsDefectAnomalyEntity> {
    /**
     * 批量添加缺陷数据
     *
     * @param list
     */
    void batchAddAnomaly(List<BatchAddAnomalyInfo> list);

    /**
     * 获取缺陷展示数据
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getAnomalyShowList(DefectFilterlParaInfo info);

    /**
     * getAllDatas
     *
     * @return
     */
    List<PqsDefectAnomalyEntity> getAllDatas();

    /**
     * 获取AUDIT缺陷展示数据
     *
     * @return
     */
    List<PqsDefectAnomalyEntity> getAuditAnomalyShowList();

    /**
     * 根据缺陷名称或者缺陷编号获取缺陷列表
     *
     * @param cout           查询limit 数量
     * @param conditionInfos 条件
     * @return 缺陷数据列表
     */
    List<PqsDefectAnomalyEntity> getTopDatasByCondtion(Integer cout, List<ConditionDto> conditionInfos);


    /**
     * 根据缺陷编码查询
     *
     * @param code 缺陷编号
     * @return 实体
     */
    PqsDefectAnomalyEntity getEntityByCode(String code);
}