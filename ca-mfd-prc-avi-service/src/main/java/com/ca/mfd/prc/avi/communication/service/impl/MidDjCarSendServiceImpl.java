package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.communication.mapper.IMidDjCarSendMapper;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjCarSendService;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 整车信息下发记录服务实现
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Service
public class MidDjCarSendServiceImpl extends AbstractCrudServiceImpl<IMidDjCarSendMapper, MidDjCarSendEntity> implements IMidDjCarSendService {


    @Override
    public ResultVO<List<CarInfoDto>> queryCarInfoByVin(String vin) {
        if(StringUtils.isEmpty(vin)){
            throw new InkelinkException("vin号不能为空");
        }
        QueryWrapper<MidDjCarSendEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(MidDjCarSendEntity::getVinCode,vin);
        List<MidDjCarSendEntity> data = this.getData(queryWrapper, false);
        List<CarInfoDto> dtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.stream().forEach(x->{
                CarInfoDto dto=new CarInfoDto();
                BeanUtils.copyProperties(x,dto);
                dtos.add(dto);
            });
        }
        return new ResultVO<List<CarInfoDto>>().ok(dtos);
    }
}