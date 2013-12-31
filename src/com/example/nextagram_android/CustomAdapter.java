package com.example.nextagram_android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<ListData> {
	private Context context;
	private int resourceId;
	private ArrayList<ListData> listData;

	public CustomAdapter(Context context, int resourceId,
			ArrayList<ListData> listData) {
		super(context, resourceId, listData);
		// TODO Auto-generated constructor stub
		try {
			this.context = context;
			this.resourceId = resourceId;
			this.listData = listData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			View row = convertView;

			if (row == null) {
				LayoutInflater inflater = ((Activity) context).getLayoutInflater();
				row = inflater.inflate(resourceId, parent, false);
			}

			TextView writer = (TextView) row.findViewById(R.id.custom_list_writer);
			TextView title = (TextView) row.findViewById(R.id.custom_list_title);

			writer.setText(listData.get(position).getWriter());
			title.setText(listData.get(position).getTitle());

			ImageView img = (ImageView) row.findViewById(R.id.custom_list_imgView);

			// InputStream is = context.getAssets().open(
			// listData.get(position).getImgName());
			// Drawable d = Drawable.createFromStream(is, null);
			// img.setImageDrawable(d);

			String imgPath = context.getFilesDir().getPath() + "/"
					+ listData.get(position).getImgName();
			File imgLoadPath = new File(imgPath);

			if (imgLoadPath.exists()) {
				// 이미지가 있으면 비트맵으로 변환해서 표현
				Bitmap bitmap = BitmapFactory.decodeFile(imgPath);
				img.setImageBitmap(bitmap);
			}
			return row;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
