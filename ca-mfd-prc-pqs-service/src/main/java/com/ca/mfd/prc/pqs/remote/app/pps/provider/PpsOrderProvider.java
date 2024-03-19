package com.ca.mfd.prc.pqs.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.pqs.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class PpsOrderProvider {

    @Autowired
    private IPpsOrderService ppsOrderService;

    public List<PpsOrderEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public PpsOrderEntity getPpsOrderInfoByKey(String tpsCode) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderInfoByKey(tpsCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public PpsOrderEntity getPpsOrderBySnOrBarcode(String code) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public List<PpsOrderEntity> getPpsOrderBySnsOrBarcodes(List<String> codes) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getPpsOrderBySnsOrBarcodes(codes);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }
    
    public List<PpsOrderEntity> getListBySnCodes(List<String> snCodes) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getListBySnCodes(snCodes);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }
}