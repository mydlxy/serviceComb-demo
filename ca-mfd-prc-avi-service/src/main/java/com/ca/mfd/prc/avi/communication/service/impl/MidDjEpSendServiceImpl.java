package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.entity.MidDjEpSendEntity;
import com.ca.mfd.prc.avi.communication.mapper.IMidDjEpSendMapper;
import com.ca.mfd.prc.avi.communication.remote.app.eps.entity.EpInfoDto;
import com.ca.mfd.prc.avi.communication.service.IMidDjEpSendService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: EP信息下发记录服务实现
 * @date 2023年12月27日
 * @变更说明 BY inkelink At 2023年12月27日
 */
@Service
public class MidDjEpSendServiceImpl extends AbstractCrudServiceImpl<IMidDjEpSendMapper, MidDjEpSendEntity> implements IMidDjEpSendService {

    @Override
    public ResultVO<List<EpInfoDto>> queryEpInfoByVin(String vin) {
        if(StringUtils.isEmpty(vin)){
            throw new InkelinkException("vin号不能为空");
        }
        QueryWrapper<MidDjEpSendEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(MidDjEpSendEntity::getVinCode,vin);
        List<MidDjEpSendEntity> data = this.getData(queryWrapper, false);
        List<EpInfoDto> dtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.stream().forEach(x->{
                EpInfoDto dto=new EpInfoDto();
                BeanUtils.copyProperties(x,dto);
                dtos.add(dto);
            });
        }
        return new ResultVO<List<EpInfoDto>>().ok(dtos);
    }
}