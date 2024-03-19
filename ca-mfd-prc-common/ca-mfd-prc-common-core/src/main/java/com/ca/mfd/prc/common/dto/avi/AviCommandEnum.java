package com.ca.mfd.prc.common.dto.avi;


/**
 * AviCommandEnum
 *
 * @author eric.zhou@hg2mes.com
 * @date 2023/4/6
 */
public enum AviCommandEnum {
    RFIDMANUALREAD(1, "RFID手动读取"),
    RFIDRESET(2, "RFID复位"),
    AVIFORCEPASS(3, "AVI强制放行"),
    AVIEDITSTOPLINE(4, "AVI编辑停线"),
    AVICANCELEDITSTOPLINE(5, "AVI取消编辑停线"),
    PLCHALTRECEORDER(6, "PLC暂停接收订单"),
    PLCCACHEORDERCLEAN(7, "PLC缓存订单清除"),
    RFIDMANUALWRITE(8, "RFID手动写入"),
    RFIDKEEPRECEORDER(9, "PLC继续接收订单"),
    AVIMANUALPOINT(10, "AVI手动过点"),
    AVIMANUALMODEL(11, "AVI手动模式"),
    AVIAUTOMODEL(12, "AVI自动模式"),
    FORCEPOINT(13, "强制过点"),
    CARPULLIN(14, "车辆拉入"),
    CARPULLOUT(15, "车辆拉出"),
    AUTOPOINTRESET(16, "自动过点复位"),
    CAREDIT(17, "编辑车辆"),
    CARRANGEPAY(18, "车辆距离补偿"),
    INSERTCAR(19, "插入车辆"),
    DELETECAR(20, "删除车辆"),
    ORDERREST(21, "订单重置"),
    EMPTYPRYCONFIRM(22, "空撬确认"),
    CUTCURRENT(23, "切入当前"),
    EDITBUFF(24, "编辑Buff"),
    EDITBUFFNUMBER(25, "编辑Buff数量"),
    CARCOMPARISON(26, "车辆比对"),
    RFIDWRITE(27, "RFID写入");
    private final int code;
    private final String description;

    AviCommandEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int code() {
        return this.code;
    }

    public String description() {
        return this.description;
    }
}
