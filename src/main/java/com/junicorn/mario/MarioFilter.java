package com.junicorn.mario;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.junicorn.mario.route.Route;
import com.junicorn.mario.route.RouteMatcher;
import com.junicorn.mario.route.Routers;
import com.junicorn.mario.util.PathUtil;
import com.junicorn.mario.util.ReflectUtil;

public class MarioFilter implements Filter {
	
	private RouteMatcher routeMatcher = new RouteMatcher(new ArrayList<Route>());
	
	private ServletContext servletContext;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Mario mario = Mario.me();
		if(!mario.isInit()){
			Routers routers = mario.getRouters();
			if(null != routers){
				routeMatcher.setRoutes(routers.getRoutes());
			}
			servletContext = filterConfig.getServletContext();
			mario.setInit(true);
		}
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        // 请求的uri
        String uri = PathUtil.getRelativePath(request);
        
        Route route = routeMatcher.findRoute(uri);
        
        // 如果找到
		if (route != null) {
			// 实际执行方法
			handle(request, response, route);
		} else{
			chain.doFilter(request, response);
		}
	}
	
	private void handle(HttpServletRequest request, HttpServletResponse response, Route route){
		
		// 初始化上下文
		MarioContext.initContext(servletContext, request, response);
		
		Object controller = route.getController();
		// 要执行的路由方法
		Method actionMethod = route.getAction();
		// 执行route方法
		executeMethod(controller, actionMethod, request, response);
	}
	
	/**
	 * 获取方法内的参数
	 */
	private Object[] getArgs(HttpServletRequest request, HttpServletResponse response, Class<?>[] params){
		
		int len = params.length;
		Object[] args = new Object[len];
		
		for(int i=0; i<len; i++){
			Class<?> paramTypeClazz = params[i];
			if(paramTypeClazz.getName().equals(HttpServletRequest.class.getName())){
				args[i] = request;
			}
			if(paramTypeClazz.getName().equals(HttpServletResponse.class.getName())){
				args[i] = response;
			}
		}
		
		return args;
	}
	
	/**
	 * 执行路由方法
	 */
	private Object executeMethod(Object object, Method method, HttpServletRequest request, HttpServletResponse response){
		int len = method.getParameterTypes().length;
		method.setAccessible(true);
		if(len > 0){
			Object[] args = getArgs(request, response, method.getParameterTypes());
			return ReflectUtil.invokeMehod(object, method, args);
		} else {
			return ReflectUtil.invokeMehod(object, method);
		}
	}

	@Override
	public void destroy() {
	}

}
