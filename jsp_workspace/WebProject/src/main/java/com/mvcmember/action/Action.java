package com.mvcmember.action;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.connector.Response;
import com.mvcmember.control.ActionForward;

public interface Action {

	public ActionForward execute(HttpServletRequest request,
			HttpServletResponse response) throws IOException;
}
