package com.ww.model.container;

public interface InsideConnection extends Connection {
    void handleMessage(String msg);
}