package com.ca.mfd.prc.avi.host.scheduling.impl;

import com.ca.mfd.prc.avi.communication.service.impl.MidApiLogServiceImpl;
import com.ca.mfd.prc.avi.host.scheduling.IEntryReportPassAviService;
import com.ca.mfd.prc.avi.host.scheduling.dto.PpsEntryDto;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryReportPartsProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordPartsService;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Async
public class EntryReportPassAviImpl implements IEntryReportPassAviService {
    private static final Logger logger = LoggerFactory.getLogger(EntryReportPassAviImpl.class);

    @Autowired
    PpsEntryReportPartsProvider ppsEntryReportPartsProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    IAviTrackingRecordPartsService aviTrackingRecordPartsService;

    @Override
    public void start(int orderCategory) {
        Date dtNow = new Date();
        try {
            List<PpsEntryReportPartsEntity> ppsEntryEntityList = ppsEntryReportPartsProvider.getTopDataByOrderCategory(orderCategory);
            List<PpsEntryDto> models = ppsEntryEntityList.stream().map(s -> {
                PpsEntryDto entryDto = new PpsEntryDto();
                entryDto.setId(s.getId());
                entryDto.setEntryReportNo(s.getEntryReportNo());
                entryDto.setWorkShopCode(s.getWorkshopCode());
                entryDto.setLineCode(s.getLineCode());
                entryDto.setCreationDate(s.getCreationDate());
                entryDto.setOrderCategory(s.getOrderCategory());
                return entryDto;
            }).collect(Collectors.toList());
            PmAllDTO pm = pmVersionProvider.getObjectedPm();
            List<SysConfigurationEntity> configInfos = sysConfigurationProvider.getSysConfigurations("ReportDelayConfig");
            for (PpsEntryDto item : models) {
                SysConfigurationEntity configInfo = configInfos
                        .stream().filter(s -> StringUtils.equals(s.getText(), item.getLineCode())).findFirst().orElse(null);
                int delayTime = 0;
                if (configInfo != null) {
                    String value = StringUtils.isBlank(configInfo.getValue()) ? "" : configInfo.getValue();
                    delayTime = ConvertUtils.tryParse(value) == null ? 0 : ConvertUtils.tryParse(value);
                }
                if (DateUtils.addDateSeconds(item.getCreationDate(), delayTime).compareTo(dtNow) < 0) {
                    ppsEntryReportPartsProvider.updateIsPassAviById(item.getId());
                    aviTrackingRecordPartsService.virtualTrassAreaPoint(pm,item.getEntryReportNo(), item.getLineCode(), item.getOrderCategory());
                }
            }
            if (models.size() > 0) {
                aviTrackingRecordPartsService.saveChange();
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }

    }
}
