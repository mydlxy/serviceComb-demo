package com.ca.mfd.prc.core.main.service;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统配置
 *
 * @author inkelink ${email}
 * @date 2023-04-03
 */
public interface ISysConfigurationService
        extends ICrudService<SysConfigurationEntity> {

    /**
     * 获取Combo列表
     *
     * @return List<SysConfigurationDTO>列表
     */
    List<SysConfigurationEntity> getAllDatas();

    /**
     * 获取Combo列表
     *
     * @param category 分类key
     * @return List<ComboInfoDTO>列表
     */
    List<ComboInfoDTO> getComboDatasNoCache(String category);

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return List<SysConfigurationDTO>列表
     */
    List<SysConfigurationEntity> getSysConfigurations(String category);

    /**
     * 根据参数类型获取参数值
     *
     * @param key      关键字
     * @param category 类型
     * @return 返回text
     */
    String getConfiguration(String key, String category);

    /**
     * 获取Combo列表
     *
     * @param category 分类名称
     * @return 下拉集合模型
     */
    List<ComboInfoDTO> getComboDatas(String category);

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @return 下拉集合模型
     */
    List<ComboInfoDTO> getComboDatas(String category, String emptyText);

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @param isHide    是否显示
     * @return 下拉集合模型
     */
    List<ComboInfoDTO> getComboDatas(String category, String emptyText, Boolean isHide);

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     */
    Map<String, String> getSysConfigurationMaps(String category);

    /**
     * 根据参数类型获取参数值（val模糊查询）
     *
     * @param category 分类key
     * @param valLike  val模糊查询
     */
    List<SysConfigurationEntity> getSysConfigurationsLike(String category, String valLike);

    /**
     * 获取所有的app 应用
     *
     * @return
     */
    List<ComboInfoDTO> getAllAppId();

    /**
     * 根据服务器名称在配置中心获取服务地址
     *
     * @return
     */
    String getServerUrl(String serverName);

    /**
     * 据分类获取配置数据
     *
     * @param category
     * @return
     */
    List<ComboInfoDTO> getConfigurationByCategoryToList(String category);

    /**
     * 获取分类中的列表
     *
     * @param category
     * @return
     */
    List<ComboInfoDTO> getCategoryList(String category);

    /**
     * 远程获取参数
     *
     * @param key      关键字
     * @param category 分类名称
     * @return
     */
    String getRmoteConfiguration(String key, String category);

    /**
     * 分组获取所有的分类
     *
     * @return
     */
    List<ComboInfoDTO> getSysConfigurationsGroupBycategory();

    ResultVO updateBycategory(String value, String category, String text);

    /**
     * 获取分类中的列表
     *
     * @param category
     * @return 系统配置数据
     */
    List<SysConfigurationEntity> getCategoryDataList(String category);
}