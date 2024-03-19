package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.entity.AviCutLogEntity;
import com.ca.mfd.prc.avi.mapper.IAviCutLogMapper;
import com.ca.mfd.prc.avi.service.IAviCutLogService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * AVICUT记录表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviCutLogServiceImpl extends AbstractCrudServiceImpl<IAviCutLogMapper, AviCutLogEntity> implements IAviCutLogService {
    @Autowired
    //IPpsEntryService ppsEntryService;
    PpsEntryProvider ppsEntryProvider;

    @Autowired
    // IPmVersionService pmVersionService;
    PmVersionProvider pmVersionProvider;

    @Autowired
    SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    //private IPpsOrderService ppsOrderService;
    private PpsOrderProvider ppsOrderProvider;

    @Override
    public List<AviCutLogEntity> getAviCutLogListByAviId(String aviCode, int status) {
        QueryWrapper<AviCutLogEntity> queryWrapper = new QueryWrapper<>();
        //queryWrapper.lambda().eq(AviCutLogEntity::getPmAviId, aviId).eq(AviCutLogEntity::getStatus, 1);
        queryWrapper.lambda().eq(AviCutLogEntity::getAviCode, aviCode).eq(AviCutLogEntity::getStatus, 1);
        return this.selectList(queryWrapper);
    }


    private AviCutLogEntity getAviCutLogListBySn(String aviCode,long id) {
        QueryWrapper<AviCutLogEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviCutLogEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviCutLogEntity::getSn, aviCode);
        lambdaQueryWrapper.ne(AviCutLogEntity::getId,id);
        lambdaQueryWrapper.eq(AviCutLogEntity::getStatus, 1);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public void beforeInsert(AviCutLogEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviCutLogEntity model) {
        valid(model);
    }

    private void valid(AviCutLogEntity model) {
        PpsOrderEntity orderInfo = ppsOrderProvider.getPpsOrderBySnOrBarcode(model.getSn());
        if (orderInfo == null) {
            throw new InkelinkException("未找到【" + model.getSn() + "】对应的车辆信息!");
        }
        AviCutLogEntity tpsModel = getAviCutLogListBySn(model.getSn(),model.getId());
        if (tpsModel != null) {
            if (tpsModel.getCutType() == 0) {
                throw new InkelinkException("【" + model.getSn() + "】车辆信息!已经处于切入队列中");
            } else {
                throw new InkelinkException("【" + model.getSn() + "】车辆信息!已经处于切出队列中");
            }
        }
        //PmAllDTO pmall = pmVersionService.getObjectedPm();
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmall.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo == null) {
            throw new InkelinkException("AVI站点查询异常");
        }
        Long number = getAviCutLogNumber(model.getAviCode(), model.getSn(), model.getCutType(), model.getId());
        if (number > 0) {
            throw new InkelinkException("该站点已存在产品唯一标识:【" + model.getSn() + "】");
        }
        model.setAviName(aviInfo.getAviName());
        model.setAviCode(aviInfo.getAviCode());
        PmLineEntity areaModel =
                pmall.getLines().stream().filter(s -> Objects.equals(s.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
        if (areaModel == null) {
            throw new InkelinkException("线体不存在");
        }
        model.setLineCode(areaModel.getLineCode());
        PmWorkShopEntity shopModel = pmall.getShops().stream().filter(s -> Objects.equals(s.getId(), areaModel.getPrcPmWorkshopId())).findFirst()
                .orElse(null);
        if (shopModel == null) {
            throw new InkelinkException("车间不存在");
        }
        model.setWorkshopCode(shopModel.getWorkshopCode());
    }


    private Long getAviCutLogNumber(String aviCode, String sn, int type, Long id) {
        QueryWrapper<AviCutLogEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviCutLogEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviCutLogEntity::getAviCode, aviCode);
        lambdaQueryWrapper.eq(AviCutLogEntity::getSn, sn);
        lambdaQueryWrapper.eq(AviCutLogEntity::getCutType, type);
        lambdaQueryWrapper.ne(AviCutLogEntity::getId, id);
        return selectCount(queryWrapper);
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        PmAllDTO pmAllDatas = pmVersionProvider.getObjectedPm();
        for (Map<String, Object> data : datas) {
            if (data.containsKey("workshopCode") && data.getOrDefault("workshopCode", null) != null) {
                PmWorkShopEntity workshopInfo = pmAllDatas.getShops().stream()
                        .filter(s -> StringUtils.equals(s.getWorkshopCode(), data.get("workshopCode").toString())).findFirst().orElse(null);
                if (workshopInfo != null) {
                    data.put("workshopCode", workshopInfo.getWorkshopName());
                }
            }
            if (data.containsKey("finishDt") && data.getOrDefault("finishDt", null) != null) {
                data.put("finishDt", DateUtils.format((Date) data.get("finishDt"), DateUtils.DATE_TIME_PATTERN));
            }
            if (data.containsKey("lineCode") && data.getOrDefault("lineCode", null) != null) {
                PmLineEntity lineInfo = pmAllDatas.getLines().stream()
                        .filter(s -> StringUtils.equals(s.getLineCode(), data.get("lineCode").toString())).findFirst().orElse(null);
                if (lineInfo != null) {
                    data.put("lineCode", lineInfo.getLineName());
                }
            }
            if (data.containsKey("cutType") && data.getOrDefault("cutType", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("cutType").toString(), "AviCutType");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("cutType", keyValues);
                }
            }
            if (data.containsKey("reason") && data.getOrDefault("reason", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("reason").toString(), "avicutoutreason");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("reason", keyValues);
                }
            }
            if (data.containsKey("status") && data.getOrDefault("status", null) != null) {
                String keyValues = sysConfigurationProvider.getConfiguration(data.get("status").toString(), "AviCutStatus");
                if (StringUtils.isNotBlank(keyValues)) {
                    data.put("status", keyValues);
                }
            }
        }
    }
}