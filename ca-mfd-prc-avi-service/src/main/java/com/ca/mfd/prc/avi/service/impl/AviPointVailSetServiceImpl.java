package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseSetEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.entity.AviPointVailSetEntity;
import com.ca.mfd.prc.avi.mapper.IAviPointVailSetMapper;
import com.ca.mfd.prc.avi.service.IAviPointVailSetService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author inkelink
 * @Description: AVI过点方法验证配置服务实现
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
@Service
public class AviPointVailSetServiceImpl extends AbstractCrudServiceImpl<IAviPointVailSetMapper, AviPointVailSetEntity> implements IAviPointVailSetService {
    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    private LocalCache localCache;

    private static final String cacheName = "PRC_AVI_POINT_VAIL_SET";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void beforeInsert(AviPointVailSetEntity model) {
        removeCache();
        valid(model);
    }

    @Override
    public void beforeUpdate(AviPointVailSetEntity model) {
        removeCache();
        valid(model);
    }

    @Override
    public void afterDelete(Wrapper<AviPointVailSetEntity> queryWrapper) {
        removeCache();
    }


    private void valid(AviPointVailSetEntity model) {
        if (StringUtils.isBlank(model.getAviCode())) {
            model.setAviCode("0");
            model.setAviName("所有站点");
        }
        if (StringUtils.isBlank(model.getPrvAviFunction())) {
            throw new InkelinkException("请选择执行方法");
        }
        Long countNum = getAviPointListCount(model.getAviCode(), model.getId());
        if (countNum > 0) {
            throw new InkelinkException("该站点已存在设置");
        }

        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo != null) {
            model.setAviName(aviInfo.getAviName());
        }

        PmLineEntity lineEntity;
        if (aviInfo != null) {
            lineEntity = pmAllDtoResultVo.getLines().stream().filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId()))
                    .findFirst().orElse(null);
            if (lineEntity != null) {
                model.setLineCode(lineEntity.getLineCode());
            }
        } else {
            lineEntity = null;
        }

        PmWorkShopEntity shopEntity = null;
        if (lineEntity != null) {
            shopEntity = pmAllDtoResultVo.getShops().stream().filter(s -> Objects.equals(s.getId(), lineEntity.getPrcPmWorkshopId()))
                    .findFirst().orElse(null);
            if (shopEntity != null) {
                model.setWorkshopCode(shopEntity.getWorkshopCode());
            }
        }
    }

    private Long getAviPointListCount(String aviCode, Long id) {
        QueryWrapper<AviPointVailSetEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviPointVailSetEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviPointVailSetEntity::getAviCode, aviCode);
        lambdaQueryWrapper.ne(AviPointVailSetEntity::getId, id);
        return selectCount(queryWrapper);
    }
}