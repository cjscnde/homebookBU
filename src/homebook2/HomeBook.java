package homebook2;

public class HomeBook {
	private long serialno;
	private String day;
	private String section;
	private String remark;
	private long revenue;
	private long expense;
	private String titleid;
	private String title;
	private String userid;
	private long sum;
	
	
	
	public long getSerialno() {
		return serialno;
	}
	public void setSerialno(long serialno) {
		this.serialno = serialno;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getSection() {
		return section;
	}
	public void setSection(String section) {
		try {
			if (section.equals("수입") || section.equals("지출")) {
				this.section=section;
			}else {
				throw new Exception("섹션오류");
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public long getRevenue() {
		return revenue;
	}
	public void setRevenue(long revenue) {
		this.revenue = revenue;
	}
	public long getExpense() {
		return expense;
	}
	public void setExpense(long expense) {
		this.expense = expense;
	}
	public String getTitleid() {
		return titleid;
	}
	public void setTitleid(String titleid) {
		this.titleid = titleid;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public long getSum() {
		return sum;
	}
	public void setSum(long sum) {
		this.sum = sum;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Override
	public String toString() {
		return "HomeBook [serialno=" + serialno + ", day=" + day + ", section=" + section + ", remark=" + remark
				+ ", revenue=" + revenue + ", expense=" + expense + ", titleid=" + titleid + ", title=" + title
				+ ", userid=" + userid + ", sum=" + sum + "]";
	}
	
	
	
	
}
