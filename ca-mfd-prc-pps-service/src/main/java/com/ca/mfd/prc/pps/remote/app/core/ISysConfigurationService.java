package com.ca.mfd.prc.pps.remote.app.core;

import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pps.remote.app.core.sys.entity.SysConfigurationEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * ISysConfigurationService
 *
 * @author inkelink eric.zhou
 * @since 1.0.0 2023-04-17
 */
@FeignClient(name = "ca-mfd-prc-core-service", path = "main/sysconfiguration", contextId = "inkelink-core-sysconfiguration")
public interface ISysConfigurationService {
    /**
     * 获取Combo列表
     *
     * @return 查询列表
     */
    @GetMapping(value = "/provider/getalldatas")
    ResultVO<List<SysConfigurationEntity>> getAllDatas();


    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return 查询列表
     */
    @GetMapping(value = "/provider/getsysconfigurations")
    ResultVO<List<SysConfigurationEntity>> getSysConfigurations(@RequestParam("category") String category);

    /**
     * 根据参数类型获取参数值
     *
     * @param key      关键字
     * @param category 类型
     * @return 返回text
     */
    @GetMapping(value = "/provider/getconfiguration")
    ResultVO<String> getConfiguration(@RequestParam("key") String key, @RequestParam("category") String category);

    /**
     * 获取Combo列表
     *
     * @param category 分类名称
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatas")
    ResultVO<List<ComboInfoDTO>> getComboDatas(@RequestParam("category") String category);

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatasbyemptytext")
    ResultVO<List<ComboInfoDTO>> getComboDatasByEmptyText(@RequestParam("category") String category, @RequestParam("emptyText") String emptyText);

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @param isHide    是否显示
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatasbyishide")
    ResultVO<List<ComboInfoDTO>> getComboDatasByIsHide(@RequestParam("category") String category, @RequestParam("emptyText") String emptyText, @RequestParam("isHide") Boolean isHide);

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return
     */
    @GetMapping(value = "/provider/getsysconfigurationmaps")
    ResultVO<Map<String, String>> getSysConfigurationMaps(@RequestParam("category") String category);

    /**
     * 根据参数类型获取参数值（val模糊查询）
     *
     * @param category 分类key
     * @param valLike  val模糊查询
     * @return
     */
    @GetMapping(value = "/provider/getsysconfigurationslike")
    ResultVO<List<SysConfigurationEntity>> getSysConfigurationsLike(@RequestParam("category") String category, @RequestParam("valLike") String valLike);

    @Component
    class SysConfigurationServiceFallbackFactory implements FallbackFactory<ISysConfigurationService> {
        private static final Logger logger = LoggerFactory.getLogger(ISysConfigurationService.SysConfigurationServiceFallbackFactory.class);

        @Override
        public ISysConfigurationService create(Throwable cause) {
            logger.error("inkelink-core-sysconfiguration feign request error:" + cause.getMessage(), cause);
            return new ISysConfigurationService() {
                /**
                 * 获取Combo列表
                 *
                 * @return 查询列表
                 */
                @Override
                public ResultVO<List<SysConfigurationEntity>> getAllDatas() {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 根据参数类型获取参数值
                 *
                 * @param category 分类key
                 * @return 查询列表
                 */
                @Override
                public ResultVO<List<SysConfigurationEntity>> getSysConfigurations(String category) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 根据参数类型获取参数值
                 *
                 * @param key      关键字
                 * @param category 类型
                 * @return 返回text
                 */
                @Override
                public ResultVO<String> getConfiguration(String key, String category) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 获取Combo列表
                 *
                 * @param category 分类名称
                 * @return 下拉集合模型
                 */
                @Override
                public ResultVO<List<ComboInfoDTO>> getComboDatas(String category) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 获取Combo列表
                 *
                 * @param category  分类名称
                 * @param emptyText 插入一个新key
                 * @return 下拉集合模型
                 */
                @Override
                public ResultVO<List<ComboInfoDTO>> getComboDatasByEmptyText(String category, String emptyText) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 获取Combo列表
                 *
                 * @param category  分类名称
                 * @param emptyText 插入一个新key
                 * @param isHide    是否显示
                 * @return 下拉集合模型
                 */
                @Override
                public ResultVO<List<ComboInfoDTO>> getComboDatasByIsHide(String category, String emptyText, Boolean isHide) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 根据参数类型获取参数值
                 *
                 * @param category 分类key
                 */
                @Override
                public ResultVO<Map<String, String>> getSysConfigurationMaps(String category) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }

                /**
                 * 根据参数类型获取参数值（val模糊查询）
                 *
                 * @param category 分类key
                 * @param valLike  val模糊查询
                 */
                @Override
                public ResultVO<List<SysConfigurationEntity>> getSysConfigurationsLike(String category, String valLike) {
                    return new ResultVO().error("调用失败：" + cause.getMessage());
                }


            };
        }
    }
}
