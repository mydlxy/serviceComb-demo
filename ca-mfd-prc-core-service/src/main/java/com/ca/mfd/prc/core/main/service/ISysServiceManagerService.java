package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.core.main.entity.SysServiceManagerEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 服务管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysServiceManagerService extends ICrudService<SysServiceManagerEntity> {

    /**
     * @param list
     * @return
     * @throws IllegalAccessException
     */
    ResultVO<List<SysServiceManagerEntity>> getResartNeededServerNames(List<TreeNode> list) throws IllegalAccessException;

    /**
     * 获取ids
     *
     * @param ids
     * @return
     */
    List<Long> getIdsByServiceIds(Collection<? extends Serializable> ids);

    /**
     * 获取列表
     *
     * @param id
     * @return
     */
    List<SysServiceManagerEntity> getByServiceId(Long id);

    /**
     * 获取列表
     *
     * @param item
     * @param status
     * @return
     */
    List<SysServiceManagerEntity> getByServiceIdStatus(Long item, Integer status);

    /**
     * 操作
     *
     * @param serviceId
     * @param code
     */
    void oper(Long serviceId, Integer code);

    /**
     * 重启
     *
     * @param item
     */
    void restartService(Long item);
}