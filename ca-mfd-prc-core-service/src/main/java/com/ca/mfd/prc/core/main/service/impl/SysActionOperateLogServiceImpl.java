package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ca.mfd.prc.common.redis.RedisUtils;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.main.dto.SysActionOperateLogDTO;
import com.ca.mfd.prc.core.main.entity.SysActionOperateLogEntity;
import com.ca.mfd.prc.core.main.mapper.ISysActionOperateLogMapper;
import com.ca.mfd.prc.core.main.service.ISysActionOperateLogService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统请求操作日志
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysActionOperateLogServiceImpl extends AbstractCrudServiceImpl<ISysActionOperateLogMapper, SysActionOperateLogEntity> implements ISysActionOperateLogService {

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public List<SysActionOperateLogDTO> getAllDatas() {
        List<SysActionOperateLogDTO> dtoList = new ArrayList<>();
        List<SysActionOperateLogEntity> list = selectList(new QueryWrapper<>());
        list.forEach(log -> {
            SysActionOperateLogDTO dto = new SysActionOperateLogDTO();
            BeanUtils.copyProperties(log, dto);
            dtoList.add(dto);
        });
        return dtoList;
    }

    @Override
    public void initSysMenuItemsCommand() {
        List<SysActionOperateLogDTO> dtoList = new ArrayList<>();
        QueryWrapper<SysActionOperateLogEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysActionOperateLogEntity::getIsEnable, true);
        List<SysActionOperateLogEntity> list = selectList(qry);
        list.forEach(log -> {
            SysActionOperateLogDTO dto = new SysActionOperateLogDTO();
            BeanUtils.copyProperties(log, dto);
            dtoList.add(dto);
        });
        String redisKey = "System:SysMenuItemComandModel";
        redisUtils.set(redisKey, dtoList,-1);
    }
}