package com.ca.mfd.prc.bdc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteHoldEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteHoldLogEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePointEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRoutePointLogEntity;
import com.ca.mfd.prc.bdc.mapper.IRcBdcRouteHoldMapper;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteAreaService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteHoldLogService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteHoldService;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePointLogService;
import com.ca.mfd.prc.bdc.service.IRcBdcRoutePointService;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
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
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@Service
public class RcBdcRouteHoldServiceImpl extends AbstractCrudServiceImpl<IRcBdcRouteHoldMapper, RcBdcRouteHoldEntity> implements IRcBdcRouteHoldService {

    @Autowired
    IRcBdcRouteAreaService rcRouteAreaService;

    @Autowired
    IRcBdcRoutePointService rcRoutePointService;

    @Autowired
    IRcBdcRoutePointLogService rcRoutePointLogService;

    @Autowired
    IRcBdcRouteHoldLogService rcRouteHoldLogService;

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
        RcBdcRouteAreaEntity area = rcRouteAreaService.get(areaId);
        if (area == null) {
            throw new InkelinkException("传入区域不存在");
        }
        for (String tpsCode : tpsCodes) {
            RcBdcRouteHoldEntity holdDb = getRouteHoldEntityById(areaId, tpsCode);
            if (holdDb != null) {
                throw new InkelinkException("车辆" + holdDb.getAreaCode() + "在" + holdDb.getAreaName() + "已经暂存");
            }
            RcBdcRouteHoldEntity item = new RcBdcRouteHoldEntity();
            item.setPrcBdcRouteAreaId(area.getId());
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

    private RcBdcRouteHoldEntity getRouteHoldEntityById(String areaId, String tpsCode) {
        QueryWrapper<RcBdcRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getRcRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBdcRouteHoldEntity::getPrcBdcRouteAreaId, areaId);
        //lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getTpsCode, tpsCode);
        lambdaQueryWrapper.eq(RcBdcRouteHoldEntity::getSn, tpsCode);
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
        List<RcBdcRouteHoldEntity> holdList = getHoldListByIds(ids);
        //List<String> strList = holdList.stream().map(RcBsRouteHoldEntity::getRcRouteAreaId).distinct().collect(Collectors.toList());
        List<Long> strList = holdList.stream().map(RcBdcRouteHoldEntity::getPrcBdcRouteAreaId).distinct().collect(Collectors.toList());
        Long[] areaIds = strList.toArray(new Long[strList.size()]);
        List<RcBdcRouteAreaEntity> areas = rcRouteAreaService.getAreaEntityByIds(areaIds);
        for (Long areaId : areaIds) {
            List<RcBdcRouteHoldEntity> areaHoldList = holdList.stream().filter(w -> Objects.equals(w.getPrcBdcRouteAreaId(), areaId))
                    .collect(Collectors.toList());
            RcBdcRouteAreaEntity area = areas.stream().filter(w -> Objects.equals(w.getId(), areaId)).findFirst().orElse(null);
            for (RcBdcRouteHoldEntity holdDb : areaHoldList) {
                //日志
                RcBdcRouteHoldLogEntity log = new RcBdcRouteHoldLogEntity();
                log.setPrcBdcRouteAreaId(holdDb.getPrcBdcRouteAreaId());
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
                UpdateWrapper<RcBdcRouteHoldEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<RcBdcRouteHoldEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.eq(RcBdcRouteHoldEntity::getId, holdDb.getId());
                delete(updateWrapper, false);
                addRcRoutePointLogEntity(areaId, area, holdDb.getSn(), 2);
            }
        }
    }

    public List<RcBdcRouteHoldEntity> getHoldListByIds(Long[] ids) {
        QueryWrapper<RcBdcRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBdcRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcBdcRouteHoldEntity::getId, Arrays.asList(ids));
        return selectList(queryWrapper);
    }

    private void addRcRoutePointLogEntity(Long areaId, RcBdcRouteAreaEntity area, String tpsCode, int type) {
        List<RcBdcRoutePointEntity> points = rcRoutePointService.getEntityByAreaId(areaId);
        List<RcBdcRoutePointLogEntity> logEntities = new ArrayList<>();
        for (RcBdcRoutePointEntity point : points) {
            RcBdcRoutePointLogEntity log = new RcBdcRoutePointLogEntity();
            log.setPrcBdcRouteAreaId(area.getId());
            log.setBufferCode(area.getBufferCode());
            log.setAreaCode(area.getAreaCode());
            log.setAreaName(area.getAreaName());
            log.setPrcBdcRoutePointId(point.getId());
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