package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.pps.entity.PpsVtVinTimeEntity;
import com.ca.mfd.prc.pps.mapper.IPpsVtVinTimeMapper;
import com.ca.mfd.prc.pps.service.IPpsVtVinTimeService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author eric.zhou
 * @Description: VIN推迟时间配置服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsVtVinTimeServiceImpl extends AbstractCrudServiceImpl<IPpsVtVinTimeMapper, PpsVtVinTimeEntity> implements IPpsVtVinTimeService {


    /**
     * 获取首条
     *
     * @return
     */
    @Override
    public PpsVtVinTimeEntity getFirstNew() {
        QueryWrapper<PpsVtVinTimeEntity> qry = new QueryWrapper<>();
        qry.lambda().orderByDesc(PpsVtVinTimeEntity::getLastUpdateDate);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取当前年份配置
     *
     * @param orderSign 定编码
     * @return 年份
     */
    @Override
    public String getYearByOrderSign(String orderSign) {
        int nowYear = Integer.parseInt(DateUtils.format(new Date(), "yyyy"));
        int month = Integer.parseInt(DateUtils.format(new Date(), "MM"));
        QueryWrapper<PpsVtVinTimeEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsVtVinTimeEntity::getStateOrderSign, orderSign)
                .eq(PpsVtVinTimeEntity::getNowYear, nowYear)
                .le(PpsVtVinTimeEntity::getStartMonth, month)
                .ge(PpsVtVinTimeEntity::getEndMonth, month);
        PpsVtVinTimeEntity vinTime = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (vinTime != null && vinTime.getVinYear() != null) {
            return vinTime.getVinYear().toString();
        } else {
            return String.valueOf(nowYear);
        }
    }
}