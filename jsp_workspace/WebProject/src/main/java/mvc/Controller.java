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

import org.apache.commons.collections4.map.HashedMap;

// 클라이언트의 모든 요청을 받아 처리하는 프론트 컨트롤러(Front Controller) 서블릿 클래스
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

	// 명령어(예: /index.do)와 그 명령어를 처리할 실제 클래스 객체를 쌍으로 저장하는 맵(Map)
	private HashedMap<String, Object> commandMap = new HashedMap<String, Object>();

	/*
	 * 서블릿이 처음 생성될 때(서버가 켜질 때 또는 최초 요청 시) 한 번 실행되는 초기화 메소드
	 * web.xml에 설정된 properties 파일 경로를 읽어와 명령어와 클래스 매핑 정보를 메모리에 적재합니다.
	 */
	public void init(ServletConfig config) throws ServletException {
		// web.xml에 정의된 'propertyConfig' 초기 파라미터 값을 읽어옴 (예: Command.properties 파일명)
		String props = config.getInitParameter("propertyConfig");

		// 명령어와 처리 클래스의 매핑 정보를 읽어오기 위한 Properties 객체 생성
		Properties pr = new Properties();

		// 프로젝트 내부의 /WEB-INF 폴더 경로를 가져옴
		String path = config.getServletContext().getRealPath("/WEB-INF");
		FileInputStream f = null;

		try {
			// WEB-INF 폴더 안에 있는 설정 파일(Command.properties)을 스트림으로 읽음
			f = new FileInputStream(new File(path, props));

			// 파일 안의 설정 내용을 Properties 객체에 로드 (키=값 형태)
			pr.load(f);

		} catch (IOException e) {
			throw new ServletException(e);
		} finally {
			if (f != null) {
				try {
					f.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}

		// Properties에 저장된 키(명령어 목록)들을 하나씩 순회하기 위한 Iterator 객체 생성
		Iterator<Object> keyIter = pr.keySet().iterator();

		// 설정 파일에 등록된 명령어 개수만큼 반복하며 객체를 생성하여 Map에 저장
		while (keyIter.hasNext()) {
			String command = (String) keyIter.next();         // 명령어 추출 (예: /index.do)
			String className = pr.getProperty(command);       // 해당 명령어에 대응하는 클래스 이름 추출 (예: mvc.IndexProcess)

			try {
				// 문자열로 된 클래스 이름 실제 클래스로 로딩
				Class<?> commandClass = Class.forName(className);

				// 해당 클래스의 객체(인스턴스)를 동적으로 생성
				Object commandInstance = commandClass.newInstance();

				// commandMap에 명령어와 생성된 객체를 쌍으로 저장 (메모리에 등록)
				commandMap.put(command, commandInstance);

			} catch (ClassNotFoundException ce) {
				throw new ServletException(ce);
			} catch (InstantiationException ie) {
				throw new ServletException(ie);
			} catch (IllegalAccessException ia) {
				throw new ServletException(ia);
			}
		}
	}

	// GET 방식의 HTTP 요청을 처리하는 메소드 -> 공통 요청 처리 메소드(requestPro)로 위임
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request, response);
	}

	// POST 방식의 HTTP 요청을 처리하는 메소드 -> 공통 요청 처리 메소드(requestPro)로 위임
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		requestPro(request, response);
	}

	/*
	 * 사용자의 모든 요청(GET/POST)을 실제로 분석하고 처리하는 핵심 공통 메소드
	 * 1. 요청 주소(URI) 분석
	 * 2. commandMap에서 알맞은 처리 객체(Action) 가져오기
	 * 3. 비즈니스 로직 실행 후 이동할 뷰(JSP) 경로 리턴받기
	 * 4. 해당 뷰로 포워딩(Forward) 처리
	 */
	protected void requestPro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String view = null;         // 이동할 뷰(JSP 페이지)의 경로를 담을 변수
		CommandProcess com = null;  // 작업을 처리할 인터페이스 타입 변수

		try {
			// 사용자가 요청한 전체 URI 주소를 가져옴 (예: /WebProject/index.do)
			String command = request.getRequestURI();

			// 프로젝트 컨텍스트 경로(예: /WebProject)를 제외한 순수 명령어 주소만 추출
			if (command.indexOf(request.getContextPath()) == 0) {
				command = command.substring(request.getContextPath().length());
			}

			// 추출한 명령어를 키로 하여 commandMap에서 알맞은 처리 객체를 꺼내옴
			com = (CommandProcess) commandMap.get(command);

			// 해당 객체의 실행 메소드를 호출하여 비즈니스 로직을 수행하고, 결과로 이동할 JSP 페이지 경로를 돌려받음
			view = com.requestPro(request, response);

		} catch (Throwable e) {
			throw new ServletException(e);
		}

		// 리턴받은 뷰(JSP) 경로로 요청과 응답 객체를 싣고 포워딩(화면 전환) 수행
		RequestDispatcher dispatcher = request.getRequestDispatcher(view);
		dispatcher.forward(request, response);
	}
}