package com.example.nextagram_android;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.util.Log;

public class FileDownloader {
	private final Context context;

	public FileDownloader(Context context) {
		super();
		this.context = context;
	}
	
	public void downFile(String fileUrl, String fileName) {
		try {
			File file = new File(context.getFilesDir().getPath() + "/" + fileName);
			
			// 파일이 이미 있으면 다운하지 않음
			if (!file.exists()) {
				// 서버의 파일 경로
				URL url = new URL(fileUrl);
				// HTTP로 연
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				
				// 서버 접속시 Time out(ms)
				conn.setConnectTimeout(10 * 1000);
				// Read시 Time out(ms)
				conn.setReadTimeout(10 * 1000);
				
				// 요청 방식 선택
				conn.setRequestMethod("GET");
				// 연결을 지속
				conn.setRequestProperty("Connection", "Keep-Alive");
				// 캐릭터셋을 UTF-8로 요청
				conn.setRequestProperty("Accept-Charset", "UTF-8");
				
				// 캐시 사용 안함
				conn.setRequestProperty("Cache-Control", "no-cache");
				// 서버로부터 아무 형식의 데이터 다 요청
				conn.setRequestProperty("Accept", "*/*");
				
				// InputStream으로 서버로부터 응답 받음
				conn.setDoInput(true);
				
				conn.connect();
				int status = conn.getResponseCode();
				
				switch (status) {
				case 200:
				case 201:
					// 정상 연결
					InputStream is = conn.getInputStream();
					
					BufferedInputStream bis = new BufferedInputStream(is);
					ByteArrayBuffer baf = new ByteArrayBuffer(50);
					
					int current = 0;
					
					while ((current = bis.read()) != -1) {
						baf.append((byte) current);
					}
					
					FileOutputStream fos = context.openFileOutput(fileName, 0);
					fos.write(baf.toByteArray());
					
					fos.close();
					bis.close();
					is.close();
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			Log.e("FileDownloader > downFile", "ERROR: " + e);
		} catch (ProtocolException e) {
			e.printStackTrace();
			Log.e("FileDownloader > downFile", "ERROR: " + e);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.e("FileDownloader > downFile", "ERROR: " + e);
		} catch (IOException e) {
			e.printStackTrace();
			Log.e("FileDownloader > downFile", "ERROR: " + e);
		}
	}
}
