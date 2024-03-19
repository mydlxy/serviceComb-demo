package com.ca.mfd.prc.core.prm.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDate;
import com.ca.mfd.prc.common.utils.DateUtils;
import com.ca.mfd.prc.core.prm.entity.PrmUserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * @author eric.zhou
 */
public class UserDTO {

    /**
     * @author eric.zhou
     */
    @Schema(title = "BatchUserEdit", description = "BatchUserEdit")
    @Data
    public static class BatchUserEdit implements Serializable {
        private static final long serialVersionUID = 1L;

        @Schema(description = "ids")
        private List<Serializable> ids;

        @Schema(description = "密码")
        private String password;
    }

    /**
     * @author eric.zhou
     */
    @Schema(title = "UpdatePassword", description = "用户临时权限")
    @Data
    public static class SaveModel extends BasicServiceModel<PrmUserEntity> {
        private static final long serialVersionUID = 1L;
        @Schema(description = "账户主键")
        private Serializable id = Constant.DEFAULT_ID;
        @Schema(description = "角色")
        private List<Serializable> roles;
        @Schema(description = "导入的时候临时使用")
        private String importRolesStr;
        @Schema(description = "用户名")
        private String userName;
        @Schema(description = "英文名")
        private String enName;
        @Schema(description = "中文名")
        private String cnName;
        @Schema(description = "昵称")
        private String nickName;
        @Schema(description = "邮箱")
        private String email;
        @Schema(description = "手机号")
        private String phone;
        @Schema(description = "工号")
        private String jobNo;
        @Schema(description = "扩展编号")
        private String no;
        @Schema(description = "组中文名称")
        private String cnGroupName;
        @Schema(description = "组英文名称")
        private String enGroupName;
        @Schema(description = "组名称")
        private String groupName;
        @Schema(description = "身份证")
        private String idCard;
        @Schema(description = "是否激活")
        private Boolean isActive;
        @Schema(description = "备注")
        private String remark;
        @Schema(description = "用户类型")
        private Integer userType;
        @Schema(description = "过期时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
        @JsonDeserialize(using = JsonDeserializeDate.class)
        private Date passExpireDt;
        @Schema(description = "过期时间")
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING, timezone = "GMT+8")
        @JsonDeserialize(using = JsonDeserializeDate.class)
        private Date expiredDt;
        @Schema(description = "部门ID")
        private Serializable departmentId = Constant.DEFAULT_ID;

        public SaveModel() {
            setService(new PrmUserEntity());
        }

        public String getId() {
            return getService().getId().toString();
        }

        public void setId(Serializable value) {
            this.id = value;
            if (value != "") {
                getService().setId(Long.parseLong(value.toString()));
            } else {
                getService().setId(Constant.DEFAULT_ID);
            }
        }

        public List<Serializable> getRoles() {
            return getService().getRoles();
        }

        public void setRoles(List<Serializable> value) {
            this.roles = value;
            getService().setRoles(value);
        }

        public String getUserName() {
            return getService().getUserName();
        }

        public void setUserName(String value) {
            this.userName = value;
            getService().setUserName(value);
        }

        public String getEnName() {
            return getService().getEnName();
        }

        public void setEnName(String value) {
            this.enName = value;
            getService().setEnName(value);
        }

        public String getCnName() {
            return getService().getCnName();
        }

        public void setCnName(String value) {
            this.cnName = value;
            getService().setCnName(value);
        }

        public String getNickName() {
            return getService().getNickName();
        }

        public void setNickName(String value) {
            this.nickName = value;
            getService().setNickName(value);
        }

        public String getEmail() {
            return getService().getEmail();
        }

        public void setEmail(String value) {
            this.email = value;
            getService().setEmail(value);
        }

        public String getPhone() {
            return getService().getPhone();
        }

        public void setPhone(String value) {
            this.phone = value;
            getService().setPhone(value);
        }

        public String getJobNo() {
            return getService().getJobNo();
        }

        public void setJobNo(String value) {
            this.jobNo = value;
            getService().setJobNo(value);
        }

        public String getNo() {
            return getService().getNo();
        }

        public void setNo(String value) {
            this.no = value;
            getService().setNo(value);
        }

        public String getCnGroupName() {
            return getService().getCnGroupName();
        }

        public void setCnGroupName(String value) {
            this.cnGroupName = value;
            getService().setCnGroupName(value);
        }

        public String getEnGroupName() {
            return getService().getEnGroupName();
        }

        public void setEnGroupName(String value) {
            this.enGroupName = value;
            getService().setEnGroupName(value);
        }

        public String getGroupName() {
            return getService().getGroupName();
        }

        public void setGroupName(String value) {
            this.groupName = value;
            getService().setGroupName(value);
        }

        public String getIdCard() {
            return getService().getIdCard();
        }

        public void setIdCard(String value) {
            this.idCard = value;
            getService().setIdCard(value);
        }

        public Boolean getIsActive() {
            return getService().getIsActive();
        }

        public void setIsActive(Boolean value) {
            this.isActive = value;
            getService().setIsActive(value);
        }

        public String getRemark() {
            return getService().getRemark();
        }

        public void setRemark(String value) {
            this.remark = value;
            getService().setRemark(value);
        }

        public Integer getUserType() {
            return getService().getUserType();
        }

        public void setUserType(Integer value) {
            this.userType = value;
            getService().setUserType(value);
        }

        public Date getPassExpireDt() {
            return getService().getPassExpireDt();
        }

        public void setPassExpireDt(Date value) {
            if (value == null) {
                value = DateUtils.addDateMonths(new Date(), 3);
            }
            this.passExpireDt = value;
            getService().setPassExpireDt(value);
        }

        public Date getExpiredDt() {
            return getService().getExpiredDt();
        }

        public void setExpiredDt(Date value) {
            this.expiredDt = value;
            getService().setExpiredDt(value);
        }

        public String getDepartmentId() {
            return getService().getDepartmentId();
        }

        public void setDepartmentId(Serializable value) {
            this.departmentId = value;
            getService().setDepartmentId(value.toString());
        }


    }
}


