package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.entity.CookieSession;
import com.ca.mfd.prc.common.entity.LocalSession;
import com.ca.mfd.prc.common.entity.OnlineUserDTO;
import com.ca.mfd.prc.common.entity.PrmUserDto;
import com.ca.mfd.prc.common.entity.SessionEntity;

import java.util.List;

/**
 * @author tom.wu
 */
public interface ISessionService {

    /**
     * 用户LocalSession信息获取并将相关公共内容同步memcache缓存
     *
     * @param sessionId
     * @return 本地session
     */
    LocalSession getLocalSession(String sessionId);

    /**
     * 刷新会话
     *
     * @param userId
     * @return 是否成功
     */
    Boolean refreshSessionAsync(String userId);

    /**
     * 登录
     *
     * @param userName
     * @param pwd
     * @param loginStyle
     * @param rememberType
     * @param extData
     * @param sessionEntity
     * @param userType      = 0
     * @return 本地session
     */
    LocalSession memberLoginAsync(String userName, String pwd,
                                  int loginStyle, int rememberType,
                                  String extData, SessionEntity sessionEntity, int userType) throws Exception;

    /**
     * 自动登录 并同步到异步缓存中去2: string sessionId,
     *
     * @param cookieSession
     * @param userHostAddress
     * @return 本地session
     */
    LocalSession memberAutoLoginAsync(CookieSession cookieSession, String userHostAddress) throws Exception;

    /**
     * 登出
     *
     * @param cookieSession
     * @param status
     * @return 成功
     */
    Boolean memberLogOut(CookieSession cookieSession, int status) throws Exception;

    /**
     * 仓储所有失效的会话
     *
     * @return 成功
     */
    Boolean sessionStoreAll();

    /**
     * 仓储所有选中的会话
     *
     * @param ids
     * @return 成功
     */
    Boolean sessionStoreSelected(List<String> ids);

    /**
     * UA专用接口用于刷新TOKEN
     *
     * @param id
     * @param accessToken
     * @param refreshToken
     * @return 用户信息
     */
    Boolean updateUaToken(String id, String accessToken, String refreshToken);

    /**
     * 获取所有用户信息
     *
     * @return 用户信息
     */
    List<OnlineUserDTO> getUsers() throws Exception;

    /**
     * 所有当前用户信息
     *
     * @param ids
     * @return 用户信息
     */
    List<OnlineUserDTO> getUsersByArrayId(List<String> ids) throws Exception;


    /**
     * 所有当前用户信息
     *
     * @param ids
     * @return 用户信息
     */
    List<OnlineUserDTO> getUsersByRoleId(List<String> ids) throws Exception;

    /**
     * 获取所有用户信息
     *
     * @param id
     * @return 用户信息
     */
    OnlineUserDTO getUserById(String id) throws Exception;

    /**
     * 获取用户信息
     *
     * @param codeOrUserName
     * @return 用户信息
     */
    OnlineUserDTO getUserByCode(String codeOrUserName) throws Exception;

    /**
     * 根基部门id获取用户信息
     *
     * @param ids
     * @return 用户信息
     */
    List<OnlineUserDTO> getDepartmentByUser(List<String> ids) throws Exception;

    /**
     * 获取用户信息
     *
     * @param ids
     * @return 用户信息
     */
    List<OnlineUserDTO> getUserByDepartmentIds(List<String> ids) throws Exception;

    /**
     * 添加用户
     *
     * @param userDatas 添加用户
     * @return 成功
     */
    Boolean prmAddUsersByRequest(List<PrmUserDto> userDatas);


    /**
     * 获取用户信息
     *
     * @param base64user
     * @return 用户信息
     */
    OnlineUserDTO getUsersByBase64Request(String base64user) throws Exception;
}