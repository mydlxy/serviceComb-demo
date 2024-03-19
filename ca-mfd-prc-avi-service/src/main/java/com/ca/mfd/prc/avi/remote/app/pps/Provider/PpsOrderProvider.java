package com.ca.mfd.prc.avi.remote.app.pps.Provider;

import com.ca.mfd.prc.avi.remote.app.pps.IPpsOrderService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PpsOrderProvider {
    @Autowired
    IPpsOrderService ppsOrderService;

    /**
     * 根据sn查询实体
     *
     * @param code 产品唯一标识
     * @return 实体
     */
    public PpsOrderEntity getPpsOrderBySnOrBarcode(String code) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 修改订单状态
     *
     * @param sn       产品唯一标识码
     * @param isFreeze 状态
     * @param remark   备注
     */
    public void operateIsFreezeById(String sn, Boolean isFreeze, String remark) {
        ResultVO<String> result = ppsOrderService.operateIsFreezeById(sn, isFreeze, remark);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsorder调用失败" + result.getMessage());
        }
    }

    /**
     * 获取类型为T的未逻辑删除的列表数据
     *
     * @param conditions 条件表达式
     * @return List<T>
     */
    public List<PpsOrderEntity> getData(List<ConditionDto> conditions) {
        ResultVO<List<PpsOrderEntity>> result = ppsOrderService.getData(conditions);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取整车信息
     *
     * @param tpsCode 产品唯一标识码
     * @return 整车信息
     */

    public PpsOrderEntity getPpsOrderInfo(String tpsCode) {
        ResultVO<PpsOrderEntity> result = ppsOrderService.getPpsOrderInfo(tpsCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsorder调用失败" + result.getMessage());
        }
        return result.getData();
    }

}
