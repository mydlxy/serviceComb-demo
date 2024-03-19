package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.PrmDepartMentNode;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;

import java.util.List;

/**
 * 部门管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmDepartmentService extends ICrudService<PrmDepartmentEntity> {
    /**
     * 获取部门树
     *
     * @param orgName 名字
     * @return 部门集合
     */
    List<PrmDepartMentNode> getPrmDepartmentTree(String orgName);

    /**
     * 获取下拉部门
     *
     * @param orgName 名字
     * @return 部门集合
     */
    List<ComboInfoDTO> getDropDownPrmDepartment(String orgName);

    /**
     * 保存部门数据
     *
     * @param models  部门集合
     * @param orgName 组织机构
     */
    void save(List<PrmDepartmentEntity> models, String orgName);
}