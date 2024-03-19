package com.ca.mfd.prc.core.main.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.enums.ConditionDirection;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.core.prm.dto.SysServiceManagerDTO;
import com.ca.mfd.prc.core.main.entity.SysServiceHostManagerEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceManagerEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceOperLogEntity;
import com.ca.mfd.prc.core.main.service.ISysServiceHostManagerService;
import com.ca.mfd.prc.core.main.service.ISysServiceManagerService;
import com.ca.mfd.prc.core.main.service.ISysServiceOperLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * 服务管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("servicemanager/sysservicemanager")
@Tag(name = "服务管理")
public class SysServiceManagerController extends BaseController<SysServiceManagerEntity> {

    private final ISysServiceManagerService sysServiceManagerService;
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private ISysServiceOperLogService sysServiceOperLogService;
    @Autowired
    private ISysServiceHostManagerService sysServiceHostManagerService;

    @Autowired
    public SysServiceManagerController(ISysServiceManagerService sysServiceManagerService) {
        this.crudService = sysServiceManagerService;
        this.sysServiceManagerService = sysServiceManagerService;
    }

    @GetMapping(value = "/provider/getresartneededservernames")
    ResultVO<List<SysServiceManagerEntity>> getResartNeededServerNames(@RequestBody List<TreeNode> list) throws IllegalAccessException {
        return sysServiceManagerService.getResartNeededServerNames(list);
    }


    @Operation(summary = "操作单个服务")
    @PostMapping("oper")
    public ResultVO oper(@RequestBody SysServiceManagerDTO.OperModel model) {
        List<SysServiceManagerEntity> servieManagerList = sysServiceManagerService.getByServiceId(ConvertUtils.stringToLong(model.getServiceId()));
        for (SysServiceManagerEntity item : servieManagerList) {
            sysServiceManagerService.oper(item.getId(), model.getCode());
            String description = "";
            switch (model.getCode()) {
                case 1:
                    description = "安装了服务";
                    break;
                case 2:
                    description = "暂停了服务";
                    break;
                case 3:
                    description = "启动了服务";
                    break;
                case 4:
                    description = "卸载了服务";
                    break;
                case 5:
                    description = "重启了服务";
                    break;
                default:
                    break;
            }
            SysServiceOperLogEntity log = new SysServiceOperLogEntity();
            log.setPrcSysServiceManagerId(item.getId());
            log.setRemark(description);
            log.setOperDt(new Date());
            log.setOperUserId(identityHelper.getUserId());
            log.setOperUserName(identityHelper.getUserName() + "/" + identityHelper.getLoginName());
            sysServiceOperLogService.insert(log);
            sysServiceOperLogService.saveChange();
        }
        return new ResultVO().ok("", "已经加入执行队列，请稍后刷新页面查看结果");
    }


    @Operation(summary = "操作多个服务器")
    @PostMapping("opers")
    public ResultVO opers(@RequestBody SysServiceManagerDTO.OpersModel model) {
        for (SysServiceManagerDTO.OperModel oper : model.getDatas()) {
            List<SysServiceManagerEntity> servieManagerList = sysServiceManagerService.getByServiceId(ConvertUtils.stringToLong(oper.getServiceId()));
            for (SysServiceManagerEntity item : servieManagerList) {
                Long serviceId = item.getId();
                Integer code = oper.getCode();
                sysServiceManagerService.oper(serviceId, code);
                String description = "";
                switch (oper.getCode()) {
                    case 1:
                        description = "安装了服务";
                        break;
                    case 2:
                        description = "暂停了服务";
                        break;
                    case 3:
                        description = "启动了服务";
                        break;
                    case 4:
                        description = "卸载了服务";
                        break;
                    case 5:
                        description = "重启了服务";
                        break;
                    default:
                        break;
                }
                SysServiceOperLogEntity log = new SysServiceOperLogEntity();
                log.setPrcSysServiceManagerId(item.getId());
                log.setRemark(description);
                log.setOperDt(new Date());
                log.setOperUserId(identityHelper.getUserId());
                log.setOperUserName(identityHelper.getUserName() + "/" + identityHelper.getLoginName());
                sysServiceOperLogService.insert(log);
            }
        }
        sysServiceOperLogService.saveChange();
        return new ResultVO().ok("", "已经加入执行队列，请稍后刷新页面查看结果");
    }

