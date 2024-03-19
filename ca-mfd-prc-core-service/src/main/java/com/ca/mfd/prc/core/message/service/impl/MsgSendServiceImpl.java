package com.ca.mfd.prc.core.message.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ca.mfd.prc.common.enums.DistinationType;
import com.ca.mfd.prc.common.enums.MethodType;
import com.ca.mfd.prc.common.exception.InkelinkException;
import com.ca.mfd.prc.common.model.main.ReportQueue;
import com.ca.mfd.prc.common.service.impl.AbstractCrudServiceImpl;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.core.message.dto.DistinationData;
import com.ca.mfd.prc.core.message.dto.MessageContent;
import com.ca.mfd.prc.core.message.entity.MsgPushEntity;
import com.ca.mfd.prc.core.message.entity.MsgSendEntity;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterEntity;
import com.ca.mfd.prc.core.message.entity.MsgTemplaterToEntity;
import com.ca.mfd.prc.core.message.mapper.IMsgSendMapper;
import com.ca.mfd.prc.core.message.service.IMsgPushService;
import com.ca.mfd.prc.core.message.service.IMsgSendService;
import com.ca.mfd.prc.core.message.service.IMsgTemplaterService;
import com.ca.mfd.prc.core.message.service.IMsgTemplaterToService;
import com.ca.mfd.prc.core.message.service.IMsgTypePushQueueService;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.ca.mfd.prc.core.prm.service.IPrmUserService;
import com.ca.mfd.prc.mq.rabbitmq.entity.RabbitMQConstants;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.IRabbitSysQueueNoteService;
import com.ca.mfd.prc.mq.rabbitmq.remote.app.core.entity.SysQueueNoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jay.he
 * @Description: 信息发送记录
 */
@Service
public class MsgSendServiceImpl extends AbstractCrudServiceImpl<IMsgSendMapper, MsgSendEntity> implements IMsgSendService {
    @Autowired
    IMsgPushService msgPushService;
    @Autowired
    IMsgTemplaterService msgTemplaterService;
    @Autowired
    IMsgTemplaterToService msgTemplaterToService;
    @Autowired
    IPrmUserService prmUserService;
    @Lazy
    @Autowired
    IRabbitSysQueueNoteService sysQueueNoteService;//调sysqueuenoteservice，以后应该会直接用service

    @Autowired
    private IdentityHelper identityHelper;

    private final List<IMsgTypePushQueueService> msgTypePushQueueServices;

    //  private final static String AddMessageTopic = "MSG_AddMessage";


    public MsgSendServiceImpl(List<IMsgTypePushQueueService> msgTypePushQueueServices) {
        this.msgTypePushQueueServices = msgTypePushQueueServices;
    }

    @Override
    public void addMessage(MessageContent content) {
        if (!StringUtils.hasText(content.getTplCode()) && !StringUtils.hasText(content.getDistination())) {
            throw new InkelinkException("请指定目标发送用户或模板发送");
        }
        if (StringUtils.hasText(content.getDistination()) && !StringUtils.hasText(content.getMethod())) {
            throw new InkelinkException("请指定推送渠道！");
        }
        if (content.getDistinationType().equals(DistinationType.Address)
                && (content.getDistination().contains(",") || content.getDistination().contains(";"))) {
            throw new InkelinkException("接收类型为地址时，只能选择一种推送渠道！");
        }

        //默认系统参数
        if (content.getParameters() == null) {
            content.setParameters(new HashMap<>());
        }
        String currentUserName = identityHelper.getUserName() + "/" + identityHelper.getLoginName();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!content.getParameters().containsKey("Creater")) {
            content.getParameters().put("Creater", currentUserName);
        }
        if (!content.getParameters().containsKey("CreationDate")) {
            content.getParameters().put("CreationDate", dateFormat.format(new Date()));
        }
        if (!content.getParameters().containsKey("MessageCreateDt")) {
            content.getParameters().put("MessageCreateDt", dateFormat.format(new Date()));
        }

