package com.mvcmember.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class StudentDAO {

    private static volatile StudentDAO instance = null;
    private static DataSource dataSource = null;

    private StudentDAO() {
    }

    public static StudentDAO getInstance() {
        if (instance == null) {
            synchronized (StudentDAO.class) {
                if (instance == null) {
                    instance = new StudentDAO();
                }
            }
        }
        return instance;
    }

    // 연결 실패 시 null 대신 SQLException을 던진다
    private Connection getConnection() throws SQLException {
        try {
            if (dataSource == null) {
                synchronized (StudentDAO.class) {
                    if (dataSource == null) {
                        Context init = new InitialContext();
                        dataSource = (DataSource) init.lookup("java:comp/env/jdbc/myOracle");
                    }
                }
            }
            return dataSource.getConnection();
        } catch (NamingException ne) {
            System.out.println("Connection 생성실패!! (JNDI lookup 오류)");
            throw new SQLException("JNDI lookup 실패: jdbc/myOracle", ne);
        }
    }

    // 아이디 중복 확인: 존재하면 true
    public boolean idCheck(String id) {
        boolean result = true;
        String sql = "select id from student where id=?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (!rs.next()) result = false;
            }

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return result;
    }// end idCheck

    // 우편번호 검색
    public Vector<ZipcodeVO> zipcodeRead(String dong) {
        Vector<ZipcodeVO> vecList = new Vector<ZipcodeVO>();
        String sql = "select * from zipcode where dong like ?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, dong + "%");
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ZipcodeVO tempZipcode = new ZipcodeVO();
                    tempZipcode.setZipcode(rs.getString("zipcode"));
                    tempZipcode.setSido(rs.getString("sido"));
                    tempZipcode.setGugun(rs.getString("gugun"));
                    tempZipcode.setDong(rs.getString("dong"));
                    tempZipcode.setRi(rs.getString("ri"));
                    tempZipcode.setBunji(rs.getString("bunji"));

                    vecList.addElement(tempZipcode);
                }
            }

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return vecList;
    }// end zipcodeRead

    // 회원가입
    public boolean memberInsert(StudentVO vo) {
        boolean flag = false;
        String sql = "insert into student"
                + "(id, pass, name, phone1, phone2, phone3, email, zipcode, address1, address2) "
                + "values(?,?,?,?,?,?,?,?,?,?)";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

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
            if (count > 0) flag = true;

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return flag;
    }// end memberInsert

    // 로그인: -1 아이디 없음, 0 비밀번호 오류, 1 성공
    public int loginCheck(String id, String pass) {
        int check = -1;
        String sql = "select pass from student where id=?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String dbPass = rs.getString("pass");
                    if (pass != null && pass.equals(dbPass)) {
                        check = 1; // 비밀번호 일치
                    } else {
                        check = 0; // 비밀번호 오류
                    }
                }
            }

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return check;
    }// end loginCheck

    // 회원 정보 조회
    public StudentVO getMember(String id) {
        StudentVO vo = null;
        String sql = "select * from student where id=?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    vo = new StudentVO();
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
            }

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return vo;
    }// end getMember

    // 회원 정보 수정
    public void updateMember(StudentVO vo) {
        String sql = "update student set pass=?, phone1=?, phone2=?, phone3=?, "
                + "email=?, zipcode=?, address1=?, address2=? where id=?";

        try (Connection con = getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, vo.getPass());
            pstmt.setString(2, vo.getPhone1());
            pstmt.setString(3, vo.getPhone2());
            pstmt.setString(4, vo.getPhone3());
            pstmt.setString(5, vo.getEmail());
            pstmt.setString(6, vo.getZipcode());
            pstmt.setString(7, vo.getAddress1());
            pstmt.setString(8, vo.getAddress2());
            pstmt.setString(9, vo.getId());
            pstmt.executeUpdate();

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
    }// end updateMember

    // 회원 탈퇴: -1 id 없음, 0 비밀번호 오류, 1 성공
    public int deleteMember(String id, String pass) {
        int result = -1;
        String sql1 = "select pass from student where id=?";
        String sql2 = "delete from student where id=?";

        try (Connection con = getConnection()) {

            String dbPass = null;
            boolean exists = false;

            // 1) 비밀번호 조회 (자원은 여기서 바로 닫는다)
            try (PreparedStatement pstmt1 = con.prepareStatement(sql1)) {
                pstmt1.setString(1, id);
                try (ResultSet rs = pstmt1.executeQuery()) {
                    if (rs.next()) {
                        exists = true;
                        dbPass = rs.getString("pass");
                    }
                }
            }

            // 2) 비교 후 삭제
            if (exists) {
                if (pass != null && pass.equals(dbPass)) {
                    try (PreparedStatement pstmt2 = con.prepareStatement(sql2)) {
                        pstmt2.setString(1, id);
                        pstmt2.executeUpdate();
                    }
                    result = 1; // 회원탈퇴 성공
                } else {
                    result = 0; // 비밀번호 오류
                }
            }

        } catch (SQLException ss) {
            ss.printStackTrace();
        }
        return result;
    }// end deleteMember

}