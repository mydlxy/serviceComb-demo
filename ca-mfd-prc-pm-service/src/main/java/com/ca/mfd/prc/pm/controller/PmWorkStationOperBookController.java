package com.ca.mfd.prc.pm.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.entity.PmWorkstationOperBookEntity;
import com.ca.mfd.prc.pm.service.IPmWorkStationOperBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author inkelink
 * @Description: 岗位操作指导书
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@RestController
@RequestMapping("pmworkstationoperbook")
@Tag(name = "岗位操作指导书")
public class PmWorkStationOperBookController extends PmBaseController<PmWorkstationOperBookEntity> {

    private final IPmWorkStationOperBookService pmWorkplaceOperBookService;

    @Autowired
    public PmWorkStationOperBookController(IPmWorkStationOperBookService pmWorkplaceOperBookService) {
        this.crudService = pmWorkplaceOperBookService;
        this.pmWorkplaceOperBookService = pmWorkplaceOperBookService;
    }

    /**
     * 获取类型为T的未逻辑删除的列表数据
     *
     * @param conditionInfos 条件表达式
     * @return List<T>
     */
    @PostMapping(value = "/getdata")
    @Operation(summary = "获取类型为T的未逻辑删除的列表数据")
    public ResultVO getData(@RequestBody List<ConditionDto> conditionInfos) {
        List<PmWorkstationOperBookEntity> lst = pmWorkplaceOperBookService.getData(conditionInfos);
        return new ResultVO<>().ok(lst);
    }

    /**
     * 获取质量操作指导书
     *
     * <returns></returns>
     */
    @GetMapping("/getqualitybook")
    @Operation(summary = "获取质量操作指导书")
    public ResultVO<List<ComboInfoDTO>> getQualityBook() {
        return pmWorkplaceOperBookService.getQualityBook();
    }

    /**
     * 操作指导书列表
     * </summary>
     * <param name="category">分类 1生产 2质量</param>
     * <param name="workplaceName">岗位</param>
     * <param name="fileName">文件名</param>
     * <returns></returns>
     */
    @GetMapping("/getbooklist")
    @Operation(summary = "操作指导书列表")
    public ResultVO<List<PmWorkstationOperBookEntity>> getBookList(@NotNull(message = "分类不能为空") Integer category, String workplaceName, String fileName) {
        return pmWorkplaceOperBookService.getBookList(category, workplaceName, fileName);
    }
}