package com.ca.mfd.prc.common.entity;

import com.ca.mfd.prc.common.enums.ServiceMethodRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * TokenDataInfo信息
 *
 * @author inkelink eric.zhou@hg2mes.com
 * @date 2023-08-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "TokenDataInfo信息")
public class MiddleRequest extends MiddleResponse {
    private static final long serialVersionUID = 1L;

    @Schema(title = "系统请求域对应的key")
    private String serviceDomainKey;

    @Schema(title = "请求路由")
    private String useRoutName;

    @Schema(title = "服务方式 http Get;Post")
    private ServiceMethodRequest serviceTypeEnum;

    @Schema(title = "需要验证具体用户默认可以获取token")
    private Boolean attachToken;

    @Schema(title = "前提是默认token 设置为flase")
    private String overrideToken = StringUtils.EMPTY;

    @Schema(title = "内部服务默认true 系统自动添加token")
    private Boolean defalutEqualityService = true;

    @Schema(title = "超时时间")
    private Integer timeout = 1200000;

    @Schema(title = "请求参数")
    private Map params = new LinkedHashMap<>();

    @Schema(title = "请求头")
    private Map<String, String> header;

//    /// <summary>
//    ///  TODO 所有的键值对对象实现最终数据请求在这里 SseviceMethodRequest 如果是Get 请完善键值对提交数据
//    /// </summary>
//    /// <returns></returns>
//    Dictionary<string, object> GetDictionary()

}

