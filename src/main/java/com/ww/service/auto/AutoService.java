package com.ww.service.auto;

import com.ww.game.auto.AutoManager;
import com.ww.game.play.PlayManager;
import com.ww.model.entity.inside.social.InsideProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.inside.social.InsideProfileRepository;
import com.ww.service.auto.command.AutoManageBooksService;
import com.ww.service.auto.command.AutoManageMailService;
import com.ww.service.auto.command.AutoStartRivalService;
import com.ww.service.auto.command.AutoUpgradeWisiesService;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class AutoService {
    private static Logger logger = LoggerFactory.getLogger(AutoService.class);
    public static int MAX_ACTIVE_AUTO_MANAGERS = 0;
    public static final Map<Long, AutoManager> activeAutoManagersMap = new ConcurrentHashMap<>();

    private final ProfileService profileService;
    private final AutoProfileService autoProfileService;
    private final AutoManageBooksService autoManageBooksService;
    private final AutoManageMailService autoManageMailService;
    private final AutoUpgradeWisiesService autoUpgradeWisiesService;
    private final AutoStartRivalService autoStartRivalService;
    private final RivalGlobalService rivalGlobalService;
    private final ConnectionService connectionService;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;
    private final InsideProfileRepository insideProfileRepository;

    public Optional<AutoManager> createAutoManager() {
        Profile profile = autoProfileService.getNotLoggedAutoProfile();
        InsideProfile insideProfile = insideProfileRepository.findFirstByUsername(profile.getName()).orElse(new InsideProfile().initStats());
        AutoManager manager = new AutoManager(profile, insideProfile, this);
        return Optional.of(manager);
    }

    public void perform() {
        if (needAutoRival()) {
            startManager();
            return;
        }
        if (activeAutoManagersMap.size() >= MAX_ACTIVE_AUTO_MANAGERS) {
//            logger.debug("Already have max active auto managers: {}", activeAutoManagersMap.size());
            return;
        }
        startManager();
    }

    private boolean needAutoRival() {
        return rivalInitRandomOpponentService.maybeGetRivalInitWaitingLong().isPresent();
    }

    private void startManager() {
        Optional<AutoManager> optionalManager = createAutoManager();
        if (!optionalManager.isPresent()) {
            return;
        }
        AutoManager manager = optionalManager.get();
        activeAutoManagersMap.put(manager.getProfile().getId(), manager);
        manager.getFlow().start();
    }

    public PlayManager getPlayManager(Profile profile) {
        return rivalGlobalService.get(profile.getId());
    }

    public void disposeManager(AutoManager manager) {
        logger.debug("Disposed auto profile=" + manager.getProfile().toString());
        manager.disposeContainer();
        connectionService.deleteConnection(manager.getConnection());
        activeAutoManagersMap.remove(manager.getProfile().getId());
    }

    public void manageBooks(Profile profile) {
        autoManageBooksService.manage(profile);
    }

    public void manageMails(Profile profile) {
        autoManageMailService.manage(profile);
    }

    public void upgradeWisies(Profile profile) {
        autoUpgradeWisiesService.upgrade(profile);
    }

    public void startRival(AutoManager manager) {
        manager.setProfile(profileService.getProfile(manager.getProfile().getId()));
        boolean result = autoStartRivalService.start(manager);
        if (!result) {
            disposeManager(manager);
        }
    }
}
