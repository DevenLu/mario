package com.junicorn.mario.servlet.wrapper;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * HttpServletResponse增强
 * @author biezhi
 *
 */
public class Response {

	private HttpServletResponse raw;
	
	private OutputStream outputStream;
	
	public Response(HttpServletResponse httpServletResponse) {
		this.raw = httpServletResponse;
		try {
			this.outputStream = httpServletResponse.getOutputStream();
			raw.setHeader("Framework", "Mario");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void text(String text) {
		raw.setContentType("text/plan;charset=UTF-8");
		this.print(text);
	}

	public void html(String html) {
		raw.setContentType("text/html;charset=UTF-8");
		this.print(html);
	}
	
	private void print(String str){
		try {
			outputStream.write(str.getBytes());
			outputStream.flush();
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void cookie(String name, String value){
		Cookie cookie = new Cookie(name, value);
		raw.addCookie(cookie);
	}
	
	public HttpServletResponse getRaw() {
		return raw;
	}

	public OutputStream getOutputStream() {
		return outputStream;
	}

}