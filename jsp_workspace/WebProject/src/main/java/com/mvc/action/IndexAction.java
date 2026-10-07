package com.mvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvc.control.ActionForward;

public class IndexAction implements Action{

   @Override
   public ActionForward execite(HttpServletRequest requset, HttpServletResponse response) throws IOException {
      // TODO Auto-generated method stub
	   System.out.println("IndexAction의 execute()수행됨!!!!");
      return new ActionForward("index.jsp", false);
   }

}