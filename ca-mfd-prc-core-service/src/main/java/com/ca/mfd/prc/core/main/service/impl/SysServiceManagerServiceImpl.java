package com.ca.mfd.prc.core.main.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.ca.mfd.prc.common.enums.ConditionOper;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.common.utils.SpringContextUtils;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.core.prm.entity.ChangeDataInfoEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceHostManagerEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceManagerEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceOperLogEntity;
import com.ca.mfd.prc.core.main.entity.SysServiceProcessEntity;
import com.ca.mfd.prc.core.main.mapper.ISysServiceManagerMapper;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import com.ca.mfd.prc.core.main.service.ISysServiceHostManagerBaseService;
import com.ca.mfd.prc.core.main.service.ISysServiceManagerService;
import com.ca.mfd.prc.core.main.service.ISysServiceOperLogService;
import com.ca.mfd.prc.core.main.service.ISysServiceProcessService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 服务管理
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@Service
public class SysServiceManagerServiceImpl extends AbstractCrudServiceImpl<ISysServiceManagerMapper, SysServiceManagerEntity> implements ISysServiceManagerService {

    private static final Logger logger = LoggerFactory.getLogger(SysServiceManagerServiceImpl.class);
    private static final String EnableShopVersioServiceList = "EnableShopVersioServiceList";
    @Autowired
    private ISysServiceManagerMapper managerMapper;
    @Autowired
    private ISysServiceHostManagerBaseService sysServiceHostManagerBaseService;
    @Autowired
    private ISysServiceProcessService sysServiceProcessService;
    @Autowired
    private IdentityHelper identityHelper;
    @Resource
    private TaskExecutor task;
    @Autowired
    private ISysConfigurationService configurationService;

    private void setProperty(List<String> typeProperty, Field[] fields, String type) throws IllegalAccessException {
        for (Field field : fields) {
            if (StringUtils.equals(type, field.getName())) {
                typeProperty.add(field.get(field.getName()).toString());
            }
        }
    }

