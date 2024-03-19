package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordPartsEntity;
import com.ca.mfd.prc.avi.mapper.IAviTrackingRecordPartsMapper;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordPartsService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author inkelink
 * @Description: 离散产品过点信息服务实现
 * @date 2023年10月31日
 * @变更说明 BY inkelink At 2023年10月31日
 */
@Service
public class AviTrackingRecordPartsServiceImpl extends AbstractCrudServiceImpl<IAviTrackingRecordPartsMapper, AviTrackingRecordPartsEntity> implements IAviTrackingRecordPartsService {


    @Autowired
    PmShcCalendarProvider pmShcCalendarProvider;


    @Override
    public void virtualTrassAreaPoint(PmAllDTO pm, String sn, String lineCode, Integer orderCategory) {
        PmLineEntity lineInfo = pm.getLines().stream().filter(w -> StringUtils.equals(w.getLineCode(), lineCode)).findFirst().orElse(null);
        if (lineInfo == null) {
            throw new InkelinkException("未找到对应线体");
        }
        PmAviEntity pmAviInfo = pm.getAvis().stream().filter(w -> Objects.equals(w.getPrcPmLineId(), lineInfo.getId()))
                .sorted(Comparator.comparing(PmAviEntity::getAviDisplayNo).reversed()).findFirst().orElse(null);
        if (pmAviInfo != null) {
            insertData(pm, sn, pmAviInfo.getId(), orderCategory);
        }
    }

    private void insertData(PmAllDTO pm, String sn, Long aviId, Integer orderCategory) {
        PmAviEntity pmAviInfo = pm.getAvis().stream().filter(w -> Objects.equals(w.getId(), aviId)).findFirst().orElse(null);
        PmLineEntity pmLine = pm.getLines().stream().filter(w -> Objects.equals(w.getId(), pmAviInfo.getPrcPmLineId())).findFirst().orElse(null);
        PmWorkShopEntity pmShopInfo = pm.getShops().stream().filter(w -> Objects.equals(w.getId(), pmLine.getPrcPmWorkshopId())).findFirst().orElse(null);
        long shcCalendarId = 0;

        ShcCalendarDetailInfo shiftInfo = pmShcCalendarProvider.getCurrentShift(pmLine.getLineCode());
        if (shiftInfo != null) {
            shcCalendarId = ConvertUtils.stringToLong(shiftInfo.getId());
        }
        Boolean isProcess = false;

        AviTrackingRecordPartsEntity aviTrackingRecordInfo = new AviTrackingRecordPartsEntity();
        aviTrackingRecordInfo.setInsertDt(new Date());
        aviTrackingRecordInfo.setMode(1);
        aviTrackingRecordInfo.setIsProcess(isProcess);
        aviTrackingRecordInfo.setSn(sn);
        aviTrackingRecordInfo.setOrderCategory(orderCategory);
        aviTrackingRecordInfo.setLineCode(pmLine.getLineCode());
        aviTrackingRecordInfo.setAviCode(pmAviInfo.getAviCode());
        aviTrackingRecordInfo.setAviName(pmAviInfo.getAviName());
        aviTrackingRecordInfo.setWorkshopCode(pmShopInfo.getWorkshopCode());
        aviTrackingRecordInfo.setPrcPpsShcShiftId(shcCalendarId);
        this.save(aviTrackingRecordInfo);
    }


    public List<AviTrackingRecordPartsEntity> getRecordByOrderCategory(int orderCategory) {
        QueryWrapper<AviTrackingRecordPartsEntity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviTrackingRecordPartsEntity> lambdaWrapper = wrapper.lambda();
        lambdaWrapper.eq(AviTrackingRecordPartsEntity::getOrderCategory, orderCategory);
        lambdaWrapper.eq(AviTrackingRecordPartsEntity::getIsProcess, false);
        lambdaWrapper.orderByAsc(AviTrackingRecordPartsEntity::getCreationDate);
        return this.getTopDatas(200, wrapper);
    }


    @Override
    public void updateProcessByIds(List<Long> ids) {
        UpdateWrapper<AviTrackingRecordPartsEntity> wrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<AviTrackingRecordPartsEntity> lambdaUpdateWrapper = wrapper.lambda();
        lambdaUpdateWrapper.set(AviTrackingRecordPartsEntity::getIsProcess, true);
        lambdaUpdateWrapper.eq(AviTrackingRecordPartsEntity::getIsProcess, false);
        lambdaUpdateWrapper.in(AviTrackingRecordPartsEntity::getId, ids);
        this.update(wrapper);
    }

}