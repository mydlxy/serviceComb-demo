package com.ca.mfd.prc.core.dc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.ButtonBatchSetPara;
import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;

import java.util.List;

/**
 * 按钮配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IDcButtonConfigService extends ICrudService<DcButtonConfigEntity> {

    /**
     * 根据页面获取ButtonList
     *
     * @param id 页面ID
     * @return 按钮列表
     */
    List<DcButtonConfigEntity> getButtonListByPage(Long id);

    /**
     * 批量设置按钮配置
     *
     * @param para 配置数据
     */
    void batchSet(ButtonBatchSetPara para);

    /**
     * 获取页面按钮列表
     *
     * @param pageId 页面外键
     * @return
     */
    List<DcButtonConfigEntity> getPageButtonList(Long pageId);

    /**
     * 根据权限代码查询按钮配置数据
     *
     * @param code 权限代码
     * @return
     */
    List<DcButtonConfigEntity> getListByCode(String code);
}