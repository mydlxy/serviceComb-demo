package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.dto.ModuleDetailInfo;
import com.ca.mfd.prc.pps.mapper.IPpsModuleIssueModuleMapper;
import com.ca.mfd.prc.pps.entity.PpsModuleIssueModuleEntity;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueModuleService;
import com.ca.mfd.prc.pps.service.IPpsModuleIssueUnitService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 电池预成组下发模组服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsModuleIssueModuleServiceImpl extends AbstractCrudServiceImpl<IPpsModuleIssueModuleMapper, PpsModuleIssueModuleEntity> implements IPpsModuleIssueModuleService {

//    @Autowired
//    private IPpsModuleIssueSpacerService ppsModuleIssueSpacerService;
    @Autowired
    private IPpsModuleIssueUnitService ppsModuleIssueUnitService;

    /**
     * 获取详情
     *
     * @param mainId
     * @return
     */
    @Override
    public List<PpsModuleIssueModuleEntity> getListByMainId(Long mainId) {
        QueryWrapper<PpsModuleIssueModuleEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleIssueModuleEntity::getPrcPpsModuleIssueMainId, mainId);
        return selectList(qry);
    }

    @Override
    public String getEntryNoByLineCode(String lineCode) {
        QueryWrapper<PpsModuleIssueModuleEntity> qry = new QueryWrapper<>();
        qry.lambda().like(PpsModuleIssueModuleEntity::getLineCode, lineCode)
                .ne(PpsModuleIssueModuleEntity::getStatus, 2)
                .select(PpsModuleIssueModuleEntity::getEntryNo)
                .orderByAsc(PpsModuleIssueModuleEntity::getCreationDate);
        PpsModuleIssueModuleEntity et = getTopDatas(1, qry).stream().findFirst().orElse(null);
        return et == null ? "" : et.getEntryNo();
    }

    @Override
    public List<PpsModuleIssueModuleEntity> getListByLineEntryCode(String entryNo, String lineCode) {
        QueryWrapper<PpsModuleIssueModuleEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsModuleIssueModuleEntity::getEntryNo, entryNo)
                .like(PpsModuleIssueModuleEntity::getLineCode, lineCode)
                .ne(PpsModuleIssueModuleEntity::getStatus, 2);
        return selectList(qry);
    }

    /**
     * 获取详情
     *
     * @param id
     * @return
     */
    @Override
    public ModuleDetailInfo moduleDetail(Long id) {
        ModuleDetailInfo detial = new ModuleDetailInfo();
        //detial.setSpacerInfos(ppsModuleIssueSpacerService.getByModuleIssueModuleId(id)); TODO wdl
        detial.setUnitInfos(ppsModuleIssueUnitService.getByModuleIssueModuleId(id));
        return detial;
    }

    /**
     * 获取电池预成组下发模组
     *
     * @param planNo   计划编码
     * @param lineCode 生产线体编码
     * @return 电池预成组下发模组
     */
    @Override
    public PpsModuleIssueModuleEntity getRecordInfoByPlanNo(String planNo, String lineCode) {
        QueryWrapper<PpsModuleIssueModuleEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsModuleIssueModuleEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsModuleIssueModuleEntity::getPlanNo, planNo);
        lambdaQueryWrapper.eq(PpsModuleIssueModuleEntity::getLineCode, lineCode);
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}