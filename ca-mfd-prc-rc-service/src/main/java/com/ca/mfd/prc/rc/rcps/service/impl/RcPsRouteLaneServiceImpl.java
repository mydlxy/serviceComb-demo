package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteLaneEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteLaneMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteLaneService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由车道服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcPsRouteLaneServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteLaneMapper, RcPsRouteLaneEntity> implements IRcPsRouteLaneService {

    @Override
    public void beforeUpdate(RcPsRouteLaneEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcPsRouteLaneEntity model) {
        validData(model);
    }

    private void validData(RcPsRouteLaneEntity model) {
        if (model.getLaneCode() < 0) {
            throw new InkelinkException("代码不能为小于0");
        }
        if (StringUtils.isBlank(model.getLaneName())) {
            throw new InkelinkException("名称");
        }
        Long laneQty = getRecodCountByCode(model.getPrcRcPsRouteAreaId(),
                model.getLaneCode(), model.getLaneName(), model.getDisplayNo(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在代码—>名称->排序号:" + model.getLaneCode() + "->"
                    + model.getLaneName() + "->" + model.getDisplayNo() + "数据");
        }
    }

    private Long getRecodCountByCode(Long areaId, int laneCode, String laneName, int displayNo, Long id) {
        QueryWrapper<RcPsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteLaneEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.ne(RcPsRouteLaneEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.eq(RcPsRouteLaneEntity::getLaneCode, laneCode).
                or().eq(RcPsRouteLaneEntity::getLaneName, laneName).
                or().eq(RcPsRouteLaneEntity::getDisplayNo, displayNo));
        return selectCount(queryWrapper);
    }

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @return 路由缓存实体
     */
    @Override
    public RcPsRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code) {
        QueryWrapper<RcPsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteLaneEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteLaneEntity::getLaneCode, code);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    @Override
    public List<RcPsRouteLaneEntity> getLaneEntityByAreaId(String areaId) {
        QueryWrapper<RcPsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteLaneEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcPsRouteLaneEntity::getDisplayNo);
        return selectList(queryWrapper);
    }
}