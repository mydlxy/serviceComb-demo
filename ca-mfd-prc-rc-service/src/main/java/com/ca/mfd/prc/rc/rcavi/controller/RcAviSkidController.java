package com.ca.mfd.prc.rc.rcavi.controller;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.LongIdsListModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviSkidEntity;
import com.ca.mfd.prc.rc.rcavi.entity.RcAviSkidLogEntity;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviSkidLogService;
import com.ca.mfd.prc.rc.rcavi.service.IRcAviSkidService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author inkelink
 * @Description: 滑橇Controller
 * @date 2023年09月01日
 * @变更说明 BY inkelink At 2023年09月01日
 */
@RestController
@RequestMapping("rcaviskid")
@Tag(name = "滑橇服务", description = "滑橇")
public class RcAviSkidController extends BaseController<RcAviSkidEntity> {

    @Autowired
    private IRcAviSkidLogService rcAviSkidLogService;

    @Autowired
    private IdentityHelper identityHelper;

    private final IRcAviSkidService rcAviSkidService;

    @Autowired
    public RcAviSkidController(IRcAviSkidService rcAviSkidService) {
        this.crudService = rcAviSkidService;
        this.rcAviSkidService = rcAviSkidService;
    }

    @PostMapping("clean")
    @Operation(summary = "触发清洗")
    public ResultVO<String> clean(@RequestBody LongIdsListModel model) {
        if (model.getIds() == null || model.getIds().isEmpty()) {
            throw new InkelinkException("没有需要操作的数据");
        }
        UpdateWrapper<RcAviSkidEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RcAviSkidEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RcAviSkidEntity::getIsClean, true);
        lambdaUpdateWrapper.in(RcAviSkidEntity::getId, model.getIds());
        crudService.update(updateWrapper);

        for (Long item : model.getIds()) {
            RcAviSkidLogEntity info = new RcAviSkidLogEntity();
            info.setPrcRcAviSkidId(item);
            info.setOperateType(4);
            info.setOperater(identityHelper.getUserName() + "/" + identityHelper.getLoginName());
            info.setOperateDt(new Date());
            info.setContent(identityHelper.getUserName() + "在" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ",发起清洗要求");
            rcAviSkidLogService.save(info);
        }
        rcAviSkidService.saveChange();
        return new ResultVO<String>().ok("操作成功", "操作成功");
    }

    @PostMapping("cleancancel")
    @ApiOperation("取消清洗")
    @Operation(summary = "取消清洗")
    public ResultVO<String> cleanCancel(@RequestBody LongIdsListModel model) {
        if (model.getIds() == null || model.getIds().size() == 0) {
            throw new InkelinkException("没有需要操作的数据");
        }
        UpdateWrapper<RcAviSkidEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<RcAviSkidEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(RcAviSkidEntity::getIsClean, false);
        lambdaUpdateWrapper.in(RcAviSkidEntity::getId, model.getIds());
        crudService.update(updateWrapper);

        for (Long item : model.getIds()) {
            RcAviSkidLogEntity info = new RcAviSkidLogEntity();
            info.setPrcRcAviSkidId(item);
            info.setOperateType(4);
            info.setOperater(identityHelper.getUserName() + "/" + identityHelper.getLoginName());
            info.setOperateDt(new Date());
            info.setContent(identityHelper.getUserName() + "在" + DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN) + ",取消清洗");
            rcAviSkidLogService.save(info);
        }
        rcAviSkidService.saveChange();
        return new ResultVO<String>().ok("操作成功");
    }

}