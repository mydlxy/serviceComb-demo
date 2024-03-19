package com.ca.mfd.prc.pm.remote.app.core.provider;

import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.remote.app.core.IntegratedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntegratedProvider {
    @Autowired
    private IntegratedService integratedService;

    public QueryUserDTO getUserInfoByLoginName(String loginId) {
        ResultVO<QueryUserDTO> result = integratedService.getUserInfoByLoginName(loginId);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-core-integrated调用失败" + result.getMessage());
        }
        return result.getData();
    }
}
