package com.ca.mfd.prc.pm.communication.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.dto.MidAsShcShiftDto;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsLineCalendarEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidAsShiftMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShiftService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS班次信息中间表服务实现
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@Service
public class MidAsShiftServiceImpl extends AbstractCrudServiceImpl<IMidAsShiftMapper, MidAsShiftEntity> implements IMidAsShiftService {

    private static final Logger logger = LoggerFactory.getLogger(MidAsShiftServiceImpl.class);

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsShiftEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsShiftEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsShiftEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

    @Autowired
    IMidApiLogService midApiLogService;
    @Override
    public MidApiLogEntity saveAsShfShift(String mqMsg) {
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setId(IdGenerator.getId());
        loginfo.setApiType(ApiTypeConst.AS_SHC_SHIFT);
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        loginfo.setAttribute1(mqMsg);
        ResultVO<String> asShcShiftData = midApiLogService.getAsAllQuery("ascalendardaymode", null);
        if (asShcShiftData == null || !asShcShiftData.getSuccess()) {
            loginfo.setStatus(6);
            loginfo.setRequestStopTime(new Date());
            loginfo.setRemark(asShcShiftData == null ? "" : asShcShiftData.getMessage());
            midApiLogService.insert(loginfo);
            midApiLogService.saveChange();
        } else {
            try {
                String rspJson = asShcShiftData.getData();
                if (StringUtils.isNotBlank(rspJson)) {
                    String nowstr = DateUtils.format(new Date(), DateUtils.DATE_PATTERN);
                    List<MidAsShcShiftDto> datas = JsonUtils.parseArray(rspJson, MidAsShcShiftDto.class);
                    if (!CollectionUtil.isEmpty(datas)) {
                        List<MidAsShiftEntity> models = new ArrayList<>();
                        for (MidAsShcShiftDto vt : datas) {

                            MidAsShiftEntity et = new MidAsShiftEntity();

                            et.setIsCross(StringUtils.equals(vt.getIsCross(), "1") ? true : false);
                            et.setShiftCode(vt.getShiftCode());
                            et.setShiftName(vt.getShiftCode());
                            String start = nowstr + " " + vt.getFrameBegin().trim() + ":00";
                            et.setStartTime(DateUtils.parse(start, DateUtils.DATE_TIME_PATTERN));
                            String end = nowstr + " " + vt.getFrameEnd().trim() + ":00";
                            et.setEndTime(DateUtils.parse(end, DateUtils.DATE_TIME_PATTERN));

                            models.add(et);
                        }
                        //models = models.stream().distinct().collect(Collectors.toList());
                        for (MidAsShiftEntity et : models) {
                            et.setExeStatus(0);
                            et.setExeTime(new Date());
                            et.setExeMsg(StringUtils.EMPTY);
                            et.setOpCode(1);
                            et.setPrcMidApiLogId(loginfo.getId());
                        }
                        loginfo.setDataLineNo(models.size());
                        this.insertBatch(models, 100, false, 1);
                    }
                }
                loginfo.setStatus(1);
                loginfo.setRequestStopTime(new Date());
                loginfo.setRemark("AS班次日历接收完成");
                midApiLogService.insert(loginfo);
                midApiLogService.saveChange();
            } catch (Exception e) {
                log.error("", e);
                loginfo.setStatus(6);
                loginfo.setRequestStopTime(new Date());
                loginfo.setRemark("AS班次日历接收失败:" + e.getMessage());
                midApiLogService.insert(loginfo);
                midApiLogService.saveChange();
            }
        }
        return loginfo;
    }


    /**
     * 执行数据处理逻辑(考虑异步)
     *
     */
    @Override
    public void excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmShcShiftService pmShcShiftService = SpringContextUtils.getBean(IPmShcShiftService.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_SHC_SHIFT, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                List<MidAsShiftEntity> datas = this.getListByLog(apilog.getId());
                pmShcShiftService.syncShiftFromAS(datas);
                this.updateBatchById(datas);
                pmShcShiftService.saveChange();
                success = true;

            } catch (Exception exception) {
                logger.debug("数据保存异常：{}", exception.getMessage());
            }
            try {
                midApiLogService.clearChange();
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogEnd);
                midApiLogService.saveChange();
            } catch (Exception exception) {
                logger.debug("日志保存异常：{}", exception.getMessage());
            }
        }
    }


}