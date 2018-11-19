package com.ww.service.auto;

import com.ww.game.auto.AutoManager;
import com.ww.model.container.AutoProfileConnection;
import com.ww.model.container.Connection;
import com.ww.model.container.rival.init.RivalOneInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@AllArgsConstructor
public class AutoService {
    private static final List<AutoManager> activeAutoManagers = new CopyOnWriteArrayList<>();

    private final AutoProfileService autoProfileService;
    private final ConnectionService connectionService;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;

    public Optional<AutoManager> createAutoManager() {
        Optional<Profile> optionalProfile = autoProfileService.getNotLoggedAutoProfile();
        if (!optionalProfile.isPresent()) {
            return Optional.empty();
        }
        Profile profile = optionalProfile.get();
        AutoManager manager = new AutoManager(this, profile);
        return Optional.of(manager);
    }

    public void perform() {
        Optional<AutoManager> optionalManager = createAutoManager();
        if (!optionalManager.isPresent()) {
            return;
        }
        AutoManager manager = optionalManager.get();
        activeAutoManagers.add(manager);
        manager.start();
    }

    public AutoProfileConnection addAutoProfileConnection(Profile profile) {
        return connectionService.newAutoProfileConnection(profile);
    }

    public void removeAutoProfileConnection(Connection connection) {
        connectionService.deleteConnection(connection);
    }

    public Optional<RivalOneInit> maybeNeedStartRival() {
        return rivalInitRandomOpponentService.maybeGetRivalInitWaitingLong();
    }

}
