package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.BatchAddComponentInfo;
import com.ca.mfd.prc.pqs.dto.DefectFilterlParaInfo;
import com.ca.mfd.prc.pqs.dto.DefectShowInfo;
import com.ca.mfd.prc.pqs.entity.PqsDefectComponentEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 组件代码服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsDefectComponentService extends ICrudService<PqsDefectComponentEntity> {
    /**
     * 获取所有的组件信息
     *
     * @return
     */
    List<PqsDefectComponentEntity> getAllDatas();

    /**
     * 批量添加缺陷分类
     *
     * @param list
     */
    void batchAddComponent(List<BatchAddComponentInfo> list);

    /**
     * 批量传入需要删除的组件代码
     *
     * @param codeList
     */
    void batchDelByCodes(List<String> codeList);

    /**
     * getComponentShowList
     *
     * @param info
     * @return
     */
    List<DefectShowInfo> getComponentShowList(DefectFilterlParaInfo info);
}