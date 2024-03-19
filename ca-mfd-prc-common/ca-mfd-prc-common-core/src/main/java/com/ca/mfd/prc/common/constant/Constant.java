/**
 * Copyright (c) 2023 依柯力 All rights reserved.
 * <p>
 * https://www.inkelink.com
 * <p>
 * 版权所有，侵权必究！
 */

package com.ca.mfd.prc.common.constant;

import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 常量
 *
 * @author inkelink
 */
public interface Constant {
    /**
     * 成功
     */
    int SUCCESS = 1;
    /**
     * 失败
     */
    int FAIL = 0;
    /**
     * 菜单根节点标识
     */
    Long MENU_ROOT = 0L;
    /**
     * 部门根节点标识
     */
    Long DEPT_ROOT = 0L;
    /**
     * 升序
     */
    String ASC = "asc";
    /**
     * 降序
     */
    String DESC = "desc";
    /**
     * 创建时间字段名
     */
    String CREATE_DATE = "create_date";

    /**
     * 数据权限过滤
     */
    String SQL_FILTER = "sqlFilter";
    /**
     * 当前页码
     */
    String PAGE = "page";
    /**
     * 每页显示记录数
     */
    String LIMIT = "limit";
    /**
     * 排序字段
     */
    String ORDER_FIELD = "orderField";
    /**
     * 排序方式
     */
    String ORDER = "order";
    /**
     * token header
     */
    String TOKEN_HEADER = "token";

    /**
     * 云存储配置KEY
     */
    String CLOUD_STORAGE_CONFIG_KEY = "CLOUD_STORAGE_CONFIG_KEY";
    /**
     * 空ID
     */
    String EMPTY_ID = "00000000-0000-0000-0000-000000000000";
    /**
     * 空ID
     */
    String EMPTY_ID2 = "11111111-1111-1111-1111-111111111111";
    String EQUALITY = "Iot";
    String EQUALITY_INNER_HTTP = "IotInnerHttp";

    /**
     * 定义是否启用监控根据配置文件决定
     */
    String MONITOR_AUDIT_EVENT = "MonitorAuditEvent:AuditEvent";
    /**
     * 定义自定义权限code
     */
    String CUSTOM_PERMISSION = "Custom_Permission";
    /**
     * 定义多住户的权限code
     */
    String PERMISSION_CODES = "PermissionCodes";
    /**
     * 定义用户密码超时分类
     */
    String USER_PASS_STRATEGY = "UserPassStrategy";
    /**
     * 定义用户密码是否启用过期设置
     */
    String IS_ACTIVE_EXPIRE = "IsActiveExpire";
    /**
     * 定义用户密码是否启用过期的天数
     */
    String EXPIRE_MONTH = "ExpireMonth";
    /**
     * 定义用户默认密码
     */
    String DEFALUT_PASSWORD = "DefalutPassword";
    /**
     * 系统的产品类型
     */
    String PRODUCT_CODE = "ProductCode";
    /**
     * AD 域配置
     */
    String AD = "AD";
    String AD_HOST = "AdHost";
    String AD_ADMIN_USER_NAME = "AdAdminUserName";
    String AD_ADMIN_PASSWORD = "AdAdminPassword";
    String AD_PATHS = "AdPaths";
    String AD_PROT = "AdProt";
    String SAM_ACCOUNT_NAME = "sAMAccountName";
    String USER_NAME = "UserName";
    String NICK_NAME = "NickName";
    String PHONE = "Phone";
    String EMAIL = "Email";
    String CODE = "Code";
    /**
     * 授权中心授权地址 VerifyLicenseAbstract
     */
    String CONFIG_EQUALITY_AUT_URL = "EQualityAutUrl:AutUrl";
    String CONFIG_EQUALITY_MAC_AUTH = "EQualityAutUrl:IsMacAuth";
    /**
     * 系统配置授权中心授权分类 VerifyLicenseAbstract
     */
    String EQUALITY_AUT_URL = "EQualityAutUrl";
    /**
     * 系统配置授权中心授权http://0.0.0.0/equality.authentication.authorization/authorization/Authentication
     */
    String AUT_URL = "AutUrl";
    /**
     * 第三方授权
     */
    String AUT_OPEN = "AutOpen";
    /**
     * auth EQuality.Framework.Common.License.LicenseManager
     */
    String ENCRYPT_KEY = "0EF7CA79-FF11-6430-CC75-39DDA9219E6C";
    String ENCRYPT_IV = "3707602E-2DE7-2152-0CD5-39F41DB4FD3C";
    /**
     * 授权文件保存路径 EQuality.Framework.Common.License.LicenseManager
     */
    String LICENSE_FILE = "LicenseFile";
    String FILE_PATH = "FilePath";
    String LOG_VIEW = "System:LogView";
    String MINI_PROFILER = "System:MiniProfiler";
    String SWAGGER_VIEW = "System:SwaggerView";
    String SYSTEM_DEFALUT = "system";
    String SYSTEM_MANAGER = "superadmin";
    String SYSTEM_ADMINROLE = "Administrator";
    String SPLIT_ARRAYS = "\"[]\"";
    String SPLIT_JIN = "#";
    Long DEFAULT_USER_ID = 0L;
    Long DEFAULT_ID = 0L;
    /**
     * 系统默认用户
     */
    String SYS_DEFAULT_USER = "SYS";
    /**
     * 可用状态-可用
     */
    String SYS_FLAGS_YES = "Y";
    /**
     * 可用状态-不可用
     */
    String SYS_FLAGS_NO = "N";


//    Date MAX_DATE = new GregorianCalendar(9999, 1, 1).getTime();
//    Date MIN_DATE = new GregorianCalendar(1, 1, 1).getTime();

    MediaType APPLICATION_JSON_UTF8 = new MediaType("application", "json", StandardCharsets.UTF_8);

    String TRUE_BOOL = "true";
    String TRUE_NUM = "1";
    String TRUE_CHINESE = "是";

    String VEHICLE_MODEL = "vehicleModel";

    String AVI_TYPE = "aviType";
    String LINE_TYPE = "LineType";
    String LINE_RUN_MODEL = "LineRunModel";
}