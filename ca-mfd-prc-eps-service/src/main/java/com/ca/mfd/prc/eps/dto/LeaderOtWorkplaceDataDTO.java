package com.ca.mfd.prc.eps.dto;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 班长OT
 *
 * @author eric.zhou
 * @date 2023/04/12
 */
@Data
public class LeaderOtWorkplaceDataDTO {

    /**
     * 车间编号
     */
    private String shopCode = StringUtils.EMPTY;
    /**
     * 线体编号
     */
    private String areaId = StringUtils.EMPTY;
    /**
     * 关联的工位及岗位集合
     */
    private Map<String, List<ComboInfoDTO>> workplaceDictionary;
    /**
     * 服务接口和推送地址
     */
    @JsonAlias(value = {"oTSingalServerIp", "otSingalServerIp"})
    private String otSingalServerIp = StringUtils.EMPTY;
    /**
     * OT服务Signalr地址
     */
    @JsonAlias(value = {"oTSignalRIp", "otSignalRip"})
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

    public LeaderOtWorkplaceDataDTO() {
        this.workplaceDictionary = new HashMap<>();
    }

}
