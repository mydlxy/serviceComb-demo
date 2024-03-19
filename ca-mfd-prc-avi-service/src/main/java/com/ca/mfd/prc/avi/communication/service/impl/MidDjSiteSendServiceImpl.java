package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.dto.CarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjCarSendEntity;
import com.ca.mfd.prc.avi.communication.entity.MidDjEcuSendEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.communication.mapper.IMidDjSiteSendMapper;
import com.ca.mfd.prc.avi.communication.entity.MidDjSiteSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjSiteSendService;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 过点信息下发记录服务实现
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Service
public class MidDjSiteSendServiceImpl extends AbstractCrudServiceImpl<IMidDjSiteSendMapper, MidDjSiteSendEntity> implements IMidDjSiteSendService {


    @Override
    public ResultVO<List<SiteInfoDto>> querySiteInfoByVin(String vin) {
        if(StringUtils.isEmpty(vin)){
            throw new InkelinkException("vin号不能为空");
        }
        QueryWrapper<MidDjSiteSendEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(MidDjSiteSendEntity::getVinCode,vin);
        List<MidDjSiteSendEntity> data = this.getData(queryWrapper, false);
        List<SiteInfoDto> dtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.stream().forEach(x->{
                SiteInfoDto dto=new SiteInfoDto();
                BeanUtils.copyProperties(x,dto);
                dtos.add(dto);
            });
        }
        return new ResultVO<List<SiteInfoDto>>().ok(dtos);
    }
}