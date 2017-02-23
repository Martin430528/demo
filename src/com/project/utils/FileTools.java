package com.project.utils;

import java.io.File;

/**文件工具类
 * @author liuding
 * @create Aug 26, 2013 4:27:55 PM
 */
public class FileTools {
	/**根据路径创建路径下的所有目录
	 * @param path
	 * @return
	 * @author liuding
	 */
	public static boolean createFolders(String path){
		File folder = new File(path);
		if(!folder.exists() || !folder.isDirectory()){
			return folder.mkdirs();
		}
		return false;
	}
}
