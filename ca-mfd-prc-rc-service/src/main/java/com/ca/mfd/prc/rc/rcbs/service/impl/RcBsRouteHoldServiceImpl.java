package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteHoldEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePointEntity;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteHoldMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteHoldLogService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteHoldService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePointLogService;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRoutePointService;
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
public class RcBsRouteHoldServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteHoldMapper, RcBsRouteHoldEntity> implements IRcBsRouteHoldService {

    @Autowired
    IRcBsRouteAreaService rcRouteAreaService;

    @Autowired
    IRcBsRoutePointService rcRoutePointService;

    @Autowired
    IRcBsRoutePointLogService rcRoutePointLogService;

    @Autowired
    IRcBsRouteHoldLogService rcRouteHoldLogService;

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
        RcBsRouteAreaEntity area = rcRouteAreaService.get(areaId);
        if (area == null) {
            throw new InkelinkException("传入区域不存在");
        }
        for (String tpsCode : tpsCodes) {
            RcBsRouteHoldEntity holdDb = getRouteHoldEntityById(areaId, tpsCode);
            if (holdDb != null) {
                throw new InkelinkException("车辆" + holdDb.getAreaCode() + "在" + holdDb.getAreaName() + "已经暂存");
            }
            RcBsRouteHoldEntity item = new RcBsRouteHoldEntity();
            item.setPrcRcBsRouteAreaId(area.getId());
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


    private RcBsRouteHoldEntity getRouteHoldEntityById(String areaId, String tpsCode) {
        QueryWrapper<RcBsRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteHoldEntity::getSn, tpsCode);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 解除暂存车辆-后台
     *
     * @param ids    主键集合
     * @param reason 原因
     */
    @Override
    public void removeHoldList(String[] ids, String reason) {
        List<RcBsRouteHoldEntity> holdList = getHoldListByIds(ids);
        //List<String> strList = holdList.stream().map(RcBsRouteHoldEntity::getRcRouteAreaId).distinct().collect(Collectors.toList());
        List<Long> strList = holdList.stream().map(RcBsRouteHoldEntity::getPrcRcBsRouteAreaId).distinct().collect(Collectors.toList());
        Long[] areaIds = strList.toArray(new Long[strList.size()]);
        List<RcBsRouteAreaEntity> areas = rcRouteAreaService.getAreaEntityByIds(areaIds);
        for (Long areaId : areaIds) {
            List<RcBsRouteHoldEntity> areaHoldList = holdList.stream().filter(w -> Objects.equals(w.getPrcRcBsRouteAreaId(), areaId))
                    .collect(Collectors.toList());
            RcBsRouteAreaEntity area = areas.stream().filter(w -> Objects.equals(w.getId(), areaId)).findFirst().orElse(null);
            for (RcBsRouteHoldEntity holdDb : areaHoldList) {
                //日志
                RcBsRouteHoldLogEntity log = new RcBsRouteHoldLogEntity();
                log.setPrcRcBsRouteAreaId(holdDb.getPrcRcBsRouteAreaId());
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
                UpdateWrapper<RcBsRouteHoldEntity> updateWrapper = new UpdateWrapper<>();
                LambdaUpdateWrapper<RcBsRouteHoldEntity> lambdaUpdateWrapper = updateWrapper.lambda();
                lambdaUpdateWrapper.eq(RcBsRouteHoldEntity::getId, holdDb.getId());
                delete(updateWrapper, false);
                addRcRoutePointLogEntity(areaId, area, holdDb.getSn(), 2);
            }
        }
    }

    public List<RcBsRouteHoldEntity> getHoldListByIds(String[] ids) {
        QueryWrapper<RcBsRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcBsRouteHoldEntity::getId, Arrays.asList(ids));
        return selectList(queryWrapper);
    }

    private void addRcRoutePointLogEntity(Long areaId, RcBsRouteAreaEntity area, String tpsCode, int type) {
        List<RcBsRoutePointEntity> points = rcRoutePointService.getEntityByAreaId(areaId);
        List<RcBsRoutePointLogEntity> logEntities = new ArrayList<>();
        for (RcBsRoutePointEntity point : points) {
            RcBsRoutePointLogEntity log = new RcBsRoutePointLogEntity();
            log.setPrcRcBsRoutePointId(point.getId());
            log.setPointCode(point.getPointCode());
            log.setPointName(point.getPointName());
            log.setPrcRcBsRouteAreaId(area.getId());
            log.setAreaCode(area.getAreaCode());
            log.setBufferCode(area.getBufferCode());
            log.setAreaName(area.getAreaName());
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