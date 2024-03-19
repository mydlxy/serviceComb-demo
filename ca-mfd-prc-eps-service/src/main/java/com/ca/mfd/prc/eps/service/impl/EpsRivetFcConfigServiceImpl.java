package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsRivetFcConfigMapper;
import com.ca.mfd.prc.eps.service.IEpsRivetFcConfigService;
import com.ca.mfd.prc.eps.service.IEpsRivetFcLogService;
import com.ca.mfd.prc.eps.dto.VerifyRivetPara;
import com.ca.mfd.prc.eps.entity.EpsRivetFcConfigEntity;
import com.ca.mfd.prc.eps.entity.EpsRivetFcLogEntity;
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
 * @Description: 铆钉防错配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsRivetFcConfigServiceImpl extends AbstractCrudServiceImpl<IEpsRivetFcConfigMapper, EpsRivetFcConfigEntity> implements IEpsRivetFcConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsRivetFcConfigServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_RIVET_FC_CONFIG";
    @Autowired
    private IEpsRivetFcLogService epsRivetFcLogService;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsRivetFcConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsRivetFcConfigEntity model) {
        QueryWrapper<EpsRivetFcConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsRivetFcConfigEntity::getOrificeLocation, model.getOrificeLocation());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(model.getOrificeLocation() + "已进行了配置");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(EpsRivetFcConfigEntity model) {
        QueryWrapper<EpsRivetFcConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsRivetFcConfigEntity::getOrificeLocation, model.getOrificeLocation())
                .ne(EpsRivetFcConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException(model.getOrificeLocation() + "已进行了配置");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsRivetFcConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsRivetFcConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsRivetFcConfigEntity>> getDataFunc = (obj) -> {
                List<EpsRivetFcConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsRivetFcConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 校验料口上料
     *
     * @param para
     * @return
     */
    @Override
    public Boolean verifyRivet(VerifyRivetPara para) {
        EpsRivetFcConfigEntity configData = getAllDatas().stream().filter(c -> StringUtils.equals(c.getOrificeLocation(), para.getOrificeLocation()))
                .findFirst().orElse(null);
        if (configData == null) {
            throw new InkelinkException("未找到" + para.getOrificeLocation() + "料口的配置");
        }
        Boolean result = false;
        if (para.getBarcode().contains(configData.getMaterialCode())) {
            result = true;
        }
        EpsRivetFcLogEntity log = new EpsRivetFcLogEntity();
        log.setOrificeBarcode(para.getOrificeBarcode());
        log.setOrificeLocation(para.getOrificeLocation());
        log.setMaterialBarcode(para.getBarcode());
        log.setMaterialName(configData.getMaterialName());
        log.setMaterialCode(configData.getMaterialCode());
        log.setVerifyResult(result);
        epsRivetFcLogService.insert(log);
        return result;
    }
}