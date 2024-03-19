package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmUserService extends ICrudService<PrmUserEntity> {

    /**
     * 获取实体
     */
    PrmUserEntity getFirstByPwdUid(String pwd, Serializable userId);

    /**
     * 获取所有用户（非管理员）
     *
     * @return 集合
     */
    List<PrmUserEntity> getAllNomallUsers();

    /**
     * 获取列表
     */
    List<PrmUserEntity> getListIds(List<Serializable> ids);

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<PrmUserEntity> getListByKey(String keyword);

    /**
     * 获取列表
     */
    List<PrmUserEntity> getListLa(String quary);

    /**
     * 获取列表
     */
    List<PrmUserEntity> getListLaOrder();

    /**
     * 设置用户密码
     *
     * @param userId   用户外键
     * @param password 用户密码
     */
    void setPassword(Serializable userId, String password);


    /**
     * 获取角色信息
     *
     * @return 角色集合
     */
    List<PrmRoleEntity> getRoleItem();

    /**
     * 编辑用户角色权限
     *
     * @param roles  角色集合
     * @param userId 用户外键
     */
    void editRoleSave(List<Serializable> roles, Serializable userId);

    /**
     * 获取用户权限
     *
     * @param userId   用户外键
     * @param userName 用户名
     * @return 权限集合
     */
    List<String> getPermissions(Serializable userId, String userName);

    //bool HasPermission(string permissionCode);

    //List<UserGroupBase> GetUserGroup(String language);

    /**
     * 查询所有用户
     *
     * @return 用户集合
     */
    List<PrmUserEntity> getAllUser();

    /**
     * 在用户缓存中获取一个用户
     *
     * @param id 用户外键
     * @return 用户对象
     */
    PrmUserEntity getCacheById(Serializable id);

    /**
     * MesLogin登录
     *
     * @return 返回MesLogin登录结果
     */
    List<PrmUserEntity> getMesLogin(String pwd, String userName);

    /**
     * 用户登录
     *
     * @param pwd
     * @param userName
     * @return
     */
    PrmUserEntity getPrmUserInfo(String pwd, String userName);

    /**
     * 授权登录
     *
     * @param userName 用户名
     * @return 返回AuthOpenLogin登录结果
     */
    List<PrmUserEntity> getAuthOpenLogin(String userName);

    /**
     * 更新冻结时间
     *
     * @param loginLockTime
     * @param userName
     */
    void updateEntityFrozenDt(int loginLockTime, String userName);

    /**
     * 用户冻结
     *
     * @param userName 用户名
     * @param userType 用户类型
     */
    void updateMemberFrozen(String userName, int userType);

    /**
     * 根据用户id获取用户信息
     *
     * @param ids 权限ids
     * @return 用户列表
     */
    List<UserData> getUsersByRoleId(List<Serializable> ids);
}