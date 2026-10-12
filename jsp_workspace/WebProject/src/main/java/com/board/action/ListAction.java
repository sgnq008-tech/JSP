package com.board.action;

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.board.model.BoardDAO;
import com.board.model.BoardVO; // BoardDAO도 같은 BoardVO를 import 해야 함

public class ListAction implements CommandAction {

	@Override
	public String requestPro(HttpServletRequest request, HttpServletResponse response) throws Throwable {

		// 1. 사용자로부터 페이지 번호(pageNum)를 받아옴 (없거나 잘못된 값이면 1페이지)
		String pageNum = request.getParameter("pageNum");
		int currentPage = 1;
		if (pageNum != null) {
			try {
				currentPage = Integer.parseInt(pageNum.trim());
			} catch (NumberFormatException e) {
				currentPage = 1;
			}
			if (currentPage < 1) {
				currentPage = 1;
			}
		}

		// 2. 페이지당 보여줄 게시글 수 및 범위 계산
		int pageSize = 10;
		int startRow = (currentPage - 1) * pageSize + 1; // 한 페이지의 시작 글 번호
		int endRow = currentPage * pageSize;             // 한 페이지의 마지막 글 번호

		// 3. 전체 글 개수 조회 및 페이징된 글 목록 가져오기
		BoardDAO dbPro = BoardDAO.getInstance();
		int count = dbPro.getArticleCount();

		List<BoardVO> articleList = null;
		if (count > 0) {
			articleList = dbPro.getArticles(startRow, endRow);
		}
		if (articleList == null) {
			articleList = Collections.emptyList();
		}

		// 화면에 출력할 가상의 글 번호 계산
		int number = count - (currentPage - 1) * pageSize;

		// 4. JSP(View)에서 사용할 데이터를 request 객체에 저장
		request.setAttribute("currentPage", currentPage);
		request.setAttribute("startRow", startRow);
		request.setAttribute("endRow", endRow);
		request.setAttribute("count", count);
		request.setAttribute("pageSize", pageSize);
		request.setAttribute("number", number);
		request.setAttribute("articleList", articleList);

		request.setAttribute("bodyback_c", "#e0f8f8"); // 배경색 (연한 민트)
		request.setAttribute("value_c", "#c0edf8");    // 헤더 및 포인트 색상

		// 5. 이동할 뷰(JSP)의 경로 리턴
		return "/board/list.jsp";
	}
}