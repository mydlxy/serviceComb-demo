package com.ca.mfd.prc.rc.rcbs.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaAttachShowVO;
import com.ca.mfd.prc.rc.rcbs.dto.RcRouteAreaAttachVO;
import com.ca.mfd.prc.rc.rcbs.entity.RcBsRouteAreaAttachEntity;
import com.ca.mfd.prc.rc.rcbs.service.IRcBsRouteAreaAttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 路由点关联附加模块Controller
 * @date 2023年08月08日
 * @变更说明 BY inkelink At 2023年08月08日
 */
@RestController
@RequestMapping("rcbsrouteareaattach")
@Tag(name = "路由点关联附加模块服务", description = "路由点关联附加模块")
public class RcBsRouteAreaAttachController extends BaseController<RcBsRouteAreaAttachEntity> {

    private final IRcBsRouteAreaAttachService rcRouteAreaAttachService;

    @Autowired
    public RcBsRouteAreaAttachController(IRcBsRouteAreaAttachService rcRouteAreaAttachService) {
        this.crudService = rcRouteAreaAttachService;
        this.rcRouteAreaAttachService = rcRouteAreaAttachService;
    }

    @GetMapping("showlist")
    @Operation(summary = "查询所有附加")
    public ResultVO<List<RcRouteAreaAttachVO>> showList() {
        List<RcRouteAreaAttachShowVO> list = rcRouteAreaAttachService.getAttachShowList();
        List<RcRouteAreaAttachVO> data = list.stream().map(c -> {
            RcRouteAreaAttachVO et = new RcRouteAreaAttachVO();
            et.setId(c.getAttachId());
            et.setCode(c.getAttachCode());
            et.setName(c.getAttachName());
            return et;
        }).collect(Collectors.toList());
        return new ResultVO<List<RcRouteAreaAttachVO>>().ok(data, "成功！");
    }
}