package com.ca.mfd.prc.avi.remote.app.pps;

import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ca-mfd-prc-pps-service", path = "ppsentry", contextId = "inkelink-pps-ppsentry")
public interface IPpsEntryService {

    /**
     * 根据TPS查询工单列表
     *
     * @return 获取一个列表
     */
    @GetMapping("/provider/getppsentrybysn")
    ResultVO<List<PpsEntryEntity>> getPpsEntryBySn(@RequestParam("tpsCode") String tpsCode);

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 获取一个列表
     */
    @GetMapping("/provider/getnoprinttpscode")
    ResultVO<List<BodyVehicleDTO>> getNoPrintTpscode();

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    @GetMapping("/provider/getprinttpscode")
    ResultVO<List<BodyVehicleDTO>> getPrintTpscode();

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    @GetMapping("/provider/getdowntpscode")
    ResultVO<List<BodyVehicleDTO>> getDownTpsCode();

    /**
     * 设置TPS码为已打印
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 参数车间编码
     */
    @GetMapping("/provider/setprinttpscode")
    ResultVO<String> setPrintTpsCode(@RequestParam("tpsCode") String tpsCode, @RequestParam("shopCode") String shopCode);

    /**
     * 设置车辆为上线
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 车间code
     */
    @GetMapping("/provider/setbodyentryonline")
    ResultVO<String> setBodyEntryOnline(@RequestParam("tpsCode") String tpsCode, @RequestParam("shopCode") String shopCode);

}
