package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.pps.entity.PpsRinTimeConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsRinTimeConfigMapper;
import com.ca.mfd.prc.pps.service.IPpsRinTimeConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * @author eric.zhou
 * @Description: 电池RIN码时间配置服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsRinTimeConfigServiceImpl extends AbstractCrudServiceImpl<IPpsRinTimeConfigMapper, PpsRinTimeConfigEntity> implements IPpsRinTimeConfigService {

    private static final Logger logger = LoggerFactory.getLogger(PpsRinTimeConfigServiceImpl.class);

    private static final String CACHE_NAME = "PRC_PPS_RIN_TIME_CONFIG";
    private static final Object LOCK_OBJ = new Object();
    private final Map<String, String> orderDic = new LinkedHashMap<>();
    @Autowired
    private LocalCache localCache;

    {
        orderDic.put(MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getPackModel), "电池类型");
        orderDic.put(MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getDatepart), "日期编码");
        orderDic.put(MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getPosition), "填充下标");

        orderDic.put(MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getDateValue), "日期值");
        orderDic.put(MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getTimeCode), "时间代码");
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<PpsRinTimeConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsRinTimeConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsRinTimeConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsRinTimeConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<PpsRinTimeConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<PpsRinTimeConfigEntity>> getDataFunc = (obj) -> {
                List<PpsRinTimeConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<PpsRinTimeConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
    public Map<String, String> getExcelColumnNames() {
        return orderDic;
    }

    @Override
    public void validImportDatas(List<Map<String, String>> datas, Map<String, String> fieldParam) {
        super.validImportDatas(datas, fieldParam);
        //验证必填
        for (int i = 0; i < datas.size(); i++) {
            Map<String, String> data = datas.get(i);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getPackModel), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getDatepart), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getPosition), i + 1);

            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getDateValue), i + 1);
            validExcelDataRequire(fieldParam, data, MpSqlUtils.getColumnName(PpsRinTimeConfigEntity::getTimeCode), i + 1);
        }
    }

    void validExcelDataRequire(Map<String, String> fieldParam, Map<String, String> data, String col, int rowIndex) {
        String columnName = fieldParam.get(col);
        String val = data.get(col);
        if (StringUtils.isBlank(val)) {
            throw new InkelinkException("第“" + rowIndex + "”行，“" + columnName + "”列：不能为空");
        }
    }
}