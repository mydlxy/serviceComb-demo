package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.entity.AviBlockEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.entity.AviPointOperationBlockEntity;
import com.ca.mfd.prc.avi.mapper.IAviPointOperationBlockMapper;
import com.ca.mfd.prc.avi.service.IAviPointOperationBlockService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * AVI车辆过点工艺完成阻塞检查
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviPointOperationBlockServiceImpl extends AbstractCrudServiceImpl<IAviPointOperationBlockMapper, AviPointOperationBlockEntity> implements IAviPointOperationBlockService {

    @Autowired
    PmVersionProvider pmVersionProvider;


    @Override
    public void beforeInsert(AviPointOperationBlockEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviPointOperationBlockEntity model) {
        valid(model);
    }

    private void valid(AviPointOperationBlockEntity model) {
        Long recordNumber = getRecordNumber(model.getAviCode(), model.getId());
        if (recordNumber > 0) {
            throw new InkelinkException("该站点已存在，不能重复添加");
        }

        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis().stream()
                .filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo != null) {
            model.setAviCode(aviInfo.getAviCode());
            model.setAviName(aviInfo.getAviName());
        }

        PmLineEntity areaInfo;
        if (aviInfo != null) {
            areaInfo = pmVersionProvider.getObjectedPm().getLines().stream()
                    .filter(c -> Objects.equals(c.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
            if (areaInfo != null) {
                model.setLineCode(areaInfo.getLineCode());
                model.setLineName(areaInfo.getLineName());
            }
        } else {
            areaInfo = null;
        }

        PmWorkShopEntity shopInfo;
        if (areaInfo != null) {
            shopInfo = pmVersionProvider.getObjectedPm().getShops().stream()
                    .filter(s -> Objects.equals(s.getId(), areaInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
            if (shopInfo != null) {
                model.setWorkshopCode(shopInfo.getWorkshopCode());
                model.setWorkshopName(shopInfo.getWorkshopName());
            }
        }

    }

    private Long getRecordNumber(String aviCode, Long id) {
        QueryWrapper<AviPointOperationBlockEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviPointOperationBlockEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviPointOperationBlockEntity::getAviCode, aviCode);
        lambdaQueryWrapper.ne(AviPointOperationBlockEntity::getId, id);
        return selectCount(queryWrapper);
    }


    //    @Override
    //    public void beforeInsert(AviPointOperationBlockEntity model) {
    //        QueryWrapper<AviPointOperationBlockEntity> queryWrapper = new QueryWrapper<>();
    //        LambdaQueryWrapper<AviPointOperationBlockEntity> lambdaQueryWrapper = queryWrapper.lambda();
    //        lambdaQueryWrapper.eq(AviPointOperationBlockEntity::getAviCode, model.getAviCode());
    //        lambdaQueryWrapper.ne(AviPointOperationBlockEntity::getId, model.getId());
    //        if (selectCount(queryWrapper) > 0) {
    //            throw new InkelinkException("该站点已存在，不能重复添加");
    //        }
    //
    //        PmAviEntity aviInfo = pmVersionProvider.getObjectedPm().getAvis().stream()
    //                .filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
    //        if (aviInfo != null) {
    //            model.setAviCode(aviInfo.getAviCode());
    //            model.setAviName(aviInfo.getAviName());
    //        }
    //
    //        PmLineEntity areaInfo;
    //        if (aviInfo != null) {
    //            areaInfo = pmVersionProvider.getObjectedPm().getLines().stream()
    //                    .filter(c -> Objects.equals(c.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
    //            if (areaInfo != null) {
    //                model.setLineCode(areaInfo.getLineCode());
    //                model.setLineName(areaInfo.getLineName());
    //            }
    //        } else {
    //            areaInfo = null;
    //        }
    //
    //        PmWorkShopEntity shopInfo;
    //        if (areaInfo != null) {
    //            shopInfo = pmVersionProvider.getObjectedPm().getShops().stream()
    //                    .filter(s -> Objects.equals(s.getId(), areaInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
    //            if (shopInfo != null) {
    //                model.setWorkshopCode(shopInfo.getWorkshopCode());
    //                model.setWorkshopName(shopInfo.getWorkshopName());
    //            }
    //        }
    //    }
}