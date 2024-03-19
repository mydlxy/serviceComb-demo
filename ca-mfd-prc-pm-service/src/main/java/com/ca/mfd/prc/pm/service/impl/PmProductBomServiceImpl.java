package com.ca.mfd.prc.pm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmProductBomVersionsEntity;
import com.ca.mfd.prc.pm.mapper.IPmProductBomMapper;
import com.ca.mfd.prc.pm.mapper.IPmProductBomVersionsMapper;
import com.ca.mfd.prc.pm.service.IPmProductBomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @author inkelink
 * @Description: BOM
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmProductBomServiceImpl extends AbstractCrudServiceImpl<IPmProductBomMapper, PmProductBomEntity> implements IPmProductBomService {

    private static final String cacheName = "PRC_PM_PRODUCT_BOM";
    private final String cacheVersionName = "PRC_PM_PRODUCT_BOM_VERSIONS";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPmProductBomVersionsMapper pmProductBomVersionsMapper;

    /**
     * 删除缓存的数据
     */
    private void removeVerCache(PmProductBomEntity model) {
        PmProductBomVersionsEntity bomVersionsInfo = pmProductBomVersionsMapper.selectById(model.getBomVersionsId());
        if (bomVersionsInfo != null) {
            localCache.removeObject(cacheVersionName + bomVersionsInfo.getProductMaterialNo() + bomVersionsInfo.getBomVersions());
        }
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        if (idList == null) {
            return;
        }
        Serializable id = idList.stream().findFirst().orElse(null);
        if (id == null) {
            return;
        }
        PmProductBomEntity model = get(id);
        if (model != null) {
            removeVerCache(model);
        }
    }

    @Override
    public void afterInsert(PmProductBomEntity model) {
        removeVerCache(model);
    }

    @Override
    public void afterUpdate(PmProductBomEntity model) {
        removeVerCache(model);
    }

    /**
     * 更具版本号获取数据
     */
    @Override
    public List<PmProductBomEntity> getByBomVersionId(Long bomVersionId) {
        QueryWrapper<PmProductBomEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PmProductBomEntity::getBomVersionsId, bomVersionId);
        return selectList(qry);
    }

}