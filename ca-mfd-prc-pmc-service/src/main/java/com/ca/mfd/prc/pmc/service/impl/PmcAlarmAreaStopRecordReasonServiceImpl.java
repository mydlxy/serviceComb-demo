package com.ca.mfd.prc.pmc.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.TimeSpan;
import com.ca.mfd.prc.pmc.dto.AuditMeasureInfoDTO;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordReasonEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcAlarmAreaStopRecordReasonMapper;
import com.ca.mfd.prc.pmc.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pmc.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopRecordReasonService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 停线记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcAlarmAreaStopRecordReasonServiceImpl extends AbstractCrudServiceImpl<IPmcAlarmAreaStopRecordReasonMapper, PmcAlarmAreaStopRecordReasonEntity> implements IPmcAlarmAreaStopRecordReasonService {
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private PmVersionProvider pmVersionProvider;

    @Override
    public PageData<PmcAlarmAreaStopRecordReasonEntity> page(PageDataDto model) {
        IPage<PmcAlarmAreaStopRecordReasonEntity> page = this.getDataByPage(model);
        PageData<PmcAlarmAreaStopRecordReasonEntity> result = getPageData(page, this.currentModelClass());
        if (model.getPageIndex() != null) {
            result.setPageIndex(model.getPageIndex());
        }
        if (model.getPageSize() != null) {
            result.setPageSize(model.getPageSize());
        }
        result.getDatas().stream().forEach(i -> {
            i.setTime(secToHms(i.getDuration()));
        });
        return result;
    }


    @Override
    public void auditMeasure(AuditMeasureInfoDTO auditMeasureInfo) {
        Integer status = 1;
        if (auditMeasureInfo.getShortMeasureAuditStatus().equals(1) && auditMeasureInfo.getLongMeasureAuditStatus().equals(1)) {
            status = 2;
        } else if (auditMeasureInfo.getShortMeasureAuditStatus().equals(0) && auditMeasureInfo.getLongMeasureAuditStatus().equals(0)) {
            status = 0;
        } else {
            status = 1;
        }

        UpdateWrapper<PmcAlarmAreaStopRecordReasonEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PmcAlarmAreaStopRecordReasonEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.eq(PmcAlarmAreaStopRecordReasonEntity::getId, auditMeasureInfo.getAreaStopRecordReasonId());
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getAuditPrcPmcUserName, identityHelper.getUserName());
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getAuditPrcPmcUserId, identityHelper.getUserId());
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getLongMeasureAuditStatus, auditMeasureInfo.getLongMeasureAuditStatus());
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getShortMeasureAuditStatus, auditMeasureInfo.getShortMeasureAuditStatus());
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getStatus, status);
        lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getAuditTime, new Date());
        this.update(updateWrapper);
        this.saveChange();
    }


    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        List<PmWorkShopEntity> shopsList = pmVersionProvider.getObjectedPm().getShops();
        List<SysConfigurationEntity> pmcAreaStopStatusConfig = sysConfigurationProvider.getSysConfigurations("pmcAreaStopStatus");
        List<SysConfigurationEntity> alarmAuditStatus = sysConfigurationProvider.getSysConfigurations("Alarmauditstatus");
        List<SysConfigurationEntity> pmcReasonDutyConfig = sysConfigurationProvider.getSysConfigurations("pmcreasonduty");
        DateFormat englishDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        DateFormat chineseDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINESE);
        datas.forEach(data -> {
            Set<String> keySet = data.keySet();
            for (String key : keySet) {
                if(!"time".equals(key)){
                    if(key.toLowerCase().endsWith("dt")||key.toLowerCase().endsWith("time")){
                        Date englishDateString = (Date) data.get(key);
                        String chineseDateString = chineseDateFormat.format(englishDateString);
                        data.put(key,chineseDateString);
                    }
                }else {
                    String time = (String) data.get(key);
                    if(StringUtils.isBlank(time)){
                        data.put(key,"00:00");
                    }
                }

                if ("shopCode".equals(key)) {
                    String shopCode = (String) data.get(key);
                    PmWorkShopEntity shopEntity = shopsList.stream().filter(shop -> shop.getWorkshopCode().equals(shopCode)).findFirst().orElse(null);
                    if (Objects.nonNull(shopEntity)) {
                        data.put(key, shopEntity.getWorkshopName());
                    }
                }
                if ("status".equals(key)) {
                    Integer status = (Integer) data.get(key);
                    SysConfigurationEntity configEntity = pmcAreaStopStatusConfig.stream().filter(config -> config.getValue().equals(String.valueOf(status))).findFirst().orElse(null);
                    if (Objects.nonNull(configEntity)) {
                        data.put(key, configEntity.getText());
                    }
                }
                if ("shortMeasureAuditStatus".equals(key)||"longMeasureAuditStatus".equals(key)) {
                    Integer status = (Integer) data.get(key);
                    SysConfigurationEntity configEntity = alarmAuditStatus.stream().filter(config -> config.getValue().equals(String.valueOf(status))).findFirst().orElse(null);
                    if (Objects.nonNull(configEntity)) {
                        data.put(key, configEntity.getText());
                    }
                }
                if ("stopCauseType".equals(key)) {
                    String stopCauseType = (String) data.get(key);
                    SysConfigurationEntity configEntity = pmcReasonDutyConfig.stream().filter(config -> config.getValue().equals(stopCauseType)).findFirst().orElse(null);
                    if (Objects.nonNull(configEntity)) {
                        data.put(key, configEntity.getText());
                    }
                }

            }
        });
        super.dealExcelDatas(datas);
    }

    private String secToHms(Integer duration) {
        TimeSpan ts = new TimeSpan(duration);
        String str;
        if (ts.getHours() > 0) {
            str = String.format("%02d", ts.getHours()) + ":" + String.format("%02d", ts.getMinutes()) + ":" + String.format("%02d", ts.getSeconds());
        } else if (ts.getMinutes() > 0) {
            str = "00:" + String.format("%02d", ts.getMinutes()) + ":" + String.format("%02d", ts.getSeconds());
        } else {
            str = "00:00:" + String.format("%02d", ts.getSeconds());
        }
        return str;
    }
}