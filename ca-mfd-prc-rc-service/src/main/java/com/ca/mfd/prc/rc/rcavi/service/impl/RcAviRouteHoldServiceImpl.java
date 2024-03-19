package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteHoldLogEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointLogEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRouteHoldMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteAreaService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteHoldLogService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteHoldService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointLogService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
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
public class RcAviRouteHoldServiceImpl extends AbstractCrudServiceImpl<IRcAviRouteHoldMapper, RcAviRouteHoldEntity> implements IRcAviRouteHoldService {

    @Autowired
    IRcAviRouteAreaService rcRouteAreaService;

    @Autowired
    IRcAviRoutePointService rcRoutePointService;

    @Autowired
    IRcAviRoutePointLogService rcRoutePointLogService;

    @Autowired
    IRcAviRouteHoldLogService rcRouteHoldLogService;

    @Autowired
    IdentityHelper identityHelper;

    /**
     * 暂存车辆-后台
     *
     * @param pointId 路由点ID
     * @param snList  车辆识别码
     * @param reason  原因
     */
    @Override
    public void addHoldList(String pointId, List<String> snList, String reason) {
        RcAviRoutePointEntity point = rcRoutePointService.get(pointId);
        if (point == null) {
            throw new InkelinkException("传入路由点不存在");
        }
        RcAviRouteAreaEntity area = rcRouteAreaService.get(point.getPrcRcAviRouteAreaId());
        if (area == null) {
            throw new InkelinkException("传入区域不存在");
        }
        for (String sn : snList) {
            RcAviRouteHoldEntity holdDb = getRouteHoldEntityById(pointId, sn);
            if (holdDb != null) {
                throw new InkelinkException("车辆" + holdDb.getSn() + "已经暂存");
            }
            RcAviRouteHoldEntity item = new RcAviRouteHoldEntity();
            item.setHoldDt(new Date());
            item.setHoldId(identityHelper.getUserId());
            item.setHoldName(identityHelper.getUserName());
            item.setPrcRcAviRoutePointId(point.getId());
            item.setPointCode(point.getPointCode());
            item.setPointName(point.getPointName());
            item.setPrcRcAviRouteAreaId(point.getPrcRcAviRouteAreaId());
            item.setAreaCode(area.getAreaCode());
            item.setAreaName(area.getAreaName());
            item.setSn(sn);
            item.setHoldReason(reason);
            this.save(item);
            addRcRoutePointLogEntity(point, area, item.getSn(), 1);
        }
    }


