package com.ca.mfd.prc.core.prm.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.entity.UserData;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.enums.ConditionRelation;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.common.utils.EncryptionUtils;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.MpSqlUtils;
import com.ca.mfd.prc.common.utils.UUIDUtils;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentEntity;
import com.ca.mfd.prc.core.prm.entity.PrmDepartmentJoinUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmRoleEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.entity.PrmUserRoleEntity;
import com.ca.mfd.prc.core.prm.mapper.IPrmDepartmentJoinUserMapper;
import com.ca.mfd.prc.core.prm.mapper.IPrmUserMapper;
import com.ca.mfd.prc.core.prm.service.IPrmDepartmentJoinUserService;
import com.ca.mfd.prc.core.prm.service.IPrmPermissionService;
import com.ca.mfd.prc.core.prm.service.IPrmRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmUserRoleService;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 用户表
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class PrmUserServiceImpl extends AbstractCrudServiceImpl<IPrmUserMapper, PrmUserEntity> implements IPrmUserService {

    private static final String cacheName = "PRC_PRM_USER";
    private static final String allcacheName = "All_PRC_PRM_USER";
    private static final Object lockObj = new Object();
    private final Map<String, String> excelColumnNames = new LinkedHashMap<>();
    @Autowired
    private IdentityHelper identityHelper;
    @Autowired
    private IPrmUserMapper prmUserDao;
    @Autowired
    private IPrmUserRoleService prmUserRoleService;
    @Autowired
    private IPrmRoleService prmRoleService;
    @Autowired
    private IPrmPermissionService prmPermissionService;
    @Autowired
    private IPrmDepartmentJoinUserMapper prmDepartmentJoinUserDao;
    @Autowired
    private IPrmDepartmentJoinUserService prmDepartmentJoinUserService;
    @Autowired
    private ISysConfigurationService sysConfigurationService;
    @Autowired
    private LocalCache localCache;

    {
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getJobNo), "工号");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getUserName), "用户名");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getNickName), "姓名");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getPhone), "联系电话");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getEmail), "邮箱");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getGroupName), "分组");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getRoleName), "角色");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getDepartmentName), "部门");
        excelColumnNames.put(MpSqlUtils.getColumnName(PrmUserEntity::getIdCard), "设备卡号");
    }

    /**
     * 删除缓存的数据
     */
    private void removeCache() {
        localCache.removeObject(cacheName);
        localCache.removeObject(allcacheName);
    }

    @Override
    public void afterDelete(Wrapper<PrmUserEntity> queryWrapper) {
        removeCache();
    }

    @Override
    public void afterDelete(Collection<? extends Serializable> idList) {
        removeCache();
    }

    @Override
    public void afterInsert(PrmUserEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(PrmUserEntity model) {
        removeCache();
    }

    @Override
    public void afterUpdate(Wrapper<PrmUserEntity> updateWrapper) {
        removeCache();
    }

    @Override
    public void beforeInsert(PrmUserEntity model) {
        validData(model);
    }

    @Override
    public void beforeUpdate(PrmUserEntity model) {
        validData(model);
    }

    /**
     * 查询所有用户
     *
     * @return 用户集合
     */
    @Override
    public List<PrmUserEntity> getAllUser() {
        List<PrmUserEntity> datas = localCache.getObject(allcacheName);
        if (datas != null && !datas.isEmpty()) {
            return datas;
        }
        synchronized (lockObj) {
            datas = localCache.getObject(allcacheName);
            if (datas != null && !datas.isEmpty()) {
                return datas;
            }
            datas = getData(null);
            for (PrmUserEntity item : datas) {
                List<Serializable> roles = prmUserDao.getUserRoles(item.getId()).stream().distinct().map(PrmRoleEntity::getId).collect(Collectors.toList());
                item.setRoles(roles == null ? new ArrayList<>() : roles);
                List<PrmDepartmentEntity> lst = prmDepartmentJoinUserDao.getPrmUserByDepartmentId(item.getId());
                PrmDepartmentEntity depModel = lst == null || lst.size() == 0 ? null : lst.stream().findFirst().orElse(null);
                if (depModel != null) {
                    item.setDepartmentId(depModel.getId().toString());
                    item.setDepartmentName(depModel.getDepartmentName());
                    item.setDepartmentCode(depModel.getDepartmentCode());
                }
            }
            localCache.addObject(allcacheName, datas, 60 * 10);
        }

        return datas;
    }

    /**
     * 获取实体
     */
    @Override
    public PrmUserEntity getFirstByPwdUid(String pwd, Serializable userId) {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(PrmUserEntity::getPassword, pwd).eq(PrmUserEntity::getId, userId);
        return getTopDatas(1, qry).stream().findFirst().orElse(null);
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<PrmUserEntity> getListByKey(String keyword) {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> lamdQry = qry.lambda().orderByAsc(PrmUserEntity::getRemark);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(PrmUserEntity::getJobNo, keyword)
                    .or(c -> c.like(PrmUserEntity::getUserName, keyword))
                    .or(c -> c.like(PrmUserEntity::getNickName, keyword))
                    .or(c -> c.like(PrmUserEntity::getRemark, keyword));
        }
        return selectList(qry);
    }

    /**
     * 获取列表
     */
    @Override
    public List<PrmUserEntity> getListIds(List<Serializable> ids) {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        qry.lambda().in(PrmUserEntity::getId, ids);
        return selectList(qry);
    }

    /**
     * 获取列表
     */
    @Override
    public List<PrmUserEntity> getListLa(String quary) {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> qryLambda = qry.lambda();
        qryLambda.ne(PrmUserEntity::getUserName, Constant.SYSTEM_ADMINROLE)
                .ne(PrmUserEntity::getUserName, Constant.SYSTEM_MANAGER);
        if (!StringUtils.isBlank(quary)) {
            qryLambda.and(c -> c.like(PrmUserEntity::getNickName, quary)
                    .or().like(PrmUserEntity::getUserName, quary)
                    .or().like(PrmUserEntity::getJobNo, quary)
            );
        }
        return selectList(qry);
    }

    /**
     * 获取列表
     */
    @Override
    public List<PrmUserEntity> getListLaOrder() {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> qryLambda = qry.lambda();
        qryLambda.ne(PrmUserEntity::getUserName, Constant.SYSTEM_ADMINROLE)
                .ne(PrmUserEntity::getUserName, Constant.SYSTEM_MANAGER)
                .orderByAsc(PrmUserEntity::getJobNo);
        return selectList(qry);
    }

    /**
     * 设置用户密码
     *
     * @param userId   用户外键
     * @param password 用户密码
     */
    @Override
    public void setPassword(Serializable userId, String password) {
        PrmUserEntity userModel = get(userId);
        if (userModel != null && !userModel.getIsDelete()) {
            userModel.setPassword(password);
            userModel.setPassExpireDt(new Date());
        }
        update(userModel);
        saveChange();
    }

    private String getDefaultPass() {
        String defalutPassword = sysConfigurationService.getConfiguration(Constant.DEFALUT_PASSWORD, Constant.USER_PASS_STRATEGY);
        if (StringUtils.isBlank(defalutPassword)) {
            return getFakePassword();
        }
        return defalutPassword;
    }

    private String getFakePassword() {
        char[] fakePassChars = {'1', '2', '3', '!', '@', '#', 'A', 'a'};
        return new String(fakePassChars);
    }

    /**
     * 数据验证
     *
     * @param model 用户
     */
    private void validData(PrmUserEntity model) {
        model.setEnGroupName("");
        if (StringUtils.isBlank(model.getEmail())) {
            model.setEmail(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(model.getPassword())) {
            model.setPassword(EncryptionUtils.md5(getDefaultPass()));
        }
        //ValidDataUnique(model.Id, "CnName", model.CnName, "已经存在用户名为{0}的数据");
    }

    /**
     * 获取用户数据，包括角色
     *
     * @param id 用户外键
     * @return 用户对象
     */
    @Override
    public PrmUserEntity get(Serializable id) {
        PrmUserEntity data = selectById(id);
        if (data != null) {
            if (data.getIsDelete()) {
                return null;
            }
            List<Long> ids = prmUserDao.getUserRoles(id).stream().distinct().map(PrmRoleEntity::getId).collect(Collectors.toList());
            List<Serializable> roles = ids.stream().map(String::valueOf).collect(Collectors.toList());
            data.setRoles(roles == null ? new ArrayList<>() : roles);
            List<PrmDepartmentEntity> lst = prmDepartmentJoinUserDao.getPrmUserByDepartmentId(id);
            PrmDepartmentEntity depModel = lst == null || lst.size() == 0 ? null : lst.stream().findFirst().orElse(null);
            if (depModel != null) {
                data.setDepartmentId(depModel.getId().toString());
                data.setDepartmentName(depModel.getDepartmentName());
                data.setDepartmentCode(depModel.getDepartmentCode());
            }
        }
        return data;
    }

    /**
     * 获取分页
     *
     * @return 角色集合
     */
    @Override
    public List<PrmUserEntity> getData(List<ConditionDto> conditions, List<SortDto> sorts
            , Boolean isDelete) {
        if (conditions == null) {
            conditions = new ArrayList<>();
        }
        conditions.add(new ConditionDto("UserName", Constant.SYSTEM_MANAGER, ConditionOper.Unequal, ConditionRelation.And));
        conditions.add(new ConditionDto("UserName", Constant.SYSTEM_ADMINROLE, ConditionOper.Unequal, ConditionRelation.And));
        conditions.add(new ConditionDto("UserName", Constant.SYSTEM_DEFALUT, ConditionOper.Unequal, ConditionRelation.And));

        List<PrmUserEntity> datas = getData(conditions, sorts, isDelete);
        return datas;
    }

    /**
     * 获取所有用户（非管理员）
     *
     * @return 集合
     */
    @Override
    public List<PrmUserEntity> getAllNomallUsers() {
        QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
        qry.lambda().ne(PrmUserEntity::getId, UUIDUtils.getEmpty())
                .ne(PrmUserEntity::getUserName, Constant.SYSTEM_ADMINROLE)
                .ne(PrmUserEntity::getUserName, Constant.SYSTEM_MANAGER);
        return selectList(qry);
    }

    @Override
    public List<PrmUserEntity> getMesLogin(String pwd, String userName) {
        QueryWrapper<PrmUserEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmUserEntity::getPassword, pwd);
        lambdaQueryWrapper.eq(PrmUserEntity::getIsActive, true);
        lambdaQueryWrapper.lt(PrmUserEntity::getFrozenDt, new Date());
        lambdaQueryWrapper.and(s -> s.eq(PrmUserEntity::getUserName, userName)
                .or().eq(PrmUserEntity::getEmail, userName)
                .or().eq(PrmUserEntity::getPhone, userName)
                .or().eq(PrmUserEntity::getJobNo, userName)
                .or().eq(PrmUserEntity::getIdCard, userName)
        );
        return selectList(queryWrapper);
    }

    @Override
    public PrmUserEntity getPrmUserInfo(String pwd, String userName) {
        QueryWrapper<PrmUserEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmUserEntity::getUserName, userName);
        lambdaQueryWrapper.eq(PrmUserEntity::getPassword, pwd);
        return selectList(queryWrapper).stream().findFirst().orElse(null);
    }

    @Override
    public List<PrmUserEntity> getAuthOpenLogin(String userName) {
        QueryWrapper<PrmUserEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<PrmUserEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(PrmUserEntity::getIsActive, true);
        lambdaQueryWrapper.lt(PrmUserEntity::getFrozenDt, new Date());
        lambdaQueryWrapper.and(s -> s.eq(PrmUserEntity::getUserName, userName)
                .or().eq(PrmUserEntity::getEnName, userName)
                .or().eq(PrmUserEntity::getEmail, userName)
                .or().eq(PrmUserEntity::getPhone, userName)
                .or().eq(PrmUserEntity::getJobNo, userName)
                .or().eq(PrmUserEntity::getIdCard, userName)
        );
        return selectList(queryWrapper);
    }


    @Override
    public void updateEntityFrozenDt(int loginLockTime, String userName) {
        UpdateWrapper<PrmUserEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PrmUserEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        Date date = DateUtils.addDateMinutes(new Date(), loginLockTime * 60);
        lambdaUpdateWrapper.set(PrmUserEntity::getFrozenDt, date);
        lambdaUpdateWrapper.and(s -> s.eq(PrmUserEntity::getUserName, userName)
                .or().eq(PrmUserEntity::getPhone, userName)
                .or().eq(PrmUserEntity::getEmail, userName)
        );
        update(lambdaUpdateWrapper);
    }

    @Override
    public void updateMemberFrozen(String userName, int userType) {
        UpdateWrapper<PrmUserEntity> updateWrapper = new UpdateWrapper<>();
        LambdaUpdateWrapper<PrmUserEntity> lambdaUpdateWrapper = updateWrapper.lambda();
        lambdaUpdateWrapper.set(PrmUserEntity::getIsActive, false);
        lambdaUpdateWrapper.eq(PrmUserEntity::getUserName, userName);
        lambdaUpdateWrapper.eq(PrmUserEntity::getUserType, userType);
        this.update(updateWrapper);
    }

    @Override
    public List<UserData> getUsersByRoleId(List<Serializable> ids) {
        return prmUserDao.getUsersByRoleId(ids);
    }

    /**
     * 在用户缓存中获取一个用户
     *
     * @param id 用户外键
     * @return 用户对象
     */
    @Override
    public PrmUserEntity getCacheById(Serializable id) {
        return getAllUser().stream().filter(c -> Objects.equals(c.getId(), Long.valueOf(id.toString())))
                .findFirst().orElse(null);
    }

    /**
     * 获取角色信息
     *
     * @return 角色集合
     */
    @Override
    public List<PrmRoleEntity> getRoleItem() {
        List<PrmRoleEntity> datas = prmRoleService.getData(null);
        return datas;
    }

    /**
     * 编辑用户角色权限
     *
     * @param roles  角色集合
     * @param userId 用户外键
     */
    @Override
    public void editRoleSave(List<Serializable> roles, Serializable userId) {
        if (roles == null) {
            roles = new ArrayList<>();
        }
        List<ConditionDto> conditions = new ArrayList<>();
        conditions.add(new ConditionDto("PRC_PRM_USER_ID", userId.toString(), ConditionOper.Equal));

        prmUserRoleService.delete(conditions, false);
        for (Serializable id : roles) {
            PrmUserRoleEntity et = new PrmUserRoleEntity();
            et.setPrcPrmRoleId(Long.parseLong(id.toString()));
            et.setPrcPrmUserId(Long.valueOf(userId.toString()));
            prmUserRoleService.insert(et);
        }
    }

    /**
     * 获取用户权限
     *
     * @param userId   用户外键
     * @param userName 用户名
     * @return 权限集合
     */
    @Override
    public List<String> getPermissions(Serializable userId, String userName) {
        if (userId == null || Long.valueOf(userId.toString()) <= 0) {
            return prmPermissionService.getData(null).stream()
                    .map(item -> item.getPermissionCode()).distinct().collect(Collectors.toList());
        }
        if (!StringUtils.equals(userName, Constant.SYSTEM_MANAGER) ||
                !StringUtils.equals(userName, Constant.SYSTEM_ADMINROLE)) {
            List<String> permissions = prmUserDao.getPermissions(userId, new Date());
            return permissions;
        }
        return new ArrayList<>();
    }

    /**
     * 保存用户
     *
     * @param model 用户模型
     */
    @Override
    public void save(PrmUserEntity model) {
        model.setUserName(model == null || model.getUserName() == null ? "" : model.getUserName().trim());
        model.setEmail(model == null || model.getEmail() == null ? "" : model.getEmail().trim());
        model.setPhone(model == null || model.getPhone() == null ? "" : model.getPhone().trim());
        model.setJobNo(model == null || model.getJobNo() == null ? "" : model.getJobNo().trim());
        model.setCnName(model == null || model.getNickName() == null ? "" : model.getNickName());
        model.setCnGroupName(model == null || model.getCnGroupName() == null ? "" : model.getCnGroupName());
        model.setNo(model == null || model.getNo() == null ? "" : model.getNo());
        model.setEnGroupName(model == null || model.getEnGroupName() == null ? "" : model.getEnGroupName());
        model.setEnName(model == null || model.getEnName() == null ? "" : model.getEnName());
        model.setIdCard(model == null || model.getIdCard() == null ? "" : model.getIdCard());
        model.setPassExpireDt(DateUtils.addDateMonths(new Date(), 3));
        if (model.getId() == null || Long.valueOf(model.getId().toString()) <= 0) {
            if (StringUtils.isBlank(model.getNickName())) {
                throw new InkelinkException("姓名不能为空");
            }
            QueryWrapper<PrmUserEntity> qry = new QueryWrapper<>();
            qry.lambda().eq(PrmUserEntity::getUserName, model.getUserName());
            Long username = selectCount(qry);

            if (username > 0) {
                throw new InkelinkException("用户名被占用");
            }
            if (!StringUtils.isBlank(model.getEmail())) {
                QueryWrapper<PrmUserEntity> qryEmail = new QueryWrapper<>();
                qryEmail.lambda().eq(PrmUserEntity::getEmail, model.getEmail());
                Long email = selectCount(qryEmail);
                if (email > 0) {
                    throw new InkelinkException("邮箱被占用");
                }
            }

            if (!StringUtils.isBlank(model.getPhone())) {
                QueryWrapper<PrmUserEntity> qryPhone = new QueryWrapper<>();
                qryPhone.lambda().eq(PrmUserEntity::getPhone, model.getPhone());
                Long mobile = selectCount(qryPhone);
                if (mobile > 0) {
                    throw new InkelinkException("手机被占用");
                }
            }

            if (!StringUtils.isBlank(model.getJobNo())) {
                QueryWrapper<PrmUserEntity> qryCode = new QueryWrapper<>();
                qryCode.lambda().eq(PrmUserEntity::getJobNo, model.getJobNo());
                Long mobile = selectCount(qryCode);
                if (mobile > 0) {
                    throw new InkelinkException("工号被占用");
                }
            }
            if (!StringUtils.isBlank(model.getIdCard())) {
                QueryWrapper<PrmUserEntity> qryIdCard = new QueryWrapper<>();
                qryIdCard.lambda().eq(PrmUserEntity::getIdCard, model.getIdCard());
                Long idCard = selectCount(qryIdCard);
                if (idCard > 0) {
                    throw new InkelinkException("员工卡被占用");
                }
            }
            if (StringUtils.isBlank(model.getPassword())) {
                model.setPassword(EncryptionUtils.md5(getDefaultPass()));
            }
            model.setCreationDate(new Date());
            model.setId(IdGenerator.getId());
            if (model.getDepartmentId() != null && Long.valueOf(model.getDepartmentId()) > 0) {
                PrmDepartmentJoinUserEntity et = new PrmDepartmentJoinUserEntity();
                et.setPrcPrmDepartmentId(Long.parseLong(model.getDepartmentId()));
                et.setPrcPrmUserId(model.getId());
                prmDepartmentJoinUserService.insert(et);
            }
            //var success = await addUser(model);
            addUser(model);
            removeCache();
            //return success;
        } else {
            QueryWrapper<PrmUserEntity> qryuserName = new QueryWrapper<>();
            qryuserName.lambda().eq(PrmUserEntity::getUserName, model.getUserName())
                    .ne(PrmUserEntity::getId, model.getId());
            Long username = selectCount(qryuserName);
            if (username > 0) {
                throw new InkelinkException("用户名被占用");
            }
            if (!StringUtils.isBlank(model.getEmail())) {
                QueryWrapper<PrmUserEntity> qryEmail = new QueryWrapper<>();
                qryEmail.lambda().eq(PrmUserEntity::getEmail, model.getEmail())
                        .ne(PrmUserEntity::getId, model.getId());
                Long email = selectCount(qryEmail);
                if (email > 0) {
                    throw new InkelinkException("邮箱被占用");
                }
            }

            if (!StringUtils.isBlank(model.getPhone())) {
                QueryWrapper<PrmUserEntity> qryEmail = new QueryWrapper<>();
                qryEmail.lambda().eq(PrmUserEntity::getPhone, model.getPhone())
                        .ne(PrmUserEntity::getId, model.getId());
                Long email = selectCount(qryEmail);
                if (email > 0) {
                    throw new InkelinkException("手机被占用");
                }
            }

            if (!StringUtils.isBlank(model.getJobNo())) {
                QueryWrapper<PrmUserEntity> qryEmail = new QueryWrapper<>();
                qryEmail.lambda().eq(PrmUserEntity::getJobNo, model.getJobNo())
                        .ne(PrmUserEntity::getId, model.getId());
                Long email = selectCount(qryEmail);
                if (email > 0) {
                    throw new InkelinkException("工号被占用");
                }
            }
            if (!StringUtils.isBlank(model.getIdCard())) {
                QueryWrapper<PrmUserEntity> qryEmail = new QueryWrapper<>();
                qryEmail.lambda().eq(PrmUserEntity::getIdCard, model.getIdCard())
                        .ne(PrmUserEntity::getId, model.getId());
                Long email = selectCount(qryEmail);
                if (email > 0) {
                    throw new InkelinkException("员工卡被占用");
                }
            }
            ///需要对密码加密
            if (!StringUtils.isBlank(model.getPassword())) {
                model.setPassword(EncryptionUtils.md5(model.getPassword()));
            }
            if (model.getDepartmentId() != null && Long.valueOf(model.getDepartmentId()) > 0) {
                PrmDepartmentJoinUserEntity departjoinUserModel = prmDepartmentJoinUserService.getFirstPrmUserId(model.getId());
                if (departjoinUserModel != null) {
                    UpdateWrapper<PrmDepartmentJoinUserEntity> upset = new UpdateWrapper();
                    upset.lambda().set(PrmDepartmentJoinUserEntity::getPrcPrmDepartmentId, model.getDepartmentId())
                            .eq(PrmDepartmentJoinUserEntity::getId, departjoinUserModel.getId());
                    prmDepartmentJoinUserService.update(upset);
                } else {
                    PrmDepartmentJoinUserEntity et = new PrmDepartmentJoinUserEntity();
                    et.setPrcPrmDepartmentId(Long.valueOf(model.getDepartmentId()));
                    et.setPrcPrmUserId(model.getId());
                    prmDepartmentJoinUserService.insert(et);
                }
            }
            //var success = await UpdateUserAndDetail(model);
            updateUserAndDetail(model);
            removeCache();
            //return success;
        }
    }

    /**
     * 增加一个新用户
     *
     * @param user 用户登录表的 model
     * @return 是否成功
     */
    private Boolean addUser(PrmUserEntity user) {
        Boolean result = true;
        insert(user);
        saveChange();
        editRoleSave(user.getRoles(), user.getId());
        saveChange();
        //await Task.CompletedTask;
        return result;
    }

    /**
     * 更新用户信息和用户的基本信息
     *
     * @param user 用户登录表的
     * @return 是否成功
     */
    private Boolean updateUserAndDetail(PrmUserEntity user) {
        int acl = -1;
        if (StringUtils.isBlank(user.getPassword())) {
            UpdateWrapper<PrmUserEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmUserEntity::getUserName, user.getUserName())
                    .set(PrmUserEntity::getPhone, user.getPhone())
                    .set(PrmUserEntity::getEmail, user.getEmail())
                    .set(PrmUserEntity::getJobNo, user.getJobNo())
                    .set(PrmUserEntity::getIdCard, user.getIdCard())
                    .set(PrmUserEntity::getNickName, user.getNickName())
                    .set(PrmUserEntity::getNo, user.getNo())
                    .set(PrmUserEntity::getCnName, user.getCnName())
                    .set(PrmUserEntity::getGroupName, user.getGroupName())
                    .set(PrmUserEntity::getCnGroupName, user.getCnGroupName())
                    .set(PrmUserEntity::getEnName, user.getEnName())
                    .set(PrmUserEntity::getEnGroupName, user.getEnGroupName())
                    .set(PrmUserEntity::getLastUpdateDate, new Date())
                    .set(PrmUserEntity::getPassExpireDt, new Date())

                    //.set(PrmUserEntity::getUpdateUserId, user.getUpdateUserId())
                    //.set(PrmUserEntity::getPassword, user.getPassword())
                    .set(PrmUserEntity::getIsActive, user.getIsActive())
                    .set(PrmUserEntity::getRemark, user.getRemark())
                    .set(PrmUserEntity::getUserType, user.getUserType())
                    .set(PrmUserEntity::getExpiredDt, user.getExpiredDt())

                    .eq(PrmUserEntity::getId, user.getId());

            acl = update(upset) ? 1 : 0;
        } else {
            UpdateWrapper<PrmUserEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(PrmUserEntity::getUserName, user.getUserName())
                    .set(PrmUserEntity::getPhone, user.getPhone())
                    .set(PrmUserEntity::getEmail, user.getEmail())
                    .set(PrmUserEntity::getJobNo, user.getJobNo())
                    .set(PrmUserEntity::getIdCard, user.getIdCard())
                    .set(PrmUserEntity::getNickName, user.getNickName())
                    .set(PrmUserEntity::getNo, user.getNo())
                    .set(PrmUserEntity::getCnName, user.getCnName())
                    .set(PrmUserEntity::getGroupName, user.getGroupName())
                    .set(PrmUserEntity::getCnGroupName, user.getCnGroupName())
                    .set(PrmUserEntity::getEnName, user.getEnName())
                    .set(PrmUserEntity::getEnGroupName, user.getEnGroupName())
                    .set(PrmUserEntity::getLastUpdateDate, new Date())
                    .set(PrmUserEntity::getPassExpireDt, new Date())

                    //.set(PrmUserEntity::getUpdateUserId, user.getUpdateUserId())
                    .set(PrmUserEntity::getPassword, user.getPassword())
                    .set(PrmUserEntity::getIsActive, user.getIsActive())
                    .set(PrmUserEntity::getRemark, user.getRemark())
                    .set(PrmUserEntity::getUserType, user.getUserType())
                    .set(PrmUserEntity::getExpiredDt, user.getExpiredDt())

                    .eq(PrmUserEntity::getId, user.getId());

            acl = update(upset) ? 1 : 0;
        }

        Boolean result = acl > 0;

        editRoleSave(user.getRoles(), user.getId());
        saveChange();
        //await Task.CompletedTask;
        return result;
    }
}