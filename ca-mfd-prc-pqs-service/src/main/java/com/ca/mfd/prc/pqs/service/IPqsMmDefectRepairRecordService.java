package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.DefectAnomalyParaInfo;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordDto;
import com.ca.mfd.prc.pqs.dto.MmDefectRepairRecordInfo;
import com.ca.mfd.prc.pqs.dto.PpsPlanPartsDto;
import com.ca.mfd.prc.pqs.entity.PqsMmDefectRepairRecordEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 缺陷返修记录表服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsMmDefectRepairRecordService extends ICrudService<PqsMmDefectRepairRecordEntity> {

    /**
     * 获取所有缺陷返修记录
     *
     * @return
     */
    List<PqsMmDefectRepairRecordEntity> getAllDatas();

    /**
     * 根据计划编号，查询生产计划-零部件
     *
     * @param planNo
     * @return
     */
    PpsPlanPartsDto getPpsPlanParts(String planNo);

    /**
     * 保存批次件返修记录
     *
     * @param info
     */
    void savePqsMmDefectRepairRecord(MmDefectRepairRecordInfo info);

    /**
     * 批次件返修记录列表
     *
     * @param info
     * @return
     */
    PageData<MmDefectRepairRecordDto> getMmDefectRepairRecordList(DefectAnomalyParaInfo info);
}