    @Override
    public ResultVO<List<SysServiceManagerEntity>> getResartNeededServerNames(List<TreeNode> treeNodeInfos) {
        try {
            LambdaQueryWrapper<SysServiceManagerEntity> wr = new LambdaQueryWrapper<>();
            wr.eq(SysServiceManagerEntity::getIsDelete, false);
            List<SysServiceManagerEntity> sysServiceManagerInfos = managerMapper.selectList(wr);

            List<SysServiceManagerEntity> sysServiceManagerInfosForRetart = new ArrayList<>();

            List<TreeNode> outTreeNodeInfos = new ArrayList<>();
            List<ChangeDataInfoEntity> changeDataInfos = new ArrayList<>();

            for (TreeNode treeNodeInfo : treeNodeInfos) {
                analyzeNodeDifference(treeNodeInfo, outTreeNodeInfos);
            }

            if (outTreeNodeInfos.size() > 0) {
                for (TreeNode outTreeNodeInfo : outTreeNodeInfos) {

                    Object extendData = outTreeNodeInfo.getExtendData();
                    Class<?> aClass = extendData.getClass();
                    Field[] fields = aClass.getFields();
                    List<String> operProperty = new ArrayList<>();

                    for (Field field : fields) {
                        if ("Oper".equals(field.getName())) {
                            operProperty.add(field.get(field.getName()).toString());
                        }
                    }

                    List<String> typeProperty = new ArrayList<>();
                    for (Field field : fields) {
                        if ("Type".equals(field.getName())) {
                            typeProperty.add(field.get(field.getName()).toString());
                        }
                    }

                    Optional<String> firstOperOptional = operProperty.stream().findFirst();
                    Optional<String> firstTypeOptional = typeProperty.stream().findFirst();

                    ChangeDataInfoEntity dataInfoEntity = new ChangeDataInfoEntity();
                    firstOperOptional.ifPresent(dataInfoEntity::setOper);
                    firstTypeOptional.ifPresent(dataInfoEntity::setType);
                    changeDataInfos.add(dataInfoEntity);
                }
            }

            //根据修改的数据判断需要重启哪些服务
            List<String> changeDataArea = changeDataInfos.stream().filter(t -> !"none".equals(t.getOper())).map(t -> t.getType()).collect(Collectors.toList());

            for (String item : changeDataArea) {
                switch (item) {
                    case "PmUat":
                        String pmUatServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(pmUatServices)) {

                            String join = String.join("|", pmUatServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmArea":
                        String pmAreaServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(pmAreaServices)) {
                            String join = String.join("|", pmAreaServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmStation":

                        String pmStationServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(pmStationServices)) {
                            String join = String.join("|", pmStationServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmWorkplace":

                        String pmWorkplaceServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(pmWorkplaceServices)) {
                            String join = String.join("|", pmWorkplaceServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmAvi":
                        String bpmAviServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(bpmAviServices)) {
                            String join = String.join("|", bpmAviServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmOt":
                        String bpmOtServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(bpmOtServices)) {
                            String join = String.join("|", bpmOtServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmTool":

                        String bpmToolServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(bpmToolServices)) {

                            String join = String.join("|", bpmToolServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmPullcord":

                        String bpmPullcordServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(bpmPullcordServices)) {
                            String join = String.join("|", bpmPullcordServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;

                    case "PmWo":

                        String bpmPmWoServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(bpmPmWoServices)) {
                            String join = String.join("|", bpmPmWoServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    case "PmToolJob":

                        String pmToolJobServices = configurationService.getConfiguration(item, EnableShopVersioServiceList);
                        if (StringUtils.isNotEmpty(pmToolJobServices)) {
                            String join = String.join("|", pmToolJobServices.split(","));
                            List<ConditionDto> dtos = new ArrayList<>();
                            dtos.add(new ConditionDto("NAME", join, ConditionOper.In));
                            sysServiceManagerInfosForRetart = this.getData(dtos);
                        }
                        break;
                    default:
                        break;
                }

            }
            List<SysServiceManagerEntity> collect = sysServiceManagerInfosForRetart.stream().distinct().collect(Collectors.toList());
            return new ResultVO<List<SysServiceManagerEntity>>().ok(collect);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultVO<List<SysServiceManagerEntity>>().error();
        }
    }

    private void analyzeNodeDifference(TreeNode treeNodeInfo, List<TreeNode> outTreeNodeInfos) {
        if (treeNodeInfo.getChildren().size() > 0) {
            List<TreeNode> children = treeNodeInfo.getChildren();
            for (TreeNode childTreeNodeInfo : children) {
                outTreeNodeInfos.add(childTreeNodeInfo);
                if (childTreeNodeInfo.getChildren().size() > 0) {
                    analyzeNodeDifference(childTreeNodeInfo, outTreeNodeInfos);
                }
            }
        }
    }

    @Override
    public void beforeDelete(Collection<? extends Serializable> idList) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().in(SysServiceManagerEntity::getId, idList)
                .ne(SysServiceManagerEntity::getStatus, 0);
        Long count = selectCount(qry);
        if (count > 0) {
            throw new InkelinkException("包含非未安装的服务，不能删除");
        }
    }

    @Override
    public void beforeInsert(SysServiceManagerEntity entity) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysServiceManagerEntity::getIp, entity.getIp())
                .eq(SysServiceManagerEntity::getName, entity.getName());
        Long count = selectCount(qry);
        if (count > 0) {
            throw new InkelinkException("已存在ip为：" + entity.getIp() + "，服务名为：" + entity.getName() + "的服务");
        }
        ///初始化添加的时候手动创建2个文件
        //var message = model.ToJsonString();
        // var redis = (IRemoteCache)IOCContainer.ServiceProvider.GetService(typeof(IRemoteCache));
        //var redis = IOCContainer.Current.Resolve<IRedisPubSub>();
        // var sub = redis._conn.GetSubscriber();
        //redis.Publish(nameof(SysServiceManagerInfo), message);
        entity.setStatus(0);
    }

    @Override
    public void beforeUpdate(SysServiceManagerEntity entity) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysServiceManagerEntity::getIp, entity.getIp())
                .eq(SysServiceManagerEntity::getName, entity.getName())
                .ne(SysServiceManagerEntity::getId, entity.getId());
        Long count = selectCount(qry);
        if (count > 0) {
            throw new InkelinkException("已存在ip为：" + entity.getIp() + "，服务名为：" + entity.getName() + "的服务");
        }

        SysServiceManagerEntity oldModel = this.get(entity.getId());
        if (entity.getStatus() != 0) {
            if (!StringUtils.equals(oldModel.getName(), entity.getName())) {
                throw new InkelinkException("已安装的服务不能修改服务名");
            }
        }
    }

    @Override
    public List<Long> getIdsByServiceIds(Collection<? extends Serializable> ids) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().select(SysServiceManagerEntity::getId)
                .in(SysServiceManagerEntity::getServiceId, ids);
        return selectList(qry).stream().map(SysServiceManagerEntity::getId).collect(Collectors.toList());
    }

    @Override
    public List<SysServiceManagerEntity> getByServiceId(Long id) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysServiceManagerEntity::getServiceId, id);
        return selectList(qry);
    }


    @Override
    public List<SysServiceManagerEntity> getByServiceIdStatus(Long item, Integer status) {
        QueryWrapper<SysServiceManagerEntity> qry = new QueryWrapper<>();
        qry.lambda().eq(SysServiceManagerEntity::getId, item)
                .eq(SysServiceManagerEntity::getStatus, status);
        return selectList(qry);
    }


    /**
     * 操作
     *
     * @param serviceId
     * @param code
     */
    @Override
    public void oper(Long serviceId, Integer code) {
        SysServiceManagerEntity service = get(serviceId);
        SysServiceHostManagerEntity hostService = sysServiceHostManagerBaseService.get(service.getServiceId());
        if (service == null) {
            throw new InkelinkException("没有找到对应的服务配置");
        }
        if (hostService.getServiceHostType() == 0) {
            if (service.getStatus() != service.getTargetStatus()) {
                throw new InkelinkException(
                        "该服务已经有待处理操作，无法执行。请稍后重试=》" + service.getName() + "当前服务状态" + service.getStatus() + "待处理状态" + service.getTargetStatus());
            }
        }
        switch (code) {
            case 1:
                if (service.getTargetStatus() != 0) {
                    throw new InkelinkException(service.getName() + "该服务不能重复安装");
                }

                service.setTargetStatus(1);
                break;
            case 2:
                if (service.getTargetStatus() == 0) {
                    throw new InkelinkException(service.getName() + "该服务未安装");
                }

                service.setTargetStatus(1);
                break;
            case 3:
                if (service.getTargetStatus() == 0) {
                    throw new InkelinkException(service.getName() + "该服务未安装");
                }

                service.setTargetStatus(4);
                break;
            case 4:
                if (service.getTargetStatus() == 0) {
                    throw new InkelinkException(service.getName() + "该服务未安装");
                }

                service.setTargetStatus(0);
                break;
        }

        update(service);
        //插入执行日志
        SysServiceProcessEntity serPro = new SysServiceProcessEntity();
        serPro.setPrcSysServiceManagerId(serviceId);
        serPro.setOper(code);
        serPro.setStatus(0);
        sysServiceProcessService.insert(serPro);
    }


    /**
     * 重启
     *
     * @param item
     */
    @Override
    public void restartService(Long item) {

        Long userId = identityHelper.getUserId();
        String userName = identityHelper.getUserName();
        String loginName = identityHelper.getLoginName();
        task.execute(() -> {
            resetFirstFunc(item, userId, userName, loginName);
            try {
                resetSecFunc(item, userId, userName, loginName);
            } catch (Exception e) {
                logger.error("", e);
            }

        });

    }

    public List<SysServiceOperLogEntity> resetFirstFunc(Long item, Long userId, String userName, String loginName) {
        List<SysServiceOperLogEntity> modelList = new ArrayList<>();

        UpdateWrapper<SysServiceManagerEntity> upset = new UpdateWrapper<>();
        upset.lambda().set(SysServiceManagerEntity::getTargetStatus, 1)
                .eq(SysServiceManagerEntity::getId, item);
        update(upset);

        SysServiceOperLogEntity logModel = new SysServiceOperLogEntity();
        logModel.setPrcSysServiceManagerId(item);
        logModel.setRemark("建模版本相关暂停相关工厂建模服务");
        logModel.setOperDt(new Date());
        logModel.setOperUserId(userId);
        logModel.setOperUserName(userName + "/" + loginName);

        modelList.add(logModel);
        ISysServiceOperLogService sysServiceOperLogServiceTh = SpringContextUtils.getBean(ISysServiceOperLogService.class);
        if (modelList.size() > 0) {
            sysServiceOperLogServiceTh.insertBatch(modelList);
        }
        sysServiceOperLogServiceTh.saveChange();
        return modelList;
    }


    public void resetSecFunc(Long item, Long userId, String userName, String loginName) throws InterruptedException {
        List<SysServiceOperLogEntity> modelList = new ArrayList<>();
        Thread.sleep(5 * 1000L);
        int num = 1;
        ISysServiceOperLogService sysServiceOperLogServiceTh = SpringContextUtils.getBean(ISysServiceOperLogService.class);
        ISysServiceManagerService sysServiceManagerServiceTh = SpringContextUtils.getBean(ISysServiceManagerService.class);
        for (int i = 0; i < 3; i++) {
            List<SysServiceManagerEntity> model = sysServiceManagerServiceTh.getByServiceIdStatus(item, 1);
            if (model != null) {
                num = num - 1;
            } else {
                continue;
            }

            UpdateWrapper<SysServiceManagerEntity> upset = new UpdateWrapper<>();
            upset.lambda().set(SysServiceManagerEntity::getTargetStatus, 4)
                    .eq(SysServiceManagerEntity::getId, item);

            sysServiceManagerServiceTh.update(upset);
            SysServiceOperLogEntity logModel = new SysServiceOperLogEntity();
            logModel.setPrcSysServiceManagerId(item);
            logModel.setRemark("建模版本相关启用相关工厂建模服务");
            logModel.setOperDt(new Date());
            logModel.setOperUserId(userId);
            logModel.setOperUserName(userName + "/" + loginName);

            modelList.add(logModel);

            if (modelList.size() > 0) {
                sysServiceOperLogServiceTh.insertBatch(modelList);
            }
            sysServiceOperLogServiceTh.saveChange();

            if (num == 0) {
                break;
            }
            Thread.sleep(1000L * 10);
        }
    }
}