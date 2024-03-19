package com.ca.mfd.prc.avi.service;

import com.ca.mfd.prc.avi.entity.AviBlockEntity;
import com.ca.mfd.prc.common.service.ICrudService;

/**
 * 整车AVI锁定
 *
 * @author inkelink ${email}
 * @since 1.0.0 2023-04-06
 */
public interface IAviBlockService extends ICrudService<AviBlockEntity> {
    /**
     * 验证车辆是否有锁定
     *
     * @param sn      产品唯一标识
     * @param aviCode aviCode
     * @return 返回车辆是否锁定
     */
    boolean validExistsBlock(String sn, String aviCode);
}