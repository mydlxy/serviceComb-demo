package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.page.PageData;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.core.prm.dto.PrmInterfacePermissionListInfo;
import com.ca.mfd.prc.core.prm.dto.PrmJoinUserDto;
import com.ca.mfd.prc.core.prm.dto.PrmUserjoinDepartDto;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmDepartmentJoinUserMapper;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentJoinUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门关联员工表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmDepartmentJoinUserServiceImpl extends AbstractCrudServiceImpl<IPrmDepartmentJoinUserMapper, PrmDepartmentJoinUserEntity> implements IPrmDepartmentJoinUserService {

    @Autowired
    private IPrmDepartmentJoinUserMapper prmDepartmentJoinUserDao;

    /**
     * 获取单挑数据
     */
    @Override
    public PrmDepartmentJoinUserEntity getFirstPrmUserId(Serializable userId) {
        QueryWrapper<PrmDepartmentJoinUserEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmUserId, userId);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取数据
     *
     * @param saveDepartJoinUser
     * @return
     */
    @Override
    public List<Serializable> getOldUserIds(PrmJoinUserDto saveDepartJoinUser) {
        QueryWrapper<PrmDepartmentJoinUserEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, saveDepartJoinUser.getDeparId())
                .in(PrmDepartmentJoinUserEntity::getPrcPrmUserId, saveDepartJoinUser.getUserId())
                .select(PrmDepartmentJoinUserEntity::getPrcPrmUserId);
        return selectList(qry).stream().map(PrmDepartmentJoinUserEntity::getPrcPrmUserId)
                .collect(Collectors.toList());
    }

    /**
     * 删除
     *
     * @param deptId
     * @param prmUserId
     */
    @Override
    public void deleteByDeptUserId(Serializable deptId, Serializable prmUserId) {
        UpdateWrapper<PrmDepartmentJoinUserEntity> upset = new UpdateWrapper<>();
        upset.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, deptId)
                .eq(PrmDepartmentJoinUserEntity::getPrcPrmUserId, prmUserId);
        delete(upset);
    }


    /**
     * 获取分页数据
     *
     * @param conditions
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @Override
    public PageData<PrmUserjoinDepartDto> getUserPageDatas(List<ConditionDto> conditions, int pageIndex, int pageSize) {
        Map<String, Object> map = new HashMap<>();
        ConditionDto conditionDto=conditions.stream().filter(s -> "prcPrmDepartmentId".equalsIgnoreCase(s.getColumnName())).findFirst().orElse(null);
        ConditionDto userconditionInfo=conditions.stream().filter(s -> "UserName".equalsIgnoreCase(s.getColumnName())).findFirst().orElse(null);
        map.put("prcPrmDepartmentId", conditionDto.getValue());
        if (userconditionInfo != null && StringUtils.isNotBlank(userconditionInfo.getValue())) {
            map.put("userName", userconditionInfo.getValue());
        }else{
            map.put("userName", null);
        }
        PageData<PrmUserjoinDepartDto> page = new PageData<>();
        page.setPageIndex(pageIndex <= 0 ? 1 : pageIndex);
        page.setPageSize(pageSize);
        Page<PrmInterfacePermissionListInfo> mpage = new Page<>(pageIndex, pageSize);

        Page<PrmUserjoinDepartDto> pdata = prmDepartmentJoinUserDao.getUserPageDatas(mpage, map);
        page.setTotal((int) pdata.getTotal());
        page.setDatas(pdata.getRecords());
        return page;
    }

    /**
     * 返回部门用户
     *
     * @param ids
     * @return
     */
    @Override
    public List<UserData> getPrmDepartmentJoinUser(List<Serializable> ids) {

        return prmDepartmentJoinUserDao.getPrmDepartmentJoinUser(ids);
    }

    /**
     * 通过用户id返回部门信息
     *
     * @param id
     * @return
     */
    @Override
    public PrmDepartmentEntity getPrmDepartmentByUserId(Serializable id) {
        return prmDepartmentJoinUserDao.getPrmUserByDepartmentId(id).stream().findFirst().orElse(null);
    }

    @Override
    public void afterInsert(PrmDepartmentJoinUserEntity model) {
        QueryWrapper<PrmDepartmentJoinUserEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, model.getPrcPrmDepartmentId())
                .eq(PrmDepartmentJoinUserEntity::getPrcPrmUserId, model.getPrcPrmUserId())
                .select(PrmDepartmentJoinUserEntity::getId);
        List<Serializable> dModel = selectList(qry).stream().map(PrmDepartmentJoinUserEntity::getId).collect(Collectors.toList());
        if (dModel != null && dModel.size() > 0) {
            delete(dModel.toArray(new Serializable[0]));
        }
    }

    /**
     * 保存用户信息
     *
     * @param prmJoinUserDto
     */
    @Override
    public void save(PrmJoinUserDto prmJoinUserDto) {
        if (prmJoinUserDto.getUserId() == null) {
            prmJoinUserDto.setUserId(new ArrayList<>());
        }
        QueryWrapper<PrmDepartmentJoinUserEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, prmJoinUserDto.getDeparId())
                .select(PrmDepartmentJoinUserEntity::getPrcPrmUserId);
        List<Serializable> allUser = selectList(qry).stream().map(PrmDepartmentJoinUserEntity::getPrcPrmUserId).collect(Collectors.toList());
        ///获取新增的用户
        List<Serializable> newUser = allUser.stream().filter(e -> !prmJoinUserDto.getUserId().contains(e))
                .collect(Collectors.toList());
        //需要删除的用户
        List<Serializable> delUser = prmJoinUserDto.getUserId().stream().filter(e -> !allUser.contains(e))
                .collect(Collectors.toList());
        for (Serializable item : newUser) {
            PrmDepartmentJoinUserEntity et = new PrmDepartmentJoinUserEntity();
            et.setPrcPrmUserId(Long.valueOf(item.toString()));
            et.setPrcPrmDepartmentId(Long.valueOf(prmJoinUserDto.getDeparId().toString()));
            insert(et);
        }
        QueryWrapper<PrmDepartmentJoinUserEntity> qryDelete = new QueryWrapper<>();
        qryDelete.lambda().eq(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, prmJoinUserDto.getDeparId())
                .in(PrmDepartmentJoinUserEntity::getPrcPrmUserId, delUser)
                .select(PrmDepartmentJoinUserEntity::getId);
        List<Serializable> alldeleteUser = selectList(qry).stream().map(PrmDepartmentJoinUserEntity::getId).collect(Collectors.toList());
        delete(alldeleteUser.toArray(new Serializable[0]));

    }
}