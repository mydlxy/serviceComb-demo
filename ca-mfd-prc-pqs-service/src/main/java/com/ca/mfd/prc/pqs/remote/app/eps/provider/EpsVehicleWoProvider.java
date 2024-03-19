package com.ca.mfd.prc.pqs.remote.app.eps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pqs.remote.app.eps.IEpsVehicleWoService;
import com.ca.mfd.prc.pqs.remote.app.eps.entity.EpsVehicleWoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author edwards.qu
 */
@Service
public class EpsVehicleWoProvider {

    @Autowired
    private IEpsVehicleWoService epsVehicleWoService;

    public List<EpsVehicleWoEntity> getEpsVehicleWoDatas(String sn) {
        ResultVO<List<EpsVehicleWoEntity>> result = epsVehicleWoService.getEpsVehicleWoDatas(sn);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-epsvehiclewo调用失败" + result.getMessage());
        }
        return result.getData();
    }
}