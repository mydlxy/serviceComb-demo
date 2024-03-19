package com.ca.mfd.prc.core.prm.service;

import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;

import java.io.Serializable;
import java.util.List;

/**
 * 接口权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface IPrmInterfacePermissionService extends ICrudService<PrmInterfacePermissionEntity> {
    /***
     * 获取所有的数据
     * @return
     */
    List<PrmInterfacePermissionEntity> getAllDatas();

    /**
     * 获取数据
     *
     * @param permissionId id
     * @return a
     */
    List<PrmInterfacePermissionEntity> getListByPrmPermissionid(Serializable permissionId);

    /***
     * 获取接口无效的权限ID
     * @return
     */
    List<String> getInterfacePermissionRemoves();

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    List<PrmInterfacePermissionEntity> getListByKey(String keyword);

    /**
     * 获取分页数据
     *
     * @param conditions
     * @param sorts
     * @param isDelete
     * @return
     */
    List<PrmInterfacePermissionListInfo> getInterfaceDatas(List<ConditionDto> conditions
            , List<SortDto> sorts, Boolean isDelete);

    /**
     * 获取分页数据
     *
     * @param conditions
     * @param sorts
     * @param pageIndex
     * @param pageSize
     * @param isDelete
     * @return
     */
    PageData<PrmInterfacePermissionListInfo> getInterfacePageDatas(List<ConditionDto> conditions
            , List<SortDto> sorts, int pageIndex, int pageSize, Boolean isDelete);

    /**
     * 获取APIList
     *
     * @return 返回接口列表
     */
    List<ApiSession> getApiSession();
}