package com.ca.mfd.prc.pmc.remote.app.pm;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pmc.remote.app.pm.entity.PmEquipmentEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pm-service", path = "pmequipment", contextId = "inkelink-pm-pmequipment")
public interface IPmEquipmentService {
    @GetMapping(value = "/provider/getPmEquipmentEntityByCode")
    ResultVO<PmEquipmentEntity> getPmEquipmentEntityByCode(@RequestParam("code") String code);
}
