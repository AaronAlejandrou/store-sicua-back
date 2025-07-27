package com.sicua.infrastructure.common.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * Service for managing user session data
 */
@Service
public class SessionService {
    
    private static final String STORE_ID_KEY = "storeId";
    
    /**
     * Get the current store ID from the session
     * @return the current store ID
     * @throws IllegalStateException if no store ID is found in session
     */
    public String getCurrentStoreId() {
        HttpSession session = getCurrentSession();
        String storeId = (String) session.getAttribute(STORE_ID_KEY);
        
        if (storeId == null) {
            throw new IllegalStateException("No store ID found in session. User must be authenticated.");
        }
        
        return storeId;
    }
    
    /**
     * Set the current store ID in the session
     * @param storeId the store ID to set
     */
    public void setCurrentStoreId(String storeId) {
        HttpSession session = getCurrentSession();
        session.setAttribute(STORE_ID_KEY, storeId);
    }
    
    /**
     * Check if there is a current store ID in the session
     * @return true if authenticated, false otherwise
     */
    public boolean isAuthenticated() {
        try {
            HttpSession session = getCurrentSession();
            return session.getAttribute(STORE_ID_KEY) != null;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Clear the session
     */
    public void clearSession() {
        HttpSession session = getCurrentSession();
        session.invalidate();
    }
    
    private HttpSession getCurrentSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession();
    }
}
