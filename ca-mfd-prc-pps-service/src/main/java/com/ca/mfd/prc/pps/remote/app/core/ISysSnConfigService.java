package com.ca.mfd.prc.pps.remote.app.core;


import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysSnConfigEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * ISysSnConfigService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "main/syssnconfig", contextId = "inkelink-core-syssnconfig")
public interface ISysSnConfigService {

    /**
     * 获取序列号
     *
     * @param category
     * @return 流水号
     */
    @PostMapping("/provider/createsn")
    ResultVO<String> createSn(@RequestParam("category") String category);

    /**
     * 获取序列号
     *
     * @param category
     * @param para
     * @return 流水号
     */
    @PostMapping("/provider/createsnbypara")
    ResultVO<String> createSnBypara(@RequestParam("category") String category, @RequestBody Map<String, String> para);

    /**
     * 添加编号规则
     *
     * @param seqDatas
     * @return
     */
    @PostMapping(value = "/provider/addseqconfig")
    ResultVO<String> addSeqConfig(@RequestBody List<SysSnConfigEntity> seqDatas);

    /**
     * 删除
     *
     * @param categorys
     * @return
     */
    @PostMapping(value = "/provider/deletebycategory")
    ResultVO<String> deleteByCategory(@RequestBody List<String> categorys);
}
