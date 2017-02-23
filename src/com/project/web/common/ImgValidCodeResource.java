package com.project.web.common;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.constants.IConstants;
import com.constants.SessionConstants;
import com.project.utils.VerifyCodeUtils;

/**
 * 图形验证码
 *
 * @author GuoZhiLong
 * @date 2014年11月24日 上午10:59:35
 */
@Controller
@RequestMapping("/ivcr")
public class ImgValidCodeResource extends RestResource {

    /**
     * 生成图形验证码
     *
     * @param request
     * @param response
     * @return Object
     * @throws Exception
     * @throws
     * @author GuoZhiLong
     */
    @RequestMapping(value = "bulidImgCode")
    public Object bulidImageValidCode(HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        String verifyCode = VerifyCodeUtils.generateVerifyCode(4).toUpperCase();
        request.getSession().setAttribute(
        		SessionConstants.IMAGE_CODE_SESSION_KEY, verifyCode);
        ServletOutputStream out = response.getOutputStream();
        VerifyCodeUtils.outputImage(160, 50, out, verifyCode);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

    /**
     * 校验验证码
     *
     * @param request
     * @param response
     * @param code
     * @return Object
     * @throws
     * @author GuoZhiLong
     */
    @RequestMapping(value = "checkImgCode")
    public Object checkImgValidCode(HttpServletRequest request,
                                    HttpServletResponse response, String code) {
        if (StringUtils.isBlank(code)) {
            return error(IConstants.PARAMS_ERROR);
        }
        String image_session_code = (String) request.getSession().getAttribute(
                SessionConstants.IMAGE_CODE_SESSION_KEY);
        if (!code.toUpperCase().equals(image_session_code)) {
            return error("验证码错误");
        } else {
            return success(IConstants.SUCCESS_MSG);
        }
    }
}
