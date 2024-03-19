package com.ca.mfd.prc.core.integrated.service;

import com.ca.mfd.prc.common.dto.core.OauthLoginInfo;
import com.ca.mfd.prc.common.entity.DepartmentDTO;
import com.ca.mfd.prc.common.entity.GraphicCodeDTO;
import com.ca.mfd.prc.common.entity.QueryUserDTO;
import com.ca.mfd.prc.common.entity.QueryUserOrgDTO;
import com.ca.mfd.prc.common.entity.QueryUserPermissionDTO;
import com.ca.mfd.prc.common.utils.ResultVO;

import java.util.List;

/**
 * 统一门户对接相关接口
 *
 * @author mason
 * @date 2023/9/19 16:04
 */
public interface UserService {

    /**
     * 根据用户ID或者登录ID获取用户信息
     *
     * @param userId  用户ID
     * @param loginId 登录ID
     * @return
     */
    QueryUserDTO queryUser(Long userId, String loginId);

    /**
     * 获取用户多组织信息
     *
     * @param userId 长安用户ID
     * @return
     */
    QueryUserOrgDTO queryUserOrg(Long userId);

    /**
     * 根据用户ID获取用户权限列表
     *
     * @param userId 长安用户ID
     * @return
     */
    QueryUserPermissionDTO queryUserPermission(Long userId);

    /**
     * 获取图形验证码
     *
     * @return
     */
    GraphicCodeDTO getGraphicCode();

    /**
     * 统一门户授权登录
     *
     * @param oauthLoginInfo
     * @return
     */
    ResultVO oauthLogin(OauthLoginInfo oauthLoginInfo);


    /**
     * 查询部门及下级部门
     *
     * @param departmentId
     * @return
     */
    List<DepartmentDTO> departmentQuery(String departmentId);

}
