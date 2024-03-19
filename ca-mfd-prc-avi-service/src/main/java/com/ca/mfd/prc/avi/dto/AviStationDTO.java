package com.ca.mfd.prc.avi.dto;

import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 前端AVI站点对象数据
 * <p>
 * AviStationDTO class
 *
 * @author luowenbing
 * @date 2023/04/06
 */
@Data
public class AviStationDTO {

    /**
     * Avi站点主键
     */
    private String id = StringUtils.EMPTY;

    /**
     * AVI站点代码
     */
    private String code = StringUtils.EMPTY;

    /**
     * AVI站点名称
     */
    private String name = StringUtils.EMPTY;

    /**
     * 车间名称
     */
    private String pmShopName = StringUtils.EMPTY;

    /**
     * 车间关键ID
     */
    private Long pmShopId;

    /**
     * 车间代码
     */
    private String pmShopCode = StringUtils.EMPTY;

    /**
     * 线体代码
     */
    private String pmAreaCode = StringUtils.EMPTY;

    /**
     * 线体名称
     */
    private String pmAreaName = StringUtils.EMPTY;

    /**
     * 线体关键建
     */
    private Long pmAreaId;

    /**
     * Avi所在线体的工位数
     */
    private List<PmWorkStationEntity> stationsp;

    /**
     * 路由地址服务地址
     */
    private String signalrRouteUrl = StringUtils.EMPTY;

    /**
     * 路由仿真地址服务地址
     */
    private String signalrRouteSmlUrl = StringUtils.EMPTY;

    /**
     * Avi服务地址
     */
    private String signalrAviUrl = StringUtils.EMPTY;

    /**
     * 路由页面操作地址
     */
    private String webApiRouteUrl = StringUtils.EMPTY;

    /**
     * 路由仿真页面操作地址
     */
    private String webApiRouteSmlUrl = StringUtils.EMPTY;

    /**
     * AVI页面操作地址
     */
    private String webApiAviUrl = StringUtils.EMPTY;

    /**
     * 站点地址
     */
    private String ip = StringUtils.EMPTY;

    /**
     * Avi点菜单
     */
    private List<AviMenuDTO> aviMenu;

    /**
     * 同个屏关联多个AVI点
     */
    private List<ComboInfoDTO> relAvi;

    /**
     * 权限列表
     */
    private List<String> permissions;

    ///// <summary>
    ///// Avi点关联的路由点对象
    ///// </summary>
    //private IList<RoutePointBlockInfo> RouteList { get; set; } = new List<RoutePointBlockInfo>();

    ///// <summary>
    ///// s
    ///// </summary>
    //private IList<AviRouteRuleSetInfo> RulePolicy { get; set; } = new List<AviRouteRuleSetInfo>();

    /**
     * 菜单配置
     */
    private String menus = StringUtils.EMPTY;

    /**
     * 默认页
     */
    private String defaultPage = StringUtils.EMPTY;
}
