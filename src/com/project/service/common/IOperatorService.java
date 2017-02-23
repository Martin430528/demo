package com.project.service.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.project.entity.common.Operator;

/**
 * 常用操作信息
 *
 * @author GuoZhiLong
 * @date 2016年1月5日 上午11:44:35
 */
public interface IOperatorService {

    /**
     * 设置当前用户操作信息
     *
     * @param request
     * @param userId
     * @return void
     * @author GuoZhiLong
     */
    boolean setOperator(HttpServletRequest request, int userId);

    /**
     * 设置登录信息 并保存cookie
     *
     * @param request
     * @param response
     * @param userId
     * @param day
     * @author GuoZhiLong
     */
    void setOperator(HttpServletRequest request, HttpServletResponse response,
                     int userId, int day);

    /**
     * 获取当前用户操作信息
     *
     * @param request
     * @return Operator
     * @author GuoZhiLong
     * @author GuoZhiLong
     */
    Operator getOperator(HttpServletRequest request);

    /**
     * 退出登陆
     *
     * @param request
     * @param response
     * @author GuoZhiLong
     */
    void logout(HttpServletRequest request, HttpServletResponse response);
}
