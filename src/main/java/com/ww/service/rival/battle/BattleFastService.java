package com.ww.service.rival.battle;

import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.RivalManager;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.service.SessionService;
import com.ww.service.rival.RivalFastService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BattleFastService extends RivalFastService {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private BattleService battleService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Override
    protected BattleService getRivalService() {
        return battleService;
    }

    @Override
    protected SessionService getSessionService() {
        return sessionService;
    }

    @Override
    protected ProfileService getProfileService() {
        return profileService;
    }

    @Override
    protected ProfileConnectionService getProfileConnectionService() {
        return profileConnectionService;
    }

    @Override
    protected RivalManager createManager(RivalInitContainer rival) {
        return new BattleManager(rival, battleService, profileConnectionService);
    }
}
