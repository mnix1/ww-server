package com.ww.service.rival.global;

import com.ww.manager.rival.RivalManager;
import com.ww.model.container.ProfileConnection;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.repository.outside.rival.RivalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RivalGlobalService {
    private final Map<Long, RivalManager> managerMap = new ConcurrentHashMap<>();

    @Autowired
    private RivalRepository rivalRepository;

    public void remove(Long profileId) {
        managerMap.remove(profileId);
    }

    public void put(Long profileId, RivalManager manager) {
        managerMap.put(profileId, manager);
    }

    public RivalManager get(Long profileId) {
        return managerMap.get(profileId);
    }

    public boolean contains(Long profileId) {
        return managerMap.containsKey(profileId);
    }

    public synchronized void sendActualRivalModelToNewProfileConnection(ProfileConnection profileConnection) {
        if (!contains(profileConnection.getProfileId())) {
            return;
        }
        RivalManager manager = managerMap.get(profileConnection.getProfileId());
        manager.send(manager.actualModel(profileConnection.getProfileId()), manager.getMessageContent(), profileConnection.getProfileId());
    }

    public void save(Rival rival) {
        rivalRepository.save(rival);
    }
}
