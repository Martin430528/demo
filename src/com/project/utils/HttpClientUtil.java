package com.project.utils;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class HttpClientUtil {

	private static DefaultHttpClient client;
	private static boolean init = false;

	private static void init() {
		client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		init = true;
	}

	/**
	 * ����һ��get����
	 *
	 * @param url
	 * @return
	 * @author liuding
	 */
	public static HttpResponse get(String url) {
		if (!init)
			init();
		HttpGet get = new HttpGet(url);
		try {
			HttpResponse response = client.execute(get);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ����post����
	 *
	 * @param url
	 * @param nameValuePair
	 * @return
	 * @author liuding
	 */
	public static HttpResponse post(String url, List<NameValuePair> nameValuePairList) {
		if (!init)
			init();
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			return response;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * ��ȡpost����ķ��ؽ��
	 *
	 * @param url
	 * @param nameValuePairList
	 * @return
	 * @author liuding
	 */
	public static String getPostText(String url, List<NameValuePair> nameValuePairList) {
		HttpResponse response = post(url, nameValuePairList);
		try {
			String entity = EntityUtils.toString(response.getEntity());
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建新的请求
	 *
	 * @param url
	 * @param nameValuePairList
	 * @param encoding
	 * @return
	 */
	public static String createAndGetPostText(String url, List<NameValuePair> nameValuePairList, String encoding) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		HttpPost post = new HttpPost(url);
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			String entity = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"), encoding);
			;
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String createAndGetPostText(String url, Map<String, String> params, String encoding) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		HttpPost post = new HttpPost(url);
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		if (null != params) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nameValuePairList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
		}
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
			HttpResponse response = client.execute(post);
			String entity = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"), encoding);
			;
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 创建新的请求
	 *
	 * @param url
	 * @return
	 */
	public static String createAndGetText(String url) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			String entity = EntityUtils.toString(response.getEntity());
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * 创建新的请求
	 *
	 * @param url
	 * @return
	 */
	public static String createAndGetText(String url, String encode) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		try {
			HttpGet get = new HttpGet(url);
			HttpResponse response = client.execute(get);
			String entity = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"), encode);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * 带cookie请求
	 *
	 * @author LiuDing 2014-7-8 下午12:33:21
	 * @param url
	 * @param encode
	 * @param cookie
	 * @return
	 */
	public static String getTextWithCookie(String url, String encode, String cookie) {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter("http.protocol.cookie-policy", CookiePolicy.BROWSER_COMPATIBILITY);
		client.getParams().setParameter(HttpConnectionParams.CONNECTION_TIMEOUT, 4000);
		try {
			HttpGet get = new HttpGet(url);
			get.setHeader("Cookie", cookie);
			HttpResponse response = client.execute(get);
			String entity = new String(EntityUtils.toString(response.getEntity()).getBytes("ISO-8859-1"), encode);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	/**
	 * ��ȡһ����ҳ��Դ��
	 *
	 * @param url
	 * @return
	 * @author liuding
	 */
	public static String getURLText(String url) {
		HttpResponse response = get(url);
		try {
			String entity = EntityUtils.toString(response.getEntity());
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从URL获取域名
	 *
	 * @author LiuDing 2014-5-16 下午12:48:38
	 * @param url
	 * @return
	 */
	public static String getHost(String url) {
		String arr[] = new String[] { ".com.cn", ".com", ".cn", ".net", ".org", ".jp", ".tw" };
		for (String str : arr) {
			if (url.contains(str)) {
				url = url.substring(0, url.indexOf(str) + str.length());
				return url.replace("http://", "").replace("https://", "");
			}
		}
		return null;
	}

	/**
	 * 下载网络文件并保存，只适合图片等文件，不适合文本文件
	 *
	 * @author LiuDing 2014-5-16 下午01:19:49
	 * @param fileurl
	 *            文件网址
	 * @param localdir
	 *            保存文件的磁盘目录
	 */
	public static String download(String fileurl, String localdir) {
		if (!localdir.endsWith("/"))
			localdir += "/";
		String tails[] = new String[] { ".jpg", ".png", ".gif", ".css", ".js" };
		String tail = "";
		for (String str : tails) {
			if (fileurl.contains(str)) {
				tail = str;
				break;
			}
		}
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;
		String saveName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")
				+ Math.round(Math.random() * 1000 + 1);
		saveName += tail;
		try {
			URL url = new URL(fileurl);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(localdir + saveName);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
			return saveName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	// 下载文件
	public static Map downloadFile(String fileurl, String localdir) {
		if (!localdir.endsWith("/"))
			localdir += "/";
		String tails[] = new String[] { ".jpg", ".png", ".gif", ".css", ".js" };
		String tail = ".jpg";
		for (String str : tails) {
			if (fileurl.contains(str)) {
				tail = str;
				break;
			}
		}
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;
		String saveName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")
				+ Math.round(Math.random() * 1000 + 1);
		saveName += tail;
		try {
			URL url = new URL(fileurl);
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream(localdir + saveName);
			byte[] buffer = new byte[1204];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				fs.write(buffer, 0, byteread);
			}
			fs.close();
			inStream.close();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("fileName", saveName);
			map.put("path", localdir + saveName);
			map.put("cookie", conn.getHeaderField("Set-Cookie"));
			System.out.println(map.get("cookie"));
			return map;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 下载网络文件并保存，适合文本文件
	 *
	 * @author LiuDing 2014-5-16 下午02:45:20
	 * @param fileurl
	 * @param localdir
	 * @return
	 */
	public static String downloadText(String fileurl, String localdir, String encoding) {
		if (!localdir.endsWith("/"))
			localdir += "/";
		String tails[] = new String[] { ".css", ".js" };
		String tail = "";
		for (String str : tails) {
			if (fileurl.contains(str)) {
				tail = str;
				break;
			}
		}
		// 下载网络文件
		String saveName = DateFormatUtils.format(new Date(), "yyyyMMddHHmmssSSS")
				+ Math.round(Math.random() * 1000 + 1);
		saveName += tail;
		try {
			FileOutputStream fs = new FileOutputStream(localdir + saveName);
			String text = HttpClientUtil.createAndGetText(fileurl, encoding);
			System.out.println(text);
			return saveName;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
