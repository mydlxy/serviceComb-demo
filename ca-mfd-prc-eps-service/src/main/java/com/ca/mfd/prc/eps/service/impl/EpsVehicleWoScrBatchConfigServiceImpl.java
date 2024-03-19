package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsVehicleWoScrBatchConfigMapper;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWoEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoScrBatchConfigService;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoScrBatchConfigEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 批次件自动追溯配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@Service
public class EpsVehicleWoScrBatchConfigServiceImpl extends AbstractCrudServiceImpl<IEpsVehicleWoScrBatchConfigMapper, EpsVehicleWoScrBatchConfigEntity> implements IEpsVehicleWoScrBatchConfigService {

    @Autowired
    private PmVersionProvider pmVersionProvider;

    /**
     * 工艺某个追溯工艺的自动完成配置
     *
     * @param pmWoCode
     * @param workStationCode
     * @return
     */
    @Override
    public EpsVehicleWoScrBatchConfigEntity getScrBatchConfig(String pmWoCode, String workStationCode) {
        QueryWrapper<EpsVehicleWoScrBatchConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoScrBatchConfigEntity::getWoCode, pmWoCode)
                .eq(EpsVehicleWoScrBatchConfigEntity::getWorkstationCode, workStationCode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取批次追溯条码
     *
     * @param pmWoCode
     * @param workStationCode
     * @return
     */
    @Override
    public String getScrBarcode(String pmWoCode, String workStationCode) {
        QueryWrapper<EpsVehicleWoScrBatchConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsVehicleWoScrBatchConfigEntity::getWoCode, pmWoCode)
                .eq(EpsVehicleWoScrBatchConfigEntity::getWorkstationCode, workStationCode)
                .gt(EpsVehicleWoScrBatchConfigEntity::getCount, 0);
        EpsVehicleWoScrBatchConfigEntity data = getTopDatas(1, qry).stream().findFirst().orElse(null);
        return data == null ? StringUtils.EMPTY : data.getBarCode();
    }

    /**
     * 自动维护配置
     *
     * @param pmWoCode
     * @param workStationCode
     * @param barcode
     */
    @Override
    public void autoScrBatchConfig(String pmWoCode, String workStationCode, String barcode) {
        PmAllDTO allPm = pmVersionProvider.getObjectedPm();
        PmWorkStationEntity stationInfo = allPm.getStations().stream().filter(c ->
                StringUtils.equals(c.getWorkstationCode(), workStationCode)).findFirst().orElse(null);

        PmWoEntity woInfo = allPm.getWos().stream().filter(c -> StringUtils.equals(c.getWoCode(), pmWoCode)
                && Objects.equals(c.getPrcPmWorkstationId(), stationInfo.getId())).findFirst().orElse(null);

        if (woInfo == null || !woInfo.getTrcByGroup()) {
            return;
        }

        EpsVehicleWoScrBatchConfigEntity configInfo = getScrBatchConfig(pmWoCode, workStationCode);
        if (configInfo != null) {
            UpdateWrapper<EpsVehicleWoScrBatchConfigEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(EpsVehicleWoScrBatchConfigEntity::getBarCode, barcode)
                    .setSql(" COUNT = COUNT -1 ").eq(EpsVehicleWoScrBatchConfigEntity::getId, configInfo.getId());
            update(upset);
        } else {
            EpsVehicleWoScrBatchConfigEntity et = new EpsVehicleWoScrBatchConfigEntity();
            et.setWorkstationCode(workStationCode);
            et.setWoCode(pmWoCode);
            et.setBarCode(barcode);
            et.setCount(0);
            this.insert(et);
        }
    }

    /**
     * 更新数量
     *
     * @param count
     * @param id
     */
    @Override
    public void updateCount(Integer count, Long id) {
        UpdateWrapper<EpsVehicleWoScrBatchConfigEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsVehicleWoScrBatchConfigEntity::getCount, count)
                .eq(EpsVehicleWoScrBatchConfigEntity::getId, id);
        update(upset);
    }
}