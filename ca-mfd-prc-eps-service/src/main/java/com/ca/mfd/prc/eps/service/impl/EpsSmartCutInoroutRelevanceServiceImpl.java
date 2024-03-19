package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.entity.EpsSmartCutInoroutRelevanceEntity;
import com.ca.mfd.prc.eps.mapper.IEpsSmartCutInoroutRelevanceMapper;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.service.IEpsSmartCutInoroutRelevanceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
/**
 * @author inkelink
 * @Description: PACK切入或切出关联服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsSmartCutInoroutRelevanceServiceImpl extends AbstractCrudServiceImpl<IEpsSmartCutInoroutRelevanceMapper, EpsSmartCutInoroutRelevanceEntity> implements IEpsSmartCutInoroutRelevanceService {
    private static final Logger logger = LoggerFactory.getLogger(EpsSmartCutInoroutRelevanceServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_SMART_CUT_INOROUT_RELEVANCE";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private PmVersionProvider pmVersionProvider;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<EpsSmartCutInoroutRelevanceEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsSmartCutInoroutRelevanceEntity model) {
        List<String> stationCodes = new ArrayList<>(Arrays.asList(model.getWorkstationCodes().split(",")));
        for (String scode : stationCodes) {
            if (getAllDatas().stream().filter(c -> c.getWorkstationCodes().contains(scode)).findFirst().orElse(null) != null) {
                PmWorkStationEntity station = pmVersionProvider.getObjectedPm().getStations().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), scode))
                        .findFirst().orElse(null);
                if (station != null) {
                    throw new InkelinkException(station.getWorkstationName() + "工位已进行了配置");
                } else {
                    throw new InkelinkException(scode + "工位已进行了配置");
                }
            }
        }
        removeCache();
    }

    @Override
    public void afterUpdate(EpsSmartCutInoroutRelevanceEntity model) {
        List<String> stationCodes = new ArrayList<>(Arrays.asList(model.getWorkstationCodes().split(",")));
        for (String scode : stationCodes) {
            {
                if (getAllDatas().stream().filter(c -> c.getWorkstationCodes().contains(scode)
                        && !Objects.equals(c.getId(), model.getId())).findFirst().orElse(null) != null) {
                    PmWorkStationEntity station = pmVersionProvider.getObjectedPm().getStations().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), scode))
                            .findFirst().orElse(null);
                    if (station != null) {
                        throw new InkelinkException(station.getWorkstationName() + "工位已进行了配置");
                    } else {
                        throw new InkelinkException(scode + "工位已进行了配置");
                    }
                }
            }
            removeCache();
        }
    }

    @Override
    public void afterUpdate(Wrapper<EpsSmartCutInoroutRelevanceEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     */
    @Override
    public List<EpsSmartCutInoroutRelevanceEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsSmartCutInoroutRelevanceEntity>> getDataFunc = (obj) -> {
                List<EpsSmartCutInoroutRelevanceEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsSmartCutInoroutRelevanceEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 获取工位列表
     *
     * @return
     */
    @Override
    public List<ComboInfoDTO> getCutInWorkstations() {
        List<EpsSmartCutInoroutRelevanceEntity> data = getAllDatas();
        List<ComboInfoDTO> comboInfos = new ArrayList<>();
        for (EpsSmartCutInoroutRelevanceEntity item : data) {
            String[] stationCodes = item.getWorkstationCodes().split(",");
            for (String stationCode : stationCodes) {
                ComboInfoDTO et = new ComboInfoDTO();
                et.setText(stationCode);
                et.setValue(item.getLastAviCode());
                comboInfos.add(et);
            }
        }
        comboInfos.sort(Comparator.comparing(ComboInfoDTO::getText));
        return comboInfos;
    }

    /**
     * 获取工位最近的下线站点
     *
     * @param workstationCode
     * @return
     */
    @Override
    public String getNextAviCode(String workstationCode) {
        EpsSmartCutInoroutRelevanceEntity data = getAllDatas().stream().filter(c -> c.getWorkstationCodes().contains(workstationCode)).findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("从配置中未找到工位" + workstationCode + "最近的下线站点");
        }
        return data.getNextAviCode();
    }
}