package com.sicua.application.auth;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Simple service to get current store ID from session
 */
@Service
public class SessionService {
    
    private static final String SESSION_STORE_ID = "storeId";
    
    public String getCurrentStoreId() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        
        if (session == null) {
            throw new IllegalStateException("No active session");
        }
        
        String storeId = (String) session.getAttribute(SESSION_STORE_ID);
        if (storeId == null) {
            throw new IllegalStateException("User not authenticated");
        }
        
        return storeId;
    }
    
    public boolean isAuthenticated() {
        try {
            getCurrentStoreId();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
