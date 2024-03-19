package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.communication.entity.MidAsShiftEntity;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.mapper.IPmShcCalendarMapper;
import com.ca.mfd.prc.pm.mapper.IPmShcShiftMapper;
import com.ca.mfd.prc.pm.service.IPmShcBreakService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;
import java.util.stream.Collectors;

/**
 * 工厂排班
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service
public class PmShcShiftServiceImpl extends AbstractCrudServiceImpl<IPmShcShiftMapper, PmShcShiftEntity> implements IPmShcShiftService {

    /*@Autowired
    IPmShcCalendarService pmShcCalendarService;*/
    @Resource
    private IPmShcCalendarMapper pmShcCalendarMapper;
    @Autowired
    IPmShcBreakService pmShcBreakService;

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        List<Long> ids = idList.stream().map(c -> Long.parseLong(String.valueOf(c))).collect(Collectors.toList());
        int count = pmShcCalendarMapper.getCalendarCountByShiftIds(ids);// pmShcCalendarService.getData(queryPmShcCalendarWrapper, false);
        if (count > 0) {
            throw new InkelinkException("存在班次对应的日历，该班次不能删除!");
        }
    }

    @Override
    public List<PmShcShiftEntity> getListByCodes(List<Integer> codes) {
        QueryWrapper<PmShcShiftEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().in(PmShcShiftEntity::getShiftCode, codes);
        return selectList(queryWrapper);
    }
    @Override
    public void beforeInsert(PmShcShiftEntity entity) {
        verfyOperation(entity, false);
    }

    @Override
    public void beforeUpdate(PmShcShiftEntity entity) {
        verfyOperation(entity, true);
    }

    void verfyOperation(PmShcShiftEntity model, boolean isUpdate) {

        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("SHIFT_CODE", model.getShiftCode().toString(), ConditionOper.Equal));
        if (isUpdate) {
            conditionInfos.add(new ConditionDto("ID", model.getId().toString(), ConditionOper.Unequal));
        }
        PmShcShiftEntity data = getData(conditionInfos).stream().findFirst().orElse(null);
        if (data != null) {
            throw new InkelinkException("录入的班次代码" + model.getShiftCode() + "已经存在，不允许重复录入！");
        }
    }

    /**
     * 获得最近更新的一条记录
     *
     * @return
     */
    @Override
    public PmShcShiftEntity getLastUpdatePmShcShift() {
        QueryWrapper<PmShcShiftEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcShiftEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.orderByDesc(PmShcShiftEntity::getLastUpdateDate);
        lambdaQueryWrapper.last("limit 0,1");

        List<PmShcShiftEntity> list = this.selectList(queryWrapper);
        PmShcShiftEntity entity = null;
        if (!CollectionUtils.isEmpty(list)) {
            entity = list.get(0);
        }
        return entity;
    }

    /**
     * 从as中间表导入班次
     */
    @Override
    public void syncShiftFromAS(List<MidAsShiftEntity> shiftFromAS) {

        if (CollectionUtils.isEmpty(shiftFromAS)) {
            return;
        }
        List<MidAsShiftEntity> shiftFromASDTOS = new ArrayList<>();
        for (MidAsShiftEntity each : shiftFromAS) {
            if (each.getIsCross()) {
                //如果跨天的话，将结束时间增加一天，防止传过来的时间没增加，如果传过来的数据已经增加了其实也不影响
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(each.getEndTime());
                calendar.add(Calendar.DATE, 1);
                each.setEndTime(calendar.getTime());
            }
            //数据校验
            boolean isTimeNonNull = null != each.getStartTime()
                    && null != each.getEndTime();
            if (!isTimeNonNull) {
                each.setExeMsg("开始时间和结束时间不能为空");
                each.setExeStatus(3);
                continue;
            }
            boolean isShiftInfoNonNull = StringUtils.hasText(each.getShiftName())
                    && StringUtils.hasText(each.getShiftCode());
            if (!isShiftInfoNonNull) {
                each.setExeMsg("班次代码和名称不能为空");
                each.setExeStatus(3);
                continue;
            }

            boolean isLegalTimeRange = each.getStartTime().compareTo(each.getEndTime()) < 0;
            if (!isLegalTimeRange) {
                each.setExeMsg("开始时间不能小于结束时间");
                each.setExeStatus(3);
                continue;
            }
            //方便排重
            MidAsShiftEntity nt = new MidAsShiftEntity();
            nt.setShiftCode(each.getShiftCode());
            nt.setShiftName(each.getShiftName());
            nt.setIsCross(each.getIsCross());
            nt.setStartTime(each.getStartTime());
            nt.setEndTime(each.getEndTime());
            nt.setCreationDate(null);
            nt.setLastUpdateDate(null);
            nt.setExeTime(null);
            shiftFromASDTOS.add(nt);
        }

        if (CollectionUtils.isEmpty(shiftFromASDTOS)) {
            return;//如果as没有数据，什么都不处理
        }
        shiftFromASDTOS = shiftFromASDTOS.stream().distinct().collect(Collectors.toList());
        shiftFromASDTOS.sort(Comparator.comparing(MidAsShiftEntity::getShiftCode)
                .thenComparing(MidAsShiftEntity::getIsCross)
                .thenComparing(MidAsShiftEntity::getStartTime));
        List<PmShcShiftEntity> asPmShcShiftEntities = new ArrayList<>();

        Map<String, List<MidAsShiftEntity>> mapShift = shiftFromASDTOS.stream().collect(Collectors.groupingBy(MidAsShiftEntity::getShiftCode));
        Iterator<Map.Entry<String, List<MidAsShiftEntity>>> iteratorShift = mapShift.entrySet().iterator();
        while (iteratorShift.hasNext()) {
            Map.Entry<String, List<MidAsShiftEntity>> entry = iteratorShift.next();
            //   String key = entry.getKey();//shiftCode
            List<MidAsShiftEntity> value = entry.getValue();
            if (CollectionUtils.isEmpty(value)) {
                continue;
            }

            //开始时间最小的作为开始时间，截至时间最大的作为截至时间
            value = value.stream().sorted(Comparator.comparing(MidAsShiftEntity::getEndTime).reversed()).collect(Collectors.toList());
            Date maxEndTime = value.get(0).getEndTime();
            value = value.stream().sorted(Comparator.comparing(MidAsShiftEntity::getStartTime)).collect(Collectors.toList());
            Date minStartTime = value.get(0).getStartTime();
            //如果value中有一个跨天，那么就跨天
            boolean isDayAfter = false;
            if (value.stream().anyMatch(MidAsShiftEntity::getIsCross)) {
                isDayAfter = true;
            }
            DateFormat dateformat = new SimpleDateFormat("HH:mm");

            PmShcShiftEntity pmShcShiftEntity = new PmShcShiftEntity();
            pmShcShiftEntity.setShiftName("暂无");//value.get(0).getShiftName());
            pmShcShiftEntity.setShiftCode(0);
            pmShcShiftEntity.setStartTime(dateformat.format(minStartTime));
            pmShcShiftEntity.setEndTime(dateformat.format(maxEndTime));
            pmShcShiftEntity.setIsDayBefore(false);
            pmShcShiftEntity.setIsDayAfter(isDayAfter);
            pmShcShiftEntity.setDataSource(2);//as来源
            pmShcShiftEntity.setAsFlag(value.get(0).getShiftCode());

            List<PmShcBreakEntity> pmShcBreakEntities = constructShcBreak(value);
            //记录休息时间关键值
            pmShcShiftEntity.setAttribute3(getBreakTimeKey(pmShcBreakEntities));
            pmShcShiftEntity.setPmShcBreakInfos(pmShcBreakEntities);

            asPmShcShiftEntities.add(pmShcShiftEntity);
        }

        List<PmShcShiftEntity> dbPmShcShiftEntities = this.getData(new ArrayList<>(), false);
        List<PmShcBreakEntity> dbPmShcBreakEntities = pmShcBreakService.getData(new ArrayList<>(), false);
        if (!CollectionUtils.isEmpty(asPmShcShiftEntities)) {
            Integer max = dbPmShcShiftEntities.stream().mapToInt(PmShcShiftEntity::getShiftCode).max().orElse(1);
            List<PmShcShiftEntity> addShifts = new ArrayList<>();
            List<PmShcShiftEntity> upShifts = new ArrayList<>();
            for (PmShcShiftEntity shc : asPmShcShiftEntities) {
                PmShcShiftEntity oldShc = dbPmShcShiftEntities.stream()
                        .filter(c -> c.getDataSource() == 2
                                && org.apache.commons.lang3.StringUtils.equalsIgnoreCase(c.getAsFlag(), shc.getAsFlag()))
                        .findFirst().orElse(null);
                if (oldShc == null) {
                    max = max + 1;
                    shc.setShiftCode(max);
                    addShifts.add(shc);
                } else {
                    //不同步班次名稱
                    //oldShc.setShiftName(shc.getShiftName());
                    //oldShc.setShiftCode(shc.getShiftCode());
                    oldShc.setStartTime(shc.getStartTime());
                    oldShc.setEndTime(shc.getEndTime());
                    oldShc.setIsDayBefore(false);
                    oldShc.setIsDayAfter(shc.getIsDayAfter());
                    oldShc.setDataSource(2);//as来源
                    oldShc.setAsFlag(shc.getAsFlag());
                    oldShc.setAttribute3(shc.getAttribute3());
                    oldShc.setPmShcBreakInfos(shc.getPmShcBreakInfos());
                    upShifts.add(oldShc);
                }
            }
            List<PmShcBreakEntity> pmShcBreakEntities = new ArrayList<>();
            //新增
            if (!CollectionUtils.isEmpty(addShifts)) {
                //插入班次
                this.insertBatch(addShifts);
                //插入休息数据
                for (PmShcShiftEntity each : addShifts) {
                    if (!CollectionUtils.isEmpty(each.getPmShcBreakInfos())) {
                        for (PmShcBreakEntity eachBreak : each.getPmShcBreakInfos()) {
                            eachBreak.setPrcPmShcShiftId(each.getId());
                            pmShcBreakEntities.add(eachBreak);
                        }
                    }
                }

            }
            //修改
            if (!CollectionUtils.isEmpty(upShifts)) {
                //修改班次
                this.updateBatchById(upShifts);
                for (PmShcShiftEntity each : upShifts) {

                    List<PmShcBreakEntity> oldShcBreakEntities = dbPmShcBreakEntities.stream().filter(c -> Objects.equals(c.getPrcPmShcShiftId(), each.getId()))
                            .collect(Collectors.toList());
                    String oldBreakTimeKey = getBreakTimeKey(oldShcBreakEntities);
                    String newBreakTimeKey = getBreakTimeKey(each.getPmShcBreakInfos());
                    if (CollectionUtils.isEmpty(each.getPmShcBreakInfos())) {
                        //没有休息时间
                        //删除原来as排班对应的休息记录
                        if (!CollectionUtils.isEmpty(oldShcBreakEntities)) {
                            List<Long> breakIds = oldShcBreakEntities.stream().map(PmShcBreakEntity::getId).collect(Collectors.toList());
                            pmShcBreakService.delete(breakIds.toArray(new Long[0]));
                        }
                    } else if (!org.apache.commons.lang3.StringUtils.equalsIgnoreCase(oldBreakTimeKey, newBreakTimeKey)) {
                        //休息时间有变化
                        //删除原来as排班对应的休息记录
                        if (!CollectionUtils.isEmpty(oldShcBreakEntities)) {
                            List<Long> breakIds = oldShcBreakEntities.stream().map(PmShcBreakEntity::getId).collect(Collectors.toList());
                            pmShcBreakService.delete(breakIds.toArray(new Long[0]));
                        }
                        //休息时间有变化才更新
                        for (PmShcBreakEntity eachBreak : each.getPmShcBreakInfos()) {
                            eachBreak.setPrcPmShcShiftId(each.getId());
                            pmShcBreakEntities.add(eachBreak);
                        }
                    }
                }
            }
            //插入休息数据
            if (!CollectionUtils.isEmpty(pmShcBreakEntities)) {
                pmShcBreakService.insertBatch(pmShcBreakEntities);
            }

        }

        for (MidAsShiftEntity each : shiftFromASDTOS) {
            each.setExeMsg("ok");
            each.setExeStatus(1);
        }
    }

    private String getBreakTimeKey(List<PmShcBreakEntity> breaks) {
        if (CollectionUtils.isEmpty(breaks)) {
            return "";
        }
        breaks.sort(Comparator.comparing(PmShcBreakEntity::getStartTime));
        StringBuilder sbreaks = new StringBuilder();
        for (PmShcBreakEntity bk : breaks) {
            sbreaks.append(bk.getStartTime() + "-" + bk.getEndTime());
        }
        return sbreaks.toString();
    }

    /**
     * 清理as的排班数据，两个排班不能有重叠时间
     *
     * @param shiftFromASDTOS
     * @return
     */
