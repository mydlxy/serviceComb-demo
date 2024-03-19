package com.ca.mfd.prc.pps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.pps.dto.ChangeEstimatedStartDtInfo;
import com.ca.mfd.prc.pps.dto.CutEntityPara;
import com.ca.mfd.prc.pps.dto.EntryStatusPara;
import com.ca.mfd.prc.pps.dto.OrderEntryInfo;
import com.ca.mfd.prc.pps.dto.RestEntryQueuePara;
import com.ca.mfd.prc.pps.dto.SetEntryLockPara;
import com.ca.mfd.prc.pps.dto.ShopPlanMonitorInfo;
import com.ca.mfd.prc.pps.entity.PpsEntryConfigEntity;
import com.ca.mfd.prc.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.pps.service.IPpsEntryConfigService;
import com.ca.mfd.prc.pps.service.IPpsEntryService;
import com.ca.mfd.prc.pps.service.IPpsLogicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 工单
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-04
 */
@RestController
@RequestMapping("ppsentry")
@Tag(name = "工单")
public class PpsEntryController extends BaseController<PpsEntryEntity> {

    private final IPpsEntryService ppsEntryService;

    @Autowired
    private IPpsLogicService ppsLogicService;
    @Autowired
    private IPpsEntryConfigService ppsEntryConfigService;

    @Autowired
    public PpsEntryController(IPpsEntryService ppsEntryService) {
        this.crudService = ppsEntryService;
        this.ppsEntryService = ppsEntryService;
    }

    @GetMapping("getip")
    @Operation(summary = "获取IP")
    public Map<String,String> getip(HttpServletRequest request){
        Map<String,String> map = new HashMap<>();
        map.put("真实ip",request.getHeader("X-real-ip"));
        map.put("ip列表",request.getHeader("X-Forwarded-For"));
        map.put("转发ip",request.getRemoteAddr());
        map.put("获取ip", IpUtils.getIpAddr(request));
        return map;
    }

