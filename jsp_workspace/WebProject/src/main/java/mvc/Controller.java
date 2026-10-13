package mvc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.View;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.jasper.runtime.ProtectedFunctionMapper;
import org.apache.tomcat.jni.FileInfo;


//@WebServlet("/*cdo")
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
    // 명령어와 명령어 처리 클래스를 쌍으로 저장
	private HashedMap<String,Object> commandMap = 
			new HashedMap<String,Object>();
	/* 명령어와 처리 클래스가 매핑되어있는 properties파일을 읽어서
	 * Map객체인 commandMap에 저장
	 */
	
	public void init(ServletConfig config)throws ServletException{
	//web.xml에서 propertyConfig에 해당하는 init-param의 값을 읽어본다.
	String props = config.getInitParameter("propertyConfig");
	
	// 명령어와 처리클래스의 매핑 정보를 저장할 properties 객체 생성
	Properties pr = new Properties();
	String path = config.getServletContext().getRealPath("/WEB-INF");
	FileInputStream f = null;
	try {
		// Command.properties 파일을 내용을 읽어옴 
		f=new FileInputStream(new File(path,props));
		// Command.properties 파일을 정보를 properties 객체에 저장
		pr.load(f);
	} catch (IOException e) {
	  throw new ServletException(e);	
	}finally {
		if(f !=null) {
			try {
				f.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		
		// Iterator 객체는 파일을 내용에 접근함
		Iterator<Object> keyIter = pr.keySet().iterator();
		
		// 객체는 하나씩 꺼내서 그 객체명으로 Properies 객체에 저장된 객체에 접근함
		while(keyIter.hasNext()) {
			String command = (String)keyIter.next();
			String className = pr.getProperty(command);
			try {
			// 해당 문자열을 클래스로 만든다
				Class commandClass = Class.forName(className);
				Object commandInstance = commandClass.newInstance();
				commandMap.put(command, commandInstance);
				
			}catch(ClassNotFoundException ce) {
				throw new ServletException(ce);
				
			}catch(InstantiationException ie) {
				throw new ServletException(ie);
				
			}catch(IllegalAccessException ia) {
				throw new ServletException(ia);
			}
		}
	}
	
	
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request,response);
	}
	
    // 사용자의 요청을 분석해서 해당 작업을 처리
	protected void requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    
		String view = null;
		CommandProcess com = null;
		
		try {
			String command = request.getRequestURI();
			if(command.indexOf(request.getContextPath())==0) {
				command = command.substring(request.getContextPath().length());
			}
			
			com = (CommandProcess)commandMap.get(command);
			view = com.requestPro(request, response); 
			
		}catch(Throwable e) {
		  throw new ServletException(e);
		}
		
		RequestDispatcher dispatcher = 
				request.getRequestDispatcher(view);
		dispatcher.forward(request, response);

	}

}
