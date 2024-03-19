package com.ca.mfd.prc.pm.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.pm.communication.constant.ApiTypeConst;
import com.ca.mfd.prc.pm.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.pm.communication.entity.MidPmCountryEntity;
import com.ca.mfd.prc.pm.communication.mapper.IMidPmCountryMapper;
import com.ca.mfd.prc.pm.communication.service.IMidApiLogService;
import com.ca.mfd.prc.pm.communication.service.IMidPmCountryService;
import com.ca.mfd.prc.pm.service.IPmOrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 *
 * @Description: 国家代码中间表服务实现
 * @author inkelink
 * @date 2023年10月16日
 * @变更说明 BY inkelink At 2023年10月16日
 */
@Service
public class MidPmCountryServiceImpl extends AbstractCrudServiceImpl<IMidPmCountryMapper, MidPmCountryEntity> implements IMidPmCountryService {
    private static final Logger logger = LoggerFactory.getLogger(MidPmCountryServiceImpl.class);
    private static final Object lockObj = new Object();
    @Autowired
    private LocalCache localCache;
    private final String cacheName = "PRC_MID_PM_COUNTRY";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<MidPmCountryEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(MidPmCountryEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(MidPmCountryEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<MidPmCountryEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<MidPmCountryEntity> getAllDatas() {
        List<MidPmCountryEntity> datas = localCache.getObject(cacheName);
        if (datas == null || datas.isEmpty()) {
            synchronized (lockObj) {
                datas = localCache.getObject(cacheName);
                if (datas == null || datas.isEmpty()) {
                    datas = getData(null);
                    localCache.addObject(cacheName, datas);
                }
            }
        }
        return datas;
    }

    @Override
    public List<MidPmCountryEntity> getListByLog(Long id) {
        QueryWrapper<MidPmCountryEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidPmCountryEntity::getPrcMidApiLogId, id);
        return selectList(qry);
    }

    /**
     * 执行数据处理逻辑(考虑异步)
     *
     */
    @Override
    public void excute(String logid) {
        IMidApiLogService apiLogService = SpringContextUtils.getBean(IMidApiLogService.class);
        IPmOrganizationService organizationService = SpringContextUtils.getBean(IPmOrganizationService.class);

        List<MidApiLogEntity> apilogs = apiLogService.getDoList(ApiTypeConst.MAIN_COUNTRY, ConvertUtils.stringToLong(logid));
        if (apilogs == null || apilogs.isEmpty()) {
            return;
        }
        for (MidApiLogEntity apilog : apilogs) {
            boolean success = false;
            try {
                UpdateWrapper<MidApiLogEntity> uplogStart = new UpdateWrapper<>();
                uplogStart.lambda().set(MidApiLogEntity::getStatus, 4)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                apiLogService.update(uplogStart);
                apiLogService.saveChange();

                List<MidPmCountryEntity> datas = this.getListByLog(apilog.getId());
                organizationService.receiveData(datas);
                success = true;

            } catch (Exception exception) {
                logger.debug("数据保存异常：{}", exception.getMessage());
                apiLogService.clearChange();
            }
            try {
                UpdateWrapper<MidApiLogEntity> uplogEnd = new UpdateWrapper<>();
                uplogEnd.lambda().set(MidApiLogEntity::getStatus, success ? 5 : 6)
                        .eq(MidApiLogEntity::getId, apilog.getId());
                apiLogService.saveChange();
            } catch (Exception exception) {
                logger.debug("日志保存异常：{}", exception.getMessage());
            }
        }
    }
}