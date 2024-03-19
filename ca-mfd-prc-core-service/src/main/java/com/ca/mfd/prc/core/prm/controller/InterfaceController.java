package com.ca.mfd.prc.core.prm.controller;

import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.entity.OperSession;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.prm.service.IPrmInterfacePermissionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 */
@RestController
@RequestMapping("prm/interface")
@Tag(name = "获取接口")
public class InterfaceController extends BaseApiController {
    @Autowired
    IPrmInterfacePermissionService prmInterfacePermissionService;

    @GetMapping("all")
    @Operation(summary = "获取所有接口")
    public ResultVO<OperSession> all() {
        List<ApiSession> apilist = prmInterfacePermissionService.getApiSession();
        OperSession data = new OperSession();
        data.setApiList(apilist);
        data.setMd5(EncryptionUtils.md5(JsonUtils.toJsonString(apilist)));
        return new ResultVO<OperSession>().ok(data);
    }
}
