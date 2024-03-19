package com.ca.mfd.prc.eps.remote.app.pps.provider;

import com.ca.mfd.prc.common.dto.IdsModel;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pps.IPpsModuleProductStatusService;
import com.ca.mfd.prc.eps.remote.app.pps.entity.PpsModuleProductStatusEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * PpsProductProcessProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PpsModuleProductStatusProvider {

    @Autowired
    private IPpsModuleProductStatusService ppsModuleProductStatusService;

    /**
     * 获取
     *
     * @return
     */
    public List<PpsModuleProductStatusEntity> getListByBarCodes(List<String> barcodes) {
        IdsModel dto = new IdsModel();
        dto.setIds(barcodes.toArray(new String[0]));
        ResultVO<List<PpsModuleProductStatusEntity>> result = ppsModuleProductStatusService.getListByBarCodes(dto);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsmoduleproductstatus调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 删除
     *
     * @return
     */
    public String deleteByProductBarcode(String barcode) {
        ResultVO<String> result = ppsModuleProductStatusService.deleteByProductBarcode(barcode);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsmoduleproductstatus调用失败" + result.getMessage());
        }
        return result.getData();
    }


    /**
     * 新增
     *
     * @return
     */
    public String insertModel(PpsModuleProductStatusEntity model) {
        ResultVO<String> result = ppsModuleProductStatusService.insertModel(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsmoduleproductstatus调用失败" + result.getMessage());
        }
        return result.getData();
    }

}