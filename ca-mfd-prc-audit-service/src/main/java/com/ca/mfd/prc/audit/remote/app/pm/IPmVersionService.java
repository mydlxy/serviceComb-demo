package com.ca.mfd.prc.audit.remote.app.pm;

import com.ca.mfd.prc.audit.remote.app.pm.dto.PmAllDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author edwards.qu
 */
@FeignClient(
        name = "ca-mfd-prc-pm-service",
        path = "pmversion",
        contextId = "inkelink-pm-pmversion")
public interface IPmVersionService {

    /**
     * @return 返回一个实体
     * @Description: 查询一个实体
     * @逻辑描述
     * @author eric.zhou
     * @date 2023年4月4日
     * @变更说明 BY eric.zhou At 2023年4月4日
     */
    @GetMapping("/provider/getobjectedpm")
    ResultVO<PmAllDTO> getObjectedPm();



    /**
     * @param className 转换类型
     * @return 返回一个查询列表
     * @Description: 查询整个工厂对应节点的所有数据
     * @逻辑描述
     * @author eric.zhou
     * @date 2023年6月1日
     * @变更说明 BY eric.zhou At 2023年6月1日
     */
    @PostMapping(value = "/provider/getallelements")
    ResultVO<List> getAllElements(@RequestBody Class className);

    /**
     * 获取岗位数据
     */
    @GetMapping("/provider/getworkplacecombo")
    ResultVO getWorkplaceComboPqs(@RequestParam("shopCode") String shopCode, @RequestParam("workplaceType") String workplaceType);
}
