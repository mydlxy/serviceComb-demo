package com.ca.mfd.prc.core.dc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdGenerator;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.dc.dto.DcDetail;
import com.ca.mfd.prc.core.dc.dto.InitPageDataVO;
import com.ca.mfd.prc.core.prm.entity.DcButtonConfigEntity;
import com.ca.mfd.prc.core.prm.entity.DcFieldConfigEntity;
import com.ca.mfd.prc.core.prm.entity.DcPageConfigEntity;
import com.ca.mfd.prc.core.prm.mapper.IDcPageConfigMapper;
import com.ca.mfd.prc.core.dc.service.IDcButtonConfigService;
import com.ca.mfd.prc.core.dc.service.IDcFieldConfigService;
import com.ca.mfd.prc.core.dc.service.IDcPageConfigService;
import com.ca.mfd.prc.core.main.service.IAuthorizationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 页面配置实现类
 *
 * @author inkelink
 */
@Service
public class DcPageConfigServiceImpl extends AbstractCrudServiceImpl<IDcPageConfigMapper, DcPageConfigEntity> implements IDcPageConfigService {
    @Autowired
    IdentityHelper identityHelper;

    @Autowired
    IAuthorizationService authorizationService;

    @Autowired
    IDcButtonConfigService dcButtonConfigService;

    @Autowired
    IDcFieldConfigService dcFieldConfigService;

    @Autowired
    IDcPageConfigMapper dcPageConfigMapper;

    @Value("${inkelink.gateway.isOpenCaTokenCheck:false}")
    private boolean isOpenCaTokenCheck;

    @Value("${inkelink.gateway.adminUserConfiguration:null}")
    private List<String> adminUserConfiguration;

    @Override
    public void beforeInsert(DcPageConfigEntity model) {
        IdWorker.getId();
        DcPageConfigEntity configInfo = getEntityByPageKey(model.getPageKey(), null);
        if (configInfo != null) {
            throw new InkelinkException("页面标识" + model.getPageKey() + "已被占用");
        }
        if (StringUtils.isBlank(model.getDefaultConditions())) {
            model.setDefaultConditions("[]");
        }

  /*      else {
            try {
                List<ConditionDto> conditionInfos = JsonUtils.parseArray(model.getDefaultConditions(), ConditionDto.class);
            } catch (Exception exception) {
                throw new InkelinkException("默认查询条件JSON字符串不合法");
            }
        }*/

        if (StringUtils.isBlank(model.getDefaultSorts())) {
            model.setDefaultSorts("[]");
        } else {
            try {
                List<SortDto> sortInfos = JsonUtils.parseArray(model.getDefaultSorts(), SortDto.class);
            } catch (Exception exception) {
                throw new InkelinkException("默认排序条件JSON字符串不合法");
            }
        }
    }

    @Override
    public void beforeUpdate(DcPageConfigEntity model) {
        DcPageConfigEntity configInfo = getEntityByPageKey(model.getPageKey(), model.getId());
        if (configInfo != null) {
            throw new InkelinkException("页面标识" + model.getPageKey() + "已被占用");
        }
        if (StringUtils.isBlank(model.getDefaultConditions())) {
            model.setDefaultConditions("[]");
        }
/*        else {
            try {
                List<ConditionDto> conditionInfos = JsonUtils.parseArray(model.getDefaultConditions(), ConditionDto.class);
            } catch (Exception exception) {
                throw new InkelinkException("默认查询条件JSON字符串不合法");
            }
        }*/

        if (StringUtils.isBlank(model.getDefaultSorts())) {
            model.setDefaultSorts("[]");
        } else {
            try {
                List<SortDto> sortInfos = JsonUtils.parseArray(model.getDefaultSorts(), SortDto.class);
            } catch (Exception exception) {
                throw new InkelinkException("默认排序条件JSON字符串不合法");
            }
        }
    }


