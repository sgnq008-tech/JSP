package com.mvcmember.action;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvcmember.control.ActionForward;
import com.mvcmember.model.StudentDAO;
import com.mvcmember.model.ZipcodeVO;

public class ZipCheckAction implements Action {

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("utf-8");

        String check = request.getParameter("check");
        String dong = request.getParameter("dong");

        // 팝업을 처음 열 때(dong 없음)는 검색하지 않고 화면만 보여준다
        if (dong != null && !dong.trim().isEmpty()) {
            StudentDAO dao = StudentDAO.getInstance();
            Vector<ZipcodeVO> zipcodeList = dao.zipcodeRead(dong.trim());

            request.setAttribute("check", check);
            request.setAttribute("dong", dong);
            request.setAttribute("zipcodeList", zipcodeList);
            request.setAttribute("totalList", zipcodeList.size());
        }

        return new ActionForward("/mvcmember/zipCheck.jsp", false);
    }
}