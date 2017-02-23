package com.project.utils;

public class VideoTools {
	/**
	 * 判断是否为视频文件
	 * 
	 * @author GuoZhiLong
	 * @param fileName
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean isVideo(String fileName) {
		String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
		if (".avi".equals(extension) || ".mp4".equals(extension)
				|| ".rmvb".equals(extension) || ".mpe".equals(extension)
				|| ".mpeg".equals(extension) || ".mpg".equals(extension)
				|| ".3gp".equals(extension) || ".flv".equals(extension)
				|| ".wmv".equals(extension) || ".asf".equals(extension)
				|| ".asx".equals(extension) || ".wmv9".equals(extension)
				|| ".rm".equals(extension)) {
			return true;
		}
		return false;
	}
}
