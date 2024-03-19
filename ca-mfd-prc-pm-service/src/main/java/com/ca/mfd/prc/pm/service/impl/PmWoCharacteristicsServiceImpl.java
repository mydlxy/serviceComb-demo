package com.ca.mfd.prc.pm.service.impl;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;

import com.ca.mfd.prc.pm.entity.PmCharacteristicsDataEntity;
import com.ca.mfd.prc.pm.entity.PmWoCharacteristicsEntity;
import com.ca.mfd.prc.pm.mapper.IPmCharacteristicsDataMapper;
import com.ca.mfd.prc.pm.mapper.IPmWoCharacteristicsMapper;
import com.ca.mfd.prc.pm.service.IPmWoCharacteristicsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;


/**
 *
 * @Description: 工艺特征数据服务实现
 * @author inkelink
 * @date 2023年10月17日
 * @变更说明 BY inkelink At 2023年10月17日
 */
@Service
public class PmWoCharacteristicsServiceImpl extends AbstractCrudServiceImpl<IPmWoCharacteristicsMapper, PmWoCharacteristicsEntity> implements IPmWoCharacteristicsService {
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_PM_WO_CHARACTERISTICS";

    private final IPmWoCharacteristicsMapper pmWoCharacteristicsMapper;

    private final IPmCharacteristicsDataMapper pmCharacteristicsDataMapper;

    @Autowired
    public PmWoCharacteristicsServiceImpl(IPmWoCharacteristicsMapper pmWoCharacteristicsMapper,
                                          IPmCharacteristicsDataMapper pmCharacteristicsDataMapper){
        this.pmWoCharacteristicsMapper = pmWoCharacteristicsMapper;
        this.pmCharacteristicsDataMapper = pmCharacteristicsDataMapper;
    }



    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PmWoCharacteristicsEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PmWoCharacteristicsEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PmWoCharacteristicsEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PmWoCharacteristicsEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeUpdate(PmWoCharacteristicsEntity model) {
        valid(model);
    }

    @Override
    public void beforeInsert(PmWoCharacteristicsEntity model) {
        valid(model);
    }

    private void valid(PmWoCharacteristicsEntity model){
        if(StringUtils.isBlank(model.getPrcCharacteristicsName())
            || StringUtils.isBlank(model.getPrcPmWoCode())){
            throw new InkelinkException("特征项或者工艺代码不能为空");
        }
        QueryWrapper<PmCharacteristicsDataEntity> wrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmCharacteristicsDataEntity> lqw = wrapper.lambda();
        lqw.eq(PmCharacteristicsDataEntity :: getCharacteristicsName, model.getPrcCharacteristicsName());
        List<PmCharacteristicsDataEntity> characteristics = pmCharacteristicsDataMapper.selectList(wrapper);
        if(characteristics == null){
            throw new InkelinkException("特征项不存在");
        }
        QueryWrapper<PmWoCharacteristicsEntity> woWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PmWoCharacteristicsEntity> woLqw = woWrapper.lambda();
        woLqw.eq(PmWoCharacteristicsEntity::getPrcCharacteristicsName, model.getPrcCharacteristicsName());
        woLqw.eq(PmWoCharacteristicsEntity::getPrcPmWoCode, model.getPrcPmWoCode());
        woLqw.ne(PmWoCharacteristicsEntity::getId, model.getId() == null ? 0 : model.getId());
        List<PmWoCharacteristicsEntity> woCharacteristics = pmWoCharacteristicsMapper.selectList(woWrapper);
        if(woCharacteristics == null){
            throw new InkelinkException("该工艺代码["+model.getPrcPmWoCode()+"]已经配置特征[" + model.getPrcCharacteristicsName()+"]");
        }


    }

}