package homebook2;
//계정과목vo
public class AccountTitle {
	private String title_id;
	private String title;
	
	public String getTitle_id() {
		return title_id;
	}
	public void setTitle_id(String title_id) {
		this.title_id = title_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "AccountTitle [title_id=" + title_id + ", title=" + title + "]";
	}

	
}
