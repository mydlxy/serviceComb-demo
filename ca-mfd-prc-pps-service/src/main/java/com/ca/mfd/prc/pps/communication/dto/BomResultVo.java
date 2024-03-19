/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.pps.communication.dto;

import com.ca.mfd.prc.common.exception.ErrorCode;
import com.ca.mfd.prc.common.utils.JsonUtils;
import com.ca.mfd.prc.common.utils.MessageUtils;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * AS响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "AS响应数据")
@Data
public class BomResultVo<T> implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 编码：200表示成功，其他值表示失败
     */
    @Schema(title = "编码：200表示成功，其他值表示失败")
    private int code = 0;
    /**
     * 消息内容
     */
    @Schema(title = "消息内容")
    private String msg = "success";
    /**
     * 响应数据
     */
    @Schema(title = "响应数据")
    private T data;

    public static <T> ResultVO<T> ok() {
        return new ResultVO().ok(ErrorCode.SUCCESS, "");
    }

    public BomResultVo<T> ok(T data) {
        this.setCode(ErrorCode.SUCCESS);
        this.setData(data);
        return this;
    }

    public BomResultVo<T> ok(T data, String msg) {
        this.setCode(ErrorCode.SUCCESS);
        this.setData(data);
        this.setMessage(msg);
        return this;
    }

//    public boolean success(){
//        return code == ErrorCode.SUCCESS;
//    }

    public BomResultVo<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public BomResultVo<T> error(int code) {
        this.code = code;
        this.msg = MessageUtils.getMessage(this.code);
        return this;
    }

    public BomResultVo<T> error(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }

    public BomResultVo<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.msg = msg;
        return this;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public boolean getSuccess() {
        return code == ErrorCode.SUCCESS;
    }

    public String getMessage() {
        return msg;
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtils.toJsonString(this);
    }

}
