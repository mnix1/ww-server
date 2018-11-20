package com.ww.service.auto;

import com.ww.game.auto.AutoManager;
import com.ww.game.play.PlayManager;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.auto.command.AutoManageBooksService;
import com.ww.service.auto.command.AutoStartRivalService;
import com.ww.service.auto.command.AutoUpgradeWisiesService;
import com.ww.service.rival.global.RivalGlobalService;
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
    private final AutoManageBooksService autoManageBooksService;
    private final AutoUpgradeWisiesService autoUpgradeWisiesService;
    private final AutoStartRivalService autoStartRivalService;
    private final RivalGlobalService rivalGlobalService;

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

    public PlayManager getPlayManager(Profile profile){
        return rivalGlobalService.get(profile.getId());
    }

//    public void removeAutoProfileConnection(Connection connection) {
//        connectionService.deleteConnection(connection);
//    }

    public void manageBooks(Profile profile){
        autoManageBooksService.manage(profile);
    }

    public void upgradeWisies(Profile profile){
        autoUpgradeWisiesService.upgrade(profile);
    }

    public void startRival(AutoManager manager){
        autoStartRivalService.start(manager);
    }

}
