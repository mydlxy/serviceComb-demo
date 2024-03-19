package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.eps.mapper.IEpsMaterialCutLogMapper;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmProductMaterialMasterProvider;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmTraceComponentProvider;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsMaterialCutLogService;
import com.ca.mfd.prc.eps.entity.EpsMaterialCutLogEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * @author inkelink
 * @Description: 生产物料切换记录服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsMaterialCutLogServiceImpl extends AbstractCrudServiceImpl<IEpsMaterialCutLogMapper, EpsMaterialCutLogEntity> implements IEpsMaterialCutLogService {
    private static final Logger logger = LoggerFactory.getLogger(EpsMaterialCutLogServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_MATERIAL_CUT_LOG";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private PmTraceComponentProvider pmTraceComponentProvider;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private PmProductMaterialMasterProvider pmProductMaterialMasterProvider;

    @Override
    public void afterInsert(EpsMaterialCutLogEntity entity) {
        PmAllDTO pmall = pmVersionProvider.getObjectedPm();
        PmWorkStationEntity stationInfo = pmall.getStations().stream().filter(c -> StringUtils.equals(c.getWorkstationCode(), entity.getWorkstationCode())).findFirst().orElse(null);
        if (stationInfo == null) {
            throw new InkelinkException(entity.getWorkstationCode() + "无效的生产工位");
        }
        entity.setWorkstationName(stationInfo.getWorkstationName());
        List<PpsOrderEntity> orderInfos = ppsOrderProvider.getTopOrderByCodeLike(2, entity.getBarcode());
        if (orderInfos == null || orderInfos.size() == 0) {
            throw new InkelinkException(entity.getBarcode() + "未找到对应的产品");
        }
        if (orderInfos.size() != 1) {
            throw new InkelinkException(entity.getBarcode() + "找到多条产品信息，请输入更明确的条码");
        }
        entity.setBarcode(orderInfos.get(0).getBarcode());
        if (getTopMateriaData(entity.getOldMaterialNo()) == null) {
            throw new InkelinkException("物料主数据中未找到" + entity.getOldMaterialNo() + "原物料的信息");
        }
        PmProductMaterialMasterEntity newMat = getTopMateriaData(entity.getNewMaterialNo());
        if (newMat == null) {
            throw new InkelinkException("物料主数据中未找到" + entity.getNewMaterialNo() + "新物料的信息");
        }
        pmTraceComponentProvider.saveByBom(entity.getNewMaterialNo(), newMat.getMaterialCn());
        removeCache();
    }

    private PmProductMaterialMasterEntity getTopMateriaData(String materialNo) {
        PageDataDto data = new PageDataDto();
        data.setConditions(new ArrayList<>());
        data.getConditions().add(new ConditionDto(MpSqlUtils.getColumnName(PmProductMaterialMasterEntity::getMaterialNo), materialNo, ConditionOper.Equal));
        data.setPageIndex(1);
        data.setPageSize(1);
        PageData<PmProductMaterialMasterEntity> pdata = pmProductMaterialMasterProvider.getPageData(data);
        if (pdata.getDatas() != null && pdata.getDatas().size() > 0) {
            return pdata.getDatas().get(0);
        } else {
            return null;
        }
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterUpdate(EpsMaterialCutLogEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsMaterialCutLogEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Wrapper<EpsMaterialCutLogEntity> queryWrapper) {
        removeCache();
    }

    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }

    /**
     * 获取所有的数据
     *
     * @return
     */
    @Override
    public List<EpsMaterialCutLogEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsMaterialCutLogEntity>> getDataFunc = (obj) -> {
                List<EpsMaterialCutLogEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsMaterialCutLogEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 获取未发送的数据
     *
     * @return
     */
    @Override
    public List<EpsMaterialCutLogEntity> getNotSendData() {
        QueryWrapper<EpsMaterialCutLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsMaterialCutLogEntity::getIsInform, false).orderByAsc(EpsMaterialCutLogEntity::getCreationDate);
        return selectList(qry);
    }
}