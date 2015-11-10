package com.junicorn.mario;

import com.junicorn.mario.route.RouteMatcher;
import com.junicorn.mario.route.Routers;

public final class Mario {

	private RouteMatcher routeMatcher;
	
	private Routers routers;
	
	private Mario() {
	}
	
	private static class MarioHolder {
		private static Mario ME = new Mario();
	}
	
	public static Mario me(){
		return MarioHolder.ME;
	}
	
	public Mario routes(Routers routers){
		this.routers = routers;
		return this;
	}
	
	public RouteMatcher getRouteMatcher(){
		if(null == this.routeMatcher){
			this.routeMatcher = new RouteMatcher(routers.getRoutes());
		}
		return this.routeMatcher;
	}
	
}
