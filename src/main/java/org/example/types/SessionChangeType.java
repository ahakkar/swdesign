package org.example.types;

/**
 * Enum for supported session change types
 * Used to notify listeners of changes in the session
 * 
 * @see org.example.controller.SessionController.example.model.session.SessionManager
 * @author Antti Hakkarainen
 */
public enum SessionChangeType {
    TAB_ADDED,
    TAB_REMOVED,
    TAB_MOVED,
    TAB_UPDATED,
    CHART_ADDED,
    CHART_REMOVED,
    CHART_MOVED,
    CHART_UPDATED
}