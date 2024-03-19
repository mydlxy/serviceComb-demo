package com.ca.mfd.prc.avi.controller;

import com.ca.mfd.prc.avi.dto.OrderSequenceDTO;
import com.ca.mfd.prc.avi.dto.ResetQueueParaDTO;
import com.ca.mfd.prc.avi.entity.AviQueueReleaseEntity;
import com.ca.mfd.prc.avi.service.IAviQueueReleaseService;
import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 队列发布数据表
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
@RestController
@RequestMapping("aviqueuerelease")
@Tag(name = "队列发布数据表")
public class AviQueueReleaseController extends BaseController<AviQueueReleaseEntity> {

    private final IAviQueueReleaseService aviQueueReleaseService;

    @Autowired
    public AviQueueReleaseController(IAviQueueReleaseService aviQueueReleaseService) {
        this.crudService = aviQueueReleaseService;
        this.aviQueueReleaseService = aviQueueReleaseService;
    }

    @Operation(summary = "整车数据")
    @PostMapping("getmotorpagedata")
    public ResultVO getMotorPageData(@RequestBody PageDataDto model) {
        ResultVO<PageData<OrderSequenceDTO>> result = new ResultVO<>();
        result.setMessage("获取数据成功");
        PageData<OrderSequenceDTO> data = aviQueueReleaseService.getPageVehicleDatas(model.getConditions(), model.getSorts(),
                model.getPageIndex() == null ? 1 : model.getPageIndex(), model.getPageSize() == null ? 20 : model.getPageSize());
        return result.ok(data);
    }

    @Operation(summary = "重置队列")
    @PostMapping("resetqueue")
    public ResultVO resetQueue(@RequestBody ResetQueueParaDTO para) {
        ResultVO result = new ResultVO<>();
        aviQueueReleaseService.resetQueue(para);
        aviQueueReleaseService.saveChange();
        return result.ok("","操作成功");
    }
}