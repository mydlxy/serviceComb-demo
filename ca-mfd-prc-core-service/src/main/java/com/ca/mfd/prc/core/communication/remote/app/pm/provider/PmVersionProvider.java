package com.ca.mfd.prc.core.communication.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.communication.remote.app.pm.IPmVersionService;
import com.ca.mfd.prc.core.communication.remote.app.pm.dto.PmAllDTO;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmShcCalendarProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmVersionProvider {

    @Autowired
    private IPmVersionService pmVersionService;


    public PmAllDTO getObjectedPm() {
        ResultVO<PmAllDTO> result = pmVersionService.getObjectedPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }


    public List getAllElements(Class className) {
        ResultVO<List> result = pmVersionService.getAllElements(className);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

    public Document getCurretPm() {
        ResultVO<Document> result = pmVersionService.getCurretPm();
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmversion调用失败" + result.getMessage());
        }
        return result.getData();
    }

}