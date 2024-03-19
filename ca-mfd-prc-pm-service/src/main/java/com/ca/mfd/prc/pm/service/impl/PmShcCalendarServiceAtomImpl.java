package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmCalendarSourceDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmShcBreakEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAtomEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmShcCalendarAtomMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysConfigurationEntity;
import com.ca.mfd.prc.pm.service.IPmShcBreakService;
import com.ca.mfd.prc.pm.service.IPmShcCalendarAtomService;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 工厂日历清洗
 *
 * @author jay.he
 */
@Service
public class PmShcCalendarServiceAtomImpl extends AbstractCrudServiceImpl<IPmShcCalendarAtomMapper, PmShcCalendarAtomEntity> implements IPmShcCalendarAtomService {
    private static final Logger log = LoggerFactory.getLogger(PmShcCalendarServiceAtomImpl.class);
    /**
     * 工厂日历清洗上次执行时间
     */
    private static final String calendarAtomHandlePreTime = "calendarAtomHandlePreTime";
    /**
     * 工厂日历清洗类别
     */
    private static final String calendarAtomHandleCategory = "calendarAtomHandle";
    @Autowired
    IPmShcCalendarAtomMapper pmShcCalendarAtomMapper;
    @Autowired
    IPmShcCalendarService pmShcCalendarService;
    @Autowired
    IPmShcBreakService pmShcBreakService;
    @Autowired
    IPmShcShiftService pmShcShiftService;
    @Autowired
    IPmVersionService pmVersionService;
    @Autowired
    private SysConfigurationProvider sysConfigurationService;

    @Override
    public void calendarAtomHandle() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //获取当前时间和配置的上次时间
        Date preTime = getPreTime();
        if (preTime == null) {
            //设置一个最小时间
            preTime = plusData(new Date(), Calendar.YEAR, -100);
            SysConfigurationEntity sysConfigurationEntity = new SysConfigurationEntity();
            sysConfigurationEntity.setCategory(calendarAtomHandleCategory);
            sysConfigurationEntity.setValue(calendarAtomHandlePreTime);
            sysConfigurationEntity.setText(sdf.format(preTime));
            sysConfigurationEntity.setIsHide(true);
            sysConfigurationService.edit(sysConfigurationEntity);
        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // 将时设置为0
        calendar.set(Calendar.MINUTE, 0);      // 将分设置为0
        calendar.set(Calendar.SECOND, 0);      // 将秒设置为0
        calendar.set(Calendar.MILLISECOND, 0); // 将毫秒设置为0
        Date currentDay = calendar.getTime();

        //获取calendar，shift，break的最新更新时间，用来确认是否重新生成班次
        PmShcCalendarEntity lastPmShcCalendar = pmShcCalendarService.getLastUpdatePmShcCalendar();
        PmShcBreakEntity lastUpdatePmShcBreakEntity = pmShcBreakService.getLastUpdatePmShcBreak();
        PmShcShiftEntity lastPmShcShift = pmShcShiftService.getLastUpdatePmShcShift();
        if ((lastPmShcCalendar != null && lastPmShcCalendar.getLastUpdateDate().compareTo(preTime) >= 0) ||
                (lastUpdatePmShcBreakEntity != null && lastUpdatePmShcBreakEntity.getLastUpdateDate().compareTo(preTime) >= 0) ||
                (lastPmShcShift != null && lastPmShcShift.getLastUpdateDate().compareTo(preTime) >= 0)) {
            //1、获取基础时间日历
            List<PmCalendarSourceDTO> calendarSourceData = pmShcCalendarAtomMapper.getCalendarSource(sdf.format(currentDay));
            //  List<PmCalendarSource> calendarSourceData = _pmShcCalendarAtomBll.GetCalendarSource(currentDay);
            //2、获取拆分完的数据
            List<PmShcCalendarAtomEntity> atoms = getAtomData(calendarSourceData);
            //3、清除数据
            deletePmShcCalendarAtom(currentDay);
            //   _pmShcCalendarAtomBll.DeleteEntity(item = > item.WorkDay >= currentDay, false);
            //4、重新落地数据
            //  _pmShcCalendarAtomBll.Insert(atoms);
            this.insertBatch(atoms);
            this.saveChange();
        }
        //更新时间
        sysConfigurationService.updateBycategory(calendarAtomHandlePreTime, calendarAtomHandleCategory, sdf.format(currentDay));
    }

    /**
     * 获取清理工厂日历的最新时间
     */
    private Date getPreTime() {
        String strPreTime = sysConfigurationService.getConfiguration(calendarAtomHandlePreTime, calendarAtomHandleCategory);
        Date date = null;
        if (StringUtils.hasText(strPreTime)) {
            DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = fmt.parse(strPreTime);
            } catch (Exception ex) {

            }
        }
        /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startTime = sdf.format(date);*/

