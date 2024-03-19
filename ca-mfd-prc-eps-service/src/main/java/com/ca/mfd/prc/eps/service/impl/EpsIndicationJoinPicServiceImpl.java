package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.eps.mapper.IEpsIndicationJoinPicMapper;
import com.ca.mfd.prc.eps.service.IEpsIndicationJoinPicService;
import com.ca.mfd.prc.eps.entity.EpsIndicationJoinPicEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

/**
 * @author inkelink
 * @Description: 作业指示关联图片服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsIndicationJoinPicServiceImpl extends AbstractCrudServiceImpl<IEpsIndicationJoinPicMapper, EpsIndicationJoinPicEntity> implements IEpsIndicationJoinPicService {
    private static final String CACHE_NAME = "PRC_EPS_INDICATION_JOIN_PIC";
    @Autowired
    private LocalCache localCache;

    @Override
    public void afterDelete(Wrapper<EpsIndicationJoinPicEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsIndicationJoinPicEntity model) {
        verifyExpressWithThrow(model);
    }

    @Override
    public void afterUpdate(EpsIndicationJoinPicEntity model) {
        verifyExpressWithThrow(model);

    }

    @Override
    public void afterUpdate(Wrapper<EpsIndicationJoinPicEntity> updateWrapper) {
        removeCache();
    }

    private void verifyExpressWithThrow(EpsIndicationJoinPicEntity model) {
        if (FeatureTool.verifyExpression(model.getFeatureCode())) {
            removeCache();
        } else {
            throw new InkelinkException("特征表达式格式有错误");
        }
    }

    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    public  static void main(String[]  args){
        String str = "C857EV:AAL001";
        System.out.println(FeatureTool.verifyExpression(str));
        System.out.println(str);
    }


}