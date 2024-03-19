package com.ca.mfd.prc.pm.service;

import com.ca.mfd.prc.common.service.ICrudService;
import com.ca.mfd.prc.common.utils.ResultVO;
import com.ca.mfd.prc.pm.dto.OtDto;
import com.ca.mfd.prc.pm.dto.PmAllDTO;
import com.ca.mfd.prc.pm.dto.PmVersionDTO;
import com.ca.mfd.prc.pm.entity.PmVersionEntity;
import com.ca.mfd.prc.pm.entity.PmWorkStationEntity;
import org.dom4j.Document;

import javax.naming.LinkException;
import java.util.List;

/**
 * @author inkelink ${email}
 * @Description: 建模版本控制
 * @date 2023-08-29
 */
public interface IPmVersionService extends ICrudService<PmVersionEntity> {

    /**
     * 查询一个实体
     *
     * @return 返回一个实体
     */
    PmAllDTO getObjectedPm();

    /**
     * 根据工位编号获取关联的QG岗位信息
     *
     * @param stationId
     * @return List<String> 2个
     */
    List<String> getRelevanceQgWorkplaceByStationId(String stationId);

    /**
     * 查询一个列表
     *
     * @return 返回一个列表
     */
    List<PmVersionEntity> getCurrentVersions();

    /**
     * 查询整个工厂对应节点的所有数据
     *
     * @param className 转换类型
     * @param <T>       泛型参数
     * @return 返回一个查询列表
     */
    <T> List<T> getAllElements(Class<T> className);

    /**
     * 测试
     */
    void test();

    /**
     * 获取当前工厂建模
     *
     * @return 当前工厂建模
     */
    Document getCurretPm();

    /**
     * 发布工厂建模
     *
     * @param model 参数
     * @return 结果
     */
    ResultVO publishShopPm(PmVersionDTO model);

    /**
     * 启用工厂建模
     *
     * @param shopId
     * @return 结果
     */
    ResultVO enableShopVersion(Long shopId) throws LinkException;

    /**
     * 重置
     *
     * @param shopCode 车间编码
     * @return 操作结果
     */
    ResultVO restDownConfig(String shopCode);

    /**
     * 版本对比
     *
     * @return 结果列表
     */
    ResultVO getPmDifference(String ids);

    /**
     * 获取Combo
     *
     * @param nodeName 节点名称
     * @param parentId 上级ID
     * @return 操作结果
     */
    ResultVO getPmCombo(String nodeName, String parentId);

    /**
     * 当前车间Combo
     *
     * @param shopCode 车间编码
     * @param nodeName 节点名称
     * @param parentId 上级ID
     * @return 当前车间
     */
    ResultVO getPmShopCombo(String shopCode, String nodeName, String parentId);

    /**
     * 获取车间Combo
     *
     * @return 返回结果
     */
    ResultVO getShopCombo();

    /**
     * 获取全部车间Combo
     *
     * @return 全部车间Combo
     */
    ResultVO getAllShopCombo();

    /**
     * 当前线体车间CODE
     *
     * @param shopCode 车间code
     * @return 线体
     */
    ResultVO getAreaPosition(String shopCode);

    /**
     * 岗位Combo
     *
     * @param shopCode    车间代码
     * @param stationType 工位类型
     * @return 工位Combo
     */
    ResultVO getWorkStationCombo(String shopCode, Integer stationType);

    /**
     * 获取全部岗位Combo
     *
     * @param stationType 工位分类
     * @return 全部工位Combo
     */
    ResultVO geWorkStationAllCombo(Integer stationType);

    /**
     * 查询岗位Combo
     *
     * @param lineId          线体id
     * @param workStationType 岗位分类
     * @return 实体
     */
    ResultVO getWorkStationComboByLineId(Long lineId, Integer workStationType);


    /**
     * 查询岗位Combo
     *
     * @param lineCode        线体code
     * @param workStationType 岗位分类(可空)
     * @return 实体
     */
    ResultVO getWorkStationByLineCode(String lineCode, Integer workStationType);

    /**
     * 全部分类PM
     *
     * @return 全部数据
     */
    ResultVO getAllObjectPm();

    /**
     * 查询树形数据
     *
     * @param id        ID
     * @param shopCode  工厂编码
     * @param nodeTypes 节点分类
     * @return 树形数据
     */
    ResultVO getTreeData(Long id, String shopCode, String nodeTypes);

    /**
     * 标志andon参数下载完成
     *
     * @param code 编码
     */
    void setDownAndonCommplate(String code);

    /**
     * 判断andon参数是否存在下发的版本
     *
     * @param code 编码
     * @return 是否存在下发的版本
     */
    boolean isExeistsAndonPmVersion(String code);

    ResultVO getPreview(Long id);

    /**
     * 获取ot层级列表
     *
     * @param workshopCode
     * @param workstationType
     * @return
     */
    ResultVO<OtDto> getOtDatas(String workshopCode, String workstationType);

    /**
     * 同步到通用工厂
     * @param versionId
     * @param syncEquipment 1:同步设备，否则不同步设备，默认不同步设备
     * @return
     */
    ResultVO<Object> syncCommFactory(Long versionId,int syncEquipment);

    /**
     *
     * @param workstationCode
     * @return
     */
    List<PmWorkStationEntity> getRelevanceQgWorkplaceByStation(String workstationCode);

    /**
     * 通用工厂补偿方法
     * @param type
     */
    void syncMonToCmc(int type);
}