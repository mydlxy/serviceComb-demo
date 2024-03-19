package com.ca.mfd.prc.pmc.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmEquipmentService;
import com.ca.mfd.prc.pmc.remote.app.pm.IPmShcCalendarService;
import com.ca.mfd.prc.pmc.remote.app.pm.dto.ShiftDTO;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmEquipmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmEquipmentProvider {
    @Autowired
    private IPmEquipmentService pmEquipmentService;

    public PmEquipmentEntity getPmEquipmentEntityByCode(String code) {
        ResultVO<PmEquipmentEntity> result = pmEquipmentService.getPmEquipmentEntityByCode(code);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmequipment调用失败" + result.getMessage());
        }
        return result.getData();
    }

}
