package com.ca.mfd.prc.avi.remote.app.pps.Provider;

import com.ca.mfd.prc.avi.remote.app.pps.IPpsEntryService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.avi.remote.app.pps.dto.BodyVehicleDTO;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PpsEntryProvider {

    @Autowired
    IPpsEntryService ppsEntryService;

    /**
     * 根据TPS查询工单列表
     *
     * @return 获取一个列表
     */
    public List<PpsEntryEntity> getPpsEntryBySn(String tpsCode) {
        ResultVO<List<PpsEntryEntity>> result = ppsEntryService.getPpsEntryBySn(tpsCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取已下发未打印焊装上上线列表
     *
     * @return 获取一个列表
     */
    public List<BodyVehicleDTO> getNoPrintTpscode() {
        ResultVO<List<BodyVehicleDTO>> result = ppsEntryService.getNoPrintTpscode();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取已下发已打印焊装上线列表
     *
     * @return 获取一个列表
     */
    public List<BodyVehicleDTO> getPrintTpscode() {
        ResultVO<List<BodyVehicleDTO>> result = ppsEntryService.getPrintTpscode();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 读取已经下发队列
     *
     * @return 获取一个列表
     */
    public List<BodyVehicleDTO> getDownTpsCode() {
        ResultVO<List<BodyVehicleDTO>> result = ppsEntryService.getDownTpsCode();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 设置TPS码为已打印
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 参数车间编码
     */
    public void setPrintTpsCode(String tpsCode, String shopCode) {
        ResultVO<String> result = ppsEntryService.setPrintTpsCode(tpsCode, shopCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
    }

    /**
     * 设置车辆为上线
     *
     * @param tpsCode  参数tps编码
     * @param shopCode 车间code
     */
    public void setBodyEntryOnline(String tpsCode, String shopCode) {
        ResultVO<String> result = ppsEntryService.setBodyEntryOnline(tpsCode, shopCode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-ppsentry调用失败" + result.getMessage());
        }
    }
}
