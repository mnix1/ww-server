package com.ww.service.rival.global;

import com.ww.model.container.ProfileConnection;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.play.PlayManager;
import com.ww.repository.outside.rival.RivalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class RivalGlobalService {
    private static final Map<Long, PlayManager> managerMap = new ConcurrentHashMap<>();

    private final RivalRepository rivalRepository;

    public void remove(Long profileId) {
        managerMap.remove(profileId);
    }

    public void put(Long profileId, PlayManager manager) {
        managerMap.put(profileId, manager);
    }

    public PlayManager get(Long profileId) {
        return managerMap.get(profileId);
    }

    public boolean contains(Long profileId) {
        return managerMap.containsKey(profileId);
    }

    public synchronized void sendActualRivalModelToNewProfileConnection(ProfileConnection profileConnection) {
        if (!contains(profileConnection.getProfileId())) {
            return;
        }
        PlayManager manager = managerMap.get(profileConnection.getProfileId());
        manager.sendModelFromBeginning(profileConnection.getProfileId());
    }

    @Transactional
    public void store(RivalModel rivalModel) {
        Rival rival = new Rival(rivalModel.getType(), rivalModel.getImportance(), rivalModel.getCreatorProfile(), rivalModel.getOpponentProfile(), rivalModel.getDraw(), rivalModel.getWinner());
        save(rival);
    }

    public void save(Rival rival) {
        rivalRepository.save(rival);
    }
}
