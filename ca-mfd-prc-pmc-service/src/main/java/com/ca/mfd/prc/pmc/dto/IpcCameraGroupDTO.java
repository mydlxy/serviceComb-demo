package com.ca.mfd.prc.pmc.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.ca.mfd.prc.common.convert.JsonDeserializeLong;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
@Schema(title = "工控机-摄像头分组结构VO")
public class IpcCameraGroupDTO {


    @Schema(title = "IPC节点列表")
    private List<IpcNode> ipcNodeList;

    public IpcCameraGroupDTO() {
        this.ipcNodeList = new ArrayList<>();
    }


    @Data
    @Schema(title = "IPC节点")
    public static class IpcNode {
        @Schema(title = "主键")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = JsonDeserializeLong.class)
        private Long id;
        @Schema(title = "工控机名称")
        private String name;
        /**
         * 工控机代码
         */
        @Schema(title = "工控机代码")
        private String code;


        /**
         * IP地址
         */
        @Schema(title = "IP地址")
        private String ipAddress;


        /**
         * 端口号
         */
        @Schema(title = "端口号")
        private String port;


        /**
         * 用户名
         */
        @Schema(title = "用户名")
        private String username;


        /**
         * 用户密码
         */
        @Schema(title = "用户密码")
        private String password;

        @Schema(title = "摄像头列表")
        private List<CameraNode> caremaList;
    }

    @Data
    @Schema(title = "摄像头节点")
    public static class CameraNode {
        @Schema(title = "主键")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = JsonDeserializeLong.class)
        private Long id;

        @Schema(title = "工控机ID")
        @JsonSerialize(using = ToStringSerializer.class)
        @JsonDeserialize(using = JsonDeserializeLong.class)
        private Long prcPmcIpcConfigId;
        /**
         * 摄像头名称（工位名称）
         */
        @Schema(title = "摄像头名称（工位名称）")
        @TableField("NAME")
        private String name = StringUtils.EMPTY;


        /**
         * 摄像机通道号
         */
        @Schema(title = "权限标识")
        @TableField("AUTHORITY")
        private String authority;
    }


}
