package com.ca.mfd.prc.core.prm.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.dto.PrmUserjoinDepartDto;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 部门关联员工表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmDepartmentJoinUserMapper extends IBaseMapper<PrmDepartmentJoinUserEntity> {

    /**
     * 获取用户根据部门
     *
     * @param userId
     * @return
     */
    List<PrmDepartmentEntity> getPrmUserByDepartmentId(Serializable userId);

    /**
     * 获取用户根据部门
     *
     * @param itemIds
     * @return
     */
    List<UserData> getPrmDepartmentJoinUser(List<Serializable> itemIds);

    /**
     * 接口权限集合(分页)
     *
     * @param page
     * @param pms
     * @return
     */
    Page<PrmUserjoinDepartDto> getUserPageDatas(Page<PrmInterfacePermissionListInfo> page, @Param("pms") Map<String, Object> pms);


}