package org.example.controller;

import org.example.model.session.SessionChangeData;

/**
 * Classes which want to listen to SessionController's state changes can
 * implement this class, and register themselves as listeners to SessionController.
 * Then they will receive SessionChangeData objects when SessionController's
 * state changes. Object contains information about what was changed, ie. new
 * tab created, new chart created, or deleted etc.
 * 
 * @author Antti Hakkarainen
 * 
 */
public interface SessionControllerListener {
    void onSessionChange(SessionChangeData data);
}
