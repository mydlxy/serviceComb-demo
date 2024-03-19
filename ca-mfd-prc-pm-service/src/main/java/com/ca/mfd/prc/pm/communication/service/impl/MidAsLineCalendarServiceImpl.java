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
import com.ca.mfd.prc.pm.communication.dto.MidAsLineCalendarDto;
import com.ca.mfd.prc.pm.communication.dto.MidAsLineCalendarReq;
import com.ca.mfd.prc.pm.communication.dto.MidAsShcShiftDto;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsLineCalendarEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.communication.entity.MidAsShopCalendarEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidAsLineCalendarMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidAsLineCalendarService;
import com.ca.mfd.prc.pm.communication.service.IMidAsShopCalendarService;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: AS线体日历中间表服务实现
 * @date 2023年10月19日
 * @变更说明 BY inkelink At 2023年10月19日
 */
@Service
public class MidAsLineCalendarServiceImpl extends AbstractCrudServiceImpl<IMidAsLineCalendarMapper, MidAsLineCalendarEntity> implements IMidAsLineCalendarService {
    private static final Logger logger = LoggerFactory.getLogger(MidAsLineCalendarServiceImpl.class);

    @Autowired
    IMidApiLogService midApiLogService;

    @Autowired
    IPmVersionService pmVersionService;

    @Autowired
    IPmOrganizationService pmOrganizationService;

    /**
     * 保存线体日历(全部)
     *
     */
    @Override
    public void saveAllAsLineCalendar() {
        String orgCode = pmOrganizationService.getCurrentOrgCode();
        PmAllDTO pmall = pmVersionService.getObjectedPm();
        List<PmLineEntity> lines = pmall.getLines().stream().filter(c -> StringUtils.isNotBlank(c.getMergeLine()))
                .distinct().collect(Collectors.toList());
        List<PmWorkShopEntity> shops = pmall.getShops();

        List<MidAsLineCalendarReq> reqLines = new ArrayList<>();
        for (PmLineEntity li : lines) {
            PmWorkShopEntity shop = shops.stream().filter(c -> Objects.equals(c.getId(), li.getPrcPmWorkshopId()))
                    .findFirst().orElse(null);
            if (shop != null) {
                MidAsLineCalendarReq req = new MidAsLineCalendarReq();
                req.setOrganizationCode(orgCode);
                req.setWorkshopCode(shop.getWorkshopCode());
                req.setLineCode(li.getMergeLine());
                reqLines.add(req);
            }
        }
        if (!CollectionUtil.isEmpty(reqLines)) {
            saveAsLineCalendar(reqLines);
        }
    }


    @Override
    public MidApiLogEntity saveAsLineCalendar(List<MidAsLineCalendarReq> reqLines) {
        MidApiLogEntity loginfo = new MidApiLogEntity();
        loginfo.setId(IdGenerator.getId());
        loginfo.setApiType(ApiTypeConst.AS_LINE_CALENDAR);
        loginfo.setDataLineNo(0);
        loginfo.setRequestStartTime(new Date());
        String attrib1 = JsonUtils.toJsonString(reqLines);
        loginfo.setAttribute1(attrib1.substring(0, attrib1.length()<=200? attrib1.length():200));
        loginfo.setStatus(0);
        loginfo.setRequestStopTime(new Date());
        midApiLogService.insert(loginfo);
        this.saveChange();
        String errMsg = "";
        List<MidAsLineCalendarEntity> models = new ArrayList<>();
        for (MidAsLineCalendarReq req : reqLines) {
            try {
                errMsg = saveAsLineCalendarReq(req, models);
                logger.info("AS线体日历接收完成:" + errMsg);
            } catch (Exception e) {
                log.error("", e);
                logger.info("AS线体日历接收失败:" + e.getMessage());
            }
        }
        if (!models.isEmpty()) {
            for (MidAsLineCalendarEntity et : models) {
                et.setExeStatus(0);
                et.setExeTime(new Date());
                et.setExeMsg(StringUtils.EMPTY);
                et.setOpCode(1);
                et.setPrcMidApiLogId(loginfo.getId());
            }
            this.insertBatch(models, 100, false, 1);
            this.saveChange();
        }
        midApiLogService.clearChange();
        loginfo.setStatus(1);
        loginfo.setRemark("AS线体日历接收完成:" + errMsg);
        loginfo.setRequestStopTime(new Date());
        midApiLogService.updateById(loginfo);
        this.saveChange();

        return loginfo;
    }

