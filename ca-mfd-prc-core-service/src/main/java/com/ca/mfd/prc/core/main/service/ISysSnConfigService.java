package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.entity.SysSnConfigEntity;

import java.util.List;
import java.util.Map;

/**
 * 唯一码配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysSnConfigService extends ICrudService<SysSnConfigEntity> {

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @return 唯一码
     */
    String createSn(String category);

    /**
     * 创建唯一码
     *
     * @param category 分类
     * @param para     参数
     * @return 唯一码
     */
    String createSn(String category, Map<String, String> para);

    /**
     * 添加编号规则
     *
     * @param seqDatas
     */
    void addSeqConfig(List<SysSnConfigEntity> seqDatas);

    /**
     * 删除
     *
     * @param categorys
     */
    void deleteByCategory(List<String> categorys);
}