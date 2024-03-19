package com.ca.mfd.prc.pqs.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pqs.dto.QgRiskOperInfo;
import com.ca.mfd.prc.pqs.dto.RiskProductFilterDto;
import com.ca.mfd.prc.pqs.dto.RiskProductListFilterInfo;
import com.ca.mfd.prc.pqs.dto.RiskRepairInfo;
import com.ca.mfd.prc.pqs.dto.SubmitRiskInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskDetailEntity;
import com.ca.mfd.prc.pqs.entity.PqsDefectAnomalyRiskEntity;
import com.ca.mfd.prc.pqs.mapper.IPqsDefectAnomalyRiskMapper;
import com.ca.mfd.prc.pqs.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pqs.remote.app.pps.provider.PpsOrderProvider;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskDetailService;
import com.ca.mfd.prc.pqs.service.IPqsDefectAnomalyRiskService;
import com.ca.mfd.prc.pqs.service.IPqsLogicService;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 质量围堵服务实现
 * @date 2023年09月07日
 * @变更说明 BY inkelink At 2023年09月07日
 */
@Service
public class PqsDefectAnomalyRiskServiceImpl extends AbstractCrudServiceImpl<IPqsDefectAnomalyRiskMapper, PqsDefectAnomalyRiskEntity> implements IPqsDefectAnomalyRiskService {

