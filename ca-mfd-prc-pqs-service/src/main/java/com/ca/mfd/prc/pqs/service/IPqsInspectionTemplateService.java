package com.ca.mfd.prc.pqs.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.model.base.dto.ConditionDto;
import com.ca.mfd.prc.common.model.base.dto.SortDto;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.pqs.dto.TemplateCopyDto;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateEntity;
import com.ca.mfd.prc.pqs.entity.PqsInspectionTemplateItemEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author inkelink
 * @Description: 检验模板管理服务
 * @date 2023年08月22日
 * @变更说明 BY inkelink At 2023年08月22日
 */
public interface IPqsInspectionTemplateService extends ICrudService<PqsInspectionTemplateEntity> {

    /**
     * 从缓存中获取检验模板信息
     *
     * @return 检验模板列表
     */
    List<PqsInspectionTemplateEntity> getAllDatas();

    /**
     * 获取检验模板  抽检/巡检使用
     *
     * @param entryType   工单类型
     * @param materialNo  物料号
     * @param processCode 工序
     * @return 模板列表
     */
    List<ComboInfoDTO> getTemplateList(String entryType, String materialNo, String processCode);

    /**
     * 获取检验模板明细
     *
     * @param tempalteId 模板ID
     * @return 检验模板明细
     */
    List<PqsInspectionTemplateItemEntity> getTempalteDetail(String tempalteId);

    /**
     * 保存模板明细
     *
     * @param pqsInspectTemplateItemDtos 保存检验模板
     */
    void saveTemplateItem(List<PqsInspectionTemplateItemEntity> pqsInspectTemplateItemDtos);

    /**
     * 复制模板
     *
     * @param dto
     */
    void copyTemplate(TemplateCopyDto dto);


    /**
     * 获取模板
     * @param fileName
     * @param response
     */
    void getImportTemplateMa(String fileName, HttpServletResponse response) throws IOException;


    /**
     * 导入数据
     * @param is
     * @throws Exception
     */
    void importExcelMa(InputStream is) throws Exception;

    /**
     * 导出Excel
     * @param conditions
     * @param sorts
     * @param fileName
     * @param response
     * @throws IOException
     */
    void exportMa(List<ConditionDto> conditions, List<SortDto> sorts, String fileName, HttpServletResponse response) throws IOException;
}