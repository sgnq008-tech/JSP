package com.mvc.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mvc.control.ActionForward;

public interface Action {
   
   public ActionForward execite(HttpServletRequest requset,
         HttpServletResponse response) throws IOException;
}