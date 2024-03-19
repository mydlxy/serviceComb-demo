package com.ca.mfd.prc.core.main.controller;

import com.ca.mfd.prc.common.controller.BaseController;
import com.ca.mfd.prc.common.model.base.dto.ComboInfoDTO;
import com.ca.mfd.prc.common.utils.HttpContextUtils;
import com.ca.mfd.prc.common.utils.IdentityHelper;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.core.main.dto.SysConfigurationDTO;
import com.ca.mfd.prc.core.main.entity.SysConfigurationEntity;
import com.ca.mfd.prc.core.main.service.ISysConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 系统配置
 *
 * @author inkelink ${email}
 * @date 2023-04-04
 */
@RestController
@RequestMapping("main/sysconfiguration")
@Tag(name = "系统配置")
public class SysConfigurationController extends BaseController<SysConfigurationEntity> {

    private final ISysConfigurationService sysConfigurationService;
    @Autowired
    private IdentityHelper identityHelper;

    @Autowired
    public SysConfigurationController(ISysConfigurationService sysConfigurationService) {
        this.crudService = sysConfigurationService;
        this.sysConfigurationService = sysConfigurationService;
    }

    /**
     * 获取Combo列表
     *
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping(value = "/provider/getalldatas")
    @Operation(summary ="获取Combo列表")
    public ResultVO<List<SysConfigurationEntity>> getAllDatas() {
        ResultVO<List<SysConfigurationEntity>> result = new ResultVO<>();
        String susr =  identityHelper.getLoginName();
        List<SysConfigurationEntity> data = sysConfigurationService.getAllDatas();
        return result.ok(data);
    }

    /**
     * 获取Combo列表
     *
     * @param category 分类key
     * @return List<ComboInfoDTO>列表
     */
    @GetMapping(value = "/provider/getcombodatasnocache")
    @Operation(summary ="根据分类获取Combo列表")
    public ResultVO<List<ComboInfoDTO>> getComboDatasNoCache(String category) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getComboDatasNoCache(category);
        return result.ok(data);
    }

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     * @return List<SysConfigurationDTO>列表
     */
    @GetMapping(value = "/provider/getsysconfigurations")
    @Operation(summary ="根据分类获取非隐藏的Combo列表")
    public ResultVO<List<SysConfigurationEntity>> getSysConfigurations(String category) {
        ResultVO<List<SysConfigurationEntity>> result = new ResultVO<>();
        List<SysConfigurationEntity> data = sysConfigurationService.getSysConfigurations(category);
        return result.ok(data);
    }
