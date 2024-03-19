package com.ca.mfd.prc.avi.remote.app.eps.provider;


import com.ca.mfd.prc.avi.remote.app.eps.IEpsSpareBindingDetailService;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EpsSpareBindingDetailProvider {
    @Autowired
    IEpsSpareBindingDetailService epsSpareBindingDetailService;

    /**
     * 备件过点 查询虚拟VIN号
     *
     * @param id 主键
     * @return 虚拟VIN号集合
     */
    public List<String> getPartVirtualVinByPartTrackId(String id) {
        ResultVO<List<String>> result = epsSpareBindingDetailService.getPartVirtualVinByPartTrackId(id);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-eps-epssparebindingdetail调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
