package com.example.nextagram_android;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.devspark.sidenavigation.ISideNavigationCallback;
import com.devspark.sidenavigation.SideNavigationView;
import com.devspark.sidenavigation.SideNavigationView.Mode;

public class MainActivity extends Activity implements OnItemClickListener {
	private final Handler handler = new Handler();
	private ArrayList<ListData> articleData = new ArrayList<ListData>();
	private ListView mainListView;
	private SideNavigationView sideNavigationView;
	
	ISideNavigationCallback sideNavigationCallback = new ISideNavigationCallback() {

		@Override
		public void onSideNavigationItemClick(int itemId) {
			String text = "";
			switch (itemId) {
			case R.id.side_navigation_menu_add:
				Intent intentWrite = new Intent(getApplicationContext(), WriteActivity.class);
				startActivity(intentWrite);
				text = "write";
				break;
			case R.id.side_navigation_menu_call:
				refreshData();
				text = "refresh";
				break;
			default:
				text = "";
			}
			Toast.makeText(getApplicationContext(), "side menu: " + text, Toast.LENGTH_SHORT).show();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			setContentView(R.layout.activity_main);
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.permitNetwork().build());

			sideNavigationView = (SideNavigationView) findViewById(R.id.side_navigation_view);
			sideNavigationView.setMenuItems(R.menu.side_menu);
			sideNavigationView.setMenuClickCallback(sideNavigationCallback);
			sideNavigationView.setMode(Mode.LEFT);
			
			getActionBar().setDisplayHomeAsUpEnabled(true);

			mainListView = (ListView) findViewById(R.id.listView1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void listView() {
		Dao dao = new Dao(getApplicationContext());
		articleData = dao.getArticleList();

		CustomAdapter customAdapter = new CustomAdapter(this,
				R.layout.custom_list_row, articleData);
		mainListView.setAdapter(customAdapter);
		mainListView.setOnItemClickListener(this);
	}

	public void refreshData() {
		try {
			new Thread() {
				public void run() {
					Proxy proxy = new Proxy();
					String jsonData = proxy.getJSON();

					Dao dao = new Dao(getApplicationContext());
					dao.insertJsonData(jsonData);

					// listView()
					handler.post(new Runnable() {
						public void run() {
							listView();
						}
					});
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("MainActivity > refreshData", "Error" + e);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		try {
			// Inflate the menu; this adds items to the action bar if it is
			// present.
			getMenuInflater().inflate(R.menu.main, menu);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			sideNavigationView.toggleMenu();
			break;
		default:
			
				
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		try {
			Intent intent = new Intent(this, ReadActivity.class);
			intent.putExtra("ArticleNumber", articleData.get(position)
					.getArticleNumber() + "");
			startActivity(intent);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("MainActivity:onItemclick", e.getMessage());
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshData();
		listView();
	}
}
