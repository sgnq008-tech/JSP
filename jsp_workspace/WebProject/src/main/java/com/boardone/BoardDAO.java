package com.boardone;

import java.sql.*;
import java.util.*;

import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;

public class BoardDAO {

	private static BoardDAO instance = null;

	public BoardDAO() {
	}

	public static BoardDAO getInstance() {

		if (instance == null) {
			synchronized (BoardDAO.class) {
				// 동기화 명령
				instance = new BoardDAO();
			}
		}
		return instance;
	}// end getInstance

	/*
	 * 여기서부터 게시판의 각 기능을 구현하는 메소드를 추가하여 작성하면 됨
	 */

	// 데이터베이스에 글을 저장함
	public void insertArticle(BoardVO article) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int num = article.getNum();
		int ref = article.getRef();
		int step = article.getStep(); // 수정
		int depth = article.getDepth();
		int number = 0;
		String sql = "";

		try {
			// 데이터베이스 연결
			con = ConnUtil.getConnection();

			sql = "select max(num) from board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next())
				number = rs.getInt(1) + 1;
			else
				number = 1;

			if (num != 0) { // 수정: number → num

				// 답변글일때
				sql = "update board set step=step+1 where ref=? and step > ?"; // 수정: amd → and
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, step);
				pstmt.executeUpdate();

				step = step + 1;
				depth = depth + 1;

			} else {
				// 새글일 경우
				ref = number;
				step = 0;
				depth = 0;
			}

			// 글을 데이터베이스에 저장할 쿼리문 작성
			sql = "insert into board(num,writer,email,subject,pass," + "regdate,ref,step,depth,content,ip) "
					+ "values(board_seq.nextval,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, article.getWriter()); // 수정
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPass());
			pstmt.setTimestamp(5, article.getRegdate());
			pstmt.setInt(6, article.getRef());
			pstmt.setInt(7, article.getStep());
			pstmt.setInt(8, article.getDepth());
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getIp());

			pstmt.executeUpdate(); // 추가

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}
	}// end insertArticle

	/* 전체 글의 개수를 가져오는 메소드 구현 */
	public int getArticleCount() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;

		try {
			con = ConnUtil.getConnection();
			String sql = "select count(*) from board";
			pstmt = con.prepareStatement(sql);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}
		return x;
	}// end getArticleCount

	public List<BoardVO> getArticles() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;

		try {

			con = ConnUtil.getConnection();
			String sql = "select * from board order by num desc";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				articleList = new ArrayList<BoardVO>();

				do {
					BoardVO article = new BoardVO();
					article.setNum(rs.getInt("num"));
					article.setWriter(rs.getString("writer"));
					article.setEmail(rs.getString("email"));
					article.setSubject(rs.getString("subject"));
					article.setPass(rs.getString("pass"));
					article.setRegdate(rs.getTimestamp("regdate"));
					article.setReadcount(rs.getInt("readcount"));
					article.setRef(rs.getInt("ref"));
					article.setStep(rs.getInt("step"));
					article.setDepth(rs.getInt("depth"));
					article.setContent(rs.getString("content"));
					article.setIp(rs.getString("ip"));
					articleList.add(article);

				} while (rs.next());

			}

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
				}
			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
				}
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
				}
		}
		return articleList;
	}// end getArticles
	
	/* list.jsp페이지으 ㅣ글 제목을 누르면 글 내용을 볼 수 있도록 작업을 해야함
	 * 
	 * 글의 num을 매개변수로 해서 하나의 글 정보를 가져오는 메소드를 구현
	 */
	public BoardVO getArticle(int num) {
		
		Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BoardVO article = null;
       

        try {
        	con = ConnUtil.getConnection();
        	String sql1="update board set readcount=readcount+1 where num=?";
        	pstmt = con.prepareStatement(sql1);
        	pstmt.setInt(1, num);
        	pstmt.executeUpdate();
        	
        	String sql2 ="select * from board where num=?";
        	pstmt = con.prepareStatement(sql2);
        	pstmt.setInt(1, num);
        	rs = pstmt.executeQuery();
        	if(rs.next()) {
        		article = new BoardVO();
        		article.setNum(rs.getInt("num"));
				article.setWriter(rs.getString("writer"));
				article.setEmail(rs.getString("email"));
				article.setSubject(rs.getString("subject"));
				article.setPass(rs.getString("pass"));
				article.setRegdate(rs.getTimestamp("regdate"));
				article.setReadcount(rs.getInt("readcount"));
				article.setRef(rs.getInt("ref"));
				article.setStep(rs.getInt("step"));
				article.setDepth(rs.getInt("depth"));
				article.setContent(rs.getString("content"));
				article.setIp(rs.getString("ip"));

        	}
            
        } catch (SQLException ss) {
            ss.printStackTrace();
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
       return article;
	}
   
}