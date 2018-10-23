package com.ww.service.rival.challenge;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeApproach;
import com.ww.model.constant.rival.challenge.ChallengeProfileType;
import com.ww.model.constant.rival.challenge.ChallengeType;
import com.ww.model.constant.social.FriendStatus;
import com.ww.model.constant.social.ResourceType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.challenge.Challenge;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.social.ProfileFriend;
import com.ww.repository.outside.rival.challenge.ChallengePhaseRepository;
import com.ww.repository.outside.rival.challenge.ChallengeProfileRepository;
import com.ww.repository.outside.rival.challenge.ChallengeRepository;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

import static com.ww.helper.ModelHelper.putCode;
import static com.ww.helper.ModelHelper.putErrorCode;
import static com.ww.helper.ModelHelper.putSuccessCode;
import static com.ww.helper.RandomHelper.randomElement;

@Service
@AllArgsConstructor
public class ChallengeCreateService {
    private static final List<Integer> DURATIONS = Arrays.asList(4, 8, 24, 48);
    private static final List<Long> RESOURCE_COSTS = Arrays.asList(0L, 1L, 5L, 10L);

    private final ChallengeRepository challengeRepository;
    private final ChallengeProfileRepository challengeProfileRepository;
    private final RivalChallengeService rivalChallengeService;
    private final ProfileService profileService;

    @Transactional
    public Map<String, Object> createPrivate(List<String> tags, ChallengeAccess access, ChallengeApproach approach, ResourceType resourceType, Long resourceCost, Integer duration) {
        Map<String, Object> model = new HashMap<>();
        if (!DURATIONS.contains(duration) || !RESOURCE_COSTS.contains(resourceCost)) {
            return putErrorCode(model);
        }
        if (tags.isEmpty() && access == ChallengeAccess.INVITE) {
            return putCode(model, -2);
        }
        Profile profile = profileService.getProfile();
        Challenge challenge = new Challenge(profile, ChallengeType.PRIVATE, access, approach, new Resources(resourceType, resourceCost), duration);
        challengeRepository.save(challenge);
        rivalChallengeService.preparePhase(challenge, 0, Category.random(), DifficultyLevel.random());
        createPrivateChampionProfiles(profile, challenge, new HashSet<>(tags));
        return putSuccessCode(model);
    }

    @Transactional
    public Challenge createGlobal() {
        LocalDateTime date = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0));
        Long joinCost = randomElement(Arrays.asList(2L, 4L, 6L, 8L, 10L));
        Challenge challenge = new Challenge(ChallengeType.GLOBAL, ChallengeAccess.UNLOCK, ChallengeApproach.MANY, new Resources(ResourceType.random(), joinCost), date.toInstant(ZoneOffset.UTC));
        challenge.setGainResources(challenge.getGainResources().add(new Resources(joinCost * 2, joinCost * 2, joinCost * 2, joinCost * 2)));
        challengeRepository.save(challenge);
        rivalChallengeService.preparePhase(challenge, 0, Category.random(), DifficultyLevel.random());
        return challenge;
    }

    private void createPrivateChampionProfiles(Profile profile, Challenge challenge, Set<String> tags) {
        challenge.getProfiles().add(new ChallengeProfile(challenge, profile, ChallengeProfileType.CREATOR));
        if (!tags.isEmpty()) {
            List<Profile> friends = profile.getFriends().stream()
                    .filter(profileFriend -> profileFriend.getStatus() == FriendStatus.ACCEPTED)
                    .map(ProfileFriend::getFriendProfile)
                    .filter(e -> tags.contains(e.getTag()))
                    .collect(Collectors.toList());
            if (!friends.isEmpty()) {
                challenge.getProfiles().addAll(friends.stream().map(friend -> new ChallengeProfile(challenge, friend, ChallengeProfileType.INVITED))
                        .collect(Collectors.toSet()));
            }
        }
        challengeProfileRepository.saveAll(challenge.getProfiles());
    }


}
