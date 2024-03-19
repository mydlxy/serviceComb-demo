package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.dto.AviPassedRecordDTO;
import com.ca.mfd.prc.avi.dto.AviStationDTO;
import com.ca.mfd.prc.avi.dto.PringModel;
import com.ca.mfd.prc.avi.dto.TpsCodeSanDTO;
import com.ca.mfd.prc.avi.dto.TpsPrintParamDTO;
import com.ca.mfd.prc.avi.entity.AviOperationLogEntity;
import com.ca.mfd.prc.avi.entity.AviTrackingRecordEntity;
import com.ca.mfd.prc.avi.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pps.Provider.PpsOrderProvider;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsOrderEntity;
import com.ca.mfd.prc.avi.service.IAviFrontService;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IpUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.zebra.sdk.comm.TcpConnection;
import com.zebra.sdk.printer.ZebraPrinter;
import com.zebra.sdk.printer.ZebraPrinterFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * AviFrontController class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@RestController
@RequestMapping("avifront")
@Tag(name = "AVI前端对象")
public class AviFrontController {
    @Autowired
    private IAviFrontService aviFrontService;

    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;

    @Autowired
    private PpsOrderProvider ppsOrderProvider;

    /**
     * 获取Avi站点数据
     *
     * @param strIp   参数ip
     * @param request 请求参数上下文
     * @return 返回Avi站点数据
     */
    @Operation(summary = "获取Avi站点数据")
    @GetMapping("getavitemplate")
    public ResultVO<AviStationDTO> getAviTemplate(String strIp, HttpServletRequest request) {
        if (StringUtils.isBlank(strIp)) {
            strIp = IpUtils.getIpAddr(request);
        }
        AviStationDTO info = aviFrontService.getAviTemplate(strIp);
        return new ResultVO<AviStationDTO>().ok(info);
    }

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 返回已下发未打印焊装上上线列表
     */
    @Operation(summary = "获取已下发未打印焊装上上线列表")
    @GetMapping("getnoprinttpscode")
    public ResultVO<List<BodyVehicleDTO>> getNoPrintTpscode() {
        List<BodyVehicleDTO> list = aviFrontService.getNoPrintTpscode();
        return new ResultVO<List<BodyVehicleDTO>>().ok(list, "获取已下发未打印焊装上上线列表成功");
    }

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 返回获取已下发已打印焊装上线列表
     */
    @Operation(summary = "获取已下发已打印焊装上线列表")
    @GetMapping("getprinttpscode")
    public ResultVO<List<BodyVehicleDTO>> getPrintTpscode() {
        List<BodyVehicleDTO> list = aviFrontService.getPrintTpscode();
        return new ResultVO<List<BodyVehicleDTO>>().ok(list, "获取已下发已打印焊装上线列表成功");
    }

    /**
     * 打印条码
     *
     * @param params 参数列表
     * @return 操作结果
     */
    @Operation(summary = "打印条码")
    @PostMapping("printproductno")
    public ResultVO<String> printProductNo(@RequestBody PringModel params) {
        String printName = "PrintNoPrint";
        printName = sysConfigurationProvider.getConfiguration(params.getParams().getAviCode(), "AviPrintTps");
        if (StringUtils.isBlank(printName)) {
            throw new InkelinkException("系统配置未配置该avi打印机");
        }
        String zebra = sysConfigurationProvider.getConfiguration("Zebra", "AviPrintTps");
        if (StringUtils.isBlank(zebra)) {
            throw new InkelinkException("斑马条码没有找到");
        }

        String iPConfig = sysConfigurationProvider.getConfiguration("IPConfig", "AviPrintTps");
        if (StringUtils.isBlank(zebra)) {
            throw new InkelinkException("ip没有找到");
        }

        List<ConditionDto> conditionInfos = new ArrayList<>();
        conditionInfos.add(new ConditionDto("SN", params.getParams().getSn(), ConditionOper.Equal, ConditionRelation.Or));
        conditionInfos.add(new ConditionDto("BARCODE", params.getParams().getSn(), ConditionOper.Equal, ConditionRelation.Or));
        PpsOrderEntity orderId = ppsOrderProvider.getData(conditionInfos).stream().findFirst().orElse(null);
        if (orderId == null) {
            throw new InkelinkException("产品条码不存在" + params.getParams().getSn());
        }
        zebra = zebra.replace("Vin", orderId.getBarcode());
        zebra = zebra.replace("BarCode", orderId.getSn());
        try {
            com.zebra.sdk.comm.Connection connection = new TcpConnection(iPConfig, 9100);
            if (!connection.isConnected()) {
                connection.open();
            }
            ZebraPrinter printer = ZebraPrinterFactory.getInstance(connection);
            printer.sendCommand(zebra);
            connection.close();
        } catch (Exception exception) {
            throw new InkelinkException("斑马打印机连接异常,异常信息:" + exception.getMessage());
        }
        return new ResultVO<String>().ok("操作成功");
    }

