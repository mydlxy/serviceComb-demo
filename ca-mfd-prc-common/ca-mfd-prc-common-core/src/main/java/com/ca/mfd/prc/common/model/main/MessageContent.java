package com.ca.mfd.prc.common.model.main;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.enums.DistinationType;
import com.ca.mfd.prc.common.enums.MessageTargetType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * MessageContent class
 *
 * @author inkelink
 * @date 2023/04/03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "消息内容")
public class MessageContent {

    /**
     * 模板代码，当目标地址为空字符串的时候，按模板设置的默认用户发送
     */
    @Schema(title = "模板代码")
    private String tplCode;
    /**
     * 接收对象,多个对象用 ','分割,接收对象必须是相同类型的目标地址，不能混用
     */
    @Schema(title = "接收对象")
    private String distination;
    /**
     * 接收对象为用户是存储用户名
     */
    @Schema(title = "接收对象为用户是存储用户名")
    private String distinationName;
    /**
     * 接收类型(1、地址2、用户外键)
     */
    @Schema(title = "接收类型")
    private DistinationType distinationType;
    /**
     * 消息推送方式，当DistinationType为2时，用户外键，可以指定多种推送方式否则只能一种
     */
    @Schema(title = "消息推送方式")
    private String method;
    /**
     * 目标外键
     */
    @Schema(title = "目标外键")
    private String targetId;
    /**
     * 目标类型
     */
    @Schema(title = "目标类型")
    private MessageTargetType targetType;
    /**
     * 默认主题,如果配置有模板，该主题将被模板替换
     */
    @Schema(title = "默认主题")
    private String subject;
    /**
     * 默认内容,如果配置有模板，该内容将被模板替换
     */
    @Schema(title = "默认内容")
    private String content;
    /**
     * 推送时间
     */
    @Schema(title = "推送时间")
    private Date pushDt;
    /**
     * 发送方
     */
    @Schema(title = "发送方")
    private String source;
    /**
     * 传递给模板的参数字典
     */
    @Schema(title = "传递给模板的参数字典")
    private Map<String, String> parameters;

    public MessageContent() {
        this.tplCode = "";

        this.distination = "";
        this.distinationName = "";
        this.distinationType = DistinationType.Address;
        this.method = "";
        this.targetId = Constant.EMPTY_ID;
        this.subject = "";
        this.content = "";
        this.pushDt = new Date();
        this.source = "MES系统通知";
        this.parameters = new HashMap<>();
    }

}
