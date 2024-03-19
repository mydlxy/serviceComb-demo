package com.ca.mfd.prc.pm.controller;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.CalendarCopyDTO;
import com.ca.mfd.prc.pm.dto.CalendarDeDTO;
import com.ca.mfd.prc.pm.dto.CalendarFromASDTO;
import com.ca.mfd.prc.pm.dto.CalendarParamDTO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.ShcCalendarDetailInfo;
import com.ca.mfd.prc.pm.dto.ShcWorkTimeInfo;
import com.ca.mfd.prc.pm.dto.ShcWorkTimePara;
import com.ca.mfd.prc.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pm.dto.ShopDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.service.IPmShcCalendarService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("pmshccalendar")
@Tag(name = "工厂日历")
public class PmShcCalendarController extends BaseController<PmShcCalendarEntity> {

    private final IPmShcCalendarService pmShcCalendarService;
    // private final IPmShcCalendarAreaService pmShcCalendarAreaService;

    @Autowired
    private IPmShcShiftService pmShcShiftService;
    @Autowired
    private IPmVersionService pmVersionService;

    @Autowired
    public PmShcCalendarController(IPmShcCalendarService pmShcCalendarService) {
        this.crudService = pmShcCalendarService;
        this.pmShcCalendarService = pmShcCalendarService;
        //  this.pmShcCalendarAreaService = pmShcCalendarAreaService;
    }

    @Operation(summary = "获取线体列表")
    @GetMapping("getlines")
    public ResultVO getLines() {
        ResultVO result = new ResultVO<>();
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        List<PmLineEntity> lineEntities = allPm.getLines();
        return result.ok(lineEntities);
    }

    @Operation(summary = "获取车间列表")
    @GetMapping("getshops")
    public ResultVO getShops() {
        ResultVO result = new ResultVO<>();
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        List<PmWorkShopEntity> shops = allPm.getShops();
        return result.ok(shops);
    }

    @Operation(summary = "获取车间列表")
    @GetMapping("getshopinfos")
    public ResultVO getShopInfos() {
        ResultVO result = new ResultVO<>();
        List<ShopDTO> data = pmShcCalendarService.getCalendarShopInfos();
        return result.ok(data);
    }

    @Operation(summary = "获取已经排班记录")
    @PostMapping("getpmshccalendarinfos")
    public ResultVO getPmShcCalendarInfos(@RequestBody CalendarParamDTO calendarParamInfo) {
        ResultVO result = new ResultVO<>();
        List<PmShcCalendarEntity> data = pmShcCalendarService.getPmShcCalendarInfos(calendarParamInfo);
        return result.ok(data);
    }

    @Operation(summary = "获取班次列表")
    @PostMapping("getpmshcshiftinfo")
    public ResultVO getPmShcShiftInfo() {
        ResultVO result = new ResultVO<>();
        List<PmShcShiftEntity> data = pmShcShiftService.getData(new ArrayList<>());
        return result.ok(data);
    }

