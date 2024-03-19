package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.eps.entity.EpsAssembleIndicateConfigEntity;
import com.ca.mfd.prc.eps.mapper.IEpsAssembleIndicateConfigMapper;
import com.ca.mfd.prc.eps.remote.app.pps.dto.FilterFetureExpressionPara;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.AnalysisFeatureProvider;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsAssembleIndicateConfigService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 装配指示配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsAssembleIndicateConfigServiceImpl extends AbstractCrudServiceImpl<IEpsAssembleIndicateConfigMapper, EpsAssembleIndicateConfigEntity> implements IEpsAssembleIndicateConfigService {
    private static final Logger logger = LoggerFactory.getLogger(EpsAssembleIndicateConfigServiceImpl.class);
    private static final String CACHE_NAME = "PRC_EPS_ASSEMBLE_INDICATE_CONFIG";
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private LocalCache localCache;
    @Autowired
    private AnalysisFeatureProvider analysisFeatureProvider;


    /**
     * 获取工位上面的装配指示列表
     *
     * @param sn              产品唯一码
     * @param workstationCode 工位代码
     * @return 列表 装配指示配置
     */
    @Override
    public List<EpsAssembleIndicateConfigEntity> getWorkstationData(String sn, String workstationCode) {
        // 根据产品唯一码获取订单，检查订单是否存在
        PpsOrderEntity ppsOrderRemoteRes = ppsOrderProvider.getPpsOrderInfoByKey(sn);
        if (ppsOrderRemoteRes == null) {
            throw new InkelinkException(String.format("%s无效的车辆信息", sn));
        }

        // 获取查询工位的装配指示配置
        List<EpsAssembleIndicateConfigEntity> allData = getAllData();
        List<EpsAssembleIndicateConfigEntity> data = allData.stream().filter(dataItem -> StringUtils.equals(workstationCode, dataItem.getWorkstationCode())).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(data)) {
            // 当前工位没有装配指示配置
            return data;
        }
        // 过滤符合当前工位的装配指示配置的特征
        FilterFetureExpressionPara featureExpressionReq = new FilterFetureExpressionPara();
        featureExpressionReq.setBarcode(sn);
        List<String> featureCode = data.stream().map(EpsAssembleIndicateConfigEntity::getFeatureCode)
                .collect(Collectors.toList());
        featureExpressionReq.setFetureExpressions(featureCode);
        List<String> featureCodes = analysisFeatureProvider.filterFeatureExpression(featureExpressionReq);
        // 返回符合当前工位指定特征的装配指示配置列表
        return data.stream().filter(dataItem -> featureCodes.contains(dataItem.getFeatureCode()))
                .sorted(Comparator.comparing(EpsAssembleIndicateConfigEntity::getSequenceNum))
                .collect(Collectors.toList());
    }

    /**
     * 缓存获取所有的装配指示配置
     *
     * @return 缓存种的装配指示配置
     */
    private List<EpsAssembleIndicateConfigEntity> getAllData() {
        try {
            Function<Object, ? extends List<EpsAssembleIndicateConfigEntity>> getDataFunc = obj -> {
                List<EpsAssembleIndicateConfigEntity> list = getData(null);
                if (CollectionUtils.isEmpty(list)) {
                    return new ArrayList<>();
                }
                return list;
            };
            List<EpsAssembleIndicateConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
    public void afterDelete(Wrapper<EpsAssembleIndicateConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(EpsAssembleIndicateConfigEntity model) {
        verifyExpressWithThrow(model);
    }

    @Override
    public void afterUpdate(EpsAssembleIndicateConfigEntity model) {
        verifyExpressWithThrow(model);

    }

    @Override
    public void afterUpdate(Wrapper<EpsAssembleIndicateConfigEntity> updateWrapper) {
        removeCache();
    }

    private void verifyExpressWithThrow(EpsAssembleIndicateConfigEntity model) {
        if (FeatureTool.verifyExpression(model.getFeatureCode())) {
            removeCache();
        } else {
            throw new InkelinkException("特征表达式格式有错误");
        }
    }

    private void removeCache() {
        localCache.removeObject(CACHE_NAME);
    }
}