    /**
     * 读取已经下发队列
     *
     * @return 返回已下发队列
     */
    @Operation(summary = "读取已经下发队列")
    @GetMapping("getdowntpscode")
    public ResultVO<List<BodyVehicleDTO>> getDownTpsCode() {
        List<BodyVehicleDTO> list = aviFrontService.getDownTpsCode();
        return new ResultVO<List<BodyVehicleDTO>>().ok(list, "获取已经下发队列列表成功");
    }

    /**
     * 产品识别码打印
     *
     * @param tpsPrintParamInfo 参数列表
     * @return 操作结果
     */
    @Operation(summary = "产品识别码打印")
    @PostMapping("tpscodeprint")
    public ResultVO<String> tpsCodePrint(@RequestBody TpsPrintParamDTO tpsPrintParamInfo) {
        aviFrontService.tpsCodePrint(tpsPrintParamInfo);
        return new ResultVO<String>().ok("条码打印成功");
    }

    /**
     * 焊装上线点扫描
     *
     * @return 扫描结果
     */
    @Operation(summary = "焊装上线点扫描")
    @PostMapping("tpscodescan")
    public ResultVO<String> tpsCodeScan(@RequestBody TpsCodeSanDTO tpsCodeSanInfo) {
        aviFrontService.tpsCodeScan(tpsCodeSanInfo);
        return new ResultVO<String>().ok("上线扫描成功");
    }

    /**
     * 获取过点数据
     *
     * @param aviCode avi 编码
     * @return 返回过点数据列表
     */
    @Operation(summary = "获取过点数据")
    @GetMapping("getavipassedrecord")
    public ResultVO<List<AviPassedRecordDTO>> getAviPassedRecord(String aviCode) {
        List<AviPassedRecordDTO> list = aviFrontService.getAviPassedRecord(aviCode);
        return new ResultVO<List<AviPassedRecordDTO>>().ok(list, "获取过点数据成功");
    }


    /**
     * 获取信息履历
     *
     * @param model 分页参数
     * @return 信息履历分页列表
     */
    @Operation(summary = "获取信息履历")
    @PostMapping("getavilogoperation")
    public ResultVO<PageData<AviOperationLogEntity>> getAviLogOperation(@RequestBody PageDataDto model) {
        PageData<AviOperationLogEntity> result = aviFrontService.getAviLogOperation(model);
        return new ResultVO<PageData<AviOperationLogEntity>>().ok(result, "获取信息履历成功");
    }

    /**
     * 获取过点记录
     *
     * @param model 分页参数
     * @return 过点记录分页列表
     */
    @Operation(summary = "获取过点记录")
    @PostMapping("gettrackingrec")
    public ResultVO<PageData<AviTrackingRecordEntity>> getTrackingRec(@RequestBody PageDataDto model) {
        PageData<AviTrackingRecordEntity> result = aviFrontService.getTrackingRec(model);
        return new ResultVO<PageData<AviTrackingRecordEntity>>().ok(result, "获取数据成功");
    }

    /**
     * 获取车间列表
     *
     * @return 返回车间列表
     */
    @Operation(summary = "获取车间列表")
    @GetMapping("getshopinfos")
    public ResultVO<List<PmWorkShopEntity>> getShopInfos() {
        List<PmWorkShopEntity> list = aviFrontService.getShopInfos();
        return new ResultVO<List<PmWorkShopEntity>>().ok(list, "获取数据成功");
    }

    /**
     * 根据车间获取线体
     *
     * @param pmShopId 车间ID
     * @return 返回车间下面的线体列表
     */
    @Operation(summary = "根据车间获取线体")
    @GetMapping("getareainfos")
    public ResultVO<List<PmLineEntity>> getAreaInfos(String pmShopId) {
        List<PmLineEntity> list = aviFrontService.getAreaInfos(ConvertUtils.stringToLong(pmShopId));
        return new ResultVO<List<PmLineEntity>>().ok(list, "获取数据成功");
    }

    /**
     * 根据线体获得下边AVI
     *
     * @param pmAreaId 线体ID
     * @return 返回当前线体下面的AVI列表
     */
    @Operation(summary = "根据线体获得下边AVI")
    @GetMapping("getaviinfos")
    public ResultVO<List<PmAviEntity>> getAviInfos(String pmAreaId) {
        List<PmAviEntity> list = aviFrontService.getAviInfos(pmAreaId);
        return new ResultVO<List<PmAviEntity>>().ok(list, "操作成功");
    }
}