        MsgSendEntity msgSendEntity = new MsgSendEntity();
        msgSendEntity.setMethod(content.getMethod());
        msgSendEntity.setParameters(JsonUtils.toJsonString(content.getParameters()));//Parameters = content.Parameters.ToJsonString("{}")
        msgSendEntity.setDistination(content.getDistination());
        msgSendEntity.setDistinationName(content.getDistinationName());
        msgSendEntity.setDistinationType(content.getDistinationType().code());
        msgSendEntity.setStatus(1);
        msgSendEntity.setSubject(content.getSubject());
        msgSendEntity.setContent(content.getContent());
        msgSendEntity.setTargetId(content.getTargetId());
        msgSendEntity.setTargetType(content.getTargetType().code());
        msgSendEntity.setPushDt(content.getPushDt());
        msgSendEntity.setSource(content.getSource());
        msgSendEntity.setTplCode(content.getTplCode());
        this.insert(msgSendEntity);

        executeChannels(msgSendEntity);

        this.saveChange();
    }

    private void executeChannels(MsgSendEntity content) {
        List<MsgPushEntity> msgPushEntities = splitSendToPush(content);
        Map<String, List<MsgPushEntity>> map = msgPushEntities.stream().collect(Collectors.groupingBy(MsgPushEntity::getMethod));
        Iterator<Map.Entry<String, List<MsgPushEntity>>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<MsgPushEntity>> entry = iterator.next();
            String key = entry.getKey();//method
            MethodType methodType = MethodType.findByCode(Integer.parseInt(key));
            List<IMsgTypePushQueueService> filterMsgTypePushQueueService = msgTypePushQueueServices.stream().filter(c -> c.msgType().equals(methodType.name())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterMsgTypePushQueueService)) {
                filterMsgTypePushQueueService.get(0).push(entry.getValue());
            }
        }
    }

    private List<MsgPushEntity> splitSendToPush(MsgSendEntity model) {
        List<PrmUserEntity> users = prmUserService.getAllUser();

        model.setSendTimes(model.getSendTimes() + 1);
        model.setSendDt(new Date());
        String subjectPush = model.getSubject();
        String contentPush = model.getContent();
        boolean isReplace = false;
        List<DistinationData> result = new ArrayList<>();
        List<MsgPushEntity> datas = new ArrayList<>();
        List<MsgTemplaterToEntity> msgTemplaterToEntities = new ArrayList<>();

        if (StringUtils.hasText(model.getTplCode())) {
            QueryWrapper<MsgTemplaterEntity> queryTemplaterWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<MsgTemplaterEntity> lambdaQueryTemplaterWrapper = queryTemplaterWrapper.lambda();
            lambdaQueryTemplaterWrapper.eq(MsgTemplaterEntity::getCode, model.getTplCode());
            List<MsgTemplaterEntity> msgTemplaterEntities = msgTemplaterService.getData(queryTemplaterWrapper, false);

            if (CollectionUtils.isEmpty(msgTemplaterEntities)) {
                throw new InkelinkException("模板代码" + model.getTplCode() + "在模板库中不存在");
            }
            MsgTemplaterEntity template = msgTemplaterEntities.get(0);

            QueryWrapper<MsgTemplaterToEntity> queryTemplaterToWrapper = new QueryWrapper<>();
            LambdaQueryWrapper<MsgTemplaterToEntity> lambdaQueryTemplaterToWrapper = queryTemplaterToWrapper.lambda();
            lambdaQueryTemplaterToWrapper.eq(MsgTemplaterToEntity::getMessageTemplaterId, template.getId());
            msgTemplaterToEntities = msgTemplaterToService.getData(queryTemplaterToWrapper, false);

            //  HashMap<String, String> parameters = JSONUtil.toBean(model.getParameters(), HashMap.class);
            HashMap<String, String> parameters = JsonUtils.parseObject(model.getParameters(), HashMap.class);
            if (StringUtils.hasText(template.getSubject())) {
                subjectPush = parsePropertyTokens(template.getSubject(), parameters);
                // 标题不能超过 125个汉字，多余的切掉。
                subjectPush = subjectPush.length() > 128 ? subjectPush.substring(0, 128) : subjectPush;
            }
            if (StringUtils.hasText(template.getContent())) {
                contentPush = parsePropertyTokens(template.getContent(), parameters);
                isReplace = true;
            }
        }
        //模板代码不为空，发送地址为空，采用模板代码默认设置发送
        if (StringUtils.hasText(model.getTplCode()) && !StringUtils.hasText(model.getDistination())) {
            if (CollectionUtils.isEmpty(msgTemplaterToEntities)) {
                throw new InkelinkException("模板代码" + model.getTplCode() + "没有配置默认推送对象");
            }
            for (MsgTemplaterToEntity templateTo : msgTemplaterToEntities) {
                String[] splitDistination = templateTo.getDistination().split("[,;]");

                if (templateTo.getDistinationType().equals(DistinationType.Address.code())) {
                    int method = -1;
                    try {
                        MethodType methodType = MethodType.findByCode(Integer.parseInt(templateTo.getMethod()));
                        if (methodType != null) {
                            method = methodType.code();
                        }
                    } catch (Exception ex) {
                        throw new InkelinkException("模板代码：" + model.getTplCode() + "，默认推送记录：" + templateTo.getId() + "，接收类型：" + method + "，和地址：" + DistinationType.Address + "不匹配");
                    }
                    DistinationData distinationData = new DistinationData();
                    distinationData.setMethod(method);
                    distinationData.setDistination(templateTo.getDistination());
                    result.add(distinationData);

                    for (int i = 0; i < splitDistination.length; i++) {
                        MsgPushEntity msgPushEntity = viewMsgPushEntity(model, splitDistination[i], String.valueOf(method), isReplace, subjectPush, contentPush);

                        msgPushService.insert(msgPushEntity);
                        datas.add(msgPushEntity);
                    }

                } else {
                    if (CollectionUtils.isEmpty(users)) {
                        throw new InkelinkException("当前没有用户信息，无法发送消息！");
                    }
                    // 获取用户信息
                    String[] splitMethod = templateTo.getMethod().split("[,;]");
                    HashMap<Integer, String> distinations = new HashMap<>();
                    for (String userId : splitDistination) {
                        List<PrmUserEntity> currentUsers = null;
                        try {
                            currentUsers = users.stream().filter(c -> c.getId().equals(Long.parseLong(userId))).collect(Collectors.toList());
                        } catch (Exception ex) {
                            continue;
                        }
                        if (CollectionUtils.isEmpty(currentUsers)) {
                            continue;
                        }
                        PrmUserEntity user = currentUsers.get(0);

                        for (String methodStr : splitMethod) {
                            Integer method = Integer.parseInt(methodStr);
                            String distination = getUserDistination(methodStr, user);
                            // 推送账号不存在直接忽略
                            if (!StringUtils.hasText(distination)) {
                                continue;
                            }
                            if (!distinations.containsKey(method)) {
                                distinations.put(method, distination);
                            } else {
                                distinations.put(method, distinations.get(model) + "," + distination);
                            }
                        }

                    }
                    Iterator<Map.Entry<Integer, String>> iterator = distinations.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, String> entry = iterator.next();
                        DistinationData distinationData = new DistinationData();
                        distinationData.setMethod(entry.getKey());
                        distinationData.setDistination(entry.getValue());
                        result.add(distinationData);

                        MsgPushEntity msgPushEntity = viewMsgPushEntity(model, entry.getValue(), String.valueOf(entry.getKey()), isReplace, subjectPush, contentPush);

                        msgPushService.insert(msgPushEntity);
                        datas.add(msgPushEntity);
                    }

                }
            }

        }

        //传了目标地址，就发送到目标地址
        if (StringUtils.hasText(model.getDistination())) {
            if (model.getDistinationType().equals(DistinationType.Address.code())) {
                int method = -1;
                try {
                    MethodType methodType = MethodType.findByName(model.getMethod());
                    if (methodType != null) {
                        method = methodType.code();
                    }
                } catch (Exception ex) {
                    throw new InkelinkException("编号：" + model.getId() + "，接收类型：" + method + "，和地址：" + DistinationType.Address + "不匹配");
                }
                DistinationData distinationData = new DistinationData();
                distinationData.setMethod(method);
                distinationData.setDistination(model.getDistination());
                result.add(distinationData);

                MsgPushEntity msgPushEntity = viewMsgPushEntity(model, model.getDistination(), String.valueOf(method), isReplace, subjectPush, contentPush);

                msgPushService.insert(msgPushEntity);
                datas.add(msgPushEntity);

            } else {
                if (CollectionUtils.isEmpty(users)) {
                    throw new InkelinkException("当前没有用户信息，无法发送消息！");
                }
                // 获取用户信息
                String[] splitDistination = model.getDistination().split("[,;]");
                String[] splitMethod = model.getMethod().split("[,;]");
                HashMap<Integer, String> distinations = new HashMap<>();
                for (String userId : splitDistination) {
                    List<PrmUserEntity> currentUsers = null;
                    try {
                        currentUsers = users.stream().filter(c -> c.getId().equals(Long.parseLong(userId))).collect(Collectors.toList());
                    } catch (Exception ex) {
                        continue;
                    }
                    if (CollectionUtils.isEmpty(currentUsers)) {
                        continue;
                    }
                    PrmUserEntity user = currentUsers.get(0);

                    for (String methodStr : splitMethod) {
                        Integer method = Integer.parseInt(methodStr);
                        String distination = getUserDistination(methodStr, user);

                        // 推送账号不存在直接忽略
                        if (!StringUtils.hasText(distination)) {
                            continue;
                        }
                        if (!distinations.containsKey(method)) {
                            distinations.put(method, distination);
                        } else {
                            distinations.put(method, distinations.get(model) + "," + distination);
                        }

                    }

                }
                Iterator<Map.Entry<Integer, String>> iterator = distinations.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<Integer, String> entry = iterator.next();
                    DistinationData distinationData = new DistinationData();
                    distinationData.setMethod(entry.getKey());
                    distinationData.setDistination(entry.getValue());
                    result.add(distinationData);

                    MsgPushEntity msgPushEntity = viewMsgPushEntity(model, entry.getValue(), String.valueOf(entry.getKey()), isReplace, subjectPush, contentPush);

                    msgPushService.insert(msgPushEntity);
                    datas.add(msgPushEntity);
                }
            }
            model.setStatus(2);
           /* if (model.Status != 2)
                model.Status = 3;*/
            // String distinationJson = JSONUtil.toJsonStr(result);
            String distinationJson = JsonUtils.toJsonString(result);
            LambdaUpdateWrapper<MsgSendEntity> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(MsgSendEntity::getDistinationJson, distinationJson);
            updateWrapper.set(MsgSendEntity::getSendTimes, model.getSendTimes());
            updateWrapper.set(MsgSendEntity::getSendDt, model.getSendDt());
            updateWrapper.set(MsgSendEntity::getStatus, model.getStatus());
            updateWrapper.eq(MsgSendEntity::getId, model.getId());
            this.update(updateWrapper);
        }

        msgPushService.saveChange();
        this.saveChange();

        return datas;
    }

    private String getUserDistination(String methodStr, PrmUserEntity user) {
        Integer method = Integer.parseInt(methodStr);
        String distination = "";//从用户对象中获取接收地址 1.EMAIL;2.钉钉;3.短信;4企业微信;99站内信
        switch (method) {
            case 1:
                distination = user.getEmail();
                break;
            case 2:
                distination = user.getPhone() + ";" + user.getEmail();
                break;
            case 3:
                distination = user.getPhone();
                break;
            case 4:
                distination = user.getPhone() + ";" + user.getEmail();
                break;
            case 6:
                distination = user.getPhone();
                break;
            case 9:
                distination = user.getPhone();
                break;
            case 99:
                distination = user.getUserName();
                break;
            default:
                break;
        }
        return distination;
    }

    /**
     * @param model
     * @param distination
     * @param method      发送方式 存的枚举数字 1，2，3
     * @param isReplace
     * @param subjectPush
     * @param contentPush
     * @return
     */
    private MsgPushEntity viewMsgPushEntity(MsgSendEntity model, String distination, String method, boolean isReplace, String subjectPush, String contentPush) {
        MsgPushEntity msgPushEntity = new MsgPushEntity();
        msgPushEntity.setMessageSendId(model.getId());
        msgPushEntity.setStatus(1);
        msgPushEntity.setSubjectDefault(model.getSubject());
        msgPushEntity.setContentDefault(model.getContent());
        msgPushEntity.setDistination(distination);
        msgPushEntity.setMethod(method);
        msgPushEntity.setPushDt(model.getPushDt());
        msgPushEntity.setSource(model.getSource());
        msgPushEntity.setTargetId(model.getTargetId());
        msgPushEntity.setTargetType(model.getTargetType());
        msgPushEntity.setIsReplace(isReplace ? 1 : 0);
        msgPushEntity.setSubjectPush(subjectPush);
        msgPushEntity.setContentPush(contentPush);
        msgPushEntity.setParameters(model.getParameters());
        msgPushEntity.setTplCode(model.getTplCode());

        return msgPushEntity;
    }

    private String parsePropertyTokens(String str, HashMap<String, String> properties) {
        String OPEN = "${";
        String CLOSE = "}";
        if (!StringUtils.hasText(str) || CollectionUtils.isEmpty(properties)) {
            return str;
        }
        String newString = str;
        int start = newString.indexOf(OPEN);
        int end = newString.indexOf(CLOSE);

        while (start > -1 && end > start) {
            String prepend = newString.substring(0, start);
            String append = newString.substring(end + CLOSE.length());

            int index = start + OPEN.length();
            String propName = newString.substring(index, end);
            if (properties.containsKey(propName)) {
                String propValue = properties.get(propName);
                newString = prepend + propValue + append;
            } else {
                newString = prepend + propName + append;
            }
            start = newString.indexOf(OPEN);
            end = newString.indexOf(CLOSE);
        }
        return newString;
    }

    @Override
    public void restMessagePush(List<String> ids) {
        for (String id : ids) {
            MsgPushEntity msgPushEntity = msgPushService.get(id);
            //  String method = msgPushEntity.getMethod();//1.EMAIL;2.钉钉;3.短信
            MethodType methodType = MethodType.findByCode(Integer.parseInt(msgPushEntity.getMethod()));

            List<IMsgTypePushQueueService> filterMsgTypePushQueueService = msgTypePushQueueServices.stream().filter(c -> c.msgType().equals(methodType.name())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(filterMsgTypePushQueueService)) {
                List<MsgPushEntity> msgPushEntities = new ArrayList<>();
                msgPushEntities.add(msgPushEntity);
                filterMsgTypePushQueueService.get(0).push(msgPushEntities);
            }
        }

        LambdaUpdateWrapper<MsgPushEntity> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(MsgPushEntity::getStatus, 4);
        updateWrapper.in(MsgPushEntity::getId, ids);
        // updateWrapper.ne(MsgPushEntity::getStatus, 2);
        msgPushService.update(updateWrapper);
        msgPushService.saveChange();
    }

    @Override
    public void pushSimpleMessage(MessageContent model) {
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_ADD_MESSAGE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(model));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }

    @Override
    public void pushReportMessage(ReportQueue model) {
        SysQueueNoteEntity sysQueueNoteEntity = new SysQueueNoteEntity();
        sysQueueNoteEntity.setGroupName(RabbitMQConstants.GROUP_NAME_REPORT_ADD_QUEUE);
        sysQueueNoteEntity.setContent(JsonUtils.toJsonString(model));
        sysQueueNoteService.addSimpleMessage(sysQueueNoteEntity);
    }
}