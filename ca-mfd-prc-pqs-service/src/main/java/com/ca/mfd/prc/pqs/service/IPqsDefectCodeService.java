package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.BatchAddCodeInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectCodeEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 缺陷代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDefectCodeService extends ICrudService<PqsDefectCodeEntity> {
    /**
     * 获取所有的缺陷信息
     *
     * @return
     */
    List<PqsDefectCodeEntity> getAllDatas();

    /**
     * 批量添加缺陷分类
     *
     * @param list
     */
    void batchAddCode(List<BatchAddCodeInfo> list);

    /**
     * batchDelByCodes
     *
     * @param codeList
     */
    void batchDelByCodes(List<String> codeList);

    /**
     * getCodeShowList
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getCodeShowList(DefectFilterlParaInfo info);
}