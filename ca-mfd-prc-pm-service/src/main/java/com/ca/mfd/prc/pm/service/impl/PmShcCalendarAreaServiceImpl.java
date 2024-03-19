package com.ca.mfd.prc.pm.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.InkelinkExcelUtils;
import com.ca.mfd.prc.pm.dto.CalendarCopyDTO;
import com.ca.mfd.prc.pm.dto.LineVO;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmShcCalendarAreaEntity;
import com.ca.mfd.prc.pm.entity.PmShcShiftEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmShcCalendarAreaMapper;
import com.ca.mfd.prc.pm.service.IPmShcCalendarAreaService;
import com.ca.mfd.prc.pm.service.IPmShcShiftService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工厂日历
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Deprecated
@Service
public class PmShcCalendarAreaServiceImpl extends AbstractCrudServiceImpl<IPmShcCalendarAreaMapper, PmShcCalendarAreaEntity> implements IPmShcCalendarAreaService {
    @Autowired
    private IPmVersionService pmVersionService;
    @Autowired
    private IPmShcShiftService pmShcShiftService;

    /* private final Map<String, Map<String, String>> orderDic = new HashMap<>();
     {
         Map<String, String> mpStamping = new LinkedHashMap<>();
         mpStamping.put(MpSqlUtils.getColumnName(PmShcCalendarAreaEntity::getLineCode), "线体代码");
         mpStamping.put(MpSqlUtils.getColumnName(PmShcCalendarAreaEntity::getLineName), "线体名称");
         orderDic.put("检验模板", mpStamping);

     }

     @Override
     public void getImportTemplate(String fileName, HttpServletResponse response) throws IOException {
         super.setExcelColumnNames(orderDic.get("检验模板"));
         super.getImportTemplate(fileName, response);
     }*/
    @Override
    public void setExcelColumnNames(Map<String, String> columnNames) {
        if (columnNames == null || columnNames.size() == 0) {
            columnNames = new LinkedHashMap<>();
            columnNames.put("lineCode", "线体编码");
            columnNames.put("lineName", "线体名称");
            columnNames.put("workshopCode", "车间编码");
            columnNames.put("workshopName", "车间名称");
            columnNames.put("shcShiftCode", "班次编码");
            columnNames.put("shcShiftName", "班次名称");
            columnNames.put("workDay", "日期");

           /* columnNames.put("lineCode", "线体代码");
            columnNames.put("lineName", "线体名称");
            columnNames.put("shcShiftCode", "班次编码");
            columnNames.put("shcShiftName", "班次名称");
            columnNames.put("productivity", "产能");
            columnNames.put("workDay", "日期");
            columnNames.put("shcShiftStartDt", "班次开始时间");
            columnNames.put("shcShiftEndDt", "班次结束时间");
            columnNames.put("overtime", "加班时间(分钟)");*/
        }
        super.setExcelColumnNames(columnNames);
    }

    @Override
    public void export(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException {
        this.setExcelColumnNames(new HashMap(0));
        Map<String, String> fieldParam = getExcelColumnNames();
        List<PmShcCalendarAreaEntity> datas = this.selectList(null);
        // List<PqsInspectionTemplateEntity> datas=pqsInspectionTemplateMapper.getExcelData();
        List<Map<String, Object>> mapList = InkelinkExcelUtils.getListMap(datas);
        dealExcelDatas(mapList);
        InkelinkExcelUtils.exportByDc(fieldParam, mapList, fileName, response);
    }

    @Override
    public void batchSaveExcelData(List<Map<String, String>> data) throws Exception {
        List<PmShcCalendarAreaEntity> pmShcCalendarAreaEntities = this.convertExcelDataToEntity(data);
        saveExcelData(pmShcCalendarAreaEntities);
    }

    @Override
    public List<PmShcCalendarAreaEntity> getSourceCalendarAreaList(CalendarCopyDTO calendarCopyDTO) {
        QueryWrapper<PmShcCalendarAreaEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmShcCalendarAreaEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmShcCalendarAreaEntity::getLineCode, calendarCopyDTO.getLineCode());
        lambdaQueryWrapper.ge(PmShcCalendarAreaEntity::getWorkDay, calendarCopyDTO.getBeginTime());
        lambdaQueryWrapper.le(PmShcCalendarAreaEntity::getWorkDay, calendarCopyDTO.getEndTime());
        lambdaQueryWrapper.orderByAsc(PmShcCalendarAreaEntity::getWorkDay);
        return this.selectList(queryWrapper);
    }

    /**
     * 保存导入
     */
    @Override
    public void saveExcelData(List<PmShcCalendarAreaEntity> entities) {
        if (!CollectionUtils.isEmpty(entities) && entities.get(0) != null && entities.get(0).getWorkDay() != null) {
            checkOverdue(entities);
            checkDataRepeat(entities);
            updateLineInfo(entities);
            updateShiftInfo(entities);
            delExistsData(entities);
            super.saveExcelData(entities);
        }
    }

