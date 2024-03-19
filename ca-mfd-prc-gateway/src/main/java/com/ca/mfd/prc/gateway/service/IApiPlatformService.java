package com.ca.mfd.prc.gateway.service;

/**
 *
 * <p>功能描述: API平台相关</p>
 * @author   xxxx
 * @version  2023年7月4日
 */
public interface IApiPlatformService {
    /**
     *
     * <p>功能描述:API生命周期平台验证 </p>
     * @author
     * @version  2023年7月4日
     * @param   apiAppKey
     * @param   apiPlatformKey
     * @return   Boolean
     */
    Boolean chkApiPlatform(String apiAppKey,String apiPlatformKey);
}
