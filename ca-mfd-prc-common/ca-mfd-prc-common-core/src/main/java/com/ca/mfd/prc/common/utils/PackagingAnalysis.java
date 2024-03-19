package com.ca.mfd.prc.common.utils;

import com.ca.mfd.prc.common.dto.pmc.PackagingPointResponse;
import com.ca.mfd.prc.common.dto.pmc.PositionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * PackagingAnalysis
 *
 * @author inkelink
 * @date 2023-08-17
 */
public class PackagingAnalysis {

    private static final Logger logger = LoggerFactory.getLogger(PackagingAnalysis.class);
    private static final String START_STR = "=";
    private static final String END_STR = "$";
    private static final String POINT_SPLIT = ";";
    private static final String POINT_ALIAS_STR = "++";
    private static final String STATION_STR = "+";
    private static final Integer STATION_LEN = 3;

    /**
     * 解析点位数据
     *
     * @param pointAlias
     * @return
     */
    public static PackagingPointResponse packagingPoint(String pointAlias) {
        pointAlias = pointAlias.trim().concat(POINT_SPLIT);
        PackagingPointResponse info = new PackagingPointResponse();

        String position = pointAlias.split(POINT_SPLIT)[0] + END_STR;

        //=MB++MB250+GB01-SC;安全门打开;1
        if (pointAlias.indexOf(POINT_ALIAS_STR) != -1) {
            info.setArea(midStrEx(position, START_STR, POINT_ALIAS_STR));

            String station = position.substring(info.getArea().length() + 2);

            if (station.indexOf(STATION_STR) != -1) {
                info.setStation(midStrEx(position, START_STR + info.getArea() + POINT_ALIAS_STR, STATION_STR));
                info.setEqCode(midStrEx(position, START_STR + info.getArea() + POINT_ALIAS_STR + info.getStation() + END_STR, END_STR));
            } else {
                info.setStation(midStrEx(position, START_STR + info.getArea() + POINT_ALIAS_STR, END_STR));
                info.setEqCode("");
            }
        } else {
            info.setArea(midStrEx(position, START_STR, END_STR));
        }

        String[] pointArrary = pointAlias.split(POINT_SPLIT);

        info.setPosition(pointAlias.split(POINT_SPLIT)[0]);
        info.setAlarmCode(pointAlias.split(POINT_SPLIT)[1]);
        info.setAlarmLevel(pointAlias.split(POINT_SPLIT)[2]);
        if (pointArrary.length > STATION_LEN) {
            info.setAlarmDes(pointAlias.split(POINT_SPLIT)[3]);
        }

        return info;
    }


    private static String midStrEx(String sourse, String startstr, String endstr) {
        String result = "";
        int startindex, endindex;
        try {
            startindex = sourse.indexOf(startstr);
            if (startindex == -1) {
                return result;
            }
            String tmpstr = sourse.substring(startindex + startstr.length());
            endindex = tmpstr.indexOf(endstr);
            if (endindex == -1) {
                return result;
            }
            result = tmpstr.substring(0, endindex);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return result;
    }

    public static PositionDTO packagingPosition(String position) {
        position = position + END_STR;
        PositionDTO info = new PositionDTO();

        //=区域++工位+设备编码;报警描述;报警等级
        //=MB++MB250+GB01-SC;
        if (position.indexOf(POINT_ALIAS_STR) != -1) {
            String[] positionArr = position.split(POINT_SPLIT);
            info.setArea(positionArr[0].substring(positionArr[0].indexOf(START_STR) + 1, positionArr[0].indexOf(POINT_ALIAS_STR)));

            String station = positionArr[0].substring(positionArr[0].indexOf(START_STR) + info.getArea().length() + 2);

            if (station.indexOf(STATION_STR) != -1) {
                info.setStation(station.substring(0, station.indexOf(STATION_STR)));
            } else {
                info.setStation(station);
            }
        } else {
            String[] positionArr = position.split(POINT_SPLIT);
            info.setArea(positionArr[0].substring(positionArr[0].indexOf(START_STR) + 1));
        }

        return info;
    }
}