    private final static String RISK_NO = "RiskNo";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IPqsDefectAnomalyRiskMapper pqsDefectAnomalyRiskMapper;
    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private PpsOrderProvider ppsOrderProvider;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private IPqsDefectAnomalyRiskDetailService pqsDefectAnomalyRiskDetailService;
    @Autowired
    private IPqsLogicService pqsLogicService;
    private final String cacheName = "PRC_PQS_DEFECT_ANOMALY_RISK";

    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<PqsDefectAnomalyRiskEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PqsDefectAnomalyRiskEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PqsDefectAnomalyRiskEntity entity) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PqsDefectAnomalyRiskEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public List<PqsDefectAnomalyRiskEntity> getAllDatas() {
        Function<Object, ? extends List<PqsDefectAnomalyRiskEntity>> getDataFunc = (obj) -> {
            List<PqsDefectAnomalyRiskEntity> lst = getData(null);
            if (lst == null || lst.size() == 0) {
                return new ArrayList<>();
            }
            return lst;
        };
        List<PqsDefectAnomalyRiskEntity> caches = localCache.getObject(cacheName, getDataFunc, -1);
        if (caches == null) {
            return new ArrayList<>();
        }
        return caches;
    }

    /**
     * 获取问题排查车辆列表
     *
     * @param info
     * @return
     */
    @Override
    public List<RiskProductFilterDto> getProductList(RiskProductListFilterInfo info) {

        Map<String, Object> maps = new HashMap<>(6);
        maps.put("aviCode", info.getAviCode());
        maps.put("startDt", DateUtils.format(info.getStartDt(), DateUtils.DATE_TIME_PATTERN));
        maps.put("endDt", DateUtils.format(info.getEndDt(), DateUtils.DATE_TIME_PATTERN));
        maps.put("model", info.getModel());
        maps.put("materialNo", info.getMaterialNo());
        maps.put("category", info.getCategory());

        return pqsDefectAnomalyRiskMapper.getProductList(maps);
    }

    /**
     * 问题立项
     *
     * @param info
     * @return
     */
    @Override
    public String submitRiskInfo(SubmitRiskInfo info) {

        if (CollectionUtil.isEmpty(info.getSnList())) {
            throw new InkelinkException("输入信息不正确");
        }

        Date activeDate = new Date();
        String riskNo = sysSnConfigProvider.createSn(RISK_NO);
        PqsDefectAnomalyRiskEntity riskEntity = new PqsDefectAnomalyRiskEntity();
        riskEntity.setId(IdGenerator.getId());
        riskEntity.setRiskNo(riskNo);
        riskEntity.setDefectAnomalyCode(info.getDefectAnomalyCode());
        riskEntity.setDefectAnomalyDescription(info.getDefectAnomalyDescription());
        riskEntity.setCategory(info.getCategory());
        riskEntity.setModel(info.getModel());
        riskEntity.setRemark(info.getRemark());
        riskEntity.setStartDt(activeDate);
        riskEntity.setStartBy(identityHelper.getUserName());
        riskEntity.setCloseBy(Strings.EMPTY);
        riskEntity.setStatus(1);

        List<PqsDefectAnomalyRiskDetailEntity> riskDetailList = Lists.newArrayList();
        StringBuilder stringBuilder = new StringBuilder();
        for (String sn : info.getSnList()) {
            // 3:铸造，4:机加
            PqsDefectAnomalyRiskDetailEntity riskDetailEntity = new PqsDefectAnomalyRiskDetailEntity();
            if (info.getCategory() != 3 && info.getCategory() != 4) {
                PpsOrderEntity orderEntity = ppsOrderProvider.getPpsOrderInfoByKey(sn);
                if (orderEntity == null) {
                    stringBuilder.append(sn).append("未找到对应信息");
                    continue;
                }
                riskDetailEntity.setSn(orderEntity.getSn());
                riskDetailEntity.setInventoryCheck(orderEntity.getOrderStatus() == 99);
            } else {
                riskDetailEntity.setSn(sn);
                riskDetailEntity.setInventoryCheck(false);
            }
            riskDetailEntity.setRiskNo(riskEntity.getRiskNo());
            riskDetailEntity.setDefectAnomalyCode(info.getDefectAnomalyCode());
            riskDetailEntity.setDefectAnomalyDescription(info.getDefectAnomalyDescription());
            riskDetailEntity.setActiveBy(identityHelper.getUserName());
            riskDetailEntity.setStartDt(activeDate);
            riskDetailEntity.setActiveDt(activeDate);
            riskDetailEntity.setInventorySendStatus(0);
            riskDetailEntity.setStatus(1);

            // 物料暂时不处理
            riskDetailEntity.setDmsSendStatus(0);
            riskDetailEntity.setDmsCheck(false);
            riskDetailEntity.setIsActived(false);
            riskDetailEntity.setCategory(info.getCategory());
            riskDetailList.add(riskDetailEntity);
        }

        insert(riskEntity);
        pqsDefectAnomalyRiskDetailService.insertBatch(riskDetailList, riskDetailList.size());

        return stringBuilder.toString();
    }

    /**
     * 根据问题ID 激活缺陷
     *
     * @param id              问题ID
     * @param workstationCode 工位代码
     */
    @Override
    public void triggerAnomalyByRiskId(Long id, String workstationCode) {

        // 查找问题
        PqsDefectAnomalyRiskEntity riskEntity = get(id);
        if (riskEntity == null) {
            throw new InkelinkException("未找到对应问题信息");
        }

        // 问题明细
        List<PqsDefectAnomalyRiskDetailEntity> riskDetailEntityList = pqsDefectAnomalyRiskDetailService.getAllDatas()
                .stream().filter(p -> StringUtils.equals(p.getRiskNo(), riskEntity.getRiskNo()) && p.getStatus() == 1)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(riskDetailEntityList)) {
            return;
        }

        // 遍历明细，激活问题
        riskDetailEntityList.forEach(r -> {

            QgRiskOperInfo info = new QgRiskOperInfo();
            info.setId(r.getId());
            info.setCloseRemark("");
            info.setOperType(20);
            info.setWorkstationCode(workstationCode);
            pqsLogicService.riskManager(info);
        });
    }

    /**
     * 根据问题明细-批量激活缺陷
     *
     * @param ids             问题明细信息id
     * @param workstationCode
     */
    @Override
    public void triggerAnomalyByRiskDetailId(List<Long> ids, String workstationCode) {

        // 问题明细
        List<PqsDefectAnomalyRiskDetailEntity> riskDetails = pqsDefectAnomalyRiskDetailService.getAllDatas()
                .stream().filter(p -> ids.contains(p.getId()) && p.getStatus() == 1)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(riskDetails)) {
            return;
        }

        // 遍历明细，激活问题
        riskDetails.forEach(r -> {

            QgRiskOperInfo info = new QgRiskOperInfo();
            info.setId(r.getId());
            info.setCloseRemark("");
            info.setOperType(20);
            info.setWorkstationCode(workstationCode);
            pqsLogicService.riskManager(info);
        });
    }

    /**
     * 根据问题ID 批量修复缺陷
     *
     * @param info 问题信息
     */
    @Override
    public void repairByRiskId(RiskRepairInfo info) {

        info.getIds().forEach(i -> {
            // 查找问题
            PqsDefectAnomalyRiskEntity riskEntity = get(i);
            if (riskEntity == null) {
                throw new InkelinkException("未找到对应问题信息");
            }

            // 问题明细
            List<PqsDefectAnomalyRiskDetailEntity> riskDetailEntityList = pqsDefectAnomalyRiskDetailService.getAllDatas()
                    .stream().filter(p -> p.getRiskNo().equals(riskEntity.getRiskNo()) && p.getStatus() == 20)
                    .collect(Collectors.toList());
            if (CollectionUtil.isEmpty(riskDetailEntityList)) {
                return;
            }

            // 遍历明细，激活问题
            riskDetailEntityList.forEach(r -> {

                // 释放问题
                QgRiskOperInfo operInfo = new QgRiskOperInfo();
                operInfo.setId(r.getId());
                operInfo.setCloseRemark("后台批量修复问题关闭");
                operInfo.setOperType(30);
                operInfo.setRepairActivity(info.getRepairActivity());
                pqsLogicService.riskManager(operInfo);
            });
        });
    }

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param info 修复信息
     */
    @Override
    public void repairByRiskDetailId(RiskRepairInfo info) {

        // 问题明细
        List<PqsDefectAnomalyRiskDetailEntity> riskDetailEntityList = pqsDefectAnomalyRiskDetailService.getAllDatas()
                .stream().filter(p -> info.getIds().contains(p.getId()) && p.getStatus() == 20)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(riskDetailEntityList)) {
            return;
        }

        // 遍历明细，激活问题
        riskDetailEntityList.forEach(r -> {

            // 释放问题
            QgRiskOperInfo operInfo = new QgRiskOperInfo();
            operInfo.setId(r.getId());
            operInfo.setCloseRemark("后台批量修复问题关闭");
            operInfo.setOperType(30);
            operInfo.setRepairActivity(info.getRepairActivity());
            pqsLogicService.riskManager(operInfo);
        });
    }

    /**
     * 根据问题明细ID 批量修复缺陷
     *
     * @param info 修复信息
     */
    @Override
    public void recheck(RiskRepairInfo info) {

        info.getIds().forEach(i -> {
            // 查找问题
            PqsDefectAnomalyRiskEntity riskEntity = get(i);
            if (riskEntity == null) {
                throw new InkelinkException("未找到对应问题信息");
            }
            releaseAllRiskDetail(riskEntity.getRiskNo());
        });
    }

    /**
     * 关闭问题
     *
     * @param info
     */
    @Override
    public void closeRisk(RiskRepairInfo info) {

        info.getIds().forEach(i -> {
            // 查找问题
            PqsDefectAnomalyRiskEntity riskEntity = get(i);
            if (riskEntity == null) {
                throw new InkelinkException("未找到对应问题信息");
            }
            releaseAllRiskDetail(riskEntity.getRiskNo());

            // 关闭问题
            riskEntity.setCloseBy(identityHelper.getUserName());
            riskEntity.setCloseDt(new Date());
            riskEntity.setStatus(90);
            update(riskEntity);
        });
    }

    /**
     * 释放所有问题
     *
     * @param riskNo
     */
    private void releaseAllRiskDetail(String riskNo) {
        // 问题明细
        List<PqsDefectAnomalyRiskDetailEntity> riskDetailEntityList = pqsDefectAnomalyRiskDetailService.getAllDatas()
                .stream().filter(p -> p.getRiskNo().equals(riskNo) && p.getStatus() != 90)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(riskDetailEntityList)) {
            return;
        }

        // 遍历明细，激活问题
        riskDetailEntityList.forEach(r -> {

            // 释放问题
            QgRiskOperInfo operInfo = new QgRiskOperInfo();
            operInfo.setId(r.getId());
            operInfo.setCloseRemark("后台批量修复问题关闭");
            operInfo.setOperType(90);
            pqsLogicService.riskManager(operInfo);
        });
    }
}