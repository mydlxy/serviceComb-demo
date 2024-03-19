package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.eps.dto.MaterialPutPara;
import com.ca.mfd.prc.eps.entity.EpsFurnaceConfigEntity;
import com.ca.mfd.prc.eps.mapper.IEpsFurnacePutLogMapper;
import com.ca.mfd.prc.eps.entity.EpsFurnacePutLogEntity;
import com.ca.mfd.prc.eps.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmShcCalendarProvider;
import com.ca.mfd.prc.eps.service.IEpsFurnaceConfigService;
import com.ca.mfd.prc.eps.service.IEpsFurnacePutLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 *
 * @Description: 熔化炉投料记录服务实现
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Service
public class EpsFurnacePutLogServiceImpl extends AbstractCrudServiceImpl<IEpsFurnacePutLogMapper, EpsFurnacePutLogEntity> implements IEpsFurnacePutLogService {

    @Autowired
    private IEpsFurnaceConfigService epsFurnaceConfigService;

    @Autowired
    private PmShcCalendarProvider pmShcCalendarProvider;

    @Override
    public void putMaterial(MaterialPutPara request) {
        EpsFurnaceConfigEntity furnace = epsFurnaceConfigService.getFirstByFurnaceNo(request.getFurnaceNo());
        if (furnace == null) {
            throw new InkelinkException("未找到熔炉配置");
        }
        //数采 请求标识重复。 放弃入库
        QueryWrapper<EpsFurnacePutLogEntity> qryCnt = new QueryWrapper<>();
        qryCnt.lambda().eq(EpsFurnacePutLogEntity::getRequestFlag, request.getRequsetFlag());
        if (!StringUtils.isBlank(request.getRequsetFlag()) && selectCount(qryCnt) > 0) {
            return;
        }
        ShiftDTO shiftInfo = pmShcCalendarProvider.getCurrentShiftInfo(furnace.getLineCode());
        EpsFurnacePutLogEntity et = new EpsFurnacePutLogEntity();

        et.setFurnaceNo(furnace.getLineCode());
        et.setRequestFlag(request.getRequsetFlag());
        et.setLotNo(request.getLotNo());
        et.setShiftId(shiftInfo == null ? 0 : ConvertUtils.stringToLong(shiftInfo.getShcCalenderId()));
        et.setShiftName(shiftInfo == null ? "" : shiftInfo.getName());
        et.setSource(request.getSource());
        et.setPutType(request.getPutType());
        et.setWeight(request.getWeight());
        insert(et);
    }
}