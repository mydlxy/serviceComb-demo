package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.pm.communication.dto.MidLmsDatasVo;
import com.ca.mfd.prc.pm.communication.dto.MidLmsSigtrueVo;
import com.ca.mfd.prc.pm.entity.PmWorkstationMaterialEntity;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IPmWorkstationMaterialSubService extends IPmWorkstationMaterialService {

    List<PmWorkstationMaterialEntity> getPmMaterial(String productMaterialNo, String[] workShopCode,
                                                    String model, String type, String specifyDate);

    MidLmsDatasVo getPmWorkstationMaterial(String sigtrue);

    void sendCreateLmsSigtrueMes(List<MidLmsSigtrueVo> messages);
}
