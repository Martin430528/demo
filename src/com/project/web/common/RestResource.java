package com.project.web.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.constants.IConstants;

public abstract class RestResource {

    /**
     * 返回请求参数
     *
     * @param request
     */
    protected Map<String, Object> returnPara(HttpServletRequest request) {
        Map<String, String[]> reqMap = request.getParameterMap();
        if (MapUtils.isEmpty(reqMap)) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Map.Entry<String, String[]> entry : reqMap.entrySet()) {
            String[] strarr = entry.getValue();
            if (ArrayUtils.isEmpty(strarr)) {
                continue;
            }
            if (strarr.length > 1) {
                resultMap.put(entry.getKey(), strarr);
                request.setAttribute(entry.getKey(), entry.getValue());
            } else {
                resultMap.put(entry.getKey(), strarr[0]);
                request.setAttribute(entry.getKey(), strarr[0]);
            }
        }
        return resultMap;
    }
    
    /**返回JSON数据
     * @param response
     * @param code
     * @param msg
     * @return
     */
    protected Map<String, Object> result(Integer code, String msg){
    	Map<String, Object> map = new HashMap<>();
    	map.put("errcode", code);
    	map.put("errmsg", msg);
    	return map;
    }
    
    protected Map<String, Object> success(String msg){
    	return result(IConstants.OK, msg);
    }

    protected Map<String, Object> error(String msg){
    	return result(IConstants.FAIL, msg);
    }
    
    protected Map<String, Object> data(Object data){
    	Map<String, Object> map = new HashMap<>();
    	map.put("errcode", IConstants.OK);
    	map.put("data", data);
    	return map;
    }
    
    protected Map<String, Object> successList(Object list, Integer pageSize, Integer page, Integer totalCount, Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", IConstants.OK);
        map.put("list", list);
        map.put("pageSize", pageSize);
        map.put("page", page);
        Integer totalPage = null;
        if (pageSize != null && pageSize > 0 && totalCount != null) {
            totalPage = (totalCount + pageSize - 1) / pageSize;
        }
        map.put("totalPage", totalPage);
        if(params != null && params.size() > 0){
        	for(Entry<String, Object> entry : params.entrySet()){
        		String key = entry.getKey();
        		map.put(key, entry.getValue());
        	}
        }
        return map;
    }
    
}
