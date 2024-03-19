package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmBopMapper;
import com.ca.mfd.prc.pm.mapper.IPmControlPlanMapper;
import com.ca.mfd.prc.pm.service.IPmBopService;
import com.ca.mfd.prc.pm.service.IPmControlPlanService;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 控制计划服务实现
 * @author inkelink
 * @date 2023年11月22日
 * @变更说明 BY inkelink At 2023年11月22日
 */
@Service
public class PmControlPlanServiceImpl extends AbstractCrudServiceImpl<IPmControlPlanMapper, PmControlPlanEntity> implements IPmControlPlanService {

    @Autowired
    private IPmWorkShopService pmWorkShopService;
    @Autowired
    private IPmLineService pmLineService;
    @Autowired
    private IPmWorkStationService pmWorkStationService;
    @Autowired
    private IPmBopMapper pmBopMapper;

    @Override
    public void beforeUpdate(PmControlPlanEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeInsert(PmControlPlanEntity entity) {
        valid(entity);
    }

    private void valid(PmControlPlanEntity entity) {
        //BOP信息
        valiBop(entity);
        //工位信息
        validWorkStation(entity);
        //线体信息
        validLine(entity);
        //车间信息
        validWorkShop(entity);

    }

    private void valiBop(PmControlPlanEntity entity) {
        //工位信息
        PmBopEntity pmBopEntity = pmBopMapper.selectById(entity.getPrcPmBopId());
        if (pmBopEntity == null) {
            throw new InkelinkException(String.format("bopID[%s]不存在",entity.getPrcPmBopId()));
        }
        entity.setPrcPmWorkstationId(pmBopEntity.getPrcPmWorkstationId());
        entity.setFrockCode(pmBopEntity.getProcessStep());
    }

    private void validWorkStation(PmControlPlanEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = pmWorkStationService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException("控制计划对应的工位不存在");
        }
        entity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
        entity.setPrcPmLineId(pmWorkStationEntity.getPrcPmLineId());
    }

    private void validLine(PmControlPlanEntity entity) {
        //线体信息
        QueryWrapper<PmLineEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmLineEntity> lwq = qw.lambda();
        lwq.eq(PmLineEntity::getId, entity.getPrcPmLineId());
        lwq.eq(PmLineEntity::getVersion, 0);
        PmLineEntity pmLineEntity = pmLineService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmLineEntity == null) {
            throw new InkelinkException("控制计划对应的线体不存在");
        }
        entity.setLineCode(pmLineEntity.getLineCode());
        entity.setPrcPmWorkshopId(pmLineEntity.getPrcPmWorkshopId());
    }

    private void validWorkShop(PmControlPlanEntity entity) {
        //车间信息
        QueryWrapper<PmWorkShopEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkShopEntity> lwq = qw.lambda();
        lwq.eq(PmWorkShopEntity::getId, entity.getPrcPmWorkshopId());
        lwq.eq(PmWorkShopEntity::getVersion, 0);
        PmWorkShopEntity pmWorkShopEntity = pmWorkShopService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException("控制计划对应的车间不存在");
        }
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
    }

    @Override
    public List<PmControlPlanEntity> getByShopId(Long shopId) {
        return null;
    }

    @Override
    public Map<String, String> getExcelHead() {
        return null;
    }

    @Override
    public void publishByWorkShopId(Long workShopId, Integer version) {

    }

    @Override
    public PmControlPlanEntity get(Serializable id, Integer version) {
        return null;
    }
}