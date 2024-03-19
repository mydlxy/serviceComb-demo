package com.ca.mfd.prc.pm.remote.app.cmc;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.CmcPmAreaVo;
import com.ca.mfd.prc.pm.dto.CmcPmOrganizationVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkCenterVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkUnitVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@FeignClient(name = "ca-mfd-cmc-pm-service", path = "company", contextId = "inkelink-cmc-pm-company")
public interface ICmcPmCompanyService {

    /**
     * 获取工厂信息
     * @param model
     * @return
     */
    @PostMapping(value = "/plant/getdata")
    ResultVO<List<CmcPmOrganizationVo>> getPlantPage(@RequestBody DataDto model);

    /**
     * 编辑工厂信息
     * @param dto
     * @return
     */
    @PostMapping(value = "/plant/edit")
    ResultVO<CmcPmOrganizationVo> edit(@RequestBody CmcPmOrganizationVo dto);

    /**
     * 删除工厂
     * @param model
     * @return
     */
    @PostMapping(value = "/plant/del")
    ResultVO<CmcPmOrganizationVo> delete(@RequestBody IdsModel model);

    /**
     * 获取区域信息
     * @param model
     * @return
     */
    @PostMapping(value = "/area/getdata")
    ResultVO<List<CmcPmAreaVo>> getAreaPage(@RequestBody DataDto model);

    /**
     * 获取工作中心信息
     * @param model
     * @return
     */
    @PostMapping(value = "/workcenter/getdata")
    ResultVO<List<CmcPmWorkCenterVo>> getWorkCenterPage(@RequestBody DataDto model);

    /**
     * 获取工作单元信息
     * @param model
     * @return
     */
    @PostMapping(value = "/workunit/getdata")
    ResultVO<List<CmcPmWorkUnitVo>> getWorkUnitPage(@RequestBody DataDto model);


}
