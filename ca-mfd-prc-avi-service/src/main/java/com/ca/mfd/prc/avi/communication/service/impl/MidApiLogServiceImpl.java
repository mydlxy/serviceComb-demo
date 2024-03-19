package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.dto.MidAsWbsPbsDto;
import com.ca.mfd.prc.avi.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.avi.communication.mapper.IMidApiLogMapper;
import com.ca.mfd.prc.avi.communication.service.IMidApiLogService;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmOrgProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.avi.service.IAviTrackingRecordService;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 接口记录表服务实现
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Service
public class MidApiLogServiceImpl extends AbstractCrudServiceImpl<IMidApiLogMapper, MidApiLogEntity> implements IMidApiLogService {
    private static final Logger logger = LoggerFactory.getLogger(MidApiLogServiceImpl.class);
    @Autowired
    private IAviTrackingRecordService aviTrackingRecordService;
    @Autowired
    private IAviQueueReleaseService aviQueueReleaseService;
    @Autowired
    private PmOrgProvider pmOrgProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Override
    public List<MidApiLogEntity> getDoList(String apitype) {

        QueryWrapper<MidApiLogEntity> qry = new QueryWrapper<>();
        qry.lambda().in(MidApiLogEntity::getStatus, Arrays.asList(1, 6))
                .eq(MidApiLogEntity::getApiType, apitype);
        return selectList(qry);
    }


    @Override
    public List<MidAsWbsPbsDto> sendPbsWbs(String wbsType) {
        if (StringUtils.isBlank(wbsType)) {
            logger.info("参数【wbsType】错误");
            return new ArrayList<>();
        }
        String startAviCode = sysConfigurationProvider.getConfiguration(wbsType + "_START_AVICODE", "wbspbs");
        String endQueue = sysConfigurationProvider.getConfiguration(wbsType + "_END_AVIQUEUE", "wbspbs");
        if (StringUtils.isBlank(startAviCode) || StringUtils.isBlank(endQueue)) {
            logger.info(wbsType + "参数【START_AVICODE、END_AVIQUEUE】错误");
            return new ArrayList<>();
        }

        String orgCode = pmOrgProvider.getCurrentOrgCode();
        //按SN_avicode分组
        Map<String, List<AviQueueReleaseEntity>> avisMps = aviQueueReleaseService.getNoSendByQuee(endQueue)
                .stream().collect(Collectors.groupingBy(c -> c.getAviCode() + ":" + c.getSn()));
        List<MidAsWbsPbsDto> sends = new ArrayList<>();
        List<AviQueueReleaseEntity> aviQueues = new ArrayList<>();
        for (Map.Entry<String, List<AviQueueReleaseEntity>> mp : avisMps.entrySet()) {
            //查找最近过点记录--结束点
            List<AviQueueReleaseEntity> endAviRecores = mp.getValue();
            AviQueueReleaseEntity endAviInfo = endAviRecores.stream().sorted(Comparator.comparing(AviQueueReleaseEntity::getInsertDt).reversed()).findFirst().orElse(null);
            if (endAviInfo == null) {
                continue;
            }
            endAviRecores.forEach(c -> c.setIsSend(true));
            String sn = endAviInfo.getSn();
            String endAviCode = endAviInfo.getAviCode();
            //查找最近过点记录--开始点
            AviTrackingRecordEntity startAviRecored = aviTrackingRecordService.getTopAviPassedRecord(sn, startAviCode);
            if (startAviRecored == null) {
                //处理异常数据
                endAviInfo.setRemark(String.format("数据发送AS失败，数据没有找到进入点【%s】的过点记录", startAviCode));
            } else {
                endAviInfo.setRemark(String.format("数据发送AS，进入点【%s】", startAviCode));
                PpsOrderEntity order = ppsOrderProvider.getPpsOrderBySnOrBarcode(sn);
                String planNo = order == null ? "" : order.getPlanNo();
                MidAsWbsPbsDto et = new MidAsWbsPbsDto();
                et.setOrgCode(orgCode);
                et.setVrn(planNo);
                et.setVin(sn);
                et.setWbspbs(wbsType);
                et.setEntryTime(startAviRecored.getInsertDt());
                et.setExitTime(endAviInfo.getInsertDt());
                Integer stayTime = Long.valueOf((et.getExitTime().getTime() - et.getEntryTime().getTime()) / 1000).intValue();
                et.setStayTime(stayTime);
                sends.add(et);
            }
            aviQueues.addAll(endAviRecores);
        }
        aviQueueReleaseService.updateBatchById(aviQueues);

        return sends;
    }
}