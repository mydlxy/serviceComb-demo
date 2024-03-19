package com.ca.mfd.prc.pps.remote.app.eps.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.eps.IEpsBodySparePartTrackService;
import com.ca.mfd.prc.pps.remote.app.eps.entity.EpsBodySparePartTrackEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EpsBodySparePartTrackProvider {
    @Autowired
    IEpsBodySparePartTrackService epsBodySparePartTrackService;

    /**
     * 根据 虚拟VIN号 查询备件运输跟踪
     *
     * @param vehicleSn 虚拟VIN号
     * @return 返回 焊装车间备件运输跟踪
     */
    public EpsBodySparePartTrackEntity getEntityByVirtualVin(String vehicleSn) {
        ResultVO<EpsBodySparePartTrackEntity> result = epsBodySparePartTrackService.getEntityByVirtualVin(vehicleSn);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-epsbodyspareparttrack调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
