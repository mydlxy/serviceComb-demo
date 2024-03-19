package com.ca.mfd.prc.avi.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.avi.communication.dto.EcuCarInfoDto;
import com.ca.mfd.prc.avi.communication.dto.EcuSoftwareListDto;
import com.ca.mfd.prc.avi.communication.dto.SiteInfoDto;
import com.ca.mfd.prc.avi.communication.entity.MidDjSiteSendEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.avi.communication.mapper.IMidDjEcuSendMapper;
import com.ca.mfd.prc.avi.communication.entity.MidDjEcuSendEntity;
import com.ca.mfd.prc.avi.communication.service.IMidDjEcuSendService;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 电检软件信息下发服务实现
 * @author inkelink
 * @date 2023年12月25日
 * @变更说明 BY inkelink At 2023年12月25日
 */
@Service
public class MidDjEcuSendServiceImpl extends AbstractCrudServiceImpl<IMidDjEcuSendMapper, MidDjEcuSendEntity> implements IMidDjEcuSendService {


    @Override
    public ResultVO<List<EcuCarInfoDto>> queryEcuInfoByVin(String vin) {
        if(StringUtils.isEmpty(vin)){
            throw new InkelinkException("vin号不能为空");
        }
        QueryWrapper<MidDjEcuSendEntity> queryWrapper=new QueryWrapper<>();
        queryWrapper.lambda().eq(MidDjEcuSendEntity::getVinCode,vin);
        List<MidDjEcuSendEntity> data = this.getData(queryWrapper, false);
        List<EcuCarInfoDto> dtos=new ArrayList<>();
        if(!CollectionUtils.isEmpty(data)){
            data.stream().forEach(x->{
                EcuCarInfoDto dto=new EcuCarInfoDto();
                BeanUtils.copyProperties(x,dto);
                List<EcuSoftwareListDto> ecuSoftwareListDtos = JsonUtils.parseArray(x.getEcuList(), EcuSoftwareListDto.class);
                dto.setEcuList(ecuSoftwareListDtos);
                dtos.add(dto);
            });
        }
        return new ResultVO<List<EcuCarInfoDto>>().ok(dtos);
    }
}