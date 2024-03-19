package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsEntryProvider;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.entity.AviBlockEntity;
import com.ca.mfd.prc.avi.mapper.IAviBlockMapper;
import com.ca.mfd.prc.avi.service.IAviBlockService;
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

/**
 * 整车AVI锁定
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviBlockServiceImpl extends AbstractCrudServiceImpl<IAviBlockMapper, AviBlockEntity> implements IAviBlockService {
    @Autowired
    PpsEntryProvider ppsEntryProvider;

    @Autowired
    //IPmVersionService pmVersionService;
    PmVersionProvider pmVersionProvider;

    @Autowired
    //private IPpsOrderService ppsOrderService;
    private PpsOrderProvider ppsOrderProvider;

    @Override
    public void beforeInsert(AviBlockEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(AviBlockEntity model) {
        valid(model);
    }

    /**
     * 验证车辆是否有锁定
     *
     * @param sn      产品唯一标识
     * @param aviCode aviCode
     * @return 返回车辆是否锁定
     */
    @Override
    public boolean validExistsBlock(String sn, String aviCode) {
        boolean result = false;
        QueryWrapper<AviBlockEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviBlockEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviBlockEntity::getSn, sn);
        lambdaQueryWrapper.eq(AviBlockEntity::getAviCode, aviCode);
        return selectCount(queryWrapper) > 0;
    }

    private void valid(AviBlockEntity model) {
        if (StringUtils.isBlank(model.getSn())) {
            throw new InkelinkException("条码不能为空");
        }
        PpsOrderEntity ppsOrderEntity = ppsOrderProvider.getPpsOrderBySnOrBarcode(model.getSn());
        if (ppsOrderEntity == null) {
            throw new InkelinkException("VIN码不存在");
        }
        Long aviBlockNum = getAviBlockCount(model.getSn(), model.getAviCode(), model.getId());
        if (aviBlockNum > 0) {
            throw new InkelinkException("该站点已存在此产品唯一码");
        }
        //PmAllDTO pmAllDtoResultVo = pmVersionService.getObjectedPm();
        PmAllDTO pmAllDtoResultVo = pmVersionProvider.getObjectedPm();
        PmAviEntity aviInfo = pmAllDtoResultVo.getAvis().stream().
                filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
        if (aviInfo == null) {
            throw new InkelinkException("AVI站点查询异常");
        }
        model.setAviName(aviInfo.getAviName());
    }

    private Long getAviBlockCount(String sn, String aviCode, Long id) {
        QueryWrapper<AviBlockEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviBlockEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviBlockEntity::getSn, sn);
        lambdaQueryWrapper.eq(AviBlockEntity::getAviCode, aviCode);
        lambdaQueryWrapper.eq(AviBlockEntity::getIsProcess, false);
        lambdaQueryWrapper.ne(AviBlockEntity::getId, id);
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
            if (data.containsKey("lineCode") && data.getOrDefault("lineCode", null) != null) {
                PmLineEntity lineInfo = pmAllDatas.getLines().stream()
                        .filter(s -> StringUtils.equals(s.getLineCode(), data.get("lineCode").toString())).findFirst().orElse(null);
                if (lineInfo != null) {
                    data.put("lineCode", lineInfo.getLineName());
                }
            }
            if (data.containsKey("isProcess") && data.getOrDefault("isProcess", null) != null) {
                if (StringUtils.equals(data.get("isProcess").toString(), "true")) {
                    data.put("isProcess", "是");
                } else {
                    data.put("isProcess", "否");
                }
            }
            if (data.containsKey("creationDate") && data.getOrDefault("creationDate", null) != null) {
                if (data.containsKey("creationDate") && data.getOrDefault("creationDate", null) != null) {
                    data.put("creationDate", DateUtils.format((Date) data.get("beginDt"), DateUtils.DATE_TIME_PATTERN));
                }
            }
        }
    }

}