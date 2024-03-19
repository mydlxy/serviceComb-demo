package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.entity.MidDjTestSendEntity;
import com.ca.mfd.prc.avi.communication.mapper.IMidDjTestSendMapper;
import com.ca.mfd.prc.avi.communication.service.IMidDjTestSendService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 手动下发记录服务实现
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Service
public class MidDjTestSendServiceImpl extends AbstractCrudServiceImpl<IMidDjTestSendMapper, MidDjTestSendEntity> implements IMidDjTestSendService {
    @Override
    public List<MidDjTestSendEntity> getDataByIds(List<String> ids) {
        if (ids.isEmpty()) {
            throw new InkelinkException("请选择要发送的数据");
        }
        List<Long> id = ids.stream().map(Long::parseLong).collect(Collectors.toList());
        QueryWrapper<MidDjTestSendEntity> beforeWrapper = new QueryWrapper<>();
        beforeWrapper.lambda().in(MidDjTestSendEntity::getId, id);
        return getData(beforeWrapper, false);
    }
}