//    @Autowired
//    IdentityHelper identityHelper;

    /**
     * 根据参数类型获取参数值
     *
     * @param key      关键字
     * @param category 类型
     * @return 返回text
     */
    @GetMapping(value = "/provider/getconfiguration")
    @Operation(summary ="根据关键字/类型获取的Combo列表")
    public ResultVO<String> getConfiguration(String key, String category) {
        ResultVO<String> result = new ResultVO<>();
        //String uname = identityHelper.getLoginName();
        String data = sysConfigurationService.getConfiguration(key, category);
        return result.ok(data);
    }

    /**
     * 获取Combo列表
     *
     * @param category 分类名称
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatas")
    @Operation(summary ="根据分类名称获取的Combo列表")
    public ResultVO<List<ComboInfoDTO>> getComboDatas(String category) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getComboDatas(category);
        return result.ok(data);
    }

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatasbyemptytext")
    @Operation(summary ="根据分类名称获取的Combo列表并设置默认值")
    public ResultVO<List<ComboInfoDTO>> getComboDatasByEmptyText(String category, String emptyText) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getComboDatas(category, emptyText);
        return result.ok(data);
    }

    /**
     * 获取Combo列表
     *
     * @param category  分类名称
     * @param emptyText 插入一个新key
     * @param isHide    是否显示
     * @return 下拉集合模型
     */
    @GetMapping(value = "/provider/getcombodatasbyishide")
    @Operation(summary ="根据分类名称/是否隐藏获取的Combo列表")
    public ResultVO<List<ComboInfoDTO>> getComboDatasByIsHide(String category, String emptyText, Boolean isHide) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getComboDatas(category, emptyText, isHide);
        return result.ok(data);
    }

    /**
     * 根据参数类型获取参数值
     *
     * @param category 分类key
     */
    @GetMapping(value = "/provider/getsysconfigurationmaps")
    @Operation(summary ="根据分类key获取的Combo列表")
    public ResultVO<Map<String, String>> getSysConfigurationMaps(String category) {
        ResultVO<Map<String, String>> result = new ResultVO<>();
        Map<String, String> data = sysConfigurationService.getSysConfigurationMaps(category);
        return result.ok(data);
    }

    /**
     * 根据参数类型获取参数值（val模糊查询）
     *
     * @param category 分类key
     * @param valLike  val模糊查询
     */
    @GetMapping(value = "/provider/getsysconfigurationslike")
    @Operation(summary ="根据分类key 模糊查询Combo列表")
    public ResultVO<List<SysConfigurationEntity>> getSysConfigurationsLike(String category, String valLike) {
        ResultVO<List<SysConfigurationEntity>> result = new ResultVO<>();
        List<SysConfigurationEntity> data = sysConfigurationService.getSysConfigurationsLike(category, valLike);
        return result.ok(data);
    }

    /**
     * 根据 whereEntity 条件，更新记录
     *
     * @param value
     * @param category
     * @param text
     * @return boolean
     */
    @PostMapping(value = "/provider/updatebycategory")
    @Operation(summary ="根据分类更新Combo列表")
    public ResultVO updateBycategory(String value, String category, String text) {
        return sysConfigurationService.updateBycategory(value, category, text);
    }

    /**
     * 获取所有的AppId(服务唯一标识)
     *
     * @return
     */
    @PostMapping(value = "/getallappid")
    @Operation(summary ="获取所有的AppId(服务唯一标识)")
    public ResultVO<List<ComboInfoDTO>> getAllAppId() {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getAllAppId();
        return result.ok(data);
    }

    /**
     * 根据服务器名称在配置中心获取服务地址
     *
     * @param serverName 服务器名称
     * @return 返回服务器地址
     */
    @GetMapping(value = "/getserverurl")
    @Operation(summary ="根据服务器名称在配置中心获取服务地址")
    public ResultVO<String> getServerUrl(String serverName) {
        ResultVO<String> result = new ResultVO<>();
        String data = sysConfigurationService.getServerUrl(serverName);
        return result.ok(data);
    }

    /**
     * 根据分类获取配置数据
     *
     * @param category 分类名称
     * @return 返回数据
     */
    @GetMapping(value = "/getconfigurationbycategorytolist")
    @Operation(summary ="根据分类获取配置数据")
    public ResultVO<List<ComboInfoDTO>> getConfigurationByCategoryToList(String category) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getConfigurationByCategoryToList(category);
        return result.ok(data);
    }

    /**
     * 获取分类中的列表
     *
     * @param category 分类名称
     * @return 返回数据
     */
    @GetMapping(value = "/getcategorylist")
    @Operation(summary ="获取分类中的列表")
    public ResultVO<List<ComboInfoDTO>> getCategoryList(String category) {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getCategoryList(category);
        return result.ok(data);
    }

    /**
     * 远程获取参数
     *
     * @param key      关键字
     * @param category 分类名称
     * @return 返回text
     */
    @GetMapping(value = "/getrmoteconfigurationbykey")
    @Operation(summary ="远程获取参数")
    public ResultVO<String> getRmoteConfiguration(String key, String category) {
        ResultVO<String> result = new ResultVO<>();
        String data = sysConfigurationService.getRmoteConfiguration(key, category);
        return result.ok(data);
    }

    /**
     * 分组获取所有的分类数据
     *
     * @return
     */
    @GetMapping(value = "/getsysconfigurationsgroupbycategory")
    @Operation(summary ="分组获取所有的分类数据")
    public ResultVO<List<ComboInfoDTO>> getSysConfigurationsGroupBycategory() {
        ResultVO<List<ComboInfoDTO>> result = new ResultVO<>();
        List<ComboInfoDTO> data = sysConfigurationService.getSysConfigurationsGroupBycategory();
        return result.ok(data);
    }

    @PostMapping(value = "getsysconfigurations")
    @Operation(summary = "根据参数类型获取参数值")
    public ResultVO GetSysConfigurations(@RequestBody SysConfigurationDTO.GetModel model) throws UnsupportedEncodingException {

        if (model.getCategory() == null) {
            return new ResultVO().ok("","操作成功");
        }
        List<SysConfigurationEntity> all = sysConfigurationService.getAllDatas();
        Map<String, List<SysConfigurationEntity>> data = new HashMap<>(model.getCategory().size());
        for (String item : model.getCategory().stream().distinct().collect(Collectors.toList())) {
            String token = identityHelper.getHeaderToken(HttpContextUtils.getHttpServletRequest());
            if (!StringUtils.isBlank(identityHelper.getToken()) || StringUtils.isNotBlank(token)) {
                List<SysConfigurationEntity> cats = all.stream().filter(w ->
                                StringUtils.equals(w.getCategory().toLowerCase(),
                                        item.toLowerCase()) && !w.getIsHide())
                        .collect(Collectors.toList());
                cats.sort(Comparator.comparing(SysConfigurationEntity::getDisplayNo));
                data.put(URLEncoder.encode(item, "UTF-8"), cats);
            } else {
                List<String> category = all.stream().filter(s -> s.getCategory().equals("NotAuthSystemConfig")).map(e -> e.getText().toLowerCase()).collect(Collectors.toList());
                if (category.size() > 0) {
                    if (category.contains(item.toLowerCase())) {
                        List<SysConfigurationEntity> cats = all.stream().filter(w ->
                                        StringUtils.equals(w.getCategory().toLowerCase(),
                                                item.toLowerCase()) && !w.getIsHide())
                                .collect(Collectors.toList());
                        cats.sort(Comparator.comparing(SysConfigurationEntity::getDisplayNo));
                        data.put(URLEncoder.encode(item, "UTF-8"), cats);
                    }
                }
            }
        }
        ResultVO<Map<String, List<SysConfigurationEntity>>> result = new ResultVO<>();
        return result.ok(data);
    }

    /**
     * 获取分类中的列表
     */
    @GetMapping(value = "/getcategorydatalist")
    @Operation(summary = "获取分类中的列表")
    public ResultVO getCategoryDataList(String category) {
        ResultVO<List<SysConfigurationEntity>> result = new ResultVO<>();
        List<SysConfigurationEntity> data = sysConfigurationService.getCategoryDataList(category);
        return result.ok(data);
    }

    @PostMapping(value = "/provider/insertbath")
    @Operation(summary = "批量保存")
    public void _insertBath(@RequestBody List<SysConfigurationEntity> entitys) {
        sysConfigurationService.insertBatch(entitys,200,false,1);
        sysConfigurationService.saveChange();
    }
}