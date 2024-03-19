package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysNetworkMonitorEntity;
import com.ca.mfd.prc.core.main.service.ISysNetworkMonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;


/**
 * 系统网络设备监控
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysnetworkmonitor")
@Tag(name = "系统网络设备监控")
public class SysNetworkMonitorController extends BaseController<SysNetworkMonitorEntity> {

    private final ISysNetworkMonitorService sysNetworkMonitorService;

    @Autowired
    public SysNetworkMonitorController(ISysNetworkMonitorService sysNetworkMonitorService) {
        this.crudService = sysNetworkMonitorService;
        this.sysNetworkMonitorService = sysNetworkMonitorService;
    }

    @Operation(summary = "获取Headers 对象")
    @GetMapping("getheaders")
    public ResultVO getHeaders(HttpServletRequest request) {
        Map<String, Object> stringObjectHashMap = new HashMap<>(2);
        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String headName = headers.nextElement();
            String headValue = request.getHeader(headName);
            stringObjectHashMap.put(headName, headValue);
        }
        return new ResultVO().ok(stringObjectHashMap, "获取数据成功");
    }

}