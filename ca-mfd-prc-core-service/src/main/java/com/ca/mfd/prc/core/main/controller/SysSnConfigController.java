package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysSnConfigEntity;
import com.ca.mfd.prc.core.main.service.ISysSnConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 唯一码配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syssnconfig")
@Tag(name = "唯一码配置")
public class SysSnConfigController extends BaseController<SysSnConfigEntity> {

    private final ISysSnConfigService sysSnConfigService;

    @Autowired
    public SysSnConfigController(ISysSnConfigService sysSnConfigService) {
        this.crudService = sysSnConfigService;
        this.sysSnConfigService = sysSnConfigService;
    }

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @return 唯一码
     */
    @PostMapping(value = "/provider/createsn")
    @Operation(summary = "根据分类创建唯一码")
    public ResultVO<String> createSn(String category) {
        String sn = sysSnConfigService.createSn(category);
        ResultVO<String> result = new ResultVO<>();
        return result.ok(sn);
    }


    /**
     * 创建唯一码
     *
     * @param category 分类
     * @param para     参数
     * @return 唯一码
     */
    @PostMapping(value = "/provider/createsnbypara")
    @Operation(summary = "根据参数创建唯一码")
    public ResultVO<String> createSnBypara(String category,@RequestBody Map<String, String> para) {
        String sn = sysSnConfigService.createSn(category, para);
        ResultVO<String> result = new ResultVO<>();
        return result.ok(sn);
    }

    /**
     * 添加编号规则
     *
     * @param seqDatas
     * @return
     */
    @PostMapping(value = "/provider/addseqconfig")
    @Operation(summary = "添加编号规则")
    public ResultVO<String> addSeqConfig(@RequestBody List<SysSnConfigEntity> seqDatas) {
        sysSnConfigService.addSeqConfig(seqDatas);
        sysSnConfigService.saveChange();
        ResultVO<String> result = new ResultVO<>();
        return result.ok("","操作成功");
    }

    /**
     * 删除
     *
     * @param categorys
     * @return
     */
    @PostMapping(value = "/provider/deletebycategory")
    @Operation(summary = "删除唯一码")
    public ResultVO<String> deleteByCategory(@RequestBody List<String> categorys) {
        sysSnConfigService.deleteByCategory(categorys);
        sysSnConfigService.saveChange();
        ResultVO<String> result = new ResultVO<>();
        return result.ok("","操作成功");
    }

}