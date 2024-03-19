package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmTeamLeaderConfigEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.mapper.IPmTeamleaderConfigMapper;
import com.ca.mfd.prc.pm.remote.app.core.provider.IntegratedProvider;
import com.ca.mfd.prc.pm.service.IPmTeamLeaderConfigService;
import com.ca.mfd.prc.pm.service.IPmVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author inkelink ${email}
 * @Description: 班组长配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmTeamLeaderConfigServiceImpl extends AbstractCrudServiceImpl<IPmTeamleaderConfigMapper, PmTeamLeaderConfigEntity> implements IPmTeamLeaderConfigService {
    @Autowired
    private IPmVersionService pmVersionService;

    @Autowired
    private IntegratedProvider integratedProvider;

    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_TEAMLEADER_CONFIG";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void beforeUpdate(PmTeamLeaderConfigEntity entity) {
        validData(entity);
    }

    @Override
    public void beforeInsert(PmTeamLeaderConfigEntity entity) {
        validData(entity);
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmTeamLeaderConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmTeamLeaderConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmTeamLeaderConfigEntity> updateWrapper) {
        removeCache();
    }


    private void validData(PmTeamLeaderConfigEntity model) {
        //validUnique(model);
        PmAllDTO currentPm = pmVersionService.getObjectedPm();
        PmWorkShopEntity workShop = currentPm.getShops().stream().filter(o -> o.getWorkshopCode().equals(model.getWorkshopCode())).findFirst().orElse(null);
        if (workShop == null) {
            throw new InkelinkException("车间不存在,请重新添加车间和发布启用车间信息");
        }
        model.setWorkshopCode(workShop.getWorkshopCode());
        model.setPrcPmWorkShopId(workShop.getId());
        PmLineEntity line = currentPm.getLines().stream().filter(o -> o.getLineCode().equals(model.getLineCode())).findFirst().orElse(null);
        if (line == null) {
            throw new InkelinkException("线体不存在,请重新添加和发布启用线体信息");
        }
        model.setLineCode(line.getLineCode());
        model.setLineName(line.getLineName());
        model.setPrcPmLineId(line.getId());
        //根据班长名称获取门户用户ID
        QueryUserDTO queryUserDTO = integratedProvider.getUserInfoByLoginName(model.getUserName());
        if (queryUserDTO == null) {
            throw new InkelinkException("用户:" + model.getUserName() + "调用门户获取用户ID信息失败");
        }
        if (queryUserDTO.getUserId() != null) {
            model.setUcUserId(queryUserDTO.getUserId());
        }
    }

    private void validUnique(PmTeamLeaderConfigEntity model) {
        QueryWrapper<PmTeamLeaderConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmTeamLeaderConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PmTeamLeaderConfigEntity::getWorkshopCode, model.getWorkshopCode());
        lambdaQueryWrapper.eq(PmTeamLeaderConfigEntity::getLineCode, model.getLineCode());
        lambdaQueryWrapper.eq(PmTeamLeaderConfigEntity::getTeamNo, model.getTeamNo());
        lambdaQueryWrapper.ne(PmTeamLeaderConfigEntity::getId, model.getId());
        if (selectCount(queryWrapper) > 0) {
            throw new InkelinkException(String.format("车间%s-线体%s-已存在%s班组", model.getWorkshopCode(), model.getLineCode(), model.getTeamNo()));
        }
    }

}