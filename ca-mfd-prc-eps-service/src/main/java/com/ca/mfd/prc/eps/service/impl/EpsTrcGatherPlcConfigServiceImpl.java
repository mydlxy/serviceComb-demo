package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.eps.dto.CollectBarcodePara;
import com.ca.mfd.prc.eps.mapper.IEpsTrcGatherPlcConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsTrcGatherPlcConfigService;
import com.ca.mfd.prc.eps.entity.EpsTrcGatherPlcConfigEntity;
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
 * @Description: PLC收集追溯条码配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsTrcGatherPlcConfigServiceImpl extends AbstractCrudServiceImpl<IEpsTrcGatherPlcConfigMapper, EpsTrcGatherPlcConfigEntity> implements IEpsTrcGatherPlcConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsTrcGatherPlcConfigServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_TRC_GATHER_PLC_CONFIG";
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsTrcGatherPlcConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsTrcGatherPlcConfigEntity model) {
        QueryWrapper<EpsTrcGatherPlcConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsTrcGatherPlcConfigEntity::getWorkstationCode, model.getWorkstationCode())
                .eq(EpsTrcGatherPlcConfigEntity::getPlcIp, model.getPlcIp())
                .eq(EpsTrcGatherPlcConfigEntity::getGatherDb, model.getGatherDb());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("工位" + model.getWorkstationCode() + ";PLC:" + model.getPlcIp() + "[" + model.getGatherDb() + "]已配置了条码采集");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(EpsTrcGatherPlcConfigEntity model) {
        QueryWrapper<EpsTrcGatherPlcConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsTrcGatherPlcConfigEntity::getWorkstationCode, model.getWorkstationCode())
                .eq(EpsTrcGatherPlcConfigEntity::getPlcIp, model.getPlcIp())
                .eq(EpsTrcGatherPlcConfigEntity::getGatherDb, model.getGatherDb())
                .ne(EpsTrcGatherPlcConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("工位" + model.getWorkstationCode() + ";PLC:" + model.getPlcIp() + "[" + model.getGatherDb() + "]已配置了条码采集");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsTrcGatherPlcConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsTrcGatherPlcConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsTrcGatherPlcConfigEntity>> getDataFunc = (obj) -> {
                List<EpsTrcGatherPlcConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsTrcGatherPlcConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 采集条码
     *
     * @param para
     */
    @Override
    public void collectBarcode(CollectBarcodePara para) {
        UpdateWrapper<EpsTrcGatherPlcConfigEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsTrcGatherPlcConfigEntity::getNowBarcode, para.getNowBarcode())
                .set(EpsTrcGatherPlcConfigEntity::getExecResult, para.getExecResult())
                .set(EpsTrcGatherPlcConfigEntity::getErrorMessage, StringUtils.EMPTY)
                .eq(EpsTrcGatherPlcConfigEntity::getId, ConvertUtils.stringToLong(para.getId()));
    }
}