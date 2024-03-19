package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.eps.mapper.IEpsTraceCodeRuleMapper;
import com.ca.mfd.prc.eps.service.IEpsTraceCodeRuleService;
import com.ca.mfd.prc.eps.entity.EpsTraceCodeRuleEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author inkelink
 * @Description: 条码追溯规则服务实现
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Service
public class EpsTraceCodeRuleServiceImpl extends AbstractCrudServiceImpl<IEpsTraceCodeRuleMapper, EpsTraceCodeRuleEntity> implements IEpsTraceCodeRuleService {
    private static final Logger logger = LoggerFactory.getLogger(EpsTraceCodeRuleServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_TRACE_CODE_RULE";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsTraceCodeRuleEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsTraceCodeRuleEntity model) {
     /*   if (StringUtils.isNotBlank(model.getFeatureCode()) && FeatureTool.verifyExpression(model.getFeatureCode())) {
            removeCache();
        }else {
            throw new InkelinkException("特征表达式格式有错误");
        }*/
        removeCache();
    }

    @Override
    public void afterUpdate(EpsTraceCodeRuleEntity model) {
       /* if (StringUtils.isNotBlank(model.getFeatureCode()) && FeatureTool.verifyExpression(model.getFeatureCode())) {
            removeCache();
        }else {
            throw new InkelinkException("特征表达式格式有错误");
        }*/
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsTraceCodeRuleEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsTraceCodeRuleEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsTraceCodeRuleEntity>> getDataFunc = (obj) -> {
                List<EpsTraceCodeRuleEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsTraceCodeRuleEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
            if (caches == null) {
                return new ArrayList<>();
            }
            return caches;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }
}