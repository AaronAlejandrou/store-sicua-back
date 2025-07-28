package com.sicua.application.auth;

import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Simple service to get current store ID from session
 */
@Service
public class SessionService {
    
    private static final Logger logger = LoggerFactory.getLogger(SessionService.class);
    private static final String SESSION_STORE_ID = "storeId";
    
    public String getCurrentStoreId() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(false);
        
        logger.info("Checking session for current store ID");
        logger.info("Session exists: {}", session != null);
        
        if (session == null) {
            logger.error("No active session found");
            throw new IllegalStateException("No active session");
        }
        
        logger.info("Session ID: {}", session.getId());
        String storeId = (String) session.getAttribute(SESSION_STORE_ID);
        logger.info("Store ID from session: {}", storeId);
        
        if (storeId == null) {
            logger.error("User not authenticated - no store ID in session");
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
