package com.boardone;

import java.sql.*;
import java.util.*;
import javax.swing.text.AbstractDocument.Content;
import org.eclipse.jdt.internal.compiler.ast.ReturnStatement;

public class BoardDAO {

	// 싱글톤 패턴을 위한 정적 인스턴스 선언
	private static BoardDAO instance = null;

	public BoardDAO() {
	}

	// 외부에서 DAO 객체를 얻어갈 때 사용하는 싱글톤 메소드 (동기화 처리 포함)
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

	// 1. 데이터베이스에 새글 또는 답변글을 저장하는 메소드
	public void insertArticle(BoardVO article) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		int num = article.getNum();
		int ref = article.getRef();
		int step = article.getStep();
		int depth = article.getDepth();
		int number = 0;
		String sql = "";

		try {
			// 데이터베이스 연결
			con = ConnUtil.getConnection();

			// 글 번호(num)의 최댓값을 구해 새로운 글 번호(number) 지정 기준 마련
			sql = "select max(num) from board";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next())
				number = rs.getInt(1) + 1;
			else
				number = 1;

			if (num != 0) { // 답변글일 때
				// 기존 답변글들의 step 순서를 재정렬
				sql = "update board set step=step + 1 where ref=? and step > ?";
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

			// 글을 데이터베이스에 저장할 쿼리문 작성 (시퀀스 사용)
			sql = "insert into board(num,writer,email,subject,pass," + "regdate,ref,step,depth,content,ip) "
					+ "values(board_seq.nextval,?,?,?,?,?,?,?,?,?,?)";

			pstmt = con.prepareStatement(sql);

			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPass());
			pstmt.setTimestamp(5, article.getRegdate());
			pstmt.setInt(6, article.getRef());
			pstmt.setInt(7, article.getStep());
			pstmt.setInt(8, article.getDepth());
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getIp());

			pstmt.executeUpdate();

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			// 사용한 자원 역순으로 안전하게 해제
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
	}// end insertArticle

	// 2. 전체 글의 개수를 가져오는 메소드
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
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return x;
	}// end getArticleCount

	// 3. 페이징 처리를 반영하여 지정한 범위(start ~ end)만큼의 글 목록을 가져오는 메소드
	public List<BoardVO> getArticles(int start, int end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;

		try {
			con = ConnUtil.getConnection();

			// 오라클 ROWNUM과 서브쿼리를 이용한 페이징 처리 쿼리 (ref 내림차순, step 오름차순 정렬)
			String sql = "select * from (select rownum rnum, num,"
					+ " writer, email,subject, pass, regdate, readcount, "
					+ "ref, step, depth, content, ip from"
					+ "(select * from board order by ref desc, step asc)) "
					+ "where rnum >=? and rnum <=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				articleList = new ArrayList<BoardVO>(end-start+1);

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
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return articleList;
	}// end getArticles

	// 4. 글 번호(num)를 받아 하나의 글 상세 정보를 가져오고, 조회수를 1 증가시키는 메소드
	public BoardVO getArticle(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;

		try {
			con = ConnUtil.getConnection();

			// 상세보기에 진입할 때 조회수 1 증가
			String sql1="update board set readcount=readcount+1 where num=?";
			pstmt = con.prepareStatement(sql1);
			pstmt.setInt(1, num);
			pstmt.executeUpdate();

			// 해당 글 번호의 데이터를 조회
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
	}//end getArticle

	// 5. 글 수정 폼을 위해 조회수 증가 없이 특정 글 정보만 가져오는 메소드
	public BoardVO updateGetArticle(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;

		try {
			con = ConnUtil.getConnection();
			String sql="select * from board where num=?";
			pstmt = con.prepareStatement(sql);
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
	}// end updateGetArticle

	// 6. 입력받은 비밀번호를 검증한 후 글 내용을 수정하는 메소드
	public int updateArticle(BoardVO article) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd="";
		String sql="";
		int result = -1;

		try {
			con = ConnUtil.getConnection();

			// DB에 저장된 비밀번호 조회
			sql = "select pass from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, article.getNum());
			rs = pstmt.executeQuery();

			if(rs.next()) {
				dbpasswd = rs.getString("pass");
				// 사용자가 입력한 비밀번호와 DB 비밀번호가 일치할 경우에만 수정 수행
				if(dbpasswd.equals(article.getPass())) {
					sql = "update board set writer=?, email=?, "
							+ "subject=?, content=? where num=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setString(1, article.getWriter());
					pstmt.setString(2, article.getEmail());
					pstmt.setString(3, article.getSubject());
					pstmt.setString(4, article.getContent());
					pstmt.setInt(5, article.getNum());

					pstmt.executeUpdate();
					result = 1; // 수정 성공
				} else {
					result = 0; // 비밀번호 불일치
				}
			}

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return result;
	}// end updateArticle

	// 7. 입력받은 비밀번호를 검증한 후 글을 삭제하는 메소드
	public int deletArticle(int num, String pass) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbpasswd="";
		String sql="";
		int result = -1;

		try {
			con = ConnUtil.getConnection();
			sql = "select pass from board where num=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				dbpasswd = rs.getString("pass");
				if(dbpasswd.equals(pass)) {
					sql = "delete from board where num=?";
					pstmt = con.prepareStatement(sql);
					pstmt.setInt(1,num);
					pstmt.executeUpdate();
					result = 1; // 삭제 성공
				} else {
					result = 0; // 비밀번호 불일치
				}
			}

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return result;
	}// end deleteArticle

	// 8. 검색 조건(what)과 검색어(content)에 일치하는 글의 총 개수를 반환하는 메소드
	public int getArticleCount(String what, String content) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;

		try {
			con = ConnUtil.getConnection();
			String sql = "select count(*) from board where "
					+ what + " like '%" + content + "%'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return x;
	}// end getArticleCount (검색용)

	// 9. 검색 조건과 페이징 처리를 모두 반영하여 검색된 글 목록 리스트를 가져오는 메소드
	public List<BoardVO> getArticles(String what, String content, int start, int end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;

		try {
			con = ConnUtil.getConnection();

			// 검색 조건(what like '%content%')과 페이징 처리를 결합한 쿼리
			String sql = "select * from (select rownum rnum, num,"
					+ " writer, email,subject, pass, regdate, readcount, "
					+ "ref, step, depth, content, ip from"
					+ "(select * from board where " + what + " like '%" + content + "%' order by ref desc, step asc)) "
					+ "where rnum >=? and rnum <=?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				articleList = new ArrayList<BoardVO>(5);

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
			if (rs != null) try { rs.close(); } catch (SQLException e) {}
			if (pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
			if (con != null) try { con.close(); } catch (SQLException e) {}
		}
		return articleList;
	}// end getArticles (검색용)

}