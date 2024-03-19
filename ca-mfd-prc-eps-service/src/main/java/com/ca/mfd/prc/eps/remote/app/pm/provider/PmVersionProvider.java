package com.ca.mfd.prc.eps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.IPmVersionService;
import com.ca.mfd.prc.eps.remote.app.pm.dto.PmAllDTO;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PmVersionProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmVersionProvider {

    @Autowired
    private IPmVersionService pmVersionService;

    /**
     * 获取
     *
     * @return
     */
    public PmAllDTO getObjectedPm() {
        ResultVO<PmAllDTO> result = pmVersionService.getObjectedPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取
     *
     * @return
     */
    public Document getCurretPm() {
        ResultVO<Document> result = pmVersionService.getCurretPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

}