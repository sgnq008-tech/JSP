package com.boardone;

import java.sql.*;
import java.util.*;
import javax.swing.text.AbstractDocument.Content;
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

			if (num != 0) { // 답변글일때

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

	public List<BoardVO> getArticles(int start, int end) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;

		try {

			con = ConnUtil.getConnection();
			
			//String sql = "select * from board order by num desc";
			
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
	}//end getArticle
	
	/* 글 수정 버튼을 클릭했을 경우 updateForm.jsp로 이동하여 글 수정하면을 출력한다.
	 * 
	 * 글 수정시에는 글 목록보기와 다르게 조회수를 증가 시킬 필요가 없다.
	 * 
	 * 조회수를 증가시키는 부분을 제외하고 num에 해당하는 게시글만 가져오는 메소드 구현
	 */
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
	}// end updategetArticle
  
	
	/* 글 수정 처리
	 * 
	 *  updateForm.jsp에서 비밀번호를 입력하고 글 수정 법튼을 클릭한다.
	 *  그러면 데이터베이스에서 글이 수정처리를 하도록 메소드를 구현한다.
	 */
	
	public int updateArticle(BoardVO article) {
		
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
             pstmt.setInt(1, article.getNum());
             rs = pstmt.executeQuery();
             
             if(rs.next()) {
            	 dbpasswd = rs.getString("pass");
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
            		 result = 1;
            	 }else {
            		 result = 0;
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
	
	/* 글 내용 보기화면에서 글 삭제 버튼을 클릭하면 삭제 처리를 하도록 한다.
	 * 글 삭제처리시 비밀번호를 입력받아 데이터베이스의 비밀번호와 일치하면
	 * 글삭제를 처리하고, 그렇지 않으면 비밀번호가 틀렸다고 알려준다.
	 * 
	 * 글 삭제를 처리하는 메소드를 구현함
	 */
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
        		 result = 1;
        	 }else {
        		 result = 0;
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
	
	//--------------검색 기능 구현
	// 검색한 내용이 몇개인지를 반환하는 메소드 구현(What: 검색조건, content: 검색내용
public int getArticleCount(String what, String content) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;

		try {
			con = ConnUtil.getConnection();
			// 수정: where / 컬럼명 / like 사이에 공백이 없어서
			// "wherewriter like..." 처럼 붙어버려 SQL 문법 오류가 나던 부분
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

/* 검색한 내용을 리스트로 받아옴(what, content, start, end) 시작번호와 끝번호는 페이징 처리용
 * 
 */

public List<BoardVO> getArticles(String what, String content, int start, int end) {
	
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	List<BoardVO> articleList = null;

	try {

		con = ConnUtil.getConnection();
		
		//String sql = "select * from board order by num desc";
		
		/*
		 * String sql = "select * from (select rownum rnum, num," +
		 * " writer, email,subject, pass, regdate, readcount, " +
		 * "ref, step, depth, content, ip from" +
		 * "(select * from board order by ref desc, step asc)) " +
		 * "where rnum >=? and rnum <=?";
		 */
		
		// 수정: "where "+what+" like '%"+content+"%' order by..." 처럼
		// 단어 사이 공백을 넣어야 함 (기존엔 wherewriterlike'%...%'order by 로 다 붙어있었음)
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
}
	
}