    /**
     * 保存线体日历
     *
     */
    public String saveAsLineCalendarReq(MidAsLineCalendarReq req,List<MidAsLineCalendarEntity> models) {

        //发起请求
        Map searchData = new HashMap();
        if (StringUtils.isBlank(req.getOrganizationCode())
                || StringUtils.isBlank(req.getWorkshopCode())
                || StringUtils.isBlank(req.getLineCode())) {
            return "AS线体日历接收失败:参数无效;" + JsonUtils.toJsonString(req);
        }
        searchData.put("organizationCode", req.getOrganizationCode());
        searchData.put("workshopCode", req.getWorkshopCode());
        searchData.put("lineCode", req.getLineCode());

        ResultVO<String> asShcShiftData = midApiLogService.getAsAllQuery("ascalendarline", searchData);
        if (asShcShiftData == null || !asShcShiftData.getSuccess()) {
            return (asShcShiftData == null ? "AS线体日历接收失败" : asShcShiftData.getMessage());

        } else {
            String rspJson = asShcShiftData.getData();
            if (StringUtils.isNotBlank(rspJson)) {
                List<MidAsLineCalendarDto> datas = JsonUtils.parseArray(rspJson, MidAsLineCalendarDto.class);
                if (!CollectionUtil.isEmpty(datas)) {

                    for (MidAsLineCalendarDto vt : datas) {

                        MidAsLineCalendarEntity et = new MidAsLineCalendarEntity();

                        et.setShiftCode(vt.getShiftCode() == null ? "" : vt.getShiftCode());
                        et.setLineCode(vt.getLineCode() == null ? "" : vt.getLineCode());
                        et.setShopCode(vt.getWorkshopCode());

                        et.setWorkDay(DateUtils.parse(vt.getDateCode(), DateUtils.DATE_PATTERN_C));
                        et.setWorkDate(DateUtils.format(et.getWorkDay()));

                        et.setAttribute1(vt.getOrganizationCode());
                        et.setAttribute2(vt.getShiftModeCode());
                        et.setAttribute3(vt.getRestFlag());

                        models.add(et);
                    }
                    //models = models.stream().distinct().collect(Collectors.toList());

                    //loginfo.setDataLineNo(models.size());
                    //this.insertBatch(models, 100, false, 1);
                }
            }
            return "";

        }

    }

    /**
     * 执行数据处理逻辑(包含线体、车间日历)(考虑异步)
     */
    @Override
    public void excute(String logid) {
        IMidApiLogService midApiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmShcCalendarService pmShcCalendarService = SpringContextUtils.getBean(IPmShcCalendarService.class);
        IMidAsLineCalendarService midAsLineCalendarService = SpringContextUtils.getBean(IMidAsLineCalendarService.class);
        IPmVersionService pmVersionService = SpringContextUtils.getBean(IPmVersionService.class);
        List<MidApiLogEntity> apilogs = midApiLogService.getDoList(ApiTypeConst.AS_LINE_CALENDAR, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        PmAllDTO pmall = pmVersionService.getObjectedPm();
        List<PmLineEntity> lines = pmall.getLines().stream().filter(c -> StringUtils.isNotBlank(c.getMergeLine()))
                .distinct().collect(Collectors.toList());
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                midApiLogService.update(uplogStart);
                midApiLogService.saveChange();

                List<MidAsLineCalendarEntity> datas = midAsLineCalendarService.getListByLog(apilog.getId());
                List<CalendarFromASDTO> calendars = new ArrayList<>();
                for (MidAsLineCalendarEntity dt : datas) {
                    //查找工区对应的线体
                    List<PmLineEntity> mergeLines = lines.stream().filter(c -> StringUtils.endsWithIgnoreCase(c.getMergeLine(), dt.getLineCode())).collect(Collectors.toList());
                    for (PmLineEntity le : mergeLines) {
                        CalendarFromASDTO cl = new CalendarFromASDTO();
                        cl.setDate(dt.getWorkDay());
                        cl.setShopCode(dt.getShopCode());
                        cl.setLineCode(le.getLineCode());
                        cl.setShiftCode(dt.getShiftCode());
                        calendars.add(cl);
                    }
                }
                calendars = calendars.stream().distinct().collect(Collectors.toList());
                pmShcCalendarService.syncCalendarFromAS(calendars, 2);
                pmShcCalendarService.saveChange();
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

    /**
     * 获取计划
     *
     * @param logid
     * @return
     */
    @Override
    public List<MidAsLineCalendarEntity> getListByLog(Long logid) {
        QueryWrapper<MidAsLineCalendarEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidAsLineCalendarEntity::getPrcMidApiLogId, logid);
        return selectList(qry);
    }

}