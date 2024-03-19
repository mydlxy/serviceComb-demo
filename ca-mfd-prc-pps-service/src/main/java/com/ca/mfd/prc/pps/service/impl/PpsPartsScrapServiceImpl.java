package com.ca.mfd.prc.pps.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.pps.dto.PartsScrapPara;
import com.ca.mfd.prc.pps.dto.ScrapAffirmPara;
import com.ca.mfd.prc.pps.entity.PpsEntryPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.pps.entity.PpsPartsScrapEntity;
import com.ca.mfd.prc.pps.mapper.IPpsPartsScrapMapper;
import com.ca.mfd.prc.pps.remote.app.core.provider.SysSnConfigProvider;
import com.ca.mfd.prc.pps.service.IPpsEntryPartsService;
import com.ca.mfd.prc.pps.service.IPpsEntryReportPartsService;
import com.ca.mfd.prc.pps.service.IPpsPartsScrapService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @Description: 零件报废服务实现
 * @author inkelink
 * @date 2023年10月25日
 * @变更说明 BY inkelink At 2023年10月25日
 */
@Service
public class PpsPartsScrapServiceImpl extends AbstractCrudServiceImpl<IPpsPartsScrapMapper, PpsPartsScrapEntity> implements IPpsPartsScrapService {

    @Autowired
    private SysSnConfigProvider sysSnConfigProvider;
    @Autowired
    private IPpsEntryPartsService ppsEntryPartsService;
    @Autowired
    private IPpsEntryReportPartsService ppsEntryReportPartsService;
    @Override
    public void beforeInsert(PpsPartsScrapEntity model) {
        checkOrdeNo(model);
    }

    @Override
    public void beforeUpdate(PpsPartsScrapEntity model) {
        checkOrdeNo(model);
    }

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        if (idList == null || idList.size() == 0) {
            super.beforeDelete(idList);
            return;
        }
        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("ID", String.join("|", idList.stream().map(c -> c.toString()).collect(Collectors.toList())), ConditionOper.In));
        List<PpsPartsScrapEntity> datas = getData(conditionInfos);
        if (datas.stream().anyMatch(PpsPartsScrapEntity::getIsConfirm)) {
            throw new InkelinkException("已经确认的数据不允许删除");
        }
    }

    private void checkOrdeNo(PpsPartsScrapEntity ppsOrderScrapInfo) {
        if (StringUtils.isBlank(ppsOrderScrapInfo.getBarcode())) {
            throw new InkelinkException("请输入需要报废的产品条码");
        }
        QueryWrapper<PpsPartsScrapEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PpsPartsScrapEntity::getBarcode, ppsOrderScrapInfo.getBarcode())
                .eq(PpsPartsScrapEntity::getIsConfirm, false);
        if (selectCount(qry) > 0) {
            throw new InkelinkException("该产品已被报废请等待确认");
        }
    }

    /**
     * 报废
     *
     * @param request
     */
    @Override
    public void scrap(PartsScrapPara request) {
        PpsPartsScrapEntity ppsPartsScrapInfo = new PpsPartsScrapEntity();
        String snKey = "ppsPartsScrpNo_";
        switch (request.getOrderCategory()) {
            case 3:
            case 4: {
                snKey += request.getOrderCategory() == 3 ? "Cast" : "Machining";
                PpsEntryReportPartsEntity reportInfo = ppsEntryReportPartsService.getByBarcodeAndCatory(request.getBarcode(), request.getOrderCategory());
                if (reportInfo == null) {
                    throw new InkelinkException("未找到对应报工单");
                }
                if (reportInfo.getReportType() == 7) {
                    throw new InkelinkException("报工单" + request.getBarcode() + "已经报废，不能重复处理");
                }
                ppsPartsScrapInfo.setBarcode(request.getBarcode());
                ppsPartsScrapInfo.setOrderCategory(request.getOrderCategory());
                ppsPartsScrapInfo.setRemark(request.getRemark());
                ppsPartsScrapInfo.setIsSend(false);
                ppsPartsScrapInfo.setIsConfirm(false);
                ppsPartsScrapInfo.setQty(request.getQty());
                ppsPartsScrapInfo.setEntryNo(reportInfo.getEntryNo());
            }
            break;
            case 5:
            case 6: {
                snKey += request.getOrderCategory() == 5 ? "Stamping" : "Coverplate";
                PpsEntryPartsEntity entryInfo = ppsEntryPartsService.getFirstByEntryNoAndCategory(request.getBarcode(), request.getOrderCategory());
                if (entryInfo == null) {
                    throw new InkelinkException("未找到对应工单");
                }
                ppsPartsScrapInfo.setBarcode("");
                ppsPartsScrapInfo.setOrderCategory(request.getOrderCategory());
                ppsPartsScrapInfo.setRemark(request.getRemark());
                ppsPartsScrapInfo.setIsSend(false);
                ppsPartsScrapInfo.setIsConfirm(false);
                ppsPartsScrapInfo.setQty(request.getQty());
                ppsPartsScrapInfo.setEntryNo(entryInfo.getEntryNo());
            }
            break;
            default:
                break;
        }
        ppsPartsScrapInfo.setScrapNo(sysSnConfigProvider.createSn(snKey));

        insert(ppsPartsScrapInfo);
    }

    /**
     * 报废确认
     *
     * @param para
     */
    @Override
    public void scrapAffirm(ScrapAffirmPara para) {
        PpsPartsScrapEntity scrapInfo = get(para.getDataId());
        if (scrapInfo == null) {
            throw new InkelinkException("未找到报废单");
        }
        if (scrapInfo.getIsConfirm()) {
            return;
        }
        //报废后 压铸 机加江对应报工单状态改为报废
        switch (scrapInfo.getOrderCategory()) {
            case 3:
            case 4:
                PpsEntryReportPartsEntity reportInfo = ppsEntryReportPartsService.getByBarcodeAndCatory(scrapInfo.getBarcode(), scrapInfo.getOrderCategory());
                if (reportInfo == null) {
                    throw new InkelinkException("未找到对应报工单");
                }
                UpdateWrapper<PpsEntryReportPartsEntity> upReport = new UpdateWrapper<>();
                upReport.lambda().set(PpsEntryReportPartsEntity::getReportType, 7)
                        .eq(PpsEntryReportPartsEntity::getId, reportInfo.getId());
                ppsEntryReportPartsService.update(upReport);
                break;
            default:
                break;

        }
        UpdateWrapper<PpsPartsScrapEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(PpsPartsScrapEntity::getIsConfirm, true)
                .set(PpsPartsScrapEntity::getConfirmRemark, para.getRemark())
                .eq(PpsPartsScrapEntity::getId, para.getDataId());
        update(upset);
    }
}