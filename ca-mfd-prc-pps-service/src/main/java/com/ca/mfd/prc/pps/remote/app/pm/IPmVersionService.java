package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.dto.PmAllDTO;
import org.dom4j.Document;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: PmVersion的feign接口
 * @date 2023年6月1日
 * @变更说明 BY eric.zhou At 2023年6月1日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmversion", contextId = "inkelink-pm-pmversion")
public interface IPmVersionService {

    /**
     * 查询一个实体
     *
     * @return 返回一个实体
     */
    @GetMapping("/provider/getobjectedpm")
    ResultVO<PmAllDTO> getObjectedPm();


    /**
     * 查询整个工厂对应节点的所有数据
     *
     * @param className 转换类型
     * @return 返回一个查询列表
     */
    @PostMapping(value = "/provider/getallelements")
    ResultVO<List> getAllElements(@RequestBody Class className);

    /**
     * 获取Document对象
     *
     * @return
     */
    @PostMapping(value = "/provider/getcurretpm")
    ResultVO<Document> getCurretPm();
}
