package com.ww.game.auto;

import com.ww.game.auto.flow.AutoFlow;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.auto.AutoService;
import lombok.Getter;

@Getter
public class AutoManager {
    private AutoService autoService;
    private Profile profile;

    private AutoFlow flow;

    public AutoManager(AutoService autoService, Profile profile) {
        this.autoService = autoService;
        this.profile = profile;
        this.flow = new AutoFlow(this);
    }

    public void start() {
        flow.run("MANAGE_BOOKS");
    }
}
