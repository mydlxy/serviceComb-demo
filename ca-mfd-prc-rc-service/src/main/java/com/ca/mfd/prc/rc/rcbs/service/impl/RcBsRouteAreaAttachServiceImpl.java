package com.ca.mfd.prc.rc.rcbs.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaAttachEntity;
import com.ca.mfd.prc.rc.rcbs.mapper.IRcBsRouteAreaAttachMapper;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaAttachService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块服务实现
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@Service
public class RcBsRouteAreaAttachServiceImpl extends AbstractCrudServiceImpl<IRcBsRouteAreaAttachMapper, RcBsRouteAreaAttachEntity> implements IRcBsRouteAreaAttachService {
    @Resource
    private IRcBsRouteAreaAttachMapper rcRouteAreaAttachMapper;

    /**
     * 查询所有附加的列表
     *
     * @return 附加列表
     */
    @Override
    public List<RcRouteAreaAttachShowVO> getAttachShowList() {
        return rcRouteAreaAttachMapper.getAttachShowList();
    }


    @Override
    public void beforeUpdate(RcBsRouteAreaAttachEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcBsRouteAreaAttachEntity model) {
        validData(model);
    }

    private void validData(RcBsRouteAreaAttachEntity model) {
        if (model.getAttachType() == null || StringUtils.isBlank(model.getAttachCode())) {
            throw new InkelinkException("附加类型或附加代码不能为空");
        }
        Long laneQty = getDataCountByCode(model.getPrcRcBsRouteAreaId(), model.getAttachCode(), model.getAttachName(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在附加代码或名称" + model.getAttachCode() + "->" + model.getAttachName() + "数据");
        }
    }

    private Long getDataCountByCode(Long areaId, String attachCode, String attachName, Long id) {
        QueryWrapper<RcBsRouteAreaAttachEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcBsRouteAreaAttachEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcBsRouteAreaAttachEntity::getPrcRcBsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcBsRouteAreaAttachEntity::getAttachCode, attachCode);
        lambdaQueryWrapper.ne(RcBsRouteAreaAttachEntity::getId, id);
        return selectCount(queryWrapper);
    }
}