    @Operation(summary = "服务操作日志列表")
    @PostMapping("operlog")
    public ResultVO operLog(@RequestBody PageDataDto model) {
//        List<ConditionDto> conditions = new ArrayList<>();
//        conditions.add(new ConditionDto("prcSysServiceManagerId", model.getId(), ConditionOper.Equal));

        List<SortDto> sorts = new ArrayList<>();
        sorts.add(new SortDto("OPER_DT", ConditionDirection.DESC));
        PageDataDto page = new PageDataDto();
        page.setConditions(model.getConditions());
        page.setSorts(sorts);
        page.setPageIndex(model.getPageIndex() == null ? 1 : model.getPageIndex());
        page.setPageSize(model.getPageSize() == null ? 20 : model.getPageSize());
        IPage<SysServiceOperLogEntity> pdata = sysServiceOperLogService.getDataByPage(page);
        PageData<SysServiceOperLogEntity> presult = new PageData<>();
        presult.setPageIndex(page.getPageIndex());
        presult.setPageSize(page.getPageSize());
        presult.setTotal((int) pdata.getTotal());
        presult.setDatas(pdata.getRecords());

        return new ResultVO<PageData<SysServiceOperLogEntity>>().ok(presult, "获取数据成功");
    }

    @Operation(summary = "服务手动重启")
    @PostMapping("RestartService")
    public ResultVO restartService(@RequestBody IdsModel guids) {
        for (String item : guids.getIds()) {
            sysServiceManagerService.restartService(ConvertUtils.stringToLong(item));
        }
        return new ResultVO<>().ok("", "后台重启中.....");
    }

    @Operation(summary = "批量更新服务版本")
    @PostMapping("AppUpdateVersion")
    public ResultVO appUpdateVersion(@RequestBody List<SysServiceManagerDTO.VsersionModel> model) {
        for (SysServiceManagerDTO.VsersionModel item : model) {
            UpdateWrapper<SysServiceManagerEntity> upManage = new UpdateWrapper<>();
            upManage.lambda().set(SysServiceManagerEntity::getNewVersion, item.getVersionNumber())
                    .set(SysServiceManagerEntity::getIsUpdate, 1)
                    .eq(SysServiceManagerEntity::getServiceId, ConvertUtils.stringToLong(item.getServiceId()));
            sysServiceManagerService.update(upManage);

            UpdateWrapper<SysServiceHostManagerEntity> upHost = new UpdateWrapper<>();
            upHost.lambda().set(SysServiceHostManagerEntity::getNewVersion, item.getVersionNumber())
                    .eq(SysServiceHostManagerEntity::getId, ConvertUtils.stringToLong(item.getServiceId()));
            sysServiceHostManagerService.update(upHost);
        }
        sysServiceHostManagerService.saveChange();
        return new ResultVO<>().ok("", "指定版本待更新");
    }


    @Operation(summary = "上传文件成功后立即调用此接口 ， VersionNumber  例如2.0.0.zip  VersionNumber=2.0.0")
    @PostMapping("VersionUpLoad")
    public ResultVO versionUpLoad(@RequestBody SysServiceManagerDTO.VsersionModel model) {

        UpdateWrapper<SysServiceManagerEntity> upManage = new UpdateWrapper<>();
        upManage.lambda().set(SysServiceManagerEntity::getNewVersion, model.getVersionNumber())
                .eq(SysServiceManagerEntity::getId, ConvertUtils.stringToLong(model.getServiceId()));
        sysServiceManagerService.update(upManage);
        sysServiceManagerService.saveChange();
        return new ResultVO<>().ok("", "新版本已上传成功，待更新");
    }

    @Operation(summary = "更新版本/更新版本必须是在服务停止的状态下才允许提交")
    @PostMapping("VersionUpdate")
    public ResultVO versionUpdate(@RequestBody IdsModel guids) {

        UpdateWrapper<SysServiceManagerEntity> upManage = new UpdateWrapper<>();
        upManage.lambda().set(SysServiceManagerEntity::getIsUpdate, 1)
                .in(SysServiceManagerEntity::getId, ConvertUtils.stringToLongs(Arrays.asList(guids.getIds())));
        sysServiceManagerService.update(upManage);
        sysServiceManagerService.saveChange();
        return new ResultVO<>().ok("", "触发更新成功");
    }

    @Operation(summary = "回归版本更新， VersionNumber  例如2.0.0.zip  VersionNumber=2.0.0")
    @PostMapping("AppointVersion")
    public ResultVO appointVersion(@RequestBody SysServiceManagerDTO.VsersionModel model) {

        UpdateWrapper<SysServiceManagerEntity> upManage = new UpdateWrapper<>();
        upManage.lambda().set(SysServiceManagerEntity::getNewVersion, model.getVersionNumber())
                .set(SysServiceManagerEntity::getIsUpdate, 1)
                .eq(SysServiceManagerEntity::getServiceId, ConvertUtils.stringToLong(model.getServiceId()));
        sysServiceManagerService.update(upManage);

        UpdateWrapper<SysServiceHostManagerEntity> upHost = new UpdateWrapper<>();
        upHost.lambda().set(SysServiceHostManagerEntity::getNewVersion, model.getVersionNumber())
                .eq(SysServiceHostManagerEntity::getId, ConvertUtils.stringToLong(model.getServiceId()));
        sysServiceHostManagerService.update(upHost);

        sysServiceManagerService.saveChange();
        return new ResultVO<>().ok("", "指定版本待更新");
    }

}