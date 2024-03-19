package com.ca.mfd.prc.eps.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseApiController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.entity.EpsVehicleWoEntity;
import com.ca.mfd.prc.eps.remote.app.core.entity.SysConfigurationEntity;
import com.ca.mfd.prc.eps.remote.app.core.provider.SysConfigurationProvider;
import com.ca.mfd.prc.eps.service.IEpsVehicleWoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * SchedulingController
 *
 * @author inkelink
 * @date 2023/09/05
 */
@RestController
@RequestMapping("scheduling")
@Tag(name = "EPS模块调度接口")
public class SchedulingController extends BaseApiController {

    @Autowired
    private IEpsVehicleWoService epsVehicleWoService;
    @Autowired
    private SysConfigurationProvider sysConfigurationProvider;


    @Operation(summary = "定时byPass工艺")
    @PostMapping("bypasswoinfo")
    public ResultVO bypasswoinfo() {
        ResultVO result = new ResultVO<>();
        result.setMessage("触发成功");

        //查询配置
        List<SysConfigurationEntity> configData = sysConfigurationProvider.getSysConfigurations("byPassConfig");
        if (configData != null && configData.size() > 0) {
            configData.forEach(s -> {
                UpdateWrapper<EpsVehicleWoEntity> upset = new UpdateWrapper<>();
                upset.lambda().set(EpsVehicleWoEntity::getResult, 3)
                        .eq(EpsVehicleWoEntity::getWoCode, s.getText())
                        .eq(EpsVehicleWoEntity::getResult, 0);
                epsVehicleWoService.update(upset);
                epsVehicleWoService.saveChange();
            });
        }
        return result.ok("");
    }

}