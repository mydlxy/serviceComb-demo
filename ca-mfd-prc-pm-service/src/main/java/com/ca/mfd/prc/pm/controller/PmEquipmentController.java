package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ClassUtil;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmEquipmentEntity;
import com.ca.mfd.prc.pm.service.IPmEquipmentService;
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
import java.util.Objects;


/**
 * @author inkelink
 * @Description: 设备Controller
 * @date 2023年10月23日
 * @变更说明 BY inkelink At 2023年10月23日
 */
@RestController
@RequestMapping("pmequipment")
@Tag(name = "设备服务", description = "设备")
public class PmEquipmentController extends PmBaseController<PmEquipmentEntity> {

    private IPmEquipmentService pmEquipmentService;

    @Autowired
    public PmEquipmentController(IPmEquipmentService pmEquipmentService) {
        this.crudService = pmEquipmentService;
        this.pmEquipmentService = pmEquipmentService;
    }

    @Override
    @PostMapping(value = "edit", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "更新")
    public ResultVO<?> edit(@RequestBody @Valid PmEquipmentEntity dto) {
        PmEquipmentEntity oldPmEquipmentEntity = pmEquipmentService.getPmEquipmentEntityByCode(dto.getEquipmentCode());
        if(oldPmEquipmentEntity != null && !Objects.equals(dto.getId(),oldPmEquipmentEntity.getId())){
            throw new InkelinkException("设备编码已经存在");
        }
        return super.edit(dto);
    }

    @Operation(summary = "根据设备编码查询设备信息")
    @GetMapping("/provider/getPmEquipmentEntityByCode")
    public ResultVO<PmEquipmentEntity> getPmEquipmentEntityByCode(String code) {
        PmEquipmentEntity equipmentEntity = pmEquipmentService.getPmEquipmentEntityByCode(code);
        return new ResultVO().ok(equipmentEntity);
    }
}