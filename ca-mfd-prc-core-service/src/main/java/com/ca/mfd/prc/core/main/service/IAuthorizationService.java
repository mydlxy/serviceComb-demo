package com.ca.mfd.prc.core.main.service;

import java.util.List;

/**
 * 权限认证类
 *
 * @author eric.zhou ${email}
 * @date 2023-07-17
 */
public interface IAuthorizationService {

    /**
     * 是否具有特定的权限
     *
     * @param permissionCode
     * @return
     */
    boolean hasPermission(String permissionCode);

    /**
     * 是否具有特定的权限（具有其中的一个)
     *
     * @param permissionCodes
     * @return
     */
    boolean hasPermission(List<String> permissionCodes);
}