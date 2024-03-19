package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.core.main.dto.SysActionOperateLogDTO;
import com.ca.mfd.prc.core.main.entity.SysActionOperateLogEntity;

import java.util.List;

/**
 * 系统请求操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
public interface ISysActionOperateLogService extends ICrudService<SysActionOperateLogEntity> {

    /**
     * 获取所有数据
     *
     * @return
     */
    List<SysActionOperateLogDTO> getAllDatas();

    /**
     * 重新初始化菜单日志记录
     *
     * @return
     */
    void initSysMenuItemsCommand();
}