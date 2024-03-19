package com.ca.mfd.prc.pps.extend.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsBindingTagEntity;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.extend.IPpsOrderExtendService;
import com.ca.mfd.prc.pps.mapper.IPpsOrderMapper;
import com.ca.mfd.prc.pps.service.IPpsBindingTagService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单扩展
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@Service("ppsOrderExtendService")
public class PpsOrderExtendServiceImpl extends AbstractCrudServiceImpl<IPpsOrderMapper, PpsOrderEntity> implements IPpsOrderExtendService {

    private static final Integer SN_END_LEN = 4;
    @Autowired
    private IPpsBindingTagService ppsBindingTagService;


    @Override
    public PpsOrderEntity getPpsOrderBySn(String sn) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getSn, sn);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public Long getCountByPlanNo(String planNo) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getPlanNo, planNo);
        return selectCount(qry);
    }

    @Override
    public PpsOrderEntity getByBomVersion(String bomVersions) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getBomVersion, bomVersions);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public PpsOrderEntity getByPlanNo(String planNo) {
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getPlanNo, planNo);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 根据唯一码 或者 条码查询订单（全匹配）
     *
     * @param code 关键字
     * @return 订单实体
     */
    @Override
    public PpsOrderEntity getPpsOrderBySnOrBarcode(String code) {
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        //lambdaQueryWrapper.eq(PpsOrderEntity::getSn, code)
        //.or(o -> o.eq(PpsOrderEntity::getBarcode, code));
        lambdaQueryWrapper.and(s -> s.eq(PpsOrderEntity::getSn, code).or().eq(PpsOrderEntity::getBarcode, code));
        return this.getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据sn或barcode查询
     *
     * @param codes
     * @return
     */
    @Override
    public List<PpsOrderEntity> getPpsOrderBySnsOrBarcodes(List<String> codes) {
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.and(s -> s.in(PpsOrderEntity::getSn, codes).or().in(PpsOrderEntity::getBarcode, codes));
        return getData(queryWrapper, false);
    }

    /**
     * 获取订单数量（模糊匹配）
     *
     * @param top  关键字
     * @param code 关键字
     * @return 列表
     */
    @Override
    public List<PpsOrderEntity> getTopOrderByCodeLike(Integer top, String code) {
        if (StringUtils.isBlank(code)) {
            return new ArrayList<>();
        }
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.and(a -> a.like(PpsOrderEntity::getSn, code).or().like(PpsOrderEntity::getBarcode, code));
        if (top < 0) {
            return selectList(queryWrapper);
        } else {
            return this.getTopDatas(top, queryWrapper);
        }
    }

    /**
     * 获取订单信息
     *
     * @param key 关键字
     * @return 订单信息
     */
    @Override
    public PpsOrderEntity getPpsOrderInfoByKeyAvi(String key) {
        if (StringUtils.isBlank(key)) {
            return new PpsOrderEntity();
        }
        //条码后4位，可能是车型，去掉在用于查询
        String newCode = key.trim();
        QueryWrapper<PpsOrderEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PpsOrderEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.in(PpsOrderEntity::getSn, key).or().in(PpsOrderEntity::getBarcode, key).or().eq(PpsOrderEntity::getBarcode, newCode);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }


    /**
     * 获取订单信息
     *
     * @param key
     * @return PpsOrderEntity
     */
    @Override
    public PpsOrderEntity getPpsOrderInfoByKey(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        QueryWrapper<PpsOrderEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsOrderEntity::getSn, key)
                .or(o -> o.eq(PpsOrderEntity::getBarcode, key));
        PpsOrderEntity resultModel = getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (resultModel == null) {
            PpsBindingTagEntity tagInfo = ppsBindingTagService.getFirstByCode(key);
            if (tagInfo == null) {
                return null;
            }
            QueryWrapper<PpsOrderEntity> qryBarcode = new QueryWrapper<>();
            qryBarcode.lambda().eq(PpsOrderEntity::getBarcode, tagInfo.getBarcode());
            resultModel = getTopDatas(1, qryBarcode).stream().findFirst().orElse(null);
        }
        return resultModel;
    }


    /**
     * 根据车间订单ID 更新状态
     *
     * @param completeQuantity 完成数量
     * @param orderId          车间订单ID
     */
    @Override
    public void updateEntityByCompleteQuantity(Integer completeQuantity, Long orderId) {
        UpdateWrapper<PpsOrderEntity> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(PpsOrderEntity::getOrderStatus, 5)
                .set(PpsOrderEntity::getActualEndDt, new Date())
                .eq(PpsOrderEntity::getId, orderId);
        this.update(updateWrapper);
    }
}
