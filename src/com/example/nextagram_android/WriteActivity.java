package com.example.nextagram_android;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class WriteActivity extends Activity implements OnClickListener {
	private EditText etWriter;
	private EditText etTitle;
	private EditText etContent;
	private ImageButton ibPhoto;
	private Button btUpload;

	private String filePath;
	private String fileName;

	private ProgressDialog progressDialog;

	private static final int REQUEST_PHOTO_ALBUM = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_write);

			etWriter = (EditText) findViewById(R.id.writer_in_write);
			etTitle = (EditText) findViewById(R.id.title_in_write);
			etContent = (EditText) findViewById(R.id.content_in_write);

			ibPhoto = (ImageButton) findViewById(R.id.img_in_write);
			ibPhoto.setOnClickListener(this);

			btUpload = (Button) findViewById(R.id.do_write_btn);
			btUpload.setOnClickListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		try {
			getMenuInflater().inflate(R.menu.write, menu);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private Uri getRealPathUri(Uri uri) {
		try {
			Uri filePathUri = uri;
			if (uri.getScheme().toString().compareTo("content") == 0) {
				Cursor cursor = getApplicationContext().getContentResolver()
						.query(uri, null, null, null, null);
				if (cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					filePathUri = Uri.parse(cursor.getString(column_index));
				}
			}
			return filePathUri;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (requestCode == REQUEST_PHOTO_ALBUM) {
				Uri uri = getRealPathUri(data.getData());
				filePath = uri.toString();
				fileName = uri.getLastPathSegment();

				Bitmap bitmap = BitmapFactory.decodeFile(filePath);
				ibPhoto.setImageBitmap(bitmap);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("WriteActivity > onAcitivityResult", "Error" + e);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_in_write:
			Intent intent = new Intent(Intent.ACTION_PICK);

			intent.setType(Images.Media.CONTENT_TYPE);
			intent.setData(Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, REQUEST_PHOTO_ALBUM);

			break;
		case R.id.do_write_btn:
			final Handler handler = new Handler();

			new Thread() {
				public void run() {

					handler.post(new Runnable() {
						public void run() {
							progressDialog = ProgressDialog.show(
									WriteActivity.this, "", "업로드 중입니다.");
						}
					});
					
					String ID = Secure.getString(getApplicationContext()
							.getContentResolver(), Settings.Secure.ANDROID_ID);
					String DATE = new SimpleDateFormat("yyyy-MM-dd HH:mm",
							Locale.KOREA).format(new Date());
					ListData article = new ListData(0, etTitle.getText()
							.toString(), etWriter.getText().toString(), ID,
							etContent.getText().toString(), DATE, fileName);

					ProxyUP proxyUP = new ProxyUP();
					proxyUP.uploadArticle(article, filePath);
					
					handler.post(new Runnable() {
						public void run() {
							progressDialog.cancel();
							
							finish();
						}
					});
				}
			}.start();
		}
	}
}
