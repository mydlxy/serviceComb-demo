package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviPolicyEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRouteAreaEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviRoutePointEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviRoutePointMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviPolicyService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRouteAreaService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviRoutePointService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 路由点服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcAviRoutePointServiceImpl extends AbstractCrudServiceImpl<IRcAviRoutePointMapper, RcAviRoutePointEntity> implements IRcAviRoutePointService {
    @Autowired
    IRcAviRouteAreaService rcAviRouteAreaService;

    @Autowired
    IRcAviPolicyService rcAviPolicyService;

    @Override
    public void beforeUpdate(RcAviRoutePointEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviRoutePointEntity model) {
        validData(model);
    }

    private void validData(RcAviRoutePointEntity model) {
        if (StringUtils.isBlank(model.getPointCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (StringUtils.isBlank(model.getPointName())) {
            throw new InkelinkException("名称");
        }
        validDataUnique(model.getId(), "PointCode", model.getPointCode(), "已经存在代码为%s的数据", "", "");
        validDataUnique(model.getId(), "PointName", model.getPointName(), "已经存在名称为%s的数据", "", "");
    }


    /**
     * 根据 路由区ID 获取路由点列表
     *
     * @param areaId 路由区ID
     * @return 路由点列表
     */
    @Override
    public List<RcAviRoutePointEntity> getEntityByAreaId(Long areaId) {
        QueryWrapper<RcAviRoutePointEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviRoutePointEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcAviRoutePointEntity::getPrcRcAviRouteAreaId, areaId);
        return selectList(queryWrapper);
    }


    @Override
    public void dealExcelDatas(List<Map<String, Object>> datas) {
        for (Map<String, Object> data : datas) {
            if (data.containsKey("mode") && data.getOrDefault("mode", null) != null) {
                if (data.get("mode").equals(1)) {
                    data.put("mode", "手动");
                } else {
                    data.put("mode", "自动");
                }
            }
            if (data.containsKey("prcRcAviRouteAreaId") && data.getOrDefault("prcRcAviRouteAreaId", null) != null) {
                Long areaId = ConvertUtils.stringToLong(String.valueOf(data.get("prcRcAviRouteAreaId")));
                RcAviRouteAreaEntity entity = rcAviRouteAreaService.get(areaId);
                if (entity != null) {
                    data.put("areaId", entity.getAreaName());
                }
            }

            if (data.containsKey("prcRcAviPolicyId") && data.getOrDefault("prcRcAviPolicyId", null) != null) {
                Long policyId = ConvertUtils.stringToLong(String.valueOf(data.get("prcRcAviPolicyId")));
                RcAviPolicyEntity entity = rcAviPolicyService.get(policyId);
                if (entity != null) {
                    data.put("prcRcAviPolicyId", entity.getPolicyName());
                }
            }

            if (data.containsKey("isEnable") && data.getOrDefault("isEnable", null) != null) {
                if (data.get("isEnable").equals("false")) {
                    data.put("isEnable", "否");
                } else {
                    data.put("isEnable", "是");
                }
            }

        }
    }

}