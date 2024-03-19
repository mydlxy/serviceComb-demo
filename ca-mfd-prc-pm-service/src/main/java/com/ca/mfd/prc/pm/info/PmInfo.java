package com.ca.mfd.prc.pm.info;

import com.ca.mfd.prc.pm.entity.PmAviEntity;
import com.ca.mfd.prc.pm.entity.PmLineEntity;
import com.ca.mfd.prc.pm.entity.PmOtEntity;
import com.ca.mfd.prc.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.pm.entity.PmToolEntity;
import com.ca.mfd.prc.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.pm.entity.PmWoEntity;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import lombok.Data;

import java.util.List;

/**
 * @author luowenbing
 * @Description: PmInfo class
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */
@Deprecated
@Data
public class PmInfo {
    private PmWorkShopEntity shop;
    private List<PmLineEntity> lines;
    private List<PmWorkStationEntity> stations;
    private List<PmAviEntity> avis;
    private List<PmPullCordEntity> pullCords;
    private List<PmOtEntity> ots;
    private List<PmWoEntity> wos;
    private List<PmToolEntity> tools;
    private List<PmToolJobEntity> toolJobs;
    private List<PmWorkStationEntity> allStations;

}
