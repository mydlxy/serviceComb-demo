package com.ca.mfd.prc.common.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 门户-获取用户多组织信息返回数据
 *
 * @author mason
 * @date 2023-09-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "门户-获取用户多组织信息返回数据")
public class QueryUserOrgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(title = "账套数据")
    private List<DetailDto> ledgerList;

    @Schema(title = "组织数据")
    private List<DetailDto> organizationList;

    @Schema(title = "业务实体")
    private List<DetailDto> orgList;

    @Data
    @EqualsAndHashCode(callSuper = false)
    public class DetailDto implements Serializable {
        @Schema(title = "code")
        private String code;
        @Schema(title = "name")
        private String name;
        @Schema(title = "id")
        private String id;
    }

}
