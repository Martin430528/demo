package com.project.service.region;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.project.entity.common.Region;


/**
 * 省市区信息服务接口
 * Created by GuoZhilong on 2016/2/17.
 */
public interface IRegionService {
    void createRegion(String exesql);

    List<Object[]> queryAll();

    boolean modifyStateByIds(Integer opType, String idArr);

    boolean queryAndUpdate(boolean res, String provRegion,
                           String cityRegion);

    List<Object[]> queryByParentId(HttpServletRequest request,
                                   String pid);

    public List<Object[]> queryByParentIdAndState(HttpServletRequest request,String pid);
    public List<Region> queryByParentIdMap(HttpServletRequest request,String pid);
    Region queryByCodeAndState(String code, Integer state);

}
