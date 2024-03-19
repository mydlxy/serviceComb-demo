package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.pm.dto.AtomicTime;
import com.ca.mfd.prc.pm.dto.CalendarCopyDTO;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.dto.CalendarParamDTO;
import com.ca.mfd.prc.pm.dto.EffAtomicTime;
import com.ca.mfd.prc.pm.dto.LineDTO;
import com.ca.mfd.prc.pm.dto.LineVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmCalendarSource;
import com.ca.mfd.prc.pm.dto.PmShcCalendarAtom;
import com.ca.mfd.prc.pm.dto.ShcBreakDetailInfo;
import com.ca.mfd.prc.pm.dto.ShcCalTempInfo;
import com.ca.mfd.prc.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.pm.dto.ShcWorkTimeInfo;
import com.ca.mfd.prc.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pm.dto.ShopDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmShcCalendarMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PmShcCalendarServiceImpl extends AbstractCrudServiceImpl<IPmShcCalendarMapper, PmShcCalendarEntity> implements IPmShcCalendarService {
    /*    @Autowired
        IPmWorkShopService pmWorkShopService;*/
    /*@Autowired
    private IPmVersionService pmVersionService;*/
    @Resource
    private IPmShcCalendarMapper pmShcCalendarMapper;
    /* @Resource
     private IPmShcCalendarAreaMapper pmShcCalendarAreaMapper;*/
  /*  @Autowired
    IPmShcCalendarAreaService pmShcCalendarAreaService;*/
    @Autowired
    private IPmShcShiftService pmShcShiftService;
    @Autowired
    private IPmVersionService pmVersionService;
    @Autowired
    private SysConfigurationProvider sysConfigurationService;

    @Autowired
    private LocalCache localCache;

    private String cacheName = "PRC_PM_SHC_CALENDAR";

    private static final Object LOCK_OBJ = new Object();


    public ShcCalendarDetailInfo getCurrentShift(String lineCode) {
        ShcCalendarDetailInfo detail = null;
        List<ShcCalendarDetailInfo> calendarDetails = localCache.getObject(cacheName + lineCode);
        if (calendarDetails == null || calendarDetails.size() == 0) {
            synchronized (LOCK_OBJ) {
                calendarDetails = localCache.getObject(cacheName + lineCode);
                if (calendarDetails == null || calendarDetails.size() == 0) {
                    //获取一段时间的排班信息，用于计算当前班次
                    calendarDetails = getCalendarDetail(DateUtils.addDateDays(new Date(), -5), lineCode, 6);
                    if (calendarDetails.size() > 0) {
                        localCache.addObject(cacheName + lineCode, calendarDetails, 60 * 10);
                    }
                }
            }
        }
        detail = getShift(new Date(), calendarDetails);
        return detail;
    }

    private ShcCalendarDetailInfo getShift(Date dt, List<ShcCalendarDetailInfo> calendarDetails) {
        ShcCalendarDetailInfo data = null;
        if (calendarDetails != null) {
            data = calendarDetails.stream().filter(o -> o.getShiftStartTime().compareTo(dt) < 0)
                    .sorted(Comparator.comparing(o -> o.getShiftStartTime())).findFirst().orElse(null);
        }
        return data;
    }

    /**
     * 获取工厂日历树车间列表
     *
     * @return List<ShopDTO>
     */
    @Override
    public List<ShopDTO> getCalendarShopInfos() {
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        List<PmWorkShopEntity> datas = allPm.getShops();
        datas.sort(Comparator.comparing(PmWorkShopEntity::getDisplayNo));

        String ioc = getSysConfigurations("PMIcon").
                stream().filter(o -> "PmShop".equals(o.getText())).map(SysConfigurationEntity::getValue).findFirst().orElse(null);

        List<ShopDTO> shopInfos = new ArrayList<>();
        for (PmWorkShopEntity eachShop : datas) {
            ShopDTO et = new ShopDTO();
            et.setPmShopId(eachShop.getId());
            et.setPmShopCode(eachShop.getWorkshopCode());
            et.setPmShopName(eachShop.getWorkshopName());
            et.setIconCls(ioc);
            List<PmLineEntity> pmLineEntities = allPm.getLines().stream().filter(c -> c.getPrcPmWorkshopId().equals(eachShop.getId()) && c.getVersion().equals(eachShop.getVersion())).sorted(Comparator.comparing(PmLineEntity::getLineDisplayNo)).collect(Collectors.toList());
            //   pmLineEntities.sort(Comparator.comparing(PmLineEntity::getLineDisplayNo).thenComparing(PmLineEntity::getLastUpdateDate, Comparator.reverseOrder()));
            pmLineEntities.sort(Comparator.comparing(PmLineEntity::getLineDisplayNo));

            List<LineDTO> lineDTOS = pmLineEntities.stream().map(o -> {
                LineDTO lineDTO = new LineDTO();
                lineDTO.setPmLineId(o.getId());
                lineDTO.setPmShopId(o.getPrcPmWorkshopId());
                lineDTO.setPmLineCode(o.getLineCode());
                lineDTO.setPmLineName(o.getLineName());

                return lineDTO;
            }).collect(Collectors.toList());
            et.setChildren(lineDTOS);

            shopInfos.add(et);
        }

        /*List<ShopDTO> shopInfos = datas.stream().map(o -> {
            ShopDTO et = new ShopDTO();
            et.setPmShopId(o.getId());
            et.setPmShopCode(o.getWorkShopCode());
            et.setPmShopName(o.getWorkShopName());
            et.setIconCls(ioc);
            return et;
        }).collect(Collectors.toList());*/

        return shopInfos;
    }

    /**
     * 获取系统配置
     */
    private List<SysConfigurationEntity> getSysConfigurations(String category) {
        return sysConfigurationService.getSysConfigurations(category);
    }

    /**
     * 获得最近更新的记录
     *
     * @return
     */
    @Override
    public PmShcCalendarEntity getLastUpdatePmShcCalendar() {
        QueryWrapper<PmShcCalendarEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.orderByDesc(PmShcCalendarEntity::getLastUpdateDate);
        lambdaQueryWrapper.last("limit 0,1");

        List<PmShcCalendarEntity> list = this.selectList(queryWrapper);
        PmShcCalendarEntity entity = null;
        if (!CollectionUtils.isEmpty(list)) {
            entity = list.get(0);
        }
        return entity;
    }

    @Override
    public List<PmShcCalendarEntity> getSourceCalendarList(CalendarCopyDTO calendarCopyDTO) {
        QueryWrapper<PmShcCalendarEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmShcCalendarEntity::getWorkshopCode, calendarCopyDTO.getWorkshopCode());
        lambdaQueryWrapper.eq(PmShcCalendarEntity::getLineCode, "");
        lambdaQueryWrapper.ge(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getBeginTime());
        lambdaQueryWrapper.le(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getEndTime());
        lambdaQueryWrapper.orderByAsc(PmShcCalendarEntity::getWorkDay);
        return this.selectList(queryWrapper);
    }

    @Override
    public List<PmShcCalendarEntity> getSourceCalendarLineList(CalendarCopyDTO calendarCopyDTO) {
        QueryWrapper<PmShcCalendarEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmShcCalendarEntity::getLineCode, calendarCopyDTO.getLineCode());
        lambdaQueryWrapper.ge(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getBeginTime());
        lambdaQueryWrapper.le(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getEndTime());
        lambdaQueryWrapper.orderByAsc(PmShcCalendarEntity::getWorkDay);
        return this.selectList(queryWrapper);
    }

    /**
     * 设置工作时间
     */
    @Override
    public Integer calculateWorkTime(Date startTime, Date endTime, String shopCode) {
        Date startDay = DateUtils.stringToDate(DateUtils.format(DateUtils.addDateDays(startTime, -1),
                DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);
        Date endDay = DateUtils.stringToDate(DateUtils.format(endTime), DateUtils.DATE_PATTERN);
        List<PmCalendarSource> calendarSource = pmShcCalendarMapper.getcalendarsource(DateUtils.format(startDay, DateUtils.DATE_TIME_PATTERN),
                DateUtils.format(endDay, DateUtils.DATE_TIME_PATTERN), shopCode);
        List<PmShcCalendarAtom> pmShcCalendarlist = getAtomData(calendarSource);
        List<AtomicTime> atoms = new ArrayList<>();
        for (PmShcCalendarAtom item : pmShcCalendarlist) {
            AtomicTime addatoms = new AtomicTime();
            addatoms.setStartTime(item.getStartTime());
            addatoms.setEndTime(item.getEndTime());
            atoms.add(addatoms);
        }
        atoms = atoms.stream().sorted(Comparator.comparing(AtomicTime::getStartTime)).collect(Collectors.toList());
        atoms = unionCalculation(atoms);
        return getYouXiaoTime(atoms, startTime, endTime);
    }

    private List<AtomicTime> unionCalculation(List<AtomicTime> times) {
        List<AtomicTime> data = new ArrayList<>();

        /*times = times.stream().sorted(Comparator.comparing(AtomicTime::getStartTime).reversed())
                .collect(Collectors.toList());*/
        for (AtomicTime item : times) {
            AtomicTime time = data.size() == 0 ? null : data.get(data.size() - 1);
            if (time == null) {
                if (item.getEndTime().getTime() > times.get(times.indexOf(item)).getStartTime().getTime()) {
                    AtomicTime addtime = new AtomicTime();
                    addtime.setStartTime(item.getStartTime());
                    addtime.setEndTime(times.get(times.indexOf(item)).getEndTime());
                    data.add(addtime);
                } else {
                    AtomicTime addtime = new AtomicTime();
                    addtime.setStartTime(item.getStartTime());
                    addtime.setEndTime(item.getEndTime());
                    data.add(addtime);
                }
            } else {
//                if (time.getEndTime().getTime() >= item.getEndTime().getTime()) {
//                    continue;
//                } else if (item.getStartTime().getTime() <= time.getEndTime().getTime()
//                        && item.getEndTime().getTime() > time.getEndTime().getTime()) {
//                    time.setEndTime(item.getEndTime());
//                } else {
//                    AtomicTime addtime = new AtomicTime();
//                    addtime.setStartTime(item.getStartTime());
//                    addtime.setEndTime(item.getEndTime());
//                    data.add(addtime);
//                }

                if (item.getStartTime().getTime() <= time.getEndTime().getTime()
                        && item.getEndTime().getTime() > time.getEndTime().getTime()) {
                    time.setEndTime(item.getEndTime());
                } else if (item.getStartTime().getTime() > time.getEndTime().getTime()
                        && item.getEndTime().getTime() > time.getEndTime().getTime()) {
                    AtomicTime addtime = new AtomicTime();
                    addtime.setStartTime(item.getStartTime());
                    addtime.setEndTime(item.getEndTime());
                    data.add(addtime);
                }
            }
        }
        data = data.stream().sorted(Comparator.comparing(AtomicTime::getStartTime)).collect(Collectors.toList());
        return data;
    }

    private Integer getYouXiaoTime(List<AtomicTime> atoms, Date startTime, Date endTime) {
        Integer result = 0;
        if (startTime.getTime() >= endTime.getTime()) {
            return result;
        }
        atoms = atoms.stream().sorted(Comparator.comparing(AtomicTime::getStartTime)).collect(Collectors.toList());
        List<EffAtomicTime> baseTime = new ArrayList<>();
        int rowInx = 0;
        for (AtomicTime item : atoms) {
            EffAtomicTime effAtomicTime = new EffAtomicTime();
            rowInx = rowInx + 1;
            effAtomicTime.setRowInx(rowInx);
            effAtomicTime.setStartTime(item.getStartTime());
            effAtomicTime.setEndTime(item.getEndTime());
            effAtomicTime.setDelt(item.getEndTime().getTime() - item.getStartTime().getTime());
            baseTime.add(effAtomicTime);
        }
        Integer rowInx1 = null;
        EffAtomicTime tempatomictime = baseTime.stream().filter(c -> c.getStartTime().getTime() <= startTime.getTime()
                && c.getEndTime().getTime() >= startTime.getTime()).findFirst().orElse(null);
        if (tempatomictime != null) {
            rowInx1 = tempatomictime.getRowInx();
        }
        Integer rowInx2 = null;

        tempatomictime = baseTime.stream().filter(c -> c.getStartTime().getTime() <= endTime.getTime()
                && c.getEndTime().getTime() >= endTime.getTime()).findFirst().orElse(null);
        if (tempatomictime != null) {
            rowInx2 = tempatomictime.getRowInx();
        }

        Integer tempTime = 0;
        Integer tempTime1 = 0;
        if (rowInx1 != null && rowInx2 != null) {
            final Integer temprowInx1 = rowInx1;
            final Integer temprowInx2 = rowInx2;
            if (rowInx1.equals(rowInx2)) {
                result = (int) (endTime.getTime() - startTime.getTime());
            } else if (rowInx2 - rowInx1 == 1) {
                EffAtomicTime tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx1)).findFirst().orElse(null);
                if (tempinfo != null) {
                    result = (int) (tempinfo.getEndTime().getTime() - startTime.getTime());
                } else {
                    result = 0;
                }
                tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx2)).findFirst().orElse(null);
                if (tempinfo != null) {
                    tempTime = (int) (endTime.getTime() - tempinfo.getStartTime().getTime());
                } else {
                    tempTime = 0;
                }
                result += tempTime;
            } else if (rowInx2 - rowInx1 > 1) {
                EffAtomicTime tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx1)).
                        findFirst().orElse(null);
                if (tempinfo != null) {
                    result = (int) (tempinfo.getEndTime().getTime() - startTime.getTime());
                } else {
                    result = 0;
                }
                tempTime1 = (int) (baseTime.stream().filter(c -> c.getRowInx() > temprowInx1 && c.getRowInx() < temprowInx2)
                        .mapToLong(EffAtomicTime::getDelt).sum());

                tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx2)).findFirst().
                        orElse(null);
                if (tempinfo != null) {
                    tempTime = (int) (endTime.getTime() - tempinfo.getStartTime().getTime());
                } else {
                    tempTime = 0;
                }
                result = result + tempTime1 + tempTime;
            } else {
                result = 0;
            }
        }

        if (rowInx1 == null && rowInx2 == null) {
            List<EffAtomicTime> tempinfolist = baseTime.stream().filter(c -> c.getStartTime().getTime() > startTime.getTime()).
                    collect(Collectors.toList());
            if (tempinfolist.size() > 0) {
                tempinfolist = tempinfolist.stream().sorted(Comparator.comparingInt(EffAtomicTime::getRowInx)).collect(Collectors.toList());
                EffAtomicTime tempinfo = tempinfolist.stream().findFirst().orElse(null);
                if (tempinfo != null) {
                    rowInx1 = tempinfo.getRowInx();
                }
            }
            tempinfolist = baseTime.stream().filter(c -> c.getEndTime().getTime() < endTime.getTime()).collect(Collectors.toList());
            if (tempinfolist.size() > 0) {
                tempinfolist = tempinfolist.stream().sorted(Comparator.comparingInt(EffAtomicTime::getRowInx).reversed()).collect(Collectors.toList());
                EffAtomicTime tempinfo = tempinfolist.stream().findFirst().orElse(null);
                if (tempinfo != null) {
                    rowInx2 = tempinfo.getRowInx();
                }
            }
            final Integer temprowInx1 = rowInx1;
            final Integer temprowInx2 = rowInx2;
            result = (int) baseTime.stream().filter(c -> c.getRowInx() > temprowInx1 && c.getRowInx() <= temprowInx2).mapToLong(c -> c.getDelt()).sum();
        }

        if (rowInx1 == null && rowInx2 != null) {
            final Integer temprowInx2 = rowInx2;
            EffAtomicTime tempinfo = baseTime.stream().filter(c -> c.getStartTime().getTime() > startTime.getTime()).
                    sorted(Comparator.comparingInt(EffAtomicTime::getRowInx)).findFirst().orElse(null);
            if (tempinfo != null) {
                rowInx1 = tempinfo.getRowInx();
            }
            final Integer temprowInx1 = rowInx1;
            if (!rowInx1.equals(rowInx2)) {
                result = (int) baseTime.stream().filter(c -> c.getRowInx() >= temprowInx1 && c.getRowInx() < temprowInx2).
                        mapToLong(EffAtomicTime::getDelt).sum();
            }
            tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx2)).findFirst().orElse(null);
            if (tempinfo != null) {
                tempTime = (int) (endTime.getTime() - tempinfo.getStartTime().getTime());
            }
            result += tempTime;
        }

        if (rowInx1 != null && rowInx2 == null) {
            final Integer temprowInx1 = rowInx1;
            EffAtomicTime tempinfo = baseTime.stream().filter(c -> c.getRowInx().equals(temprowInx1)).findFirst().orElse(null);
            if (tempinfo != null) {
                result = (int) (tempinfo.getEndTime().getTime() - startTime.getTime());
            }
            tempinfo = baseTime.stream().filter(c -> c.getEndTime().getTime() < endTime.getTime()).
                    sorted(Comparator.comparingInt(EffAtomicTime::getRowInx).reversed()).findFirst().orElse(null);
            if (tempinfo != null) {
                rowInx2 = tempinfo.getRowInx();
            }
            final Integer temprowInx2 = rowInx2;
            if (!rowInx1.equals(rowInx2)) {
                tempTime = (int) baseTime.stream().filter(c -> c.getRowInx() > temprowInx1 && c.getRowInx() <= temprowInx2).mapToLong(c -> c.getDelt()).sum();
            }
            result += tempTime;
        }
        return result;
    }

    private List<PmShcCalendarAtom> getAtomData(List<PmCalendarSource> dataSource) {
        List<PmShcCalendarAtom> result = new ArrayList<>();
        List<EffAtomicTime> inputData = new ArrayList<>();
        for (PmCalendarSource item : dataSource) {
            EffAtomicTime info = new EffAtomicTime();
            info.setPmShopCode(item.getPmShopCode());
            info.setWorkDay(item.getWorkDay());
            info.setShiftName(item.getShiftName());
            String strtime = DateUtils.format(item.getWorkDay(), DateUtils.DATE_PATTERN) + " " + item.getStartTimeShift();
            Date startTimeShift = DateUtils.stringToDate(strtime, DateUtils.DATE_TIME_PATTERN);
            info.setStartTimeShift(startTimeShift);
            String strendtime = DateUtils.format(DateUtils.addDateDays(item.getWorkDay(), 1), DateUtils.DATE_PATTERN)
                    + " " + item.getEndTimeShift();
            String strendtime1 = DateUtils.format(item.getWorkDay(), DateUtils.DATE_PATTERN) + " " + item.getEndTimeShift();

            Date endTimeShift = item.getIsDayAfter() ?
                    DateUtils.addDateMinutes(DateUtils.stringToDate(strendtime, DateUtils.DATE_TIME_PATTERN), item.getOverTime()) :
                    DateUtils.addDateMinutes(DateUtils.stringToDate(strendtime1, DateUtils.DATE_TIME_PATTERN), item.getOverTime());
            info.setEndTimeShift(endTimeShift);

            startTimeShift = DateUtils.stringToDate(DateUtils.format(item.getWorkDay(), DateUtils.DATE_PATTERN)
                    + " " + item.getStartTimeShift(), DateUtils.DATE_PATTERN);

            Date tempbreak = DateUtils.stringToDate(DateUtils.format(item.getWorkDay(), DateUtils.DATE_PATTERN)
                    + " " + item.getStartTimeBreak(), DateUtils.DATE_PATTERN);
            Date tempbreak1 = DateUtils.stringToDate(DateUtils.format(DateUtils.addDateDays(item.getWorkDay(), 1), DateUtils.DATE_PATTERN)
                    + " " + item.getStartTimeBreak(), DateUtils.DATE_PATTERN);
            Date startTimeBreakTemp = startTimeShift.getTime() <= tempbreak.getTime() ? tempbreak : tempbreak1;
            Date startTimeBreak = !StringUtils.hasText(item.getStartTimeBreak())
                    ? null : startTimeBreakTemp;

            info.setStartTimeBreak(startTimeBreak);

            Date tempendbreak = DateUtils.stringToDate(DateUtils.format(item.getWorkDay(), DateUtils.DATE_PATTERN)
                    + " " + item.getEndTimeBreak(), DateUtils.DATE_PATTERN);
            Date tempendbreak1 = DateUtils.stringToDate(DateUtils.format(DateUtils.addDateDays(item.getWorkDay(), 1), DateUtils.DATE_PATTERN)
                    + " " + item.getEndTimeBreak(), DateUtils.DATE_PATTERN);
            Date endTimeBreakTemp = startTimeShift.getTime() <= tempendbreak.getTime() ? tempendbreak : tempendbreak1;
            Date endTimeBreak = !StringUtils.hasText(item.getEndTimeBreak())
                    ? null : endTimeBreakTemp;
            info.setEndTimeBreak(endTimeBreak);
            info.setShcCalendarId(item.getShcCalendarId());
            inputData.add(info);
        }
        Collections.sort(inputData, (e1, e2) -> {
            long result1 = e1.getWorkDay().getTime() - e2.getWorkDay().getTime();
            if (result1 != 0) {
                return result1 > 0 ? -1 : 1;
            }
            result1 = e1.getStartTimeShift().getTime() - e2.getStartTimeShift().getTime();
            if (result1 != 0) {
                return result1 > 0 ? -1 : 1;
            }
            result1 = e1.getStartTimeBreak().getTime() - e2.getStartTimeBreak().getTime();
            return result1 > 0 ? -1 : 1;
        });

        Map<String, List<EffAtomicTime>> gpdata = inputData.stream().collect(
                Collectors.groupingBy(c -> c.getPmShopCode() + "_" + c.getWorkDay() + "_" + c.getShiftName())
        );

        Integer rowInx = 0;
        List<EffAtomicTime> groupitems = new ArrayList<>();
        for (Map.Entry<String, List<EffAtomicTime>> st : gpdata.entrySet()) {
            List<EffAtomicTime> sdt = st.getValue();
            for (EffAtomicTime item : sdt) {
                EffAtomicTime info = new EffAtomicTime();
                rowInx = rowInx + 1;
                info.setPmShopCode(item.getPmShopCode());
                info.setWorkDay(item.getWorkDay());
                info.setShiftName(item.getShiftName());
                info.setStartTimeShift(item.getStartTimeShift());
                info.setEndTimeShift(item.getEndTimeShift());
                info.setStartTimeBreak(item.getStartTimeBreak());
                info.setEndTimeBreak(item.getEndTimeBreak());
                info.setShcCalendarId(item.getShcCalendarId());
                groupitems.add(info);
            }
        }

        List<EffAtomicTime> groupitemagain = new ArrayList<>();
        Map<String, List<EffAtomicTime>> gpdataagain = groupitems.stream().collect(
                Collectors.groupingBy(c -> c.getPmShopCode() + "_" + c.getWorkDay() + "_" + c.getShiftName())
        );
        for (Map.Entry<String, List<EffAtomicTime>> st : gpdataagain.entrySet()) {
            List<EffAtomicTime> sdt = st.getValue();
            for (EffAtomicTime item : sdt) {
                EffAtomicTime info = new EffAtomicTime();
                info.setRowInx(item.getRowInx());
                info.setCountRow(sdt.size());
                info.setPmShopCode(item.getPmShopCode());
                info.setWorkDay(item.getWorkDay());
                info.setShiftName(item.getShiftName());
                info.setStartTimeShift(item.getStartTimeShift());
                info.setEndTimeShift(item.getEndTimeShift());
                info.setStartTimeBreak(item.getStartTimeBreak());
                info.setEndTimeBreak(item.getEndTimeBreak());
                info.setShcCalendarId(item.getShcCalendarId());
                groupitemagain.add(info);
            }
        }

        groupitemagain = groupitemagain.stream().sorted
                        (Comparator.comparing(EffAtomicTime::getPmShopCode)
                                .thenComparing(EffAtomicTime::getWorkDay)
                                .thenComparing(EffAtomicTime::getStartTimeShift)
                                .thenComparing(EffAtomicTime::getStartTimeBreak))
                .collect(Collectors.toList());

        Integer rowNum = 0;
        List<EffAtomicTime> groupitemslast = new ArrayList<>();
        for (EffAtomicTime items : groupitemagain) {
            EffAtomicTime info = new EffAtomicTime();
            rowNum = rowNum + 1;
            info.setRowNum(rowNum);
            info.setRowInx(items.getRowInx());
            info.setCountRow(items.getRowNum());
            info.setPmShopCode(items.getPmShopCode());
            info.setWorkDay(items.getWorkDay());
            info.setShiftName(items.getShiftName());
            info.setStartTimeShift(items.getStartTimeShift());
            info.setEndTimeShift(items.getEndTimeShift());
            info.setStartTimeBreak(items.getStartTimeBreak());
            info.setEndTimeBreak(items.getEndTimeBreak());
            info.setShcCalendarId(items.getShcCalendarId());
            groupitemslast.add(info);
        }

        Date startTime = new Date();
        Date endTime = new Date();
        for (EffAtomicTime item : groupitemslast) {
            if (item.getCountRow() == 1) {
                if (item.getStartTimeBreak() != null) {
                    //StartTimeShift
                    startTime = item.getStartTimeShift();
                    endTime = item.getEndTimeShift();
                    PmShcCalendarAtom addinfo = new PmShcCalendarAtom();
                    addinfo.setPmShopCode(item.getPmShopCode());
                    addinfo.setWorkDay(item.getWorkDay());
                    addinfo.setShiftName(item.getShiftName());
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    addinfo.setWorkTime((int) (endTime.getTime() - startTime.getTime()));
                    addinfo.setShcCalendarId(item.getShcCalendarId());
                    addinfo.setShiftStartTime(item.getStartTimeShift());
                    addinfo.setShiftEndTime(item.getEndTimeShift());
                    result.add(addinfo);
                } else {
                    startTime = item.getStartTimeShift();
                    endTime = item.getStartTimeBreak();
                    PmShcCalendarAtom addinfo = new PmShcCalendarAtom();
                    addinfo.setPmShopCode(item.getPmShopCode());
                    addinfo.setWorkDay(item.getWorkDay());
                    addinfo.setShiftName(item.getShiftName());
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    addinfo.setWorkTime((int) (endTime.getTime() - startTime.getTime()));
                    addinfo.setShcCalendarId(item.getShcCalendarId());
                    addinfo.setShiftStartTime(item.getStartTimeShift());
                    addinfo.setShiftEndTime(item.getEndTimeShift());
                    result.add(addinfo);

                    startTime = item.getEndTimeBreak();
                    endTime = item.getEndTimeShift();
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    result.add(addinfo);
                }
            } else {
                if (item.getRowInx() == 1) {
                    startTime = item.getStartTimeShift();
                    endTime = item.getStartTimeBreak();
                    PmShcCalendarAtom addinfo = new PmShcCalendarAtom();
                    addinfo.setPmShopCode(item.getPmShopCode());
                    addinfo.setWorkDay(item.getWorkDay());
                    addinfo.setShiftName(item.getShiftName());
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    addinfo.setWorkTime((int) (endTime.getTime() - startTime.getTime()));
                    addinfo.setShcCalendarId(item.getShcCalendarId());
                    addinfo.setShiftStartTime(item.getStartTimeShift());
                    addinfo.setShiftEndTime(item.getEndTimeShift());
                    result.add(addinfo);
                    startTime = item.getEndTimeBreak();
                } else if (item.getRowInx().equals(item.getCountRow())) {
                    endTime = item.getStartTimeBreak();
                    PmShcCalendarAtom addinfo = new PmShcCalendarAtom();
                    addinfo.setPmShopCode(item.getPmShopCode());
                    addinfo.setWorkDay(item.getWorkDay());
                    addinfo.setShiftName(item.getShiftName());
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    addinfo.setWorkTime((int) (endTime.getTime() - startTime.getTime()));
                    addinfo.setShcCalendarId(item.getShcCalendarId());
                    addinfo.setShiftStartTime(item.getStartTimeShift());
                    addinfo.setShiftEndTime(item.getEndTimeShift());
                    result.add(addinfo);
                    startTime = item.getEndTimeBreak();
                    endTime = item.getEndTimeShift();
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    result.add(addinfo);
                } else {
                    endTime = item.getStartTimeBreak();
                    PmShcCalendarAtom addinfo = new PmShcCalendarAtom();
                    addinfo.setPmShopCode(item.getPmShopCode());
                    addinfo.setWorkDay(item.getWorkDay());
                    addinfo.setShiftName(item.getShiftName());
                    addinfo.setStartTime(startTime);
                    addinfo.setEndTime(endTime);
                    addinfo.setWorkTime((int) (endTime.getTime() - startTime.getTime()));
                    addinfo.setShcCalendarId(item.getShcCalendarId());
                    addinfo.setShiftStartTime(item.getStartTimeShift());
                    addinfo.setShiftEndTime(item.getEndTimeShift());
                    result.add(addinfo);
                    startTime = item.getEndTimeBreak();
                }
            }
        }
        return result;
    }

    @Override
    public ShiftDTO getCurrentShiftInfo(String lineCode) {
        return getShiftInfo(lineCode, new Date());
    }

    private PmWorkShopEntity getShopByLineCode(String lineCode) {
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        List<PmLineEntity> lineEntities = allPm.getLines();
        if (CollectionUtils.isEmpty(lineEntities)) {
            return null;
        }
        List<PmLineEntity> filterLineList = lineEntities.stream().filter(c -> c.getLineCode().equals(lineCode)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterLineList)) {
            return null;
        }
        PmLineEntity line = filterLineList.get(0);
        List<PmWorkShopEntity> shops = allPm.getShops();
        if (CollectionUtils.isEmpty(shops)) {
            return null;
        }
        List<PmWorkShopEntity> filterShop = shops.stream().filter(c -> c.getId().equals(line.getPrcPmWorkshopId())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterShop)) {
            return null;
        }
        return filterShop.get(0);
    }

    @Override
    public ShiftDTO getShiftInfo(String lineCode, Date dateTime) {
        //找线体的日历，如果线体日历没找到，就找车间日历
        String minWorkDay = DateUtils.format(DateUtils.addDateDays(dateTime, -3), "yyyy-MM-dd");
        PmWorkShopEntity shop = getShopByLineCode(lineCode);
        String pmShopCode = shop == null ? "" : shop.getWorkshopCode();
        List<ShiftDTO> lst = pmShcCalendarMapper.getShiftInfo(pmShopCode, lineCode, minWorkDay);
        if (lst == null || lst.size() == 0) {
            lst = pmShcCalendarMapper.getShiftInfo(pmShopCode, "", minWorkDay);
            if (lst == null || lst.size() == 0) {
                return null;
            }
        }
        List<ShiftDTO> result = new ArrayList<>();
        for (ShiftDTO et : lst) {
            String shiftStartdt = "";
            if (!et.getIsDayBefore()) {
                shiftStartdt = DateUtils.format(et.getWorkDay(), "yyyy-MM-dd") + " " + et.getStartTime() + ":00";
            } else {
                shiftStartdt = DateUtils.format(DateUtils.addDateDays(et.getWorkDay(), -1), "yyyy-MM-dd") + " " + et.getStartTime() + ":00";
            }
            et.setShiftStartTime(DateUtils.parse(shiftStartdt, DateUtils.DATE_TIME_PATTERN));

            String shiftEnddt = "";
            if (!et.getIsDayAfter()) {
                shiftEnddt = DateUtils.format(et.getWorkDay(), "yyyy-MM-dd") + " " + et.getEndTime() + ":00";
            } else {
                shiftEnddt = DateUtils.format(DateUtils.addDateDays(et.getWorkDay(), 1), "yyyy-MM-dd") + " " + et.getEndTime() + ":00";
            }
            et.setShiftEndTime(DateUtils.parse(shiftEnddt, DateUtils.DATE_TIME_PATTERN));
            if (et.getShiftEndTime().getTime() >= dateTime.getTime() && Objects.equals(et.getPmShopCode(), pmShopCode)) {
                result.add(et);
            }
        }
        result.sort(Comparator.comparing(ShiftDTO::getShiftEndTime));
        return result.stream().findFirst().orElse(null);
    }


    /**
     * 获取已经排班记录
     *
     * @param calendarParamInfo 查询参数
     * @return List<PmShcCalendarEntity>
     */
    @Override
    public List<PmShcCalendarEntity> getPmShcCalendarInfos(CalendarParamDTO calendarParamInfo) {
        Map daoReq = Maps.newHashMapWithExpectedSize(6);
        daoReq.put("workDayYear", calendarParamInfo.getYear());

        if (calendarParamInfo.getDay() > 0) {
            daoReq.put("workDayDay", calendarParamInfo.getDay());
            daoReq.put("workDayMonth", calendarParamInfo.getMonth());
        } else {
            daoReq.put("workDayDay", Integer.valueOf(0));
            List<Integer> months = new ArrayList<>();
            months.add(calendarParamInfo.getMonth() - 1);
            months.add(calendarParamInfo.getMonth());
            months.add(calendarParamInfo.getMonth() + 1);
            daoReq.put("workDayMonth", months);
        }
        if (org.springframework.util.StringUtils.hasText(calendarParamInfo.getPmLineCode())) {
            daoReq.put("pmLineCode", calendarParamInfo.getPmLineCode());
        } else if (org.springframework.util.StringUtils.hasText(calendarParamInfo.getPmShopCode())) {
            //如果查询线体，那就不需要shopCode了，因此要用else if
            daoReq.put("pmShopCode", calendarParamInfo.getPmShopCode());
        }

        List<PmShcCalendarEntity> pmShcCalendarEntities = pmShcCalendarMapper.getPmShcCalendarInfos(daoReq);
        for (PmShcCalendarEntity each : pmShcCalendarEntities) {
            if (org.springframework.util.StringUtils.hasText(each.getLineCode())) {
                each.setTypeFlag(2);//线体代码有值的话，就i说明是线体
            } else {
                each.setTypeFlag(1);//线体代码没有值，说明就是车间
            }
        }
        pmShcCalendarEntities.forEach(c -> c.setTypeFlag(1));
        return pmShcCalendarEntities;

       /* if (!org.springframework.util.StringUtils.hasText(calendarParamInfo.getPmLineCode())) {
            daoReq.put("pmShopCode", calendarParamInfo.getPmShopCode());
            List<PmShcCalendarEntity> pmShcCalendarEntities = pmShcCalendarMapper.getPmShcCalendarInfos(daoReq);
            pmShcCalendarEntities.forEach(c -> c.setTypeFlag(1));
            return pmShcCalendarEntities;
        } else {
            daoReq.put("lineCode", calendarParamInfo.getPmLineCode());
            List<PmShcCalendarEntity> pmShcCalendarEntities = pmShcCalendarAreaMapper.getPmShcCalendarInfos(daoReq);
            pmShcCalendarEntities.forEach(c -> c.setTypeFlag(2));
            return pmShcCalendarEntities;
        }*/

    }

    /**
     * 获取工作时间
     *
     * @param startWorkDay 开始时间
     * @param shopCode     车间code
     * @param getDays      天数
     * @return 工作时间列表
     */
    @Override
    public List<ShcWorkTimeInfo> getWorkTimes(Date startWorkDay, String shopCode, Integer getDays) {
        List<ShcWorkTimeInfo> workTimes = new ArrayList<>();
        List<ShcCalendarDetailInfo> shcCalendarDetailInfos = getCalendarDetail(startWorkDay, shopCode, getDays);
        for (ShcCalendarDetailInfo shcCalendarDetailInfo : shcCalendarDetailInfos) {
            Date startTime = shcCalendarDetailInfo.getShiftStartTime();
            Date endTime;
            for (ShcBreakDetailInfo breakItemDetail : shcCalendarDetailInfo.getBreakItemDetails()) {
                endTime = breakItemDetail.getStartTime();
                ShcWorkTimeInfo addInfo = new ShcWorkTimeInfo();
                addInfo.setShiftId(shcCalendarDetailInfo.getShiftId());
                addInfo.setProductIvity(shcCalendarDetailInfo.getProductIvity());
                addInfo.setWorkDay(shcCalendarDetailInfo.getWorkDay());
                addInfo.setStartTime(startTime);
                addInfo.setEndTime(endTime);
                workTimes.add(addInfo);
                startTime = breakItemDetail.getEndTime();
            }
            endTime = DateUtils.addDateMinutes(shcCalendarDetailInfo.getShiftEndTime(), shcCalendarDetailInfo.getOverTime());
            ShcWorkTimeInfo workTimeInfo = new ShcWorkTimeInfo();
            workTimeInfo.setShiftId(shcCalendarDetailInfo.getShiftId());
            workTimeInfo.setProductIvity(shcCalendarDetailInfo.getProductIvity());
            workTimeInfo.setWorkDay(shcCalendarDetailInfo.getWorkDay());
            workTimeInfo.setStartTime(startTime);
            workTimeInfo.setEndTime(endTime);
            workTimes.add(workTimeInfo);
        }
        return workTimes;
    }

    public List<ShcCalendarDetailInfo> getCalendarDetail(Date startWorkDay, String shopCode, Integer getDays) {
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        PmWorkShopEntity shopInfo = allPm.getShops().stream()
                .filter(s -> s.getWorkshopCode().equals(shopCode)).findFirst().orElse(null);
        if (shopInfo == null) {
            return new ArrayList<>();
        }
        return getDetails(startWorkDay, shopInfo.getWorkshopCode(), getDays);
    }

    public List<ShcCalendarDetailInfo> getDetails(Date startWorkDay, String shopCode, Integer getDays) {
        List<ShcCalendarDetailInfo> orderShiftInfos = new ArrayList<>();
        // 去除时分秒
        startWorkDay = DateUtils.stringToDate(DateUtils.format(startWorkDay, DateUtils.DATE_PATTERN), DateUtils.DATE_PATTERN);

        List<PmShcCalendarEntity> pmShcCalendarEntityList = getEntityByStartWorkDay(startWorkDay);
        if (pmShcCalendarEntityList == null) {
            return new ArrayList<>();
        }
        Date beginDt = pmShcCalendarEntityList.stream().map(PmShcCalendarEntity::getWorkDay).findFirst().orElse(null);
        if (beginDt == null) {
            return new ArrayList<>();
        }

        Date endDt = DateUtils.addDateDays(beginDt, getDays);
        List<ShcCalTempInfo> datas = pmShcCalendarMapper.getShcCalTempInfo(shopCode, beginDt, endDt);

        //生成排班信息
        for (ShcCalTempInfo data : datas) {
            ShcCalendarDetailInfo orderShiftInfo = orderShiftInfos.stream()
                    .filter(m -> data.getId().equals(m.getId()))
                    .findFirst().orElse(null);
            if (orderShiftInfo == null) {
                orderShiftInfo = new ShcCalendarDetailInfo();
                orderShiftInfo.setId(data.getId());
                orderShiftInfo.setShiftId(data.getShiftId());
                orderShiftInfo.setShiftName(data.getShiftName());
                orderShiftInfo.setProductIvity(data.getProductivity());
                orderShiftInfo.setOverTime(data.getOvertime());
                orderShiftInfo.setWorkDay(data.getWorkDay());
                String shiftStartTime = DateUtils.format(orderShiftInfo.getWorkDay(), DateUtils.DATE_PATTERN) + " "
                        + (data.getShiftStartTime().length() < 8 ? data.getShiftStartTime() + ":00" : data.getShiftStartTime());
                orderShiftInfo.setShiftStartTime(DateUtils.stringToDate(shiftStartTime, DateUtils.DATE_TIME_PATTERN));
                String shiftEndTime = DateUtils.format(orderShiftInfo.getWorkDay(), DateUtils.DATE_PATTERN) + " "
                        + (data.getShiftEndTime().length() < 8 ? data.getShiftEndTime() + ":00" : data.getShiftEndTime());
                orderShiftInfo.setShiftEndTime(DateUtils.stringToDate(shiftEndTime, DateUtils.DATE_TIME_PATTERN));
                if (data.getIsDayAfter())//现在只有向后跨天
                {
                    orderShiftInfo.setShiftEndTime(DateUtils.addDateDays(orderShiftInfo.getShiftEndTime(), 1));
                }
                orderShiftInfos.add(orderShiftInfo);
            }
            //是否配置了休息时间
            ShcBreakDetailInfo breakDetail = null;
            if (StringUtils.hasText(data.getBreakStartTime())) {
                if (breakDetail == null) {
                    breakDetail = new ShcBreakDetailInfo();
                }
                String startTime = DateUtils.format(orderShiftInfo.getWorkDay(), DateUtils.DATE_PATTERN) + " "
                        + (data.getBreakStartTime().length() < 8 ? data.getBreakStartTime() + ":00" : data.getBreakStartTime());
                breakDetail.setStartTime(DateUtils.stringToDate(startTime, DateUtils.DATE_TIME_PATTERN));
                if (breakDetail.getStartTime().getTime() < orderShiftInfo.getShiftStartTime().getTime()) {
                    Date addStartTime = DateUtils.addDateDays(breakDetail.getStartTime(), 1);
                    breakDetail.setStartTime(addStartTime);
                }
            }

            if (StringUtils.hasText(data.getBreakEndTime())) {
                if (breakDetail == null) {
                    breakDetail = new ShcBreakDetailInfo();
                }
                String endtime = DateUtils.format(orderShiftInfo.getWorkDay(), DateUtils.DATE_PATTERN) + " "
                        + (data.getBreakEndTime().length() < 8 ? data.getBreakEndTime() + ":00" : data.getBreakEndTime());
                breakDetail.setEndTime(DateUtils.stringToDate(endtime, DateUtils.DATE_TIME_PATTERN));
                if (breakDetail.getEndTime().getTime() < orderShiftInfo.getShiftStartTime().getTime()) {
                    Date addEndTime = DateUtils.addDateDays(breakDetail.getEndTime(), 1);
                    breakDetail.setEndTime(addEndTime);
                }
            }

            if (breakDetail != null) {
                orderShiftInfo.getBreakItemDetails().add(breakDetail);
            }
        }

        //对休息时间排序
        for (ShcCalendarDetailInfo orderShiftInfo : orderShiftInfos) {
            //排序(测试后可以用Lamd排序)
            List<ShcBreakDetailInfo> collect = orderShiftInfo.getBreakItemDetails().stream().sorted((ShcBreakDetailInfo o1, ShcBreakDetailInfo o2) -> {
                        Date date1 = o1.getStartTime();
                        Date date2 = o2.getStartTime();
                        return Long.compare(date2.getTime(), date1.getTime());
                    }
            ).collect(Collectors.toList());
            orderShiftInfo.setBreakItemDetails(collect);
        }
        orderShiftInfos.sort(Comparator.comparing(ShcCalendarDetailInfo::getWorkDay).thenComparing(ShcCalendarDetailInfo::getShiftStartTime));
        return orderShiftInfos;
    }

    private List<PmShcCalendarEntity> getEntityByStartWorkDay(Date startWorkDay) {
        QueryWrapper<PmShcCalendarEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.ge(PmShcCalendarEntity::getWorkDay, startWorkDay);
        lambdaQueryWrapper.orderByAsc(PmShcCalendarEntity::getWorkDay);
        return this.selectList(queryWrapper);
    }

    @Override
    public void setExcelColumnNames(Map<String, String> columnNames) {
        if (columnNames == null || columnNames.size() == 0) {
            columnNames = new LinkedHashMap<>();
            columnNames.put("workshopCode", "车间代码");
            columnNames.put("workshopName", "车间名称");
            columnNames.put("shcShiftCode", "班次代码");
            columnNames.put("shcShiftName", "班次名称");
            //  columnNames.put("workDay", "日期");
            columnNames.put("strWorkDay", "日期");
          /*  columnNames.put("productivity", "产能");
            columnNames.put("shcShiftStartDt", "班次开始时间");
            columnNames.put("shcShiftEndDt", "班次结束时间");
            columnNames.put("overtime", "加班时间");*/
        }
        super.setExcelColumnNames(columnNames);
    }

    private List<Map<String, String>> exportExcelColumnNames() {
        //对应结构
        List<Map<String, String>> columnNames = new ArrayList<>();
        columnNames.add(exportCalendarColumnNames());
        columnNames.add(exportCalendarAreaColumnNames());

        return columnNames;
    }

    private Map<String, String> exportCalendarColumnNames() {
        Map<String, String> keyValuePairs = Maps.newHashMapWithExpectedSize(5);
        keyValuePairs.put("workshopCode", "车间代码");
        keyValuePairs.put("workshopName", "车间名称");
        keyValuePairs.put("shcShiftCode", "班次代码");
        keyValuePairs.put("shcShiftName", "班次名称");
        // keyValuePairs.put("workDay", "日期");
        keyValuePairs.put("strWorkDay", "日期");

       /* keyValuePairs.put("productivity", "产能");
        keyValuePairs.put("shcShiftStartDt", "班次开始时间");
        keyValuePairs.put("shcShiftEndDt", "班次结束时间");
        keyValuePairs.put("overtime", "加班时间");*/

        return keyValuePairs;
    }

    private Map<String, String> exportCalendarAreaColumnNames() {
        Map<String, String> keyValuePairs = Maps.newHashMapWithExpectedSize(7);

        keyValuePairs.put("lineCode", "线体代码");
        keyValuePairs.put("lineName", "线体名称");
        keyValuePairs.put("workshopCode", "车间代码");
        keyValuePairs.put("workshopName", "车间名称");
        keyValuePairs.put("shcShiftCode", "班次代码");
        keyValuePairs.put("shcShiftName", "班次名称");
        //   keyValuePairs.put("workDay", "日期");
        keyValuePairs.put("strWorkDay", "日期");
       /* keyValuePairs.put("productivity", "产能");
        keyValuePairs.put("shcShiftStartDt", "班次开始时间");
        keyValuePairs.put("shcShiftEndDt", "班次结束时间");
        keyValuePairs.put("overtime", "加班时间(分钟)");*/

        return keyValuePairs;
    }

    private List<Map<String, String>> importExcelColumnNames() {
        //对应结构
        List<Map<String, String>> columnNames = new ArrayList<>();
        columnNames.add(importCalendarColumnNames());
        columnNames.add(importCalendarAreaColumnNames());

        return columnNames;
    }

    private Map<String, String> importCalendarColumnNames() {
        Map<String, String> colshop = Maps.newHashMapWithExpectedSize(5);
        colshop.put("workshopCode", "车间代码");
        colshop.put("workshopName", "车间名称");
        colshop.put("shcShiftCode", "班次代码");
        colshop.put("shcShiftName", "班次名称");
        //  colshop.put("workDay", "日期");
        colshop.put("strWorkDay", "日期");

       /* colshop.put("workshopName", "车间名称");
        colshop.put("shcShiftCode", "班次代码");
        colshop.put("shcShiftName", "班次名称");
        */

        return colshop;
    }

    private Map<String, String> importCalendarAreaColumnNames() {
        Map<String, String> colLine = Maps.newHashMapWithExpectedSize(7);
        colLine.put("lineCode", "线体代码");
        colLine.put("lineName", "线体名称");
        colLine.put("workshopCode", "车间代码");
        colLine.put("workshopName", "车间名称");
        colLine.put("shcShiftCode", "班次代码");
        colLine.put("shcShiftName", "班次名称");
        //colLine.put("workDay", "日期");
        colLine.put("strWorkDay", "日期");

       /* colLine.put("lineName", "线体名称");
        colLine.put("workshopName", "车间名称");
        colLine.put("shcShiftCode", "班次代码");
        colLine.put("shcShiftName", "班次名称");
        */

        return colLine;
    }

    /**
     * 获取导入模板
     *
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
        //sheet名称
        List<String> sheetNames = new ArrayList<>();
        sheetNames.add("车间日历信息");
        sheetNames.add("线体日历信息");
        //对应结构
       /* Map<String, String> colshop = Maps.newHashMapWithExpectedSize(4);
        colshop.put("workshopName", "车间名称");
        colshop.put("shcShiftCode", "班次代码");
        colshop.put("shcShiftName", "班次名称");
        colshop.put("workDay", "日期");

        Map<String, String> colLine = Maps.newHashMapWithExpectedSize(5);
        colLine.put("lineName", "线体名称");
        colLine.put("workshopName", "车间名称");
        colLine.put("shcShiftCode", "班次代码");
        colLine.put("shcShiftName", "班次名称");
        colLine.put("workDay", "日期");*/

        List<Map<String, String>> columnNames = importExcelColumnNames();
       /* columnNames.add(colshop);
        columnNames.add(colLine);*/

        InkelinkExcelUtils.createEmptyExcel(columnNames, sheetNames, fileName, response);
    }

    /**
     * 导出数据
     *
     * @param conditions
     * @param sorts
     * @param fileName
     * @param response
     * @throws IOException
     */
    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        /*如果只是传车间编号：导出当前车间和当前车间下面的所有线体
        如果传车间编号+线体编号：导出当前线体+上面的车间数据
        */
        //sheet名称
        List<String> sheetNames = new ArrayList<>();
        sheetNames.add("车间日历信息");
        sheetNames.add("线体日历信息");
        //对应结构
        List<Map<String, String>> columNames = exportExcelColumnNames();

        //报表数据
        List<List<Map<String, Object>>> allExcelDatas = new ArrayList<>();

        //导出线体日历，同时要将其车间的日历的日历一起导出来；导出车间日历，那就只需要导出选中车间的日历数据

        //先导出车间数据（要将lineCode去掉条件）
        List<ConditionDto> conditionsCalendar = conditions.stream().filter(c -> !c.getColumnName().equalsIgnoreCase("lineCode") && !c.getColumnName().equalsIgnoreCase("LINE_CODE")).collect(Collectors.toList());
        //查询工厂信息，要加上lineName=“”，否则将工厂下面的线体日历都查询出来了
        ConditionDto lineCodeCondition = new ConditionDto("lineCode", "", ConditionOper.Equal);
        conditionsCalendar.add(lineCodeCondition);
        List<PmShcCalendarEntity> pmShcCalendarEntities = this.getData(conditionsCalendar, sorts, false);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!CollectionUtils.isEmpty(pmShcCalendarEntities)) {
            pmShcCalendarEntities.forEach(c -> c.setStrWorkDay(formatter.format(c.getWorkDay())));
        }
        List<Map<String, Object>> excelDatas1 = InkelinkExcelUtils.getListMap(pmShcCalendarEntities);
        allExcelDatas.add(excelDatas1);

        //lineCode有值的话，就导出线体数据，此时的workshopCode必定也是这个线体的shopWorkCode
        if (conditions.stream().anyMatch(c -> c.getColumnName().equalsIgnoreCase("lineCode"))) {
            List<PmShcCalendarEntity> pmShcCalendarLineEntities = this.getData(conditions, sorts, false);
            if (!CollectionUtils.isEmpty(pmShcCalendarLineEntities)) {
                pmShcCalendarLineEntities.forEach(c -> c.setStrWorkDay(formatter.format(c.getWorkDay())));
            }
            List<Map<String, Object>> excelDatas2 = InkelinkExcelUtils.getListMap(pmShcCalendarLineEntities);
            allExcelDatas.add(excelDatas2);
        }

        InkelinkExcelUtils.exportSheets(sheetNames, columNames, allExcelDatas, fileName, response);
    }


    /**
     * 导入
     *
     * @param is
     * @throws Exception
     */
    @Override
    public void importExcel(InputStream is) throws Exception {
        List<Map<String, String>> fieldParams = importExcelColumnNames();
        List<List<Map<String, String>>> dicDatasFromExcel = InkelinkExcelUtils.importMultipleSheetExcel(is);
        if (!CollectionUtil.isEmpty(dicDatasFromExcel)) {
            validImportDatas(dicDatasFromExcel, fieldParams);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            List<PmShcCalendarEntity> pmShcCalendarEntities = this.convertExcelDataToEntity(dicDatasFromExcel.get(0));
            if (!CollectionUtils.isEmpty(pmShcCalendarEntities)) {
                for (PmShcCalendarEntity each : pmShcCalendarEntities) {
                    if (each.getWorkDay() == null && org.springframework.util.StringUtils.hasText(each.getStrWorkDay())) {
                        each.setWorkDay(formatter.parse(each.getStrWorkDay()));
                    }
                }
            }
            pmShcCalendarEntities = pmShcCalendarEntities.stream()
                    .filter(c -> org.springframework.util.StringUtils.hasText(c.getWorkshopName()) && c.getWorkDay() != null)
                    .collect(Collectors.toList());

            List<PmShcCalendarEntity> pmShcCalendarLineEntities = this.convertExcelDataToEntity(dicDatasFromExcel.get(1));
            if (!CollectionUtils.isEmpty(pmShcCalendarLineEntities)) {
                for (PmShcCalendarEntity each : pmShcCalendarLineEntities) {
                    if (each.getWorkDay() == null && org.springframework.util.StringUtils.hasText(each.getStrWorkDay())) {
                        each.setWorkDay(formatter.parse(each.getStrWorkDay()));
                    }
                    each.setTypeFlag(2);//线体
                }
                pmShcCalendarLineEntities = pmShcCalendarLineEntities.stream()
                        .filter(c -> org.springframework.util.StringUtils.hasText(c.getLineName()) && c.getWorkDay() != null)
                        .collect(Collectors.toList());
                pmShcCalendarEntities.addAll(pmShcCalendarLineEntities);
            }
            saveExcelData(pmShcCalendarEntities);

           /* this.batchSaveExcelData(dicDatasFromExcel.get(0));//批量保存车间日历
            pmShcCalendarAreaService.batchSaveExcelData(dicDatasFromExcel.get(1));//批量保存线体日历*/
        }
        is.close();
    }

    /*public void batchSaveExcelData(List<Map<String, String>> data) throws Exception {
       List<PmShcCalendarEntity> pmShcCalendarEntities = this.convertExcelDataToEntity(data);

        saveExcelData(pmShcCalendarEntities);
    }*/

    /**
     * 处理即将导出的数据
     *
     * @param datas
     */
    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("workDay") && data.get("workDay") != null) {
                data.put("workDay", DateUtils.format((Date) data.get("workDay"), "yyyy-MM-dd"));
            }
        }
    }

    /**
     * 导入前验证
     */
    @Override
    public void validImportDatas(List<List<Map<String, String>>> datas, List<Map<String, String>> fieldParam) {
        if (CollectionUtil.isEmpty(datas) || datas.size() != 2) {
            throw new InkelinkException("导入数据格式有错！");
        }
        super.validImportDatas(datas, fieldParam);

        //验证必填
//        for (int i = 0; i < datas.size(); i++)
//        {
//            Map<String, String> data = datas.get(i);
//            ValidExcelDataRequire(_excelColumnNames["ShcShiftName"], i + 1, data["ShcShiftName"]);
//            ValidExcelDataRequire(_excelColumnNames["PmShopName"], i + 1, data["PmShopName"]);
//            ValidExcelDataRequire(_excelColumnNames["Productivity"], i + 1, data["Productivity"]);
//            ValidExcelDataRequire(_excelColumnNames["WorkDay"], i + 1, data["WorkDay"]);
//            ValidExcelDataIsDateTime(_excelColumnNames["WorkDay"], i + 1, data["WorkDay"]);
//            ValidExcelDataIsInt(_excelColumnNames["Productivity"], i + 1, data["Productivity"]);
//            ValidExcelDataIsInt(_excelColumnNames["Overtime"], i + 1, data["Overtime"]);
//        }
    }

    /**
     * 保存导入
     */
    @Override
    public void saveExcelData(List<PmShcCalendarEntity> entities) {
        checkOverdue(entities);
        checkDataRepeat(entities);
        updateShopInfo(entities);
        updateLineInfo(entities);
        updateShiftInfo(entities);
        delExistsData(entities);
        super.saveExcelData(entities);
    }

    /**
     * 检测过期
     */
    void checkOverdue(List<PmShcCalendarEntity> entities) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long todayZero = calendar.getTimeInMillis();

        long i = entities.stream().filter(o -> o.getWorkDay().getTime() < todayZero).count();
        if (i > 0) {
            throw new InkelinkException("导入的日历中存在过期的数据");
        }
    }

    /**
     * 验证重复
     */
    void checkDataRepeat(List<PmShcCalendarEntity> entities) {
        List<PmShcCalendarEntity> shopEntities = entities.stream().filter(c -> c.getTypeFlag() != 2).collect(Collectors.toList());
        List<PmShcCalendarEntity> lineEntities = entities.stream().filter(c -> c.getTypeFlag() == 2).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(shopEntities)) {
            if (shopEntities.stream().collect(Collectors.groupingBy(c -> c.getWorkshopName() + Constant.SPLIT_JIN + c.getShcShiftName() + Constant.SPLIT_JIN + DateUtils.format(c.getWorkDay(), DateUtils.DATE_PATTERN)
                    , Collectors.counting())).values().stream().anyMatch(c -> c > 1)) {
                throw new InkelinkException("在同一天存在相同车间相同班次的数据");
            }
        }
        if (!CollectionUtils.isEmpty(lineEntities)) {
            if (lineEntities.stream().collect(Collectors.groupingBy(c -> c.getLineName() + Constant.SPLIT_JIN + c.getShcShiftName() + Constant.SPLIT_JIN + DateUtils.format(c.getWorkDay(), DateUtils.DATE_PATTERN)
                    , Collectors.counting())).values().stream().anyMatch(c -> c > 1)) {
                throw new InkelinkException("在同一天存在相同线体相同班次的数据");
            }
        }
        //后面会将数据库的重复数据删掉
        /*List<String> pmShopNameCode = entities.stream().map(PmShcCalendarEntity::getWorkShopCode).collect(Collectors.toList());
        Map daoReq = Maps.newHashMapWithExpectedSize(6);
        daoReq.put("pmShopNameCode", pmShopNameCode);
        List<PmShcCalendarEntity> dbShcCalendarEntities = pmShcCalendarMapper.getPmShcCalendarInfos(daoReq);

        if (!CollectionUtil.isEmpty(dbShcCalendarEntities)) {
            List<PmShcCalendarEntity> result = new ArrayList<>();
            for (PmShcCalendarEntity each : entities) {
                if (dbShcCalendarEntities.stream().noneMatch(c -> c.getWorkShopCode().equals(each.getWorkShopCode())
                        && c.getShcShiftCode().equals(each.getShcShiftCode())
                        && c.getWorkDay().equals(each.getWorkDay()))) {
                    //车间当天不能有重复的班次
                    result.add(each);
                }
            }
            return result;
        } else {
            return entities;
        }*/

        //var datas = entities.GroupBy(o => new { PmShopName = o.PmShopName, ShcShiftName = o.ShcShiftName, WorkDay = o.WorkDay })
        //       .Select(a => new { PmShopName = a.Key.PmShopName, ShcShiftName = a.Key.ShcShiftName, WorkDay = a.Key.WorkDay, Count = a.Count() }).ToList();
        //if (datas.Where(o => o.Count > 1).Any())
    }

    /**
     * 更新车间信息
     */
    void updateShopInfo(List<PmShcCalendarEntity> entities) {
        List<PmShcCalendarEntity> shopEntities = entities.stream().filter(c -> c.getTypeFlag() != 2).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(shopEntities)) {
            List<ShopDTO> shopInfos = getCalendarShopInfos();
            for (ShopDTO shopinfo : shopInfos) {
                List<PmShcCalendarEntity> datas = shopEntities.stream().filter(o -> shopinfo.getPmShopName().equals(o.getWorkshopName())).collect(Collectors.toList());
                for (PmShcCalendarEntity data : datas) {
                    data.setWorkshopCode(shopinfo.getPmShopCode());
                    data.setWorkshopName(shopinfo.getPmShopName());
                }
            }
            for (PmShcCalendarEntity each : shopEntities) {
                if (!StringUtils.hasText(each.getWorkshopCode())) {
                    throw new InkelinkException("车间名称【" + each.getWorkshopName() + "】在系统中找不到！");
                }
            }
        }
    }

    /**
     * 更新线体信息
     */
    void updateLineInfo(List<PmShcCalendarEntity> entities) {
        List<PmShcCalendarEntity> lineEntities = entities.stream().filter(c -> c.getTypeFlag() == 2).collect(Collectors.toList());

        //要填写线体名称和代码，因此不需要设置了
        // List<String> lineCodeList = entities.stream().map(PmShcCalendarAreaEntity::getLineCode).collect(Collectors.toList());
        //  List<LineVO> lineVOS = pmLineService.getByLineCodeList(lineCodeList);
        if (!CollectionUtils.isEmpty(lineEntities)) {
            PmAllDTO allPm = pmVersionService.getObjectedPm();

            for (PmShcCalendarEntity data : lineEntities) {
                PmWorkShopEntity pmWorkShopEntity = queryWorkShopByPmAllDTO(allPm, data.getWorkshopName());
                if (pmWorkShopEntity == null) {
                    throw new InkelinkException("车间名称【" + data.getWorkshopName() + "】在系统中找不到！");
                }
                LineVO lineVO = queryLineNameByPmAllDTO(allPm, pmWorkShopEntity, data.getLineName());
                if (lineVO == null) {
                    throw new InkelinkException("线体名称【" + data.getLineName() + "】在系统中找不到！");
                }
                data.setWorkshopCode(lineVO.getWorkshopCode());
                data.setWorkshopName(lineVO.getWorkshopName());
                data.setLineCode(lineVO.getLineCode());
                data.setLineName(lineVO.getLineName());
            }
        }
    }

    private PmWorkShopEntity queryWorkShopByPmAllDTO(PmAllDTO allPm, String workShopName) {
        List<PmWorkShopEntity> shops = allPm.getShops();
        for (PmWorkShopEntity shop : shops) {
            if (workShopName.equals(shop.getWorkshopName())) {
                return shop;
            }
        }
        return null;
    }

    private LineVO queryLineNameByPmAllDTO(PmAllDTO allPm, PmWorkShopEntity pmWorkShopEntity, String lineName) {
        List<PmLineEntity> lines = allPm.getLines();
        for (PmLineEntity line : lines) {
            if (pmWorkShopEntity.getId().equals(line.getPrcPmWorkshopId()) && lineName.equals(line.getLineName())) {
                LineVO lineVO = new LineVO();
                lineVO.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
                lineVO.setWorkshopName(pmWorkShopEntity.getWorkshopName());
                lineVO.setLineCode(line.getLineCode());
                lineVO.setLineName(line.getLineName());
                return lineVO;
            }
        }

       /* for (PmWorkShopEntity shop : shops) {
            if (workShopName.equals(shop.getWorkShopName())) {
                List<PmLineEntity> pmLineEntity = shop.getPmLineEntity();
                for (PmLineEntity line : pmLineEntity) {
                    if (lineName.equals(line.getLineName())) {
                        LineVO lineVO = new LineVO();
                        lineVO.setWorkShopCode(shop.getWorkShopCode());
                        lineVO.setWorkShopName(shop.getWorkShopName());
                        lineVO.setLineCode(line.getLineCode());
                        lineVO.setLineName(line.getLineName());
                        return lineVO;
                    }
                }
            }
        }*/
        return null;
    }

    /**
     * 更新班次信息
     */
    void updateShiftInfo(List<PmShcCalendarEntity> entities) {
        List<PmShcShiftEntity> pmShcShiftInfos = pmShcShiftService.getData(new ArrayList<>());
        for (PmShcCalendarEntity data : entities) {
            List<PmShcShiftEntity> matchShifts = pmShcShiftInfos.stream().filter(c -> c.getShiftName().equals(data.getShcShiftName())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(matchShifts)) {
                throw new InkelinkException("班次名称【" + data.getShcShiftName() + "】在系统中找不到");
            }
            if (data.getOvertime() == null) {
                data.setOvertime(0);//设置默认值
            }
            if (data.getProductivity() == null) {
                data.setProductivity(0);//设置默认值
            }
            data.setShcShiftCode(matchShifts.get(0).getShiftCode());
            data.setShcShiftName(matchShifts.get(0).getShiftName());
            String shiftStartTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + matchShifts.get(0).getStartTime() + ":00";
            data.setShcShiftStartDt(DateUtils.parse(shiftStartTime, DateUtils.DATE_TIME_PATTERN));
            String shiftEndTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + matchShifts.get(0).getEndTime() + ":00";
            data.setShcShiftEndDt(DateUtils.parse(shiftEndTime, DateUtils.DATE_TIME_PATTERN));
        }

        /*for (PmShcShiftEntity pmShcShiftInfo : pmShcShiftInfos) {
            List<PmShcCalendarEntity> datas = entities.stream().filter(o -> StringUtils.equals(o.getShcShiftName(), pmShcShiftInfo.getShiftName())).collect(Collectors.toList());
            for (PmShcCalendarEntity data : datas) {
                data.setShcShiftCode(pmShcShiftInfo.getShiftCode());
                data.setShcShiftName(pmShcShiftInfo.getShiftName());
                String shiftStartTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + pmShcShiftInfo.getStartTime() + ":00";
                data.setShcShiftStartDt(DateUtils.parse(shiftStartTime, DateUtils.DATE_TIME_PATTERN));
                String shiftEndTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + pmShcShiftInfo.getEndTime() + ":00";
                data.setShcShiftEndDt(DateUtils.parse(shiftEndTime, DateUtils.DATE_TIME_PATTERN));
            }
        }*/
       /* for (PmShcCalendarEntity entitie : entities) {
            if (entitie.getShcShiftCode() == null) {
                throw new InkelinkException("班次名称【" + entitie.getShcShiftName() + "】在系统中找不到");
            }
        }*/
    }

    /**
     * 删除已经存的数据
     */
    void delExistsData(List<PmShcCalendarEntity> entities) {
        //根据车间和工作日，重置数据
        for (Map.Entry<String, List<PmShcCalendarEntity>> data : entities.stream().collect(Collectors
                .groupingBy(c -> c.getWorkshopCode() + "#" + c.getLineCode() + "#" + DateUtils.format(c.getWorkDay(), "yyyy-MM-dd"))).entrySet()) {
            String[] keys = data.getKey().split("#");
            List<ConditionDto> delcons = new ArrayList<>();
            delcons.add(new ConditionDto("WORK_DAY", keys[2], ConditionOper.AllLike));
            if (org.springframework.util.StringUtils.hasText(keys[1])) {
                delcons.add(new ConditionDto("LINE_CODE", keys[1], ConditionOper.Equal));
            }
            delcons.add(new ConditionDto("WORKSHOP_CODE", keys[0], ConditionOper.Equal));

            delete(delcons);
        }


       /* for (Map.Entry<String, List<PmShcCalendarEntity>> data : entities.stream().collect(Collectors
                .groupingBy(c -> c.getWorkShopCode() + "#" + DateUtils.format(c.getWorkDay(), "yyyy-MM-dd"))).entrySet()) {
            String[] keys = data.getKey().split("#");
            List<ConditionDto> delcons = new ArrayList<>();
            delcons.add(new ConditionDto("WORK_DAY", keys[1], ConditionOper.AllLike));
            delcons.add(new ConditionDto("WORKSHOP_CODE", keys[0], ConditionOper.Equal));
            delete(delcons);
        }*/
    }

    /**
     * 从AS同步车间/线体日历
     *
     * @param calendarFromASDTOList
     * @param shopFlag              车间标识 1-车间；2-线体
     */
    @Override
    public void syncCalendarFromAS(List<CalendarFromASDTO> calendarFromASDTOList, int shopFlag) {
        if (CollectionUtils.isEmpty(calendarFromASDTOList)) {
            return;
        }

        List<CalendarFromASDTO> cleanCalendarFromASDTOList = new ArrayList<>();
        Date now = DateUtils.parse(DateUtils.format(new Date()));
        //格式化日期，并将当前及以前的日历清理掉
        for (CalendarFromASDTO each : calendarFromASDTOList) {
            DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
            try {
                each.setDate(dateformat.parse(dateformat.format(each.getDate())));
            } catch (ParseException e) {

            }
            if (each.getDate().compareTo(now) < 0) {
                continue;//当天及以前的都不导入
            }
            if (!StringUtils.hasText(each.getShiftCode())
                    || !StringUtils.hasText(each.getShopCode())
                    || each.getDate() == null) {
                continue;//空数据不能导入
            }
            if (shopFlag == 2) {
                if (!StringUtils.hasText(each.getLineCode())) {
                    continue;//空数据不能导入
                }
            }
            cleanCalendarFromASDTOList.add(each);
        }
        //获得车间/线体名称
        syncFromASUpdateNameInfo(cleanCalendarFromASDTOList);
        //过滤垃圾数据，自然日、车间编码、车间名称必须要有值；
        cleanCalendarFromASDTOList = cleanCalendarFromASDTOList.stream().filter(c -> StringUtils.hasText(c.getShopCode()) && StringUtils.hasText(c.getShopName())
                        && StringUtils.hasText(c.getShiftCode()) && null != c.getDate())
                .collect(Collectors.toList());
        if (shopFlag == 2) {
            cleanCalendarFromASDTOList = cleanCalendarFromASDTOList.stream().filter(c -> StringUtils.hasText(c.getLineName()) && StringUtils.hasText(c.getLineCode())).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(cleanCalendarFromASDTOList)) {
            return;
        }
        //获得所有班次信息，后续通过as_flag找到mom的班次主键id
        QueryWrapper<PmShcShiftEntity> queryShiftWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcShiftEntity> lambdaQueryShiftWrapper = queryShiftWrapper.lambda();
        lambdaQueryShiftWrapper.eq(PmShcShiftEntity::getDataSource, 2);
        List<PmShcShiftEntity> pmShcShiftEntities = pmShcShiftService.getData(queryShiftWrapper, false);

        //获得mom中车间日历数据（当天以后的数据）
        QueryWrapper<PmShcCalendarEntity> queryCalendarWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarEntity> lambdaQueryCalendarWrapper = queryCalendarWrapper.lambda();
        // lambdaQueryCalendarWrapper.eq(PmShcCalendarEntity::getDataSource, 2);
        lambdaQueryCalendarWrapper.gt(PmShcCalendarEntity::getWorkDay, new Date());
        List<PmShcCalendarEntity> dbPmShcCalendarEntities = this.getData(queryCalendarWrapper, false);
        //构造日历数据
        List<PmShcCalendarEntity> insertPmShcCalendarEntities = new ArrayList<>();
        List<PmShcCalendarEntity> delAsPmShcCalendarEntities = new ArrayList<>();//原来数据库中as数据要删除
        for (CalendarFromASDTO each : cleanCalendarFromASDTOList) {
            List<PmShcShiftEntity> filterShifts = pmShcShiftEntities.stream().filter(c -> c.getAsFlag().equals(each.getShiftCode())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(filterShifts)) {
                continue;
            }
            PmShcCalendarEntity pmShcCalendarEntity = syncFromASPmShcCalendarView(each, filterShifts, shopFlag);

            List<PmShcCalendarEntity> currentCalendarList;
            if (shopFlag == 1) {
                currentCalendarList = dbPmShcCalendarEntities.stream().filter(c -> c.getWorkshopCode().equals(each.getShopCode())
                        && !StringUtils.hasText(c.getLineCode())
                        && c.getWorkDay().compareTo(each.getDate()) == 0).collect(Collectors.toList());
            } else {
                currentCalendarList = dbPmShcCalendarEntities.stream().filter(c -> c.getWorkshopCode().equals(each.getShopCode())
                        && c.getLineCode().equals(each.getLineCode())
                        && c.getWorkDay().compareTo(each.getDate()) == 0).collect(Collectors.toList());
            }
            if (CollectionUtils.isEmpty(currentCalendarList)) {
                //如果原来没有数据，将本条数据插入到数据库
                insertPmShcCalendarEntities.add(pmShcCalendarEntity);
            } else {
                if (currentCalendarList.stream().noneMatch(c -> c.getDataSource() == 1)) {
                    //没有mom新增的日历数据，那说明有数据并且全是as新增的数据，那就要删掉as的数据，并新增新的数据
                    insertPmShcCalendarEntities.add(pmShcCalendarEntity);
                    delAsPmShcCalendarEntities.addAll(currentCalendarList);//这样新增该集合可能有重复数据，不过后面是用in删除，所以不影响
                }
            }
        }
        if (!CollectionUtils.isEmpty(insertPmShcCalendarEntities)) {
            // 同一个车间/线体，同一天同一个班次不能有多条
            List<PmShcCalendarEntity> list = new ArrayList<>();
            for (PmShcCalendarEntity each : insertPmShcCalendarEntities) {
                if (list.stream().noneMatch(c -> c.getWorkshopCode().equals(each.getWorkshopCode())
                        && c.getLineCode().equals(each.getLineCode())
                        && c.getWorkDay().compareTo(each.getWorkDay()) == 0
                        && c.getShcShiftCode().equals(each.getShcShiftCode()))) {
                    list.add(each);
                }
            }
            this.insertBatch(list);
        }
        if (!CollectionUtils.isEmpty(delAsPmShcCalendarEntities)) {
            List<Long> ids = delAsPmShcCalendarEntities.stream().map(PmShcCalendarEntity::getId).collect(Collectors.toList());
            LambdaUpdateWrapper<PmShcCalendarEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PmShcCalendarEntity::getIsDelete, 1);
            updateWrapper.in(PmShcCalendarEntity::getId, ids);
            this.update(updateWrapper);
        }

    }

    private PmShcCalendarEntity syncFromASPmShcCalendarView(CalendarFromASDTO each, List<PmShcShiftEntity> filterShifts, int shopFlag) {
        PmShcCalendarEntity pmShcCalendarEntity = new PmShcCalendarEntity();
        pmShcCalendarEntity.setWorkshopCode(each.getShopCode());
        pmShcCalendarEntity.setWorkshopName(each.getShopName());
        if (shopFlag == 2) {
            pmShcCalendarEntity.setLineCode(each.getLineCode());
            pmShcCalendarEntity.setLineName(each.getLineName());
        }
        pmShcCalendarEntity.setShcShiftCode(filterShifts.get(0).getShiftCode());
        pmShcCalendarEntity.setShcShiftName(filterShifts.get(0).getShiftName());
        pmShcCalendarEntity.setProductivity(filterShifts.get(0).getProductivity());
        String strDt = DateUtils.format(each.getDate(), "yyyy-MM-dd") + " " + filterShifts.get(0).getStartTime() + ":00";
        if (filterShifts.get(0).getIsDayBefore()) {
            strDt = DateUtils.format(DateUtils.addDateDays(each.getDate(), 1), "yyyy-MM-dd") + " " + filterShifts.get(0).getStartTime() + ":00";
        }
        pmShcCalendarEntity.setShcShiftStartDt(DateUtils.stringToDate(strDt, "yyyy-MM-dd HH:mm:ss"));
        strDt = DateUtils.format(each.getDate(), "yyyy-MM-dd") + " " + filterShifts.get(0).getEndTime() + ":00";
        if (filterShifts.get(0).getIsDayAfter()) {
            strDt = DateUtils.format(DateUtils.addDateDays(each.getDate(), 1), "yyyy-MM-dd") + " " + filterShifts.get(0).getEndTime() + ":00";
        }
        pmShcCalendarEntity.setShcShiftEndDt(DateUtils.stringToDate(strDt, "yyyy-MM-dd HH:mm:ss"));
        pmShcCalendarEntity.setDataSource(2);
        pmShcCalendarEntity.setWorkDay(each.getDate());

        return pmShcCalendarEntity;
    }

    /**
     * 从AS同步线体日历更新车间/线体名称信息
     *
     * @param list
     */
    void syncFromASUpdateNameInfo(List<CalendarFromASDTO> list) {
        if (!CollectionUtils.isEmpty(list)) {
            List<ShopDTO> shopInfos = getCalendarShopInfos();
            if (CollectionUtils.isEmpty(shopInfos)) {
                return;
            }

            for (CalendarFromASDTO each : list) {
                List<ShopDTO> matchList = shopInfos.stream().filter(c -> c.getPmShopCode().equals(each.getShopCode())).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(matchList)) {
                    continue;
                }
                each.setShopName(matchList.get(0).getPmShopName());
                if (StringUtils.hasText(each.getLineCode())) {
                    if (CollectionUtils.isEmpty(matchList.get(0).getChildren())) {
                        continue;
                    }
                    List<LineDTO> lineDTOS = matchList.get(0).getChildren().stream().filter(c -> c.getPmLineCode().equals(each.getLineCode())).collect(Collectors.toList());
                    if (CollectionUtils.isEmpty(lineDTOS)) {
                        continue;
                    }
                    each.setLineName(lineDTOS.get(0).getPmLineName());
                }
            }
        }
    }


}