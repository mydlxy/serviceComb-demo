package com.ca.mfd.prc.eps.controller;

import cn.hutool.core.util.ObjectUtil;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.FetrueTestWoDto;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWoEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.provider.PmVersionProvider;
import com.ca.mfd.prc.eps.remote.app.pps.dto.FilterFetureExpressionPara;
import com.ca.mfd.prc.eps.remote.app.pps.provider.AnalysisFeatureProvider;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * 车辆操作信息
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewo")
@Tag(name = "车辆操作信息")
public class EpsVehicleWoController extends BaseController<EpsVehicleWoEntity> {

    private final IEpsVehicleWoService epsVehicleWoService;
    @Autowired
    private PmVersionProvider pmVersionProvider;
    @Autowired
    private AnalysisFeatureProvider analysisFeatureProvider;

    @Autowired
    public EpsVehicleWoController(IEpsVehicleWoService epsVehicleWoService) {
        this.crudService = epsVehicleWoService;
        this.epsVehicleWoService = epsVehicleWoService;
    }


    @GetMapping("createentrywo")
    @Operation(summary = "生成工单工艺")
    public ResultVO createEntryWo(String barCode, String shopCode) {
        epsVehicleWoService.createEntryWo(barCode, shopCode);
        epsVehicleWoService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }


    @PostMapping("getpagevehicledatas")
    @Operation(summary = "获取车辆工艺分页")
    public ResultVO getPageVehicleDatas(@RequestBody PageDataDto model) {
        ResultVO<PageData<EpsVehicleWoEntity>> result = new ResultVO<>();
        result.ok(null, "获取数据成功");
        PageData<EpsVehicleWoEntity> pageVehicleDatas = epsVehicleWoService.getPageVehicleDatas(model.getConditions(), model.getSorts(), model.getPageIndex() == null ? 1 : model.getPageIndex(), model.getPageSize() == null ? 20 : model.getPageSize());
        if (ObjectUtil.isEmpty(pageVehicleDatas.getDatas())) {
            pageVehicleDatas.setDatas(new ArrayList<>());
        }
        result.setData(pageVehicleDatas);
        return result;
    }

    @GetMapping("getpmwos")
    @Operation(summary = "获取建模的工艺列表")
    public ResultVO getPmWos(String sn) {

        ResultVO<List<FetrueTestWoDto>> result = new ResultVO<>();
        PmAllDTO pmAll = pmVersionProvider.getObjectedPm();

        List<PmWoEntity> wos = pmAll.getWos();
        List<String> fetureExpressions = wos.stream().map(PmWoEntity::getFeatureCode).collect(Collectors.toList());
        FilterFetureExpressionPara para = new FilterFetureExpressionPara();
        para.setBarcode(sn);
        para.setFetureExpressions(fetureExpressions);
        List<String> resultExpressions = analysisFeatureProvider.filterFeatureExpression(para);
        List<FetrueTestWoDto> datas = new ArrayList<>();
        for (PmWoEntity woInfo : wos) {
            PmWorkShopEntity shopInfo = pmAll.getShops().stream().filter(c -> Objects.equals(c.getId(), woInfo.getPrcPmWorkshopId())).findFirst().orElse(null);
            PmLineEntity lineInfo = pmAll.getLines().stream().filter(c -> Objects.equals(c.getId(), woInfo.getPrcPmLineId())).findFirst().orElse(null);
            PmWorkStationEntity workStationInfo = pmAll.getStations().stream().filter(c -> Objects.equals(c.getId(), woInfo.getPrcPmWorkstationId())).findFirst().orElse(null);
            FetrueTestWoDto woDto = new FetrueTestWoDto();
            woDto.setShopCode(shopInfo.getWorkshopCode());
            woDto.setLineCode(lineInfo.getLineCode());
            woDto.setWorkstationCode(workStationInfo.getWorkstationCode());
            woDto.setPmWoCode(woInfo.getWoCode());
            woDto.setPmWoDescription(woInfo.getWoDescription());
            woDto.setOperType(woInfo.getOperType());
            datas.add(woDto);
        }

        return result.ok(datas, "获取数据成功");
    }

    /**
     * 根据唯一码sn获取工艺列表
     *
     * @param sn 唯一码
     * @return
     */
    @GetMapping(value = "/provider/getepsvehiclewodatas")
    @Operation(summary ="根据唯一码sn获取工艺列表")
    public ResultVO<List<EpsVehicleWoEntity>> getEpsVehicleWoDatas(String sn) {
        List<EpsVehicleWoEntity> datas = epsVehicleWoService.getBySn(sn);
        return new ResultVO<List<EpsVehicleWoEntity>>().ok(datas, "获取数据成功");
    }
}