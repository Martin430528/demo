package com.project.timetask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.project.service.common.ICommonService;

/**静态分红定时任务
 * @author LiMin
 * 2016年6月2日
 */
@Component
public class GoodsTask {

	@Autowired
	private ICommonService commonService;
	
	/**
	 * 每月最后一天23:59:59，商品月销量清零
	 */
	@Scheduled(cron = "1 0 0 0/1 * ?")
	public void calcSales(){
		System.out.println("------------月销量清零任务开启--------------");
		String sql = "update t_goods set monthlySales=0";
		commonService.executeSQL(sql);
		System.out.println("------------月销量清零任务结束--------------");
	}
	
}
