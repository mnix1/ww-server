package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.ProfileConnection;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class GlobalRivalService {
    private final ConcurrentHashMap<Long, RivalManager> profileIdToRivalManagerMap = new ConcurrentHashMap<>();

    public void remove(Long profileId) {
        profileIdToRivalManagerMap.remove(profileId);
    }

    public void put(Long profileId, RivalManager rivalManager) {
        profileIdToRivalManagerMap.put(profileId, rivalManager);
    }

    public RivalManager get(Long profileId) {
        return profileIdToRivalManagerMap.get(profileId);
    }

    public boolean contains(Long profileId) {
        return profileIdToRivalManagerMap.containsKey(profileId);
    }

    public synchronized void sendActualRivalModelToNewProfileConnection(ProfileConnection profileConnection) {
        if (!profileIdToRivalManagerMap.containsKey(profileConnection.getProfileId())) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileConnection.getProfileId());
        rivalManager.send(rivalManager.actualModel(profileConnection.getProfileId()), rivalManager.getMessageContent(), profileConnection.getProfileId());
    }
}
