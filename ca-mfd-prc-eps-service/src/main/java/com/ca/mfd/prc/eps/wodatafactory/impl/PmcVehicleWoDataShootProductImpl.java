package com.ca.mfd.prc.eps.wodatafactory.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataShootService;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import com.ca.mfd.prc.eps.wodatafactory.IDataFactoryService;
import com.ca.mfd.prc.eps.dto.SaveShootPara;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataShootEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 拍照追溯
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Service
public class PmcVehicleWoDataShootProductImpl implements IDataFactoryService {

    @Autowired
    private IEpsVehicleWoDataShootService epsVehicleWoDataShootService;
    @Autowired
    private IEpsVehicleWoService epsVehicleWoService;

    @Override
    public WoDataEnum getDataEnum() {
        return WoDataEnum.EpsVehicleWoDataShoot;
    }

    @Override
    public void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo) {
        SaveShootPara info = JsonUtils.parseObject(jsonData, SaveShootPara.class);
        EpsVehicleWoEntity woInfo = epsVehicleWoService.get(woDataInfo.getPrcEpsVehicleWoId());
        UpdateWrapper<EpsVehicleWoDataShootEntity> delQry = new UpdateWrapper<>();
        delQry.lambda()
                .eq(EpsVehicleWoDataShootEntity::getPrcEpsVehicleWoId, woDataInfo.getPrcEpsVehicleWoId())
                .eq(EpsVehicleWoDataShootEntity::getSn, info.getSn());
        epsVehicleWoDataShootService.delete(delQry);

        EpsVehicleWoDataShootEntity et = new EpsVehicleWoDataShootEntity();
        et.setPrcEpsVehicleWoDataId(woDataInfo.getId());
        et.setPrcEpsVehicleWoId(woDataInfo.getPrcEpsVehicleWoId());
        et.setSn(info.getSn());
        et.setWoDescription(woInfo.getWoDescription());
        et.setImg(info.getImg());
        epsVehicleWoDataShootService.insert(et);
    }

    @Override
    public String getDataTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EpsVehicleWoDataShootEntity.class);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        return "";
    }

}