    @Operation(summary = "按数组保存班次日历")
    @PostMapping("savedata")
    public ResultVO saveData(@RequestBody List<PmShcCalendarEntity> pmShcCalendarInfos) {
        ResultVO result = new ResultVO<>();
        result.setMessage("班次保存成功");
        //List<Integer> shcShiftCodes= pmShcCalendarInfos.stream().filter(c->c.getShcShiftCode() != null)
        //        .map(c->c.getShcShiftCode()).collect(Collectors.toList());
        //List<PmShcShiftEntity> shcEntitys =  pmShcShiftService.getListByCodes(shcShiftCodes);
        for (PmShcCalendarEntity data : pmShcCalendarInfos) {
            if (data.getTypeFlag() == 1) {
                //如果是车间的话，将线体编码和线体名称都设置成空字符
                if (!org.springframework.util.StringUtils.hasText(data.getWorkshopCode()) || !org.springframework.util.StringUtils.hasText(data.getWorkshopName())) {
                    throw new InkelinkException("保存车间，车间编码和车间名称必须有值！");
                }
                data.setLineCode("");
                data.setLineName("");
            } else {
                if (!org.springframework.util.StringUtils.hasText(data.getLineCode()) || !org.springframework.util.StringUtils.hasText(data.getLineName())) {
                    throw new InkelinkException("保存线体，线体编码和线体名称必须有值！");
                }
            }
//            PmShcShiftEntity shcEntity = shcEntitys.stream().filter(c -> Objects.equals(c.getShiftCode(), data.getShcShiftCode())).findFirst().orElse(null);
//            if (shcEntity != null && shcEntity.getIsDayAfter()) {
//                String endtime = DateUtils.format(DateUtils.addDateDays(data.getWorkDay(), 1), DateUtils.DATE_PATTERN)
//                        + " " + shcEntity.getEndTime().trim() + ":00";
//                data.setShcShiftEndDt(DateUtils.parse(endtime, DateUtils.DATE_TIME_PATTERN));
//            }
//            if (data.getShcShiftStartDt().getTime() >= data.getShcShiftEndDt().getTime()) {
//                data.setShcShiftEndDt(DateUtils.addDateDays(data.getShcShiftEndDt(), 1));
//            }
            if (data.getId() == null || data.getId().equals(Constant.DEFAULT_ID)) {
                List<ConditionDto> conditions = new ArrayList<>();
                if (data.getTypeFlag() == 1) {
                    conditions.add(new ConditionDto("LINE_CODE", "", ConditionOper.Equal));
                    conditions.add(new ConditionDto("WORKSHOP_CODE", data.getWorkshopCode(), ConditionOper.Equal));
                } else {
                    conditions.add(new ConditionDto("LINE_CODE", data.getLineCode(), ConditionOper.Equal));
                }
                conditions.add(new ConditionDto("SHC_SHIFT_CODE", String.valueOf(data.getShcShiftCode()), ConditionOper.Equal));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String workDay = dateFormat.format(data.getWorkDay());
                conditions.add(new ConditionDto("WORK_DAY", workDay, ConditionOper.Equal));
                List<PmShcCalendarEntity> dbShcCalendars = pmShcCalendarService.getData(conditions);

                if (!CollectionUtil.isEmpty(dbShcCalendars) && dbShcCalendars.size() > 0) {
                    if (data.getTypeFlag() == 1) {
                        throw new InkelinkException("车间 " + data.getWorkshopName() + " 在" + workDay + data.getShcShiftName() + "班次设置重复!");
                    } else {
                        throw new InkelinkException("线体 " + data.getLineName() + " 在" + workDay + data.getShcShiftName() + "班次设置重复!");
                    }
                }
                pmShcCalendarService.insert(data);
            } else {
                pmShcCalendarService.update(data);
            }
        }
        pmShcCalendarService.saveChange();

       /* List<PmShcCalendarEntity> shcCalendarEntities = new ArrayList<>();
        List<PmShcCalendarAreaEntity> shcCalendarAreaEntities = new ArrayList<>();
        for (PmShcCalendarEntity data : pmShcCalendarInfos) {
            if (data.getTypeFlag() == 1) {
                //车间
                shcCalendarEntities.add(data);
            } else if (data.getTypeFlag() == 2) {
                //线体
                PmShcCalendarAreaEntity shcCalendarAreaEntity = new PmShcCalendarAreaEntity();
                BeanUtils.copyProperties(data, shcCalendarAreaEntity);
                shcCalendarAreaEntities.add(shcCalendarAreaEntity);
            }
        }*/

        /*for (PmShcCalendarEntity data : shcCalendarEntities) {
            if (data.getShcShiftStartDt().getTime() >= data.getShcShiftEndDt().getTime()) {
                data.setShcShiftStartDt(DateUtils.addDateDays(data.getShcShiftEndDt(), 1));
            }
            if (data.getId() == null || data.getId().equals(Constant.DEFAULT_ID)) {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("WORKSHOP_CODE", data.getWorkShopCode(), ConditionOper.Equal));
                conditions.add(new ConditionDto("SHC_SHIFT_CODE", String.valueOf(data.getShcShiftCode()), ConditionOper.Equal));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String workDay = dateFormat.format(data.getWorkDay());
                conditions.add(new ConditionDto("WORK_DAY", workDay, ConditionOper.Equal));
                List<PmShcCalendarEntity> dbShcCalendars = pmShcCalendarService.getData(conditions);
                if (!CollectionUtil.isEmpty(dbShcCalendars) && dbShcCalendars.size() > 0) {
                    throw new InkelinkException("车间 " + data.getWorkShopName() + " 在" + workDay + "已经设置了 " + data.getShcShiftName() + "的班次!");
                }
                pmShcCalendarService.insert(data);
            } else {
                pmShcCalendarService.update(data);
            }
        }*/

        /*for (PmShcCalendarAreaEntity data : shcCalendarAreaEntities) {
            if (data.getShcShiftStartDt().getTime() >= data.getShcShiftEndDt().getTime()) {
                data.setShcShiftStartDt(DateUtils.addDateDays(data.getShcShiftEndDt(), 1));
            }
            if (data.getId() == null || data.getId().equals(Constant.DEFAULT_ID)) {
                List<ConditionDto> conditions = new ArrayList<>();
                conditions.add(new ConditionDto("WORKSHOP_CODE", data.getWorkShopCode(), ConditionOper.Equal));
                conditions.add(new ConditionDto("LINE_CODE", data.getLineCode(), ConditionOper.Equal));
                conditions.add(new ConditionDto("SHC_SHIFT_CODE", String.valueOf(data.getShcShiftCode()), ConditionOper.Equal));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String workDay = dateFormat.format(data.getWorkDay());
                conditions.add(new ConditionDto("WORK_DAY", workDay, ConditionOper.Equal));
                List<PmShcCalendarAreaEntity> dbShcCalendarAreas = pmShcCalendarAreaService.getData(conditions);
                if (!CollectionUtil.isEmpty(dbShcCalendarAreas) && dbShcCalendarAreas.size() > 0) {
                    throw new InkelinkException("车间 " + data.getWorkShopName() + "的线体 " + data.getLineName() + " 在" + workDay + "已经设置了 " + data.getShcShiftName() + "的班次!");
                }
                pmShcCalendarAreaService.insert(data);
            } else {
                pmShcCalendarAreaService.update(data);
            }
        }*/

       /* pmShcCalendarService.saveChange();
        pmShcCalendarAreaService.saveChange();*/
        return result.ok("");
    }

