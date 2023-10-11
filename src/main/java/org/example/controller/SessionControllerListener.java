package org.example.controller;

import org.example.model.session.SessionChangeData;

public interface SessionControllerListener {
    void onSessionChange(SessionChangeData data);
}
