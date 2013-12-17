package com.example.nextagram_android;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Dao {
	private Context context;
	private SQLiteDatabase database;

	public Dao(Context context) {
		try {
			this.context = context;

			// SQLite 초기화
			database = context.openOrCreateDatabase("LocalDATA.db",
					SQLiteDatabase.CREATE_IF_NECESSARY, null);
			// database.execSQL("DROP TABLE Articles");

			// 테이블 생성
			String sql = "CREATE TABLE IF NOT EXISTS Articles(ID integer primary key autoincrement,"
					+ "										  ArticleNumber integer UNIQUE not null,"
					+ "										  Title text not null,"
					+ "										  WriterName text not null,"
					+ "										  WriterID text not null,"
					+ "										  Content text not null,"
					+ "										  WriteDate text not null,"
					+ "										  ImgName text UNIQUE not null)";

			database.execSQL(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void insertJsonData(String jsonData) {
		try {
			// json으로 데이터를 파싱할 때 쓸 임시 변수
			int articleNumber;
			String title;
			String writer;
			String id;
			String content;
			String writeDate;
			String imgName;

			JSONArray jArr = new JSONArray(jsonData);

			for (int i = 0; i < jArr.length(); i++) {
				JSONObject jObj = jArr.getJSONObject(i);

				articleNumber = jObj.getInt("ArticleNumber");
				title = jObj.getString("Title");
				writer = jObj.getString("Writer");
				id = jObj.getString("Id");
				content = jObj.getString("Content");
				writeDate = jObj.getString("WriteDate");
				imgName = jObj.getString("ImgName");

				Log.i("test", "ArticleNumber: " + articleNumber + "Title" + title);

				String sql = "INSERT INTO Articles(ArticleNumber, Title, WriterName, WriterId, Content, WriteDate, ImgName)"
						+ " VALUES("
						+ articleNumber
						+ ", '"
						+ title
						+ "', '"
						+ writer
						+ "', '"
						+ id
						+ "', '"
						+ content
						+ "', '"
						+ writeDate + "', '" + imgName + "');";

				database.execSQL(sql);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getJsonTestData() {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append("");

			sb.append("[");

			sb.append("  {");
			sb.append("    'ArticleNumber':'1',");
			sb.append("    'Title':'오늘도 좋은 하루',");
			sb.append("    'Writer':'학생1',");
			sb.append("    'Id':'6614',");
			sb.append("    'Content':'하지만 곧 기말고사...',");
			sb.append("    'WriteDate':'2013-09-23-10-10',");
			sb.append("    'ImgName':'photo1.jpg'");
			sb.append("  },");
			sb.append("  {");
			sb.append("    'ArticleNumber':'2',");
			sb.append("    'Title':'대출 최고 3000만원',");
			sb.append("    'Writer':'김미영 팀장',");
			sb.append("    'Id':'6320',");
			sb.append("    'Content':'김미영 팀장입니다...',");
			sb.append("    'WriteDate':'2013-09-23-11-10',");
			sb.append("    'ImgName':'photo2.jpg'");
			sb.append("  },");
			sb.append("  {");
			sb.append("    'ArticleNumber':'3',");
			sb.append("    'Title':'맥 등록 신청',");
			sb.append("    'Writer':'학생2',");
			sb.append("    'Id':'8426',");
			sb.append("    'Content':'1a;2b...',");
			sb.append("    'WriteDate':'2013-09-23-12-33',");
			sb.append("    'ImgName':'photo3.jpg'");
			sb.append("  },");
			sb.append("]");

			return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ArrayList<ListData> getArticleList() {
		try {
			ArrayList<ListData> articleList = new ArrayList<ListData>();

			int articleNumber;
			String title;
			String writer;
			String id;
			String content;
			String writeDate;
			String imgName;

			// 데이터 선택
			String sql = "SELECT * FROM Articles";
			Cursor cursor = database.rawQuery(sql, null);

			while (cursor.moveToNext()) {
				articleNumber = cursor.getInt(1);
				title = cursor.getString(2);
				writer = cursor.getString(3);
				id = cursor.getString(4);
				content = cursor.getString(5);
				writeDate = cursor.getString(6);
				imgName = cursor.getString(7);

				articleList.add(new ListData(articleNumber, title, writer, id,
						content, writeDate, imgName));
			}

			cursor.close();

			return articleList;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public ListData getArticleByArticleNumber(int articleNumber) {
		try {
			ListData article = null;

			String title;
			String writer;
			String id;
			String content;
			String writeDate;
			String imgName;

			// 데이터 선택
			String sql = "SELECT * FROM Articles WHERE ArticleNumber = "
					+ articleNumber + ";";
			Cursor cursor = database.rawQuery(sql, null);

			cursor.moveToNext();

			title = cursor.getString(2);
			writer = cursor.getString(3);
			id = cursor.getString(4);
			content = cursor.getString(5);
			writeDate = cursor.getString(6);
			imgName = cursor.getString(7);

			article = new ListData(articleNumber, title, writer, id, content,
					writeDate, imgName);

			cursor.close();
			return article;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.e("Dao:getArticleByArticleNumber", e.getMessage());
			return null;
		}
	}
}
