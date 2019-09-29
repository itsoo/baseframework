package com.github.webapp.common.session;

import javax.servlet.http.HttpSession;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * session 缓存上下文
 *
 * @author zxy
 */
public class CacheSessionContext {

    private static final CacheSessionContext INSTANCE = new CacheSessionContext();
    private static final ConcurrentMap<String, HttpSession> CACHE = new ConcurrentHashMap<>();

    private CacheSessionContext() {}

    public static CacheSessionContext getInstance() { return INSTANCE; }

    void putSession(HttpSession session) {
        if (session != null) {
            CACHE.put(session.getId(), session);
        }
    }

    void removeSession(HttpSession session) {
        if (session != null) {
            CACHE.remove(session.getId());
        }
    }

    public HttpSession getSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }

        return CACHE.get(sessionId);
    }
}
