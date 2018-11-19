package com.ww.model.container;

public interface Connection {
    String getSessionId();
    Long getProfileId();
    String getProfileTag();

    void close();

    void sendMessage(String msg);
}