package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.eps.dto.CarrierMaterialsResponse;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.ca.mfd.prc.eps.mapper.IEpsCarrierMapper;
import com.ca.mfd.prc.eps.entity.EpsCarrierEntity;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.eps.service.IEpsCarrierLogService;
import com.ca.mfd.prc.eps.service.IEpsCarrierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * @author inkelink
 * @Description: 载具信息服务实现
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsCarrierServiceImpl extends AbstractCrudServiceImpl<IEpsCarrierMapper, EpsCarrierEntity> implements IEpsCarrierService {

    @Autowired
    private IEpsCarrierLogService epsCarrierLogService;

    @Override
    public void beforeInsert(EpsCarrierEntity model) {
        validData(model);
    }

    @Override
    public void beforeUpdate(EpsCarrierEntity model) {
        EpsCarrierEntity oldModel = this.get(model.getId());
        if (!StringUtils.equals(oldModel.getCarrierBarcode(), model.getCarrierBarcode())) {
            throw new InkelinkException("不能修改载具编号");
        }
    }

    private void validData(EpsCarrierEntity model) {
        validDataUnique(model.getId(), "CARRIER_BARCODE", model.getCarrierBarcode(), "已经存在载具编码为%s的数据", "", "");
    }

    private EpsCarrierEntity getFirstByBarCode(String carrierBarcode) {
        QueryWrapper<EpsCarrierEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierEntity::getCarrierBarcode, carrierBarcode);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    public Boolean getCheckRepeat(String carrierBarcode) {
        QueryWrapper<EpsCarrierEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<EpsCarrierEntity> lambdaQueryWrapper = qry.lambda();
        lambdaQueryWrapper.eq(EpsCarrierEntity::getCarrierBarcode, carrierBarcode);
        //lambdaQueryWrapper.eq(EpsCarrierEntity::getMaterialNo, materialNo);
        EpsCarrierEntity entity = this.getTopDatas(1, qry).stream().findFirst().orElse(null);
        if (entity == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 绑定载具
     *
     * @param ppsEntryReportPartsInfo 绑定载具
     */
    @Override
    public void bindCarrier(PpsEntryReportPartsEntity ppsEntryReportPartsInfo) {
        EpsCarrierEntity carrierInfo = getFirstByBarCode(ppsEntryReportPartsInfo.getCarrierCode());
        if (carrierInfo == null) {
            throw new InkelinkException("无效的载具");
        }
        if (!StringUtils.isBlank(carrierInfo.getMaterialNo())
                && !StringUtils.equals(carrierInfo.getMaterialNo(), ppsEntryReportPartsInfo.getMaterialNo())) {
            throw new InkelinkException("载具对应的物料不匹配");
        }
        if (carrierInfo.getIsUse() || StringUtils.isBlank(carrierInfo.getBatchNumber())) {
            carrierInfo.setBatchNumber(carrierInfo.getCarrierBarcode() + DateUtils.format(new Date(), "yyyyMMddHHmmssfff"));
        }
        if (!carrierInfo.getIsUse() && (carrierInfo.getIsPlan() || carrierInfo.getIsMaterial())) {
            CarrierMaterialsResponse result = getCarrierMaterials(carrierInfo.getCarrierBarcode());
            if (!result.getMaterials().isEmpty()) {
                //判断目前报工的计划和搭载的货物是否属于同一个计划
                if (carrierInfo.getIsPlan()) {
                    if (!result.getMaterials().stream().allMatch(c ->
                            StringUtils.equals(c.getPlanNo(), ppsEntryReportPartsInfo.getPlanNo()))) {
                        throw new InkelinkException("载具搭载的物料和生产的物料不属于同一个计划，请确认");
                    }
                }
                if (carrierInfo.getIsMaterial()) {
                    if (!result.getMaterials().stream().allMatch(c ->
                            StringUtils.equals(c.getMaterialNo(), ppsEntryReportPartsInfo.getMaterialNo()))) {
                        throw new InkelinkException("载具搭载的物料和生产的物料不属于同一种物料，请确认");
                    }
                }
            }
        }
        //判断是否装满
        if (carrierInfo.getIsVerifyQuantity() && carrierInfo.getMaterialCount()
                < (carrierInfo.getPracticalCount() + ppsEntryReportPartsInfo.getEntryReportCount())) {
            Integer count = carrierInfo.getMaterialCount() - carrierInfo.getPracticalCount();
            throw new InkelinkException("已超出载具可容纳数量,目前还可容纳" + count);
        }
        UpdateWrapper<EpsCarrierEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsCarrierEntity::getBatchNumber, carrierInfo.getBatchNumber())
                .setSql(" PRACTICAL_COUNT = PRACTICAL_COUNT + " + ppsEntryReportPartsInfo.getEntryReportCount())
                .set(EpsCarrierEntity::getIsUse, false)
                .eq(EpsCarrierEntity::getId, carrierInfo.getId());
        update(upset);

        EpsCarrierLogEntity carrierLog = new EpsCarrierLogEntity();
        carrierLog.setPrcEpsCarrierId(carrierInfo.getId());
        carrierLog.setCarrierBarcode(carrierInfo.getCarrierBarcode());
        carrierLog.setBatchNumber(carrierInfo.getBatchNumber());
        carrierLog.setMaterialNo(ppsEntryReportPartsInfo.getMaterialNo());
        carrierLog.setMaterialCn(ppsEntryReportPartsInfo.getMaterialCn());
        carrierLog.setEntryReportNo(ppsEntryReportPartsInfo.getEntryReportNo());
        carrierLog.setWorkstationCode(ppsEntryReportPartsInfo.getWorkstationCode());
        carrierLog.setMaterialCount(ppsEntryReportPartsInfo.getEntryReportCount());
        carrierLog.setBarcode(ppsEntryReportPartsInfo.getBarcode());
        carrierLog.setPlanNo(ppsEntryReportPartsInfo.getPlanNo());
        epsCarrierLogService.insert(carrierLog);
    }

    /**
     * 移除载具上面的物料
     *
     * @param entryReportNo 报工单号
     */
    @Override
    public void removeCarrierMaterial(String entryReportNo) {
        String carrierBarcode = epsCarrierLogService.getCarrierBarcodeByEntryReportNo(entryReportNo);
        if (StringUtils.isBlank(carrierBarcode)) {
            return;
        }
        EpsCarrierEntity carrierInfo = getFirstByBarCode(carrierBarcode);
        if (carrierInfo == null) {
            return;
        }
        Long count = epsCarrierLogService.getCountByBatchNumberCarriCode(carrierInfo.getBatchNumber(), carrierInfo.getCarrierBarcode(), entryReportNo);
        if (count == 0) {
            emptyCarrier(carrierBarcode);
        } else {
            useCarrierByEntryReportNo(entryReportNo);
        }
    }


    /**
     * 使用载具
     *
     * @param carrierBarcode 载具条码
     */
    @Override
    public void emptyCarrier(String carrierBarcode) {
        EpsCarrierEntity carrierInfo = getFirstByBarCode(carrierBarcode);
        if (carrierInfo == null) {
            throw new InkelinkException("无效的载具条码");
        }
        UpdateWrapper<EpsCarrierEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsCarrierEntity::getIsUse, false)
                .set(EpsCarrierEntity::getPracticalCount, 0)
                .eq(EpsCarrierEntity::getId, carrierInfo.getId());
        update(upset);

        UpdateWrapper<EpsCarrierLogEntity> uplog = new UpdateWrapper<>();
        uplog.lambda().set(EpsCarrierLogEntity::getIsUse, true)
                .eq(EpsCarrierLogEntity::getBatchNumber, carrierInfo.getBatchNumber())
                .eq(EpsCarrierLogEntity::getCarrierBarcode, carrierInfo.getCarrierBarcode());
        epsCarrierLogService.update(uplog);
    }

    /**
     * 获取载具上面的货物信息
     *
     * @param carrierCode 载具条码
     * @return 载具上面货物信息
     */
    @Override
    public CarrierMaterialsResponse getCarrierMaterials(String carrierCode) {
        EpsCarrierEntity carrierInfo = getFirstByBarCode(carrierCode);
        if (carrierInfo == null) {
            throw new InkelinkException("无效的载具条码");
        }
        CarrierMaterialsResponse info = new CarrierMaterialsResponse();
        info.setCarrierCode(carrierCode);
        info.setCapacity(carrierInfo.getMaterialCount());
        info.setPracticalCount(carrierInfo.getPracticalCount());
        info.setMaterials(epsCarrierLogService.getByBatchNumberCarriCode(carrierInfo.getBatchNumber(), carrierCode));
        info.setMaterialNo(carrierInfo.getMaterialNo());

        if (info.getMaterials().size() > 0) {
            info.setMaterialNo(carrierInfo.getMaterialNo());
            if (StringUtils.isBlank(carrierInfo.getMaterialNo())) {
                info.setMaterialNo(info.getMaterials().get(0).getMaterialNo());
            }
        }
        return info;
    }

    /**
     * 使用载具（解绑单个货物）
     *
     * @param entryReportNo 报工单号
     */
    @Override
    public void useCarrierByEntryReportNo(String entryReportNo) {
        EpsCarrierLogEntity carrierLogInfo = epsCarrierLogService.getFirstByEntryReportNo(entryReportNo);
        if (carrierLogInfo == null) {
            return;
        }
        EpsCarrierEntity carrierInfo = getFirstByBarCode(carrierLogInfo.getCarrierBarcode());
        if (carrierInfo == null) {
            throw new InkelinkException("无效的载具条码");
        }

        if (carrierInfo.getPracticalCount() > 0) {
            UpdateWrapper<EpsCarrierEntity> upset = new UpdateWrapper<>();
            upset.lambda().setSql(" PRACTICAL_COUNT = PRACTICAL_COUNT - " + carrierLogInfo.getMaterialCount())
                    .eq(EpsCarrierEntity::getId, carrierInfo.getId());
            update(upset);
        }
        UpdateWrapper<EpsCarrierLogEntity> uplog = new UpdateWrapper<>();
        uplog.lambda().set(EpsCarrierLogEntity::getIsUse, true)
                .eq(EpsCarrierLogEntity::getId, carrierLogInfo.getId());
        epsCarrierLogService.update(uplog);
    }

    /**
     * 查询载具上面的物料条码
     *
     * @param barcode 载具条码
     * @return 物料条码
     */
    @Override
    public String getTorrMaterialBarcode(String barcode) {
        //todo 用处？
        EpsCarrierEntity carrierInfo = getFirstByBarCode(barcode);
        if (carrierInfo == null) {
            return barcode;
        }
        EpsCarrierLogEntity logInfo = epsCarrierLogService.getFirstByBatchNumberCarriCode(carrierInfo.getBatchNumber(), carrierInfo.getCarrierBarcode());
        if (logInfo == null) {
            return barcode;
        }
        if (!StringUtils.isBlank(logInfo.getBarcode())) {
            return logInfo.getBarcode();
        } else {
            return barcode;
        }
    }
}