package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.*;
import com.ca.mfd.prc.pm.mapper.IPmBopMapper;
import com.ca.mfd.prc.pm.mapper.IPmPfmeaMapper;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmPfmeaService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import com.ca.mfd.prc.pm.service.IPmWorkStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *
 * @Description: PFMEA配置服务实现
 * @author inkelink
 * @date 2023年11月22日
 * @变更说明 BY inkelink At 2023年11月22日
 */
@Service
public class PmPfmeaServiceImpl extends AbstractCrudServiceImpl<IPmPfmeaMapper, PmPfmeaEntity> implements IPmPfmeaService {
    @Autowired
    private IPmWorkShopService pmWorkShopService;
    @Autowired
    private IPmLineService pmLineService;
    @Autowired
    private IPmWorkStationService pmWorkStationService;

    @Autowired
    private IPmBopMapper pmBopMapper;

    @Override
    public void beforeUpdate(PmPfmeaEntity entity) {
        valid(entity);
    }

    @Override
    public void beforeInsert(PmPfmeaEntity entity) {
        valid(entity);
    }

    private void valid(PmPfmeaEntity entity) {
        valiBop(entity);
        //工位信息
        validWorkStation(entity);
        //线体信息
        validLine(entity);
        //车间信息
        validWorkShop(entity);
    }

    private void valiBop(PmPfmeaEntity entity) {
        //bop信息
        PmBopEntity pmBopEntity = pmBopMapper.selectById(entity.getPrcPmBopId());
        if (pmBopEntity == null) {
            throw new InkelinkException(String.format("bopID[%s]不存在",entity.getPrcPmBopId()));
        }
        entity.setPrcPmWorkstationId(pmBopEntity.getPrcPmWorkstationId());
        entity.setFrockCode(pmBopEntity.getProcessStep());
    }


    private void validWorkStation(PmPfmeaEntity entity) {
        //工位信息
        QueryWrapper<PmWorkStationEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkStationEntity> lwq = qw.lambda();
        if(entity.getPrcPmWorkstationId() != null && entity.getPrcPmWorkstationId() != 0){
            lwq.eq(PmWorkStationEntity::getId, entity.getPrcPmWorkstationId());
        }else{
            lwq.eq(PmWorkStationEntity::getWorkstationCode, entity.getWorkstationCode());
        }
        lwq.eq(PmWorkStationEntity::getVersion, 0);
        PmWorkStationEntity pmWorkStationEntity = pmWorkStationService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkStationEntity == null) {
            throw new InkelinkException("Pfmea对应的工位不存在");
        }
        entity.setPrcPmWorkstationId(pmWorkStationEntity.getId());
        entity.setWorkstationCode(pmWorkStationEntity.getWorkstationCode());
    }

    private void validLine(PmPfmeaEntity entity) {
        //线体信息
        QueryWrapper<PmLineEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmLineEntity> lwq = qw.lambda();
        if(entity.getPrcPmLineId() != null && entity.getPrcPmLineId() != 0){
            lwq.eq(PmLineEntity::getId, entity.getPrcPmLineId());
        }else{
            lwq.eq(PmLineEntity::getLineCode, entity.getLineCode());
        }
        lwq.eq(PmLineEntity::getVersion, 0);
        PmLineEntity pmLineEntity = pmLineService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmLineEntity == null) {
            throw new InkelinkException("Pfmea对应的线体不存在");
        }
        entity.setLineCode(pmLineEntity.getLineCode());
        entity.setPrcPmLineId(pmLineEntity.getId());
    }

    private void validWorkShop(PmPfmeaEntity entity) {
        //车间信息
        QueryWrapper<PmWorkShopEntity> qw = new QueryWrapper();
        LambdaQueryWrapper<PmWorkShopEntity> lwq = qw.lambda();
        if(entity.getPrcPmWorkshopId() != null && entity.getPrcPmWorkshopId() != 0){
            lwq.eq(PmWorkShopEntity::getId, entity.getPrcPmWorkshopId());
        }else{
            lwq.eq(PmWorkShopEntity::getWorkshopCode, entity.getWorkshopCode());
        }
        lwq.eq(PmWorkShopEntity::getVersion, 0);
        PmWorkShopEntity pmWorkShopEntity = pmWorkShopService.getData(qw,false).stream().findFirst().orElse(null);
        if (pmWorkShopEntity == null) {
            throw new InkelinkException("Pfmea对应的车间不存在");
        }
        entity.setWorkshopCode(pmWorkShopEntity.getWorkshopCode());
        entity.setPrcPmWorkshopId(pmWorkShopEntity.getId());
    }

    @Override
    public List<PmPfmeaEntity> getByShopId(Long shopId) {
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
    public PmPfmeaEntity get(Serializable id, Integer version) {
        return null;
    }
}