package com.ca.mfd.prc.core.communication.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.dto.ApiLogStatusVo;
import com.ca.mfd.prc.core.communication.entity.MidApiLogEntity;
import com.ca.mfd.prc.core.communication.service.IMidApiLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @Description: 接口记录表Controller
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@RestController
@RequestMapping("communication/midapilog")
@Tag(name = "接口记录表服务", description = "接口记录表")
public class MidApiLogController  extends BaseController<MidApiLogEntity> {

    private final IMidApiLogService midApiLogService;

    @Autowired
    public MidApiLogController(IMidApiLogService midApiLogService) {
        this.crudService = midApiLogService;
        this.midApiLogService = midApiLogService;
    }

    @PostMapping(value = "changestatus")
    @Operation(summary = "修改状态")
    public ResultVO<String> changeStatus(@RequestBody ApiLogStatusVo model) {
        Long id = ConvertUtils.stringToLong(model.getId());
        MidApiLogEntity ent = midApiLogService.get(id);
        if (ent == null) {
            return new ResultVO<String>().error(-1, "请选择数据");
        }
        UpdateWrapper<MidApiLogEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(MidApiLogEntity::getStatus, model.getStatus())
                .eq(MidApiLogEntity::getId, id);
        midApiLogService.update(upset);
        midApiLogService.saveChange();
        return new ResultVO<String>().ok("", "操作成功");
    }

    @PostMapping(value = "getpagedatanew", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "获取分页数据")
    public ResultVO<PageData<MidApiLogEntity>> getPageDataNew(@RequestBody PageDataDto model) {

        List<String> columns = new ArrayList<>(24);
        columns.add("PRC_MID_API_LOG_ID as ID");
        columns.add("REQUEST_START_TIME");
        columns.add("REQUEST_STOP_TIME");
        columns.add("DATA_LINE_NO");
        columns.add("API_TYPE");
        columns.add("STATUS");
        columns.add("REMARK");
        columns.add("IS_DELETE");
        columns.add("CREATED_BY");
        columns.add("CREATION_DATE");
        columns.add("CREATED_USER");
        columns.add("LAST_UPDATE_DATE");
        columns.add("LAST_UPDATED_BY");
        columns.add("LAST_UPDATED_USER");
        columns.add("ATTRIBUTE1");
        columns.add("ATTRIBUTE2");
        columns.add("ATTRIBUTE3");
        columns.add("ATTRIBUTE4");
        columns.add("ATTRIBUTE5");
        columns.add("ATTRIBUTE6");
        columns.add("ATTRIBUTE7");
        columns.add("ATTRIBUTE8");
        columns.add("ATTRIBUTE9");
        columns.add("ATTRIBUTE10");

        PageData<MidApiLogEntity> page = midApiLogService.page(model,columns);
        return new ResultVO<PageData<MidApiLogEntity>>().ok(page, "获取数据成功");
    }

}