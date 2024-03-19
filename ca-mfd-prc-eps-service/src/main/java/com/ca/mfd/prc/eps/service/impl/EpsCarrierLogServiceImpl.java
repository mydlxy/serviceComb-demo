package com.ca.mfd.prc.eps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.eps.mapper.IEpsCarrierLogMapper;
import com.ca.mfd.prc.eps.entity.EpsCarrierLogEntity;
import com.ca.mfd.prc.eps.service.IEpsCarrierLogService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 *
 * @Description: 载具日志服务实现
 * @author inkelink
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@Service
public class EpsCarrierLogServiceImpl extends AbstractCrudServiceImpl<IEpsCarrierLogMapper, EpsCarrierLogEntity> implements IEpsCarrierLogService {

    @Override
    public List<EpsCarrierLogEntity> getByBatchNumberCarriCode(String batchNumber, String carrierCode) {
        QueryWrapper<EpsCarrierLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierLogEntity::getBatchNumber, batchNumber)
                .eq(EpsCarrierLogEntity::getCarrierBarcode, carrierCode)
                .eq(EpsCarrierLogEntity::getIsUse, false)
                .orderByAsc(EpsCarrierLogEntity::getCreationDate);
        return selectList(qry);
    }

    @Override
    public EpsCarrierLogEntity getFirstByEntryReportNo(String entryReportNo) {
        QueryWrapper<EpsCarrierLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierLogEntity::getEntryReportNo, entryReportNo)
                .eq(EpsCarrierLogEntity::getIsUse, false)
                .orderByDesc(EpsCarrierLogEntity::getCreationDate);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public EpsCarrierLogEntity getFirstByBatchNumberCarriCode(String batchNumber, String carrierCode) {
        QueryWrapper<EpsCarrierLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierLogEntity::getBatchNumber, batchNumber)
                .eq(EpsCarrierLogEntity::getCarrierBarcode, carrierCode)
                .eq(EpsCarrierLogEntity::getIsUse, false);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    @Override
    public String getCarrierBarcodeByEntryReportNo(String entryReportNo) {
        QueryWrapper<EpsCarrierLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierLogEntity::getEntryReportNo, entryReportNo)
                .select(EpsCarrierLogEntity::getCarrierBarcode);
        EpsCarrierLogEntity et = getTopDatas(1, qry).stream().findFirst().orElse(null);
        return et == null ? "" : et.getCarrierBarcode();
    }

    @Override
    public Long getCountByBatchNumberCarriCode(String batchNumber, String carrierCode, String entryReportNo) {
        QueryWrapper<EpsCarrierLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(EpsCarrierLogEntity::getBatchNumber, batchNumber)
                .eq(EpsCarrierLogEntity::getCarrierBarcode, carrierCode)
                .eq(EpsCarrierLogEntity::getIsUse, false)
                .ne(EpsCarrierLogEntity::getEntryReportNo, entryReportNo);
        return selectCount(qry);
    }

}