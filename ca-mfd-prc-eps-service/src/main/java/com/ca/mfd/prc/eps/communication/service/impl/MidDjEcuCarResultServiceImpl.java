package com.ca.mfd.prc.eps.communication.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.communication.dto.MidDjEcuCarResultDto;
import com.ca.mfd.prc.eps.communication.dto.MidDjEcuCarResultEcuListDto;
import com.ca.mfd.prc.eps.communication.mapper.IMidDjEcuCarResultMapper;
import com.ca.mfd.prc.eps.communication.entity.MidDjEcuCarResultEntity;
import com.ca.mfd.prc.eps.communication.service.IMidDjEcuCarResultService;
import org.springframework.stereotype.Service;

/**
 * @author inkelink
 * @Description: 电检软件结果信息数据服务实现
 * @date 2023年11月29日
 * @变更说明 BY inkelink At 2023年11月29日
 */
@Service
public class MidDjEcuCarResultServiceImpl extends AbstractCrudServiceImpl<IMidDjEcuCarResultMapper, MidDjEcuCarResultEntity> implements IMidDjEcuCarResultService {


    @Override
    public MidDjEcuCarResultDto queryEcuCarByVinCode(String vinCode) {
        QueryWrapper<MidDjEcuCarResultEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(MidDjEcuCarResultEntity::getVinCode, vinCode)
                .orderByDesc(MidDjEcuCarResultEntity::getCreationDate);
        MidDjEcuCarResultEntity entity = selectList(qry).stream().findFirst().orElse(null);
        if (entity != null) {
            MidDjEcuCarResultDto dto = new MidDjEcuCarResultDto();
            dto.setVinCode(entity.getVinCode());
            dto.setEcuList(JsonUtils.parseArray(entity.getEcuList(), MidDjEcuCarResultEcuListDto.class));
            dto.setTestTime(entity.getTestTime());
            dto.setUploadDate(entity.getUploadDate());
            return dto;
        }
        return null;
    }
}