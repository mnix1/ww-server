package com.ww.game.auto;

import com.ww.model.entity.outside.social.Profile;
import com.ww.service.auto.AutoService;

public class AutoManager {
    private AutoService autoService;
    private Profile profile;

    public AutoManager(AutoService autoService, Profile profile) {
        this.autoService = autoService;
        this.profile = profile;
    }

    public void start() {

    }
}
