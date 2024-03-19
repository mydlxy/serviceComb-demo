package com.ca.mfd.prc.avi.host.scheduling.impl;

import com.ca.mfd.prc.avi.entity.AviTrackingRecordPartsEntity;
import com.ca.mfd.prc.avi.host.scheduling.IEntryReportStatisticsService;
import com.ca.mfd.prc.avi.host.scheduling.dto.AviTrackRecordDto;
import com.ca.mfd.prc.avi.host.scheduling.dto.InsertAsAviPointInfo;
import com.ca.mfd.prc.avi.host.scheduling.dto.PpsEntryReportPartsDto;
import com.ca.mfd.prc.avi.host.scheduling.dto.PpsEntryReportsVo;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsAsAviPointServiceProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryReportPartsProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordPartsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Async
public class EntryReportStatisticsImpl implements IEntryReportStatisticsService {
    private static final Logger logger = LoggerFactory.getLogger(EntryReportStatisticsImpl.class);

    @Autowired
    PpsEntryReportPartsProvider ppsEntryReportPartsProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;
    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    PpsAsAviPointServiceProvider ppsAsAviPointServiceProvider;

    @Autowired
    IAviTrackingRecordPartsService aviTrackingRecordPartsService;

    @Override
    public void start(int orderCategory) {
        try {
            List<AviTrackingRecordPartsEntity> list = aviTrackingRecordPartsService.getRecordByOrderCategory(orderCategory);
            List<AviTrackRecordDto> datas = list.stream().map(s -> {
                AviTrackRecordDto info = new AviTrackRecordDto();
                info.setId(s.getId());
                info.setSn(s.getSn());
                info.setAviCode(s.getAviCode());
                info.setLineCode(s.getLineCode());
                return info;
            }).collect(Collectors.toList());

            if (datas.isEmpty()) {
                return;
            }
            List<String> rprtNos = datas.stream().map(s -> s.getSn()).collect(Collectors.toList());
            PpsEntryReportPartsDto dto = new PpsEntryReportPartsDto();
            dto.setRprtNos(rprtNos);
            dto.setOrderCategory(orderCategory);
            List<PpsEntryReportPartsEntity> ppsEntryList = ppsEntryReportPartsProvider.getRecordByOrderCategory(dto);
            List<PpsEntryReportsVo> ppsEntryReports = ppsEntryList.stream().map(s -> {
                PpsEntryReportsVo info = new PpsEntryReportsVo();
                info.setPlanNo(s.getPlanNo());
                info.setReportType(s.getReportType());
                info.setEntryReportCount(s.getEntryReportCount());
                info.setEntryReportNo(s.getEntryReportNo());
                return info;
            }).collect(Collectors.toList());

            List<InsertAsAviPointInfo> reportBases = new ArrayList<>();
            if (!ppsEntryReports.isEmpty()) {
                for (AviTrackRecordDto data : datas) {
                    List<PpsEntryReportsVo> ppsEntryReportsVoList = ppsEntryReports.stream().filter(s -> StringUtils.equals(s.getEntryReportNo(), data.getSn()))
                            .collect(Collectors.toList());
                    for (PpsEntryReportsVo rptInfo : ppsEntryReportsVoList) {
                        InsertAsAviPointInfo info = new InsertAsAviPointInfo();
                        info.setLineCode(data.getLineCode());
                        info.setAviCode(data.getAviCode());
                        info.setBadCount(getReportCount(rptInfo.getReportType(), rptInfo.getEntryReportCount(), false));
                        info.setPlanNo(rptInfo.getPlanNo());
                        info.setQualifiedCount(getReportCount(rptInfo.getReportType(), rptInfo.getEntryReportCount(), true));
                        reportBases.add(info);
                    }
                }

                List<InsertAsAviPointInfo> asReportInfos = new ArrayList<>();
                //汇总数据
                Map<String, List<InsertAsAviPointInfo>> map = reportBases.stream()
                        .collect(Collectors.groupingBy(score -> score.getPlanNo() + "@" + score.getAviCode() + "@" + score.getLineCode()));
                for (Map.Entry<String, List<InsertAsAviPointInfo>> entry : map.entrySet()) {
                    String groupName = entry.getKey();
                    String planNo = entry.getValue().get(0).getPlanNo();
                    String aviCode = entry.getValue().get(0).getAviCode();
                    String lineCode = entry.getValue().get(0).getLineCode();
                    InsertAsAviPointInfo info = new InsertAsAviPointInfo();
                    info.setAviCode(aviCode);
                    info.setLineCode(lineCode);
                    info.setPlanNo(planNo);
                    info.setBadCount(entry.getValue().stream().mapToInt(InsertAsAviPointInfo::getBadCount).sum());
                    info.setQualifiedCount(entry.getValue().stream().mapToInt(InsertAsAviPointInfo::getQualifiedCount).sum());
                    info.setScanType("X");
                    info.setVin("");
                    asReportInfos.add(info);
                }
                for (InsertAsAviPointInfo asReportInfo : asReportInfos) {
                    //避免失败反复写入TODO
                    ppsAsAviPointServiceProvider.insertAsAviPoint(asReportInfo);
                }
                List<Long> ids = datas.stream().map(AviTrackRecordDto::getId).collect(Collectors.toList());
                aviTrackingRecordPartsService.updateProcessByIds(ids);
                aviTrackingRecordPartsService.saveChange();
            }
        } catch (Exception ex) {
            logger.error(ex.toString());
        }
    }

    private int getReportCount(int reportType, int reportCount, boolean getQualifiedCount) {
        int result = 0;
        //合格 取 1 5
        if (getQualifiedCount && (reportType == 1 || reportType == 5)) {
            result = reportCount;
        }
        //不合格 取 3 6
        if (!getQualifiedCount && (reportType == 3 || reportType == 6)) {
            result = reportCount;
        }
        return result;
    }

}
