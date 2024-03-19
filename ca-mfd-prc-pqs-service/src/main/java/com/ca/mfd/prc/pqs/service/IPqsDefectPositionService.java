package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.BatchAddPositionInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectPositionEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 缺陷位置代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDefectPositionService extends ICrudService<PqsDefectPositionEntity> {
    /**
     * 获取所有的位置信息
     *
     * @return
     */
    List<PqsDefectPositionEntity> getAllDatas();

    /**
     * 批量添加缺陷位置
     *
     * @param list
     */
    void batchAddPosition(List<BatchAddPositionInfo> list);

    /**
     * 批量传入需要删除的缺陷位置代码
     *
     * @param codeList
     */
    void batchDelByCodes(List<String> codeList);

    /**
     * getPositionShowList
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getPositionShowList(DefectFilterlParaInfo info);
}