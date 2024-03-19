package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.entity.AviFrozenEntity;
import com.ca.mfd.prc.avi.mapper.IAviFrozenMapper;
import com.ca.mfd.prc.avi.service.IAviFrozenService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 冻结产品
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviFrozenServiceImpl extends AbstractCrudServiceImpl<IAviFrozenMapper, AviFrozenEntity> implements IAviFrozenService {

    @Autowired
    IdentityHelper identityHelper;

    @Autowired
    //IPmVersionService pmVersionService;
    PmVersionProvider pmVersionProvider;

    @Autowired
    //IPpsEntryService ppsEntryService;
    PpsEntryProvider ppsEntryProvider;

    @Autowired
    //private IPpsOrderService ppsOrderService;
    private PpsOrderProvider ppsOrderProvider;

    /**
     * 冻结确认
     *
     * @param frozenId 冻结ID
     */
    @Override
    public void confirm(String frozenId) {
        AviFrozenEntity frozenInfo = this.get(frozenId);
        if (frozenInfo != null) {
            //frozenInfo.setConfigDt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            frozenInfo.setConfigDt(new Date());
            frozenInfo.setConfigUserId(identityHelper.getUserId());
            frozenInfo.setConfigUserName(identityHelper.getUserName());
            frozenInfo.setIsConfig(true);
            this.update(frozenInfo);
            // 修改订单表的冻结状态
            //ppsOrderService.operateIsFreezeById(frozenInfo.getSn(), Boolean.TRUE, "");
            ppsOrderProvider.operateIsFreezeById(frozenInfo.getSn(), Boolean.TRUE, "");
        }
    }

    /**
     * 取消冻结
     *
     * @param frozenId 解冻ID
     */
    @Override
    public void unFrozen(String frozenId) {
        AviFrozenEntity frozenInfo = this.get(frozenId);
        if (frozenInfo != null) {
            //frozenInfo.setUnFrozenDt(DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));
            frozenInfo.setUnFrozenDt(new Date());
            frozenInfo.setUnUserName(identityHelper.getUserName());
            frozenInfo.setUnUserId(identityHelper.getUserId());
            this.update(frozenInfo);
            //修改订单状态
            //ppsOrderService.operateIsFreezeById(frozenInfo.getSn(), Boolean.FALSE, "");
            ppsOrderProvider.operateIsFreezeById(frozenInfo.getSn(), Boolean.FALSE, "");
        }
    }


    @Override
    public void beforeInsert(AviFrozenEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviFrozenEntity model) {
        valid(model);
    }

    private void valid(AviFrozenEntity model) {
        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo == null) {
            throw new InkelinkException("AVI站点查询异常");
        }
        model.setAviCode(aviInfo.getAviCode());
        model.setAviName(aviInfo.getAviName());
        if (model.getFrozenDt() == null) {
            model.setFrozenDt(new Date());
        }
        //        if (model.getUnFrozenDt() == null) {
        //            model.setUnFrozenDt(DateUtils.stringToDate("1970-01-01 00:00:00", DateUtils.DATE_TIME_PATTERN));
        //        }
        //        if (model.getConfigDt() == null) {
        //            model.setConfigDt(DateUtils.stringToDate("1970-01-01 00:00:00", DateUtils.DATE_TIME_PATTERN));
        //        }
        List<AviFrozenEntity> validModel = getFrozenEntityByTps(model.getAviCode(), model.getSn(), model.getId());
        if (StringUtils.isBlank(model.getSn())) {
            throw new InkelinkException("产品唯一编码不能为空");
        }
        if (validModel.size() >= 1) {
            throw new InkelinkException("该Avi已经存在VIN码信息");
        }
        //PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderBySnOrBarcode(model.getSn());
        PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(model.getSn());
        if (ppsOrderEntity == null) {
            throw new InkelinkException("该VIN码不存在");
        }
    }

    private List<AviFrozenEntity> getFrozenEntityByTps(String aviCode, String sn, Long id) {
        QueryWrapper<AviFrozenEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviFrozenEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviFrozenEntity::getAviCode, aviCode);
        lambdaQueryWrapper.eq(AviFrozenEntity::getSn, sn);
        lambdaQueryWrapper.ne(AviFrozenEntity::getId, id);
        return selectList(queryWrapper);
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
            if (data.containsKey("lineCode") && data.getOrDefault("lineCode", null) != null) {
                PmLineEntity lineInfo = pmAllDatas.getLines().stream()
                        .filter(s -> StringUtils.equals(s.getLineCode(), data.get("lineCode").toString())).findFirst().orElse(null);
                if (lineInfo != null) {
                    data.put("lineCode", lineInfo.getLineName());
                }
            }
            if (data.containsKey("isConfig") && data.getOrDefault("isConfig", null) != null) {
                if (StringUtils.equals(data.get("isConfig").toString(), "true")) {
                    data.put("isConfig", "是");
                } else {
                    data.put("isConfig", "否");
                }
            }

            if (data.containsKey("frozenDt") && data.getOrDefault("frozenDt", null) != null) {
                if (StringUtils.isNotBlank(data.get("frozenDt").toString())) {
                    data.put("frozenDt", DateUtils.format((Date) data.get("frozenDt"), DateUtils.DATE_TIME_PATTERN));
                }
            }
            if (data.containsKey("unFrozenDt") && data.getOrDefault("unFrozenDt", null) != null) {
                if (StringUtils.isNotBlank(data.get("unFrozenDt").toString())) {
                    data.put("unFrozenDt", DateUtils.format((Date) data.get("unFrozenDt"), DateUtils.DATE_TIME_PATTERN));
                }
            }
            if (data.containsKey("creationDate") && data.getOrDefault("creationDate", null) != null) {
                if (StringUtils.isNotBlank(data.get("creationDate").toString())) {
                    data.put("creationDate", DateUtils.format((Date) data.get("creationDate"), DateUtils.DATE_TIME_PATTERN));
                }
            }

        }
    }

}