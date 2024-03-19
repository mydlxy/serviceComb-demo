package com.ca.mfd.prc.avi.remote.app.pps.Provider;

import com.ca.mfd.prc.avi.host.scheduling.dto.PpsEntryReportPartsDto;
import com.ca.mfd.prc.avi.remote.app.pps.IPpsEntryReportPartsService;
import com.ca.mfd.prc.avi.remote.app.pps.IPpsEntryService;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryEntity;
import com.ca.mfd.prc.avi.remote.app.pps.entity.PpsEntryReportPartsEntity;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.utils.ResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class PpsEntryReportPartsProvider {
    @Autowired
    IPpsEntryReportPartsService ppsEntryReportPartsService;

    /**
     * 根据分类获取前20条
     *
     * @param orderCategory
     * @return 列表
     */
    public List<PpsEntryReportPartsEntity> getTopDataByOrderCategory(Integer orderCategory) {
        ResultVO<List<PpsEntryReportPartsEntity>> result = ppsEntryReportPartsService.gettopdatabyordercategory(orderCategory);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryreportparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 根据分类
     *
     * @param datas
     * @return 列表
     */
    public List<PpsEntryReportPartsEntity> getRecordByOrderCategory(PpsEntryReportPartsDto datas) {
        ResultVO<List<PpsEntryReportPartsEntity>> result = ppsEntryReportPartsService.getrecordbyordercategory(datas);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryreportparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

    /**
     * 更新状态
     *
     * @param id 主键
     */
    public String updateIsPassAviById(Long id) {
        ResultVO<String> result = ppsEntryReportPartsService.updateIsPassAviById(id);
        if (!result.getSuccess()) {
            throw new InkelinkException("服务inkelink-pps-ppsentryreportparts调用失败" + result.getMessage());
        }
        return result.getData();
    }

}