    /**
     * 检测过期
     */
    void checkOverdue(List<PmShcCalendarAreaEntity> entities) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long todayZero = calendar.getTimeInMillis();

        long i = entities.stream().filter(o -> o.getWorkDay().getTime() < todayZero).count();
        if (i > 0) {
            throw new InkelinkException("导入的线体日历中存在过期的数据");
        }
    }

    /**
     * 验证重复
     */
    void checkDataRepeat(List<PmShcCalendarAreaEntity> entities) {
        if (entities.stream().collect(Collectors.groupingBy(c -> c.getLineName() + Constant.SPLIT_JIN + c.getShcShiftName() + Constant.SPLIT_JIN + DateUtils.format(c.getWorkDay(), DateUtils.DATE_PATTERN)
                , Collectors.counting())).values().stream().anyMatch(c -> c > 1)) {
            throw new InkelinkException("在同一天存在相同线体相同班次的数据");
        }

    }

    /**
     * 更新线体信息
     */
    void updateLineInfo(List<PmShcCalendarAreaEntity> entities) {
        //要填写线体名称和编码，因此不需要设置了
        List<String> lineCodeList = entities.stream().map(PmShcCalendarAreaEntity::getLineCode).collect(Collectors.toList());
        //  List<LineVO> lineVOS = pmLineService.getByLineCodeList(lineCodeList);
        PmAllDTO allPm = pmVersionService.getObjectedPm();

        for (PmShcCalendarAreaEntity data : entities) {
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
        /*for (LineVO eachLine : lineVOS) {
            List<PmShcCalendarAreaEntity> datas = entities.stream().filter(o -> StringUtils.equals(o.getLineCode(), eachLine.getLineCode())).collect(Collectors.toList());
            for (PmShcCalendarAreaEntity data : datas) {
                data.setWorkShopCode(eachLine.getWorkShopCode());
                data.setWorkShopName(eachLine.getWorkShopName());
                data.setLineCode(eachLine.getLineCode());
                data.setLineName(eachLine.getLineName());
            }
        }
        for (PmShcCalendarAreaEntity entitie : entities) {
            if (StringUtils.isBlank(entitie.getLineCode())) {
                throw new InkelinkException("线体名称【" + entitie.getLineName() + "】在系统中找不到！");
            }
        }*/
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
    void updateShiftInfo(List<PmShcCalendarAreaEntity> entities) {
        List<PmShcShiftEntity> pmShcShiftInfos = pmShcShiftService.getData(new ArrayList<>());
        for (PmShcCalendarAreaEntity data : entities) {
            List<PmShcShiftEntity> matchShifts = pmShcShiftInfos.stream().filter(c -> c.getShiftCode().equals(data.getShcShiftCode())).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(matchShifts)) {
                throw new InkelinkException("班次编码【" + data.getShcShiftCode() + "】在系统中找不到");
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
            List<PmShcCalendarAreaEntity> datas = entities.stream().filter(o -> StringUtils.equals(o.getShcShiftName(), pmShcShiftInfo.getShiftName())).collect(Collectors.toList());
            for (PmShcCalendarAreaEntity data : datas) {
                data.setShcShiftCode(pmShcShiftInfo.getShiftCode());
                data.setShcShiftName(pmShcShiftInfo.getShiftName());
                String shiftStartTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + pmShcShiftInfo.getStartTime() + ":00";
                data.setShcShiftStartDt(DateUtils.parse(shiftStartTime, DateUtils.DATE_TIME_PATTERN));
                String shiftEndTime = DateUtils.format(data.getWorkDay(), "yyyy-MM-dd") + " " + pmShcShiftInfo.getEndTime() + ":00";
                data.setShcShiftEndDt(DateUtils.parse(shiftEndTime, DateUtils.DATE_TIME_PATTERN));
            }
        }
        for (PmShcCalendarAreaEntity entitie : entities) {
            if (entitie.getShcShiftCode() == null) {
                throw new InkelinkException("班次名称【" + entitie.getShcShiftName() + "】在系统中找不到");
            }
        }*/
    }

    /**
     * 删除已经存的数据
     */
    void delExistsData(List<PmShcCalendarAreaEntity> entities) {
        //根据车间和工作日，重置数据
        for (Map.Entry<String, List<PmShcCalendarAreaEntity>> data : entities.stream().collect(Collectors
                .groupingBy(c -> c.getWorkshopCode() + "#" + c.getLineCode() + "#" + DateUtils.format(c.getWorkDay(), "yyyy-MM-dd"))).entrySet()) {
            String[] keys = data.getKey().split("#");
            List<ConditionDto> delcons = new ArrayList<>();
            delcons.add(new ConditionDto("WORK_DAY", keys[2], ConditionOper.AllLike));
            delcons.add(new ConditionDto("WORKSHOP_CODE", keys[0], ConditionOper.Equal));
            delcons.add(new ConditionDto("LINE_CODE", keys[1], ConditionOper.Equal));
            delete(delcons);
        }
    }


}