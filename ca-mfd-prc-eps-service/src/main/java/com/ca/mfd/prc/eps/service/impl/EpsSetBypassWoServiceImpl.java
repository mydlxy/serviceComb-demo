package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsSetBypassWoMapper;
import com.ca.mfd.prc.eps.service.IEpsSetBypassWoService;
import com.ca.mfd.prc.eps.entity.EpsSetBypassWoEntity;
import org.apache.commons.lang3.StringUtils;
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
 * @Description: 设置进工位BYPASS工艺服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsSetBypassWoServiceImpl extends AbstractCrudServiceImpl<IEpsSetBypassWoMapper, EpsSetBypassWoEntity> implements IEpsSetBypassWoService {
    private static final Logger logger = LoggerFactory.getLogger(EpsSetBypassWoServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_SET_BYPASS_WO";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsSetBypassWoEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsSetBypassWoEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(EpsSetBypassWoEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsSetBypassWoEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsSetBypassWoEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsSetBypassWoEntity>> getDataFunc = (obj) -> {
                List<EpsSetBypassWoEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsSetBypassWoEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

    /**
     * 获取工位bypass的工艺列表
     *
     * @param workstationCode
     * @param wocodes
     * @return
     */
    @Override
    public List<String> getBypassWoCodes(String workstationCode, List<String> wocodes) {
        return getAllDatas().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), workstationCode)
                && wocodes.contains(c.getWoCode())).map(EpsSetBypassWoEntity::getWoCode).collect(Collectors.toList());
    }
}