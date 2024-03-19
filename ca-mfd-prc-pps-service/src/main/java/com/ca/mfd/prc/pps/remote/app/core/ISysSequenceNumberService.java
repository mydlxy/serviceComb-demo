package com.ca.mfd.prc.pps.remote.app.core;

import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSequenceNumberEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * ISysSequenceNumberService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "main/syssequencenumber", contextId = "inkelink-core-syssequencenumber")
public interface ISysSequenceNumberService {

    /**
     * 自定义查询
     *
     * @param conditions
     * @return 列表
     */
    @PostMapping("/provider/getdata")
    ResultVO<List<SysSequenceNumberEntity>> getData(@RequestBody List<ConditionDto> conditions);

    /**
     * 获取流水号/需要提交统一事务，如果在循环中使用生成的流水号是一个
     *
     * @param seqType 流水号类别
     * @return 流水号
     */
    @GetMapping(value = "/provider/getseqnumwithtransaction")
    ResultVO<String> getSeqNumWithTransaction(@RequestParam("seqType") String seqType);

    /**
     * 获取序列号（自动增长）
     *
     * @param sequenceType 水水好类别
     * @return 流水号
     */
    @GetMapping(value = "/provider/getseqnum")
    ResultVO<String> getSeqNum(@RequestParam("sequenceType") String sequenceType);

    /**
     * 写入
     *
     * @param body
     * @return
     */
    @PostMapping(value = "/provider/insert")
    ResultVO<String> insert(@RequestBody SysSequenceNumberEntity body);

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    @GetMapping(value = "/provider/getsyssequenceinfobytype")
    ResultVO<SysSequenceNumberEntity> getSysSequenceInfoByType(@RequestParam("sequenceType") String sequenceType);
}
