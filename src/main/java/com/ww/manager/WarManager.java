package com.ww.manager;

import com.ww.model.container.rival.RivalInitContainer;
import com.ww.service.rival.RivalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

public class WarManager extends RivalManager {

    public WarManager(RivalInitContainer bic, RivalService rivalService, ProfileConnectionService profileConnectionService) {
        super(bic, rivalService, profileConnectionService);
    }

    protected Message getMessageReadyFast() {
        return Message.WAR_READY_FAST;
    }

    protected Message getMessageContent() {
        return Message.WAR_CONTENT;
    }
}
