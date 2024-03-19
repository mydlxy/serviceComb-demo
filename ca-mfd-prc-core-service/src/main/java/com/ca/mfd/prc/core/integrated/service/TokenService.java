package com.ca.mfd.prc.core.integrated.service;

import com.ca.mfd.prc.common.entity.OnlineUserDTO;

/**
 * 统一门户对接相关接口
 *
 * @author mason
 * @date 2023/9/19 16:04
 */
public interface TokenService {
    /**
     *获取本地存储的用户信息
     *
     * @param token
     * @return
     */
    OnlineUserDTO getlocaluserinfobytoken(String token);


    /**
     * 根据token获取用户信息
     *
     * @param token
     * @param refreshToken
     * @return
     */
    OnlineUserDTO getUserInfo(String token, String refreshToken);


    /**
     * 拉取门户
     * @param token
     * @param refreshToken
     * @return
     */
    OnlineUserDTO checkToken (String token, String refreshToken);

}