    @Override
    public InitPageDataVO initPageData(String pageKey) {
        DcPageConfigEntity configInfo = getEntityByPageKey(pageKey, null);
        if (configInfo == null) {
            throw new InkelinkException("该页面没有进行配置，无法访问");
        }
        //是否为超级管理员
//        if (!identityHelper.getUserName().equals(Constant.SYSTEM_MANAGER)) {
//            //判断是否具备访问权限
//            if (StringUtils.isNotBlank(configInfo.getAuthorizationCode())
//                    && !authorizationService.hasPermission(configInfo.getAuthorizationCode())) {
//                throw new InkelinkException("当前登录用户不具备访问权限");
//            }
//        }
        InitPageDataVO dataInfo = new InitPageDataVO();
        dataInfo.setPageInfo(configInfo);
        List<DcButtonConfigEntity> buttonList = dcButtonConfigService.getButtonListByPage(configInfo.getId());
        List<DcButtonConfigEntity> addButtonList = new ArrayList<>();
        for (DcButtonConfigEntity item : buttonList) {
            //公司MOM按钮权限判断
            if (!isOpenCaTokenCheck) {
                if (StringUtils.equals(identityHelper.getUserName(), Constant.SYSTEM_MANAGER)) {
                    addButtonList.add(item);
                } else {
                    //是否为超级管理员
                    if (StringUtils.isBlank(item.getAuthorizationCode()) || authorizationService.hasPermission(item.getAuthorizationCode())) {
                        addButtonList.add(item);
                    }
                }
            } else {
                //统一门户按钮权限判断
               /* OnlineUserDTO userDTO = identityHelper.getLoginUser();
                if (userDTO != null && adminUserConfiguration != null && adminUserConfiguration.contains(userDTO.getLoginName())) {
                    addButtonList.add(item);
                } else {
                    if (userDTO != null && userDTO.getBtnPermission() != null) {
                        if (userDTO.getBtnPermission().stream().anyMatch(b -> b.equals(item.getAuthorizationCode()))) {
                            addButtonList.add(item);
                        }
                    }
                }*/
                addButtonList.add(item);
            }
        }
        if (addButtonList.size() > 0) {
            dataInfo.setButtons(addButtonList);
        }
        List<DcFieldConfigEntity> dcFieldConfigEntityList = dcFieldConfigService.getFieldListByPage(configInfo.getId());
        dataInfo.setFileds(dcFieldConfigEntityList);
        return dataInfo;
    }

    @Override
    public void copy(String pageKey, String pageName, Long sourceId) {
        DcPageConfigEntity configInfo = getEntityByPageKey(pageKey, null);
        if (configInfo != null) {
            throw new InkelinkException("页面标识已经存在");
        }
        DcPageConfigEntity sourceData = this.get(sourceId);
        if (sourceData == null) {
            throw new InkelinkException("没有获取到原表单数据");
        }
        sourceData.setId(IdGenerator.getId());
        sourceData.setPageKey(pageKey);
        sourceData.setPageName(pageName);
        this.save(sourceData);
        //获取按钮数据
        List<DcButtonConfigEntity> sourceButtons = dcButtonConfigService.getButtonListByPage(sourceId);
        for (DcButtonConfigEntity sourceButton : sourceButtons) {
            sourceButton.setPrcDcPageConfigId(sourceData.getId());
            sourceButton.setId(IdGenerator.getId());
            dcButtonConfigService.save(sourceButton);
        }
        //获取字段
        List<DcFieldConfigEntity> sourceFields = dcFieldConfigService.getFieldListByPage(sourceId);
        for (DcFieldConfigEntity sourceField : sourceFields) {
            sourceField.setPrcDcPageConfigId(sourceData.getId());
            sourceField.setId(IdGenerator.getId());
            dcFieldConfigService.save(sourceField);
        }
    }

    @Override
    public List<DcDetail> getDcDetail(String code) {
        return dcPageConfigMapper.getDcDetail(code);
    }

    @Override
    public List<DcPageConfigEntity> getListByCode(String code) {
        QueryWrapper<DcPageConfigEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(DcPageConfigEntity::getAuthorizationCode, code);
        return selectList(qry);
    }

    private DcPageConfigEntity getEntityByPageKey(String pageKey, Long id) {
        QueryWrapper<DcPageConfigEntity> queryWrapper = new QueryWrapper<>();
        LambdaQueryWrapper<DcPageConfigEntity> lambdaQueryWrapper = queryWrapper.lambda();
        lambdaQueryWrapper.eq(DcPageConfigEntity::getPageKey, pageKey);
        if (id != null && id > 0) {
            lambdaQueryWrapper.ne(DcPageConfigEntity::getId, id);
        }
        return getTopDatas(1, queryWrapper).stream().findFirst().orElse(null);
    }

    /**
     * 获取查询数据
     *
     * @param keyword 查询值
     * @return 数据
     */
    @Override
    public List<DcPageConfigEntity> getListByKey(String keyword) {
        QueryWrapper<DcPageConfigEntity> qry = new QueryWrapper<>();
        LambdaQueryWrapper<DcPageConfigEntity> lamdQry = qry.lambda().orderByAsc(DcPageConfigEntity::getPageName);
        if (!StringUtils.isBlank(keyword)) {
            lamdQry.like(DcPageConfigEntity::getPageKey, keyword)
                    .or(c -> c.like(DcPageConfigEntity::getPageName, keyword));
        }
        return selectList(qry);
    }
}

