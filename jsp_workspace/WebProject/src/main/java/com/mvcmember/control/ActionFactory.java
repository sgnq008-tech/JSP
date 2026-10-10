package com.mvcmember.control;

import com.mvcmember.action.RegFormAction;
import com.mvcmember.action.Action;
import com.mvcmember.action.IdCheckAction;
import com.mvcmember.action.IndexAction;
import com.mvcmember.action.RegProcAction;
import com.mvcmember.action.ZipCheckAction;
import com.mvcmember.action.LoginFormAction;

// 클라이언트의 요청(cmd 명령어)에 따라 알맞은 Action 객체를 생성해 반환하는 팩토리(Factory) 클래스
public class ActionFactory {

    // 싱글톤 패턴을 적용하기 위한 유일한 Factory 인스턴스 변수
    private static ActionFactory factory;

    // 외부에서 직접 객체를 생성하지 못하도록 기본 생성자를 private으로 선언
    private ActionFactory() { }

    // 외부에서 단 하나의 Factory 객체만 공유받아 사용할 수 있도록 하는 정적(Static) 메소드 (멀티스레드 동기화 처리)
    public static synchronized ActionFactory getInstance() {
        if(factory == null) {
            factory = new ActionFactory();
        }
        return factory;
    }

    // 전달받은 명령어(cmd) 문자열에 따라 어떤 비즈니스 로직(Action)을 수행할지 결정하고 객체를 생성하는 메소드
    public Action getAction(String cmd) {
        Action action = null;

        // 명령어 값에 따른 분기 처리 (Switch문)
        switch(cmd) {
            case "index":
                action = new IndexAction(); // 메인 인덱스 페이지 요청 처리 액션
                break;

            case "regForm":
                action = new RegFormAction(); // 회원가입 폼 화면 요청 처리 액션
                break;

            case "regProc":
                action = new RegProcAction(); // 회원가입 데이터 처리(DB insert) 요청 액션
                break;

            case "login":
                action = new LoginFormAction(); // 로그인 폼 화면 요청 처리 액션
                break;

            /*
             * [추후 구현 예정인 액션 목록 주석 처리]
             * case "loginProc": action = new LoginProcAction(); break; // 로그인 인증 처리
             * case "logout": action = new LogoutAction(); break; // 로그아웃 처리
             * case "modifyForm": action = new ModifyFormAction(); break; // 회원정보 수정 폼
             * case "modifyProc": action = new ModifyProcAction(); break; // 회원정보 수정 처리
             * case "deleteForm": action = new DeleteFormAction(); break; // 회원탈퇴 폼
             * case "deleteProc": action = new DeleteProcAction(); break; // 회원탈퇴 처리
             */

            case "idCheck":
                action = new IdCheckAction(); // 회원가입 시 아이디 중복 확인 요청 액션
                break;

            case "zipCheck":
                action = new ZipCheckAction(); // 우편번호 검색 요청 처리 액션
                break;

            default:
                action = new IndexAction(); // 지정되지 않은 명령어나 기본 요청 시 메인으로 이동
                break;
        }

        // 생성된 Action 객체를 컨트롤러(Controller 서블릿)로 반환
        return action;
    }
}