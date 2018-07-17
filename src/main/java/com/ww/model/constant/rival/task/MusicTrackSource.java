package com.ww.model.constant.rival.task;

public enum MusicTrackSource {
    TEKSTOWO("www.tekstowo.pl");

    String url;

    MusicTrackSource(String url) {
        this.url = url;
    }

    public static MusicTrackSource fromUrl(String url) {
        for (MusicTrackSource source : values()) {
            if (url.contains(source.url)) {
                return source;
            }
        }
        return null;
    }
}
