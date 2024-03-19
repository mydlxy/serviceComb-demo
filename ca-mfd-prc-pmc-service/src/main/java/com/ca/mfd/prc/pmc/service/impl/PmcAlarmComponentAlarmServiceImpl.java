package com.ca.mfd.prc.pmc.service.impl;

import cn.hutool.crypto.SmUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IApiPtformService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.pmc.config.IotConsumerConfiguration;
import com.ca.mfd.prc.pmc.dto.*;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopRecordReasonEntity;
import com.ca.mfd.prc.pmc.entity.PmcAlarmAreaStopSetEntity;
import com.ca.mfd.prc.pmc.entity.PmcAlarmComponentAlarmEntity;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntDetailEntity;
import com.ca.mfd.prc.pmc.entity.PmcAlarmEquipmntModelEntity;
import com.ca.mfd.prc.pmc.mapper.IPmcAlarmComponentAlarmMapper;
import com.ca.mfd.prc.pmc.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmEquipmentService;
import com.ca.mfd.prc.pmc.remote.app.pm.dto.AtomicTime;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmEquipmentProvider;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.pmc.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopRecordReasonService;
import com.ca.mfd.prc.pmc.service.IPmcAlarmAreaStopSetService;
import com.ca.mfd.prc.pmc.service.IPmcAlarmComponentAlarmService;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntDetailService;
import com.ca.mfd.prc.pmc.service.IPmcAlarmEquipmntModelService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.logging.log4j.util.StringBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 报警原始记录
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class PmcAlarmComponentAlarmServiceImpl extends AbstractCrudServiceImpl<IPmcAlarmComponentAlarmMapper, PmcAlarmComponentAlarmEntity> implements IPmcAlarmComponentAlarmService {

    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PmEquipmentProvider pmEquipmentProvider;
    @Autowired
    private IPmcAlarmEquipmntDetailService pmcAlarmEquipmntDetailService;
    @Autowired
    private IPmcAlarmAreaStopRecordReasonService pmcAlarmAreaStopRecordReasonService;
    @Autowired
    private IPmcAlarmAreaStopSetService pmcAlarmAreaStopSetService;
    @Autowired
    private IPmcAlarmEquipmntModelService pmcAlarmEquipmntModelService;
    @Autowired
    private PmShcCalendarProvider pmShcCalendarProvider;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    @Lazy
    private IotConsumerConfiguration iotConsumerConfiguration;

    @Autowired
    private LocalCache localCache;

    @Autowired
    @Qualifier("apiPtService")
    private IApiPtformService apiPtService;

    private static final String PMC_ALL_CACHE_NAME_DESCRIBE = "PMC_ALL_CACHE_NAME_DESCRIBE";

    private static final Object LOCK_OBJ = new Object();

    @Override
    public List<PmcAlarmComponentAlarmEntity> getAlarmComponentAlarmList(String startTime, String endTime, String postion) {
        List<ConditionDto> conditionInfos = new ArrayList<ConditionDto>();
        ConditionDto conditionDto = new ConditionDto();
        conditionDto.setColumnName("POSITION");
        conditionDto.setOperator(ConditionOper.RightLike);
        conditionDto.setValue(postion);
        conditionInfos.add(conditionDto);

        if (!Strings.isNullOrEmpty(startTime)) {
            ConditionDto condition = new ConditionDto();
            condition.setColumnName("ALARM_DT");
            condition.setOperator(ConditionOper.GreaterThanEqual);
            condition.setValue(startTime);
            conditionInfos.add(condition);
        }
        if (!Strings.isNullOrEmpty(endTime)) {
            ConditionDto condition = new ConditionDto();
            condition.setColumnName("ALARM_DT");
            condition.setOperator(ConditionOper.LessThanEqual);
            condition.setValue(endTime);
            conditionInfos.add(condition);
        }
        return this.getData(conditionInfos);
    }

    @Override
    public void addAndonAlarm(CreateAndonAlarmDTO para) {
        Integer alarmLevel = 0;
        switch (para.getType()) {
            case 1:
                alarmLevel = 1;
                break;
            case 2:
                alarmLevel = 2;
                break;
            case 3:
                alarmLevel = 3;
                break;
            case 4:
                alarmLevel = 4;
                break;
            case 5:
                alarmLevel = 5;
                break;
            case 6:
                alarmLevel = 6;
                break;
            case 20:
                alarmLevel = 7;
                break;
            case 30:
                alarmLevel = 8;
                break;
            default:
                alarmLevel = 9;
                break;
        }
        PmcAlarmComponentAlarmEntity alarm = new PmcAlarmComponentAlarmEntity();
        alarm.setAlarmComponentAlarmCode("Andon_" + alarmLevel);
        alarm.setWinccCounter(2L);
        alarm.setStatus(para.getIsStart() ? 1 : 2);
        alarm.setPosition(para.getPostion());
        alarm.setAlarmComponentAlarmDescription(para.getDescription());
        alarm.setAlarmDt(new Date());
        this.insert(alarm);

    }

    @Override
    public void data1() {
        List<Map<String, String>> areaShopIdMap = pmVersionProvider.getObjectedPm().getLines().stream().map(ele -> {
            Map<String, String> m = new HashMap(5);
            m.put("areaCode", ele.getLineCode());
            m.put("prcPmWorkshopId", ele.getPrcPmWorkshopId().toString());
            return m;
        }).collect(Collectors.toList());

        List<Map<String, String>> shopIds = pmVersionProvider.getObjectedPm().getShops().stream().map(ele -> {
            Map<String, String> m = new HashMap(5);
            m.put("id", ele.getId().toString());
            m.put("shopCode", ele.getWorkshopCode());
            return m;
        }).collect(Collectors.toList());

        List<Map<String, String>> areaShopMap = areaShopIdMap.stream().map(ele -> {
            Map<String, String> m = new HashMap(5);
            m.put("areaCode", ele.get("areaCode"));
            m.put("shopCode", shopIds.stream().filter(item -> StringUtils.equals(item.get("id"), ele.get("prcPmWorkshopId"))).map(item -> item.get("shopCode")).findFirst().orElse(null));
            return m;
        }).collect(Collectors.toList());

        /* 2.PMC数据处理 */
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("alarmEquipmntDetailType", "1", ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("endDt", null, ConditionOper.Equal));

        List<PmcAlarmComponentAlarmEntity> noEndDtData = pmcAlarmEquipmntDetailService.getData(conditionDtos).stream().map(ele -> {
            PmcAlarmComponentAlarmEntity entity = new PmcAlarmComponentAlarmEntity();
            entity.setPosition(ele.getPosition());
            entity.setAlarmLevel(ele.getAlarmLevel());
            entity.setAlarmComponentAlarmCode(ele.getAlarmEquipmntDetailCode());
            entity.setStatus(1);
            entity.setAlarmDt(ele.getBeginDt());
            entity.setAlarmComponentAlarmDescription(ele.getAlarmEquipmntDetailDescription());
            return entity;
        }).collect(Collectors.toList());

        List<ConditionDto> conditionDtos1 = new ArrayList<>();
        conditionDtos1.add(new ConditionDto("isDataDown", "0", ConditionOper.Equal));
        List<PmcAlarmComponentAlarmEntity> sourceData = this.getData(conditionDtos1).stream().sorted(Comparator.comparing(PmcAlarmComponentAlarmEntity::getAlarmDt)).map(ele -> {
            PmcAlarmComponentAlarmEntity entity = new PmcAlarmComponentAlarmEntity();
            entity.setId(ele.getId());
            entity.setPosition(ele.getPosition());
            entity.setAlarmLevel(ele.getAlarmLevel());
            entity.setAlarmComponentAlarmCode(ele.getAlarmComponentAlarmCode());
            entity.setStatus(ele.getStatus());
            entity.setAlarmDt(ele.getAlarmDt());
            entity.setAlarmComponentAlarmDescription(ele.getAlarmComponentAlarmDescription());
            return entity;
        }).limit(2000).collect(Collectors.toList());

        // 清洗后的数据放到这里
        List<PmcAlarmComponentAlarmEntity> preResult = new ArrayList<>();
        preResult.addAll(noEndDtData);
        preResult.addAll(sourceData);


        // 计算前的数据
        preResult = preResult.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(e -> e.getPosition() + "#" + e.getAlarmLevel() + "#" + e.getStatus() + "#" + e.getAlarmDt() + "#" + e.getAlarmComponentAlarmDescription() + "#" + e.getAlarmComponentAlarmCode()))), ArrayList::new)).stream().map(ele -> {
            PmcAlarmComponentAlarmEntity alarmEntity = new PmcAlarmComponentAlarmEntity();
            alarmEntity.setPosition(ele.getPosition());
            alarmEntity.setAlarmLevel(ele.getAlarmLevel());
            alarmEntity.setAlarmComponentAlarmCode(ele.getAlarmComponentAlarmCode());
            alarmEntity.setStatus(ele.getStatus());
            alarmEntity.setAlarmDt(ele.getAlarmDt());
            alarmEntity.setAlarmComponentAlarmDescription(ele.getAlarmComponentAlarmDescription());
            return alarmEntity;
        }).sorted(Comparator.comparing(PmcAlarmComponentAlarmEntity::getAlarmDt)).collect(Collectors.toList());

        //之后都是针对preResult的数据来进行处理计算
        List<PmcAlarmComponentAlarmEntity> metaData = preResult.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(e -> e.getPosition() + "#" + e.getAlarmLevel() + "#" + e.getAlarmComponentAlarmCode()))), ArrayList::new)).stream().map(ele -> {
            PmcAlarmComponentAlarmEntity alarmEntity = new PmcAlarmComponentAlarmEntity();
            alarmEntity.setPosition(ele.getPosition());
            alarmEntity.setAlarmLevel(ele.getAlarmLevel());
            alarmEntity.setAlarmComponentAlarmCode(ele.getAlarmComponentAlarmCode());
            return alarmEntity;
        }).sorted(Comparator.comparing(PmcAlarmComponentAlarmEntity::getPosition).thenComparing(PmcAlarmComponentAlarmEntity::getAlarmLevel).thenComparing(PmcAlarmComponentAlarmEntity::getAlarmComponentAlarmCode)).collect(Collectors.toList());

        //需要插入的数据
        List<PmcAlarmEquipmntDetailEntity> pmcInsertData = new ArrayList<>();
        //计算逻辑,先是按照position和alarmLevel和code分类
        List<PmcAlarmComponentAlarmEntity> finalPreResult = preResult;
        metaData.forEach(ele -> {
            PmcAlarmEquipmntDetailEntity detailEntity = new PmcAlarmEquipmntDetailEntity();
            List<PmcAlarmComponentAlarmEntity> temp = finalPreResult.stream().filter(item -> item.getPosition().equals(ele.getPosition()) && item.getAlarmLevel().equals(ele.getAlarmLevel()) && item.getAlarmComponentAlarmCode().equals(ele.getAlarmComponentAlarmCode())).sorted(Comparator.comparing(PmcAlarmComponentAlarmEntity::getAlarmDt)).collect(Collectors.toList());
            int rowInx = 1;
            for (PmcAlarmComponentAlarmEntity item : temp) {
                if (item.getStatus() == 1) {
                    //status = "insert";
                    if (detailEntity.getBeginDt() == null) {
                        detailEntity.setPosition(ele.getPosition());
                        detailEntity.setBeginDt(item.getAlarmDt());
                        detailEntity.setAlarmLevel(ele.getAlarmLevel());
                        detailEntity.setAlarmEquipmntDetailCode(ele.getAlarmComponentAlarmCode());
                        detailEntity.setAlarmEquipmntDetailDescription(item.getAlarmComponentAlarmDescription());
                        detailEntity.setLineCode(this.postionGetValue(ele.getPosition()).get(0));
                    }
                    // 解决12221以最后的2作为结束的问题
                    if (detailEntity.getEndDt() != null) {
                        pmcInsertData.add(detailEntity);
                        detailEntity = new PmcAlarmEquipmntDetailEntity();
                        detailEntity.setPosition(ele.getPosition());
                        detailEntity.setBeginDt(item.getAlarmDt());
                        detailEntity.setAlarmLevel(ele.getAlarmLevel());
                        detailEntity.setAlarmEquipmntDetailCode(ele.getAlarmComponentAlarmCode());
                        detailEntity.setIsDataDown(false);
                        detailEntity.setAlarmEquipmntDetailDescription(item.getAlarmComponentAlarmDescription());
                        detailEntity.setLineCode(this.postionGetValue(ele.getPosition()).get(0));

                    }
                }
                if (item.getStatus() == 2) {
                    if (detailEntity.getBeginDt() != null) {
                        detailEntity.setEndDt(item.getAlarmDt());
                        detailEntity.setAlarmEquipmntDetailDescription(item.getAlarmComponentAlarmDescription());
                        detailEntity.setLineCode(this.postionGetValue(ele.getPosition()).get(0));
                    }
                }
                if (rowInx == temp.size() && detailEntity.getBeginDt() != null) {
                    pmcInsertData.add(detailEntity);
                }
                rowInx++;
            }

        });

        Integer second = 60;

        /*3.计算duration和标记小于60s停线*/
        pmcInsertData.forEach(ele -> {
            String shopCode = areaShopMap.stream().filter(item -> item.get("areaCode").equals(ele.getLineCode())).map(item -> item.get("shopCode")).findFirst().orElse("");
            ele.setWorkshopCode(shopCode);
            if (ele.getEndDt() != null) {
                Integer duration = pmShcCalendarProvider.calculateWorkTime(DateUtils.format(ele.getBeginDt(), DateUtils.DATE_TIME_PATTERN), DateUtils.format(ele.getEndDt(), DateUtils.DATE_TIME_PATTERN), shopCode);
                if (duration <= second) {// 小于60s标记为已经填报
                    ele.setIsFill(true);
                    ele.setStopCode("小于60s停线");
                    ele.setStopName("小于60s停线");
                    ele.setStopCodeDesc("小于60s停线");
                    ele.setStopType("小于60s停线");
                    ele.setStopCauseType("小于60s停线");
                    ele.setStopDepartment("小于60s停线");
                }
                ele.setDuration(duration);
            }
        });

        /* 4.所有的数据汇总，区分哪些数据是需要更新的，哪些数据是需要插入的*/
        List<PmcAlarmEquipmntDetailEntity> preInsert = new ArrayList<PmcAlarmEquipmntDetailEntity>();
        preInsert.addAll(pmcInsertData);
        // 要更新的数据
        List<ConditionDto> dtos = new ArrayList<>();
        dtos.add(new ConditionDto("endDt", null, ConditionOper.Equal));
        List<Map> allno = pmcAlarmEquipmntDetailService.getData(dtos).stream().map(ele -> {
            Map m = new HashMap(10);
            m.put("id", ele.getId());
            m.put("position", ele.getPosition());
            m.put("alarmLevel", ele.getAlarmLevel());
            m.put("alarmEquipmntDetailCode", ele.getAlarmEquipmntDetailCode());
            m.put("beginDt", ele.getBeginDt());
            return m;
        }).collect(Collectors.toList());

        // 最后的插入数据
        List<PmcAlarmEquipmntDetailEntity> resultInsert = new ArrayList<>();
        preInsert.forEach(ele -> {
            Map info = allno.stream().filter(c -> c.get("position").equals(ele.getPosition()) && c.get("alarmLevel") == ele.getAlarmLevel() && c.get("beginDt") == ele.getBeginDt() && c.get("alarmEquipmntDetailCode").equals(ele.getAlarmEquipmntDetailCode())).findFirst().orElse(null);
            if (info != null) {
                UpdateWrapper<PmcAlarmEquipmntDetailEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<PmcAlarmEquipmntDetailEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.eq(PmcAlarmEquipmntDetailEntity::getId, info.get("id"));
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getEndDt, ele.getEndDt());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getIsFill, ele.getIsFill());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCode, ele.getStopCode());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopName, ele.getStopName());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCodeDesc, ele.getStopCodeDesc());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopType, ele.getStopType());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopCauseType, ele.getStopCauseType());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getStopDepartment, ele.getStopDepartment());
                lambdaUpdateWrapper.set(PmcAlarmEquipmntDetailEntity::getDuration, ele.getDuration());
                pmcAlarmEquipmntDetailService.update(updateWrapper);
            } else {
                resultInsert.add(ele);
            }

        });
        pmcAlarmEquipmntDetailService.insertBatch(resultInsert);
        /* 5.更新状态*/
        if (sourceData.size() > 0) {
            UpdateWrapper<PmcAlarmComponentAlarmEntity> updateWrapper = new UpdateWrapper<>();
            LambdaUpdateWrapper<PmcAlarmComponentAlarmEntity> lambdaUpdateWrapper = updateWrapper.lambda();
            lambdaUpdateWrapper.in(PmcAlarmComponentAlarmEntity::getId, sourceData.stream().map(PmcAlarmComponentAlarmEntity::getId).collect(Collectors.toList()));
            lambdaUpdateWrapper.set(PmcAlarmComponentAlarmEntity::getIsDataDown, true);
            super.update(updateWrapper);
        }

        // 更新andon数据选取配置
        sysConfigurationProvider.updateBycategory("PushDetail", "OeeConfig", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));

    }

    /**
     * 获取position中的线体代码
     *
     * @param
     * @return
     */
    private List<String> postionGetValue(String position) {
        List<String> result = new ArrayList<String>();
        String str = "=";
        String strTwo = "++";
        String strThree = "+";
        if (position.contains(str)) {
            String temp = position.split("=")[1];
            if (temp.contains(strTwo)) {
                result.add(temp.split("\\+\\+")[0]);
                temp = temp.split("\\+\\+")[1];
                if (temp.contains(strThree)) {
                    result.add(temp.split("\\+")[0]);
                    result.add(temp.split("\\+")[1]);
                } else {
                    result.add(temp);
                    result.add("");
                }
            } else {
                result.addAll(Arrays.asList(temp));
            }
        } else {
            result.addAll(new ArrayList<>());
        }
        return result;
    }


    @Override
    public void data2() {
        List<PmWorkShopEntity> shops = pmVersionProvider.getObjectedPm().getShops();
        List<PmLineEntity> lines = pmVersionProvider.getObjectedPm().getLines();
        List<PmcAlarmAreaStopSetEntity> mapData = pmcAlarmAreaStopSetService.getData(null).stream().map(w -> {
            PmcAlarmAreaStopSetEntity entity = new PmcAlarmAreaStopSetEntity();
            entity.setId(w.getId());
            entity.setPositions(w.getPositions());
            entity.setAlarmAreaStopSetName(w.getAlarmAreaStopSetName());
            entity.setAlarmAreaStopSetDescription(w.getAlarmAreaStopSetDescription());
            entity.setLineCode(w.getLineCode());
            return entity;
        }).collect(Collectors.toList());

        // 通过positions先查询最后一次Reason数据，按照开始时间再去查Detail数据，重新并集计算，再插入或更新Reason数据（startTime相等更新，其他插入）
        mapData.forEach(item -> {
            PmLineEntity lineInfo = lines.stream().filter(c -> c.getLineCode().equals(item.getLineCode())).findFirst().orElse(null);
            String shopCode;
            if (lineInfo != null) {
                PmWorkShopEntity shopEntity = shops.stream().filter(c -> c.getId().equals(lineInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
                if (shopEntity != null && !shopEntity.getWorkshopCode().isEmpty()) {
                    shopCode = shopEntity.getWorkshopCode();
                } else {
                    shopCode = StringUtils.EMPTY;
                }
            } else {
                shopCode = StringUtils.EMPTY;
            }


            String positions = item.getPositions();
            PmcAlarmAreaStopRecordReasonEntity lastData = pmcAlarmAreaStopRecordReasonService.getData(null).stream().filter(ele -> ele.getPositions().equals(positions)).sorted(Comparator.comparing(PmcAlarmAreaStopRecordReasonEntity::getStartDt)).map(ele -> {
                PmcAlarmAreaStopRecordReasonEntity reasonEntity = new PmcAlarmAreaStopRecordReasonEntity();
                reasonEntity.setId(ele.getId());
                reasonEntity.setStartDt(ele.getStartDt());
                return reasonEntity;
            }).findFirst().orElse(null);

            // 保证初始数据
            Date st;
            if (lastData != null) {
                st = lastData.getStartDt();
            } else {
                st = new Date(0);
            }

            // 要取并集计算的Deatil数据, 防止 =F和=FDI都满足=F%的情况，再通过"++"截取等于"=F"这样
            List<AtomicTime> detailData = pmcAlarmEquipmntDetailService.getData(null).stream().filter(ele -> ele.getPosition().startsWith(positions) && ele.getBeginDt().getTime() >= st.getTime()).collect(Collectors.toList()).
                    stream().sorted(Comparator.comparing(PmcAlarmEquipmntDetailEntity::getBeginDt)).map(ele -> {
                        PmcAlarmEquipmntDetailEntity detailEntity = new PmcAlarmEquipmntDetailEntity();
                        detailEntity.setPosition(ele.getPosition());
                        detailEntity.setBeginDt(ele.getBeginDt());
                        detailEntity.setEndDt(ele.getEndDt());
                        return detailEntity;
                    }).collect(Collectors.toList()).stream().filter(ele -> ele.getPosition().split("\\+\\+")[0].equals(positions)).sorted(Comparator.comparing(PmcAlarmEquipmntDetailEntity::getBeginDt)).map(ele -> {
                        AtomicTime atomicTime = new AtomicTime();
                        atomicTime.setStartTime(ele.getBeginDt());
                        atomicTime.setEndTime(ele.getEndDt());
                        return atomicTime;
                    }).collect(Collectors.toList());

            // 并集计算之后的数据
            List<PmcAlarmAreaStopRecordReasonEntity> result = unionCalculation(detailData).stream().map(ele -> {
                PmcAlarmAreaStopRecordReasonEntity reasonEntity = new PmcAlarmAreaStopRecordReasonEntity();
                reasonEntity.setStatus(0);
                reasonEntity.setShopCode(shopCode);
                reasonEntity.setPositions(positions);
                reasonEntity.setStartDt(ele.getStartTime());
                reasonEntity.setEndDt(ele.getEndTime());
                reasonEntity.setDuration(ele.getEndTime() == null ? 0 : pmShcCalendarProvider.calculateWorkTime(DateUtils.format(ele.getStartTime(), DateUtils.DATE_TIME_PATTERN), DateUtils.format(ele.getEndTime(), DateUtils.DATE_TIME_PATTERN), item.getLineCode()));
                reasonEntity.setPrcPmcAlarmAreaStopSetId(item.getId());
                reasonEntity.setAlarmAreaStopSetName(item.getAlarmAreaStopSetName());
                return reasonEntity;
            }).collect(Collectors.toList());

            //lastData是更新，其他的插入
            List<PmcAlarmAreaStopRecordReasonEntity> insertData = new ArrayList<>();
            if (lastData != null) {
                Date finalSt = st;
                Date et = result.stream().filter(ele -> ele.getPositions().equals(positions) && ele.getStartDt() == finalSt).map(ele -> ele.getEndDt()).findFirst().orElse(null);
                UpdateWrapper<PmcAlarmAreaStopRecordReasonEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<PmcAlarmAreaStopRecordReasonEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.eq(PmcAlarmAreaStopRecordReasonEntity::getId, lastData.getId());
                lambdaUpdateWrapper.set(PmcAlarmAreaStopRecordReasonEntity::getEndDt, et);
                pmcAlarmAreaStopRecordReasonService.update(updateWrapper);
                Date finalSt1 = st;
                insertData.addAll(result.stream().filter(ele -> positions.equals(ele.getPositions()) && ele.getStartDt() != finalSt1).collect(Collectors.toList()));
            }
            if (lastData == null) {// 首次是全部插入
                insertData.addAll(result);
            }
            pmcAlarmAreaStopRecordReasonService.insertBatch(insertData);
            pmcAlarmAreaStopRecordReasonService.saveChange();

        });
    }

    /**
     * 时间并集计算 (支持结束时间为null)
     * 将一批时间转换为不重叠的时间
     *
     * @param times 一批时间
     * @return
     */
    private List<AtomicTime> unionCalculation(List<AtomicTime> times) {
        // 过滤一下脏数据
        times = times.stream()
                .filter(ele -> ele.getEndTime() == null || ele.getStartTime().getTime() <= ele.getEndTime().getTime())
                .sorted(Comparator.comparing(AtomicTime::getStartTime))
                .collect(Collectors.toList());
        // 临时数据
        AtomicTime temp = new AtomicTime();
        // 结果数据
        List<AtomicTime> result = new ArrayList<>();
        for (int i = 0; i < times.size(); i++) {
            if (i == 0) {
                temp.setStartTime(times.get(i).getStartTime());
                temp.setEndTime(times.get(i).getEndTime());
            } else {
                if (temp.getEndTime() == null) {
                    result.add(new AtomicTime(temp.getStartTime(), temp.getEndTime()));
                    break;
                }

                if (times.get(i).getStartTime().getTime() <= temp.getEndTime().getTime()) {
                    Date endTime = null;
                    if (times.get(i).getEndTime() != null) {
                        boolean before = times.get(i).getEndTime().after(temp.getEndTime());
                        if (before) {
                            endTime = times.get(i).getEndTime();
                        } else {
                            endTime = temp.getEndTime();
                        }
                    }
                    temp.setEndTime(endTime);

                } else {
                    result.add(new AtomicTime(temp.getStartTime(), temp.getEndTime()));
                    temp.setStartTime(times.get(i).getStartTime());
                    temp.setEndTime(times.get(i).getEndTime());
                }
            }

            if (i == times.size() - 1) {
                result.add(new AtomicTime(temp.getStartTime(), temp.getEndTime()));
            }
        }

        // 对结果进行排序
        Collections.sort(result, Comparator.comparing(AtomicTime::getStartTime));
        return result;
    }


    @Override
    public void autoDealModel() {
        List<PmcAlarmEquipmntModelEntity> result = new ArrayList<PmcAlarmEquipmntModelEntity>();
        List<ConditionDto> conditionDtos = new ArrayList<>();
        conditionDtos.add(new ConditionDto("isDataDown", "0", ConditionOper.Equal));
        conditionDtos.add(new ConditionDto("alarmEquipmntDetailType", "1", ConditionOper.Equal));
        List<PmcAlarmEquipmntDetailEntity> data = pmcAlarmEquipmntDetailService.getData(conditionDtos).stream().map(ele -> {
            PmcAlarmEquipmntDetailEntity modelEntity = new PmcAlarmEquipmntDetailEntity();
            modelEntity.setPosition(ele.getPosition());
            modelEntity.setId(ele.getId());
            return modelEntity;
        }).limit(2000).collect(Collectors.toList());
        if (data.size() > 0) {
            //工厂建模数据
            List<Map<String, String>> areaShopIdMap = pmVersionProvider.getObjectedPm().getLines().stream().map(ele -> {
                Map<String, String> map = new HashMap<>(5);
                map.put("areaCode", ele.getLineCode());
                map.put("prcPmWorkshopId", ele.getPrcPmWorkshopId().toString());
                return map;
            }).collect(Collectors.toList());

            List<Map<String, String>> shopIds = pmVersionProvider.getObjectedPm().getShops().stream().map(ele -> {
                Map<String, String> map = new HashMap<>(5);
                map.put("id", ele.getId().toString());
                map.put("shopCode", ele.getWorkshopCode());
                map.put("shopName", ele.getWorkshopName());
                return map;
            }).collect(Collectors.toList());

            List<Map<String, String>> areaShopMap = areaShopIdMap.stream().map(ele -> {
                Map<String, String> map = new HashMap<>(5);
                map.put("areaCode", ele.get("areaCode"));
                map.put("shopCode", shopIds.stream().filter(item -> item.get("id").equals(ele.get("prcPmWorkshopId"))).map(item -> item.get("shopCode")).findFirst().orElse(""));
                map.put("shopName", shopIds.stream().filter(item -> item.get("id").equals(ele.get("prcPmWorkshopId"))).map(item -> item.get("shopName")).findFirst().orElse(""));
                return map;
            }).collect(Collectors.toList());

            // 数据去重
            List<String> positionDatas = data.stream().map(ele -> ele.getPosition()).distinct().collect(Collectors.toList());
            List<PmcAlarmEquipmntModelEntity> preResult = new ArrayList<>();

            positionDatas.forEach(position -> {
                List<String> positionList = postionGetValue(position);
                String areaCode = positionList.get(0);
                String shopCode = areaShopMap.stream().filter(ele -> ele.get("areaCode").equals(areaCode)).map(ele -> ele.get("shopCode")).findFirst().orElse("");
                String shopName = areaShopMap.stream().filter(ele -> ele.get("areaCode").equals(areaCode)).map(ele -> ele.get("shopName")).findFirst().orElse("");
                // 第一级数据
                PmcAlarmEquipmntModelEntity equipmntModelEntity = new PmcAlarmEquipmntModelEntity();
                equipmntModelEntity.setAlarmEquipmntModelCode(positionList.get(0));
                equipmntModelEntity.setAlarmEquipmntModelName(positionList.get(0));
                equipmntModelEntity.setWorkshopCode(shopCode);
                equipmntModelEntity.setWorkshopName(shopName);
                equipmntModelEntity.setLevel(1);
                equipmntModelEntity.setPosition("=" + positionList.get(0));
                preResult.add(equipmntModelEntity);

                // 第二级数据
                if (!Strings.isNullOrEmpty(positionList.get(1))) {
                    PmcAlarmEquipmntModelEntity entity = new PmcAlarmEquipmntModelEntity();
                    entity.setAlarmEquipmntModelCode(positionList.get(1));
                    entity.setAlarmEquipmntModelName(positionList.get(1));
                    entity.setWorkshopCode(shopCode);
                    entity.setWorkshopName(shopName);
                    entity.setLevel(2);
                    entity.setPosition("=" + positionList.get(0) + "\\+\\+" + positionList.get(1));
                    preResult.add(entity);
                }

                // 第三级数据
                if (!Strings.isNullOrEmpty(positionList.get(2))) {
                    PmcAlarmEquipmntModelEntity entity = new PmcAlarmEquipmntModelEntity();
                    entity.setAlarmEquipmntModelCode(positionList.get(2));
                    entity.setAlarmEquipmntModelName(positionList.get(2));
                    entity.setWorkshopCode(shopCode);
                    entity.setWorkshopName(shopName);
                    entity.setLevel(3);
                    entity.setPosition("=" + positionList.get(0) + "\\+\\+" + positionList.get(1) + "+" + positionList.get(2));
                    preResult.add(entity);
                }

            });

            List<String> positions = preResult.stream().map(ele -> ele.getPosition()).distinct().collect(Collectors.toList());
            List<String> havePositions = pmcAlarmEquipmntModelService.getData(null).stream().map(ele -> ele.getPosition()).distinct().collect(Collectors.toList());
            // 需要插入的posiiton
            List<String> insertPositions = positions.stream().filter(item -> !havePositions.contains(item)).collect(Collectors.toList());
            insertPositions.forEach(position -> {
                PmcAlarmEquipmntModelEntity a = preResult.stream().filter(item -> item.getPosition().equals(position)).findFirst().orElse(null);
                if (a != null) {
                    result.add(a);
                }
            });
            List<Long> ids = data.stream().map(item -> item.getId()).collect(Collectors.toList());
            UpdateWrapper<PmcAlarmEquipmntDetailEntity> updateWrapper2 = new UpdateWrapper<>();
            LambdaUpdateWrapper<PmcAlarmEquipmntDetailEntity> lambdaUpdateWrapper2 = updateWrapper2.lambda();
            lambdaUpdateWrapper2.in(PmcAlarmEquipmntDetailEntity::getId, ids);
            lambdaUpdateWrapper2.set(PmcAlarmEquipmntDetailEntity::getIsDataDown, true);
            pmcAlarmEquipmntDetailService.update(updateWrapper2);

        }
        pmcAlarmEquipmntModelService.insertBatch(result);
    }

    /**
     * IOT 设备报警处理
     *
     * @param jsonString
     */
    public void analysisIotData(String jsonString) {
        if (StringUtils.isBlank(jsonString)) {
            return;
        }
        BaseIotDataInfo context = JsonUtils.parseObject(jsonString, BaseIotDataInfo.class);
        if (context == null || StringUtils.isBlank(context.getRawdata())) {
            return;
        }
        String rawdata = context.getRawdata();
        if (org.apache.commons.lang3.StringUtils.isBlank(rawdata)) {
            return;
        }
        if (StringUtils.isBlank(context.getProduct_key()) || StringUtils.isBlank(context.getDevice_key())) {
            return;
        }
        BaseIotItemsDataInfo baseIotItemsDataInfo = JsonUtils.parseObject(rawdata, BaseIotItemsDataInfo.class);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String paramsStr = baseIotItemsDataInfo.getParams();
            Map<String, Object> mapValue = objectMapper.readValue(paramsStr, new TypeReference<Map<String, Object>>() {
            });

            for (Map.Entry entry : mapValue.entrySet()) {
                String alarmCode = entry.getKey().toString();
                IotItemsInfo iotItemsInfo = JsonUtils.parseObject(entry.getValue().toString(), IotItemsInfo.class);
                if (iotItemsInfo == null) {
                    continue;
                }
                int status = -1;
                if (iotItemsInfo.getValue().equals("true")) {
                    status = 1;
                } else if (iotItemsInfo.getValue().equals("false")) {
                    status = 2;
                }
                // 非正常的报警数据 丢弃
                if (status == -1) {
                    continue;
                }
                List<IotModelPropertiesInfo> propertiesList = getObjectedMode(context.getProduct_key());
                PmEquipmentEntity pmEquipmentEntity = getEquipmentEntityByCode(context.getDevice_key());
                Date date = new Date(iotItemsInfo.getTs());
                PmcAlarmComponentAlarmEntity alarm = new PmcAlarmComponentAlarmEntity();
                alarm.setAlarmComponentAlarmCode(alarmCode);
                alarm.setWinccCounter(1L);
                String describe = org.apache.commons.lang3.StringUtils.EMPTY;
                IotModelPropertiesInfo iotModelPropertiesInfo = propertiesList.stream().filter(s -> s.getKey().equals(alarmCode)).findFirst().orElse(null);
                if (iotModelPropertiesInfo != null) {
                    describe = iotModelPropertiesInfo.getName();
                }
                alarm.setPosition(getPosition(pmEquipmentEntity, alarmCode, context.getDevice_key(), describe));
                alarm.setAlarmComponentAlarmDescription(describe);
                alarm.setAlarmLevel(1);
                alarm.setAlarmDt(date);
                this.save(alarm);
                this.saveChange();
            }
        } catch (Exception ex) {
            log.error("设备报警异常,错误信息：" + ex.getMessage());
        }
    }

    private String getPosition(PmEquipmentEntity pmEquipmentEntity, String alarmCode, String deviceKey, String describe) {
        if (pmEquipmentEntity == null) {
            return "";
        }
        //点位示例：=MB++MB250+GB01-SC;AAAA;1
        //=区域++工位+设备编码;报警描述代码;报警等级;报警描述
        String lineCode = pmEquipmentEntity.getLineCode();
        String workStationCode = pmEquipmentEntity.getWorkstationCode();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(lineCode);
        stringBuilder.append("++");
        stringBuilder.append(workStationCode);
        stringBuilder.append("+");
        stringBuilder.append(deviceKey);
        stringBuilder.append(";");
        stringBuilder.append(alarmCode);
        stringBuilder.append(";");
        stringBuilder.append("1");
        stringBuilder.append(";");
        stringBuilder.append(describe);
        return stringBuilder.toString();
    }

    /**
     * 根据设备编码获取设备信息
     *
     * @param deviceKey
     * @return
     */
    private PmEquipmentEntity getEquipmentEntityByCode(String deviceKey) {
        return pmEquipmentProvider.getPmEquipmentEntityByCode(deviceKey);
    }

    public List<IotModelPropertiesInfo> getObjectedMode(String productKey) {
        try {
            List<IotModelPropertiesInfo> data = new ArrayList<>();
            data = localCache.getObject(PMC_ALL_CACHE_NAME_DESCRIBE + "_" + productKey);
            if (data != null) {
                return data;
            }
            synchronized (LOCK_OBJ) {
                data = localCache.getObject(PMC_ALL_CACHE_NAME_DESCRIBE + "_" + productKey);
                if (data != null) {
                    return data;
                }
                IotModelBaseInfo iotModelBaseInfo = getDescribeInfo(productKey);
                if (iotModelBaseInfo != null && iotModelBaseInfo.getData() != null) {
                    IotModelDataInfo iotModelDataInfo = iotModelBaseInfo.getData();
                    if (iotModelDataInfo != null && iotModelDataInfo.getThingDefinitionJson() != null) {
                        IotModelDefinitionInfo info = iotModelDataInfo.getThingDefinitionJson();
                        if (info != null) {
                            List<IotModelPropertiesInfo> propertiesList = info.getProperties();
                            data.addAll(propertiesList);
                        }
                    }
                }
                if (data != null) {
                    localCache.addObject(PMC_ALL_CACHE_NAME_DESCRIBE + "_" + productKey, data, -1);
                }
            }
            return data;
        } catch (Exception ex) {
            throw new InkelinkException(ex.getMessage());
        }
    }

    /**
     * 根据产品编码获取物模型
     *
     * @param productKey 产品编码
     * @return 加密后的数据
     */
    private IotModelBaseInfo getDescribeInfo(String productKey) {
        if (StringUtils.isBlank(productKey)) {
            return null;
        }
        String apiPath = sysConfigurationProvider.getConfiguration("iotmodel_queue", "midapi");
        if (StringUtils.isBlank(apiPath)) {
            return null;
        }
        String appId = iotConsumerConfiguration.getAppId();
        String appsecret = iotConsumerConfiguration.getAppsecret();
        DefinitionInfo info = new DefinitionInfo();
        Map<String, Object> makesInfo = makeToken(appId, appsecret);
        if (makesInfo == null || makesInfo.size() == 0) {
            throw new InkelinkException("设备报警SM3加密失败，产品编码:" + productKey);
        }
        info.setApp_id(appId);
        info.setTimestamp(makesInfo.getOrDefault("timestamp", "").toString());
        info.setTrans_id(makesInfo.getOrDefault("makesInfo", "").toString());
        info.setToken(makesInfo.getOrDefault("token", "").toString());
        info.setRequestId("");
        DefinitionItems items = new DefinitionItems();
        items.setProductKey(productKey);
        info.setData(items);
        try {
            String ars = apiPtService.postapi(apiPath, info, null);
            IotModelBaseInfo resultVo = JSONObject.parseObject(ars, IotModelBaseInfo.class);
            if (resultVo != null) {
                if ("000000".equals(resultVo.getCode())) {
                    return resultVo;
                } else {
                    throw new InkelinkException("调用IOT物模型接口异常,message：" + resultVo.getMessage());
                }
            }
        } catch (Exception ex) {
            throw new InkelinkException("调用IOT物模型接口异常,错误信息:" + ex.getMessage());
        }
        return null;
    }

    private Map<String, Object> makeToken(String appId, String appsecret) {
        Map<String, Object> result = new HashMap<>();
        String token = org.apache.commons.lang3.StringUtils.EMPTY;
        String timestamp = DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN_M);
        String transId = org.apache.commons.lang3.StringUtils.EMPTY;
        transId = timestamp.replace("-", "").replace(" ", "").replace(":", "");
        Random random = new Random();
        transId = transId + random.nextInt(999999) + 1;
        StringBuilder sb = new StringBuilder();
        sb.append("app_id");
        sb.append(appId);
        sb.append("timestamp");
        sb.append(timestamp);
        sb.append("trans_id");
        sb.append(transId);
        sb.append(appsecret);
        token = SmUtil.sm3(sb.toString());
        result.put("token", token);
        result.put("timestamp", timestamp);
        result.put("trans_id", transId);
        return result;
    }


}