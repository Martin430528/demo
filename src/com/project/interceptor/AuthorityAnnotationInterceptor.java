package com.project.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.project.authority.Authority;
import com.project.entity.admin.SysMenu;
import com.project.entity.admin.SysPurview;
import com.project.entity.admin.SysUserRole;
import com.project.entity.common.Operator;
import com.project.exception.NoPermissionException;
import com.project.exception.SessionInvalidException;
import com.project.service.admin.ISysMenuService;
import com.project.service.admin.ISysPurviewService;
import com.project.service.common.IOperatorService;

/**
 * 控制权限的interceptor类,用于过滤需要控制权限的方法.
 *
 * @author GuoZhiLong
 * @date 2015年5月19日 上午10:59:18
 */
public class AuthorityAnnotationInterceptor extends HandlerInterceptorAdapter {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ISysMenuService sysMenuService;// 系统菜单

    @Autowired
    private ISysPurviewService sysPurviewService;// 角色对应的权限(菜单)
    @Autowired
    private IOperatorService operatorService;

    /**
     * 预处理回调方法(true表示继续流程 false表示流程中断)
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        response.reset();
        logger.info("request.getRequestURI()-----:" + request.getRequestURI());
        // 拦截到静态文件时不需要过滤直接返回true
        if (handler instanceof HandlerMethod) {
            // session超时跳转登录
            Operator operator = operatorService.getOperator(request);
            if (operator == null) {
            	throw new SessionInvalidException("登录超时");
            }
            Authority authority = ((HandlerMethod) handler).getMethodAnnotation(Authority.class);
            // 当自定义的注解为空或者validate==false时跳过验证
            if (operator.getMark() == 1 || authority == null || authority.validate() == false) {
                return true;
            } else {
                // 判断当前用户是否有对应的角色信息
                if (CollectionUtils.isEmpty(operator.getSysUserRoleList())) {
                    throw new NoPermissionException("无操作权限");
                }
                // 判断当前请求的菜单在系统中是否存在
                System.out.println(authority.purviewVal());
                SysMenu sysMenu = sysMenuService.queryByMenuCode(authority.purviewVal());
                if (sysMenu == null) {
                	throw new NoPermissionException("无操作权限");
                }
                List<SysUserRole> sysUserRoleList = operator.getSysUserRoleList();
                boolean flag = true;
                for (SysUserRole sysUserRole : sysUserRoleList) {
                    String menuIds = "";
                    SysPurview sysPurview = sysPurviewService.queryByRoleId(sysUserRole.getSysRoleId());
                    if (sysPurview == null) {
                        flag = false;
                        break;
                    }
                    menuIds = sysPurview.getMenuIds();
                    if (!("," + menuIds + ",").contains("," + sysMenu.getId() + ",")) {
                        flag = false;
                    }
                }
                if (!flag) {
                	throw new NoPermissionException("无操作权限");
				}
                return true;
            }
        } else {
            return true;
        }
    }

    /**
     * 整个请求处理完毕回调方法
     */
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }

    /**
     * 后处理回调方法
     */
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        String contextPath = request.getContextPath();
        if (modelAndView != null) {
            request.setAttribute("base", contextPath);
        }
    }

}
