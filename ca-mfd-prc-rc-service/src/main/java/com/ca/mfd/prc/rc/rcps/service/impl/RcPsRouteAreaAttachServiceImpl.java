package com.ca.mfd.prc.rc.rcps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcps.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.rc.rcps.entity.RcPsRouteAreaAttachEntity;
import com.ca.mfd.prc.rc.rcps.mapper.IRcPsRouteAreaAttachMapper;
import com.ca.mfd.prc.rc.rcps.service.IRcPsRouteAreaAttachService;
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
public class RcPsRouteAreaAttachServiceImpl extends AbstractCrudServiceImpl<IRcPsRouteAreaAttachMapper, RcPsRouteAreaAttachEntity> implements IRcPsRouteAreaAttachService {
    @Resource
    private IRcPsRouteAreaAttachMapper rcRouteAreaAttachMapper;

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
    public void beforeUpdate(RcPsRouteAreaAttachEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcPsRouteAreaAttachEntity model) {
        validData(model);
    }

    private void validData(RcPsRouteAreaAttachEntity model) {
        if (model.getAttachType() == null || StringUtils.isBlank(model.getAttachCode())) {
            throw new InkelinkException("附加类型或附加代码不能为空");
        }
        Long laneQty = getDataCountByCode(model.getPrcRcPsRouteAreaId(), model.getAttachCode(), model.getAttachName(), model.getId());
        if (laneQty > 0) {
            throw new InkelinkException("区域已经存在附加代码或名称" + model.getAttachCode() + "->" + model.getAttachName() + "数据");
        }
    }

    private Long getDataCountByCode(Long areaId, String attachCode, String attachName, Long id) {
        QueryWrapper<RcPsRouteAreaAttachEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcPsRouteAreaAttachEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcPsRouteAreaAttachEntity::getPrcRcPsRouteAreaId, areaId);
        lambdaQueryWrapper.eq(RcPsRouteAreaAttachEntity::getAttachCode, attachCode);
        lambdaQueryWrapper.ne(RcPsRouteAreaAttachEntity::getId, id);
        return selectCount(queryWrapper);
    }
}