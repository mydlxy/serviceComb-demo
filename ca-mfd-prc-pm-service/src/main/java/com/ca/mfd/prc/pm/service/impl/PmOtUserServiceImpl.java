package com.ca.mfd.prc.pm.service.impl;

import com.ca.mfd.prc.common.caching.LocalCache;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.pm.entity.PmOtUserEntity;
import com.ca.mfd.prc.pm.mapper.IPmOtUserMapper;
import com.ca.mfd.prc.pm.service.IPmOtUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author inkelink
 * @Description: 操作终端可操作用户
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Service
public class PmOtUserServiceImpl extends AbstractCrudServiceImpl<IPmOtUserMapper, PmOtUserEntity> implements IPmOtUserService {

    private final String cacheName = "PRC_PM_OT_USER";
    private final String ADMIN = "admin";
    @Autowired
    private LocalCache localCache;
    @Autowired
    private IdentityHelper identityHelper;

    /**
     * 获取所有的数据
     *
     * @return 所有的数据
     */
    @Override
    public List<PmOtUserEntity> getAllDatas() {
        Function<Object, ? extends List<PmOtUserEntity>> getDataFunc = (obj) -> {
            return getData(new ArrayList<>());
        };
        return localCache.getObject(cacheName, getDataFunc, 60 * 10);
    }

    /**
     * 返回 true 表示改用户在该岗位具备访问权限
     *
     * @param workStationName
     * @param userName
     * @return
     */
    @Override
    public boolean getWorkStationByUser(String workStationName, String userName) {
        if (Constant.SYSTEM_DEFALUT.equals(identityHelper.getUserName())
                || ADMIN.equals(identityHelper.getUserName())) {
            return true;
        }
        List<PmOtUserEntity> allOtUserList = getAllDatas();
        if (allOtUserList != null && !allOtUserList.isEmpty()) {
            List<PmOtUserEntity> filteredOtUserList = allOtUserList.stream().filter(s -> workStationName.equals(s.getStationName())
                    && Boolean.TRUE.equals(s.getIsEnable())).collect(Collectors.toList());
            if (filteredOtUserList.isEmpty()) {
                return true;
            }
            PmOtUserEntity pmWorkPlace = filteredOtUserList.get(0);
            String name = pmWorkPlace.getUserName();
            if (StringUtils.isBlank(name)) {
                return false;
            }
            String newName = name.replaceAll("\\|", ",").replaceAll(";", ",");
            String[] users = StringUtils.split(newName, ',');
            if (users.length == 0) {
                return false;
            }
            return !ArrayUtils.contains(users, userName);

        } else {
            return false;
        }

    }

    @Override
    public void beforeInsert(PmOtUserEntity model) {
        this.valid(model);
    }

    @Override
    public void beforeUpdate(PmOtUserEntity model) {
        this.valid(model);
    }

    public void afterDelete(List<String> ids) {
        this.localCache.removeObject(cacheName);
    }

    public void afterInsert(List<String> ids) {
        this.localCache.removeObject(cacheName);
    }

    public void afterUpdate(List<String> ids) {
        this.localCache.removeObject(cacheName);
    }


    private void valid(PmOtUserEntity model) {
        this.validDataUnique(model.getId(), "PRC_PM_WORK_STATION_ID", String.valueOf(model.getPrcPmWorkStationId()),
                "已经存在岗位为%s的数据", null, null);
    }

}