    @PostMapping(value = "getpagedata", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    @Override
    public ResultVO<PageData<PpsEntryEntity>> getPageData(@RequestBody PageDataDto model) {
        if (model != null && model.getSorts() != null && !model.getSorts().isEmpty()) {
            for (SortDto sot : model.getSorts()) {
                if (StringUtils.equalsIgnoreCase(sot.getColumnName(), "subScriubCode")) {
                    sot.setColumnName("subscriubeCode");
                }
            }
        }
        if (model != null && model.getConditions() != null && !model.getConditions().isEmpty()) {
            for (ConditionDto sot : model.getConditions()) {
                if (StringUtils.equalsIgnoreCase(sot.getColumnName(), "subScriubCode")) {
                    sot.setColumnName("subscriubeCode");
                }
            }
        }
        PageData<PpsEntryEntity> page = crudService.page(model);
        return new ResultVO<PageData<PpsEntryEntity>>().ok(page, "获取数据成功");
    }

    @Operation(summary = "获取车间工单")
    @PostMapping("getshoporders")
    public ResultVO<PageData<OrderEntryInfo>> getShopOrders(@RequestBody PageDataDto model) {
        ResultVO<PageData<OrderEntryInfo>> result = new ResultVO<>();
        result.setMessage("获取焊装订单列表成功");
        PageData<OrderEntryInfo> page = new PageData<>();
        page.setPageIndex(model.getPageIndex() == null ? 1 : model.getPageIndex());
        page.setPageSize(model.getPageSize() == null ? 20 : model.getPageSize());
        ppsEntryService.getShopOrders(model.getConditions(), model.getSorts(), page);
        return result.ok(page);
    }

    @Operation(summary = "获取订阅码工单(分线工单)未下发的工单列表")
    @GetMapping("getbranchingentryunissued")
    public ResultVO<List<OrderEntryInfo>> getBranchingEntryUnissued(String subCode, String model, Integer take) {
        if (take == null) {
            take = 1;
        }
        if (model == null) {
            model = "";
        }
        return new ResultVO<List<OrderEntryInfo>>().ok(ppsEntryService.getBranchingEntryUnissued(subCode, model, take));
    }

    @Operation(summary = "获取订阅码工单(分线工单)已下发的工单列表")
    @GetMapping("getbranchingentryissued")
    public ResultVO<List<OrderEntryInfo>> getBranchingEntryIssued(String subCode, String model, Integer take) {
        if (take == null) {
            take = 1;
        }
        if (model == null) {
            model = "";
        }
        return new ResultVO<List<OrderEntryInfo>>().ok(ppsEntryService.getBranchingEntryIssued(subCode, model, take));
    }

    @Operation(summary = "车间分线订单监控")
    @PostMapping("getshopplanmonitorinfo")
    public ResultVO<ShopPlanMonitorInfo> getShopPlanMonitorInfos(@RequestBody ShopPlanMonitorInfo shopPlanMonitorInfo) {
        ppsEntryService.getShopPlanMonitorInfos(shopPlanMonitorInfo);
        return new ResultVO<ShopPlanMonitorInfo>().ok(shopPlanMonitorInfo);
    }

    @Operation(summary = "设置状态")
    @PostMapping("setstatus")
    public ResultVO<String> setStatus(@RequestBody EntryStatusPara para) {
        ppsEntryService.setStatus(para);
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @Operation(summary = "重置分线工单队列")
    @PostMapping("restentryqueue")
    public ResultVO<String> restEntryQueue(@RequestBody CutEntityPara para) {
        if (!StringUtils.isBlank(para.getLineCode())) {
            PpsEntryConfigEntity configInfo = ppsEntryConfigService.getFirstByLineCode(para.getLineCode());
            if (configInfo == null) {
                throw new InkelinkException("线体" + para.getLineCode() + "未配置分线工单");
            }
            para.setSubScriubCode(configInfo.getSubCode());
        }
        if (!StringUtils.isBlank(para.getSubScriubCode())) {
            PpsEntryConfigEntity configInfo = ppsEntryConfigService.getFirstBySubCode(para.getSubScriubCode());
            if (configInfo == null) {
                throw new InkelinkException("未找到订阅吗" + para.getSubScriubCode() + "对应的分线工单配置");
            }
            para.setSubScriubCode(configInfo.getSubCode());
        }
        RestEntryQueuePara reEt = new RestEntryQueuePara();
        reEt.setSubScriubCode(para.getSubScriubCode());
        reEt.setSn(para.getSn());
        ppsEntryService.restEntryQueue(reEt);
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "重置队列成功");
    }

    @Operation(summary = "重新生成工艺")
    @PostMapping("resetwo")
    public ResultVO<String> resetWo(@RequestBody(required = false) String reqJson) {
        if (StringUtils.isBlank(reqJson) || "{}".equals(reqJson.trim())) {
            return new ResultVO<String>().error("没有找到订单");
        }
        List<String> entryIds = JsonUtils.parseArray(reqJson, String.class);
        if (entryIds == null || entryIds.isEmpty()) {
            return new ResultVO<String>().error("没有找到订单");
        }
        for (String entryId : entryIds) {
            Long eid = ConvertUtils.stringToLong(entryId);
            PpsEntryEntity entry = ppsEntryService.get(eid);
            if (entry != null) {
                if (entry.getStatus() > 1) {
                    ppsEntryService.resetWo(eid);
                } else {
                    throw new InkelinkException("订单：" + entry.getOrderNo() + ",状态不正确");
                }
            } else {
                throw new InkelinkException("没有找到订单");
            }
        }
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "重置工艺成功");
    }

