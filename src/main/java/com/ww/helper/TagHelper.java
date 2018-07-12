package com.ww.helper;

import java.util.UUID;

public class TagHelper {
    static public String randomTag(){
        return UUID.randomUUID().toString().substring(0, 6);
    }
}
