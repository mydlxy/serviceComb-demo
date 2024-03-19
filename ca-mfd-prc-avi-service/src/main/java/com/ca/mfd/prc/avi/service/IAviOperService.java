package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviOperEntity;
import com.ca.mfd.prc.common.service.ICrudService;

import java.util.List;
import com.ca.mfd.prc.common.utils.ResultVO;

/**
 * 关键点行为配置
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviOperService extends ICrudService<AviOperEntity> {
    /**
     * 获取所有数据
     *
     * @return 返回数据列表
     */
    List<AviOperEntity> getAllDatas();


    List<AviOperEntity> getAviOperEntityList(int aviType, String aviCode);




    /**
     * 后台手动获取需要执行的行为
     * @param aviCode
     * @param sn
     * @param aviType
     * @return
     */
//    ResultVO<List<AviOperEntity>> getAviOperInfo(String aviCode, String sn, int aviType);
}