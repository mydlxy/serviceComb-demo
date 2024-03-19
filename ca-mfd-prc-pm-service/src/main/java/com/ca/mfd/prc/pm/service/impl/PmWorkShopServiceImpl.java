package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmWorkShopMapper;
import com.ca.mfd.prc.pm.service.IPmLineService;
import com.ca.mfd.prc.pm.service.IPmWorkShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author inkelink ${email}
 * @Description: 车间
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmWorkShopServiceImpl extends AbstractPmCrudServiceImpl<IPmWorkShopMapper, PmWorkShopEntity> implements IPmWorkShopService {

    @Autowired
    private IPmWorkShopMapper pmShopDao;
    @Autowired
    private IPmLineService pmLineService;

    @Override
    public void update(PmWorkShopEntity dto) {
        beforeUpdate(dto);
        LambdaUpdateWrapper<PmWorkShopEntity> luw = new LambdaUpdateWrapper<>();
        luw.set(PmWorkShopEntity::getWorkshopCode, dto.getWorkshopCode());
        luw.set(PmWorkShopEntity::getWorkshopName, dto.getWorkshopName());
        luw.set(PmWorkShopEntity::getProductTime, dto.getProductTime());
        luw.set(PmWorkShopEntity::getWorkshopDesignJph, dto.getWorkshopDesignJph());
        luw.set(PmWorkShopEntity::getDisplayNo, dto.getDisplayNo());
        luw.set(PmWorkShopEntity::getRemark, dto.getRemark());
        luw.set(PmWorkShopEntity::getBufferCount, dto.getBufferCount());
        luw.set(PmWorkShopEntity::getBufferCountLowLimt, dto.getBufferCountLowLimt());
        luw.set(PmWorkShopEntity::getVehicleCount, dto.getVehicleCount());
        luw.set(PmWorkShopEntity::getVehicleMax, dto.getVehicleMax());
        luw.set(PmWorkShopEntity::getVehicleMin, dto.getVehicleMin());
        luw.eq(PmWorkShopEntity::getId, dto.getId());
        luw.eq(PmWorkShopEntity::getVersion, dto.getVersion());
        this.update(luw);
    }
    @Override
    public void delete(Serializable[] ids) {
        pmLineService.canDeleteWorkShop(ids);
        super.delete(ids);
    }

    @Override
    public void beforeUpdate(PmWorkShopEntity entity) {
        ValidData(entity);
    }

    @Override
    public void beforeInsert(PmWorkShopEntity entity) {
        ValidData(entity);
    }

    private void ValidData(PmWorkShopEntity entity) {
        //this.pmShopDao.selectCount(o->)
        QueryWrapper<PmWorkShopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkShopEntity::getWorkshopCode, entity.getWorkshopCode());
        lambdaQueryWrapper.eq(PmWorkShopEntity::getVersion, entity.getVersion());
        lambdaQueryWrapper.ne(PmWorkShopEntity::getId, entity.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException("代码" + entity.getWorkshopCode() + "已存在");
        }
    }


    @Override
    public List<PmWorkShopEntity> getByShopId(Long shopId) {
        return pmShopDao.selectList(Wrappers.lambdaQuery(PmWorkShopEntity.class)
                .eq(PmWorkShopEntity::getId, shopId)
                .eq(PmWorkShopEntity::getVersion, 0)
                .eq(PmWorkShopEntity::getIsDelete, false));
    }

    @Override
    public Map<String, String> getExcelHead() {
        Map<String, String> shopMap = new HashMap<>(11);
        shopMap.put("displayNo", "顺序号");
        shopMap.put("code", "代码");
        shopMap.put("name", "名称");
        shopMap.put("vehicleCount", "标准在制");
        shopMap.put("bufferCount", "缓存在制");
        shopMap.put("bufferCountLowLimt", "缓存下限");
        shopMap.put("vehicleMax", "最高在制");
        shopMap.put("vehicleMin", "最低在制");
        shopMap.put("workShopDesignJph", "JPH");
        shopMap.put("remark", "备注");
        return shopMap;
    }

    @Override
    public List<PmWorkShopEntity> getListByParentId(Long parentId) {
        QueryWrapper<PmWorkShopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkShopEntity::getPrcPmOrganizationId, parentId);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getVersion, 0);
        lambdaQueryWrapper.orderByAsc(PmWorkShopEntity::getDisplayNo);
        return this.pmShopDao.selectList(queryWrapper);
    }

    @Override
    public PmWorkShopEntity getPmShopEntityByVersion(Long shopId, int version, Boolean flags) {
        QueryWrapper<PmWorkShopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkShopEntity::getId, shopId);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getIsDelete, flags);
        return pmShopDao.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PmWorkShopEntity getPmShopEntityByCodeAndVersion(String shopCode, int version, Boolean flags) {
        QueryWrapper<PmWorkShopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkShopEntity::getWorkshopCode, shopCode);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getVersion, version);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getIsDelete, flags);
        return pmShopDao.selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public PmWorkShopEntity get(Serializable id) {
        QueryWrapper<PmWorkShopEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWorkShopEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmWorkShopEntity::getId, id);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getIsDelete, false);
        lambdaQueryWrapper.eq(PmWorkShopEntity::getVersion, 0);
        List<PmWorkShopEntity> pmWorkShopEntityList = pmShopDao.selectList(queryWrapper);
        return pmWorkShopEntityList.stream().findFirst().orElse(null);
    }

    @Override
    public void importExcel(List<Map<String, String>> listFromExcelSheet, Map<String,
            Map<String, String>> mapSysConfigByCategory, String sheetName, PmAllDTO currentUnDeployData) throws Exception {
        List<PmWorkShopEntity> listWorkShop = this.convertExcelDataToEntity(listFromExcelSheet,
                mapSysConfigByCategory, sheetName);
        verifyAndSaveEntity(listWorkShop, currentUnDeployData);
    }

    private void verifyAndSaveEntity(List<PmWorkShopEntity> listEntity, PmAllDTO currentUnDeployData) {
        if (listEntity == null || listEntity.isEmpty()) {
            return;
        }
        //验证顺序号
        Set<Integer> displayNos = new HashSet();
        PmOrganizationEntity organization = currentUnDeployData.getOrganization();
        List<PmWorkShopEntity> shops = currentUnDeployData.getShops();
        for (PmWorkShopEntity entity : listEntity) {
            if (displayNos.contains(entity.getDisplayNo())) {
                throw new InkelinkException("车间编码[" + entity.getWorkshopCode() + "]顺序号[" + entity.getDisplayNo() + "]重复");
            }
            displayNos.add(entity.getDisplayNo());
            if (entity.getWorkshopDesignJph() <= 0) {
                throw new InkelinkException("车间产能必须大于0");
            }

            PmWorkShopEntity existShop = shops.stream().filter(item -> item.getWorkshopCode().equals(entity.getWorkshopCode())).findFirst().orElse(null);
            if (existShop != null) {
                if (shops.stream().filter(item -> item.getDisplayNo().equals(entity.getDisplayNo())
                        && !item.getId().equals(existShop.getId())).findFirst().orElse(null) != null) {
                    throw new InkelinkException("[" + existShop.getWorkshopName() + "]的顺序号[" + entity.getDisplayNo() + "]已经存在");
                }
                LambdaUpdateWrapper<PmWorkShopEntity> luw = new LambdaUpdateWrapper();
                luw.setEntity(entity);
                luw.set(PmWorkShopEntity::getPrcPmOrganizationId, organization.getId());
                luw.eq(PmWorkShopEntity::getId, existShop.getId());
                luw.eq(PmWorkShopEntity::getVersion, 0);
                this.update(luw);
            } else if (!entity.getIsDelete()) {
                PmWorkShopEntity sameDisplayNoShop = shops.stream().filter(item -> item.getDisplayNo().equals(entity.getDisplayNo())).findFirst().orElse(null);
                if (sameDisplayNoShop != null) {
                    throw new InkelinkException("[" + entity.getWorkshopName() + "]的顺序号[" + entity.getDisplayNo() + "]已经存在");
                }
                entity.setPrcPmOrganizationId(organization.getId());
                entity.setVersion(0);
                this.insert(entity);
            }
        }


    }
}