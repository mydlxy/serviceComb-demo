package com.ca.mfd.prc.eps.communication.remote.app.pps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * PpsOrderProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service("MidPpsOrderProvider")
public class PpsOrderProvider {

    @Autowired
    private IPpsOrderService ppsOrderService;

    /**
     * 获取
     *
     * @param code
     * @return
     */
    public PpsOrderEntity getPpsOrderBySnOrBarcode(String code) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取订单数量（模糊匹配）
     *
     * @param code
     * @return
     */
    public List<PpsOrderEntity> getTopOrderByCodeLike(Integer top, String code) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getTopOrderByCodeLike(top, code);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param tpsCode
     * @return
     */
    public PpsOrderEntity getPpsOrderInfoByKey(String tpsCode) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderInfoByKey(tpsCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param orderid
     * @return
     */
    public List<PmProductCharacteristicsEntity> getOrderCharacteristicByOrderId(String orderid) {
        ResultVO<List<PmProductCharacteristicsEntity>> result = ppsOrderService.getOrderCharacteristicByOrderId(orderid);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @param barcodes
     * @return
     */
    public List<PpsOrderEntity> getListByBarcodes(List<String> barcodes) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getListByBarcodes(barcodes);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 校验特征10
     *
     * @param characteristics
     * @return
     */
    public Boolean checkCharacteristic10(List<String> characteristics) {
        ResultVO<Boolean> result = ppsOrderService.checkCharacteristic10(characteristics);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

}