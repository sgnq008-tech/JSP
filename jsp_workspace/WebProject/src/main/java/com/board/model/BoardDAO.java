package com.board.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


// BoardVO, ConnUtil 은 같은 패키지(com.board.model)에 있어야 합니다.
public class BoardDAO {

	private static BoardDAO instance;

	public BoardDAO() {
	}

	public static BoardDAO getInstance() {
		if (instance == null) {
			synchronized (BoardDAO.class) {
				if (instance == null) {
					instance = new BoardDAO();
				}
			}
		}
		return instance;
	}

	// 전체 글 개수를 조회하여 반환
	public int getArticleCount() {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;

		try {
			con = ConnUtil.getConnection();
			pstmt = con.prepareStatement("select count(*) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);
			}
		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			close(rs, pstmt, con);
		}
		return x;
	}// end getArticleCount

	// 페이지 범위(start ~ end)의 글 목록 조회
	public List<BoardVO> getArticles(int start, int end) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<BoardVO> articleList = null;

		try {
			con = ConnUtil.getConnection();

			String sql = "select * from (select rownum rnum, num,"
					+ " writer, email, subject, pass, regdate, readcount, "
					+ "ref, step, depth, content, ip from "
					+ "(select * from board order by ref desc, step asc)) "
					+ "where rnum >= ? and rnum <= ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, start);
			pstmt.setInt(2, end);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				articleList = new ArrayList<BoardVO>(end - start + 1);
				do {
					articleList.add(makeArticle(rs));
				} while (rs.next());
			}
		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			close(rs, pstmt, con);
		}
		return articleList;
	}// end getArticles

	// 글 상세 조회 (조회수 1 증가 후 조회) - ContentAction에서 사용
	public BoardVO getArticle(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		BoardVO article = null;

		try {
			con = ConnUtil.getConnection();

			pstmt = con.prepareStatement("update board set readcount = readcount + 1 where num = ?");
			pstmt.setInt(1, num);
			pstmt.executeUpdate();
			pstmt.close();

			pstmt = con.prepareStatement("select * from board where num = ?");
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				article = makeArticle(rs);
			}
		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			close(rs, pstmt, con);
		}
		return article;
	}// end getArticle

	// 데이터베이스에 글을 저장함
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
			con = ConnUtil.getConnection();

			pstmt = con.prepareStatement("select max(num) from board");
			rs = pstmt.executeQuery();

			if (rs.next())
				number = rs.getInt(1) + 1;
			else
				number = 1;
			rs.close();
			pstmt.close();

			if (num != 0) { // 답변글일 때
				sql = "update board set step = step + 1 where ref = ? and step > ?";
				pstmt = con.prepareStatement(sql);
				pstmt.setInt(1, ref);
				pstmt.setInt(2, step);
				pstmt.executeUpdate();
				pstmt.close();

				step = step + 1;
				depth = depth + 1;
			} else { // 새 글일 때
				ref = number;
				step = 0;
				depth = 0;
			}

			sql = "insert into board(num, writer, email, subject, pass, "
					+ "regdate, ref, step, depth, content, ip) "
					+ "values(board_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, article.getWriter());
			pstmt.setString(2, article.getEmail());
			pstmt.setString(3, article.getSubject());
			pstmt.setString(4, article.getPass());
			pstmt.setTimestamp(5, article.getRegdate());
			pstmt.setInt(6, ref);   // 계산된 값 사용 (article.getRef() 아님)
			pstmt.setInt(7, step);  // 계산된 값 사용
			pstmt.setInt(8, depth); // 계산된 값 사용
			pstmt.setString(9, article.getContent());
			pstmt.setString(10, article.getIp());

			pstmt.executeUpdate();

		} catch (SQLException ss) {
			ss.printStackTrace();
		} finally {
			close(rs, pstmt, con);
		}
	}// end insertArticle

	// ResultSet 한 행을 BoardVO로 변환
	private BoardVO makeArticle(ResultSet rs) throws SQLException {
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
		return article;
	}

	// 자원 정리
	private void close(ResultSet rs, PreparedStatement pstmt, Connection con) {
		if (rs != null)
			try { rs.close(); } catch (SQLException e) { }
		if (pstmt != null)
			try { pstmt.close(); } catch (SQLException e) { }
		if (con != null)
			try { con.close(); } catch (SQLException e) { }
	}
	
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

public int getArticleCount(String find, String find_box) {
	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	int x = 0;

	try {
	    con = ConnUtil.getConnection();

	    // 컬럼명은 ? 로 바인딩할 수 없으므로 허용된 값만 직접 사용
	    String column = "writer";
	    if ("subject".equals(find)) {
	        column = "subject";
	    } else if ("content".equals(find)) {
	        column = "content";
	    }

	    String sql = "select count(*) from board where " + column + " like ?";
	    pstmt = con.prepareStatement(sql);
	    pstmt.setString(1, "%" + find_box + "%");
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

public List<BoardVO> getArticles(String find, String find_box, int start, int end) {

Connection con = null;
PreparedStatement pstmt = null;
ResultSet rs = null;
List<BoardVO> articleList = null;

try {

	con = ConnUtil.getConnection();
	StringBuffer sql = new StringBuffer();
	sql.append("select * from");
	sql.append("select rownum rnum, num,"
			+ " writer, email,subject, pass, regdate, readcount,"
			+ "ref, step, depth, content, ip from");
	if(find.equals("writer")) {
		sql.append("(select * from board writer=? order by ref desc, step asc))"
				+ "			+ \"where rnum >=? and rnum <=?");
		pstmt= con.prepareStatement(sql.toString());
		pstmt.setString(1, find_box);
		pstmt.setInt(2, start);
		pstmt.setInt(3, end);
	}else if(find.equals("content")) {
		sql.append("(select * from board content=? order by ref desc, step asc))"
				+ "			+ \"where rnum >=? and rnum <=?");
	pstmt.setInt(1, start);
	pstmt.setInt(2, end);
	}else if(find.equals("subject")) {
		sql.append("(select * from board subject=? order by ref desc, step asc))"
				+ "			+ \"where rnum >=? and rnum <=?");
	pstmt.setInt(1, start);
	pstmt.setInt(2, end);
	}
	
	rs = pstmt.executeQuery();
	
	if(find.equals("writer")) {
		pstmt = con.prepareStatement("select count (*) from board where writer=?");
		pstmt.setString(1, find_box);
	}else if(find.equals("subject")) {
		pstmt = con.prepareStatement("select count (*) from board where subject like'%"+find_box+"%'order by");
	}else if(find.equals("content")) {
		pstmt = con.prepareStatement("select count (*) from board where content like'%"+find_box+"%'order by");
	}else {
		pstmt=con.prepareStatement("select count (*) from board");
	}
	
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