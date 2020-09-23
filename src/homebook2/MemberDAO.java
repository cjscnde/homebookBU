package homebook2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberDAO implements IDAO<Member, String> {
	private Connection conn = null;
	
	public MemberDAO() {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.err.println("오라클 드라이버가 없습니다.");
			System.exit(0); // 시스템을 종료
		}

		try {
			conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", // url
					"HOMEBOOK2", // user
					"homebook2" // password
			);
		} catch (SQLException e) {
			System.err.println("url, userid, password점검");
			System.exit(0);
		}
	}
	
	
	@Override
	public boolean isExists(String key, Connection conn) throws Exception {
		String sql = "select count(*) from member where userid=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, key);
		ResultSet rs =pstmt.executeQuery();
		while(rs.next()) {
		}
		close();
		return (rs.getInt(1)>0)?true:false;
	}

	@Override
	public boolean insert(Member t, Connection conn) throws Exception {
		String sql = "insert into member (userid,username,phone)values (?,?,?)";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, t.getUserid());
		pstmt.setString(2, t.getUsername());
		pstmt.setString(3, t.getPhone());
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}

	@Override
	public boolean delete(String key, Connection conn) throws Exception {
		String sql = "delete member where userid=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1,key);
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}

	@Override
	public boolean update(Member t, Connection conn) throws Exception {
		String sql = "update member set username=?,phone=? where userid=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, t.getUsername());
		pstmt.setString(2, t.getPhone());
		pstmt.setString(3, t.getUserid());
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}

	@Override
	public Member select(String key, Connection conn) throws Exception {
		String sql = "select * from member where userid=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, key);
		ResultSet rs = pstmt.executeQuery();
		Member vo = new Member();
		while(rs.next()) {
			//안정성을 위해 컬럼순서를 지양하고 컬럼이용
			vo.setUserid(rs.getString("USERID"));
			vo.setUsername(rs.getString("USERNAME"));
			vo.setPhone(rs.getString("PHONE"));
		}
		close();
		return vo;

	}

	@Override
	public List<Member> selectByCondition(
			Map<Object, Object> conditionMap, Connection conn) throws Exception {
			String sql = "select * from MEMBER where ";
			String sql2= "";//where 절 뒷부분
			for(Object x:conditionMap.keySet()) {
				sql2 = sql2+ (String)x +"="+conditionMap.get((String)x)+" AND ";
			}
			sql2=sql2.substring(0, sql2.length()-5);
			sql = sql+sql2;
			System.out.println(sql);
			PreparedStatement pstmt =conn.prepareStatement(sql);
			ResultSet rs = pstmt.executeQuery();
			List<Member> list = new ArrayList<>();
			while(rs.next()) {
				Member vo = new Member();
				vo.setUserid(rs.getString("USERID"));
				vo.setUsername(rs.getString("USERNAME"));
				vo.setPhone(rs.getString("PHONE"));
				list.add(vo);
			}
			close();
			return list;
	}

	@Override
	public List<Member> selectAll(Connection conn) throws Exception {
		String sql = "select * from member";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<Member> list= new ArrayList<>();
		while(rs.next()) {
			Member vo = new Member();
			vo.setUserid(rs.getString("USERID"));
			vo.setUsername(rs.getString("USERNAME"));
			vo.setPhone(rs.getString("PHONE"));
			list.add(vo);
		}
		close();
		return list;
	}
	@Override
	public void close() throws Exception {
		conn.close();
	}
	
	public Long Login (String id, String password) {
		try {
			String sql = "select count(*) from member where userid=? and password=?";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				return rs.getLong(1);
			} 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} return 0l;
	}







	


	
}
