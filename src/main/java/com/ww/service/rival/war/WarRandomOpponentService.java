package com.ww.service.rival.war;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.RivalRandomOpponentService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WarRandomOpponentService extends RivalRandomOpponentService {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private WarService warService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Override
    protected WarService getRivalService() {
        return warService;
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
        return new WarManager(rival, warService, profileConnectionService);
    }

    protected RivalType getRivalType() {
        return RivalType.WAR;
    }

    protected boolean checkIfCanPlay(Profile profile) {
        return profile.getTeamInitialized();
    }

}
