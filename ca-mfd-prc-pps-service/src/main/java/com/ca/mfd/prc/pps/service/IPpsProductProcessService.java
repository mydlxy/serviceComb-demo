package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.entity.PpsProductProcessEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 工艺路径设置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPpsProductProcessService extends ICrudService<PpsProductProcessEntity> {
    /**
     * 获取所有的数据
     *
     * @return List<PpsProductProcessEntity>
     */
    List<PpsProductProcessEntity> getAllDatas();

    /**
     * 获取数据
     *
     * @param orderCategory 分类
     * @return 查询结果
     */
    PpsProductProcessEntity getProcess(String orderCategory);

    /**
     * 获取工艺路径列表
     *
     * @param orderCategory orderCategory
     * @param orderType     orderType
     * @return 工艺路径列表
     */
    List<PpsProductProcessEntity> getProcessList(String orderCategory, String orderType);
}