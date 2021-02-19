package co.mydiary;

import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




public class DiaryOracleDAO implements DAO {
	
	Connection conn;
	Statement stmt;
	PreparedStatement psmt;
	ResultSet rs;

	@Override
	public int insert(DiaryVO vo) {
		conn = JdbcUtil.connect();	
		String sql = "INSERT INTO DIARY VALUES(?,?)";
		int r = 0;
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, vo.getWdate());
			psmt.setString(2, vo.getContents());
			r = psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JdbcUtil.disconnect(conn);
		}
		return r;
	}

	@Override
	public void update(DiaryVO vo) {
		//수정
		conn = JdbcUtil.connect();
		String sql = "update diary set wdate =?, contents=?";
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, vo.getWdate());
			psmt.setString(2, vo.getContents());
			psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JdbcUtil.disconnect(conn);
		}
	}

	@Override
	public int delete(String date) {
		//삭제
		int n = 0;
		conn = JdbcUtil.connect();
		String sql = "DELETE FROM DIARY WHERE WDATE =?";
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, date);
			rs = psmt.executeQuery();
			n = psmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JdbcUtil.disconnect(conn);
		}
		return n;
	}

	@Override
	public DiaryVO selectDate(String date) {
		DiaryVO vo = null;
		String sql = "SELECT WDATE, CONTENTS FROM DIARY WHERE WDATE =?";
		conn = JdbcUtil.connect();
		
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, date);
			rs = psmt.executeQuery();
			if(rs.next()) {
				vo = new DiaryVO();
				vo.setWdate(rs.getString("wdate"));
				vo.setContents(rs.getString("contents"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			JdbcUtil.disconnect(conn);
		}
		return vo;
	}

	@Override
	public List<DiaryVO> selectContent(String content) {
		//내용으로 조회
		ArrayList<DiaryVO> list = new ArrayList<DiaryVO>();
		DiaryVO vo =null;
		String sql = "SELECT WDATE, CONTENTS FROM DIARY WHERE CONTENTS like '%'||?||'%'";
		conn = JdbcUtil.connect();
		try {
			psmt = conn.prepareStatement(sql);
			psmt.setString(1, content);
			rs = psmt.executeQuery();
			if(rs.next()) {
				vo = new DiaryVO();
				vo.setWdate(rs.getString("wdate"));
				vo.setContents(rs.getString("contents"));
				list.add(vo);
			}			
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JdbcUtil.disconnect(conn);
		}
		return list;
	}

	@Override
	public List<DiaryVO> selectAll() {
		//전체조회
		ArrayList<DiaryVO> list = new ArrayList<DiaryVO>();
		String sql = "SELECT WDATE, CONTENTS FROM DIARY ORDER BY WDATE";
		DiaryVO vo = null;
		conn=JdbcUtil.connect();
		try {
			psmt = conn.prepareStatement(sql);
			ResultSet rs = psmt.executeQuery();
			while(rs.next()) {
				vo = new DiaryVO();
				vo.setWdate(rs.getString("wdate"));
				vo.setContents(rs.getString("contents"));
				list.add(vo);
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}finally {
			JdbcUtil.disconnect(conn);
		}			
		return list;
	}

}
