package com.ww.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.game.replay.Replay;
import com.ww.helper.CompressHelper;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.repository.outside.rival.RivalRepository;
import com.ww.service.social.ProfileConnectionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyMap;

@Service
@Getter
@AllArgsConstructor
public class ReplayService {

    private final ProfileConnectionService profileConnectionService;
    private final RivalRepository rivalRepository;

    public void replay(Long rivalId, Long perspectiveProfileId, Long targetProfileId) {
        Optional<Rival> optionalRival = rivalRepository.findById(rivalId);
        if (!optionalRival.isPresent()) {
            return;
        }
        Rival rival = optionalRival.get();
        new Replay(profileConnectionService, prepareModels(rival, perspectiveProfileId), targetProfileId).play();
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
}
