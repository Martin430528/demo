package com.project.web.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.project.utils.MatrixToImageWriter;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Hashtable;

/**
 * 二维码
 *
 * @author GuoZhiLong
 * @date 2014年11月24日 上午10:59:35
 */
@Controller
@RequestMapping("/bitcode")
public class TwoBitCodeResource extends RestResource {

    /**
     * 生成图形验证码
     *
     * @param request
     * @param response
     * @param text      链接
     * @param widthStr  宽
     * @param heightStr 高
     * @return Object
     * @throws Exception
     * @throws
     * @author GuoZhiLong
     */
    @RequestMapping(value = "build")
    public Object build(HttpServletRequest request,
                        HttpServletResponse response, String text, String widthStr,
                        String heightStr) throws Exception {
        ServletOutputStream out = response.getOutputStream();
        int width = NumberUtils.toInt(widthStr, 300);
        int height = NumberUtils.toInt(heightStr, 300);
        // 二维码的图片格式
        String format = "gif";
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        // 内容所使用编码
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码
        MatrixToImageWriter.writeToStream(bitMatrix, format, out);
        try {
            out.flush();
        } finally {
            out.close();
        }
        return null;
    }

}
