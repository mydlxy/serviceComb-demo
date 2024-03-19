package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.entity.PpsProductProcessAviEntity;
import com.ca.mfd.prc.pps.mapper.IPpsProductProcessAviMapper;
import com.ca.mfd.prc.pps.remote.app.pm.dto.AviInfoDTO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.pps.service.IPpsProductProcessAviService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author inkelink ${email}
 * @Description: 工艺路径详细
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PpsProductProcessAviServiceImpl extends AbstractCrudServiceImpl<IPpsProductProcessAviMapper, PpsProductProcessAviEntity> implements IPpsProductProcessAviService {

    private final String cacheName = "PRC_PPS_PRODUCT_PROCESS_AVI";
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private LocalCache localCache;

    /**
     * 清空缓存
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    /**
     * 数据验证
     */
    private void verfyOperation(PpsProductProcessAviEntity model) {
        if (model.getPrcPpsProductProcessId() == 0) {
            throw new InkelinkException("没有选择工艺路径设置主表！");
        }
        if (StringUtils.isBlank(model.getLineCode())) {
            throw new InkelinkException("请选择线体");
        }
        PmAllDTO pmAll = pmVersionProvider.getObjectedPm();
        PmLineEntity area = pmAll.getLines().stream().filter(o -> StringUtils.equals(o.getLineCode(), model.getLineCode()))
                .findFirst().orElse(null);
        if (area == null) {
            throw new InkelinkException("请选择线体");
        }
        model.setLineCode(area.getLineCode());
        model.setLineName(area.getLineName());
        if (!StringUtils.isBlank(model.getAviCode())) {
            PmAviEntity avi = pmAll.getAvis().stream().filter(o -> StringUtils.equals(o.getAviCode(), model.getAviCode()))
                    .findFirst().orElse(null);
            if (avi != null) {
                model.setAviCode(avi.getAviCode());
                model.setAviName(avi.getAviName());
            }
        }
        QueryWrapper<PpsProductProcessAviEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsProductProcessAviEntity::getPrcPpsProductProcessId, model.getPrcPpsProductProcessId())
                .eq(PpsProductProcessAviEntity::getLineCode, model.getLineCode())
                .eq(PpsProductProcessAviEntity::getAviCode, model.getAviCode())
                .ne(PpsProductProcessAviEntity::getId, model.getId());
        if (selectCount(qry) > 0) {
            throw new InkelinkException("线体“" + model.getLineCode() + "”，AVI“" + model.getAviCode() + "”已经配置过");
        }
    }

    @Override
    public void afterInsert(PpsProductProcessAviEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PpsProductProcessAviEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PpsProductProcessAviEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterDelete(Wrapper<PpsProductProcessAviEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PpsProductProcessAviEntity model) {
        verfyOperation(model);
    }

    @Override
    public void beforeUpdate(PpsProductProcessAviEntity model) {
        verfyOperation(model);
    }


    @Override
    public List<PpsProductProcessAviEntity> getByProductProcessId(Long productProcessId) {
        QueryWrapper<PpsProductProcessAviEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsProductProcessAviEntity::getPrcPpsProductProcessId, productProcessId)
                .orderByAsc(PpsProductProcessAviEntity::getDisplayNo);
        return selectList(qry);
    }

    /**
     * 获取AVI站点已设置列表
     *
     * @param productProcessId
     * @return
     */
    @Override
    public List<PpsProductProcessAviEntity> getSetAviInfos(Long productProcessId) {
        List<PpsProductProcessAviEntity> ppsProductProcessAviList = this.getData(Arrays.asList(new ConditionDto("PRC_PPS_PRODUCT_PROCESS_ID", productProcessId.toString(), ConditionOper.Equal)),
                Arrays.asList(new SortDto("DISPLAY_NO", ConditionDirection.ASC)), false);
        if (ppsProductProcessAviList.isEmpty()) {
            return Collections.emptyList();
        }
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();
        Map<String, String> pmShopCodeAndNameMap = pmAllDTO.getShops().stream().collect(Collectors.toMap(PmWorkShopEntity::getWorkshopName, PmWorkShopEntity::getWorkshopName));
        Map<String, String> pmAviCodeAndNameMap = pmAllDTO.getAvis().stream().collect(Collectors.toMap(PmAviEntity::getAviCode, PmAviEntity::getAviName));
        ppsProductProcessAviList.stream().forEach(e -> {
            String shopName = pmShopCodeAndNameMap.get(e.getWorkshopCode());
            if (StringUtils.isNotBlank(shopName)) {
                e.setWorkshopName(shopName);
            }
            String aviName = pmAviCodeAndNameMap.get(e.getAviCode());
            if (StringUtils.isNotBlank(aviName)) {
                e.setAviName(aviName);
            }
        });
        return ppsProductProcessAviList;
    }

    /**
     * 获取未设置的AVI点
     *
     * @param productProcessId
     * @return
     */
    @Override
    public List<AviInfoDTO> getNoSetAviInfo(Long productProcessId) {
        QueryWrapper<PpsProductProcessAviEntity> qw = new QueryWrapper<>();
        qw.lambda().eq(PpsProductProcessAviEntity::getPrcPpsProductProcessId, productProcessId);
        List<String> aviCodesList = this.selectList(qw).stream().map(PpsProductProcessAviEntity::getAviCode).collect(Collectors.toList());
        PmAllDTO pmAllDTO = pmVersionProvider.getObjectedPm();
        List<PmAviEntity> aviInfos = pmAllDTO.getAvis().stream()
                .filter(e -> !aviCodesList.contains(e.getAviCode())).collect(Collectors.toList());

        List<AviInfoDTO> datas = new ArrayList<>(aviInfos.size());
        for (PmAviEntity item : aviInfos) {
            AviInfoDTO info = new AviInfoDTO();
            info.setPmAviName(item.getAviName());
            info.setPmAviCode(item.getAviCode());
            info.setPmAviId(item.getId());
            PmLineEntity areaInfo = pmAllDTO.getLines().stream().filter(c -> Objects.equals(c.getId(), item.getPrcPmLineId())).findFirst().orElse(null);
            if (areaInfo != null) {
                info.setPmLineName(areaInfo.getLineName());
                info.setPmLineCode(areaInfo.getLineCode());
            }
            PmWorkShopEntity shopInfo = pmAllDTO.getShops().stream().filter(c -> Objects.equals(c.getId(), item.getPrcPmWorkshopId())).findFirst().orElse(null);
            if (shopInfo != null) {
                info.setPmShopName(shopInfo.getWorkshopName());
                info.setPmShopCode(shopInfo.getWorkshopCode());
            }
            datas.add(info);
        }
        return datas;
    }
}