    @Operation(summary = "重新生成工艺")
    @PostMapping("resetwobyordernos")
    public ResultVO<String> resetWoByOrderNos(@RequestBody(required = false) String reqJson) {
        if (StringUtils.isBlank(reqJson) || "{}".equals(reqJson.trim())) {
            return new ResultVO<String>().ok("", "重置工艺成功");
        }
        List<String> orderNos = JsonUtils.parseArray(reqJson, String.class);
        if (orderNos == null || orderNos.isEmpty()) {
            return new ResultVO<String>().ok("", "重置工艺成功");
        }
        for (String orderNo : orderNos) {
            PpsEntryEntity entry = ppsEntryService.getPpsEntryEntityByOrderNo(orderNo, 1);
            if (entry != null) {
                if (entry.getStatus() > 1) {
                    ppsEntryService.resetWo(entry.getId());
                } else {
                    throw new InkelinkException("订单：" + entry.getOrderNo() + ",状态不正确");
                }
            } else {
                throw new InkelinkException("没有找到订单");
            }
        }
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "重置工艺成功");
    }

    @Operation(summary = "更改预计上线时间")
    @PostMapping("changeestimatedstartdt")
    public ResultVO<String> changeEstimatedStartDt(@RequestBody ChangeEstimatedStartDtInfo changeEstimatedStartDtInfo) {
        if (changeEstimatedStartDtInfo.getIds() == null || changeEstimatedStartDtInfo.getIds().size() == 0) {
            throw new InkelinkException("没有需要更新的数据");
        }
        ppsEntryService.changeEstimatedStartDt(changeEstimatedStartDtInfo.getEstimatedStartDt(), ConvertUtils.stringToLongs(changeEstimatedStartDtInfo.getIds()));
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "更新成功");
    }

    @Operation(summary = "更改预计下线时间")
    @PostMapping("changeestimatedenddt")
    public ResultVO<String> changeEstimatedEndDt(@RequestBody ChangeEstimatedStartDtInfo changeEstimatedStartDtInfo) {
        if (changeEstimatedStartDtInfo.getIds() == null || changeEstimatedStartDtInfo.getIds().size() == 0) {
            throw new InkelinkException("没有需要更新的数据");
        }
        ppsEntryService.changeEstimatedEndDt(changeEstimatedStartDtInfo.getEstimatedStartDt(), ConvertUtils.stringToLongs(changeEstimatedStartDtInfo.getIds()));
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("", "更新成功");
    }

    @Operation(summary = "获取条件数据")
    @PostMapping("/provider/getdata")
    public ResultVO getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(ppsEntryService.getData(conditions));
    }

    @Operation(summary = "获取条件数据")
    @GetMapping("/provider/getfirstentrytypeshopcodesn")
    public ResultVO getFirstEntryTypeShopCodeSn(String sn, Integer entryType, String shopCode) {
        return new ResultVO<PpsEntryEntity>().ok(ppsEntryService.getFirstEntryTypeShopCodeSn(sn, entryType, shopCode));
    }

    @Operation(summary = "根据TPS查询工单列表")
    @GetMapping("/provider/getppsentrybysn")
    public ResultVO<List<PpsEntryEntity>> getPpsEntryBySn(@RequestParam String tpsCode) {
        return new ResultVO<List<PpsEntryEntity>>().ok(ppsEntryService.getPpsEntryBySn(tpsCode));
    }

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 获取一个列表
     */
    @Operation(summary = "获取已下发未打印焊装上上线列表")
    @GetMapping("/provider/getNoPrintTpscode")
    public ResultVO<List<BodyVehicleDTO>> getNoPrintTpscode() {
        return new ResultVO<List<BodyVehicleDTO>>().ok(ppsEntryService.getNoPrintTpscode());
    }

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    @Operation(summary = "获取已下发已打印焊装上线列表")
    @GetMapping("/provider/getprinttpscode")
    public ResultVO<List<BodyVehicleDTO>> getPrintTpscode() {
        return new ResultVO<List<BodyVehicleDTO>>().ok(ppsEntryService.getPrintTpscode());
    }

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    @Operation(summary = "读取已经下发队列")
    @GetMapping("/provider/getdowntpscode")
    public ResultVO<List<BodyVehicleDTO>> getDownTpsCode() {
        return new ResultVO<List<BodyVehicleDTO>>().ok(ppsEntryService.getDownTpsCode());
    }

    /**
     * 设置TPS码为已打印
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 参数车间编码
     */
    @Operation(summary = "设置TPS码为已打印")
    @GetMapping("/provider/setprinttpscode")
    public ResultVO<String> setPrintTpsCode(String tpsCode, String shopCode) {
        ppsEntryService.setPrintTpsCode(tpsCode, shopCode);
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("成功");
    }

    /**
     * 设置车辆为上线
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 车间code
     */
    @Operation(summary = "设置车辆为上线")
    @GetMapping("/provider/setbodyentryonline")
    public ResultVO<String> setBodyEntryOnline(String tpsCode, String shopCode) {
        ppsEntryService.setBodyEntryOnline(tpsCode, shopCode);
        ppsEntryService.saveChange();
        return new ResultVO<String>().ok("成功");
    }

}