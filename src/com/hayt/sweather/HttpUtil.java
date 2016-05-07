package com.hayt.sweather;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


import android.widget.Toast;

public class HttpUtil {
	public static String getJson(String httpUrl) {
		HttpGet httpRequest = new HttpGet(httpUrl);
		try {
			HttpClient httpclient = new DefaultHttpClient();
			/*建立HTTP Post联机*/ 
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return strResult;
			} else {//请求错误。
				Toast.makeText(new MainActivity().getApplicationContext(), "网络请求错误", Toast.LENGTH_SHORT).show();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return null;
	}
}
