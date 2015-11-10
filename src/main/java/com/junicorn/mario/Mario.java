package com.junicorn.mario;

import com.junicorn.mario.route.Routers;

public final class Mario {

	private Routers routers;
	
	private boolean init = false;
	
	private Mario() {
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
	
	public Mario routes(Routers routers){
		this.routers = routers;
		return this;
	}

	public Routers getRouters() {
		return routers;
	}
	
}
