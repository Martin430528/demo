package com.project.web.common;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.alibaba.fastjson.JSONException;
import com.project.utils.FileTools;
import com.project.utils.ImageTools;

import net.coobird.thumbnailator.Thumbnails;

/**
 * 文件上传
 *
 * @author liuding
 * @create Aug 26, 2013 4:08:37 PM
 */
@Controller
@RequestMapping("/upload")
public class UploadResource extends RestResource {

    /**
     * 通过kindeditor上传文件
     *
     * @param request
     * @param response
     * @param maxWidth  图片最大宽度，等比缩放
     * @param maxHeight 图片最大高度，等比缩放
     * @return
     * @throws JSONException
     * @author LiuDing 2014-4-3 上午12:45:42
     * @throws IOException 
     * @throws IllegalStateException 
     */
    @ResponseBody
    @RequestMapping(value = "/kindeditorJson")
    public Map<String, Object> kindeditorJson(HttpServletRequest request,
                                 HttpServletResponse response, String maxWidth, String maxHeight) throws JSONException, IllegalStateException, IOException {
            // img文件尺寸限制，默认最大800px
            int defaultSize = 800;
            int iwidth = NumberUtils.isDigits(maxWidth) ? Integer
                    .parseInt(maxWidth) : defaultSize;
            int iheight = NumberUtils.isDigits(maxHeight) ? Integer
                    .parseInt(maxHeight) : 4000;
            // 获取文件
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest) request;
            MultipartFile file = null;
            Iterator<String> fileNames = mreq.getFileNames();
            String fileName = null;
            while (fileNames.hasNext()) {
                fileName = fileNames.next();
                file = mreq.getFile(fileName);
            }
            if (null == file) {
                return error("没有文件！");
            }
            fileName = file.getOriginalFilename();
            // 保存
            Date date = new Date();
            String realpath = request.getSession().getServletContext()
                    .getRealPath("/attachment/kindeditor");
            String dateFolder = DateFormatUtils.format(date, "/yyyy/MM");
            String folderPath = realpath + dateFolder;
            FileTools.createFolders(folderPath);
            String extension = fileName.substring(fileName.lastIndexOf(".")); // 后缀名
            String saveName = DateFormatUtils.format(date, "yyyyMMddHHmmssSSS")
                    + Math.round(Math.random() * 1000 + 1);
            String savePath = folderPath + "/" + saveName + extension;
            file.transferTo(new File(savePath));
            // 如果是img则生成缩略图
            if (ImageTools.isImage(savePath)) {
                String min_savePath = folderPath + "/" + "min_" + saveName + extension;
                Thumbnails.of(savePath).size(iwidth, iheight)
                        .toFile(min_savePath);
            } else if (".JSP".equals(extension.toUpperCase())) { // 非法文件！
                return null;
            }
            String relativePath = "/attachment/kindeditor" + dateFolder + "/"
                    + saveName + extension;
            Map<String, Object> map = new HashMap<>();
            map.put("error", 0);
            map.put("fileName",  fileName.substring(0, fileName.lastIndexOf(".")));
            map.put("url", relativePath);
            map.put("size", file.getSize());
            return map;
    }

}
