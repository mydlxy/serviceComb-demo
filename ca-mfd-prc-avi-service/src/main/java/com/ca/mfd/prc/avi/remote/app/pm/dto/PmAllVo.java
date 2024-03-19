package com.ca.mfd.prc.avi.remote.app.pm.dto;

import com.ca.mfd.prc.avi.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmBopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmOtEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmToolEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWoEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkStationEntity;
import com.ca.mfd.prc.avi.remote.app.pm.entity.PmWorkstationMaterialEntity;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author inkelink
 * @Description: PM模块主信息
 * @date 2023年4月28日
 * @变更说明 BY inkelink At 2023年4月28日
 */
@Data
public class PmAllVo implements Serializable {
    private String version = StringUtils.EMPTY;
    /***
     * 工厂
     ***/
    private PmOrganizationEntity organization = new PmOrganizationEntity();
    /***
     * 车间集合
     ***/
    private List<PmWorkShopEntity> workShops;
    /***
     * 线体集合
     ***/
    private List<PmLineEntity> lines;
    /***
     * 工位集合（包含启用）
     ***/
    private List<PmWorkStationEntity> workStations;
    /***
     * 所有岗位集合（包含启用和不启动）
     ***/
    private List<PmWorkStationEntity> allWorkStations;
    /***
     * AVI集合
     ***/
    private List<PmAviEntity> avis;
    /***
     * 拉绳集合
     ***/
    private List<PmPullCordEntity> pullCords;
    /***
     * ot集合
     ***/
    private List<PmOtEntity> ots;
    /***
     * 操作集合
     ***/
    private List<PmWoEntity> wos;
    /***
     * 工具集合
     ***/
    private List<PmToolEntity> tools;
    /***
     * 工具job集合
     ***/
    private List<PmToolJobEntity> toolJobs;
    /***
     * bop集合
     ***/
    private List<PmBopEntity> bops;
    /**
     * 工位物料集合
     */
    private List<PmWorkstationMaterialEntity> workstationMaterials;

    public PmAllVo() {
        version = "";
        workShops = new ArrayList<>();
        lines = new ArrayList<>();
        workStations = new ArrayList<>();
        avis = new ArrayList<>();
        allWorkStations = new ArrayList<>();
        pullCords = new ArrayList<>();
        ots = new ArrayList<>();
        wos = new ArrayList<>();
        tools = new ArrayList<>();
        toolJobs = new ArrayList<>();
        bops = new ArrayList<>();
        workstationMaterials = new ArrayList<>();
    }
}
