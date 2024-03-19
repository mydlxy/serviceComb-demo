package com.ca.mfd.prc.eps.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.basedto.ExportByDcModel;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.EpsVehicleWoDataTrcInfo;
import com.ca.mfd.prc.eps.entity.EpsVehicleEqumentDataEntity;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoDataEntity;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 工艺数据
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-09
 */
@RestController
@RequestMapping("epsvehiclewodata")
@Tag(name = "工艺数据")
public class EpsVehicleWoDataController extends BaseController<EpsVehicleWoDataEntity> {

    private final IEpsVehicleWoDataService epsVehicleWoDataService;

    @Autowired
    public EpsVehicleWoDataController(IEpsVehicleWoDataService epsVehicleWoDataService) {
        this.crudService = epsVehicleWoDataService;
        this.epsVehicleWoDataService = epsVehicleWoDataService;
    }


    @Operation(summary = "获取车辆工艺分页")
    @PostMapping("getpagevehicledatas")
    public ResultVO getPageVehicleDatas(@RequestBody PageDataDto model) {
        ResultVO<PageData<EpsVehicleWoDataEntity>> result = new ResultVO<>();
        result.ok(null, "获取数据成功");
        result.setData(epsVehicleWoDataService.getPageVehicleDatas(model.getConditions(), model.getSorts(), model.getPageIndex() == null ? 1 : model.getPageIndex(), model.getPageSize() == null ? 20 : model.getPageSize()));
        return result;
    }


    @Operation(summary = "获取自定义采集数据")
    @GetMapping("getcurrentdata")
    public ResultVO getcurrentdata(String vehicleWoDataId) {
        ResultVO<List<EpsVehicleEqumentDataEntity>> result = new ResultVO<>();
        result.ok(null, "获取数据成功");
        result.setData(epsVehicleWoDataService.getCurrentData(ConvertUtils.stringToLong(vehicleWoDataId)));
        return result;
    }

    @Operation(summary = "获取车辆追溯分页")
    @PostMapping("getpagetrcvehicledatas")
    public ResultVO getPageTrcVehicleDatas(@RequestBody PageDataDto model) {
        ResultVO<PageData<EpsVehicleWoDataTrcInfo>> result = new ResultVO<>();
        result.ok(null, "获取数据成功");
        result.setData(epsVehicleWoDataService.getPageTrcVehicleDatas(model.getConditions(), model.getSorts(), model.getPageIndex() == null ? 1 : model.getPageIndex(), model.getPageSize() == null ? 20 : model.getPageSize()));
        return result;
    }

    @PostMapping(value = "exporttrcvehicledatas", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "trc-导出")
    public void exportTrcVehicleDatas(@RequestBody ExportByDcModel model, HttpServletResponse response) throws Exception {
        epsVehicleWoDataService.exportTrcVehicleDatas(model.getField(), model.getConditions(), model.getSorts(), model.getFileName(), response);
    }

}