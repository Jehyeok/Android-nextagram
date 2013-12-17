package com.example.nextagram_android;

public class ListData {
	private int articleNumber;
	private String title;
	private String writer;
	private String id;
	private String content;
	private String writeDate;
	private String imgName;
	
	public ListData(int articleNumber, String title, String writer, String id,
			String content, String writeDate, String imgName) {
		super();
		try {
			this.articleNumber = articleNumber;
			this.title = title;
			this.writer = writer;
			this.id = id;
			this.content = content;
			this.writeDate = writeDate;
			this.imgName = imgName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int getArticleNumber() {
		return articleNumber;
	}

	public String getTitle() {
		return title;
	}

	public String getWriter() {
		return writer;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public String getWriteDate() {
		return writeDate;
	}

	public String getImgName() {
		return imgName;
	}
}
