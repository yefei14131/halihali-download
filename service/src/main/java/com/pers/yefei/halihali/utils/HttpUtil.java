package com.pers.yefei.halihali.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.MultipartPostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpUtil {

	final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
	private final static String DEFAULT_ENCODING = "UTF-8";


	public static int getResponseCode(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

			httpcon.setDoOutput(false);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);

			httpcon.setRequestMethod("GET");

			httpcon.setConnectTimeout(10 * 60 * 1000);
			httpcon.setReadTimeout(10 * 60 * 1000);
			httpcon.connect();

			return httpcon.getResponseCode();

		}catch (Exception e){

			log.error(ExceptionUtils.getStackTrace(e));
		}

		return 0;
	}


	public static String doFormPost(String strUrl, String formData) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

			httpcon.setDoOutput(true);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);
			httpcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpcon.setRequestMethod("POST");

			// 锟斤拷锟斤拷30锟斤拷锟接筹拷时
			httpcon.setConnectTimeout(30 * 1000);
			httpcon.setReadTimeout(30 * 1000);

			httpcon.setRequestProperty("Cookie",
					"time_zone=" + java.net.URLEncoder.encode("Asia/Shanghai (GMT+8) offset 28800", "UTF-8").trim());

			httpcon.connect();
			OutputStream os = httpcon.getOutputStream();
			os.write(formData.getBytes("utf-8"));
			os.flush();
			InputStream is = httpcon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			for (int len = 0; (len = is.read(buf)) != -1;) {
				baos.write(buf, 0, len);
			}
			is.close();

			byte[] resData = baos.toByteArray();
			baos.close();
			httpcon.disconnect();
			return new String(resData, "utf-8");
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		}

	}

	public static byte[] doPost(String strUrl, byte[] reqData) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

			httpcon.setDoOutput(true);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);

			httpcon.setRequestMethod("POST");

			// 锟斤拷锟斤拷30锟斤拷锟接筹拷时
			httpcon.setConnectTimeout(30 * 1000);
			httpcon.setReadTimeout(30 * 1000);

			httpcon.setRequestProperty("Cookie",
					"time_zone=" + java.net.URLEncoder.encode("Asia/Shanghai (GMT+8) offset 28800", "UTF-8").trim());

			httpcon.connect();
			OutputStream os = httpcon.getOutputStream();
			os.write(reqData);
			os.flush();
			InputStream is = httpcon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			for (int len = 0; (len = is.read(buf)) != -1;) {
				baos.write(buf, 0, len);
			}
			is.close();

			byte[] resData = baos.toByteArray();
			baos.close();
			httpcon.disconnect();
			return resData;
		} catch (Exception ex) {
			logger.error(ex.toString(), ex);
			return null;
		}
	}

	public static String multipartUploadDfsFile(String strUrl, String dfsFilePath) {
		try {
			String localPath = transformDfsUrl2LocalFile(dfsFilePath);
			return multipartUploadFiles(strUrl, localPath);
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	public static String transformDfsUrl2LocalFile(String dfsFilePath) throws Exception {
		URL url = new URL(dfsFilePath);
		InputStream input = getInputStreamFromURL(url);
		String fileType = dfsFilePath.substring(dfsFilePath.lastIndexOf(".")+1);
		String tempDir = System.getProperty("java.io.tmpdir");
		File file = new File(tempDir + File.separator + UUIDUtil.getUUID() + "." + fileType);
		FileOutputStream out = null;
		try {
			byte[] buf = new byte[2048];
			out = new FileOutputStream(file);
			while (true) {
				int len = input.read(buf);
				if (len == -1) {
					break;
				}
				out.write(buf, 0, len);
			}
		} catch (Exception e) {
			logger.error(e.toString(), e);
		} finally {
			try {
				if (input != null) {
					input.close();
				}
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
			if (out != null) {
				out.close();
			}
		}
		return file.getPath();
	}
	
	public static String multipartUploadFiles(String strUrl, String filePath) {
		String strResponse = null;
		HttpClient httpClient = new HttpClient();
		MultipartPostMethod multipartPostMethod = new MultipartPostMethod(strUrl);
		File file = new File(filePath);
		try {
			multipartPostMethod.addPart(new FilePart("file", file));
			int statusCode = httpClient.executeMethod(multipartPostMethod);
			if (statusCode == 200) {

				byte[] result = multipartPostMethod.getResponseBody();
				
				return new String(result, "utf-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			multipartPostMethod.releaseConnection();
			if (file.exists()) {
				file.delete();
			}
		}
		return strResponse;
	}

	public static byte[] doGet(String strUrl) {
		try {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

			httpcon.setDoOutput(false);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);

			httpcon.setRequestMethod("GET");

			// 锟斤拷锟斤拷10锟斤拷锟接筹拷时
			httpcon.setConnectTimeout(10 * 60 * 1000);
			httpcon.setReadTimeout(10 * 60 * 1000);
			httpcon.connect();
			InputStream is = httpcon.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// DataInputStream dis = new DataInputStream(is);
			// short len1 = dis.readShort();
			// System.out.println(len1);
			byte[] buf = new byte[4096];
			for (int len = 0; (len = is.read(buf)) != -1;) {
				baos.write(buf, 0, len);
			}
			is.close();

			byte[] resData = baos.toByteArray();
			baos.close();
			httpcon.disconnect();
			return resData;
		} catch (Exception ex) {
			logger.error("HttpUtil request " + strUrl + " error", ex);
			return null;
		}
	}

	public static byte[] doGet(String strUrl, int timeout) throws IOException {
			URL url = new URL(strUrl);
			HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();

			httpcon.setDoOutput(false);
			httpcon.setDoInput(true);
			httpcon.setUseCaches(false);
			httpcon.setInstanceFollowRedirects(true);

			httpcon.setRequestMethod("GET");

			// 锟斤拷锟矫筹拷时
			httpcon.setConnectTimeout(timeout * 1000);
			httpcon.setReadTimeout(timeout * 1000);
			httpcon.connect();
			InputStream is = httpcon.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			for (int len = 0; (len = is.read(buf)) != -1;) {
				baos.write(buf, 0, len);
			}
			is.close();

			byte[] resData = baos.toByteArray();
			baos.close();
			httpcon.disconnect();
			return resData;
	}

	public static double getDoubleParameter(HttpServletRequest request, String paraName) {
		String paramValue = request.getParameter(paraName);
		if (paramValue.equals("null") || paramValue.isEmpty()) {
			return 0.0d;
		} else {
			return Double.parseDouble(paramValue);
		}
	}

	public static String postJSONByClient(String url, String jsonStr) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);
		StringEntity myEntity = new StringEntity(jsonStr, ContentType.APPLICATION_JSON);// 构造请求数据
		post.setEntity(myEntity);// 设置请求体
		String responseContent = null; // 响应内容
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = response.getEntity();
				responseContent = EntityUtils.toString(entity, "UTF-8");
				return responseContent;
			} else {
				return response.toString();
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (response != null)
					response.close();

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static String getEntityString(HttpServletRequest request) {
		BufferedReader br;
		try {
			br = request.getReader();
			StringBuilder jsonStrBuilder = new StringBuilder();
			String tmpStr;
			while ((tmpStr = br.readLine()) != null) {
				jsonStrBuilder.append(tmpStr);
			}
			String jsonStr = jsonStrBuilder.toString();
			return jsonStr;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static String getMapByClient(String url, Map<String, String> paramMap) {
		CloseableHttpClient client = HttpClients.createDefault();
		// HttpPost post = new HttpPost(url);
		// StringEntity myEntity = new StringEntity(jsonStr,
		// ContentType.APPLICATION_JSON);// 构造请求数据
		// post.setEntity(myEntity);// 设置请求体

		URI uri = null;
		try {
			URIBuilder uriBulder = new URIBuilder(url);
			Set<String> set = paramMap.keySet();
			Iterator<String> it = set.iterator();
			while (it.hasNext()) {
				String key = it.next();
				uriBulder.setParameter(key, paramMap.get(key));
			}
			uri = uriBulder.build();
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		HttpGet get = new HttpGet(uri);
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				String responseContent = EntityUtils.toString(entity, "UTF-8");
				return responseContent;
			} else {
				return httpResponse.toString();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (httpResponse != null)
					httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (client != null)
						client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static byte[] doSslGet(String httpsUrl) throws Exception {
		HttpsURLConnection urlCon = null;
		try {
			urlCon = (HttpsURLConnection) (new URL(httpsUrl)).openConnection();
			urlCon.setDoInput(true);
			urlCon.setDoOutput(true);
			urlCon.setRequestMethod("GET");
			urlCon.setUseCaches(false);
			// 设置为gbk可以解决服务器接收时读取的数据中文乱码问题
			urlCon.connect();
			InputStream is = urlCon.getInputStream();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buf = new byte[4096];
			for (int len = 0; (len = is.read(buf)) != -1;) {
				baos.write(buf, 0, len);
			}
			is.close();

			byte[] resData = baos.toByteArray();
			baos.close();
			urlCon.disconnect();
			return resData;
		} catch (Exception e) {
			throw new Exception();
		}
	}

	public static InputStream getInputStreamFromURL(URL myURL) throws Exception {
		if (myURL == null) {
			return null;
		}
		if (myURL.getProtocol().equals("http")) {
			return myURL.openStream();
		}

		class MyX509TrustManager implements X509TrustManager {
			/*
			 * The default X509TrustManager returned by SunX509. We’ll delegate
			 * decisions to it, and fall back to the logic in this class if the
			 * default X509TrustManager doesn’t trust it.
			 */

			MyX509TrustManager() throws Exception {

			}

			/*
			 * Delegate to the default trust manager.
			 */
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			/*
			 * Delegate to the default trust manager.
			 */
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			/*
			 * Merely pass this through.
			 */
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		}

		// 创建SSLContext对象，并使用我们指定的信任管理器初始化

		TrustManager[] tm = { new MyX509TrustManager() };
		SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");

		sslContext.init(null, tm, new java.security.SecureRandom());

		// 从上述SSLContext对象中得到SSLSocketFactory对象
		SSLSocketFactory ssf = sslContext.getSocketFactory();

		// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
		HttpsURLConnection httpsConn = (HttpsURLConnection) myURL.openConnection();
		httpsConn.setSSLSocketFactory(ssf);

		// 取得该连接的输入流，以读取响应内容
		return httpsConn.getInputStream();

	}

}