/*    private List<ShiftFromASDTO> cleanAsShift(List<ShiftFromASDTO> shiftFromASDTOS) {
        List<ShiftFromASDTO> result = new ArrayList<>();
        result.add(shiftFromASDTOS.get(0));
        //开始时间已经升序排列，因此不会存在前面开始时间大于后面开始时间的情况
        for (int i = 1; i < shiftFromASDTOS.size(); i++) {
            if (result.get(result.size() - 1).getStartTime().compareTo(shiftFromASDTOS.get(i).getStartTime()) == 0) {
                //不管名称和code是否相同，都认为两条是一个班，这种按理说是垃圾数据
                if (result.get(result.size() - 1).getEndTime().compareTo(shiftFromASDTOS.get(i).getEndTime()) < 0) {
                    result.get(result.size() - 1).setEndTime(shiftFromASDTOS.get(i).getEndTime());
                }
            } else {
                //上一条的开始时间小于当前的开始时间
                if (result.get(result.size() - 1).getEndTime().compareTo(shiftFromASDTOS.get(i).getStartTime()) > 0) {
                    //上一条的截至时间大于当前的开始时间，说明两条记录有时间重叠，这种情况应该是垃圾数据才对，
                    // 兼容处理，将当前记录的开始时间设置成上调记录的截至时间，这样两条记录就不会有重叠时间了
                    shiftFromASDTOS.get(i).setStartTime(result.get(result.size() - 1).getEndTime());
                }
                result.add(shiftFromASDTOS.get(i));
            }
        }
        return result;
    }*/

    /**
     * 构造休息时间
     *
     * @param shiftFromASDTOS
     * @return
     */
    private List<PmShcBreakEntity> constructShcBreak(List<MidAsShiftEntity> shiftFromASDTOS) {
        List<PmShcBreakEntity> breakEntities = new ArrayList<>();
        shiftFromASDTOS = shiftFromASDTOS.stream().sorted(Comparator.comparing(MidAsShiftEntity::getIsCross)
                .thenComparing(MidAsShiftEntity::getStartTime)).collect(Collectors.toList());
        if (shiftFromASDTOS.size() <= 1) {
            return breakEntities;//只有一条记录的话，那就不存在休息时间
        }
        DateFormat dateformat = new SimpleDateFormat("HH:mm");

        for (int i = 1; i < shiftFromASDTOS.size(); i++) {
            //上一条的截至时间和当前记录的开始时间一样，说明中间没有休息时间
            if (shiftFromASDTOS.get(i - 1).getEndTime().compareTo(shiftFromASDTOS.get(i).getStartTime()) == 0) {
                continue;
            }

            PmShcBreakEntity pmShcBreakEntity = new PmShcBreakEntity();
            pmShcBreakEntity.setStartTime(dateformat.format(shiftFromASDTOS.get(i - 1).getEndTime()));
            pmShcBreakEntity.setEndTime(dateformat.format(shiftFromASDTOS.get(i).getStartTime()));
            if (pmShcBreakEntity.getStartTime().equals(pmShcBreakEntity.getEndTime())) {
                continue;
            }
            breakEntities.add(pmShcBreakEntity);
        }

        return breakEntities;
    }


    /*private void aa(List<PmShcShiftEntity> dbPmShcShiftEntities, List<PmShcBreakEntity> dbPmShcBreakEntities, List<PmShcShiftEntity> asPmShcShiftEntities) throws ParseException {
        dbPmShcShiftEntities = dbPmShcShiftEntities.stream().sorted(Comparator.comparing(PmShcShiftEntity::getStartTime)).collect(Collectors.toList());
        asPmShcShiftEntities = asPmShcShiftEntities.stream().sorted(Comparator.comparing(PmShcShiftEntity::getStartTime)).collect(Collectors.toList());

        List<PmShcShiftEntity> result = new ArrayList<>();
        for (PmShcShiftEntity asEach : asPmShcShiftEntities) {
            //如果当前记录的时间范围，在db的排班表中已经包含了，就不用处理了
            //判断开始时间是否是在某一个db排班记录开始-截至时间范围内
            if (asEach.getWorkDayStartTime().compareTo(dbPmShcShiftEntities.get(0).getWorkDayStartTime()) < 0) {
                //如果as当前记录的开始时间，小于db中第一条记录的开始时间
                if (asEach.getWorkDayEndTime().compareTo(dbPmShcShiftEntities.get(0).getWorkDayStartTime()) <= 0) {
                    result.add(asEach);
                } else if (asEach.getWorkDayEndTime().compareTo(dbPmShcShiftEntities.get(dbPmShcShiftEntities.size() - 1).getWorkDayEndTime()) <= 0) {
                    //当前记录开始时间小于数据库第一条记录的开始时间，截至时间小于等于数据库最后一条记录的截至时间
                    for (PmShcShiftEntity dbEach : dbPmShcShiftEntities) {
                        if (asEach.getEndTime().compareTo(dbEach.getEndTime()) < 0) continue;

                    }
                } else if (asEach.getWorkDayEndTime().compareTo(dbPmShcShiftEntities.get(dbPmShcShiftEntities.size() - 1).getWorkDayEndTime()) > 0) {
                    //当前记录开始时间小于数据库第一条记录的开始时间，截至时间大于数据库最后一条记录的截至时间
                }
            }
            for (PmShcShiftEntity dbEach : dbPmShcShiftEntities) {
                if (asEach.getEndTime().compareTo(dbEach.getEndTime()) < 0) continue;

            }

            //如果当前记录跟db中的排班表记录是交集关系，那么需要将原来db表中没包含的时间范围新增一条排班记录

        }
    }*/


}