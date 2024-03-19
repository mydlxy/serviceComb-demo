package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteLaneEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteLaneMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteLaneService;
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
public class RcBsRouteLaneServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteLaneMapper, RcBsRouteLaneEntity> implements IRcBsRouteLaneService {

    @Override
    public void beforeUpdate(RcBsRouteLaneEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRouteLaneEntity model) {
        validData(model);
    }

    private void validData(RcBsRouteLaneEntity model) {
        if (model.getLaneCode() < 0) {
            throw new InkelinkException("代码不能为小于0");
        }
        if (StringUtils.isBlank(model.getLaneName())) {
            throw new InkelinkException("名称");
        }
        Long laneQty = getRecodCountByCode(model.getPrcRcBsRouteAreaId(),
                model.getLaneCode(), model.getLaneName(), model.getDisplayNo(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在代码—>名称->排序号:" + model.getLaneCode() + "->"
                    + model.getLaneName() + "->" + model.getDisplayNo() + "数据");
        }
    }

    private Long getRecodCountByCode(Long areaId, int laneCode, String laneName, int displayNo, Long id) {
        QueryWrapper<RcBsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteLaneEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.ne(RcBsRouteLaneEntity::getId, id);
        lambdaQueryWrapper.and(s -> s.eq(RcBsRouteLaneEntity::getLaneCode, laneCode).
                or().eq(RcBsRouteLaneEntity::getLaneName, laneName).
                or().eq(RcBsRouteLaneEntity::getDisplayNo, displayNo));
        return selectCount(queryWrapper);
    }

    /**
     * 根据路由区和编码查询路由缓存
     *
     * @return 路由缓存实体
     */
    @Override
    public RcBsRouteLaneEntity getLaneEntityByAreaIdAndCode(Long areaId, int code) {
        QueryWrapper<RcBsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteLaneEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteLaneEntity::getLaneCode, code);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 根据路由区查询列表
     *
     * @param areaId 路由区
     * @return 路由车道列表
     */
    @Override
    public List<RcBsRouteLaneEntity> getLaneEntityByAreaId(String areaId) {
        QueryWrapper<RcBsRouteLaneEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteLaneEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteLaneEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.orderByAsc(RcBsRouteLaneEntity::getDisplayNo);
        return selectList(queryWrapper);
    }
}