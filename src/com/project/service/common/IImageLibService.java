package com.project.service.common;

import java.util.List;

import com.project.entity.common.ImageLib;

/**
 * 图库Service
 *
 * @author LiuDing 2014-6-5-下午06:21:08
 */
public interface IImageLibService {
    /**
     * 查询列表
     *
     * @param accountId
     * @param entiyType
     * @param dataId
     * @param DESC      true倒序
     * @return
     * @author LiuDing 2014-6-5 下午06:22:48
     * @max 条数 0不限制
     */
    public List<ImageLib> list(Integer accountId, Integer entiyType,
                               Integer dataId, boolean DESC, int max);

    /**
     * 新增
     *
     * @param @param  entity
     * @param @return
     * @return int
     * @throws
     * @author GuoZhiLong
     */
    public int addReturnID(ImageLib entity);

    List<ImageLib> queryByIdAndType(Integer id, Integer entiyType);

    List<ImageLib> queryByFkAndType(Integer foreignKey, Integer type, Integer start, Integer max);
    
    List<ImageLib> queryByFkAndType(String foreignKeyIds, Integer type, Integer start, Integer max);

    List<ImageLib> queryByType(Integer entiyType);

    boolean delete(Integer type);

    boolean saveOrUpdate(ImageLib imageLib);

    ImageLib queryById(Integer id);

    boolean deleteById(Integer id);

    /**
     * 根据外键ID、类型查询单张图片
     *
     * @param foreignKey
     * @param type
     * @return
     * @author GuoZhilong
     */
    ImageLib queryOneByFkAndType(Integer foreignKey, Integer type);

    void deleteByFkIdAndType(Integer fkId, Integer type);

    boolean addByPaths(String imgPaths, int fkid, int type)  throws RuntimeException;

}
