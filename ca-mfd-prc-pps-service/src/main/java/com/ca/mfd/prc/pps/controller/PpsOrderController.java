package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.TopDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.*;
import com.ca.mfd.prc.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductCharacteristicsEntity;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import com.ca.mfd.prc.pps.service.IPpsOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 订单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsorder")
@Tag(name = "订单")
public class PpsOrderController extends BaseController<PpsOrderEntity> {

    private final IPpsOrderService ppsOrderService;
    @Autowired
    IPpsLogicService ppsLogicService;


    @Autowired
    public PpsOrderController(IPpsOrderService ppsOrderService) {
        this.crudService = ppsOrderService;
        this.ppsOrderService = ppsOrderService;
    }

    @Operation(summary = "冻结选中订单")
    @PostMapping("freeze")
    public ResultVO<String> freeze(@RequestBody PpsDataIdsPara para) {
        if (para.getPpsDataIds() == null || para.getPpsDataIds().isEmpty()) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        List<Long> list = ConvertUtils.stringToLongs(para.getPpsDataIds());
        ppsOrderService.freeze(list);
        ppsOrderService.saveChange();
        ppsOrderService.sendAsKeepCarMessage(list, true);
        return new ResultVO<String>().ok("", "设置滑线车成功");
    }

    @Operation(summary = "取消订单冻结")
    @PostMapping("unfreeze")
    public ResultVO<String> unFreeze(@RequestBody PpsDataIdsPara para) {
        if (para.getPpsDataIds() == null || para.getPpsDataIds().isEmpty()) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        List<Long> list = ConvertUtils.stringToLongs(para.getPpsDataIds());
        ppsOrderService.unFreeze(list);
        ppsOrderService.saveChange();
        ppsOrderService.sendAsKeepCarMessage(list, false);
        return new ResultVO<String>().ok("", "取消设置滑线车成功");
    }

    @Operation(summary = "冻结选中订单")
    @PostMapping("freezefororder")
    public ResultVO<String> freezeForOrder(String code, String remark) {
        PpsOrderEntity order = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        if (order == null) {
            throw new InkelinkException("没有找到车辆订单");
        }
        if (StringUtils.isBlank(remark)) {
            throw new InkelinkException("请输入备注");
        }
        List<Long> list = new ArrayList<>();
        list.add(order.getId());
        ppsOrderService.freeze(list, "【AVI 冻结，车辆信息：" + code + "】" + remark);
        ppsOrderService.saveChange();

        ppsOrderService.sendAsKeepCarMessage(list, true);
        return new ResultVO<String>().ok("", "冻结成功");
    }

    @Operation(summary = "取消订单冻结")
    @PostMapping("unfreezefororder")
    public ResultVO<String> unFreezeForOrder(String code, String remark) {
        PpsOrderEntity order = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        if (order == null) {
            throw new InkelinkException("没有找到车辆订单");
        }
        if (StringUtils.isBlank(remark)) {
            throw new InkelinkException("请输入备注");
        }
        List<Long> list = new ArrayList<>();
        list.add(order.getId());
        ppsOrderService.unFreeze(list, "【AVI 取消冻结，车辆信息：" + code + "】" + remark);
        ppsOrderService.saveChange();
        ppsOrderService.sendAsKeepCarMessage(list, false);
        return new ResultVO<String>().ok("", "取消冻结成功");
    }