        return date;
    }

    /**
     * 时间操作
     *
     * @param date   原来时间对象
     * @param field  操作类型 年、月、日、时、分、秒等Calendar.DATE
     * @param amount 要增加的时间数值
     * @return
     */
    private Date plusData(Date date, int field, int amount) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(field, amount);
        return c.getTime();
    }

    private List<PmShcCalendarAtomEntity> getAtomData(List<PmCalendarSourceDTO> pmCalendarSourceList) throws ParseException {
        List<PmShcCalendarAtomEntity> result = new ArrayList<>();
        //车间日历数据
        List<PmCalendarSourceDTO> shopCalendarSourceList = pmCalendarSourceList.stream().filter(c -> !StringUtils.hasText(c.getLineCode())).collect(Collectors.toList());
        //线体日历数据
        List<PmCalendarSourceDTO> lineCalendarSourceList = pmCalendarSourceList.stream().filter(c -> StringUtils.hasText(c.getLineCode())).collect(Collectors.toList());
        PmAllDTO pmAllDTO = pmVersionService.getObjectedPm();//所有车间、线体等数据集合
        List<PmLineEntity> allLineEntities = pmAllDTO.getLines();//所有线体集合
        List<PmWorkShopEntity> allPmWorkShopEntities = pmAllDTO.getShops();//所有车间集合

        //没有日历数据的线体继承所属车间的日历数据
        lineInheritShopCalendar(shopCalendarSourceList, lineCalendarSourceList, allPmWorkShopEntities, allLineEntities);
        DateFormat fmtDay = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        //计算前整理数据
        List<PmCalendarSourceDTO> inputData = new ArrayList<>();
        for (PmCalendarSourceDTO each : lineCalendarSourceList) {
            PmCalendarSourceDTO eachInputData = new PmCalendarSourceDTO();
            eachInputData.setWorkshopCode(each.getWorkshopCode());
            eachInputData.setLineCode(each.getLineCode());
            eachInputData.setWorkDay(each.getWorkDay());
            eachInputData.setShiftName(each.getShiftName());
            eachInputData.setOverTime(each.getOverTime());
            eachInputData.setDayAfter(each.isDayAfter());
            eachInputData.setPrcShcCalendarId(each.getPrcShcCalendarId());

            eachInputData.setStartTimeShift(fmtDay.format(each.getWorkDay()) + " " + each.getStartTimeShift().substring(0, 5) + ":00");
            String endTimeShift = "00:00:00";
            if (StringUtils.hasText(each.getEndTimeShift())) {
                endTimeShift = each.getEndTimeShift().substring(0, 5) + ":00";
            }
            if (each.isDayAfter()) {
                String time = fmtDay.format(plusData(each.getWorkDay(), Calendar.DATE, 1)) + " " + endTimeShift;
                eachInputData.setEndTimeShift(fmtDateTime.format(plusData(fmtDateTime.parse(time), Calendar.MINUTE, each.getOverTime())));
            } else {
                String time = fmtDay.format(each.getWorkDay()) + " " + endTimeShift;
                eachInputData.setEndTimeShift(fmtDateTime.format(plusData(fmtDateTime.parse(time), Calendar.MINUTE, each.getOverTime())));
            }
            // eachInputData.setStartTimeBreak(fmtDateTime.format(new Date()));
            //  eachInputData.setEndTimeBreak(fmtDateTime.format(new Date()));
            if (StringUtils.hasText(each.getStartTimeBreak())) {
                Date startTimeShift = fmtDateTime.parse(fmtDay.format(new Date()) + " " + each.getStartTimeShift().substring(0, 5) + ":00");
                Date startTimeBreak = fmtDateTime.parse(fmtDay.format(new Date()) + " " + each.getStartTimeBreak().substring(0, 5) + ":00");
                if (startTimeShift.compareTo(startTimeBreak) <= 0) {
                    eachInputData.setStartTimeBreak(fmtDay.format(each.getWorkDay()) + " " + each.getStartTimeBreak().substring(0, 5) + ":00");
                } else {
                    eachInputData.setStartTimeBreak(fmtDay.format(plusData(each.getWorkDay(), Calendar.DATE, 1)) + " " + each.getStartTimeBreak().substring(0, 5) + ":00");
                }
            }
            if (StringUtils.hasText(each.getEndTimeBreak())) {
                Date startTimeShift = fmtDateTime.parse(fmtDay.format(new Date()) + " " + each.getStartTimeShift().substring(0, 5) + ":00");
                Date endTimeBreak = fmtDateTime.parse(fmtDay.format(new Date()) + " " + each.getEndTimeBreak().substring(0, 5) + ":00");
                if (startTimeShift.compareTo(endTimeBreak) <= 0) {
                    eachInputData.setEndTimeBreak(fmtDay.format(each.getWorkDay()) + " " + each.getEndTimeBreak().substring(0, 5) + ":00");
                } else {
                    eachInputData.setEndTimeBreak(fmtDay.format(plusData(each.getWorkDay(), Calendar.DATE, 1)) + " " + each.getEndTimeBreak().substring(0, 5) + ":00");
                }
            }
            inputData.add(eachInputData);
        }

        // 计算排序值等相关处理：班次内排序RowInx, 班次内计数CountRow，循环顺序RowNum
        calOrderValue(inputData);

        //构造atom集合
        result = structureAtomList(inputData);

        return result;
    }

    //计算排序值等相关处理：班次内排序RowInx, 班次内计数CountRow，循环顺序RowNum
    private void calOrderValue(List<PmCalendarSourceDTO> inputData) {
        inputData.sort(Comparator.comparing(PmCalendarSourceDTO::getWorkDay)
                .thenComparing(PmCalendarSourceDTO::getStartTimeShift)
                .thenComparing(PmCalendarSourceDTO::getStartTimeBreak));

        Map<String, List<PmCalendarSourceDTO>> inputDataGroup = inputData.stream().collect(Collectors.groupingBy(c -> c.getWorkshopCode() + "_" + c.getLineCode() + "_" + c.getWorkDay().getTime() + "_" + c.getShiftName()));
        Iterator<Map.Entry<String, List<PmCalendarSourceDTO>>> iteratorShopCalendar = inputDataGroup.entrySet().iterator();
        while (iteratorShopCalendar.hasNext()) {
            Map.Entry<String, List<PmCalendarSourceDTO>> entry = iteratorShopCalendar.next();
            /*String key = entry.getKey();
            String workshopCode = key.split("_")[0];
            String lineCode = key.split("_")[1];
            String strWorkDay = key.split("_")[2];
            String shiftName = key.split("_")[3];*/
            List<PmCalendarSourceDTO> currentGroupList = entry.getValue();
            int count = currentGroupList.size();
            int i = 1;
            for (PmCalendarSourceDTO each : currentGroupList) {
                each.setRowInx(i);
                each.setCountRow(count);
                i++;
            }
        }
       /* int i = 1;
        for (PmCalendarSourceDTO each : inputData) {
            each.setRowNum(i);
            i++;
        }*/
    }

    private List<PmShcCalendarAtomEntity> structureAtomList(List<PmCalendarSourceDTO> inputData) throws ParseException {
        inputData.sort(Comparator.comparing(PmCalendarSourceDTO::getWorkshopCode)
                .thenComparing(PmCalendarSourceDTO::getLineCode)
                .thenComparing(PmCalendarSourceDTO::getWorkDay)
                .thenComparing(PmCalendarSourceDTO::getStartTimeShift)
                .thenComparing(PmCalendarSourceDTO::getStartTimeBreak));

        DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<PmShcCalendarAtomEntity> pmShcCalendarAtomEntities = new ArrayList<>();
        Date startTime = new Date();
        Date endTime = new Date();
        for (PmCalendarSourceDTO each : inputData) {
            // PmShcCalendarAtomEntity data = new PmShcCalendarAtomEntity();
            if (each.getCountRow() == 1) {
                // 一个班次的休息时间个数为0或者1
                if (!StringUtils.hasText(each.getStartTimeBreak())) {
                    //开始休息时间没有值，说明一个休息时间都没有
                    startTime = fmtDateTime.parse(each.getStartTimeShift());
                    endTime = fmtDateTime.parse(each.getEndTimeShift());

                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));
                } else {
                    //有1个休息时间
                    startTime = fmtDateTime.parse(each.getStartTimeShift());
                    endTime = new Date();
                    if (StringUtils.hasText(each.getStartTimeBreak())) {
                        endTime = fmtDateTime.parse(each.getStartTimeBreak());//事实上既然有一个休息时间，StartTimeBreak应该就不会为空
                    }
                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));

                    startTime = fmtDateTime.parse(each.getEndTimeBreak());
                    endTime = fmtDateTime.parse(each.getEndTimeShift());
                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));
                }

            } else {
                //一个班次的休息时间个>=2
                if (each.getRowInx() == 1) {
                    //首个休息时间
                    startTime = fmtDateTime.parse(each.getStartTimeShift());
                    endTime = fmtDateTime.parse(each.getStartTimeBreak());

                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));

                    startTime = fmtDateTime.parse(each.getEndTimeBreak());
                } else if (each.getRowInx() == each.getCountRow()) {
                    //最后一个休息时间
                    endTime = fmtDateTime.parse(each.getStartTimeBreak());

                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));

                    startTime = fmtDateTime.parse(each.getEndTimeBreak());
                    endTime = fmtDateTime.parse(each.getEndTimeShift());

                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));
                } else {
                    //中间的休息时间
                    endTime = fmtDateTime.parse(each.getStartTimeBreak());
                    pmShcCalendarAtomEntities.add(viewPmShcCalendarAtomEntity(each, startTime, endTime));

                    startTime = fmtDateTime.parse(each.getEndTimeBreak());
                }

            }

        }

        List<PmShcCalendarAtomEntity> result = pmShcCalendarAtomEntities.stream().filter(c -> c.getWorkTime() > 0).collect(Collectors.toList());
        return result;
    }

    private PmShcCalendarAtomEntity viewPmShcCalendarAtomEntity(PmCalendarSourceDTO each, Date startTime, Date endTime) throws ParseException {
        DateFormat fmtDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        PmShcCalendarAtomEntity data = new PmShcCalendarAtomEntity();
        //   BeanUtils.copyProperties(each,data);
        data.setStartTime(startTime);
        data.setEndTime(endTime);
        data.setWorkshopCode(each.getWorkshopCode());
        data.setLineCode(each.getLineCode());
        data.setWorkDay(each.getWorkDay());
        data.setShiftName(each.getShiftName());
        data.setPrcShcCalendarId(each.getPrcShcCalendarId());
        data.setShiftStartTime(fmtDateTime.parse(each.getStartTimeShift()));
        data.setShiftEndTime(fmtDateTime.parse(each.getEndTimeShift()));
        data.setWorkTime((int) (endTime.getTime() - startTime.getTime()) / 1000);

        return data;
    }


    /**
     * 没有日历数据的线体继承所属车间的日历数据
     *
     * @param shopCalendarSourceList 所有车间的日历集合
     * @param lineCalendarSourceList 所有线体的日历集合
     * @param allPmWorkShopEntities  所有车间集合
     * @param allLineEntities        所有线体集合
     */
    private void lineInheritShopCalendar(List<PmCalendarSourceDTO> shopCalendarSourceList, List<PmCalendarSourceDTO> lineCalendarSourceList
            , List<PmWorkShopEntity> allPmWorkShopEntities, List<PmLineEntity> allLineEntities) {
        Map<String, List<PmCalendarSourceDTO>> mapShopCalendarGroup = shopCalendarSourceList.stream().collect(Collectors.groupingBy(c -> c.getWorkshopCode() + "_" + c.getWorkDay().getTime()));
        Iterator<Map.Entry<String, List<PmCalendarSourceDTO>>> iteratorShopCalendar = mapShopCalendarGroup.entrySet().iterator();
        while (iteratorShopCalendar.hasNext()) {
            Map.Entry<String, List<PmCalendarSourceDTO>> entry = iteratorShopCalendar.next();
            String key = entry.getKey();
            List<PmCalendarSourceDTO> currentGroupShopCalendarList = entry.getValue();
            if (CollectionUtils.isEmpty(currentGroupShopCalendarList)) {
                continue;
            }
            String eachWorkShopCode = key.split("_")[0];
            long longWorkDay = Long.parseLong(key.split("_")[1]);
            List<PmWorkShopEntity> eachWorkShopList = allPmWorkShopEntities.stream().filter(c -> c.getWorkshopCode().equals(eachWorkShopCode)).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(eachWorkShopList)) {
                continue;
            }
            PmWorkShopEntity eachWorkShop = eachWorkShopList.get(0);
            List<PmLineEntity> eachPmLineEntities = allLineEntities.stream().filter(c -> c.getPrcPmWorkshopId().equals(eachWorkShop.getId())).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(eachPmLineEntities)) {
                continue;
            }
            for (PmLineEntity item : eachPmLineEntities) {
                //判断线体当天是否有无排班，如果没有将继承车间排班
                if (lineCalendarSourceList.stream().noneMatch(c -> c.getLineCode().equals(item.getLineCode()) && c.getWorkDay().getTime() == longWorkDay)) {
                    for (PmCalendarSourceDTO eachCurrentGroupShopCalendar : currentGroupShopCalendarList) {
                        PmCalendarSourceDTO linePmCalendarSourceDTO = new PmCalendarSourceDTO();
                        BeanUtils.copyProperties(eachCurrentGroupShopCalendar, linePmCalendarSourceDTO);
                        linePmCalendarSourceDTO.setLineCode(item.getLineCode());
                        lineCalendarSourceList.add(linePmCalendarSourceDTO);
                    }
                }
            }
        }
    }

    /**
     * 删除某个时间以后的所有atom数据
     *
     * @param workDay
     */
    private void deletePmShcCalendarAtom(Date workDay) {
        //逻辑删除
        UpdateWrapper<PmShcCalendarAtomEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda()
                .set(PmShcCalendarAtomEntity::getIsDelete, true)
                .ge(PmShcCalendarAtomEntity::getWorkDay, workDay);
        //update(updateWrapper);
        //物理刪除
        delete(updateWrapper,false);
    }

}