package com.ca.mfd.prc.eps.remote.app.pm.provider;

import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.DataDto;
import com.ca.mfd.prc.common.model.base.dto.PageDataDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.eps.remote.app.pm.IPmProductMaterialMasterService;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmProductMaterialMasterEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * PmProductMaterialMasterProvider
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@Service
public class PmProductMaterialMasterProvider {

    @Autowired
    private IPmProductMaterialMasterService pmProductMaterialMasterService;

    /**
     * 获取
     *
     * @param model
     * @return
     */
    public List<PmProductMaterialMasterEntity> getdata(DataDto model) {
        ResultVO<List<PmProductMaterialMasterEntity>> result = pmProductMaterialMasterService.getdata(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductmaterialmaster调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 获取分页数据
     *
     * @param model
     * @return
     */
    public PageData<PmProductMaterialMasterEntity> getPageData(PageDataDto model) {
        ResultVO<PageData<PmProductMaterialMasterEntity>> result = pmProductMaterialMasterService.getPageData(model);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pm-pmproductmaterialmaster调用失败" + result.getMessage());
        }
        return result.getData();
    }

}