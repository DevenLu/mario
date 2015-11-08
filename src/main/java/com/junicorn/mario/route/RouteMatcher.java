package com.junicorn.mario.route;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.junicorn.mario.util.PathUtil;

/**
 * 路由匹配器，用于匹配路由
 * 
 * @author biezhi
 */
public class RouteMatcher {

	private List<Route> routes;

	public RouteMatcher(List<Route> routes) {
		this.routes = routes;
	}

	public Route findRoute(String path) {
		String cleanPath = parsePath(path);
		List<Route> matchRoutes = new ArrayList<Route>();
		for (Route route : this.routes) {
			if (matchesPath(route.getPath(), cleanPath)) {
				matchRoutes.add(route);
			}
		}
		// 优先匹配原则
        giveMatch(path, matchRoutes);
        
        return matchRoutes.size() > 0 ? matchRoutes.get(0) : null;
	}

	private void giveMatch(final String uri, List<Route> routes) {
		Collections.sort(routes, new Comparator<Route>() {
			@Override
			public int compare(Route o1, Route o2) {
				if (o2.getPath().equals(uri)) {
					return o2.getPath().indexOf(uri);
				}
				return -1;
			}
		});
	}

	private boolean matchesPath(String routePath, String pathToMatch) {
		routePath = routePath.replaceAll(PathUtil.VAR_REGEXP, PathUtil.VAR_REPLACE);
		return pathToMatch.matches("(?i)" + routePath);
	}

	private String parsePath(String path) {
		path = PathUtil.fixPath(path);
		try {
			URI uri = new URI(path);
			return uri.getPath();
		} catch (URISyntaxException e) {
			return null;
		}
	}


	public boolean matchPath(Route route, String path) {
		return matchPath(route.getPath(), path);
	}

	/**
	 * 继续匹配
	 * 
	 * @param uri
	 * @return
	 */
	private boolean matchPath(String path, String uri) {

		// /hello
		if (!path.endsWith("*")
				&& ((uri.endsWith("/") && !path.endsWith("/")) || (path.endsWith("/") && !uri.endsWith("/")))) {
			return false;
		}

		if (path.equals(uri)) {
			return true;
		}

		// 检查参数
		List<String> thisPathList = PathUtil.convertRouteToList(path);
		List<String> uriList = PathUtil.convertRouteToList(uri);

		int thisPathSize = thisPathList.size();
		int uriSize = uriList.size();

		if (thisPathSize == uriSize) {
			for (int i = 0; i < thisPathSize; i++) {
				String thisPathPart = thisPathList.get(i);
				String pathPart = uriList.get(i);

				if ((i == thisPathSize - 1) && (thisPathPart.equals("*") && path.endsWith("*"))) {
					// 通配符匹配
					return true;
				}

				if ((!thisPathPart.startsWith(":")) && !thisPathPart.equals(pathPart) && !thisPathPart.equals("*")) {
					return false;
				}
			}
			// 全部匹配
			return true;
		} else {
			if (path.endsWith("*")) {
				if (uriSize == (thisPathSize - 1) && (path.endsWith("/"))) {
					uriList.add("");
					uriList.add("");
					uriSize += 2;
				}

				if (thisPathSize < uriSize) {
					for (int i = 0; i < thisPathSize; i++) {
						String thisPathPart = thisPathList.get(i);
						String pathPart = uriList.get(i);
						if (thisPathPart.equals("*") && (i == thisPathSize - 1) && path.endsWith("*")) {
							return true;
						}
						if (!thisPathPart.startsWith(":") && !thisPathPart.equals(pathPart)
								&& !thisPathPart.equals("*")) {
							return false;
						}
					}
					return true;
				}
			}
			return false;
		}
	}
}
