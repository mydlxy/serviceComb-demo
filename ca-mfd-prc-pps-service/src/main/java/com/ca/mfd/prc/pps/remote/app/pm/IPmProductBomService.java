package com.ca.mfd.prc.pps.remote.app.pm;

import com.ca.mfd.prc.common.model.base.dto.TopDataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.pm.entity.PmProductBomEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author eric.zhou
 * @Description: BOM详细
 * @date 2023年6月7日
 * @变更说明 BY eric.zhou At 2023年6月7日
 */
@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmproductbom", contextId = "inkelink-pm-pmproductbom")
public interface IPmProductBomService {
    /**
     * 获取列表数据
     *
     * @param model 条件表达式
     * @return List<PmProductBomEntity>
     */
    @PostMapping("/provider/gettopdatas")
    ResultVO<List<PmProductBomEntity>> getTopDatas(@RequestBody TopDataDto model);
}
