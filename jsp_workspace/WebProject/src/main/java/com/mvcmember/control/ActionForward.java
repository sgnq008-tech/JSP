package com.mvcmember.control;
// return 페이지(view)와 이동방식(redirect,forward)
public class ActionForward {

	private String url;
	private boolean redirect;
	
	public ActionForward() {  }
	// 기본 생성자
	
	public ActionForward(String url) { 
	   this.url=url;
	}
	public ActionForward(String url,boolean redirect) {  
		this.url=url;
		this.redirect=redirect;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isRedirect() {
		return redirect;
	}

	public void setRedirect(boolean redirect) {
		this.redirect = redirect;
	}
	
}
