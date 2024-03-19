package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.dto.GenTokenModel;
import com.ca.mfd.prc.core.prm.service.Signature;
import com.ca.mfd.prc.core.main.entity.SysInterfaceLogEntity;
import com.ca.mfd.prc.core.main.service.ISysInterfaceLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;


/**
 * 第三方接口交互记录
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysinterfacelog")
@Tag(name = "第三方接口交互记录")
public class SysInterfaceLogController extends BaseController<SysInterfaceLogEntity> {

    private final ISysInterfaceLogService sysInterfaceLogService;

    @Autowired
    public SysInterfaceLogController(ISysInterfaceLogService sysInterfaceLogService) {
        this.crudService = sysInterfaceLogService;
        this.sysInterfaceLogService = sysInterfaceLogService;
    }

    @Operation(summary = "gentoken")
    @PostMapping("获取gentoken")
    public ResultVO genToken(GenTokenModel model) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        Map<String, String> map = Signature.buildHttpHeaders(model.getUri(), model.getAppId(), model.getSecret());
        return new ResultVO().ok(map);
    }

}