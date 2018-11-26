package com.ww.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.game.replay.Replay;
import com.ww.helper.CompressHelper;
import com.ww.model.container.MapModel;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.repository.outside.rival.RivalRepository;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Getter
@AllArgsConstructor
public class ReplayService {
    public static final List<Replay> activeReplays = new CopyOnWriteArrayList<>();

    private final ConnectionService connectionService;
    private final RivalRepository rivalRepository;

    public Map<String, Object> list() {
        return new MapModel("rivals", StringUtils.join(rivalRepository.findAllByModelsJSONCompressedIsNotNull(), ",^_^")).get();
    }

    public Map<String, Object> replay(Long rivalId, Double speed, Long perspectiveProfileId, Long targetProfileId) {
        Optional<Rival> optionalRival = rivalRepository.findById(rivalId);
        if (!optionalRival.isPresent()) {
            return Collections.emptyMap();
        }
        cancel(targetProfileId);
        Rival rival = optionalRival.get();
        if (perspectiveProfileId == null) {
            perspectiveProfileId = rival.getCreator().getId();
        }
        Replay replay = new Replay(this, prepareModels(rival, perspectiveProfileId), targetProfileId);
        activeReplays.add(replay);
        replay.play(speed);
        return new MapModel("manager", CompressHelper.decompress(rival.getManagerStringCompressed())).get();
    }

    private List prepareModels(Rival rival, Long profileId) {
        try {
            Map<String, List<String>> map = new ObjectMapper().readValue(CompressHelper.decompress(rival.getModelsJSONCompressed()), HashMap.class);
            return map.get(profileId.toString()).stream().map(s -> {
                try {
                    return new ObjectMapper().readValue(s, HashMap.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void cancel(Long profileId) {
        findReplayFromProfileId(profileId).ifPresent(this::disposeReplay);
    }

    public void disposeReplay(Replay replay) {
        replay.stop();
        activeReplays.remove(replay);
    }

    public Optional<Replay> findReplayFromProfileId(Long profileId) {
        return activeReplays.stream().filter(replay -> replay.getProfileId().equals(profileId)).findFirst();
    }
}
