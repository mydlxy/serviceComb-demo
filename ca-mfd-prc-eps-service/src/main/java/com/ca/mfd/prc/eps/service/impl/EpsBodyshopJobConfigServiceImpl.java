package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.FeatureTool;
import com.ca.mfd.prc.eps.dto.VehicleJobDetails;
import com.ca.mfd.prc.eps.dto.VehicleJobInfo;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobConfigEntity;
import com.ca.mfd.prc.eps.entity.EpsBodyshopJobDetailsEntity;
import com.ca.mfd.prc.eps.mapper.IEpsBodyshopJobConfigMapper;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.eps.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.eps.service.IEpsBodyshopJobConfigService;
import com.ca.mfd.prc.eps.service.IEpsBodyshopJobDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 焊装车间执行码配置服务实现
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@Service
public class EpsBodyshopJobConfigServiceImpl extends AbstractCrudServiceImpl<IEpsBodyshopJobConfigMapper, EpsBodyshopJobConfigEntity> implements IEpsBodyshopJobConfigService {

    private static final Logger logger = LoggerFactory.getLogger(EpsBodyshopJobConfigServiceImpl.class);

    private static final String CACHE_NAME = "PRC_EPS_BODYSHOP_JOB_CONFIG";
    @Autowired
    private LocalCache localCache;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    @Autowired
    private IEpsBodyshopJobDetailsService epsBodyshopJobDetailsService;

    @Override
    public void afterDelete(Wrapper<EpsBodyshopJobConfigEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        for (Serializable item : idList) {
            UpdateWrapper<EpsBodyshopJobDetailsEntity> delUp = new UpdateWrapper<>();
            delUp.lambda().eq(EpsBodyshopJobDetailsEntity::getEpsBodyshopJobConfigId, item);
            epsBodyshopJobDetailsService.delete(delUp, true);
        }
        removeCache();
    }

    @Override
    public void afterInsert(EpsBodyshopJobConfigEntity model) {
        verifyExpressWithThrow(model, false);

        QueryWrapper<EpsBodyshopJobDetailsEntity> delQry = new QueryWrapper<>();
        delQry.lambda().eq(EpsBodyshopJobDetailsEntity::getEpsBodyshopJobConfigId, 0L);

        List<EpsBodyshopJobDetailsEntity> details = epsBodyshopJobDetailsService.getData(delQry, false).stream().map(c -> {
            EpsBodyshopJobDetailsEntity dto = new EpsBodyshopJobDetailsEntity();
            dto.setEpsBodyshopJobConfigId(model.getId());
            dto.setLineCode(c.getLineCode());
            dto.setMaterialName(c.getMaterialName());
            dto.setMaterialCode(c.getMaterialCode());
            dto.setJobValue("");
            return dto;

        }).collect(Collectors.toList());
        //epsBodyshopJobDetailsService.insertBatch(details);

        removeCache();
    }

    @Override
    public void afterUpdate(EpsBodyshopJobConfigEntity model) {
        verifyExpressWithThrow(model, true);
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<EpsBodyshopJobConfigEntity> updateWrapper) {
        removeCache();
    }

    private void verifyExpressWithThrow(EpsBodyshopJobConfigEntity model,Boolean isUpdate) {
        QueryWrapper<EpsBodyshopJobConfigEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<EpsBodyshopJobConfigEntity> qryLmp = qry.lambda();
        qryLmp.eq(EpsBodyshopJobConfigEntity::getVehicleModel, model.getVehicleModel());
        if (isUpdate) {
            qryLmp.ne(EpsBodyshopJobConfigEntity::getId, model.getId());
        }
        if (selectCount(qry) > 0) {
            throw new InkelinkException(String.format("车系%s已经存在", model.getVehicleModel()));
        }
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
    public List<EpsBodyshopJobConfigEntity> getAllDatas() {
        try {
            Function<Object, ? extends List<EpsBodyshopJobConfigEntity>> getDataFunc = (obj) -> {
                List<EpsBodyshopJobConfigEntity> lst = getData(null);
                if (lst == null || lst.size() == 0) {
                    return new ArrayList<>();
                }
                return lst;
            };
            List<EpsBodyshopJobConfigEntity> caches = localCache.getObject(CACHE_NAME, getDataFunc, -1);
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
     * 获取车辆各个区域的执行码
     *
     * @param sn
     * @return
     */
    @Override
    public VehicleJobInfo getJobConfigBySn(String sn) {

        VehicleJobInfo info = new VehicleJobInfo();
        PpsOrderEntity orderInfo = ppsOrderProvider.getPpsOrderInfoByKey(sn);
        if (orderInfo == null) {
            throw new InkelinkException("无效的车辆VIN：" + sn);
        }

        EpsBodyshopJobConfigEntity data = getAllDatas().stream().filter(c ->
                        StringUtils.equals(c.getVehicleModel(), orderInfo.getModel()))
                .findFirst().orElse(null);
        if (data == null) {
            throw new InkelinkException("焊装车系码配置中未找到对应的车系" + orderInfo.getModel());
        }

        info.setSn(sn);
        info.setModel(orderInfo.getModel());

        List<PmProductBomEntity> boms = ppsOrderProvider.getOrderBomByOrderId(orderInfo.getId());

        List<EpsBodyshopJobDetailsEntity> details = epsBodyshopJobDetailsService.getAllDatas()
                .stream().filter(c-> Objects.equals(c.getEpsBodyshopJobConfigId(),data.getId()))
                .collect(Collectors.toList());
        if (details.isEmpty()) {
            details = epsBodyshopJobDetailsService.getAllDatas()
                    .stream().filter(c -> Objects.equals(c.getEpsBodyshopJobConfigId(), 0L))
                    .collect(Collectors.toList());
        }
        for (EpsBodyshopJobDetailsEntity item : details) {
            if (StringUtils.isBlank(item.getMaterialCode())) {
                continue;
            }
            String job = StringUtils.EMPTY;
            PmProductBomEntity bomInfo = boms.stream().filter(c -> StringUtils.startsWith(c.getMaterialNo(), item.getMaterialCode()))
                    .findFirst().orElse(null);
            if (bomInfo != null)
            {
                //job = StringUtils.substring(bomInfo.getMaterialNo(), bomInfo.getMaterialNo().length() - 2, bomInfo.getMaterialNo().length());
                job = StringUtils.substring(bomInfo.getMaterialNo(), item.getMaterialCode().length() + 3, item.getMaterialCode().length() + 5);
                //job = bomInfo.MaterialNo.Substring(item.MaterialCode.Length + 3, 2);
            }
            else
            {
                job = item.getJobValue();
            }

            if (info.getDetails() == null) {
                info.setDetails(new ArrayList<>());
            }
            VehicleJobDetails de = new VehicleJobDetails();
            de.setLineCode(item.getLineCode());
            de.setMaterialName(item.getMaterialName());
            de.setMaterialCode(item.getMaterialCode());
            de.setJob(data.getStateCode() + job);
            info.getDetails().add(de);
        }

        return info;
    }

}