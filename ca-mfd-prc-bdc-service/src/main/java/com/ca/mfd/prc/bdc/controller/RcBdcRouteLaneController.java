package com.ca.mfd.prc.bdc.controller;

import com.ca.mfd.prc.bdc.dto.RouteLaneDTO;
import com.ca.mfd.prc.bdc.dto.RouteLaneItems;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteAreaEntity;
import com.ca.mfd.prc.bdc.entity.RcBdcRouteLaneEntity;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteAreaService;
import com.ca.mfd.prc.bdc.service.IRcBdcRouteLaneService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author inkelink
 * @Description: 路由车道Controller
 * @date 2023年08月31日
 * @变更说明 BY inkelink At 2023年08月31日
 */
@RestController
@RequestMapping("rcbdcroutelane")
@Tag(name = "路由车道服务", description = "路由车道")
public class RcBdcRouteLaneController extends BaseController<RcBdcRouteLaneEntity> {

    @Autowired
    private IRcBdcRouteAreaService rcBdcRouteAreaService;

    private final IRcBdcRouteLaneService rcBdcRouteLaneService;

    @Autowired
    public RcBdcRouteLaneController(IRcBdcRouteLaneService rcBdcRouteLaneService) {
        this.crudService = rcBdcRouteLaneService;
        this.rcBdcRouteLaneService = rcBdcRouteLaneService;
    }

    @PostMapping("generate")
    @Operation(summary = "立体库车道自动添加")
    public ResultVO<String> generate(@RequestBody RouteLaneDTO.GenerateModel model) {
        RcBdcRouteAreaEntity area = rcBdcRouteAreaService.get(model.getAreaId());
        if (area == null) {
            throw new InkelinkException("区域" + model.getAreaId() + "不存在");
        }
        List<RouteLaneItems> list = rcBdcRouteLaneService.getRouteLaneItems(area.getId());
        if (list == null) {
            throw new InkelinkException("数据异常");
        }
        for (int x = 1; x <= area.getPositionX(); x++) {
            for (int y = 1; y <= area.getPositionY(); y++) {
                for (int z = 1; z <= area.getPositionZ(); z++) {
                    String xstr = StringUtils.leftPad(String.valueOf(x), 2, '0');
                    String ystr = StringUtils.leftPad(String.valueOf(y), 2, '0');
                    String zstr = StringUtils.leftPad(String.valueOf(z), 2, '0');
                    String storage = xstr + ystr + zstr;
                    String bdcArea = "A";
                    if (y > 5) {
                        bdcArea = "B";
                    }
                    String laneName = storage;
                    int laneCode = Integer.parseInt("1" + storage);
                    RouteLaneItems item = list.stream().filter(s -> StringUtils.equals(s.getBdcStorage(), storage)).findFirst().orElse(null);
                    if (item == null) {
                        RcBdcRouteLaneEntity info = new RcBdcRouteLaneEntity();
                        info.setPrcBdcRouteAreaId(area.getId());
                        info.setLaneCode(laneCode);
                        info.setBdcArea(bdcArea);
                        info.setBdcStorage(storage);
                        info.setLaneName(laneName);
                        info.setAllowIn(true);
                        info.setAllowOut(true);
                        info.setButtonIn(true);
                        info.setButtonOut(true);
                        info.setMaxValue(1);
                        info.setMaxLevel(1);
                        info.setDisplayNo(laneCode);
                        info.setPositionX(x);
                        info.setPositionY(y);
                        info.setPositionZ(z);
                        rcBdcRouteLaneService.save(info);
                    }
                }
            }
        }
        rcBdcRouteLaneService.saveChange();
        return new ResultVO<String>().ok("成功", "初始化成功");
    }
}