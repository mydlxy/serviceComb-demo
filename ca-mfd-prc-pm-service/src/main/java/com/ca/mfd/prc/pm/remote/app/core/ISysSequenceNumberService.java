package com.ca.mfd.prc.pm.remote.app.core;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.sys.entity.SysSequenceNumberEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ca-mfd-prc-core-service", path = "main/syssequencenumber", contextId = "inkelink-core-syssequencenumber")
public interface ISysSequenceNumberService {

    /**
     * 判断是否已经配置了工单生成规则
     *
     * @param sequenceType 流水号类型
     * @return 查询实体
     */
    @GetMapping(value = "/provider/getSysSequenceInfoByType")
    ResultVO<SysSequenceNumberEntity> getSysSequenceInfoByType(@RequestParam("sequenceType") String sequenceType);

    /**
     * 新增
     *
     * @param entity 实体
     * @return 结果
     */
    @PostMapping(value = "/provider/insert")
    ResultVO<String> insert(@RequestBody SysSequenceNumberEntity entity);

    /**
     * 查询类型数量
     * @param sequenceType
     * @return
     */
    @GetMapping(value = "/provider/getSequenceTypeCount")
    ResultVO<Long> getSequenceTypeCount(@RequestParam("sequenceType") String sequenceType);
}
