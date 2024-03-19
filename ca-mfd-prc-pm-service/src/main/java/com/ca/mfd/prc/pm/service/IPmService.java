package com.ca.mfd.prc.pm.service;

import com.alibaba.fastjson.JSONObject;
import com.ca.mfd.prc.common.utils.TreeNode;
import com.ca.mfd.prc.pm.dto.ComponentDataDTO;
import com.ca.mfd.prc.pm.dto.PmInfo;
import com.ca.mfd.prc.pm.dto.TextValueStationsMappingDTO;
import com.ca.mfd.prc.pm.entity.PmWorkShopEntity;
import org.dom4j.Document;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @author luowenbing
 * @Description: PmInfo interface
 * @date 2023年4月4日
 * @变更说明 BY inkelink At 2023年4月4日
 */

public interface IPmService {

    /**
     * 获取一个实体
     *
     * @param shopCode shopCode
     * @param version  version
     * @return 返回一个实体
     */
    PmInfo getVersionShop(String shopCode, int version);

    /**
     * 导出
     *
     * @param shopId   工厂Id
     * @param response 请求
     * @throws IOException 异常
     */
    void export(Long shopId, HttpServletResponse response) throws IOException;

    /**
     * 获取导入模板
     *
     * @param response 请求
     * @throws IOException 异常
     */
    void getImportTemplate(HttpServletResponse response) throws IOException;

    /**
     * @param file 文件
     * @throws Exception 异常
     */
    void importExcel(InputStream file) throws Exception;

    /**
     * 获取渲染的树形结构
     *
     * @param id       主键
     * @param noteType 节点类型
     * @return 工厂树结果
     */
    List<TreeNode> getTreeNodes(Long id, String noteType);

    /**
     * 工厂节点
     *
     * @param shopId 工厂ID
     * @return 节点XML
     */
    Document generationShopModel(Long shopId);

    /**
     * 获取线体工位
     *
     * @param shopCode 工厂编码
     * @return 线体工位列表
     */
    List<TextValueStationsMappingDTO> getAreaStations(String shopCode);

    /**
     * 本地组件列表
     *
     * @param query 查询条件
     * @return 组件列表
     */
    List<ComponentDataDTO> getLocalComponent(String query);

    JSONObject generationJson(Long shopId);

    PmInfo getCurrentVersionShop(String shopCode);

    List<PmWorkShopEntity> getTree(String shopCode, int level);
}