    @Operation(summary = "复制日历数据")
    @PostMapping("copycalendararea")
    public ResultVO copycalendararea(@RequestBody CalendarCopyDTO calendarCopyDTO) {
        ResultVO result = new ResultVO<>();
        result.setMessage("复制成功");
        if (StringUtils.isBlank(calendarCopyDTO.getWorkshopCode()) && StringUtils.isBlank(calendarCopyDTO.getLineCode())) {
            throw new InkelinkException("没有获取到源车间或者线体信息");
        }
        if (calendarCopyDTO.getBeginTime().getTime() <= System.currentTimeMillis()) {
            throw new InkelinkException("不能复制今天以前（含今天）的排班信息");
        }
        if (calendarCopyDTO.getBeginTime().getTime() > calendarCopyDTO.getEndTime().getTime()) {
            throw new InkelinkException("开始时间不能大于结束时间");
        }
        if (!StringUtils.isBlank(calendarCopyDTO.getWorkshopCode())) {
            if (CollectionUtils.isEmpty(calendarCopyDTO.getDistWorkshopCodeList())) {
                throw new InkelinkException("没有获取到目的车间信息");
            }
            copyShopCalendar(calendarCopyDTO);
        }
        if (!StringUtils.isBlank(calendarCopyDTO.getLineCode())) {
            if (CollectionUtils.isEmpty(calendarCopyDTO.getDistLineCodeList())) {
                throw new InkelinkException("没有获取到目的线体信息");
            }
            copyLineCalendar(calendarCopyDTO);
        }
        pmShcCalendarService.saveChange();

        return result.ok("");
    }

