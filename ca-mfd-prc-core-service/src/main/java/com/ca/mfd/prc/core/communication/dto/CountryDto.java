package com.ca.mfd.prc.core.communication.dto;

import com.ca.mfd.prc.common.constant.Constant;
import com.ca.mfd.prc.common.convert.JsonDeserializeDefault;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @Description: 国家代码实体
 * @author inkelink
 * @date 2023年10月09日
 * @变更说明 BY inkelink At 2023年10月09日
 */
@Data
public class CountryDto {


    @Schema(title = "国家ID")
    @JsonAlias(value = {"NATION_ID","nationId"})
    private String nationId = StringUtils.EMPTY;


    @Schema(title = "代码")
    @JsonAlias(value = {"NATION_CODE","nationCode"})
    private String nationCode = StringUtils.EMPTY;


    @Schema(title = "国家名称")
    @JsonAlias(value = {"NATION_NAME","nationName"})
    private String nationName = StringUtils.EMPTY;

    @Schema(title = "英文全称")
    @JsonAlias(value = {"NATION_EN","nationEn"})
    private String nationEn = StringUtils.EMPTY;

    @Schema(title = "英文简称")
    @JsonAlias(value = {"NATION_ENGLISH_ABBREVIATION","nationEnglishAbbreviation"})
    private String nationEnglishAbbreviation = StringUtils.EMPTY;

    @Schema(title = "三字符拉丁字母代码")
    @JsonAlias(value = {"NATION_THREE_CHARACTER_LATIN_ALPHABET_CODE","nationThreeCharacterLatinAlphabetCode"})
    private String nationThreeCharacterLatinAlphabetCode = StringUtils.EMPTY;

    @Schema(title = "阿拉伯数字代码")
    @JsonAlias(value = {"NATION_ARABIC_NUMERAL_CODE","nationArabicNumeralCode"})
    private String nationArabicNumeralCode = StringUtils.EMPTY;

    @Schema(title = "中文全称")
    @JsonAlias(value = {"NATION_FULL_CHINESE_NAME","nationFullChineseName"})
    private String nationFullChineseName = StringUtils.EMPTY;

    @Schema(title = "2字母语种代码")
    @JsonAlias(value = {"NATION_2-LETTER_LANGUAGE_CODE","nation2LetterLanguageCode"})
    private String nation2LetterLanguageCode = StringUtils.EMPTY;

    @Schema(title = "3字母语种代码")
    @JsonAlias(value = {"NATION_3-LETTER_LANGUAGE_CODE","nation3LetterLanguageCode"})
    private String nation3LetterLanguageCode = StringUtils.EMPTY;

    @Schema(title = "国家和地区当地简称")
    @JsonAlias(value = {"NATION_COUNTRY_AND_REGION_LOCAL_ABBREVIATION","nationGountryAndRegionLocalAbbreviation"})
    private String nationGountryAndRegionLocalAbbreviation = StringUtils.EMPTY;


    /**
     * 备注
     */
    @Schema(title = "备注")
    @JsonAlias(value = {"NATION_REMARK","nationRemark"})
    private String nationRemark = StringUtils.EMPTY;


    /**
     * 主数据状态0：正常， 2：废止
     */
    @Schema(title = "主数据状态0：正常， 2：废止")
    @JsonAlias(value = {"STATUS","status"})
    private Integer status = 0;


    /**
     * 变化记录序列号
     */
    @Schema(title = "变化记录序列号")
    @JsonAlias(value = {"SUB_ID","subId"})
    private Long subId = Constant.DEFAULT_ID;


    /**
     * 数据变化类型1：新增 2：更新 3：删除 4：更新或新增
     */
    @Schema(title = "数据变化类型1：新增 2：更新 3：删除 4：更新或新增")
    @JsonAlias(value = {"OP_CODE","opCode"})
    private Integer opCode = 0;

    /**
     * 校验结果
     */
    @Schema(title = "校验结果")
    @JsonDeserialize(using = JsonDeserializeDefault.class)
    private Integer checkResult = 0;


    /**
     * 校验结果说明
     */
    @Schema(title = "校验结果说明")
    private String checkResultDesc = StringUtils.EMPTY;


}