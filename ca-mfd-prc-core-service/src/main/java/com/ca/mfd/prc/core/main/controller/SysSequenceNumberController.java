package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ConvertUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysSequenceNumberEntity;
import com.ca.mfd.prc.core.main.service.ISysSequenceNumberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


/**
 * 业务编码配规则配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/syssequencenumber")
@Tag(name = " 业务编码配规则配置")
public class SysSequenceNumberController extends BaseController<SysSequenceNumberEntity> {

    private static final Logger logger = LoggerFactory.getLogger(SysSequenceNumberController.class);
    private final ISysSequenceNumberService sysSequenceNumberService;

    @Autowired
    public SysSequenceNumberController(ISysSequenceNumberService sysSequenceNumberService) {
        this.crudService = sysSequenceNumberService;
        this.sysSequenceNumberService = sysSequenceNumberService;
    }

    /**
     * 获取流水号/需要提交统一事务，如果在循环中使用生成的流水号是一个
     *
     * @param seqType 流水号类别
     * @return 流水号
     */
    @GetMapping(value = "/provider/getseqnumwithtransaction")
    @Operation(summary = "获取流水号/需要提交统一事务，如果在循环中使用生成的流水号是一个")
    public ResultVO<String> getSeqNumWithTransaction(String seqType) {
        String data = sysSequenceNumberService.getSeqNumWithTransaction(seqType);
        return new ResultVO<String>().ok(data);
    }

    /**
     * 获取序列号（自动增长）
     *
     * @param sequenceType 水水好类别
     * @return 流水号
     */
    @GetMapping(value = "/provider/getseqnum")
    @Operation(summary = "获取序列号（自动增长）")
    public ResultVO<String> getSeqNum(String sequenceType) {
        String data = sysSequenceNumberService.getSeqNum(sequenceType);
        if (StringUtils.isBlank(data)) {
            return new ResultVO<String>().error(-1, "获取序列号失败!");
        }
        return new ResultVO<String>().ok(data);
    }

    /**
     * 新增
     *
     * @return 新增
     */
    @PostMapping(value = "/provider/insert")
    @Operation(summary = "新增业务编码配规则配置")
    public ResultVO<String> insert(@RequestBody SysSequenceNumberEntity entity) {
        try {
            sysSequenceNumberService.save(entity);
            sysSequenceNumberService.saveChange();
            return new ResultVO<String>().ok(null);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultVO<String>().error("新增失败");
        }
    }

    @PostMapping("/provider/getdata")
    @Operation(summary = "根据添加获取业务编码配规则配置")
    public ResultVO<List<SysSequenceNumberEntity>> getData(@RequestBody List<ConditionDto> conditions) {
        return new ResultVO().ok(sysSequenceNumberService.getData(conditions));
    }

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    @GetMapping(value = "/provider/getsyssequenceinfobytype")
    @Operation(summary = "判断是否已经配置了工单生成规则")
    public ResultVO<SysSequenceNumberEntity> getSysSequenceInfoByType(String sequenceType) {
        return new ResultVO<SysSequenceNumberEntity>().ok(sysSequenceNumberService.getSysSequenceInfoByType(sequenceType));
    }

    /**
     * 查询sequenceType的数量
     * @param sequenceType
     * @return
     */
    @GetMapping(value = "/provider/getsequencetypecount")
    @Operation(summary = "查询sequenceType的数量")
    public ResultVO<Long> getSequenceTypeCount(String sequenceType){
        return new ResultVO<Long>().ok(sysSequenceNumberService.getSequenceTypeCount(sequenceType));
    }
}