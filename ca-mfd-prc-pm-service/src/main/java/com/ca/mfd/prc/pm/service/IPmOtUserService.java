package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pm.entity.PmOtUserEntity;

import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 操作终端可操作用户
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
public interface IPmOtUserService extends ICrudService<PmOtUserEntity> {

    /**
     * 获取所有的数据
     *
     * @return List<PmOtUserEntity>
     */
    List<PmOtUserEntity> getAllDatas();

    /**
     * 返回 true 表示改用户在该岗位具备访问权限
     *
     * @param workStationName
     * @param userName
     * @return
     */
    boolean getWorkStationByUser(String workStationName, String userName);
}