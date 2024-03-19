package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.eps.remote.app.pm.entity.PmPullCordEntity;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * OT站点初始化数据
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class InitOtWorkplaceDataDTO {

    /**
     * 车间编号
     */
    private String shopId = StringUtils.EMPTY;
    /**
     * 线体编号
     */
    private String areaId = StringUtils.EMPTY;
    /**
     * 工位编号
     */
    private String stationId = StringUtils.EMPTY;
    /**
     * 岗位编号
     */
    private String workplaceId = StringUtils.EMPTY;
    /**
     * 开始距离
     */
    private Integer beginDistance = 0;
    /**
     * 结束距离
     */
    private Integer endDistance = 0;
    /**
     * 车间代码
     */
    private String shopCode = StringUtils.EMPTY;
    /**
     * 线体代码
     */
    private String areaCode = StringUtils.EMPTY;
    /**
     * 岗位名称
     */
    private String workplaceName = StringUtils.EMPTY;

    /**
     * OT名称
     */
    private String otName = StringUtils.EMPTY;

    /**
     * OT站点模板
     */
    private String otTemplate = StringUtils.EMPTY;

    /**
     * 服务接口和推送地址
     */
    private String otSingalServerIp = StringUtils.EMPTY;

    /**
     * OT服务Signalr地址
     */
    @JsonAlias(value = {"otSignalRIp", "otSignalRip"})
    private String otSignalRip = StringUtils.EMPTY;

    /**
     * 线体服务接口和推送地址
     */
    private String areaSingalServerIp = StringUtils.EMPTY;

    /**
     * AREA服务Signalr地址
     */
    @JsonAlias(value = {"areaSignalRIp", "areaSignalRip"})
    private String areaSignalRip = StringUtils.EMPTY;

    /**
     * 操作指导书
     */
    private List<OperBookDTO> books;

    /**
     * 呼叫列表
     */
    private List<PmPullCordEntity> pullcordInfos;

}
