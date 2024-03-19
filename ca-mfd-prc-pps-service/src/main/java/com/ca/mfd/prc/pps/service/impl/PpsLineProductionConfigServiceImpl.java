package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.pps.dto.SetLinePara;
import com.ca.mfd.prc.pps.entity.PpsLineProductionConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsLineProductionConfigMapper;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsLineProductionConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author inkelink
 * @Description: 线体生产配置服务实现
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
@Service
public class PpsLineProductionConfigServiceImpl extends AbstractCrudServiceImpl<IPpsLineProductionConfigMapper, PpsLineProductionConfigEntity> implements IPpsLineProductionConfigService {
    private static final Logger logger = LoggerFactory.getLogger(PpsLineProductionConfigServiceImpl.class);
    private static final String cacheName = "PRC_PPS_LINE_PRODUCTION_CONFIG";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private PmVersionProvider pmVersionProvider;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PpsLineProductionConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsLineProductionConfigEntity model) {
        QueryWrapper<PpsLineProductionConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsLineProductionConfigEntity::getLineCode, model.getLineCode());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("生产线体" + model.getLineCode() + "已存在");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(PpsLineProductionConfigEntity model) {
        QueryWrapper<PpsLineProductionConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsLineProductionConfigEntity::getLineCode, model.getLineCode())
                .ne(PpsLineProductionConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("生产线体" + model.getLineCode() + "已存在");
        }
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsLineProductionConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<PpsLineProductionConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<PpsLineProductionConfigEntity>> getDataFunc = (obj) -> {
                List<PpsLineProductionConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<PpsLineProductionConfigEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
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
     * 设置生产区域
     *
     * @param para
     */
    @Override
    public void setLine(SetLinePara para) {
        UpdateWrapper<PpsLineProductionConfigEntity> upSet = new UpdateWrapper<>();
        upSet.lambda().set(PpsLineProductionConfigEntity::getOrderCategory, para.getOrderCategory())
                .eq(PpsLineProductionConfigEntity::getLineCode, para.getLineCode());
        update(upSet);
    }

    /**
     * 根据线体获取区域产品类型
     *
     * @param lineCode
     * @return
     */
    @Override
    public String getLineByWorkstationCode(String lineCode) {
        PmAllDTO pmAll = pmVersionProvider.getObjectedPm();
        PmLineEntity lineInfo = pmAll.getLines()
                .stream().filter(c -> StringUtils.equals(c.getLineCode(), lineCode)).findFirst().orElse(null);
        if (lineInfo == null) {
            //throw new InkelinkException("未找到线体" + lineCode);
            return StringUtils.EMPTY;
        }
        PpsLineProductionConfigEntity data = getAllDatas().stream().filter(c -> StringUtils.equals(c.getLineCode(), lineInfo.getLineCode()))
                .findFirst().orElse(null);
        if (data == null) {
            return StringUtils.EMPTY;
        }
        return data.getOrderCategory();
    }

    /**
     * 根据线体获取配置
     *
     * @param lineCode
     * @return
     */
    @Override
    public PpsLineProductionConfigEntity getFirstByLineCode(String lineCode) {
        QueryWrapper<PpsLineProductionConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsLineProductionConfigEntity::getLineCode, lineCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

}