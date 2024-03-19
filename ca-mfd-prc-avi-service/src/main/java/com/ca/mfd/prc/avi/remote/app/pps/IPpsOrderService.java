package com.ca.mfd.prc.avi.remote.app.pps;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsorder", contextId = "inkelink-pps-ppsorder")
public interface IPpsOrderService {

    /**
     * 根据sn查询实体
     *
     * @param code 产品唯一标识
     * @return 实体
     */
    @GetMapping("/provider/getppsorderbysnorbarcode")
    ResultVO<PpsOrderEntity> getPpsOrderBySnOrBarcode(@RequestParam("code") String code);

    /**
     * 根据产品唯一码冻结/解冻 订单
     *
     * @param sn       产品唯一码
     * @param isFreeze 冻结/解冻
     * @param remark   备注
     */
    @GetMapping("/provider/operateisfreezebyid")
    ResultVO<String> operateIsFreezeById(@RequestParam("sn") String sn,
                                         @RequestParam("isFreeze") Boolean isFreeze, @RequestParam("remark") String remark);

    /**
     * 获取类型为T的未逻辑删除的列表数据
     *
     * @param conditions 条件表达式
     * @return List<T>
     */
    @PostMapping("/provider/getdata")
    ResultVO<List<PpsOrderEntity>> getData(@RequestBody List<ConditionDto> conditions);

    /**
     * 获取整车信息
     *
     * @param tpsCode 产品唯一标识码
     * @return 整车信息
     */
    @GetMapping("/provider/getppsorderinfo")
    ResultVO<PpsOrderEntity> getPpsOrderInfo(@RequestParam("tpsCode") String tpsCode);
}
