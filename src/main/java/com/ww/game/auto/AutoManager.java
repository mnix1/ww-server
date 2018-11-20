package com.ww.game.auto;

import com.ww.game.auto.communication.AutoCommunication;
import com.ww.game.auto.container.AutoPlayContainer;
import com.ww.game.auto.flow.AutoFlow;
import com.ww.game.play.PlayManager;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.auto.AutoService;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class AutoManager {
    public static Logger logger = LoggerFactory.getLogger(AutoManager.class);
    private AutoService autoService;
    private Profile profile;
    @Setter
    private AutoProfileConnection connection;
    private AutoFlow flow;
    private AutoCommunication communication;
    @Setter
    private AutoPlayContainer autoPlayContainer;

    public AutoManager(AutoService autoService, Profile profile) {
        this.autoService = autoService;
        this.profile = profile;
        this.flow = new AutoFlow(this);
        this.communication = new AutoCommunication(this);
    }

    public void start() {
        flow.run("MANAGE_BOOKS");
    }

}
