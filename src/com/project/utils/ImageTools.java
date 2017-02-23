package com.project.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;

/**图片处理
 * @author LiuDing
 * 2014-4-3-上午12:29:44
 */
public class ImageTools {
	
	/**获取图片尺寸
	 * @author LiuDing
	 * 2014-4-3 上午12:32:31
	 * @param imgpath
	 * @return
	 */
	public static int[] getSize(String imgpath){
		File picture = new File(imgpath);
        BufferedImage sourceImg;
		try {
			sourceImg = ImageIO.read(new FileInputStream(picture));
			return new int[]{sourceImg.getWidth(), sourceImg.getHeight()};
		} catch (Exception e) {
			e.printStackTrace();
			return new int[]{-1, -1};
		} 
	}
	
	/**根据文件名判断是不是图片
	 * @author LiuDing
	 * 2014-4-3 上午12:38:17
	 * @param fileName
	 * @return
	 */
	public static boolean isImage(String fileName){
		String extension = fileName.substring(fileName.lastIndexOf("."));
		extension = extension.toUpperCase();
		if(".PNG".equals(extension)
				|| ".JPG".equals(extension)
				|| ".JEPG".equals(extension)
				|| ".JPEG".equals(extension)){
			return true;
		}
		return false;
	}
}
