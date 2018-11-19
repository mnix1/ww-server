package com.ww.model.container;

import com.ww.helper.TagHelper;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class AutoProfileConnection implements Connection {
    private static final Logger logger = LoggerFactory.getLogger(AutoProfileConnection.class);

    private Long profileId;
    private String profileTag;
    private String sessionId;

    public AutoProfileConnection(Long profileId, String profileTag) {
        this.profileId = profileId;
        this.profileTag = profileTag;
        this.sessionId = TagHelper.randomUUID();
    }

    public void close() {
    }

    public void sendMessage(String msg) {
    }

    @Override
    public boolean equals(Object obj) {
        return getProfileId().equals(((Connection) obj).getProfileId());
    }

}