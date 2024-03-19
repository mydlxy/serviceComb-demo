package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.PrmJoinUserDto;
import com.ca.mfd.prc.core.prm.dto.PrmUserjoinDepartDto;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 部门关联员工表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmDepartmentJoinUserService extends ICrudService<PrmDepartmentJoinUserEntity> {

    /**
     * 获取数据
     *
     * @param saveDepartJoinUser
     * @return
     */
    List<Serializable> getOldUserIds(PrmJoinUserDto saveDepartJoinUser);

    /**
     * 删除
     *
     * @param deptId
     * @param prmUserId
     */
    void deleteByDeptUserId(Serializable deptId, Serializable prmUserId);

    /**
     * 获取单挑数据
     *
     * @param userId
     * @return
     */
    PrmDepartmentJoinUserEntity getFirstPrmUserId(Serializable userId);

    /**
     * 获取分页数据
     *
     * @param conditions
     * @param pageIndex
     * @param pageSize
     * @return
     */
    PageData<PrmUserjoinDepartDto> getUserPageDatas(List<ConditionDto> conditions, int pageIndex, int pageSize);

    /**
     * 返回部门用户
     *
     * @param ids
     * @return
     */
    List<UserData> getPrmDepartmentJoinUser(List<Serializable> ids);

    /**
     * 通过用户id返回部门信息
     *
     * @param id
     * @return
     */
    PrmDepartmentEntity getPrmDepartmentByUserId(Serializable id);

    /**
     * 保存用户信息
     *
     * @param prmJoinUserDto
     */
    void save(PrmJoinUserDto prmJoinUserDto);
}