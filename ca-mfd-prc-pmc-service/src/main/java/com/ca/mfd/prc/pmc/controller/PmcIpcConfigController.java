package com.ca.mfd.prc.pmc.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.dto.IpcCameraGroupDTO;
import com.ca.mfd.prc.pmc.dto.ListCameraByGroupDTO;
import com.ca.mfd.prc.pmc.entity.PmcIpcConfigEntity;
import com.ca.mfd.prc.pmc.service.IPmcIpcConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 工控机配置;Controller
 * @date 2023年10月26日
 * @变更说明 BY inkelink At 2023年10月26日
 */
@RestController
@RequestMapping("equality.mes.pmc/{version}/pmcipcconfig")
@Tag(name = "工控机配置;服务", description = "工控机配置;")
public class PmcIpcConfigController extends BaseController<PmcIpcConfigEntity> {

    IPmcIpcConfigService service;

    @Autowired
    public PmcIpcConfigController(IPmcIpcConfigService pmcIpcConfigService) {
        this.crudService = pmcIpcConfigService;
        this.service = pmcIpcConfigService;
    }


    @Operation(summary = "按照工控机分组查看摄像机列表")
    @GetMapping("/listCameraByGroup/{workstationCode}")
    public ResultVO<IpcCameraGroupDTO> listCameraByGroup(@PathVariable("workstationCode")String workstationCode){
        ResultVO<IpcCameraGroupDTO> result = new ResultVO<>();
        ListCameraByGroupDTO dto = new ListCameraByGroupDTO();
        dto.setWorkstationCode(workstationCode);
        return result.ok(service.listCameraByGroup(dto));
    }
}