    private RcAviRouteHoldEntity getRouteHoldEntityById(String pointId, String sn) {
        QueryWrapper<RcAviRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcAviRouteHoldEntity::getPrcRcAviRoutePointId, pointId);
        lambdaQueryWrapper.eq(RcAviRouteHoldEntity::getSn, sn);
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
        List<RcAviRouteHoldEntity> holdList = getHoldListByIds(ids);
        for (RcAviRouteHoldEntity holdDb : holdList) {
            RcAviRouteHoldLogEntity log = new RcAviRouteHoldLogEntity();
            log.setPrcRcAviRoutePointId(holdDb.getPrcRcAviRoutePointId());
            log.setPointCode(holdDb.getPointCode());
            log.setPointName(holdDb.getPointName());
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
            this.delete(holdDb.getId(), false);

            RcAviRoutePointLogEntity pointLog = new RcAviRoutePointLogEntity();
            pointLog.setPrcRcAviRoutePointId(holdDb.getPrcRcAviRoutePointId());
            pointLog.setPointCode(holdDb.getPointCode());
            pointLog.setPointName(holdDb.getPointName());
            pointLog.setPrcRcAviRouteAreaId(holdDb.getPrcRcAviRouteAreaId());
            pointLog.setAreaCode(holdDb.getAreaCode());
            pointLog.setAreaName(holdDb.getAreaName());
            pointLog.setAttachId(0L);
            pointLog.setAreaName("");
            pointLog.setOperation("用户/“" + identityHelper.getUserName() + "执行命令:" + holdDb.getPointName() + "后台解除暂存车:" + holdDb);
            pointLog.setOperationLever(1);
            pointLog.setOperationName(identityHelper.getUserName());
            pointLog.setOperationDt(new Date());
            rcRoutePointLogService.save(pointLog);
        }
    }

    public List<RcAviRouteHoldEntity> getHoldListByIds(String[] ids) {
        QueryWrapper<RcAviRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(RcAviRouteHoldEntity::getId, Arrays.asList(ids));
        return selectList(queryWrapper);
    }

    private void addRcRoutePointLogEntity(RcAviRoutePointEntity point, RcAviRouteAreaEntity area, String tpsCode, int type) {
        RcAviRoutePointLogEntity log = new RcAviRoutePointLogEntity();
        log.setPrcRcAviRoutePointId(point.getId());
        log.setPointCode(point.getPointCode());
        log.setPointName(point.getPointName());
        log.setPrcRcAviRouteAreaId(point.getPrcRcAviRouteAreaId());
        log.setAreaCode(area.getAreaCode());
        log.setAreaName(area.getAreaName());
        log.setAttachId(0L);
        log.setAttachCode("");
        log.setAttachName("");
        if (type == 1) {
            log.setOperation("用户/“" + identityHelper.getUserName() + "执行命令:" + point.getPointName() + "后台暂存车:" + tpsCode);
        } else {
            log.setOperation("用户/“" + identityHelper.getUserName() + "执行命令:" + point.getPointName() + "后台解除暂存车:" + tpsCode);
        }
        log.setOperationLever(1);
        log.setOperationName(identityHelper.getUserName());
        log.setOperationDt(new Date());
        rcRoutePointLogService.save(log);
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        Map<String, List<Map<String, String>>> stringListMap = datas.stream().collect(Collectors.groupingBy(s -> s.get("AreaCode") + s.get("PointCode") + s.get("Sn")));
        int codes = stringListMap.size();
        if (codes > 0) {
            throw new InkelinkException("数据重复");
        }
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(getExcelColumnNames().get("AreaCode"), i + 1, data.get("AreaCode"), "");
            validExcelDataRequire(getExcelColumnNames().get("PointCode"), i + 1, data.get("PointCode"), "");
            validExcelDataRequire(getExcelColumnNames().get("Sn"), i + 1, data.get("Sn"), "");
        }
    }

    @Override
    public Map<String, String> getExcelColumnNames() {
        Map<String, String> maps = new HashMap<>();
        maps.put("AreaCode", "路由区代码");
        maps.put("PointCode", "路由点代码");
        maps.put("Sn", "车辆识别码");
        maps.put("HoldReason", "操作原因");
        maps.put("IsDelete", "是否删除");
        return maps;
    }

    @Override
    public void saveExcelData(List<RcAviRouteHoldEntity> entities) {
        List<RcAviRoutePointEntity> points = rcRoutePointService.getData(null);
        List<RcAviRouteAreaEntity> areas = rcRouteAreaService.getData(null);
        for (RcAviRouteHoldEntity item : entities) {
            RcAviRouteHoldEntity holdDb = getEntityByCodes(item.getAreaCode(), item.getPointCode(), item.getSn());
            if (item.getIsDelete()) {
                if (holdDb == null) {
                    continue;
                }
                RcAviRouteHoldLogEntity holdLog = new RcAviRouteHoldLogEntity();
                holdLog.setPrcRcAviRoutePointId(holdDb.getPrcRcAviRoutePointId());
                holdLog.setPointCode(holdDb.getPointCode());
                holdLog.setPointName(holdDb.getPointName());
                holdLog.setPrcRcAviRouteAreaId(holdDb.getPrcRcAviRouteAreaId());
                holdLog.setAreaCode(holdDb.getAreaCode());
                holdLog.setAreaName(holdDb.getAreaName());
                holdLog.setSn(holdDb.getSn());
                holdLog.setCancelId(identityHelper.getUserId());
                holdLog.setCancelDt(new Date());
                holdLog.setCancelName(identityHelper.getUserName());
                holdLog.setCancelReason(item.getHoldReason());
                holdLog.setHoldId(holdDb.getHoldId());
                holdLog.setHoldDt(holdDb.getHoldDt());
                holdLog.setHoldName(holdDb.getHoldName());
                holdLog.setHoldReason(holdDb.getHoldReason());
                rcRouteHoldLogService.save(holdLog);
                this.delete(holdDb.getId());

                RcAviRoutePointLogEntity pointLog = new RcAviRoutePointLogEntity();
                pointLog.setPrcRcAviRoutePointId(holdDb.getPrcRcAviRoutePointId());
                pointLog.setPointCode(holdDb.getPointCode());
                pointLog.setPointName(holdDb.getPointName());
                pointLog.setPrcRcAviRouteAreaId(holdDb.getPrcRcAviRouteAreaId());
                pointLog.setAreaCode(holdDb.getAreaCode());
                pointLog.setAreaName(holdDb.getAreaName());
                pointLog.setAttachId(0L);
                pointLog.setAttachCode("");
                pointLog.setAttachName("");
                pointLog.setOperation("用户" + identityHelper.getUserName() + "执行命令:" + holdDb.getPointName() + "后台导入解除暂存车:" + holdDb.getSn());
                pointLog.setOperationLever(1);
                pointLog.setOperationName(identityHelper.getUserName());
                pointLog.setOperationDt(new Date());
                rcRoutePointLogService.save(pointLog);

                continue;
            }

            if (holdDb == null) {
                continue;
            }
            RcAviRoutePointEntity point = points.stream().filter(s -> Objects.equals(s.getPrcRcAviRouteAreaId(), item.getPrcRcAviRouteAreaId())
                    && Objects.equals(s.getId(), item.getPrcRcAviRoutePointId())).findFirst().orElse(null);
            if (point == null) {
                throw new InkelinkException("传入的路由代码异常");
            }
            RcAviRouteAreaEntity area = areas.stream().filter(s -> Objects.equals(s.getId(), point.getPrcRcAviRouteAreaId())).findFirst().orElse(null);
            if (area == null) {
                throw new InkelinkException("路由区代码异常");
            }
            item.setHoldDt(new Date());
            item.setHoldId(identityHelper.getUserId());
            item.setHoldName(identityHelper.getUserName());
            item.setPrcRcAviRoutePointId(point.getId());
            item.setPointCode(point.getPointCode());
            item.setPointName(point.getPointName());
            item.setPrcRcAviRouteAreaId(area.getId());
            item.setAreaCode(area.getAreaCode());
            item.setAreaName(area.getAreaName());
            this.save(item);

            RcAviRoutePointLogEntity log = new RcAviRoutePointLogEntity();
            log.setPrcRcAviRouteAreaId(area.getId());
            log.setAreaCode(area.getAreaCode());
            log.setAreaName(area.getAreaName());
            log.setPrcRcAviRoutePointId(point.getId());
            log.setPointCode(point.getPointCode());
            log.setPointName(point.getPointName());
            log.setAttachId(0L);
            log.setAttachCode("");
            log.setAttachName("");
            log.setOperation("用户" + identityHelper.getUserName() + "执行命令：" + point.getPointName() + "后台导入暂存车：" + item.getSn());
            log.setOperationLever(1);
            log.setOperationName(identityHelper.getUserName());
            log.setOperationDt(new Date());
            rcRoutePointLogService.save(log);
        }
        saveChange();
    }

    private RcAviRouteHoldEntity getEntityByCodes(String areaCode, String pointCode, String sn) {
        QueryWrapper<RcAviRouteHoldEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRouteHoldEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcAviRouteHoldEntity::getAreaCode, areaCode);
        lambdaQueryWrapper.eq(RcAviRouteHoldEntity::getPointCode, pointCode);
        lambdaQueryWrapper.eq(RcAviRouteHoldEntity::getSn, sn);
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }
}