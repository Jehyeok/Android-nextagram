package com.example.nextagram_android;

import java.io.IOException;
import java.io.InputStream;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ReadActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_read);

			TextView tvWriter = (TextView) findViewById(R.id.writer_in_read);
			TextView tvDate = (TextView) findViewById(R.id.date_in_read);
			TextView tvTitle = (TextView) findViewById(R.id.title_in_read);
			TextView tvContent = (TextView) findViewById(R.id.content_in_read);

			ImageView ivImage = (ImageView) findViewById(R.id.img_in_read);

			String articleNumber = getIntent().getExtras().getString(
					"ArticleNumber");
			
			Toast.makeText(this, articleNumber, Toast.LENGTH_SHORT).show();
			
			// Dao 초기화
			Dao dao = new Dao(getApplicationContext());
			ListData article = dao.getArticleByArticleNumber(Integer.parseInt(articleNumber));
			tvTitle.setText(article.getTitle());
			tvWriter.setText(article.getWriter());
			tvDate.setText(article.getWriteDate());
			tvContent.setText(article.getContent());
			Log.i("ReadActivity : onCreate", article.getTitle());

			InputStream ims = getApplicationContext().getAssets().open(article.getImgName());
			Drawable d = Drawable.createFromStream(ims, null);
			ivImage.setImageDrawable(d);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ReadActivity:OnCreate", e + "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("ReadActivity:OnCreate", e + "");
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("ReadActivity:OnCreate", e + "");
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.read, menu);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
}
