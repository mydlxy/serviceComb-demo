package com.ca.mfd.prc.pm.remote.app.cmc;

import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.CmcPmAreaVo;
import com.ca.mfd.prc.pm.dto.CmcPmBopVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentPowerVo;
import com.ca.mfd.prc.pm.dto.CmcPmEquipmentVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkCenterVo;
import com.ca.mfd.prc.pm.dto.CmcPmWorkUnitVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@FeignClient(name = "ca-mfd-cmc-pm-service", path = "factory", contextId = "inkelink-cmc-pm-factory")
public interface ICmcPmFactoryService {

    /**
     * 批量删除
     * @param code
     * @param source
     * @param dataType
     * @param ids
     * @return
     */
    @PostMapping("/main/batchdelete")
    ResultVO batchDelete(@RequestHeader String code,
                                @RequestHeader Integer source,
                                @RequestHeader String dataType,
                                @RequestBody List<Long> ids);

    /**
     * 保存区域
     * @param code
     * @param source
     * @param areas
     * @return
     */
    @PostMapping("/main/batchsave/area")
     ResultVO areaBatchSave(@RequestHeader String code,
                                  @RequestHeader Integer source,
                                  @RequestBody List<CmcPmAreaVo> areas);

    /**
     * 保存工作中心
     * @param code
     * @param source
     * @param workCenters
     * @return
     */
     @PostMapping("/main/batchsave/workcenter")
     ResultVO workCenterBatchSave(@RequestHeader String code,
                                        @RequestHeader Integer source,
                                        @RequestBody List<CmcPmWorkCenterVo> workCenters);

    /**
     * 保存工作单元
     * @param code
     * @param source
     * @param workUnits
     * @return
     */
     @PostMapping("/main/batchsave/workunit")
     ResultVO workUnitBatchSave(@RequestHeader String code,
                                      @RequestHeader Integer source,
                                      @RequestBody List<CmcPmWorkUnitVo> workUnits);

    /**
     * 保存bop
     * @param code
     * @param source
     * @param bops
     * @return
     */
    @PostMapping("/main/batchsave/bop")
    ResultVO bopBatchSave(@RequestHeader String code,
                                 @RequestHeader Integer source,
                                 @RequestBody List<CmcPmBopVo> bops);

    /**
     * 保存设备
     * @param code
     * @param source
     * @param equipments
     * @return
     */
    @PostMapping("/main/batchsave/equipment")
    ResultVO equipmentBatchSave(@RequestHeader String code,
                          @RequestHeader Integer source,
                          @RequestBody List<CmcPmEquipmentVo> equipments);

    /**
     * 保存设备能力
     * @param code
     * @param source
     * @param equipmentPowers
     * @return
     */
    @PostMapping("/main/batchsave/equipmentpower")
    ResultVO equipmentPowerBatchSave(@RequestHeader String code,
                                @RequestHeader Integer source,
                                @RequestBody List<CmcPmEquipmentPowerVo> equipmentPowers);

    /**
     * 获取设备信息
     * @param model
     * @return
     */
    @PostMapping(value = "/equipment/getdata")
    ResultVO<List<CmcPmEquipmentVo>> getEquipmentPage(@RequestBody DataDto model);

    /**
     * 获取设备能力信息
     * @param model
     * @return
     */
    @PostMapping(value = "/equipmentpower/getdata")
    ResultVO<List<CmcPmEquipmentPowerVo>> getEquipmentPowerPage(@RequestBody DataDto model);

    /**
     * 获取Bop信息
     * @param model
     * @return
     */
    @PostMapping(value = "/bop/getdata")
    ResultVO<List<CmcPmBopVo>> getBopPage(@RequestBody DataDto model);

}
