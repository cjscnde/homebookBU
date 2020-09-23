package homebook2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import common.MyUtils;


public class HomeBookDAO implements IDAO<HomeBook, Long> {

	private Connection conn = MyUtils.getConnection();
	
	@Override
	public boolean isExists(Long key, Connection conn) throws Exception {
		String sql = "select * from homebook where serialno=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, key);
		ResultSet rs =pstmt.executeQuery();
		while(rs.next()) {
			close();
			return true;
		}
		close();
		return false;
	}

	@Override
	public boolean insert(HomeBook t, Connection conn) throws Exception {
		String sql = "insert into homebook(serialno, day, "
				+ "section, remark, revenue, expense, titleid, userid)"
				+ " values(?, ?, ?, ?, ?, ?, ?, ?)";
		//System.out.println(sql);
		PreparedStatement pstmt=conn.prepareStatement(sql);
		pstmt.setLong(1,t.getSerialno());
		pstmt.setString(2,t.getDay());
		pstmt.setString(3,t.getSection());
		pstmt.setString(4,t.getRemark());
		pstmt.setLong(5,t.getRevenue());
		pstmt.setLong(6,t.getExpense());
		pstmt.setString(7,t.getTitleid());
		pstmt.setString(8,t.getUserid());
		
		int res = pstmt.executeUpdate();
		close();
		return res > 0;
	}

	@Override
	public boolean delete(Long key, Connection conn) throws Exception {
		//사전점검
		boolean is = isExists(key, conn);
		if(!is) return false;
		String sql="delete homebook where serialno=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setLong(1, key);
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}

	@Override
	public boolean update(HomeBook t, Connection conn) throws Exception {
		long key = t.getSerialno();
		boolean is = isExists(key, conn);
		if(!is)return false;
		String sql = "update homebook set day=to_date(?,'RRMMDD'),"
				+ "section=?,remark=?,revenue=?, "
				+ "titleid=?,userid=? where serialno = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1,t.getDay());
		pstmt.setString(2,t.getSection());
		pstmt.setString(3,t.getRemark());
		pstmt.setLong(4,t.getRevenue());
		pstmt.setString(6,t.getTitleid());
		pstmt.setString(7,t.getUserid());
		pstmt.setLong(8,t.getSerialno());//나중에 바꿔보자구!!
		int res = pstmt.executeUpdate();
		close();
		return res==1;//or res>0
	}

	@Override
	public HomeBook select(Long key, Connection conn) throws Exception {
		String sql = "select * from homebook where serialno=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setLong(1, key);
		ResultSet rs = pstmt.executeQuery();
		HomeBook vo = new HomeBook();
		while(rs.next()) {
			vo.setSerialno(rs.getLong("SERIALNO"));
			vo.setDay(rs.getString("DAY"));//수정
			vo.setSection(rs.getString("SECTION"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setRevenue(rs.getLong("REVENUE"));
			vo.setTitleid(rs.getString("TITLEID"));
			vo.setUserid(rs.getString("USERID"));
		}
		close();
		return vo;
	}
	
	

	@Override
	public List<HomeBook> selectByCondition(
			Map<Object, Object> conditionMap, Connection conn) throws Exception {
		String sql = "select * from homebook where ";
		String sql2 = "";
		for (Object x:conditionMap.keySet()) {
			sql2 += (String)x+"="+conditionMap.get((String)x)+" AND ";
		}
		sql2=sql2.substring(0,sql.length()-5);
		sql +=sql2;
		System.out.println(sql);
		PreparedStatement pstmt=conn.prepareStatement(sql);
		ResultSet rs=pstmt.executeQuery();
		List<HomeBook> list = new ArrayList<>();
		while(rs.next()) {
			HomeBook vo=new HomeBook();
			vo.setSerialno(rs.getLong("SERIALNO"));
			vo.setDay(rs.getString("DAY"));
			vo.setSection(rs.getString("SECTION"));
			vo.setRemark(rs.getString("REMARK"));
			vo.setRevenue(rs.getLong("REVENUE"));
			vo.setTitleid(rs.getString("TITLEID"));
			vo.setUserid(rs.getString("USERID"));
			list.add(vo);
		}
		close();
		return list;
	}

	@Override
	public List<HomeBook> selectAll(Connection conn) throws Exception {
		// 모든자료를 얻어와 LIST에 담아 리턴 select * FROM score;
		List<HomeBook> data = new ArrayList<HomeBook>();
		String sql = "select * from homebook";

		PreparedStatement pstmt = conn.prepareStatement(sql);

		// 정적질의
		// select는 executequery를 이용한다.
		ResultSet rs = pstmt.executeQuery();

		while (rs.next()) {
			HomeBook vo = new HomeBook();
			// DB로부터값을 가져와서(getString)해서 등록(setName)한다.
			// 값이 없을때까지 while문이 돌아가며,
			vo.setSerialno(rs.getLong("serialno"));
			vo.setUserid(rs.getString(2));
			vo.setDay(rs.getString("day")); // 컬럼순서가 바뀔수있으므로 컬럼명으로 하는것이좋음
			vo.setSection(rs.getString(4));
			vo.setTitle(rs.getString(5));
			vo.setRemark(rs.getString(6));
			vo.setRevenue(rs.getLong(7));
			data.add(vo); // 한행씩 읽어서 data에 등록한다.
		}
		// System.out.println(data.size());
		return data;
	}

	@Override
	public void close() throws Exception {
		//close();

	}
	

	
	public long getMaxSerialno(Connection conn) throws Exception {
		long max =0L;
		String sql = "select max(serialno) from homebook";
		PreparedStatement pstmt=conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			max = rs.getLong(1);
		}
		close();
		return max;
	}
	public long getMinSerialno(Connection conn) throws Exception {
		long min =0L;
		String sql = "select min(serialno) from homebook";
		PreparedStatement pstmt=conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		while(rs.next()) {
			min = rs.getLong(1);
		}
		close();
		return min;
	}
	
	
	
	public Long sum1(Connection conn) throws SQLException {

		String sql = "SELECT sum(REVENUE) FROM HOMEBOOK";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		HomeBook vo = new HomeBook();
		Long data;
		while (rs.next()) {
			vo.setSum(rs.getLong(1));
		}
		data = vo.getSum();
		return data;

	}
	
	   public String gettitleid (String aa) {
		      String sql = "SELECT titleid FROM account_title where title=?";
		      PreparedStatement pstmt;
		      try {
		         pstmt = conn.prepareStatement(sql);
		         pstmt.setString(1, aa);
		         ResultSet rs = pstmt.executeQuery();
		         if (rs.next())
		            return rs.getString(1);
		      } catch (SQLException e) {
		         e.printStackTrace();
		      }
		      return "";
		   }

	   //타이틀의 리스트를 가져오는 메소드
	   public List<String> gettitle() {
		      try {
		         List<HomeBook> data = new ArrayList<HomeBook>();
		         String sql = "SELECT title FROM accounttitle";
		         PreparedStatement pstmt;
		         pstmt = conn.prepareStatement(sql);
		         ResultSet rs = pstmt.executeQuery();
		         while (rs.next()) {
		            HomeBook vo = new HomeBook();
		            vo.setTitle(rs.getString("title"));
		            data.add(vo);
		         }
		         
		 		ArrayList<String> title_list = new ArrayList<String>();
				for (int i = 0; i  < data.size(); i++) {
					title_list.add(data.get(i).getTitle());
				}
		         
		         return title_list;
		      } catch (SQLException e) {
		         // TODO Auto-generated catch block
		         e.printStackTrace();
		      } return null;
		   }
}