    /**
     * 复制车间日历
     */
    private void copyShopCalendar(CalendarCopyDTO calendarCopyDTO) {
        //源车间的日历集合
        List<PmShcCalendarEntity> dbPmShcCalendarEntities = pmShcCalendarService.getSourceCalendarList(calendarCopyDTO);
        if (!CollectionUtils.isEmpty(dbPmShcCalendarEntities)) {
            //原来有的删除，然后插入数据
            LambdaUpdateWrapper<PmShcCalendarEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmShcCalendarEntity::getIsDelete, true);
            upset.ge(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getBeginTime());
            upset.le(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getEndTime());
            upset.eq(PmShcCalendarEntity::getLineCode, "");
            upset.in(PmShcCalendarEntity::getWorkshopCode, calendarCopyDTO.getDistWorkshopCodeList().stream().map(CalendarCopyDTO.DistWorkShop::getWorkshopCode).collect(Collectors.toList()));
            pmShcCalendarService.update(upset);

            List<PmShcCalendarEntity> pmShcCalendarEntities = new ArrayList<>();
            for (PmShcCalendarEntity dbEntity : dbPmShcCalendarEntities) {
                for (CalendarCopyDTO.DistWorkShop eachDistWorkShop : calendarCopyDTO.getDistWorkshopCodeList()) {
                    PmShcCalendarEntity each = new PmShcCalendarEntity();
                    each.setWorkshopCode(eachDistWorkShop.getWorkshopCode());
                    each.setWorkshopName(eachDistWorkShop.getWorkshopName());
                    each.setShcShiftCode(dbEntity.getShcShiftCode());
                    each.setShcShiftName(dbEntity.getShcShiftName());
                    each.setProductivity(dbEntity.getProductivity());
                    each.setWorkDay(dbEntity.getWorkDay());
                    each.setShcShiftStartDt(dbEntity.getShcShiftStartDt());
                    each.setShcShiftEndDt(dbEntity.getShcShiftEndDt());
                    each.setOvertime(dbEntity.getOvertime());

                    pmShcCalendarEntities.add(each);
                }
            }
            pmShcCalendarService.insertBatch(pmShcCalendarEntities);
        }
    }

    /**
     * 复制线体日历
     */
    private void copyLineCalendar(CalendarCopyDTO calendarCopyDTO) {
        //源线体的日历集合
        List<PmShcCalendarEntity> dbPmShcCalendarEntities = pmShcCalendarService.getSourceCalendarLineList(calendarCopyDTO);
        if (CollectionUtils.isEmpty(dbPmShcCalendarEntities)) {
            return;
        }

        // 如果目标线体的车间编码没传，需要从数据库中查询出来赋值
        PmAllDTO allPm = pmVersionService.getObjectedPm();
        List<PmLineEntity> allPmLines = allPm.getLines();//通过线体获得车间id，然后通过车间id拿到车间code和车间name
        List<PmWorkShopEntity> allShopList = allPm.getShops();

        for (CalendarCopyDTO.DistLine eachDistLine : calendarCopyDTO.getDistLineCodeList()) {
            for (PmLineEntity eachLine : allPmLines) {
                if (eachLine.getLineCode().equals(eachDistLine.getLineCode())) {
                    setWorkshopCodeAndName(eachDistLine, eachLine, allShopList);
                    break;
                }
               /* if (eachLine.getLineCode().equals(eachDistLine.getLineCode())) {
                    Long workShopId = eachLine.getPrcPmWorkshopId();
                    for (PmWorkShopEntity eachWorkShop : allShopList) {
                        if (eachWorkShop.getId().equals(workShopId)) {
                            eachDistLine.setWorkshopCode(eachWorkShop.getWorkshopCode());
                            eachDistLine.setWorkshopName(eachWorkShop.getWorkshopName());
                            break;
                        }
                    }
                    break;
                }*/
            }
        }
        //原来有的删除，然后插入数据
        LambdaUpdateWrapper<PmShcCalendarEntity> upset = new LambdaUpdateWrapper<>();
        upset.set(PmShcCalendarEntity::getIsDelete, true);
        upset.ge(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getBeginTime());
        upset.le(PmShcCalendarEntity::getWorkDay, calendarCopyDTO.getEndTime());
        upset.in(PmShcCalendarEntity::getLineCode, calendarCopyDTO.getDistLineCodeList().stream().map(CalendarCopyDTO.DistLine::getLineCode).collect(Collectors.toList()));
        pmShcCalendarService.update(upset);

        List<PmShcCalendarEntity> pmShcCalendarEntities = new ArrayList<>();
        for (PmShcCalendarEntity dbEntity : dbPmShcCalendarEntities) {
            for (CalendarCopyDTO.DistLine eachDistLine : calendarCopyDTO.getDistLineCodeList()) {
                PmShcCalendarEntity each = new PmShcCalendarEntity();
                each.setLineCode(eachDistLine.getLineCode());
                each.setLineName(eachDistLine.getLineName());
                each.setWorkshopCode(eachDistLine.getWorkshopCode());
                each.setWorkshopName(eachDistLine.getWorkshopName());
                each.setShcShiftCode(dbEntity.getShcShiftCode());
                each.setShcShiftName(dbEntity.getShcShiftName());
                each.setProductivity(dbEntity.getProductivity());
                each.setWorkDay(dbEntity.getWorkDay());
                each.setShcShiftStartDt(dbEntity.getShcShiftStartDt());
                each.setShcShiftEndDt(dbEntity.getShcShiftEndDt());
                each.setOvertime(dbEntity.getOvertime());

                pmShcCalendarEntities.add(each);
            }
        }
        pmShcCalendarService.insertBatch(pmShcCalendarEntities);

    }

    private void setWorkshopCodeAndName(CalendarCopyDTO.DistLine eachDistLine, PmLineEntity eachLine, List<PmWorkShopEntity> allShopList) {
        Long workShopId = eachLine.getPrcPmWorkshopId();
        for (PmWorkShopEntity eachWorkShop : allShopList) {
            if (eachWorkShop.getId().equals(workShopId)) {
                eachDistLine.setWorkshopCode(eachWorkShop.getWorkshopCode());
                eachDistLine.setWorkshopName(eachWorkShop.getWorkshopName());
                break;
            }
        }
    }


    @Operation(summary = "重置日历数据")
    @PostMapping("resetdata")
    public ResultVO resetData(@RequestBody CalendarDeDTO calendarDelInfo) {
        ResultVO result = new ResultVO<>();
        result.setMessage("重置成功");
        if (StringUtils.isBlank(calendarDelInfo.getShopCode()) && StringUtils.isBlank(calendarDelInfo.getLineCode())) {
            throw new InkelinkException("没有获取到车间和线体信息");
        }
        if (calendarDelInfo.getBeginDt().getTime() <= System.currentTimeMillis()) {
            throw new InkelinkException("不能删除今天以前（含今天）的排班信息");
        }
        if (calendarDelInfo.getBeginDt().getTime() > calendarDelInfo.getEndDt().getTime()) {
            throw new InkelinkException("开始时间不能大于结束时间");
        }
        if (!StringUtils.isBlank(calendarDelInfo.getLineCode())) {
            LambdaUpdateWrapper<PmShcCalendarEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmShcCalendarEntity::getIsDelete, true);
            upset.ge(PmShcCalendarEntity::getWorkDay, calendarDelInfo.getBeginDt());
            upset.le(PmShcCalendarEntity::getWorkDay, calendarDelInfo.getEndDt());
            upset.eq(PmShcCalendarEntity::getLineCode, calendarDelInfo.getLineCode());
            pmShcCalendarService.update(upset);
        }
        if (!StringUtils.isBlank(calendarDelInfo.getShopCode()) && StringUtils.isBlank(calendarDelInfo.getLineCode())) {
            //只有线体编码为空时，才表示要重置车间日历
            LambdaUpdateWrapper<PmShcCalendarEntity> upset = new LambdaUpdateWrapper<>();
            upset.set(PmShcCalendarEntity::getIsDelete, true);
            upset.ge(PmShcCalendarEntity::getWorkDay, calendarDelInfo.getBeginDt());
            upset.le(PmShcCalendarEntity::getWorkDay, calendarDelInfo.getEndDt());
            upset.eq(PmShcCalendarEntity::getWorkshopCode, calendarDelInfo.getShopCode());
            upset.eq(PmShcCalendarEntity::getLineCode, "");//设置车间的值，需要加上线体编码不等于空的条件
            pmShcCalendarService.update(upset);
        }

        pmShcCalendarService.saveChange();
        return result.ok("");
    }

    @PostMapping("/provider/getdata")
    @Operation(summary = "根据条件查询工厂日历数据")
    public ResultVO<List<PmShcCalendarEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO<List<PmShcCalendarEntity>>().ok(pmShcCalendarService.getData(conditions));
    }

    //    @GetMapping("/provider/test111")
    //    @Operation(summary = "test接口数据")
    //    public ResultVO getCurrentShiftInfo() throws InterruptedException {
    //        //  Thread.sleep(1000 * 1000);
    //        return new ResultVO<ShiftDTO>().ok(null);
    //    }

    @GetMapping("/provider/getcurrentshiftinfo")
    @Operation(summary = "获取当前产线的工厂日历")
    public ResultVO getCurrentShiftInfo(@RequestParam String lineCode) {
        return new ResultVO<ShiftDTO>().ok(pmShcCalendarService.getCurrentShiftInfo(lineCode));
    }

    @Operation(summary = "获取排班信息")
    @GetMapping("/provider/getcurrentshift")
    public ResultVO<ShcCalendarDetailInfo> getCurrentShift(String lineCode) {
        return new ResultVO<ShcCalendarDetailInfo>().ok(pmShcCalendarService.getCurrentShift(lineCode));
    }

    @GetMapping("/provider/calculateworktime")
    @Operation(summary = "设置工作时间")
    public ResultVO calculateWorkTime(@RequestParam String startTime, @RequestParam String endTime, @RequestParam String shopCode) {
        return new ResultVO<Integer>().ok(pmShcCalendarService.calculateWorkTime(DateUtils.parse(startTime), DateUtils.parse(endTime), shopCode));
    }

    @PostMapping("/provider/getworktimes")
    @Operation(summary = "获取工作时间")
    public ResultVO getWorkTimes(@RequestBody ShcWorkTimePara para) {
        return new ResultVO<List<ShcWorkTimeInfo>>().ok(pmShcCalendarService.getWorkTimes(para.getWorkDay(), para.getShopCode(), para.getDayas()));
    }
}