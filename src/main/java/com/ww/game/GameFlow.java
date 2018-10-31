package com.ww.game;

import io.reactivex.disposables.Disposable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameFlow {
    protected Map<String, Disposable> disposableMap = new ConcurrentHashMap<>();
}
