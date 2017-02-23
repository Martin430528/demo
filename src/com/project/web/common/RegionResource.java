package com.project.web.common;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.constants.IConstants;
import com.project.entity.common.Region;
import com.project.service.region.IRegionService;

/**
 * 全国省市区
 *
 * @author LiuDing 2014-6-21-下午11:18:25
 */
@Controller
@RequestMapping("/region")
public class RegionResource extends RestResource {
    @Autowired
    private IRegionService regionService;

    /**
     * 查询省市数据
     *
     * @param request
     * @param response
     * @param pid      //省pid=1
     * @return
     * @author LiuDing 2014-6-21 下午11:23:20
     */
    @ResponseBody
    @RequestMapping(value = "/queryByState")
    public Map<String, Object> queryByState(HttpServletRequest request,
                               HttpServletResponse response, String pid) {
        if (!NumberUtils.isDigits(pid)) {
            return error(IConstants.PARAMS_ERROR);
        }
        List<Object[]> list = regionService.queryByParentId(request, pid);
        return data(list);
    }

    @ResponseBody
    @RequestMapping(value = "/query")
    public Map<String, Object> query(HttpServletRequest request,
                        HttpServletResponse response, String pid) {
        if (!NumberUtils.isDigits(pid)) {
            return error(IConstants.PARAMS_ERROR);
        }
        List<Object[]> list = regionService.queryByParentIdAndState(request, pid);
        return data(list);
    }
    
    /*修改了上面的方法*/
    @ResponseBody
    @RequestMapping(value = "/queryMap")
    public Map<String, Object> queryMap(HttpServletRequest request,
                        HttpServletResponse response, String pid) {
    	if (!NumberUtils.isDigits(pid)) {
            return error(IConstants.PARAMS_ERROR);
        }
        if(!NumberUtils.isDigits(pid) || "0".equals(pid)){
        	pid = "1";
        }
        List<Region> list = regionService.queryByParentIdMap(request, pid);
        return data(list);
    }

    /**
     * 如果不存在列则创建
     *
     * @param columnName
     * @param exesql
     */
    void addColumnIfNotExist(String columnName, String exesql) {
        regionService.createRegion(exesql);
    }

    /**
     * 设置/取消
     *
     * @param request
     * @param response
     * @return Object
     * @throws
     * @author GuoZhiLong
     */
    @ResponseBody
    @RequestMapping(value = "/ajaxModify")
    public Map<String, Object> ajaxModify(HttpServletRequest request,
                             HttpServletResponse response) {
    	Integer opType = NumberUtils.toInt(request.getParameter("opType"),
                -1);
        String idArr = request.getParameter("idArr");
        idArr = idArr.replace("[", "").replace("]", "");
        if (opType == -1 || StringUtils.isBlank(idArr)) {
            return error(IConstants.PARAMS_ERROR);
        }
        boolean res = false;
        res = regionService.modifyStateByIds(opType, idArr);

        if (opType == 0) {
            String provRegion = request.getParameter("provRegion");// 省ID
            String cityRegion = request.getParameter("cityRegion");// 市ID
            if (StringUtils.isBlank(provRegion)
                    || StringUtils.isBlank(cityRegion)) {
                return error(IConstants.PARAMS_ERROR);
            }
            res = regionService.queryAndUpdate(res, provRegion, cityRegion);
        }
        if (res) {
            return success(IConstants.SUCCESS_MSG);
        } else {
            return error(IConstants.FAILURE_MSG);
        }
    }

}
