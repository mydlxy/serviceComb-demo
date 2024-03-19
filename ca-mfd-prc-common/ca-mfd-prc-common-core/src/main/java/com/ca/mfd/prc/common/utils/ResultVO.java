/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.exception.ErrorCode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author inkelink
 * @since 1.0.0
 */
@Schema(description = "响应")
public class ResultVO<T> implements Serializable {
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
    private String message = "success";
    /**
     * 响应数据
     */
    @Schema(title = "响应数据")
    private T data;

    public static <T> ResultVO<T> ok() {
        return new ResultVO().ok(ErrorCode.SUCCESS, "");
    }

    public ResultVO<T> ok(T data) {
        this.setCode(ErrorCode.SUCCESS);
        this.setData(data);
        return this;
    }

    public ResultVO<T> ok(T data, String msg) {
        this.setCode(ErrorCode.SUCCESS);
        this.setData(data);
        this.setMessage(msg);
        return this;
    }

//    public boolean success(){
//        return code == ErrorCode.SUCCESS;
//    }

    public ResultVO<T> error() {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = MessageUtils.getMessage(this.code);
        return this;
    }

    public ResultVO<T> error(int code) {
        this.code = code;
        this.message = MessageUtils.getMessage(this.code);
        return this;
    }

    public ResultVO<T> error(int code, String msg) {
        this.code = code;
        this.message = msg;
        return this;
    }

    public ResultVO<T> error(String msg) {
        this.code = ErrorCode.INTERNAL_SERVER_ERROR;
        this.message = msg;
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
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
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
