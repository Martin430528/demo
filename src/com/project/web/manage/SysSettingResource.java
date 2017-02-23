package com.project.web.manage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.constants.IConstants;
import com.project.entity.common.Config;
import com.project.service.common.ICommonService;
import com.project.web.common.RestResource;

/**设置
 * @author LiMin
 * 2016年6月28日
 */
@Controller
@RequestMapping("/sys/setting")
public class SysSettingResource extends RestResource {
	
	private String PATH = "/manage/setting/";
	
	@Autowired
	private ICommonService commonService;
	
	@RequestMapping("param")
	public String edit(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("@configKey", "logo,serviceContent,serviceQq,qrcode,copyright,stockWarning");
		@SuppressWarnings("unchecked")
		List<Config> configList = commonService.queryByHql(Config.class, params);
		if(CollectionUtils.isNotEmpty(configList)){
			Map<String, Object> configMap = new HashMap<String, Object>();
			request.setAttribute("configMap", configMap);
			for (Config config : configList) {
				configMap.put(config.getConfigKey(), config);
			}
		}
		return PATH.concat("settings");
	}
	
	@ResponseBody
	@RequestMapping("save")
	public Map<String, Object> save(HttpServletRequest request, HttpServletResponse response){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("@configKey", "logo,serviceContent,serviceQq,qrcode,copyright,stockWarning");
		@SuppressWarnings("unchecked")
		List<Config> configList = commonService.queryByHql(Config.class, params);
		
		String datas = request.getParameter("datas");
		JSONObject json = JSONObject.parseObject(datas);
		Set<String> set = json.keySet();
		for(String key : set){
			if(!key.contains("configValue")){
				continue;
			}
			String[] keys = key.split("_");
			String configKey = keys[1];
			String configValue = json.getString(key);
			if(CollectionUtils.isNotEmpty(configList)){
				for(Config config : configList){
					if(config.getConfigKey().equals(configKey)){
						config.setConfigValue(configValue);
					}
				}
			}
		}
		boolean flag = commonService.saveOrUpdateByList(Config.class, configList);
		if(flag){
			return success(IConstants.SUCCESS_MSG);
		}else{
			return error(IConstants.FAILURE_MSG);
		}
	}

}
