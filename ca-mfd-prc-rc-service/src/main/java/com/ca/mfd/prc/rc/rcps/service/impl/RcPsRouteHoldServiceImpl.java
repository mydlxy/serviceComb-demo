package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteAreaEntity;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteHoldEntity;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePointEntity;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteHoldMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteAreaService;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteHoldLogService;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteHoldService;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePointLogService;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRoutePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由区暂存表服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteHoldServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteHoldMapper, RcPsRouteHoldEntity> implements IRcPsRouteHoldService {

    @Autowired
    IRcPsRouteAreaService rcRouteAreaService;

    @Autowired
    IRcPsRoutePointService rcRoutePointService;

    @Autowired
    IRcPsRoutePointLogService rcRoutePointLogService;

    @Autowired
    IRcPsRouteHoldLogService rcRouteHoldLogService;

    @Autowired
    IdentityHelper identityHelper;

    /**
     * 暂存车辆-后台
     *
     * @param areaId   路由ID
     * @param tpsCodes 车辆识别码
     * @param reason   原因
     */
    @Override
    public void addHoldList(String areaId, List<String> tpsCodes, String reason) {
        RcPsRouteAreaEntity area = rcRouteAreaService.get(areaId);
        if (area == null) {
            throw new InkelinkException("传入区域不存在");
        }
        for (String tpsCode : tpsCodes) {
            RcPsRouteHoldEntity holdDb = getRouteHoldEntityById(areaId, tpsCode);
            if (holdDb != null) {
                throw new InkelinkException("车辆" + holdDb.getAreaCode() + "在" + holdDb.getAreaName() + "已经暂存");
            }
            RcPsRouteHoldEntity item = new RcPsRouteHoldEntity();
            item.setPrcRcPsRouteAreaId(area.getId());
            item.setAreaCode(area.getAreaCode());
            item.setAreaName(area.getAreaName());
            item.setBufferCode(area.getBufferCode());
            item.setSn(tpsCode);
            item.setHoldDt(new Date());
            item.setHoldId(identityHelper.getUserId());
            item.setHoldName(identityHelper.getUserName());
            item.setHoldReason(reason);
            this.save(item);
            addRcRoutePointLogEntity(area.getId(), area, item.getSn(), 1);
        }
    }


    private RcPsRouteHoldEntity getRouteHoldEntityById(String areaId, String tpsCode) {
        QueryWrapper<RcPsRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteHoldEntity::getPrcRcPsRouteAreaId, areaId);
        //lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getTpsCode, tpsCode);
        lambdaQueryWrapper.eq(RcPsRouteHoldEntity::getSn, tpsCode);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 解除暂存车辆-后台
     *
     * @param ids    主键集合
     * @param reason 原因
     */
    @Override
    public void removeHoldList(Long[] ids, String reason) {
        List<RcPsRouteHoldEntity> holdList = getHoldListByIds(ids);
        //List<String> strList = holdList.stream().map(RcBsRouteHoldEntity::getRcRouteAreaId).distinct().collect(Collectors.toList());
        List<Long> strList = holdList.stream().map(RcPsRouteHoldEntity::getPrcRcPsRouteAreaId).distinct().collect(Collectors.toList());
        Long[] areaIds = strList.toArray(new Long[strList.size()]);
        List<RcPsRouteAreaEntity> areas = rcRouteAreaService.getAreaEntityByIds(areaIds);
        for (Long areaId : areaIds) {
            List<RcPsRouteHoldEntity> areaHoldList = holdList.stream().filter(w -> Objects.equals(w.getPrcRcPsRouteAreaId(), areaId))
                    .collect(Collectors.toList());
            RcPsRouteAreaEntity area = areas.stream().filter(w -> Objects.equals(w.getId(), areaId)).findFirst().orElse(null);
            for (RcPsRouteHoldEntity holdDb : areaHoldList) {
                //日志
                RcPsRouteHoldLogEntity log = new RcPsRouteHoldLogEntity();
                log.setPrcRcPsRouteAreaId(holdDb.getPrcRcPsRouteAreaId());
                log.setBufferCode(holdDb.getBufferCode());
                log.setAreaCode(holdDb.getAreaCode());
                log.setAreaName(holdDb.getAreaName());
                log.setSn(holdDb.getSn());
                log.setCancelId(identityHelper.getUserId());
                log.setCancelDt(new Date());
                log.setCancelName(identityHelper.getUserName());
                log.setCancelReason(reason);
                log.setHoldId(holdDb.getHoldId());
                log.setHoldDt(holdDb.getHoldDt());
                log.setHoldName(holdDb.getHoldName());
                log.setHoldReason(holdDb.getHoldReason());
                rcRouteHoldLogService.save(log);

                // 删除
                UpdateWrapper<RcPsRouteHoldEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<RcPsRouteHoldEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.eq(RcPsRouteHoldEntity::getId, holdDb.getId());
                delete(updateWrapper, false);
                addRcRoutePointLogEntity(areaId, area, holdDb.getSn(), 2);
            }
        }
    }

    public List<RcPsRouteHoldEntity> getHoldListByIds(Long[] ids) {
        QueryWrapper<RcPsRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcPsRouteHoldEntity::getId, Arrays.asList(ids));
        return selectList(queryWrapper);
    }

    private void addRcRoutePointLogEntity(Long areaId, RcPsRouteAreaEntity area, String tpsCode, int type) {
        List<RcPsRoutePointEntity> points = rcRoutePointService.getEntityByAreaId(areaId);
        List<RcPsRoutePointLogEntity> logEntities = new ArrayList<>();
        for (RcPsRoutePointEntity point : points) {
            RcPsRoutePointLogEntity log = new RcPsRoutePointLogEntity();
            log.setPrcRcPsRouteAreaId(area.getId());
            log.setBufferCode(area.getBufferCode());
            log.setAreaCode(area.getAreaCode());
            log.setAreaName(area.getAreaName());
            log.setPrcRcPsRoutePointId(point.getId());
            log.setPointCode(point.getPointCode());
            log.setPointName(point.getPointName());
            log.setAttachId(Constant.DEFAULT_ID);
            log.setAttachCode("");
            log.setAttachName("");
            if (type == 1) {
                log.setOperation("用户/“" + identityHelper.getUserName() + "/”执行命令:" + area.getAreaName() + "后台暂存车:" + tpsCode);
            } else {
                log.setOperation("用户/“" + identityHelper.getUserName() + "/”执行命令:" + area.getAreaName() + "后台解除暂存车:" + tpsCode);
            }
            log.setOperationLever(1);
            log.setOperationName(identityHelper.getUserName());
            log.setOperationDt(new Date());
            logEntities.add(log);
        }
        rcRoutePointLogService.insertBatch(logEntities);
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("holdDt") && data.getOrDefault("holdDt", null) != null) {
                data.put("holdDt", DateUtils.format((Date) data.get("holdDt"), DateUtils.DATE_TIME_PATTERN));
            }
        }
    }
}