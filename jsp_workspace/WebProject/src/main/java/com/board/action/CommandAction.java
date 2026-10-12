package com.board.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.Response;

public interface CommandAction {

  public String requestPro(HttpServletRequest request,
		  HttpServletResponse response) throws Throwable; 
}
