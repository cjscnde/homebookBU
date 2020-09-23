package homebook2;

import java.sql.*;
import java.util.*;

import common.MyUtils;


public class AccountTitleDAO implements IDAO<AccountTitle, String> {
	private Connection conn = MyUtils.getConnection();

	@Override
	public boolean isExists(String key, Connection conn) throws Exception {
		String sql = "select count(*) from Account_Title where title_id=?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, key);
		ResultSet rs =pstmt.executeQuery();
		int cnt =0;
		while(rs.next()) {
			cnt =rs.getInt(1);
		}
		//ResultSetMetaData rsmd = rs.getMetaData();
		close();
		return (cnt>0)?true:false;
	}

	@Override
	public boolean insert(AccountTitle t, Connection conn) throws Exception {
		
		String sql = "insert into account_title (title_id,title)values (?,?)";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, t.getTitle_id());
		pstmt.setString(2, t.getTitle());
		int res = pstmt.executeUpdate();
		
		return res>0;
	}
	
//	@Override
//	public boolean insert(AccountTitle t, Connection conn) throws Exception {
//		System.out.println(conn);
//		String sql = "insert into account_title (title_id,title)values (?,?)";
//		PreparedStatement pstmt =conn.prepareStatement(sql);
//		pstmt.setString(1, t.getTitle_id());
//		pstmt.setString(2, t.getTitle());
//		int res = pstmt.executeUpdate();
//		close(conn);
//		return res>0;
//	}

	@Override
	public boolean delete(String key, Connection conn) throws Exception {
		String sql = "delete account_title where title_id=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1,key);
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}

	@Override
	public boolean update(AccountTitle t, Connection conn) throws Exception {
		String sql = "update account_title set title=? where title_id=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, t.getTitle());
		pstmt.setString(2, t.getTitle_id());
		int res = pstmt.executeUpdate();
		close();
		return res>0;
	}
	
	

	@Override
	public AccountTitle select(String key, Connection conn) throws Exception {
		String sql = "select * from account_title where title_id=?";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		pstmt.setString(1, key);
		ResultSet rs = pstmt.executeQuery();
		AccountTitle vo = new AccountTitle();
		while(rs.next()) {
			//안정성을 위해 컬럼순서를 지양하고 컬럼이용
			vo.setTitle_id(rs.getString("TITLE_ID"));
			vo.setTitle(rs.getString("Title"));
		}
		close();
		return vo;
	}

	@Override
	public List<AccountTitle> selectByCondition(
			Map<Object, Object> conditionMap, Connection conn) throws Exception {
		String sql = "select * from account_title where ";
		String sql2= "";//where 절 뒷부분
		for(Object x:conditionMap.keySet()) {
			sql2 = sql2+ (String)x +"="+conditionMap.get((String)x)+" AND ";
		}
		sql2=sql2.substring(0, sql2.length()-5);
		sql = sql+sql2;
		System.out.println(sql);
		PreparedStatement pstmt =conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<AccountTitle> list = new ArrayList<>();
		while(rs.next()) {
			AccountTitle vo = new AccountTitle();
			vo.setTitle_id(rs.getString("TITLE_ID"));
			vo.setTitle(rs.getString("TITLE"));
			list.add(vo);
		}
		close();
		return list;
	}

	@Override
	public List<AccountTitle> selectAll(Connection conn) throws Exception {
		String sql = "select * from account_title";
		PreparedStatement pstmt =conn.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<AccountTitle> list= new ArrayList<>();
		while(rs.next()) {
			AccountTitle vo = new AccountTitle();
			vo.setTitle_id(rs.getString("TITLE_ID"));
			vo.setTitle(rs.getString("TITLE"));
			list.add(vo);
		}
		close();
		return list;
		
	}

	@Override
	public void close() throws Exception {
		conn.close();
	}
	
	
}
