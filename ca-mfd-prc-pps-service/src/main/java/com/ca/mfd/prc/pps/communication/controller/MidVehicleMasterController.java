package com.ca.mfd.prc.pps.communication.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.communication.dto.VehicleModelDto;
import com.ca.mfd.prc.pps.communication.entity.MidVehicleMasterEntity;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleCodeService;
import com.ca.mfd.prc.pps.communication.service.IMidVehicleMasterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author inkelink
 * @Description: 车型主数据中间表Controller
 * @date 2023年11月02日
 * @变更说明 BY inkelink At 2023年11月02日
 */
@RestController
@RequestMapping("communication/midvehiclemode")
@Tag(name = "车型主数据中间表服务", description = "车型主数据中间表")
public class MidVehicleMasterController extends BaseController<MidVehicleMasterEntity> {
    private IMidVehicleMasterService vehicleMasterService;

    @Autowired
    public MidVehicleMasterController(IMidVehicleMasterService vehicleMasterService) {
        this.crudService = vehicleMasterService;
        this.vehicleMasterService = vehicleMasterService;
    }


    @GetMapping(value = "receive")
    @Operation(summary = "获取车型信息")
    public ResultVO receive() {
        vehicleMasterService.receive();
        return new ResultVO().ok(null, "处理完成");
    }

    @GetMapping(value = "getvehiclemodeldata")
    @Operation(summary = "获取车型信息")
    public ResultVO<List<VehicleModelDto>> getVehicleModelData(String materialNo) {
        return new ResultVO<List<VehicleModelDto>>().ok(vehicleMasterService.getVehicleModelData(materialNo));
    }


    @GetMapping(value = "excute")
    @Operation(summary = "同步车型信息")
    public ResultVO excute() {
        vehicleMasterService.excute();
        return new ResultVO().ok(null, "处理完成");
    }

    @GetMapping(value = "/provider/getvehiclemasterbyparam")
    @Operation(summary = "查询车型主数据")
    public ResultVO<MidVehicleMasterEntity> getVehicleMasterByParam(String vehicleMaterialNumber, String bomRoom) {
        return new ResultVO<MidVehicleMasterEntity>().ok(vehicleMasterService.getVehicleMasterByParam(vehicleMaterialNumber, bomRoom));
    }

    @PostMapping(value = "copy", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "复制")
    public ResultVO<?> copy(@RequestBody @Valid MidVehicleMasterEntity dto) {
        Long id = vehicleMasterService.currentModelGetKey(dto);
        if (id == null || id <= 0) {
            //vehicleMasterService.save(dto);
        } else {
            dto.setId(IdGenerator.getId());
            dto.setCreationDate(new Date());
            vehicleMasterService.insert(dto);
        }
        vehicleMasterService.saveChange();
        return new ResultVO<>().ok(dto, "保存成功");
    }
}