    @Operation(summary = "根据车辆识别码、Vin获取车辆信息")
    @GetMapping("getvehicleinfobyobjectno")
    public ResultVO<PpsOrderEntity> getVehicleInfoByObjectNo(String objectNo) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderInfoByKey(objectNo);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "订单匹配切换验证")
    @PostMapping("validvehiclechangetps")
    public ResultVO<String> validVehicleChangeTps(@RequestBody VehicleChangeTpsInfo vehicleChangeTpsInfo) {
        String message = ppsOrderService.validVehicleChangeTps(vehicleChangeTpsInfo);
        ppsOrderService.saveChange();
        return new ResultVO<String>().ok("", message);
    }

    @Operation(summary = "订单匹配切换")
    @PostMapping("vehiclechangetps")
    public ResultVO<String> vehicleChangeTps(@RequestBody VehicleChangeTpsInfo vehicleChangeTpsInfo) {
        ppsOrderService.vehicleChangeTps(vehicleChangeTpsInfo);
        ppsOrderService.saveChange();
        return new ResultVO<String>().ok("", "订单匹配切换成功");
    }

    @Operation(summary = "获取计划的bom数据")
    @GetMapping("getorderbom")
    public ResultVO<List<PmProductBomEntity>> getorderBom(String ppsOrderNo) {
        List<PmProductBomEntity> list = ppsOrderService.getOrderBom(ppsOrderNo);
        return new ResultVO<List<PmProductBomEntity>>().ok(list);
    }

    @Operation(summary = "获取计划的特征数据")
    @GetMapping("getordercharacteristic")
    public ResultVO<List<PmProductCharacteristicsEntity>> getOrderCharacteristic(String ppsOrderNo) {
        List<PmProductCharacteristicsEntity> list = ppsOrderService.getOrderCharacteristicByOrderNo(ppsOrderNo);
        return new ResultVO<List<PmProductCharacteristicsEntity>>().ok(list);
    }

    @Operation(summary = "获取计划的特征数据")
    @GetMapping("getordercharacteristicbyorderid")
    public ResultVO<List<PmProductCharacteristicsEntity>> getOrderCharacteristicByOrderId(String orderid) {
        List<PmProductCharacteristicsEntity> list = ppsOrderService.getOrderCharacteristicByOrderId(ConvertUtils.stringToLong(orderid));
        return new ResultVO<List<PmProductCharacteristicsEntity>>().ok(list);
    }

    @Operation(summary = "重置车辆")
    @PostMapping("resetvehicle")
    public ResultVO resetvehicle(@RequestBody ResetVehiclePara para) {
        ppsOrderService.resetVehicle(para);
        return new ResultVO().ok("", "重置车辆成功");
    }

    @Operation(summary = "设置工艺路径")
    @PostMapping("setprocess")
    public ResultVO<String> setProcess(@RequestBody OrderProcessInfo orderProcessInfo) {
        if (orderProcessInfo.getPlanIds() == null || orderProcessInfo.getPlanIds().isEmpty()) {
            throw new InkelinkException("请选择需要设置的数据");
        }
        Long processId = ConvertUtils.stringToLong(orderProcessInfo.getProcessId());
        if (processId <= 0) {
            throw new InkelinkException("请选择需生产区域");
        }
        ppsOrderService.setProcess(ConvertUtils.stringToLongs(orderProcessInfo.getPlanIds()), processId);
        ppsOrderService.saveChange();
        return new ResultVO<String>().ok("", "设置工艺路径成功");
    }

    @Operation(summary = "删除备件订单（手动创建的备件）")
    @PostMapping("deletestamping")
    public ResultVO deleteStamping(@RequestBody IdsModel model) {
        ResultVO<String> value = new ResultVO<>();
        List<Long> ids = ConvertUtils.stringToLongs(Arrays.asList(model.getIds()));
        if (ids != null && ids.size() <= 0) {
            throw new InkelinkException("请选择要删除的数据");
        }
        ppsOrderService.deleteStamping(ids);
        ppsOrderService.saveChange();
        return value.ok("", "删除成功");
    }

    @Operation(summary = "获取整车信息")
    @PostMapping("/provider/getdata")
    public ResultVO<List<PpsOrderEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(ppsOrderService.getData(conditions));
    }

    @Operation(summary = "获取整车信息")
    @GetMapping("/provider/getppsorderinfo")
    public ResultVO<PpsOrderEntity> getPpsOrderInfo(String tpsCode) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderInfoByKey(tpsCode);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "获取整车信息")
    @GetMapping("/provider/getppsorderinfobykey")
    public ResultVO<PpsOrderEntity> getPpsOrderInfoByKey(String tpsCode) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderInfoByKey(tpsCode);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "根据唯一码 或者 条码查询订单")
    @GetMapping("/provider/getppsorderbysnorbarcode")
    public ResultVO<PpsOrderEntity> getPpsOrderBySnOrBarcode(String code) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderBySnOrBarcode(code);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "根据唯一码集合 或者 条码集合查询订单")
    @GetMapping("/provider/getppsorderbysnsorbarcodes")
    public ResultVO<List<PpsOrderEntity>> getPpsOrderBySnsOrBarcodes(@RequestBody List<String> codes) {
        List<PpsOrderEntity> ppsOrderList = ppsOrderService.getPpsOrderBySnsOrBarcodes(codes);
        return new ResultVO<List<PpsOrderEntity>>().ok(ppsOrderList);
    }

    @Operation(summary = "获取订单数量（模糊匹配）")
    @GetMapping("/provider/gettoporderbycodelike")
    public ResultVO<List<PpsOrderEntity>> getTopOrderByCodeLike(Integer top, String code) {
        return new ResultVO<List<PpsOrderEntity>>().ok(ppsOrderService.getTopOrderByCodeLike(top, code));
    }


    @Operation(summary = "获取订单信息")
    @GetMapping("/provider/getorderinfo")
    public ResultVO<PpsOrderEntity> getOrderInfo(String tpsCode) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderBySnOrBarcode(tpsCode);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "获取订单信息")
    @GetMapping("/provider/getppsorderbysn")
    public ResultVO<PpsOrderEntity> getPpsOrderBySn(String sn) {
        PpsOrderEntity ppsOrderEntity = ppsOrderService.getPpsOrderBySn(sn);
        return new ResultVO<PpsOrderEntity>().ok(ppsOrderEntity);
    }

    @Operation(summary = "获取订单信息")
    @PostMapping("/provider/page")
    public ResultVO page(@RequestBody PageDataDto model) {
        PageData<PpsOrderEntity> ppsOrderEntity = ppsOrderService.page(model);
        return new ResultVO<>().ok(ppsOrderEntity);
    }

    @Operation(summary = "获取订单信息")
    @PostMapping("/provider/gettopdatas")
    public ResultVO getTopDatas(@RequestBody TopDataDto top) {
        List<PpsOrderEntity> ppsOrderEntity = ppsOrderService.getTopDatas(top.getTop(), top.getConditions(), top.getSorts());
        return new ResultVO<>().ok(ppsOrderEntity);
    }

    @Operation(summary = "根据产品唯一码冻结/解冻 订单")
    @GetMapping("/provider/operateisfreezebyid")
    public ResultVO<String> operateIsFreezeById(String sn, Boolean isFreeze, String remark) {
        ppsOrderService.operateIsFreezeById(sn, isFreeze, remark);
        ppsOrderService.saveChange();
        PpsOrderEntity order = ppsOrderService.getPpsOrderBySnOrBarcode(sn);
        ppsOrderService.sendAsKeepCarMessage(Arrays.asList(order.getId()), isFreeze);
        return new ResultVO<String>().ok("修改成功");
    }

    @Operation(summary = "获取车辆订单信息列表")
    @PostMapping("/provider/getlistbysncodes")
    public ResultVO<List<PpsOrderEntity>> getListBySnCodes(@RequestBody List<String> snCodes) {
        return new ResultVO<List<PpsOrderEntity>>().ok(ppsOrderService.getListBySnCodes(snCodes));
    }

    @Operation(summary = "获取订单信息")
    @PostMapping("/provider/getlistbybarcodes")
    public ResultVO<List<PpsOrderEntity>> getListByBarcodes(@RequestBody List<String> barcodes) {
        return new ResultVO<List<PpsOrderEntity>>().ok(ppsOrderService.getListByBarcodes(barcodes));
    }

    @Operation(summary = "校验特征10")
    @PostMapping("/provider/checkcharacteristic10")
    public ResultVO<Boolean> checkCharacteristic10(@RequestBody List<String> characteristics) {
        return new ResultVO<Boolean>().ok(ppsOrderService.checkCharacteristic10(characteristics));
    }

    @Operation(summary = "根据订单号获取BOM信息")
    @PostMapping("/provider/getorderbombyorderid")
    public ResultVO<List<PmProductBomEntity>> getOrderBomByOrderId(Long orderId) {
        return new ResultVO<List<PmProductBomEntity>>().ok(ppsOrderService.getOrderBomByOrderId(orderId));
    }

}