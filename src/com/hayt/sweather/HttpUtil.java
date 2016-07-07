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
			/*����HTTP Post����*/ 
			HttpResponse httpResponse = httpclient.execute(httpRequest);
			if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(httpResponse
						.getEntity());
				return strResult;
			} else {//�������
				Toast.makeText(new MainActivity().getApplicationContext(), "�����������", Toast.LENGTH_SHORT).show();
			}
		} catch (ClientProtocolException e) {
		} catch (IOException e) {
		} catch (Exception e) {
		}
		return null;
	}
}
