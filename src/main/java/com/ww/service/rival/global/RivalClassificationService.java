package com.ww.service.rival.global;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.dto.social.ClassificationProfileDTO;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class RivalClassificationService {
    public static final int CLASSIFICATION_POSITIONS_COUNT = 10;

    @Autowired
    private ProfileService profileService;

    public List<ClassificationProfileDTO> classification(RivalType type) {
        String profileTag = profileService.getProfileTag();
        List<Profile> profiles = profileService.classification(type);
        return IntStream.range(0, profiles.size())
                .mapToObj(value -> new ClassificationProfileDTO(profiles.get(value), type, (long) value + 1))
                .filter(value -> value.getPosition() <= CLASSIFICATION_POSITIONS_COUNT || value.getTag().equals(profileTag))
                .collect(Collectors.toList());
    }
}
