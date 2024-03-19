package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboDataDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsSpareConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsSpareConfigService;
import com.ca.mfd.prc.eps.entity.EpsSpareConfigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author inkelink
 * @Description: 区域对应备件配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsSpareConfigServiceImpl extends AbstractCrudServiceImpl<IEpsSpareConfigMapper, EpsSpareConfigEntity> implements IEpsSpareConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsSpareConfigServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_SPARE_CONFIG";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsSpareConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsSpareConfigEntity model) {
        QueryWrapper<EpsSpareConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsSpareConfigEntity::getMaterialCode, model.getMaterialCode());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(model.getMaterialCode() + "已进行配置");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(EpsSpareConfigEntity model) {
        QueryWrapper<EpsSpareConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsSpareConfigEntity::getMaterialCode, model.getMaterialCode())
                .ne(EpsSpareConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(model.getMaterialCode() + "已进行配置");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsSpareConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsSpareConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsSpareConfigEntity>> getDataFunc = (obj) -> {
                List<EpsSpareConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsSpareConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ComboDataDTO> getByLineCodes(String lineCode) {
        QueryWrapper<EpsSpareConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsSpareConfigEntity::getLineCode, lineCode);
        List<EpsSpareConfigEntity> list = selectList(qry);
        return list.stream().map(c -> new ComboDataDTO("", c.getMaterialName(),
                c.getMaterialCode())).collect(Collectors.toList());
    }
}