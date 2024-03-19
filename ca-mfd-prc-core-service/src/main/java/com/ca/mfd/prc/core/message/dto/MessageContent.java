package com.ca.mfd.prc.core.message.dto;

import com.ca.mfd.prc.common.enums.DistinationType;
import com.ca.mfd.prc.common.enums.MessageTargetType;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
public class MessageContent {

    /**
     * 模板代码，当目标地址为空字符串的时候，按模板设置的默认用户发送
     */
    private String tplCode = "";

    /**
     * 接收对象,多个对象用 ','分割,接收对象必须是相同类型的目标地址，不能混用
     */
    private String distination = "";

    /**
     * 接收对象为用户是存储用户名
     */
    private String distinationName = "";

    /**
     * 接收类型(1、地址2、用户外键)
     */
    private DistinationType distinationType = DistinationType.Address;

    /**
     * 消息推送方式，当DistinationType为2时，用户外键，可以指定多种推送方式否则只能一种
     * method为MethodType枚举的名称，比如Email，DingDing
     */
    private String method = "";

    /**
     * 目标外键
     */
    private long targetId = 0;

    /**
     * 目标类型
     */
    private MessageTargetType targetType = MessageTargetType.UnKnown;

    /**
     * 默认主题,如果配置有模板，该主题将被模板替换
     */
    private String subject = "";

    /**
     * 默认内容,如果配置有模板，该内容将被模板替换
     */
    private String content = "";

    /**
     * 推送时间
     */
    private Date pushDt = new Date();

    /**
     * 发送方
     */
    private String source = "MES系统通知";

    /**
     * 传递给模板的参数字典
     */
    private HashMap<String, String> parameters = new HashMap<>();
    //  private Dictionary<String, String> parameters = new Hashtable<>();
}
