package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.rival.Rival;
import com.ww.repository.rival.RivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class GlobalRivalService {
    private final ConcurrentHashMap<Long, RivalManager> profileIdToRivalManagerMap = new ConcurrentHashMap<>();

    @Autowired
    private RivalRepository rivalRepository;

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
        if (!contains(profileConnection.getProfileId())) {
            return;
        }
        RivalManager rivalManager = profileIdToRivalManagerMap.get(profileConnection.getProfileId());
        rivalManager.send(rivalManager.actualModel(profileConnection.getProfileId()), rivalManager.getMessageContent(), profileConnection.getProfileId());
    }

    public void save(Rival rival) {
        rivalRepository.save(rival);
    }
}
