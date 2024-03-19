package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.dto.ProcessRelationInfo;
import com.ca.mfd.prc.pps.mapper.IPpsProcessRelationMapper;
import com.ca.mfd.prc.pps.entity.PpsProcessRelationEntity;
import com.ca.mfd.prc.pps.service.IPpsProcessRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 工序关联配置服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class PpsProcessRelationServiceImpl extends AbstractCrudServiceImpl<IPpsProcessRelationMapper, PpsProcessRelationEntity> implements IPpsProcessRelationService {

    /**
     * 获取列表
     *
     * @param orderCategory
     * @param processCode
     * @return
     */
    @Override
    public List<PpsProcessRelationEntity> getListByOrderCategory(Integer orderCategory, String processCode) {
        QueryWrapper<PpsProcessRelationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsProcessRelationEntity::getOrderCategory, orderCategory)
                .eq(PpsProcessRelationEntity::getProcessCode, processCode)
                .orderByAsc(PpsProcessRelationEntity::getLineCode);
        return selectList(qry);
    }

    /**
     * 获取
     *
     * @param lineCode
     * @return
     */
    @Override
    public PpsProcessRelationEntity getFirstByLineCode(String lineCode,Integer processType) {
        QueryWrapper<PpsProcessRelationEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsProcessRelationEntity::getLineCode, lineCode)
                .eq(PpsProcessRelationEntity::getProcessType, processType);
        //TODO 加入排序
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }


    /**
     * 查询工序关联配置
     *
     * @param category 订单大类
     * @return 工序关联配置
     */
    @Override
    public List<PpsProcessRelationEntity> getListByOrderCategory(int category) {
        QueryWrapper<PpsProcessRelationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsProcessRelationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsProcessRelationEntity::getOrderCategory, category);
        lambdaQueryWrapper.eq(PpsProcessRelationEntity::getIsEnabled, Boolean.TRUE);

        return selectList(queryWrapper);
    }

    /**
     * 查询工序关联配置
     *
     * @param category 订单大类
     * @return 工序关联配置
     */
    @Override
    public List<ProcessRelationInfo> getRecordByOrderCategory(int category) {
        QueryWrapper<PpsProcessRelationEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsProcessRelationEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PpsProcessRelationEntity::getOrderCategory, category);
        lambdaQueryWrapper.eq(PpsProcessRelationEntity::getIsEnabled, Boolean.TRUE);
        List<PpsProcessRelationEntity> processRelationList = selectList(queryWrapper);
        return processRelationList.stream().map(s -> {
            ProcessRelationInfo info = new ProcessRelationInfo();
            info.setProcessCode(s.getProcessCode());
            info.setProcessName(s.getProcessName());
            info.setLineCode(s.getLineCode());
            info.setLineName(s.getLineName());
            info.setProcessType(s.getProcessType());
            info.setShopCode(s.getShopCode());
            info.setId(s.getId());
            info.setAttribute1(s.getAttribute1());
            return info;
        }).collect(Collectors.toList());
    }
}