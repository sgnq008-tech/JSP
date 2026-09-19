package com.boardone;

import java.sql.Timestamp;

// 게시판의 데이터를 담아서 운반하는 VO(Value Object) / JavaBean 클래스입니다.
public class BoardVO {

	// --- 게시판 테이블 컬럼과 1:1 대응되는 멤버 변수 선언 ---
	private int num;          // 글 번호 (Primary Key)
	private String writer;    // 작성자 이름
	private String email;     // 이메일 주소
	private String subject;   // 글 제목
	private String pass;      // 비밀번호
	private int readcount;    // 조회수
	private int ref;          // 글 그룹 번호 (계층형 게시판용)
	private int step;         // 답변 글 정렬 순서
	private int depth;        // 답변 글 들여쓰기 깊이
	private Timestamp regdate;// 작성일시
	private String content;   // 글 내용
	private String Ip;        // 작성자 IP 주소

	// --- 번호(num) Getter / Setter ---
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	// --- 작성자(writer) Getter / Setter ---
	public String getWriter() {
		return writer;
	}

	public void setWriter(String writer) {
		this.writer = writer;
	}

	// --- 이메일(email) Getter / Setter ---
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// --- 제목(subject) Getter / Setter ---
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	// --- 비밀번호(pass) Getter / Setter ---
	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	// --- 조회수(readcount) Getter / Setter ---
	public int getReadcount() {
		return readcount;
	}

	public void setReadcount(int readcount) {
		this.readcount = readcount;
	}

	// --- 그룹 번호(ref) Getter / Setter ---
	public int getRef() {
		return ref;
	}

	public void setRef(int ref) {
		this.ref = ref;
	}

	// --- 정렬 순서(step) Getter / Setter ---
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	// --- 들여쓰기 깊이(depth) Getter / Setter ---
	public int getDepth() {
		return depth;
	}

	public void setDepth(int depth) {
		this.depth = depth;
	}

	// --- 작성일시(regdate) Getter / Setter ---
	public java.sql.Timestamp getRegdate() {
		return regdate;
	}

	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}

	// --- 내용(content) Getter / Setter ---
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	// --- IP 주소(Ip) Getter / Setter ---
	public String getIp() {
		return Ip;
	}

	public void setIp(String Ip) {
		this.Ip = Ip;
	}

}