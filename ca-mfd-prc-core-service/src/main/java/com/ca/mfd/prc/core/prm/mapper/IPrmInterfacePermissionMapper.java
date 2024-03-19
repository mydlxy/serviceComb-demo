package com.ca.mfd.prc.core.prm.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.entity.ApiSession;
import com.ca.mfd.prc.common.mapper.IBaseMapper;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.entity.PrmInterfacePermissionEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 接口权限表
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
@Mapper
public interface IPrmInterfacePermissionMapper extends IBaseMapper<PrmInterfacePermissionEntity> {

    /**
     * 接口权限集合
     *
     * @param pms 参数
     * @return 接口权限集合
     */
    List<PrmInterfacePermissionListInfo> getInterfaceDatas(@Param("pms") Map<String, Object> pms);

    /***
     * 接口权限集合(分页)
     * @param page
     * @param pms
     * @return
     */
    Page<PrmInterfacePermissionListInfo> getInterfacePageDatas(Page<PrmInterfacePermissionListInfo> page, @Param("pms") Map<String, Object> pms);

    /***
     * 获取接口无效的权限ID
     * @return
     */
    List<String> getInterfacePermissionRemoves();

    /***
     *获取所有接口权限表数据
     * @return
     */
    List<ApiSession> getApiSessionData();
}