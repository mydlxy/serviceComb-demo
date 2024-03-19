package com.ca.mfd.prc.pqs.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.ca.mfd.prc.common.entity.BaseEntity;
import com.ca.mfd.prc.common.constant.Constant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 *
 * @Description: 焊点数据统计实体
 * @author inkelink
 * @date 2024年02月02日
 * @变更说明 BY inkelink At 2024年02月02日
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description= "焊点数据统计")
@TableName("PRC_PQS_INSPECT_SOLD_JOINT_DATA_STAT")
public class PqsInspectSoldJointDataStatEntity extends BaseEntity {

    /**
     * ID
     */
    @Schema(title = "ID")
    @TableId(value = "PRC_PQS_INSPECT_SOLD_JOINT_DATA_STAT_ID", type = IdType.INPUT)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id = Constant.DEFAULT_ID;


    /**
     * 部位
     */
    @Schema(title = "部位")
    @TableField("POSITION")
    private String position = StringUtils.EMPTY;


    /**
     * 焊点总数
     */
    @Schema(title = "焊点总数")
    @TableField("TOTAL_NUMBER_SOLDER_JOINTS")
    private Integer totalNumberSolderJoints = 0;


    /**
     * 关键焊点总数
     */
    @Schema(title = "关键焊点总数")
    @TableField("TOTAL_NUMBER_KEY_SOLDER_JOINTS")
    private Integer totalNumberKeySolderJoints = 0;


    /**
     * 焊点不合格数
     */
    @Schema(title = "焊点不合格数")
    @TableField("NUMBER_UNQUALIFIED_SOLDER_JOINTS")
    private Integer numberUnqualifiedSolderJoints = 0;


    /**
     * 关键焊点不合格数
     */
    @Schema(title = "关键焊点不合格数")
    @TableField("NUMBER_UNQUALIFIED_KEY_WELDING_POINTS")
    private Integer numberUnqualifiedKeyWeldingPoints = 0;


    /**
     * 不合格焊点比率
     */
    @Schema(title = "不合格焊点比率")
    @TableField("UNQU_SOLD_JOINT_RATIO")
    private BigDecimal unquSoldJointRatio = BigDecimal.valueOf(0);


    /**
     * 破坏性试验前照片
     */
    @Schema(title = "破坏性试验前照片")
    @TableField("PHOTOS_BEFORE_DEST_TEST")
    private String photosBeforeDestTest = StringUtils.EMPTY;


    /**
     * 破坏性试验后照片
     */
    @Schema(title = "破坏性试验后照片")
    @TableField("PHOTOS_AFTER_DEST_TESTING")
    private String photosAfterDestTesting = StringUtils.EMPTY;


    /**
     * 外协件焊点总数
     */
    @Schema(title = "外协件焊点总数")
    @TableField("TOTAL_NUMBER_WELD_POINTS_OUTS_PARTS")
    private Integer totalNumberWeldPointsOutsParts = 0;


    /**
     * 外协件焊点不合格数
     */
    @Schema(title = "外协件焊点不合格数")
    @TableField("NUMBER_UNQU_WELD_POINTS_OUTS_PARTS")
    private Integer numberUnquWeldPointsOutsParts = 0;


    /**
     * 外协件不合格焊点比率
     */
    @Schema(title = "外协件不合格焊点比率")
    @TableField("RATIO_UNQU_SOLDER_JOINTS_OUTS_PARTS")
    private BigDecimal ratioUnquSolderJointsOutsParts = BigDecimal.valueOf(0);


    /**
     * 破坏性试验前照片
     */
    @Schema(title = "破坏性试验前照片")
    @TableField("PHOTOS_BEFORE_DEST_TEST_OUTS_COMP")
    private String photosBeforeDestTestOutsComp = StringUtils.EMPTY;


    /**
     * 破坏性试验后照片
     */
    @Schema(title = "破坏性试验后照片")
    @TableField("PHOTOS_AFTER_DEST_TEST_OUTS_COMP")
    private String photosAfterDestTestOutsComp = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @TableField("REMARK")
    private String remark = StringUtils.EMPTY;


}