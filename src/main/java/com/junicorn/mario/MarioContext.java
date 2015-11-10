package com.junicorn.mario;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 当前线程上下文环境
 */
public final class MarioContext {

	private static final ThreadLocal<MarioContext> CONTEXT = new ThreadLocal<MarioContext>();

	private ServletContext context;
	private HttpServletRequest request;
	private HttpServletResponse response;

	private MarioContext() {
	}
	
    public static void initContext(ServletContext context, HttpServletRequest request, HttpServletResponse response) {
    	MarioContext marioContext = new MarioContext();
    	marioContext.context = context;
    	marioContext.request = request;
    	marioContext.response = response;
    	CONTEXT.set(marioContext);
    }
    
    public static void remove(){
    	CONTEXT.remove();
    }
	
	public ServletContext getContext() {
		return context;
	}

	public void setContext(ServletContext context) {
		this.context = context;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

}
