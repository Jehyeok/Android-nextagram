package com.example.nextagram_android;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.util.Log;

/**
 * 
 * @author jehyeok Http로 외부의 데이터를 반환하는 클래
 * 
 */

public class Proxy {

	public String getJSON() {
		
		try {
			// URL에는 JSON데이터를 출력해주는 페이지의 주소를 입력
			URL url = new URL("http://10.73.44.93/~stu03/loadData.php");
			
			// HttpURLConnection을 url의 주소로 연결
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			// 서버 접속 시 Time out(ms)
			conn.setConnectTimeout(10 * 1000);
			// Read시 Time out(ms)
			conn.setReadTimeout(10 * 1000);
			
			// 요청 방식 선택
			conn.setRequestMethod("GET");
			// 연결을 지속하도록 함
			conn.setRequestProperty("Connection", "Keep-Alive");
			// 캐릭터셋 UTF-8
			conn.setRequestProperty("Accept-Charset", "UTF-8");
			
			// 캐시된 데이터 사용 안함 매번 서버로부터 다시 받음
			conn.setRequestProperty("Cache-Control", "no-cache");
			// 서버로부터 JSON 형식의 타입 데이터 요청
			conn.setRequestProperty("Accept", "application/json");
			
			// InputStream으로 응답을 받음
			conn.setDoInput(true);
			
			conn.connect();
			
			int status = conn.getResponseCode();
			Log.i("Proxy > getJSON", "ProxyResponseCode" + status);
			
			switch (status) {
			case 200:
			case 201:
				// 정상적으로 연결이 된 상태
				BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				br.close();
				
				return sb.toString();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e("Proxy > getJSON", "NETWORK ERROR" + e);
		} catch (ProtocolException e) {
			e.printStackTrace();
			Log.e("Proxy > getJSON", "NETWORK ERROR" + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("Proxy > getJSON", "NETWORK ERROR" + e);
		} catch (Exception e) {
			Log.e("Proxy > getJSON", "NETWORK ERROR" + e);
		}
		
		return null;
	}
}
