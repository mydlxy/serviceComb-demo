package com.ca.mfd.prc.core.dc.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.dc.dto.DcDetail;
import com.ca.mfd.prc.core.dc.dto.InitPageDataVO;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;

import java.util.List;

/**
 * @author inkelink
 */
public interface IDcPageConfigService extends ICrudService<DcPageConfigEntity> {

    /**
     * 初始化页面元素
     *
     * @param pagekey 页面参数
     * @return 页面配置
     */
    InitPageDataVO initPageData(String pagekey);

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<DcPageConfigEntity> getListByKey(String keyword);


    /**
     * 复制页面
     *
     * @param pageKey  key
     * @param pageName name
     * @param sourceId sourceId
     */
    void copy(String pageKey, String pageName, Long sourceId);

    /**
     * 当前权限用户临时权限绑定
     *
     * @param code
     * @return
     */
    List<DcDetail> getDcDetail(String code);

    /**
     * 根据权限代码查询页面数据
     *
     * @param code 权限代码
     * @return
     */
    List<DcPageConfigEntity> getListByCode(String code);
}
