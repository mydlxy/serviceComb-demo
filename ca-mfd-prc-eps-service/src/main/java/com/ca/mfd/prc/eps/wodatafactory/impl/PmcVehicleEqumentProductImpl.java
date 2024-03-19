package com.ca.mfd.prc.eps.wodatafactory.impl;

import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.eps.service.IEpsVehicleEqumentDataService;
import com.ca.mfd.prc.eps.wodatafactory.IDataFactoryService;
import com.ca.mfd.prc.eps.dto.EqumentWoItem;
import com.ca.mfd.prc.eps.dto.ProductEqumentWoData;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.enums.WoDataEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 追溯设备工艺生产数据
 *
 * @author eric.zhou
 * @since 1.0.0 2023-04-14
 */
@Service
public class PmcVehicleEqumentProductImpl implements IDataFactoryService {

    @Autowired
    private IEpsVehicleEqumentDataService epsVehicleEqumentDataService;
    private String tableName = StringUtils.EMPTY;

    @Override
    public WoDataEnum getDataEnum() {
        return WoDataEnum.EpsVehicleEqument;
    }

    @Override
    public void saveWoData(String jsonData, EpsVehicleWoDataEntity woDataInfo) {
        ProductEqumentWoData ewoData = JsonUtils.parseObject(jsonData, ProductEqumentWoData.class);
        tableName = ewoData.getTableName();
        if (StringUtils.equals(getVehicleEqumentDataTableName(), tableName)) {
            List<EpsVehicleEqumentDataEntity> dataInfo = new ArrayList<>();

            if (ewoData.getItems() != null && ewoData.getItems().size() > 0) {
                for (EqumentWoItem item : ewoData.getItems()) {

                    EpsVehicleEqumentDataEntity et = new EpsVehicleEqumentDataEntity();
                    et.setId(IdGenerator.getId());
                    et.setPrcEpsVehicleWoId(woDataInfo.getPrcEpsVehicleWoId());
                    et.setPrcEpsVehicleWoDataId(woDataInfo.getId());
                    et.setDisplayNo(item.getDisPlayNo());
                    et.setWoName(item.getWoName());
                    et.setWoCode(item.getWoCode());
                    et.setWoUnit(item.getWoUnit());
                    et.setWoDownlimit(item.getWoDownlimit());
                    et.setWoUplimit(item.getWoUplimit());
                    et.setWoStandard(item.getWoStandard());
                    et.setWoValue(item.getWoValue());
                    et.setWoResult(item.getWoResult());
                    dataInfo.add(et);
                }
            }
            if (dataInfo.size() > 0) {
                epsVehicleEqumentDataService.insertBatch(dataInfo);
            }
        }
    }

    private String getVehicleEqumentDataTableName() {
        TableInfo tableInfo = TableInfoHelper.getTableInfo(EpsVehicleEqumentDataEntity.class);
        if (tableInfo != null) {
            return tableInfo.getTableName();
        }
        return "";
    }

    @Override
    public String getDataTableName() {
        return tableName;
    }

}