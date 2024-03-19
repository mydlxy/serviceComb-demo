package com.ca.mfd.prc.rc.rcavi.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviSkidEntity;
import com.ca.mfd.prc.rc.rcavi.mapper.IRcAviSkidMapper;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviSkidService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 滑橇服务实现
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@Service
public class RcAviSkidServiceImpl extends AbstractCrudServiceImpl<IRcAviSkidMapper, RcAviSkidEntity> implements IRcAviSkidService {

    @Override
    public void beforeUpdate(RcAviSkidEntity model) {
        validData(model);
    }

    @Override
    public void beforeInsert(RcAviSkidEntity model) {
        validData(model);
    }

    private void validData(RcAviSkidEntity model) {
        if (StringUtils.isBlank(model.getSkidCode())) {
            throw new InkelinkException("代码不能为空");
        }
        if (model.getSkidType() <= 0) {
            throw new InkelinkException("请选择滑橇类型");
        }
        Long qty = getQtyCount(model.getWorkshopCode(), model.getSkidCode(), model.getSkidType(), model.getId());
        if (qty > 0) {
            throw new InkelinkException("已经存在代码—>车间->代码->" +
                    model.getWorkshopCode() + "->" + model.getSkidCode() + "->" + model.getSkidType() + "数据");
        }
    }

    private Long getQtyCount(String workshopCode, String skidCode, Integer skidType, Long id) {
        QueryWrapper<RcAviSkidEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<RcAviSkidEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(RcAviSkidEntity::getWorkshopCode, workshopCode);
        lambdaQueryWrapper.eq(RcAviSkidEntity::getSkidCode, skidCode);
        lambdaQueryWrapper.eq(RcAviSkidEntity::getSkidType, skidType);
        lambdaQueryWrapper.ne(RcAviSkidEntity::getId, id);
        return selectCount(queryWrapper);
    }
}