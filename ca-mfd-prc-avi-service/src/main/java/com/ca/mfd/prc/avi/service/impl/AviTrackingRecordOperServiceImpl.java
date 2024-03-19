package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.avi.entity.AviOperEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordOperEntity;
import com.ca.mfd.prc.avi.mapper.IAviTrackingRecordOperMapper;
import com.ca.mfd.prc.avi.service.IAviOperService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordOperService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 产品过点信息行为记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviTrackingRecordOperServiceImpl extends AbstractCrudServiceImpl<IAviTrackingRecordOperMapper, AviTrackingRecordOperEntity> implements IAviTrackingRecordOperService {

    @Autowired
    IAviTrackingRecordService aviTrackingRecordService;
    @Autowired
    IAviOperService aviOperService;

    /**
     * 过点生产行为
     */
    @Override
    public void createAviPointOperDetailData(String id) {
        AviTrackingRecordEntity trackingRecord = aviTrackingRecordService.get(id);
        List<AviOperEntity> operConfigs = aviOperService.getAllDatas();
        //        List<AviOperEntity> aviOperConfigs = operConfigs.stream().filter(o -> (StringUtils.equals(o.getPmAviId(), trackingRecord.getPmAviId())
        //                || StringUtils.equals(o.getPmAviId(), Constant.EMPTY_ID))
        //                && trackingRecord.getType().intValue() == o.getAviType().intValue()).collect(Collectors.toList());
        List<AviOperEntity> aviOperConfigs = operConfigs.stream().filter(o -> (StringUtils.equals(o.getAviCode(), trackingRecord.getAviCode())
                || StringUtils.equals(o.getAviCode(), StringUtils.EMPTY))
                && trackingRecord.getAviTrackIngRecordType().intValue() == o.getAviType().intValue()).collect(Collectors.toList());
        for (AviOperEntity aviOperConfig : aviOperConfigs) {
            if (!aviOperConfig.getIsEnabled()) {
                continue;
            }
            //不允许重复执行
            if (!aviOperConfig.getIsRepeat()) {
                QueryWrapper<AviTrackingRecordOperEntity> qry = new QueryWrapper<>();
                qry.lambda()
                        .eq(AviTrackingRecordOperEntity::getSn, trackingRecord.getSn())
                        //.eq(AviTrackingRecordOperEntity::getPmAviId, trackingRecord.getPmAviId())
                        .eq(AviTrackingRecordOperEntity::getAviCode, trackingRecord.getAviCode())
                        //.eq(AviTrackingRecordOperEntity::getPmAviOperId, aviOperConfig.getFlag());
                        .eq(AviTrackingRecordOperEntity::getPrcAviOperId, aviOperConfig.getId());
                if (this.selectCount(qry) > 0) {
                    continue;
                }
            }
            AviTrackingRecordOperEntity et = new AviTrackingRecordOperEntity();
            et.setSn(trackingRecord.getSn());
            et.setInsertDt(trackingRecord.getInsertDt());
            et.setAction(aviOperConfig.getAction());
            et.setIsProcess(false);
            et.setOrderCategory(trackingRecord.getOrderCategory());
            //et.setPmWorkCenterCode(trackingRecord.getPmWorkCenterCode());
            et.setWorkshopCode(trackingRecord.getWorkshopCode());
            //et.setPmAviCode(trackingRecord.getPmAviCode());
            et.setAviCode(trackingRecord.getAviCode());
            //et.setPmAviId(trackingRecord.getPmAviId());
            //et.setPmAviName(trackingRecord.getPmAviName());
            et.setAviName(trackingRecord.getAviName());
            //et.setPmAviOperId(aviOperConfig.getId());
            et.setPrcAviOperId(aviOperConfig.getId());
            //et.setPmAreaCode(trackingRecord.getPmAreaCode());
            et.setLineCode(trackingRecord.getLineCode());
            et.setProcessCount(0);
            et.setRemark("");
            insert(et);
        }
        UpdateWrapper<AviTrackingRecordEntity> uprec = new UpdateWrapper<>();
        uprec.lambda().set(AviTrackingRecordEntity::getIsProcess, true)
                .eq(AviTrackingRecordEntity::getId, id);
        aviTrackingRecordService.update(uprec);
    }

    /**
     * 根据配置行为获取未处理队列
     */
    @Override
    public List<AviTrackingRecordOperEntity> getTopNoProcessData(String action) {
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("ACTION", action, ConditionOper.Equal));
        conditionInfos.add(new ConditionDto("IS_PROCESS", "0", ConditionOper.Equal));

        List<SortDto> sortInfos = new ArrayList<>();
        sortInfos.add(new SortDto("CREATION_DATE", ConditionDirection.ASC));

        return getTopDatas(100, conditionInfos, sortInfos);
    }

    @Override
    public List<AviTrackingRecordOperEntity> getData(String sn, String aviCode) {
        QueryWrapper<AviTrackingRecordOperEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviTrackingRecordOperEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviTrackingRecordOperEntity::getSn, sn)
                .eq(AviTrackingRecordOperEntity::getAviCode, aviCode);
        return selectList(queryWrapper);
    }
}