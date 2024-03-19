package com.ca.mfd.prc.pps.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pps.dto.SetLinePara;
import com.ca.mfd.prc.pps.entity.PpsLineProductionConfigEntity;

import java.util.List;

/**
 * @author inkelink
 * @Description: 线体生产配置服务
 * @date 2023年09月12日
 * @变更说明 BY inkelink At 2023年09月12日
 */
public interface IPpsLineProductionConfigService extends ICrudService<PpsLineProductionConfigEntity> {

    /**
     * 获取所有的数据
     *
     * @return
     */
    List<PpsLineProductionConfigEntity> getAllDatas();

    /**
     * 设置生产区域
     *
     * @param para
     */
    void setLine(SetLinePara para);

    /**
     * 根据工位获取区域产品类型
     *
     * @param workstationCode
     * @return
     */
    String getLineByWorkstationCode(String workstationCode);

    /**
     * 根据线体获取配置
     *
     * @param lineCode
     * @return
     */
    PpsLineProductionConfigEntity getFirstByLineCode(String lineCode);
}