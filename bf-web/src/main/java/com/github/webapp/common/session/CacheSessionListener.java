package com.github.webapp.common.session;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * session 缓存监听
 *
 * @author zxy
 */
@WebListener
public class CacheSessionListener implements HttpSessionListener {

    private static final CacheSessionContext CACHE = CacheSessionContext.getInstance();

    @Override
    public void sessionCreated(HttpSessionEvent event) { CACHE.putSession(event.getSession()); }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) { CACHE.removeSession(event.getSession()); }
}
