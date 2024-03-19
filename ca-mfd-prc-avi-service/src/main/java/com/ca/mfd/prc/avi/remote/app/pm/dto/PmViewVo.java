package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author inkelink
 * @Description: PM模块主信息
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
public class PmViewVo implements Serializable {

    /***
     * 车间集合
     ***/
    @JSONField(name = "PmWorkShopEntity")
    private PmWorkShopEntity pmWorkShopEntity;


}
