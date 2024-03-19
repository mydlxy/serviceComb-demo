package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.communication.entity.MidMaterialMasterEntity;
import com.ca.mfd.prc.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.pm.mapper.IPmProductMaterialMasterMapper;
import com.ca.mfd.prc.pm.service.IPmProductMaterialMasterService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 物料主数据
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductMaterialMasterServiceImpl extends AbstractCrudServiceImpl<IPmProductMaterialMasterMapper, PmProductMaterialMasterEntity> implements IPmProductMaterialMasterService {

    private final String cacheName = "PRC_PM_PRODUCT_MATERIAL_MASTER";
    @Autowired
    private LocalCache localCache;

    /**
     * 获取所有的数据
     *
     * @return List<PmProductMaterialMasterEntity>
     */
    @Override
    public List<PmProductMaterialMasterEntity> getAllDatas() {
        Function<Object, ? extends List<PmProductMaterialMasterEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, -1);
    }

    @Override
    public void syncFromBom(List<MidMaterialMasterEntity> datas) {
        if (CollectionUtils.isEmpty(datas)) {
            throw new InkelinkException("未查询到数据");
        }
        List<PmProductMaterialMasterEntity> adds = new ArrayList<>();
        List<PmProductMaterialMasterEntity> updates = new ArrayList<>();
        datas.stream().forEach(c -> {
            QueryWrapper<PmProductMaterialMasterEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PmProductMaterialMasterEntity::getMaterialNo, c.getMaterialCode());
            PmProductMaterialMasterEntity entity = getTopDatas(1, qry).stream().findFirst().orElse(null);
            if (entity == null) {
                entity = new PmProductMaterialMasterEntity();
                entity.setMaterialNo(c.getMaterialCode());
                entity.setMaterialCn(c.getMaterialName());
                entity.setMaterialEn(c.getMaterialNameEn());
                entity.setUnit(c.getMeasureUnit());
                entity.setWeight(StringUtils.isNoneEmpty(c.getActualWeight()) ? new BigDecimal(c.getActualWeight()) : new BigDecimal(0));
                entity.setAttribute10("AUTO");
                adds.add(entity);
            } else if (StringUtils.equalsIgnoreCase(entity.getAttribute10(), "AUTO")) {
                entity.setMaterialNo(c.getMaterialCode());
                entity.setMaterialCn(c.getMaterialName());
                entity.setMaterialEn(c.getMaterialNameEn());
                entity.setUnit(c.getMeasureUnit());
                entity.setWeight(StringUtils.isNoneEmpty(c.getActualWeight()) ? new BigDecimal(c.getActualWeight()) : new BigDecimal(0));
                entity.setAttribute10("AUTO");
                updates.add(entity);
            }
        });
        if (!adds.isEmpty()) {
            this.insertBatch(adds, 200, false, 1);
        }
        if (!updates.isEmpty()) {
            this.updateBatchById(updates, 200, false);
        }
        this.saveChange();
    }

}