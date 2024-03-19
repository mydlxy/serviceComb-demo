package com.ca.mfd.prc.avi.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.entity.AviPointVailSetEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.remote.app.pqs.provider.PqsdefectanomalyProvider;
import com.ca.mfd.prc.avi.entity.AviDefectAnomalyAviEntity;
import com.ca.mfd.prc.avi.mapper.IAviDefectAnomalyAviMapper;
import com.ca.mfd.prc.avi.service.IAviDefectAnomalyAviService;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pqs.entity.PqsDefectAnomalyEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: AVI缺陷阻塞配置[作废]服务实现
 * @date 2023年09月09日
 * @变更说明 BY inkelink At 2023年09月09日
 */
@Service
public class AviDefectAnomalyAviServiceImpl extends AbstractCrudServiceImpl<IAviDefectAnomalyAviMapper, AviDefectAnomalyAviEntity> implements IAviDefectAnomalyAviService {

    @Autowired
    PmVersionProvider pmVersionProvider;

    @Autowired
    PqsdefectanomalyProvider pqsdefectanomalyProvider;

    @Autowired
    private LocalCache localCache;

    private static final String cacheName = "PRC_AVI_DEFECT_ANOMALY_AVI";

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
    }

    @Override
    public void afterDelete(Wrapper<AviDefectAnomalyAviEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(AviDefectAnomalyAviEntity model) {
        if (StringUtils.isBlank(model.getPqsDefectAnomalyCode())) {
            model.setPqsDefectAnomalyCode("000000");
            model.setPqsDefectAnomalyDescription("所有缺陷");
        } else {
            if (!model.getIsRisk()) {
                //PqsDefectAnomalyEntity defectInfo = pqsDefectAnomalyService.getEntityByCode(model.getPqsDefectAnomalyCode());
                ResultVO<PqsDefectAnomalyEntity> defectInfo = pqsdefectanomalyProvider.getEntityByCode(model.getPqsDefectAnomalyCode());
                if (defectInfo == null || !defectInfo.getSuccess() || defectInfo.getData() == null) {
                    throw new InkelinkException("无效的缺陷选择，请重新选择");
                }
                model.setPqsDefectAnomalyCode(defectInfo.getData().getDefectAnomalyCode());
                model.setPqsDefectAnomalyDescription(defectInfo.getData().getDefectAnomalyDescription());
            }
        }
        if (StringUtils.isNotBlank(model.getAviCode())) {
            //PmAllDTO pmall = pmVersionService.getObjectedPm();
            PmAllDTO pmall = pmVersionProvider.getObjectedPm();
            PmAviEntity aviInfo = pmall.getAvis().stream().
                    filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
            if (aviInfo == null) {
                throw new InkelinkException("AVI站点查询异常");
            }
            model.setAviName(aviInfo.getAviName());
        } else {
            throw new InkelinkException("请选择AVI代码");
        }
        if (model.getIsRisk()) {
            model.setPqsDefectAnomalyCode("111111");
            model.setPqsDefectAnomalyDescription("风控缺陷");
        }
        Long countNumber = getDefectAnomalyNumber(model.getAviCode(), model.getPqsDefectAnomalyCode(), model.getId());
        if (countNumber > 0) {
            throw new InkelinkException("该站点已存在缺陷代码【" + model.getPqsDefectAnomalyCode() + "】");
        }
        removeCache();
    }

    @Override
    public void beforeUpdate(AviDefectAnomalyAviEntity model) {
        if (StringUtils.isBlank(model.getPqsDefectAnomalyCode())) {
            model.setPqsDefectAnomalyCode("000000");
            model.setPqsDefectAnomalyDescription("所有缺陷");
        } else {
            if (!model.getIsRisk()) {
                ResultVO<PqsDefectAnomalyEntity> defectInfo = pqsdefectanomalyProvider.getEntityByCode(model.getPqsDefectAnomalyCode());
                if (defectInfo == null || !defectInfo.getSuccess() || defectInfo.getData() == null) {
                    throw new InkelinkException("无效的缺陷选择，请重新选择");
                }
                model.setPqsDefectAnomalyCode(defectInfo.getData().getDefectAnomalyCode());
                model.setPqsDefectAnomalyDescription(defectInfo.getData().getDefectAnomalyDescription());
            }
        }

        if (StringUtils.isNotBlank(model.getAviCode())) {
            //PmAllDTO pmall = pmVersionService.getObjectedPm();
            PmAllDTO pmall = pmVersionProvider.getObjectedPm();
            PmAviEntity aviInfo = pmall.getAvis().stream().
                    filter(s -> StringUtils.equals(s.getAviCode(), model.getAviCode())).findFirst().orElse(null);
            if (aviInfo == null) {
                throw new InkelinkException("AVI站点查询异常");
            }
            model.setAviName(aviInfo.getAviName());
        }
        if (model.getIsRisk()) {
            model.setPqsDefectAnomalyCode("111111");
            model.setPqsDefectAnomalyDescription("风控缺陷");
        }

        Long countNumber = getDefectAnomalyNumber(model.getAviCode(), model.getPqsDefectAnomalyCode(), model.getId());
        if (countNumber > 0) {
            throw new InkelinkException("该站点已存在缺陷代码【" + model.getPqsDefectAnomalyCode() + "】");
        }
        removeCache();
    }

    @Override
    public List<AviDefectAnomalyAviEntity> getAnomalysByAviId(String aviCode) {
        QueryWrapper<AviDefectAnomalyAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviDefectAnomalyAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviDefectAnomalyAviEntity::getAviCode, aviCode);
        return selectList(queryWrapper);
    }

    public Long getDefectAnomalyNumber(String aviCode, String defectAnomalyCode, Long id) {
        QueryWrapper<AviDefectAnomalyAviEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<AviDefectAnomalyAviEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(AviDefectAnomalyAviEntity::getAviCode, aviCode);
        lambdaQueryWrapper.eq(AviDefectAnomalyAviEntity::getPqsDefectAnomalyCode, defectAnomalyCode);
        lambdaQueryWrapper.ne(AviDefectAnomalyAviEntity::getId, id);
        return selectCount(queryWrapper);
    }

    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("isRisk") && data.getOrDefault("isRisk", null) != null) {
                if (data.containsKey("pqsDefectAnomalyCode") && data.getOrDefault("pqsDefectAnomalyCode", null) != null) {
                    if (StringUtils.equals(data.get("pqsDefectAnomalyCode").toString(), "111111")) {
                        data.put("isRisk", "是");
                    } else {
                        data.put("isRisk", "否");
                    }
                } else {
                    data.put("isRisk", "否");
                }
            }
        }
    }
}