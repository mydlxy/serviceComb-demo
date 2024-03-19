package com.ca.mfd.prc.core.main.service.impl;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.core.main.service.IAuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限认证类
 *
 * @author eric.zhou ${email}
 * @date 2023-07-17
 */
@Service
public class AuthorizationServiceImpl implements IAuthorizationService {

    private static final Object lockObj = new Object();
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private LocalCache localCache;

    /**
     * 是否具有特定的权限
     *
     * @param permissionCode
     */
    @Override
    public boolean hasPermission(String permissionCode) {
        OnlineUserDTO user = identityHelper.getLoginUser();
        if (user == null) {
            return false;
        }
        String userId = user.getId().toString();

        List<String> permissions = localCache.getObject("USER_PERMISSIONS_" + userId);
        if (permissions == null || permissions.size() == 0) {
            synchronized (lockObj) {
                permissions = localCache.getObject("USER_PERMISSIONS_" + userId);
                if (permissions == null || permissions.size() == 0) {
                    permissions = identityHelper.getLoginUser().getPermissions();
                    //历史写法未集成ua
                    //permissions = GetPermissions(userId);
                    localCache.addObject("USER_PERMISSIONS_" + userId, permissions, 60 * 10);
                }
            }
        }
        return permissions.contains(permissionCode);
    }

    /**
     * 是否具有特定的权限（具有其中的一个)
     *
     * @param permissionCodes
     */
    @Override
    public boolean hasPermission(List<String> permissionCodes) {
        boolean hasPermission = false;
        if (permissionCodes != null) {
            for (String permissionCode : permissionCodes) {
                hasPermission = hasPermission(permissionCode);
                if (hasPermission) {
                    break;
                }
            }
        }
        return hasPermission;
    }
}