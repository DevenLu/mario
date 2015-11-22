package com.junicorn.mario;

import java.lang.reflect.Method;

import com.junicorn.mario.config.ConfigLoader;
import com.junicorn.mario.route.Routers;
import com.junicorn.mario.servlet.wrapper.Request;
import com.junicorn.mario.servlet.wrapper.Response;

/**
 * Mario
 * @author biezhi
 *
 */
public final class Mario {

	/**
	 * 存放所有路由
	 */
	private Routers routers;
	
	/**
	 * 配置加载器
	 */
	private ConfigLoader configLoader;
	
	/**
	 * 框架是否已经初始化
	 */
	private boolean init = false;
	
	private Mario() {
		routers = new Routers();
		configLoader = new ConfigLoader();
	}
	
	public boolean isInit() {
		return init;
	}

	public void setInit(boolean init) {
		this.init = init;
	}
	
	private static class MarioHolder {
		private static Mario ME = new Mario();
	}
	
	public static Mario me(){
		return MarioHolder.ME;
	}
	
	public Mario addConf(String conf){
		configLoader.load(conf);
		return this;
	}
	
	public String getConf(String name){
		return configLoader.getConf(name);
	}
	
	public Mario addRoutes(Routers routers){
		this.routers.addRoute(routers.getRoutes());
		return this;
	}

	public Routers getRouters() {
		return routers;
	}
	
	public Mario addRoute(String path, String methodName, Object controller){
		try {
			Method method = controller.getClass().getMethod(methodName, Request.class, Response.class);
			this.routers.addRoute(path, method, controller);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return this;
	}
	
}
