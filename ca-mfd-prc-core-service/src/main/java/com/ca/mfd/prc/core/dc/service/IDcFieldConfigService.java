package com.ca.mfd.prc.core.dc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.dc.dto.FieldBatchPara;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;

import java.util.List;

/**
 * 字段配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IDcFieldConfigService extends ICrudService<DcFieldConfigEntity> {
    /**
     * 根据页面获取字段
     *
     * @param id 页面ID
     * @return 字段列表
     */
    List<DcFieldConfigEntity> getFieldListByPage(Long id);

    /**
     * 批量设置字段配置
     *
     * @param para 配置数据
     */
    void batchSet(FieldBatchPara para);

    /**
     * 获取页面字段列表
     *
     * @param pageId
     * @return
     */
    List<DcFieldConfigEntity> getPageFieldList(Long pageId);
}