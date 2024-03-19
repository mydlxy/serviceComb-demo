package com.ca.mfd.prc.eps.remote.app.pm.dto;

import com.ca.mfd.prc.eps.remote.app.pm.entity.PmAviEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmLineEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmOrganizationEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmOtEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmPullCordEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmToolEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmToolJobEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWoEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkShopEntity;
import com.ca.mfd.prc.eps.remote.app.pm.entity.PmWorkStationEntity;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
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
public class PmAllDTO implements Serializable {
    private String version = StringUtils.EMPTY;
    /*** 
     * 工厂
     ***/
    private PmOrganizationEntity organization = new PmOrganizationEntity();
    /***
     * 车间集合
     ***/
    private List<PmWorkShopEntity> shops;
    /***
     * 线体集合
     ***/
    private List<PmLineEntity> lines;
    /***
     * 工位集合
     ***/
    private List<PmWorkStationEntity> stations;
    /***
     * AVI集合
     ***/
    private List<PmAviEntity> avis;

    /***
     * 所有岗位集合（包含启用和不启动）
     ***/
    private List<PmWorkStationEntity> allStations;
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

    public PmAllDTO() {
        version = "";
        shops = new ArrayList<>();
        lines = new ArrayList<>();
        stations = new ArrayList<>();
        avis = new ArrayList<>();
        allStations = new ArrayList<>();
        pullCords = new ArrayList<>();
        ots = new ArrayList<>();
        wos = new ArrayList<>();
        tools = new ArrayList<>();
        toolJobs = new ArrayList<>();
    }

    public void addPmInfo(PmInfo info) {
        if (info.getShop() != null) {
            getShops().add(info.getShop());
        }
        if (CollectionUtils.isNotEmpty(info.getLines())) {
            getLines().addAll(info.getLines());
        }
        if (CollectionUtils.isNotEmpty(info.getStations())) {
            getStations().addAll(info.getStations());
        }
        if (CollectionUtils.isNotEmpty(info.getAvis())) {
            getAvis().addAll(info.getAvis());
        }
        if (CollectionUtils.isNotEmpty(info.getPullCords())) {
            getPullCords().addAll(info.getPullCords());
        }
        if (CollectionUtils.isNotEmpty(info.getOts())) {
            getOts().addAll(info.getOts());
        }
        if (CollectionUtils.isNotEmpty(info.getWos())) {
            getWos().addAll(info.getWos());
        }
        if (CollectionUtils.isNotEmpty(info.getTools())) {
            getTools().addAll(info.getTools());
        }
        if (CollectionUtils.isNotEmpty(info.getToolJobs())) {
            getToolJobs().addAll(info.getToolJobs());
        }
        if (CollectionUtils.isNotEmpty(info.getAllStations())) {
            getAllStations().addAll(info.getAllStations());
        }
    }
}
