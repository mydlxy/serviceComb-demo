package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.pps.entity.PpsRinStandardConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsRinTimeConfigEntity;
import com.ca.mfd.prc.pps.mapper.IPpsRinStandardConfigMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.service.IPpsRinStandardConfigService;
import com.ca.mfd.prc.pps.service.IPpsRinTimeConfigService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

/**
 * @author eric.zhou
 * @Description: 电池RIN码前14位配置服务实现
 * @date 2023年08月21日
 * @变更说明 BY eric.zhou At 2023年08月21日
 */
@Service
public class PpsRinStandardConfigServiceImpl extends AbstractCrudServiceImpl<IPpsRinStandardConfigMapper, PpsRinStandardConfigEntity> implements IPpsRinStandardConfigService {

    private static final Logger logger = LoggerFactory.getLogger(PpsRinStandardConfigServiceImpl.class);

    private static final Integer VALID_CODE_LEN = 14;
    private static final String CACHE_NAME = "PRC_PPS_RIN_STANDARD_CONFIG";
    private static final Object LOCK_OBJ = new Object();
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IPpsRinTimeConfigService ppsRinTimeConfigService;
    @Autowired
    private LocalCache localCache;

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    @Override
    public void afterDelete(Wrapper<PpsRinStandardConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PpsRinStandardConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsRinStandardConfigEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsRinStandardConfigEntity> updateWrapper) {
        removeCache();
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<PpsRinStandardConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<PpsRinStandardConfigEntity>> getDataFunc = (obj) -> {
                List<PpsRinStandardConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<PpsRinStandardConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 生成RIN码
     *
     * @param model
     * @return
     */
    @Override
    public String generateRin(String model) {
        StringBuilder sb = new StringBuilder();
        //获取rin码配置
        PpsRinStandardConfigEntity rinConfigData = getAllDatas().stream().filter(o -> StringUtils.equals(o.getPackModel(), model)).findFirst().orElse(null);
        if (rinConfigData == null) {
            throw new InkelinkException("电池类型" + model + "没有配置RIN码前14位");
        }
        sb.append(rinConfigData.getRinCode());
        List<PpsRinTimeConfigEntity> ressDateConfig = ppsRinTimeConfigService.getAllDatas();
        //第六部分 生产日期 年
        PpsRinTimeConfigEntity yearCode = ressDateConfig.stream().filter(t -> t.getPosition() == 17
                && StringUtils.equals(t.getDateValue(), DateUtils.format(new Date(), t.getDatepart()))
                && StringUtils.equals(t.getPackModel(), model)).findFirst().orElse(null);

        if (yearCode == null) {
            throw new InkelinkException("未配置年份代码");
        }
        sb.append(yearCode.getTimeCode());
        //第六部分 生产日期 月
        PpsRinTimeConfigEntity monthCode = ressDateConfig.stream().filter(t -> t.getPosition() == 18
                && StringUtils.equals(StringUtils.leftPad(t.getDateValue(), 2, '0'), DateUtils.format(new Date(), t.getDatepart()))
                && StringUtils.equals(t.getPackModel(), model)).findFirst().orElse(null);

        if (monthCode == null) {
            throw new InkelinkException("未配置月份代码");
        }
        sb.append(monthCode.getTimeCode());
        //第六部分 生产日期 日
        PpsRinTimeConfigEntity dayCode = ressDateConfig.stream().filter(t -> t.getPosition() == 19
                && StringUtils.equals(StringUtils.leftPad(t.getDateValue(), 2, '0'), DateUtils.format(new Date(), t.getDatepart()))
                && StringUtils.equals(t.getPackModel(), model)).findFirst().orElse(null);
        if (dayCode == null) {
            throw new InkelinkException("未配置日期代码");
        }
        sb.append(dayCode.getTimeCode());
        //第七部分 生产序号
        String sequenceNo = sysSnConfigProvider.createSn(model + "_Rin");
        sb.append(sequenceNo);
        return sb.toString();
    }

    @Override
    public void beforeInsert(PpsRinStandardConfigEntity model) {
        valid(model);
    }

    @Override
    public void beforeUpdate(PpsRinStandardConfigEntity model) {
        valid(model);
    }

    private void valid(PpsRinStandardConfigEntity model) {
        if (model.getRinCode() == null || model.getRinCode().length() != VALID_CODE_LEN) {
            throw new InkelinkException("RIN码前14位长度不对");
        }
        QueryWrapper<PpsRinStandardConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsRinStandardConfigEntity::getPackModel, model.getPackModel())
                .ne(PpsRinStandardConfigEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("物料号“" + model.getPackModel() + "”已经配置过RIN码前14位");
        }
    }

}