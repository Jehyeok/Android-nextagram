package com.example.nextagram_android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends Activity implements OnClickListener, OnItemClickListener {
	
	private ArrayList<ListData> articleData = new ArrayList<ListData>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			setContentView(R.layout.activity_main);
			
			Dao dao = new Dao(getApplicationContext());
			String testJsonData = dao.getJsonTestData();
			dao.insertJsonData(testJsonData);
			
			Button writeBtn = (Button)findViewById(R.id.write_btn);
			Button refreshBtn = (Button)findViewById(R.id.refresh_btn);
			
			writeBtn.setOnClickListener(this);
			refreshBtn.setOnClickListener(this);
			
			ListView listView = (ListView)findViewById(R.id.listView1);
			
			articleData = dao.getArticleList();
			
			CustomAdapter customAdapter = new CustomAdapter(this, R.layout.custom_list_row, articleData);
			listView.setAdapter(customAdapter);
			listView.setOnItemClickListener(this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case R.id.write_btn:
				Intent intentWrite = new Intent(this, WriteActivity.class);
				startActivity(intentWrite);
				break;
				
			case R.id.refresh_btn:
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		try {
			Intent intent = new Intent(this, ReadActivity.class);
			intent.putExtra("ArticleNumber", articleData.get(position).getArticleNumber() + "");
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MainActivity:onItemclick", e.getMessage());
		}
	}
	
}