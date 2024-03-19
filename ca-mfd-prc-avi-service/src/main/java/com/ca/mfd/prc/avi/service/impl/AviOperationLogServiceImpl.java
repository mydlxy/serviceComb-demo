package com.ca.mfd.prc.avi.service.impl;

import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.avi.mapper.IAviOperationLogMapper;
import com.ca.mfd.prc.avi.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.avi.service.IAviOperationLogService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 产品跟踪站点终端操作日志
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@Service
public class AviOperationLogServiceImpl extends AbstractCrudServiceImpl<IAviOperationLogMapper, AviOperationLogEntity> implements IAviOperationLogService {

    @Autowired
    PmVersionProvider pmVersionProvider;

    private PmAllDTO getObjectedPm() {
        PmAllDTO pmData = pmVersionProvider.getObjectedPm();
        if (null == pmData) {
            throw new InkelinkException("PM模块获取PM信息失败");
        }
        return pmData;
    }

    @Override
    public void insertAviLog(String aviCode, String operName, String operation) {
        insertAviLog(aviCode, operName, Arrays.asList(operation));
    }

    @Override
    public void insertAviLog(String aviCode, String operName, List<String> operations) {
        PmAllDTO pm = getObjectedPm();
        PmAviEntity aviInfo = pm.getAvis().stream().filter(w -> StringUtils.equals(w.getAviCode(), aviCode)).findFirst().orElse(null);
        if (aviInfo != null) {
            PmLineEntity areaInfo = pm.getLines().stream().filter(w -> Objects.equals(w.getId(), aviInfo.getPrcPmLineId())).findFirst().orElse(null);
            PmWorkShopEntity shopInfo = pm.getShops().stream().filter(w -> Objects.equals(w.getId(), areaInfo.getPrcPmWorkshopId())).findFirst().orElse(null);

            List<AviOperationLogEntity> logs = new ArrayList<>();
            for (String operation : operations) {
                AviOperationLogEntity log = new AviOperationLogEntity();
                log.setWorkshopName(shopInfo.getWorkshopName());
                log.setWorkshopCode(shopInfo.getWorkshopCode());
                log.setLineName(areaInfo.getLineName());
                log.setLineCode(areaInfo.getLineCode());
                log.setAviCode(aviInfo.getAviCode());
                log.setAviName(aviInfo.getAviName());
                log.setAviIpAddress(aviInfo.getIpAddress());
                log.setOperation(operation);
                log.setOperationLever(2);
                log.setOperationUser(operName);
                log.setOprerationTime(new Date());
                logs.add(log);
            }
            insertBatch(logs);
        }
    }
}