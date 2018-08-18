package com.ww.model.constant.rival.task;

public enum TrackSource {
    TEKSTOWO("www.tekstowo.pl"),
    ISING("ising.pl");
//    GROOVE("www.groove.pl");

    String url;

    TrackSource(String url) {
        this.url = url;
    }

    public static TrackSource fromUrl(String url) {
        for (TrackSource source : values()) {
            if (url.contains(source.url)) {
                return source;
            }
        }
        return null;
    }
}
