package com.project.service.admin;

import javax.servlet.http.HttpServletRequest;

import com.project.entity.admin.SysManager;
import com.project.entity.common.Operator;
import com.project.service.common.ICommonService;

/**
 * 管理员service
 *
 * @author GeRuzhen
 */
public interface ISysManagerService extends ICommonService {

    /**
     * 获取登录者id
     *
     * @param request
     * @return
     */
    public Integer getLoginId(HttpServletRequest request);

    /**
     * 判断是否登录
     *
     * @param request
     * @return
     * @author GeRuzhen
     */
    public boolean isLogin(HttpServletRequest request);

    /**
     * 登录标记
     *
     * @param request
     * @param operator
     * @author GeRuzhen
     */
    public void markLogin(HttpServletRequest request, Operator operator);

    /**
     * 退出登录
     *
     * @param request
     * @author GeRuzhen
     */
    public void logout(HttpServletRequest request);

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @author GeRuzhen
     */
    public SysManager login(String userName, String password);

    /**
     * 用户名查询
     *
     * @param userName
     * @return
     * @author GeRuzhen
     */
    SysManager queryByUserName(String userName);

    boolean customSave(SysManager userManager);
    boolean customUpdate(SysManager userManager);
    public boolean deleteBySql(String sql);
}
