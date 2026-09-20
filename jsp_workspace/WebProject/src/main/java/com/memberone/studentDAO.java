package com.memberone;
import java.rmi.StubNotFoundException;
import java.sql.*;
import java.util.Vector;

import javax.sql.*;
import javax.naming.*;

public class studentDAO {

	// 데이터베이스 커넥션풀(DBCP)을 통해 오라클 연결 객체를 얻어오는 메소드
	private Connection getConnection() {
		Connection con = null;

		try {
			Context init = new InitialContext();
			DataSource
					ds = (DataSource)init.lookup("java:comp/env/jdbc/myOracle");
			con = ds.getConnection();
		}catch(Exception e){
			e.printStackTrace();
		}
		return con;
	}

	// 아이디 중복 체크를 수행하는 메소드 (존재하면 true, 없으면 false 리턴)
	public boolean idCheck(String id) {
		boolean result = true;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			// 데이터베이스 연결
			con= getConnection();
			// 쿼리 실행
			String sql = "select*from student where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if(!rs.next()) result = false;

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			// 사용한 자원 역순으로 안전하게 해제
			if(rs != null) try {rs.close();}catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}

		return result;
	}//end idCheck

	// 동(dong) 이름으로 우편번호를 검색하여 리스트(Vector)로 반환하는 메소드
	public Vector<ZipcodeVO> zipcodeRead(String dong){

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Vector<ZipcodeVO> vecList = new Vector<ZipcodeVO>();

		try {
			con = getConnection();

			String sql = "select * from zipcode where "
					+"dong like '"+dong+"%'";
			pstmt = con.prepareStatement(sql);
			rs= pstmt.executeQuery();

			while(rs.next()) {
				ZipcodeVO tempZipcode = new ZipcodeVO();
				tempZipcode.setZipcode(rs.getString("zipcode"));
				tempZipcode.setSido(rs.getString("sido"));
				tempZipcode.setGugun(rs.getString("gugun"));
				tempZipcode.setDong(rs.getString("dong"));
				tempZipcode.setRi(rs.getString("ri"));
				tempZipcode.setBunji(rs.getString("bunji"));

				vecList.addElement(tempZipcode);
			}

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			if(rs != null) try {rs.close();}catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}
		return vecList;
	}//end zipcodeRead

	// 회원가입 처리 -> 실제로 데이터베이스의 회원 테이블에 정보를 저장하는 메소드
	public boolean memberInsert(studentVO vo) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean flag = false;
		try {
			con = getConnection();
			String sql ="insert into student values(?,?,?,?,?,?,?,?,?,?)";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getId());
			pstmt.setString(2, vo.getPass());
			pstmt.setString(3, vo.getName());
			pstmt.setString(4, vo.getPhone1());
			pstmt.setString(5, vo.getPhone2());
			pstmt.setString(6, vo.getPhone3());
			pstmt.setString(7, vo.getEmail());
			pstmt.setString(8, vo.getZipcode());
			pstmt.setString(9, vo.getAddress1());
			pstmt.setString(10, vo.getAddress2());

			int count = pstmt.executeUpdate();
			if(count > 0) flag = true;

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			if(rs != null) try {rs.close();}catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}
		return flag;
	}

	/* 로그인 구현
	 * 가입한 ID와 Password를 가지고 로그인을 처리함
	 * 입력한 ID와 Password를 데이터베이스의 정보와 비교하여 결과를 정수형으로 리턴함
	 * 1: 로그인 성공, 0: 비밀번호 오류, -1: 아이디 없음
	 */
	public int loginCheck(String id, String pass) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int check = -1; // 아이디 없음으로 초기화

		try {
			con = getConnection();
			String sql="select pass from student where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);

			rs = pstmt.executeQuery();

			if(rs.next()){
				String dbPass = rs.getString("pass");
				if(pass.equals(dbPass)) {
					check = 1; // 비밀번호 일치 (로그인 성공)
				}else {
					check = 0; // 비밀번호 오류
				}
			}

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			if(rs != null) try {rs.close();}catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}
		return check;

	}//end loginCheck

	/* 정보 수정
	 * 정보수정 버튼을 클릭하면 현재 로그인한 회원의 정보를 화면에 미리 보여주기 위해
	 * 세션에 설정된 아이디를 가지고 회원 정보를 조회하여 studentVO 객체로 리턴하는 메소드
	 */
	public studentVO getMember(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		studentVO vo = null;

		try {
			con = getConnection();
			String sql = "select * from student where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if(rs.next()) { // 해당 아이디에 대한 회원이 존재한다면 VO에 데이터 담기
				vo = new studentVO();
				vo.setId(rs.getString("id"));
				vo.setPass(rs.getString("pass"));
				vo.setName(rs.getString("name"));
				vo.setPhone1(rs.getString("phone1"));
				vo.setPhone2(rs.getString("phone2"));
				vo.setPhone3(rs.getString("phone3"));
				vo.setEmail(rs.getString("email"));
				vo.setZipcode(rs.getString("zipcode"));
				vo.setAddress1(rs.getString("address1"));
				vo.setAddress2(rs.getString("address2"));
			}

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			if(rs != null) try {rs.close();}catch(SQLException e) {}
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}
		return vo;
	}//end getMember

	/* 정보수정 처리를 위해 데이터베이스에서 update를 수행하는 메소드 */
	public void updateMember(studentVO vo) {
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			con = getConnection();
			String sql = "update student set pass=?, phone1=?, phone2=?, phone3=?,"
					+ "email=?, zipcode=?, address1=?, address2=? where id=?";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, vo.getPass());
			pstmt.setString(2,vo.getPhone1());
			pstmt.setString(3,vo.getPhone2());
			pstmt.setString(4,vo.getPhone3());
			pstmt.setString(5,vo.getEmail());
			pstmt.setString(6,vo.getZipcode());
			pstmt.setString(7,vo.getAddress1());
			pstmt.setString(8,vo.getAddress2());
			pstmt.setString(9,vo.getId());
			pstmt.executeUpdate();

		}catch(SQLException ss) {
			ss.printStackTrace();

		}finally {
			if(pstmt != null) try {pstmt.close();}catch(SQLException e) {}
			if(con != null) try {con.close();}catch(SQLException e) {}
		}

	}//end updateMember

	/* 회원탈퇴 처리
	 * 비밀번호를 입력받아 데이터베이스의 비밀번호와 일치하는지 확인한 후
	 * 일치하면 회원 데이터를 삭제하고 1을 리턴, 불일치하면 0을 리턴하는 메소드
	 */
	public int deleteMember(String id, String pass) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String dbPass ="";
		int result = -1; // 아이디가 존재하지 않는 경우 등 기본값

		try {
			con = getConnection();
			String sql1 = "select pass from student where id=?";
			pstmt= con.prepareStatement(sql1);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if(rs.next()) {
				dbPass = rs.getString("pass");

				if(dbPass.equals(pass)) { // 비밀번호 일치 시 삭제 실행
					String sql2 = "delete from student where id=?";
					pstmt= con.prepareStatement(sql2);
					pstmt.setString(1, id);
					pstmt.executeUpdate();
					result = 1; // 회원탈퇴 성공
				}else { // 본인 인증 실패 -> 비밀번호 오류
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
	}// end deleteMember
}