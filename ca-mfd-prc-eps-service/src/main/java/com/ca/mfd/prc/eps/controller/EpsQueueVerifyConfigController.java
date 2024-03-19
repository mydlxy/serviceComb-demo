package com.ca.mfd.prc.eps.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.dto.UpdateVerifyTypePara;
import com.ca.mfd.prc.eps.entity.EpsQueueVerifyConfigEntity;
import com.ca.mfd.prc.eps.service.IEpsQueueVerifyConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author inkelink
 * @Description: 虚拟队列校验配置Controller
 * @date 2023年08月29日
 * @变更说明 BY inkelink At 2023年08月29日
 */
@RestController
@RequestMapping("epsqueueverifyconfig")
@Tag(name = "虚拟队列校验配置服务", description = "虚拟队列校验配置")
public class EpsQueueVerifyConfigController extends BaseController<EpsQueueVerifyConfigEntity> {

    private final IEpsQueueVerifyConfigService epsQueueVerifyConfigService;

    @Autowired
    public EpsQueueVerifyConfigController(IEpsQueueVerifyConfigService epsQueueVerifyConfigService) {
        this.crudService = epsQueueVerifyConfigService;
        this.epsQueueVerifyConfigService = epsQueueVerifyConfigService;
    }


    @Operation(summary = "修改校验方式")
    @PostMapping("updateverifytype")
    public ResultVO<String> updateVerifyType(@RequestBody UpdateVerifyTypePara para) {
        if (para.getVerifyType() != 1 && para.getVerifyType() != 2) {
            throw new InkelinkException("无效的校验方式");
        }
        UpdateWrapper<EpsQueueVerifyConfigEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(EpsQueueVerifyConfigEntity::getVerifyType, para.getVerifyType())
                .eq(EpsQueueVerifyConfigEntity::getId, ConvertUtils.stringToLong(para.getId()));
        epsQueueVerifyConfigService.update(upset);
        epsQueueVerifyConfigService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

}