package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmProductBomEntity;
import com.ca.mfd.prc.pm.entity.PmTraceComponentEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 追溯组件配置
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmTraceComponentService extends ICrudService<PmTraceComponentEntity> {

    /**
     * 获取所有数据
     *
     * @return 数据列表
     */
    List<PmTraceComponentEntity> getAllDatas();

    /**
     * 根据code查询
     * @param code
     * @return
     */
    PmTraceComponentEntity getByCode(String code);

    /**
     * 根据物料保存
     * @param materialNo
     * @param materialCn
     * @return
     */
    void saveByBom(String  materialNo,String materialCn) ;

    /**
     * 根据物料保存
     * @param boms
     * @return
     */
     void saveByBom(List<PmProductBomEntity> boms);
}