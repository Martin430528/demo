package com.project.service.region.impl;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Service;

import com.project.core.proxy.ProxyFactory;
import com.project.entity.common.Region;
import com.project.service.region.IRegionService;

@Service("regionService")
public class RegionServiceImpl implements IRegionService {

    public void createRegion(String exesql) {
        try {
            String sql = "select COLUMN_NAME from information_schema.COLUMNS where table_name = 'region' and COLUMN_NAME='REGION_STATE' ";
            Object obj = ProxyFactory.baseService.findOneReturn(sql);
            if (null == obj) { // 添加列
                if (null == exesql)
                    exesql = "ALTER TABLE region ADD COLUMN REGION_STATE int(4)";
                ProxyFactory.baseService.executeSql(exesql);
            }
        } catch (RuntimeException e) {
            throw e;
        }

    }

    @SuppressWarnings("unchecked")
	public List<Object[]> queryAll() {
        try {
            String sql = "select r.REGION_ID,r.PARENT_ID,r.REGION_NAME, r.REGION_STATE,r.REGION_CODE from region r where r.REGION_ID>1";
            return ProxyFactory.baseService.findByList(sql, StringUtils.EMPTY);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean modifyStateByIds(Integer opType, String idArr) {
        try {
            String sql = "update region set REGION_STATE=" + opType
                    + " where REGION_ID in(" + idArr + ")";
            return ProxyFactory.baseService.update(sql);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public boolean queryAndUpdate(boolean res, String provRegion,
                                  String cityRegion) {
        try {

            Object[] objects = null;
            String area_sql = "select * from region where PARENT_ID="
                    + cityRegion + " and REGION_STATE=1 limit 0,1";
            objects = ProxyFactory.baseService.findByOne(area_sql);
            if (objects == null || objects.length == 0) {
                String cityRegion_sql = "update region set REGION_STATE = (select count(t.REGION_ID) from (select * from region) as t where t.PARENT_ID = "
                        + cityRegion
                        + " and t.REGION_STATE=1 limit 1) where REGION_ID="
                        + cityRegion + "";
                res = ProxyFactory.baseService.update(cityRegion_sql);
                String provRegion_sql = "update region set REGION_STATE = (select count(t.REGION_ID) from (select * from region) as t where t.PARENT_ID = "
                        + provRegion
                        + " and t.REGION_STATE=1 limit 1) where REGION_ID="
                        + provRegion + "";
                res = ProxyFactory.baseService.update(provRegion_sql);
            }
            return res;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
	public List<Object[]> queryByParentId(HttpServletRequest request, String pid) {
        try {

            String sql = "select r.REGION_ID, r.REGION_NAME, r.PARENT_ID, r.REGION_CODE,r.REGION_STATE from region r where r.PARENT_ID = ? ";
            if (NumberUtils.isDigits(request.getParameter("state"))) {
                sql += " and r.REGION_STATE=1";
            }
            List<Object[]> list = ProxyFactory.baseService.findByList(sql,
                    Integer.valueOf(pid));
            return list;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
	public List<Object[]> queryByParentIdAndState(HttpServletRequest request,
                                                  String pid) {
        try {

            String sql = "select r.REGION_ID, r.REGION_NAME, r.PARENT_ID, r.REGION_CODE,r.REGION_STATE from region r where r.PARENT_ID = ? ";
            if (!NumberUtils.isDigits(request.getParameter("state"))) {
                sql = "select r.REGION_ID, r.REGION_NAME, r.PARENT_ID, r.REGION_CODE from region r where r.PARENT_ID = ? ";
            }
            if (NumberUtils.isDigits(request.getParameter("state"))) {
                sql += " and r.REGION_STATE=1";
            }
            List<Object[]> list = ProxyFactory.baseService.findByList(sql,
                    Integer.valueOf(pid));
            return list;
        } catch (RuntimeException e) {
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
	public List<Region> queryByParentIdMap(HttpServletRequest request,String pid) {
        try {

            String sql = "select * from region r where r.PARENT_ID = ? ";
            if (!NumberUtils.isDigits(request.getParameter("state"))) {
                sql = "select * from region r where r.PARENT_ID = ? ";
            }
            if (NumberUtils.isDigits(request.getParameter("state"))) {
                sql += " and r.REGION_STATE=1";
            }
            List<Object> params = new ArrayList<Object>();
            params.add(Integer.parseInt(pid));
            
            List<Region> list = ProxyFactory.baseService.findByListObject(Region.class, sql, params);
            return list;
        } catch (RuntimeException e) {
            throw e;
        }
    }
    
    public Region queryByIdMap(HttpServletRequest request,Integer id) {
        try {
        	Region r = (Region)ProxyFactory.baseService.findById(Region.class.getName(), id);
            return r;
        } catch (RuntimeException e) {
            throw e;
        }
    }
    
    public Region queryByCodeAndState(String code, Integer state) {
        String hql = "from Region where REGION_CODE=? and REGION_STATE=?";
        return (Region) ProxyFactory.baseService.findObjectByHql(Region.class, hql, code, state);
    }

}
